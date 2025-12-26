package io.qase.commons.kotlin.android.listeners

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.Description
import org.junit.runner.notification.RunListener

/**
 * Listener that automatically grants external storage permissions
 * required for test results to be saved using TestStorage.
 *
 * This is necessary for Android 10+ devices where scoped storage is enforced.
 */
class ExternalStoragePermissionsListener : RunListener() {

    override fun testRunStarted(description: Description?) {
        InstrumentationRegistry.getInstrumentation().uiAutomation.apply {
            val testServicesPackage = "androidx.test.services"
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    executeShellCommand("appops set $testServicesPackage MANAGE_EXTERNAL_STORAGE allow")
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    executeShellCommand("pm grant $testServicesPackage android.permission.READ_EXTERNAL_STORAGE")
                    executeShellCommand("pm grant $testServicesPackage android.permission.WRITE_EXTERNAL_STORAGE")
                }
            }
        }
    }
}

