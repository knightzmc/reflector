package me.bristermitten.reflector.generator.property;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.ToString;
import me.bristermitten.jui.JUI;
import me.bristermitten.jui.generator.property.setter.Setter;
import me.bristermitten.jui.generator.property.setter.SetterFactory;
import me.bristermitten.jui.helper.ReflectionHelper;

import java.lang.reflect.Field;

@ToString(callSuper = true)
public class FieldProperty extends AbstractProperty {

    @Inject
    public FieldProperty(ReflectionHelper helper,
                         SetterFactory factory,
                         JUI jui,
                         @Assisted String name,
                         @Assisted Field field) {
        super(helper, factory, jui, name, field, null, null);
    }

    @Override
    public Object getValue(Object source) {
        return reflectionHelper.getFieldValue(field, source);
    }

    @Override
    public Setter createSetter(Object settingOn) {
        return factory.createFieldSetter(field, settingOn);
    }
}
