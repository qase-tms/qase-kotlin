package io.qase.commons.kotlin.junit4

/**
 * Annotation to set the test case title.
 *
 * @param value The test case title
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class QaseTitle(val value: String)

