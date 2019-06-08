package me.bristermitten.reflector.searcher;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class MethodSearcher extends Searcher<Method> {
    private final Options options;

    @Inject
    public MethodSearcher(Options options) {
        super(options);
        this.options = options;
    }

    @Override
    protected Set<Method> find(Class clazz) {
        ImmutableSet.Builder<Method> builder = ImmutableSet.builder();
        builder.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        builder.addAll(Arrays.asList(clazz.getMethods()));

        if (options.scanSuperClasses()) {
           searchSuper(clazz, builder);
        }
        ImmutableSet<Method> methods = builder.build();
        methods.removeIf(Method::isSynthetic);
        return methods;
    }
}
