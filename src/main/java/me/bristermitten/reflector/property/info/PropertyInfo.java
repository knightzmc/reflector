package me.bristermitten.reflector.property.info;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class PropertyInfo {
    private final Annotation[] annotations;

    public PropertyInfo(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public boolean hasAnnotation(Annotation annotation) {
        return Arrays.asList(annotations).contains(annotation);
    }

    public boolean hasAnnotationType(Class<? extends Annotation> clazz) {
        return Arrays.stream(annotations).map(Annotation::annotationType).anyMatch(clazz::equals);
    }
}
