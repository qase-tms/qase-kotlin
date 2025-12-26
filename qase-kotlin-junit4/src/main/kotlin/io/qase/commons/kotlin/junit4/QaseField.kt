package io.qase.commons.kotlin.junit4

/**
 * Annotation to add a single custom field to the test case.
 * Use multiple @QaseField annotations to add multiple fields.
 *
 * @param key The field key
 * @param value The field value
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class QaseField(val key: String, val value: String)

