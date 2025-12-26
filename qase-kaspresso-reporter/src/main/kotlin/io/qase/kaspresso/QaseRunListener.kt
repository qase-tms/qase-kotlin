package io.qase.kaspresso

import com.kaspersky.kaspresso.runner.listener.KaspressoLateRunListener
import io.qase.commons.kotlin.listener.QaseJunit4Listener
import io.qase.commons.kotlin.android.writer.TestStorageResultsWriter
import io.qase.commons.kotlin.listener.Qase
import org.junit.runner.Description
import org.junit.runner.notification.Failure

class QaseRunListener : KaspressoLateRunListener {
    init {
        Qase.listener = QaseJunit4Listener(TestStorageResultsWriter())
    }

    override fun testStarted(description: Description) {
        Qase.listener?.testStarted(description)
    }

    override fun testFinished(description: Description) {
        Qase.listener?.testFinished(description)
    }

    override fun testFailure(failure: Failure) {
        Qase.listener?.testFailure(failure)
    }

    override fun testAssumptionFailure(failure: Failure) {
        Qase.listener?.testAssumptionFailure(failure)
    }

    override fun testIgnored(description: Description) {
        Qase.listener?.testIgnored(description)
    }
}
