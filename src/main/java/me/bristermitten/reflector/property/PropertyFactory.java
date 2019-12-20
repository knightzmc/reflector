package me.bristermitten.reflector.property;

import com.google.inject.assistedinject.Assisted;
import me.bristermitten.reflector.property.info.Info;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface PropertyFactory {

    FullAccessorProperty createProperty(String name,
                                        Field field,
                                        Info info,
                                        @Assisted("getterMethod") Method getter,
                                        @Assisted("setterMethod") Method setter);

    GetterProperty createProperty(String name,
                                  Field field,
                                  Method getter,
                                  Info info);

    FieldProperty createProperty(String name,
                                 Field field,
                                 Info info);
}
