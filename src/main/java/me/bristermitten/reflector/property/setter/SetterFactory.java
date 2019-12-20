package me.bristermitten.reflector.property.setter;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Guice assisted injection factory for creating setters
 */
public interface SetterFactory {

    FieldSetter createFieldSetter(Field toSet, @Nullable Object settingOn);

    SetterSetter createSetterSetter(Method setter, @Nullable Object setting);
}
