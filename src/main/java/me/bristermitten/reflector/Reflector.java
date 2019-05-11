package me.bristermitten.reflector;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.property.structure.Null;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;
import me.bristermitten.reflector.searcher.ClassSearcher;
import me.bristermitten.reflector.searcher.ClassStructureFactory;
import me.bristermitten.reflector.inject.ReflectorBindingModule;

import java.util.HashSet;
import java.util.Set;

public class Reflector {

    @Inject
    private ClassSearcher searcher;

    @Inject
    private ClassStructureFactory factory;

    public Reflector() {
        this(new ReflectorBindingModule().createInjector());
    }

    public Reflector(Options options) {
        this(new ReflectorBindingModule(options).createInjector());
    }

    private Reflector(Injector injector) {
        injector.injectMembers(this);
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
                structure.getProperties(), valuesFrom);
    }

    public ValuedClassStructure getValuedStructure(Object o) {
        if (o == null) {
            return assignValues(getStructure(Null.class), null);
        }
        ClassStructure structure = getStructure(o.getClass());
        return assignValues(structure, o);
    }
}
