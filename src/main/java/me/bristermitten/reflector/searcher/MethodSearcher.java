package me.bristermitten.reflector.searcher;

import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

/**
 * Simple searcher that finds all methods in a class,
 * except synthetic methods
 */
public class MethodSearcher extends Searcher<Method> {

    @Inject
    public MethodSearcher(Options options) {
        super(options);
    }

    @Override
    protected void find0(Class<?> clazz, Set<Method> addTo) {
        Collections.addAll(addTo, clazz.getDeclaredMethods());
        Collections.addAll(addTo, clazz.getMethods());
        addTo.removeIf(Method::isSynthetic);
    }
}
