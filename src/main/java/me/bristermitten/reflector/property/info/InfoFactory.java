package me.bristermitten.reflector.property.info;

import com.google.inject.Inject;
import me.bristermitten.reflector.helper.ArrayHelper;
import me.bristermitten.reflector.helper.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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

    public Info createInfo(Constructor constructor) {
        return new TypeInfo(helper.getAnnotations(constructor));
    }

    public Info createInfo(Class clazz) {
        return new TypeInfo(helper.getAnnotations(clazz));
    }

    public Info createInfo(Field f) {
        return new TypeInfo(helper.getAnnotations(f));
    }

    public Info createInfo(Field f, Method getter) {
        return new TypeInfo(a.add(
                helper.getAnnotations(f),
                helper.getAnnotations(getter)));
    }

    public Info createInfo(Field f, Method getter, Method setter) {
        return new TypeInfo(a.add(
                helper.getAnnotations(f),
                helper.getAnnotations(getter),
                helper.getAnnotations(setter)));
    }

}
