package me.bristermitten.reflector.property.valued;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
<<<<<<< HEAD:src/main/java/me/bristermitten/reflector/property/valued/ValuedClassStructure.java
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.helper.ReflectionHelper;
=======
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.Element;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.structure.ClassStructure;
>>>>>>> 7abc07d18c071d4af5c28719584509a0782193d9:src/main/java/me/bristermitten/reflector/property/valued/ValuedClassStructure.java

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

<<<<<<< HEAD:src/main/java/me/bristermitten/reflector/property/valued/ValuedClassStructure.java
public class ValuedClassStructure extends ClassStructure {
=======
public class ValuedClassStructure extends ClassStructure implements Element {
>>>>>>> 7abc07d18c071d4af5c28719584509a0782193d9:src/main/java/me/bristermitten/reflector/property/valued/ValuedClassStructure.java
    private final Map<Property, Object> cachedPropertyValues = new WeakHashMap<>();

    private Object valuesFrom;

    @Inject
    public ValuedClassStructure(@Assisted Class type,
                                @Assisted Set<Property> properties,
                                @Assisted Object valuesFrom,
                                ReflectionHelper helper) {
        super(type, valuesFrom == null ? ImmutableSet.of() : properties, helper);
        setValues(valuesFrom);
    }

    public void setValues(Object from) {
        if (from == null) return;
        this.valuesFrom = from;
        getProperties().forEach(p -> {
            cachedPropertyValues.put(p, p.getValue(from));
            p.setSource(from);
        });
    }

    public Object getValue() {
        return valuesFrom;
    }

    public Map<Property, Object> getPropertyValues() {
        return cachedPropertyValues;
    }

    public String getName() {
        return getType().getSimpleName();
    }
}
