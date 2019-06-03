package me.bristermitten.reflector.property;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.ToString;
import me.bristermitten.reflector.Reflector;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.info.PropertyInfo;
import me.bristermitten.reflector.property.setter.Setter;
import me.bristermitten.reflector.property.setter.SetterFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

@ToString(callSuper = true)
public class GetterProperty extends AbstractProperty {

    @Inject
    public GetterProperty(ReflectionHelper helper,
                          SetterFactory factory,
                          Reflector reflector,
                          @Assisted String name,
                          @Assisted Field field,
                          @Assisted Method getter,
                          @Assisted PropertyInfo info) {
        super(helper, factory, reflector, name, field, getter, null, info);
    }

    @Override
    public Object getValue(Object source) {
        return reflectionHelper.invokeMethod(Objects.requireNonNull(getter), source);
    }

    @Override
    public Setter createSetter(Object settingOn) {
        return Setter.EMPTY;
    }
}
