package me.bristermitten.reflector.property.structure;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.Data;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.constructor.NoConstructorExistsException;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.info.search.PropertySearcher;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;

import java.util.Arrays;
import java.util.Optional;
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
     * The Class that this ClassStructure wraps. Ideally a Bean.
     */
    private final Class type;
    /**
     * An immutable set of all of the properties (wrapped fields and/or accessors)
     * of {@link ClassStructure#type}
     */
    private final Set<Property> properties;

    private final ReflectionHelper helper;

    private final Set<InstanceConstructor> constructors;

    @Inject
    public ClassStructure(
            @Assisted Class type,
            @Assisted Set<Property> properties,
            @Assisted Set<InstanceConstructor> constructors,
            ReflectionHelper helper) {
        this.type = type;
        this.properties = properties;
        this.constructors = constructors;
        this.helper = helper;
    }

    public boolean isComplexType() {
        return !helper.isSimple(type);
    }

    public PropertySearcher searchProperties() {
        return new PropertySearcher(this);
    }


    public <T> T createInstance(Object... args) throws NoConstructorExistsException {
        Class[] argTypes = (Class[]) Arrays.stream(args).map(Object::getClass).toArray();
        InstanceConstructor<T> constructor = constructorFor(argTypes);
        return constructor.create(args);
    }

    public <T> InstanceConstructor<T> constructorFor(Class... types) throws NoConstructorExistsException {
        Optional<InstanceConstructor> first = constructors.stream().filter(c -> c.argsMatches(types)).findFirst();
        if (!first.isPresent()) {
            throw new NoConstructorExistsException("No constructor found for " + type + " with parameters " + Arrays.toString(types));
        }
        return first.get();
    }
}
