package io.qase.kaspresso.interceptors

import com.kaspersky.kaspresso.interceptors.watcher.testcase.StepWatcherInterceptor
import com.kaspersky.kaspresso.testcases.models.info.StepInfo
import io.qase.commons.kotlin.models.StepResultStatus
import io.qase.commons.kotlin.storage.StepStorage

class StepInterceptor : StepWatcherInterceptor {

    override fun interceptBefore(stepInfo: StepInfo) {
        StepStorage.startStep()
    }

    override fun interceptAfterWithSuccess(stepInfo: StepInfo) {
        val current = StepStorage.getCurrentStep()

        current.data.action = stepInfo.description
        current.execution.status = StepResultStatus.PASSED
    }

    override fun interceptAfterWithError(stepInfo: StepInfo, error: Throwable) {
        val current = StepStorage.getCurrentStep()

        current.data.action = stepInfo.description
        current.execution.status = StepResultStatus.FAILED
    }

    override fun interceptAfterFinally(stepInfo: StepInfo) {
        StepStorage.stopStep()
    }

}
