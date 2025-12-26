package io.qase.commons.kotlin.junit4

import io.qase.commons.kotlin.listener.QaseJunit4Listener
import io.qase.commons.kotlin.writer.FileWriter
import org.junit.runner.notification.RunNotifier
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.Parameterized

/**
 * Custom JUnit4 runner that automatically attaches QaseJunit4Listener.
 *
 * Usage:
 * ```
 * @RunWith(QaseRunner::class)
 * class MyTest {
 *     @Test
 *     fun testExample() {
 *         // Test code
 *     }
 * }
 * ```
 */
class QaseRunner(clazz: Class<*>) : BlockJUnit4ClassRunner(clazz) {

    override fun run(notifier: RunNotifier) {
        notifier.addListener(QaseJunit4Listener(FileWriter("qase-results")))
        super.run(notifier)
    }
}

/**
 * Custom Parameterized runner that automatically attaches QaseJunit4Listener.
 *
 * Usage:
 * ```
 * @RunWith(QaseParametrizedRunner::class)
 * class MyParametrizedTest {
 *     @Parameterized.Parameters
 *     fun data(): Collection<Array<Any>> {
 *         return listOf(...)
 *     }
 * }
 * ```
 */
class QaseParametrizedRunner(clazz: Class<*>) : Parameterized(clazz) {

    override fun run(notifier: RunNotifier) {
        notifier.addListener(QaseJunit4Listener(FileWriter("qase-results")))
        super.run(notifier)
    }
}

