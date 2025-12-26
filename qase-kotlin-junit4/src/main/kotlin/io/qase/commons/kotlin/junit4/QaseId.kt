package io.qase.commons.kotlin.junit4

/**
 * Annotation to set a single Qase test case ID.
 *
 * @param value The test case ID
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class QaseId(val value: Long)

