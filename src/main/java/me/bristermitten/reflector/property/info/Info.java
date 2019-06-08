package me.bristermitten.reflector.property.info;

import java.lang.annotation.Annotation;

public interface Info {
    boolean hasAnnotation(Annotation annotation);

    boolean hasAnnotationType(Class<? extends Annotation> clazz);
}
