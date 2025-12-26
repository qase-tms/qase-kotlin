package io.qase.commons.kotlin

/**
 * Annotation that allows to attach a description for a test or for a step.
 *
 * @param value Description text as String.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CLASS
)
annotation class Description(
    /**
     * Simple description text as String.
     *
     * @return Description text.
     */
    val value: String = ""
)

