package me.bristermitten.reflector.generator.property.setter;

import com.google.inject.name.Named;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface SetterFactory {
    @Named("FieldSetter")
    Setter createFieldSetter(Field toSet,
                             @Nullable Object settingOn);

    @Named("SetterSetter")
    Setter createSetterSetter(Method setter,
                              @Nullable Object setting);
}
