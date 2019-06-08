package me.bristermitten.reflector.searcher;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

public class FieldSearcher extends Searcher<Field> {
    @Inject
    public FieldSearcher(Options options) {
        super(options);
    }

    @Override
    protected Set<Field> find(Class clazz) {
        ImmutableSet.Builder<Field> builder = ImmutableSet.builder();
        builder.addAll(Arrays.asList(clazz.getDeclaredFields()));
        builder.addAll(Arrays.asList(clazz.getFields()));
        if (options.scanSuperClasses()) {
            searchSuper(clazz, builder);
        }
        ImmutableSet<Field> fields = builder.build();
        fields.removeIf(Field::isSynthetic);
        return fields;
    }
}
