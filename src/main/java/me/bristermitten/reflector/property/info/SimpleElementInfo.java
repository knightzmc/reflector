package me.bristermitten.reflector.property.info;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Simple {@link Info} implementation
 */
public class SimpleElementInfo implements Info {
    private final Annotation[] annotations;

    public SimpleElementInfo(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public boolean hasAnnotation(Annotation annotation) {
        return Arrays.asList(annotations).contains(annotation);
    }

    public boolean hasAnnotationType(Class<? extends Annotation> clazz) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (clazz.equals(annotationType)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> clazz) {
        for (Annotation a : annotations) {
            if (a.annotationType().equals(clazz)) {
                return (A) a;
            }
        }
        return null;
    }
}
