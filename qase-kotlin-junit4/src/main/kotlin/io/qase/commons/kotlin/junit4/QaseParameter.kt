package io.qase.commons.kotlin.junit4

/**
 * Annotation to add a single parameter to the test case.
 * Use multiple @QaseParameter annotations to add multiple parameters.
 *
 * @param key The parameter key
 * @param value The parameter value
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class QaseParameter(val key: String, val value: String)

