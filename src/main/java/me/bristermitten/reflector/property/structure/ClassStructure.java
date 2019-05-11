package me.bristermitten.reflector.property.structure;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.Data;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;
import me.bristermitten.reflector.helper.ReflectionHelper;

import java.util.Set;

/**
 * Similar to a JavaScript prototype, a ClassStructure represents a Class, with each or some
 * of its fields, getters, and setters wrapped into {@link Property} objects
 * <p>
 * ClassStructures are the first step in the UI Generation process, which can then be
 * wrapped with an Object's values to create a {@link ValuedClassStructure}
 */
@Data
public class ClassStructure {

    /**
     * The Class that this ClassStructure wraps. Ideally a Java Bean / POJO.
     */
    private final Class type;
    /**
     * An immutable set of all of the properties (wrapped fields and/or accessors)
     * of {@link ClassStructure#type}
     */
    private final Set<Property> properties;

    private final ReflectionHelper helper;

    @Inject
    public ClassStructure(
            @Assisted Class type,
            @Assisted Set<Property> properties, ReflectionHelper helper) {
        this.type = type;
        this.properties = properties;
        this.helper = helper;
    }

    public boolean isComplexType() {
        return !helper.isSimple(type);
    }

}
