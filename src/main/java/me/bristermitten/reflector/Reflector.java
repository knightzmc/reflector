package me.bristermitten.reflector;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.inject.ReflectorBindingModule;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.property.structure.Null;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;
import me.bristermitten.reflector.searcher.ClassSearcher;
import me.bristermitten.reflector.searcher.ClassStructureFactory;

import java.util.HashSet;
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
    private ReflectionHelper reflectionHelper;

    @Inject
    private ClassStructureFactory factory;

    public Reflector() {
        this(Options.DEFAULT);
    }

    public Reflector(Options options) {
        new ReflectorBindingModule(options).createInjector().injectMembers(this);
    }


    public ClassStructure getStructure(Class clazz) {
        return searcher.search(clazz);
    }

    public Set<ClassStructure> getStructures(Class... classes) {
        Set<ClassStructure> set = new HashSet<>();
        for (Class c : classes) {
            set.add(getStructure(c));
        }
        return set;
    }

    public ValuedClassStructure assignValues(ClassStructure structure, Object valuesFrom) {
        return factory.createValuedStructure(structure.getType(),
                structure.getProperties(), structure.getInfo(), structure.getConstructors(), valuesFrom);
    }

    public ValuedClassStructure getValuedStructure(Object o) {
        if (o == null) {
            return assignValues(getStructure(Null.class), null);
        }
        ClassStructure structure = getStructure(o.getClass());
        return assignValues(structure, o);
    }

    public <T> InstanceConstructor<T> construct(Class<T> tClass, Class... args) {
        return getStructure(tClass).constructorFor(args);
    }

    public ReflectionHelper helper() {
        return reflectionHelper;
    }
}
