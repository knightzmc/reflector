package me.bristermitten.reflector.property.structure;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import lombok.Data;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.constructor.NoConstructorExistsException;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.info.Informational;
import me.bristermitten.reflector.property.info.search.PropertySearcher;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Similar to a JavaScript prototype, a ClassStructure represents a Class, with each or some
 * of its fields, getters, and setters wrapped into {@link Property} objects
 * <p>
 * ClassStructures are the first step in the UI Generation process, which can then be
 * wrapped with an Object's values to create a {@link ValuedClassStructure}
 */
@Data
public class ClassStructure implements Informational {
    /**
     * The Class that this ClassStructure wraps. Ideally a Bean.
     */
    private final Class type;
    /**
     * An immutable set of all of the properties (wrapped fields and/or accessors)
     * of {@link ClassStructure#type}
     */
    private final Set<Property> properties;

    private final Set<InstanceConstructor> constructors;

    private final Info info;

    private final Provider<ReflectionHelper> helper;

    private final Options options;

    @Inject
    public ClassStructure(
            @Assisted Class type,
            @Assisted Set<Property> properties,
            @Assisted Info info,
            @Assisted Set<InstanceConstructor> constructors,
            Provider<ReflectionHelper> helper,
            Options options) {
        this.type = type;
        this.properties = properties;
        this.info = info;
        this.constructors = constructors;
        this.helper = helper;
        this.options = options;
    }

    /**
     * @return if this structure is of a complex type
     * @see ReflectionHelper#isSimple(Class)
     */
    public boolean isComplexType() {
        return !helper.get().isSimple(type);
    }

    /**
     * Search properties in this structure
     *
     * @return a new {@link PropertySearcher}
     */
    public PropertySearcher searchProperties() {
        return new PropertySearcher(this);
    }

    @Override
    public Info getInfo() {
        return info;
    }

    /**
     * Create a new instance of the underlying type
     *
     * @param args arguments to call
     * @param <T>  the type that is returned
     * @return a new instance, or null if something went wrong
     * @throws NoConstructorExistsException if no constructor matches the arguments
     * @see InstanceConstructor#create(Object...)
     */
    public <T> T createInstance(Object... args) throws NoConstructorExistsException {
        Class[] argTypes = (Class[]) Arrays.stream(args).map(Object::getClass).toArray();
        InstanceConstructor<T> constructor = constructorFor(argTypes);
        return constructor.create(args);
    }

    /**
     * Get a constructor matching the given types (or not if {@link Options#lenientConstructorSearch()})
     *
     * @param types the types to match
     * @param <T>   the type of constructor
     * @return a constructor or
     * @throws NoConstructorExistsException if no constructor was found
     */
    public <T> InstanceConstructor<T> constructorFor(Class... types) throws NoConstructorExistsException {
        Optional<InstanceConstructor> first = constructors.stream().filter(c -> c.argsMatches(types)).findFirst();
        if (!options.lenientConstructorSearch() || first.isPresent()) {
            return first.orElseThrow(noConstructorFound(types));
        }
        if (types.length > 0) {
            return constructorFor(Arrays.copyOf(types, types.length - 1)); //keep testing reducing arguments
        }
        throw noConstructorFound(types).get();
    }

    private Supplier<NoConstructorExistsException> noConstructorFound(Class... types) {
        return () -> new NoConstructorExistsException(String.format(
                "No constructor found for %s with parameters %s", type, Arrays.toString(types)));
    }
}
