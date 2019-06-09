package me.bristermitten.reflector.property.info;

import java.lang.annotation.Annotation;

/**
 * Information about an {@link Informational}
 */
public interface Info {
    /**
     * @param annotation the annotation
     * @return if the {@link Informational} has this annotation
     */
    boolean hasAnnotation(Annotation annotation);

    /**
     * @param clazz the annotation type
     * @return if the {@link Informational} has an annotation of this type
     */
    boolean hasAnnotationType(Class<? extends Annotation> clazz);

    /**
     * @param clazz the annotation type to get
     * @param <A>   the annotation type
     * @return an annotation if present on the {@link Informational}, otherwise null
     */
    <A extends Annotation> A getAnnotation(Class<A> clazz);
}
