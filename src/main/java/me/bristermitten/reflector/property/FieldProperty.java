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

@ToString(callSuper = true)
public class FieldProperty extends AbstractProperty {

    @Inject
    public FieldProperty(ReflectionHelper helper,
                         SetterFactory factory,
                         Reflector reflector,
                         @Assisted String name,
                         @Assisted Field field) {
        super(helper, factory, reflector, name, field, null, null);
    }

    @Override
    public Object getValue(Object source) {
        return reflectionHelper.getFieldValue(field, source);
    }

    @Override
    public Setter createSetter(Object settingOn) {
        return factory.createFieldSetter(field, settingOn);
    }

    @Override
    public boolean hasAnnotation(@NotNull Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(@NotNull Class<A> aClass) {
        return field.getAnnotation(aClass);
    }
}
