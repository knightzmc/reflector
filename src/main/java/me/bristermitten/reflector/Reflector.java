package me.bristermitten.reflector;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.constructor.NoConstructorExistsException;
import me.bristermitten.reflector.inject.ReflectorBindingModule;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.property.structure.Null;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;
import me.bristermitten.reflector.searcher.ClassSearcher;
import me.bristermitten.reflector.searcher.ClassStructureFactory;

import java.util.Set;

/**
 * Main class for Reflector
 * Wraps any internals and provides methods for creating structures
 * <p>
 * There are two ways of creating a Reflector instance
 * 1) Simply call a constructor. This is ideal for simple projects which don't use Guice
 * 2) In your Guice Module, create and install a new instance of {@link ReflectorBindingModule},
 * and an instance of {@link Reflector} will be injected.
 */
@Singleton
public class Reflector {

    @Inject
    private ClassSearcher searcher;

    @Inject
    private ClassStructureFactory factory;

    /**
     * Simple constructor for use without Guice and using default options
     */
    public Reflector() {
        this(Options.DEFAULT);
    }

    /**
     * Simple constructor for use without Guice and with custom options
     *
     * @param options custom options to use
     */
    public Reflector(Options options) {
        new ReflectorBindingModule(options).createInjector().injectMembers(this);
    }


    /**
     * Create or get from cache a ClassStructure of a given Class
     *
     * @param clazz the class to create a structure of
     * @return a ClassStructure of the given class
     */
    public ClassStructure getStructure(Class<?> clazz) {
        return searcher.search(clazz);
    }

    /**
     * Simple iterative method to get a Set of ClassStructures from a given
     * array of classes
     *
     * @param classes the classes to get structures of
     * @return an ImmutableSet containing structures of the classes
     * @see Reflector#getStructure(Class)
     */
    public Set<ClassStructure> getStructures(Class<?>... classes) {
        ImmutableSet.Builder<ClassStructure> builder = ImmutableSet.builder();
        for (Class<?> c : classes) {
            builder.add(getStructure(c));
        }
        return builder.build();
    }

    /**
     * Assign values to a given ClassStructure from a given object
     * This iterates over all the properties in the structure and
     * gets their value from the given object, essentially calling the getter or getting
     * the field value, then caching that and returning a ValuedClassStructure
     *
     * @param structure  The structure to get properties from
     * @param valuesFrom The object to get property values from
     * @return a ValuedClassStructure from the given data
     */
    public ValuedClassStructure assignValues(ClassStructure structure, Object valuesFrom) {
        return factory.createValuedStructure(structure.getType(), structure.getProperties(), structure.getInfo(),
                structure.getConstructors(), valuesFrom, structure.isFullClass());
    }

    /**
     * Create a ValuedClassStructure from a given object
     *
     * @param o the object to transform
     * @return a ValuedClassStructure matching the given object
     * @see Reflector#assignValues(ClassStructure, Object)
     */
    public ValuedClassStructure getValuedStructure(Object o) {
        if (o == null) {
            return assignValues(getStructure(Null.class), null);
        }
        ClassStructure structure = getStructure(o.getClass());
        return assignValues(structure, o);
    }

    /**
     * Create an InstanceConstructor for a given class
     * based on a given set of argument types
     *
     * @param tClass the class to find the constructor in
     * @param args   the argument types
     * @param <T>    the type that the constructor returns
     * @return an InstanceConstructor matching the specification if any exists or
     * @throws NoConstructorExistsException if no constructor matches the argument types
     */
    public <T> InstanceConstructor<T> construct(Class<T> tClass, Class<?>... args) throws NoConstructorExistsException {
        return construct(getStructure(tClass), args);
    }

    /**
     * Create an InstanceConstructor for a given class structure
     * based on a given set of argument types
     *
     * @param structure the structure to find the constructor in
     * @param args      the argument types
     * @param <T>       the type that the constructor returns
     * @return an InstanceConstructor matching the specification if any exists or
     * @throws NoConstructorExistsException if no constructor matches the argument types
     */
    public <T> InstanceConstructor<T> construct(ClassStructure structure, Class<?>... args) throws NoConstructorExistsException {
        return structure.constructorFor(args);
    }

}
