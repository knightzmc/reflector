package me.bristermitten.reflector.property;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import me.bristermitten.reflector.property.info.PropertyInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface PropertyFactory {

    @Named("FullAccessorProperty")
    Property createProperty(String name,
                            Field field,
                            @Assisted("getterMethod") Method getter,
                            @Assisted("setterMethod") Method setter,
                            PropertyInfo info);

    @Named("GetterProperty")
    Property createProperty(String name,
                            Field field,
                            Method getter,
                            PropertyInfo info);

    @Named("FieldProperty")
    Property createProperty(String name,
                            Field field,
                            PropertyInfo info);
}
