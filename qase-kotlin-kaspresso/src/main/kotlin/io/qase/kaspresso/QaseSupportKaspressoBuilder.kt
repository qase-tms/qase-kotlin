package io.qase.kaspresso

import io.qase.kaspresso.interceptors.DumpViewsTestInterceptor
import io.qase.kaspresso.interceptors.DumpLogcatTestInterceptor
import io.qase.kaspresso.interceptors.ScreenshotStepInterceptor
import io.qase.kaspresso.interceptors.ScreenshotTestInterceptor
import io.qase.kaspresso.interceptors.StepInterceptor
import com.kaspersky.kaspresso.instrumental.InstrumentalDependencyProvider
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.runner.listener.addUniqueListener


fun Kaspresso.Builder.Companion.withQaseSupport(
    customize: Kaspresso.Builder.() -> Unit = {}
): Kaspresso.Builder = simple {
    if (!isAndroidRuntime) {
        return@simple
    }
    customize.invoke(this)
    val instrumentalDependencyProvider =
        instrumentalDependencyProviderFactory.getComponentProvider<Kaspresso>(instrumentation)
    addRunListenersIfNeeded(instrumentalDependencyProvider)
}.apply {
    postInit(builder = this)
}

private fun Kaspresso.Builder.addRunListenersIfNeeded(provider: InstrumentalDependencyProvider) {
    provider.runNotifier.apply {
        addUniqueListener(::QaseRunListener)
    }
}

private fun postInit(builder: Kaspresso.Builder): Unit = with(builder) {
    if (!isAndroidRuntime) {
        return@with
    }

    stepWatcherInterceptors.addAll(
        listOf(
            StepInterceptor(),
            ScreenshotStepInterceptor(screenshots)
        )
    )
    testRunWatcherInterceptors.addAll(
        listOf(
            DumpLogcatTestInterceptor(logcatDumper),
            ScreenshotTestInterceptor(screenshots),
            DumpViewsTestInterceptor(viewHierarchyDumper),
        )
    )
}
