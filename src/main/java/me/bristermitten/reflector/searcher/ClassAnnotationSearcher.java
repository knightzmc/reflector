package me.bristermitten.reflector.searcher;

import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

public class ClassAnnotationSearcher extends Searcher<Annotation> {

    @Inject
    public ClassAnnotationSearcher(Options options) {
        super(options);
    }

    @Override
    protected void find0(Class<?> clazz, Set<Annotation> addTo) {
        addTo.addAll(Arrays.asList(clazz.getAnnotations()));

        if (options.scanSuperInterfaceAnnotations()) {
            for (Class<?> i : clazz.getInterfaces()) {
                find0(i, addTo);
            }
        }
    }
}
