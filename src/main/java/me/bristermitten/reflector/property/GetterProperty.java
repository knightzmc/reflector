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
public class GetterProperty extends AbstractProperty {

    @Inject
    public GetterProperty(ReflectionHelper helper,
                          SetterFactory factory,
                          Reflector reflector,
                          @Assisted String name,
                          @Assisted Field field,
                          @Assisted @NotNull Method getter) {
        super(helper, factory, reflector, name, field, getter, null);
    }

    @Override
    public Object getValue(Object source) {
        return reflectionHelper.invokeMethod(Objects.requireNonNull(getterM), source);
    }

    @Override
    public Setter createSetter(Object settingOn) {
        return Setter.EMPTY;
    }

    @Override
    public boolean hasAnnotation(@NotNull Class<? extends Annotation> annotation) {
        return getterM.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(@NotNull Class<A> aClass) {
        return getterM.getAnnotation(aClass);
    }
}
