package me.bristermitten.reflector.generator.searcher;

import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FieldSearcher extends Searcher<Field> {
    @Inject
    public FieldSearcher(Options options) {
        super(options);
    }

    @Override
    protected Set<Field> find(Class clazz) {
        Set<Field> tempSet = new HashSet<>();
        Collections.addAll(tempSet, clazz.getDeclaredFields());
        if (options.scanSuperClasses()) {
            Collections.addAll(tempSet, clazz.getFields());
        }
        return tempSet;
    }
}
