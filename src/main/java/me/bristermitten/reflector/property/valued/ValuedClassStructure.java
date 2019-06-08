package me.bristermitten.reflector.property.valued;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.Element;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.structure.ClassStructure;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class ValuedClassStructure extends ClassStructure implements Element {
    private final Map<Property, Object> cachedPropertyValues = new WeakHashMap<>();

    private Object valuesFrom;

    @Inject
    public ValuedClassStructure(@Assisted Class type,
                                @Assisted Set<Property> properties,
                                @Assisted Info info,
                                @Assisted Set<InstanceConstructor> constructors,
                                @Assisted Object valuesFrom,
                                Provider<ReflectionHelper> helper) {
        super(type, valuesFrom == null ? ImmutableSet.of() : properties, info, constructors, helper);
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

    @Override
    public boolean isSubTypeOf(Class type) {
        return type.isAssignableFrom(getType());
    }

    public Map<Property, Object> getPropertyValues() {
        return cachedPropertyValues;
    }

    public String getName() {
        return getType().getSimpleName();
    }
}
