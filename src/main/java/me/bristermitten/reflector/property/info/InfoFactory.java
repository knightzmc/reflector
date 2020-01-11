package me.bristermitten.reflector.property.info;

import com.google.inject.Inject;
import lombok.var;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.helper.ArrayHelper;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.searcher.ClassAnnotationSearcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Factory for creating new instances of {@link Info}
 */
public class InfoFactory {
    private final ReflectionHelper helper;
    private final ArrayHelper<Annotation> arrayHelper;
    private final Options options;
    private final ClassAnnotationSearcher annotationSearcher;

    @Inject
    public InfoFactory(ReflectionHelper helper, Options options, ClassAnnotationSearcher annotationSearcher) {
        this.helper = helper;
        this.arrayHelper = helper.annotationHelper();
        this.options = options;
        this.annotationSearcher = annotationSearcher;
    }

    public Info createInfo(Constructor<?> constructor) {
        return new SimpleElementInfo(helper.getAnnotations(constructor));
    }

    public Info createInfo(Class<?> clazz) {
        return new SimpleElementInfo(annotationSearcher.search(clazz).toArray(new Annotation[0]));
    }

    public Info createInfo(Field f) {
        return new SimpleElementInfo(helper.getAnnotations(f));
    }

    public Info createInfo(Field f, Method getter) {
        Annotation[] annotations = arrayHelper.add(helper.getAnnotations(f), helper.getAnnotations(getter));
        return new SimpleElementInfo(annotations);
    }

    public Info createInfo(Field f, Method getter, Method setter) {
        return new SimpleElementInfo(arrayHelper.add(
                helper.getAnnotations(f),
                helper.getAnnotations(getter),
                helper.getAnnotations(setter)));
    }

}
