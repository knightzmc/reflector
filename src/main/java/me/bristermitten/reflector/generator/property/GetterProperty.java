package me.bristermitten.reflector.generator.property;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.ToString;
import me.bristermitten.jui.JUI;
import me.bristermitten.jui.generator.property.setter.Setter;
import me.bristermitten.jui.generator.property.setter.SetterFactory;
import me.bristermitten.jui.helper.ReflectionHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

@ToString(callSuper = true)
public class GetterProperty extends AbstractProperty {

    @Inject
    public GetterProperty(ReflectionHelper helper,
                          SetterFactory factory,
                          JUI jui,
                          @Assisted String name,
                          @Assisted Field field,
                          @Assisted @NotNull Method getter) {
        super(helper, factory, jui, name, field, getter, null);
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
