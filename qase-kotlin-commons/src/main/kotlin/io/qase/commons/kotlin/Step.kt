package io.qase.commons.kotlin

/**
 * Annotation to mark step methods.
 * Methods annotated with @Step will be tracked as separate steps in test results.
 *
 * @param value Optional step name. If not provided, method name will be used.
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Step(val value: String = "")

