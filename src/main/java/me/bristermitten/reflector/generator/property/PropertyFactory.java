package me.bristermitten.reflector.generator.property;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface PropertyFactory {

    @Named("FullAccessorProperty")
    Property createProperty(String name, Field field, @Assisted("getter") Method getter,
                            @Assisted("setter") Method setter);

    @Named("GetterProperty")
    Property createProperty(String name, Field field, Method getter);

    @Named("FieldProperty")
    Property createProperty(String name, Field field);
}
