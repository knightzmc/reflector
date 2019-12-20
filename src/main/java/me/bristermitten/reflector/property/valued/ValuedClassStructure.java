package me.bristermitten.reflector.property.valued;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.bristermitten.reflector.annotation.Nullable;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.Element;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.structure.ClassStructure;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Extension of {@link ClassStructure} that maps properties with values from a given object
 */
public class ValuedClassStructure extends ClassStructure implements Element {
    private final Map<Property, Object> cachedPropertyValues = new WeakHashMap<>();
    private Object valuesFrom;

    @Inject
    public ValuedClassStructure(@Assisted Class<?> type,
                                @Assisted Set<Property> properties,
                                @Assisted Info info,
                                @Assisted Set<InstanceConstructor<?>> constructors,
                                @Nullable @Assisted Object valuesFrom,
                                @Assisted boolean fullClass,
                                ReflectionHelper helper,
                                Options options) {
        super(type, properties, info, constructors, fullClass, helper, options);

        setValues(valuesFrom);
    }

    /**
     * Update all property values to be from a given object
     *
     * @param from the object to update
     */
    public void setValues(Object from) {
        if (from == null) return;
        this.valuesFrom = from;
        getProperties().forEach(p -> {
            p.setSource(from);
            cachedPropertyValues.put(p, p.getValue());
        });
    }

    public Object getValue() {
        return valuesFrom;
    }

    @Override
    public boolean isSubTypeOf(Class<?> type) {
        return type.isAssignableFrom(getType());
    }

    public Map<Property, Object> getPropertyValues() {
        return cachedPropertyValues;
    }

    public String getName() {
        return getType().getSimpleName();
    }
}
