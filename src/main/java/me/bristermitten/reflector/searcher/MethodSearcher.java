package me.bristermitten.reflector.searcher;

import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public class MethodSearcher extends Searcher<Method> {
    private final Options options;

    @Inject
    public MethodSearcher(Options options) {
        super(options);
        this.options = options;
    }

    @Override
    protected void find0(Class clazz, Set<Method> addTo) {
        Collections.addAll(addTo, clazz.getDeclaredMethods());
        Collections.addAll(addTo, clazz.getMethods());
        addTo.removeIf(Method::isSynthetic);
    }
}
