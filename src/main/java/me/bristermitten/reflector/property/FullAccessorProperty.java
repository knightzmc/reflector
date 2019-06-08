package me.bristermitten.reflector.property;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.ToString;
import me.bristermitten.reflector.Reflector;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.info.TypeInfo;
import me.bristermitten.reflector.property.setter.Setter;
import me.bristermitten.reflector.property.setter.SetterFactory;
import me.bristermitten.reflector.helper.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

@ToString(callSuper = true)
public class FullAccessorProperty extends AbstractProperty {
    @Inject
    public FullAccessorProperty(ReflectionHelper helper,
                                SetterFactory factory,
                                Reflector reflector,
                                @Assisted String name,
                                @Assisted Field field,
                                @Assisted("getterMethod") Method getter,
                                @Assisted("setterMethod") Method setter,
                                @Assisted Info info) {
        super(helper, factory, reflector, name, field, getter, setter, info);
    }

    @Override
    public Object getValue(Object source) {
        return reflectionHelper.invokeMethod(Objects.requireNonNull(getterMethod), source);
    }

    @Override
    public Setter createSetter(Object settingOn) {
        return factory.createSetterSetter(setterMethod, settingOn);
    }

}
