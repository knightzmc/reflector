package me.bristermitten.reflector.generator.property.setter;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.bristermitten.reflector.helper.ReflectionHelper;

import java.lang.reflect.Field;

public class FieldSetter<V> implements Setter {
    private final Field toSet;
    private final Object settingOn;
    private final ReflectionHelper helper;

    @Inject
    public FieldSetter(ReflectionHelper helper,
                       @Assisted Field toSet, @Assisted Object settingOn) {
        this.toSet = toSet;
        this.settingOn = settingOn;
        this.helper = helper;
    }

    @Override
    public Object set(Object newValue) {
        return helper.setFieldValue(toSet, settingOn, newValue);
    }
}
