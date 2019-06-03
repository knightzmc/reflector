package me.bristermitten.reflector.property.info.search;

import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.structure.ClassStructure;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

public final class PropertySearcher {
    private Stream<Property> properties;

    public PropertySearcher(ClassStructure search) {
        this.properties = search.getProperties().stream();
    }

    public final PropertySearcher byType(Class... types) {
        filter(new TypeSearchCondition(types));
        return this;
    }

    @SafeVarargs
    public final PropertySearcher byAnnotation(Class<? extends Annotation>... annotations) {
        filter(new AnnotationSearchCondition(annotations));
        return this;
    }

    public PropertySearcher byName(String name) {
        filter(new NameSearchCondition(name));
        return this;
    }

    public PropertySearcher by(SearchCondition condition) {
        filter(condition);
        return this;
    }

    public Stream<Property> search() {
        return properties;
    }

    private void filter(SearchCondition condition) {
        properties = properties.filter(condition);
    }
}
