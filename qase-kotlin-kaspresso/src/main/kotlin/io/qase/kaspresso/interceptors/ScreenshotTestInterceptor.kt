package io.qase.kaspresso.interceptors

import com.kaspersky.kaspresso.interceptors.watcher.testcase.TestRunWatcherInterceptor
import com.kaspersky.kaspresso.testcases.models.info.TestInfo
import io.qase.kaspresso.files.attachScreenshotToReport
import com.kaspersky.kaspresso.device.screenshots.Screenshots

class ScreenshotTestInterceptor(
    private val screenshots: Screenshots
) : TestRunWatcherInterceptor {

    override fun onBeforeSectionFinishedFailed(testInfo: TestInfo, throwable: Throwable) {
        onSectionFailed(makeTag("BeforeTestSection", throwable))
    }

    override fun onAfterSectionFinishedFailed(testInfo: TestInfo, throwable: Throwable) {
        onSectionFailed(makeTag("AfterTestSection", throwable))
    }

    private fun onSectionFailed(tag: String) {
        screenshots.takeAndApply(tag) { attachScreenshotToReport() }
    }

    private fun makeTag(section: String, throwable: Throwable): String =
        "Screenshot_${section}_failure_${throwable.javaClass.simpleName}"
}
