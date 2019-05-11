package me.bristermitten.reflector.searcher;

import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
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
        Set<Method> tempSet = new HashSet<>();
        Collections.addAll(tempSet, clazz.getDeclaredMethods());
        if (options.scanSuperClasses()) {
            Collections.addAll(tempSet, clazz.getMethods());
        }
        return tempSet;
    }
}
