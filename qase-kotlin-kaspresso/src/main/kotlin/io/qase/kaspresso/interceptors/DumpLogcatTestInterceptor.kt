package io.qase.kaspresso.interceptors

import com.kaspersky.kaspresso.interceptors.watcher.testcase.TestRunWatcherInterceptor
import com.kaspersky.kaspresso.testcases.models.info.TestInfo
import io.qase.kaspresso.files.attachLogcatToReport
import com.kaspersky.kaspresso.device.logcat.dumper.LogcatDumper

class DumpLogcatTestInterceptor(
    private val logcatDumper: LogcatDumper
) : TestRunWatcherInterceptor {

    override fun onTestStarted(testInfo: TestInfo) {
        logcatDumper.charge()
    }

    override fun onTestFinished(testInfo: TestInfo, success: Boolean) {
        logcatDumper.dumpAndApply("TestLogcat") { attachLogcatToReport() }
    }
}
