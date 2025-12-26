package io.qase.commons.kotlin.android.listeners

import io.qase.commons.kotlin.android.writer.TestStorageResultsWriter
import io.qase.commons.kotlin.listener.Qase
import io.qase.commons.kotlin.listener.QaseJunit4Listener
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunListener

/**
 * Android-specific RunListener that wraps QaseJunit4Listener with TestStorageResultsWriter.
 * This listener is registered via AndroidJUnitRunner arguments and ensures TestStorageResultsWriter is used.
 */
class QaseAndroidRunListener : RunListener() {
    
    private val delegate: QaseJunit4Listener = QaseJunit4Listener(TestStorageResultsWriter()).also {
        // Set Qase.listener so that Qase helper methods work
        Qase.listener = it
    }

    override fun testStarted(description: Description) {
        delegate.testStarted(description)
    }

    override fun testFinished(description: Description) {
        delegate.testFinished(description)
    }

    override fun testFailure(failure: Failure) {
        delegate.testFailure(failure)
    }

    override fun testAssumptionFailure(failure: Failure) {
        delegate.testAssumptionFailure(failure)
    }

    override fun testIgnored(description: Description) {
        delegate.testIgnored(description)
    }
}

