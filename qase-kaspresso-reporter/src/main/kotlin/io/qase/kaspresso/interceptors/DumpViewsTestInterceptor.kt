package io.qase.kaspresso.interceptors

import com.kaspersky.kaspresso.interceptors.watcher.testcase.TestRunWatcherInterceptor
import com.kaspersky.kaspresso.testcases.models.info.TestInfo
import io.qase.kaspresso.files.attachViewHierarchyToReport
import com.kaspersky.kaspresso.device.viewhierarchy.ViewHierarchyDumper

class DumpViewsTestInterceptor(
    private val viewHierarchyDumper: ViewHierarchyDumper
) : TestRunWatcherInterceptor {

    override fun onBeforeSectionFinishedFailed(testInfo: TestInfo, throwable: Throwable) {
        onSectionFailed(makeTag("BeforeTestSection", throwable))
    }

    override fun onMainSectionFinishedFailed(testInfo: TestInfo, throwable: Throwable) {
        onSectionFailed(makeTag("MainTestSection", throwable))
    }

    override fun onAfterSectionFinishedFailed(testInfo: TestInfo, throwable: Throwable) {
        onSectionFailed(makeTag("AfterTestSection", throwable))
    }

    private fun onSectionFailed(tag: String) {
        viewHierarchyDumper.dumpAndApply(tag) { attachViewHierarchyToReport() }
    }

    private fun makeTag(section: String, throwable: Throwable): String =
        "ViewHierarchy_${section}_failure_${throwable.javaClass.simpleName}"
}
