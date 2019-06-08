package me.bristermitten.reflector.searcher;

import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

public class FieldSearcher extends Searcher<Field> {
    @Inject
    public FieldSearcher(Options options) {
        super(options);
    }

    @Override
    protected void find0(Class clazz, Set<Field> addTo) {
        Collections.addAll(addTo, clazz.getDeclaredFields());
        Collections.addAll(addTo, clazz.getFields());
        if (options.scanSuperClasses()) {
            searchSuper(clazz, addTo);
        }
        addTo.removeIf(Field::isSynthetic);
    }
}
