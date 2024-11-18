package io.qase.kaspresso.interceptors

import io.qase.kaspresso.files.attachScreenshotToReport
import com.kaspersky.kaspresso.device.screenshots.Screenshots
import com.kaspersky.kaspresso.interceptors.watcher.testcase.StepWatcherInterceptor
import com.kaspersky.kaspresso.testcases.models.info.StepInfo

class ScreenshotStepInterceptor(
    private val screenshots: Screenshots
) : StepWatcherInterceptor {

    override fun interceptAfterWithSuccess(stepInfo: StepInfo) {
        intercept(makeTag(stepInfo))
    }

    override fun interceptAfterWithError(stepInfo: StepInfo, error: Throwable) {
        intercept("${makeTag(stepInfo)}_failure_${error.javaClass.simpleName}")
    }

    private fun intercept(tag: String) {
        screenshots.takeAndApply(tag) { attachScreenshotToReport() }
    }

    private fun makeTag(stepInfo: StepInfo): String = "${stepInfo.testClassName}_step_${stepInfo.ordinal}"
}
