package io.qase.commons.kotlin.junit4

/**
 * Annotation to mark a test case as ignored.
 * The test will execute, but the results will not be reported to Qase.
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class QaseIgnore

