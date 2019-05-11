package me.bristermitten.reflector.property.setter;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.bristermitten.reflector.helper.ReflectionHelper;

import java.lang.reflect.Field;

public class FieldSetter<V> implements Setter {
    private final Field toSet;
    private final Object settingOn;
    private final ReflectionHelper helper;
    private Object cachedValue;

    @Inject
    public FieldSetter(ReflectionHelper helper,
                       @Assisted Field toSet, @Assisted Object settingOn) {
        this.toSet = toSet;
        this.settingOn = settingOn;
        this.helper = helper;
    }

    @Override
    public Object set(Object newValue) {
        Object cached = cachedValue;
        cachedValue = helper.setFieldValue(toSet, settingOn, newValue);
        return cached;
    }
}
