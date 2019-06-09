package me.bristermitten.reflector.property.info.search;

import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.info.Info;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Search condition that matches any annotations on a property
 */
public class AnnotationSearchCondition implements SearchCondition {
    private final Class<? extends Annotation>[] types;

    @SafeVarargs
    public AnnotationSearchCondition(Class<? extends Annotation>... types) {
        this.types = types;
    }

    @Override
    public boolean matches(Property p) {
        Info info = p.getInfo();
        return Arrays.stream(types).allMatch(info::hasAnnotationType);
    }
}
