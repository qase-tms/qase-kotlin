package io.qase.commons.kotlin.junit4

/**
 * Annotation to set multiple Qase test case IDs.
 *
 * @param value Array of test case IDs
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class QaseIds(vararg val value: Long)

