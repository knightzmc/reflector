package me.bristermitten.reflector.property.info;

import com.google.inject.Inject;
import me.bristermitten.reflector.helper.ArrayHelper;
import me.bristermitten.reflector.helper.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InfoFactory {
    private final ReflectionHelper helper;
    private final ArrayHelper<Annotation> a;

    @Inject
    public InfoFactory(ReflectionHelper helper) {
        this.helper = helper;
        this.a = helper.annotationHelper();
    }

    public PropertyInfo createInfo(Field f) {
        return new PropertyInfo(a.add(
                helper.getAnnotations(f)));
    }

    public PropertyInfo createInfo(Field f, Method getter) {
        return new PropertyInfo(a.add(
                helper.getAnnotations(f),
                helper.getAnnotations(getter)));
    }

    public PropertyInfo createInfo(Field f, Method getter, Method setter) {
        return new PropertyInfo(a.add(
                helper.getAnnotations(f),
                helper.getAnnotations(getter),
                helper.getAnnotations(setter)));
    }

}
