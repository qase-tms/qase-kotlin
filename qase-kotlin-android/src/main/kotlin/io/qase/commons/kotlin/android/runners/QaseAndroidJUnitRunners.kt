package io.qase.commons.kotlin.android.runners

import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.AndroidJUnitRunner
import io.qase.commons.kotlin.android.listeners.ExternalStoragePermissionsListener
import io.qase.commons.kotlin.android.listeners.QaseAndroidRunListener
import io.qase.commons.kotlin.android.writer.TestStorageResultsWriter
import io.qase.commons.kotlin.listener.Qase
import io.qase.commons.kotlin.listener.QaseJunit4Listener
import org.junit.runner.Description
import org.junit.runner.Runner
import org.junit.runner.manipulation.Filter
import org.junit.runner.manipulation.Sorter
import org.junit.runner.notification.RunListener
import org.junit.runner.notification.RunNotifier

private val listenerInitialized = ThreadLocal<Boolean>()

// Static flag to track if QaseAndroidJUnitRunner has already registered QaseAndroidRunListener
private var qaseRunnerListenerRegistered = false

private fun isListenerInitialized(): Boolean {
    return listenerInitialized.get() == true
}

private fun markListenerInitialized() {
    listenerInitialized.set(true)
}

/**
 * Wrapper over [AndroidJUnit4] that attaches the [QaseJunit4Listener] listener.
 *
 * Usage:
 * ```
 * @RunWith(QaseAndroidJUnit4::class)
 * class MyTest {
 *     @Test
 *     fun testExample() {
 *         // Test code
 *     }
 * }
 * ```
 */
open class QaseAndroidJUnit4(clazz: Class<*>) : Runner() {

    private val delegate = AndroidJUnit4(clazz)

    override fun run(notifier: RunNotifier?) {
        // If QaseAndroidJUnitRunner has already registered QaseAndroidRunListener,
        // don't add another listener to avoid duplication
        // Also check if we haven't already added one in this thread
        if (!qaseRunnerListenerRegistered && !isListenerInitialized()) {
            val listener = QaseJunit4Listener(TestStorageResultsWriter()).also {
                // Set Qase.listener so that Qase helper methods work
                Qase.listener = it
            }
            notifier?.addListener(listener)
            markListenerInitialized()
        }
        delegate.run(notifier)
    }

    override fun getDescription(): Description = delegate.description
}

/**
 * Custom [AndroidJUnitRunner] that attaches [QaseJunit4Listener] with [TestStorageResultsWriter].
 * It also automatically grants the external storage permission (required for the test results to be saved).
 *
 * Usage in build.gradle:
 * ```
 * android {
 *     defaultConfig {
 *         testInstrumentationRunner "io.qase.commons.kotlin.android.runners.QaseAndroidJUnitRunner"
 *     }
 * }
 * ```
 */
open class QaseAndroidJUnitRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle) {
        // Mark that QaseAndroidJUnitRunner is registering the listener
        qaseRunnerListenerRegistered = true
        
        // Register QaseAndroidRunListener which uses TestStorageResultsWriter internally
        val existingListeners = arguments.getCharSequence("listener")?.toString() ?: ""
        val listenerArg = listOfNotNull(
            existingListeners.takeIf { it.isNotEmpty() },
            QaseAndroidRunListener::class.java.name,
            ExternalStoragePermissionsListener::class.java.name
        ).joinToString(separator = ",")
        arguments.putCharSequence("listener", listenerArg)
        super.onCreate(arguments)
    }
}

/**
 * [QaseAndroidJUnitRunner] that additionally patches the instrumentation context using [MultiDex].
 *
 * Use this runner if your app uses MultiDex.
 */
open class MultiDexQaseAndroidJUnitRunner : QaseAndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle) {
        MultiDex.installInstrumentation(context, targetContext)
        super.onCreate(arguments)
    }
}


