package me.bristermitten.reflector.searcher;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.bristermitten.reflector.annotation.ReflectorExpose;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.PropertyFactory;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.helper.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class ClassSearcher {
    private final Options options;
    private final Searcher fieldSearcher;
    private final LoadingCache<Field, Optional<Method>> getterCache, setterCache;
    private final ReflectionHelper reflectionHelper;
    private final PropertyFactory propertyFactory;
    private final ClassStructureFactory structureFactory;
    private final NameDecider decider;

    @Inject
    public ClassSearcher(
            Options options,
            @Named("FieldSearcher") Searcher fieldSearcher,
            ReflectionHelper reflectionHelper,
            PropertyFactory propertyFactory,
            ClassStructureFactory structureFactory,
            NameDecider decider) {
        this.options = options;
        this.fieldSearcher = fieldSearcher;
        this.reflectionHelper = reflectionHelper;
        this.propertyFactory = propertyFactory;
        this.structureFactory = structureFactory;
        this.decider = decider;

        CacheBuilder cacheBuilder = CacheBuilder.newBuilder();
        getterCache = cacheBuilder.build(CacheLoader.from(reflectionHelper::getGetterFor));
        setterCache = cacheBuilder.build(CacheLoader.from(reflectionHelper::getSetterFor));
    }

    public ClassStructure search(Class clazz) {
        Set<Field> fields = fieldSearcher.search(clazz); //find all fields in class
        Set<Property> properties = new TreeSet<>(Comparator.comparing(Property::getName));

        for (Field field : fields) {
            Optional<Method> getter = getterCache.getUnchecked(field); //workaround as guava's caches don't allow null values
            Optional<Method> setter = setterCache.getUnchecked(field);
            Property property = getProperty(field, getter.orElse(null), setter.orElse(null));
            if (property != null)
                properties.add(property);
        }
        return structureFactory.createStructure(clazz, ImmutableSet.copyOf(properties));
    }

    private Property getProperty(Field field, Method getter, Method setter) {
        String name = decider.makeName(field);
        if (getter != null && setter != null) {
            return propertyFactory.createProperty(name, field, getter, setter);
        }
        if (getter != null) {
            return propertyFactory.createProperty(name, field, getter);
        }
        if (options.includeFields()) //fields must be marked as included
            if (Modifier.isPublic(field.getModifiers()) //we only include public fields
                    || field.isAnnotationPresent(ReflectorExpose.class)) //or force a non-public field
            {
                return propertyFactory.createProperty(name, field);
            }
        return null; //TODO implement a "no properties found" return value
    }

}
