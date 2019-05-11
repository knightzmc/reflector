package me.bristermitten.reflector.property;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.ToString;
import me.bristermitten.reflector.Reflector;
import me.bristermitten.reflector.property.setter.Setter;
import me.bristermitten.reflector.property.setter.SetterFactory;
import me.bristermitten.reflector.helper.ReflectionHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
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
                                @Assisted("getter") @NotNull Method getter,
                                @Assisted("setter") @NotNull Method setter) {
        super(helper, factory, reflector, name, field, getter, setter);
    }

    @Override
    public Object getValue(Object source) {
        return reflectionHelper.invokeMethod(Objects.requireNonNull(getter), source);
    }

    @Override
    public Setter createSetter(Object settingOn) {
        return factory.createSetterSetter(setter, settingOn);
    }

    @Override
    public boolean hasAnnotation(@NotNull Class<? extends Annotation> annotation) {
        boolean has = getter.isAnnotationPresent(annotation);
        if (!has) has = setter.isAnnotationPresent(annotation);
        if (!has) has = field.isAnnotationPresent(annotation);
        return has;
    }

    @Override
    public <A extends Annotation> A getAnnotation(@NotNull Class<A> aClass) {
        A a = getter.getAnnotation(aClass);
        if (a == null) a = setter.getAnnotation(aClass);
        if (a == null) a = field.getAnnotation(aClass);
        return a;
    }

}
