package me.bristermitten.reflector.property.info.search;

import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.structure.ClassStructure;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

/**
 * Wraps a Stream to find properties matching one or multiple {@link SearchCondition}s
 */
public final class PropertySearcher {
    private Stream<Property> properties;

    /**
     * Create a new Property Searcher from a ClassStructure to search
     * This is called in {@link ClassStructure#searchProperties()}
     *
     * @param search the structure to search
     */
    public PropertySearcher(ClassStructure search) {
        this.properties = search.getProperties().stream();
    }

    /**
     * Search by type of property
     *
     * @param types the types that must be matched
     * @return this
     */
    public final PropertySearcher byType(Class<?>... types) {
        filter(new TypeSearchCondition(types));
        return this;
    }

    /**
     * Search by annotation
     *
     * @param annotations the annotations a property must have
     * @return this
     */
    @SafeVarargs
    public final PropertySearcher byAnnotation(Class<? extends Annotation>... annotations) {
        filter(new AnnotationSearchCondition(annotations));
        return this;
    }

    /**
     * Search by name
     *
     * @param name the name that must match
     * @return this
     */
    public PropertySearcher byName(String name) {
        filter(new NameSearchCondition(name));
        return this;
    }

    /**
     * Search by custom condition
     *
     * @param condition the condition
     * @return this
     */
    public PropertySearcher by(SearchCondition condition) {
        filter(condition);
        return this;
    }

    /**
     * @return a stream of all matching properties
     */
    public Stream<Property> search() {
        return properties;
    }

    private void filter(SearchCondition condition) {
        properties = properties.filter(condition);
    }
}
