package io.qase.commons.kotlin.android.internal

import android.annotation.SuppressLint
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import java.io.File

/**
 * Utility functions for Android test environment detection and file operations.
 */

/**
 * Determines if tests are running on a real Android device or emulator.
 * Returns false for Robolectric tests.
 *
 * This is the same logic as present in [androidx.test.ext.junit.runners.AndroidJUnit4]
 * for resolving class runner.
 */
@SuppressLint("DefaultLocale")
internal fun isDeviceTest(): Boolean {
    // Check multiple ways to detect Android runtime
    val runtimeName = System.getProperty("java.runtime.name")?.lowercase() ?: ""
    val vmName = System.getProperty("java.vm.name")?.lowercase() ?: ""
    
    if (runtimeName.contains("android") || vmName.contains("android")) {
        return true
    }
    
    // Try to access Android-specific classes at runtime
    return try {
        Class.forName("android.app.Activity")
        true
    } catch (e: ClassNotFoundException) {
        false
    } catch (e: Exception) {
        // If any other exception occurs, assume it's not Android device
        false
    }
}

/**
 * Retrieves [UiDevice] if it's available, otherwise null is returned.
 * In Robolectric tests [UiDevice] is inaccessible and this property serves as a safe way of accessing it.
 */
internal val uiDevice: UiDevice?
    get() = runCatching { UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()) }
        .onFailure { Log.e("UiDevice", "UiDevice unavailable") }
        .getOrNull()

/**
 * Creates a temporary file in the test context cache directory.
 */
internal fun createTemporaryFile(prefix: String = "temp", suffix: String? = null): File {
    return try {
        val cacheDir = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir
        createTempFile(prefix, suffix, cacheDir)
    } catch (e: Exception) {
        // Fallback for non-Android environment
        createTempFile(prefix, suffix)
    }
}


