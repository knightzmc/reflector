package me.bristermitten.reflector.searcher;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.bristermitten.reflector.annotation.ReflectorExpose;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.PropertyFactory;
import me.bristermitten.reflector.property.info.InfoFactory;
import me.bristermitten.reflector.property.structure.ClassStructure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Singleton
public class ClassSearcher {
    private final Options options;
    private final Searcher fieldSearcher;
    private final LoadingCache<Class, ClassStructure> structureCache;
    private final LoadingCache<Field, Optional<Method>> getterCache, setterCache;
    private final LoadingCache<Class, Set<InstanceConstructor>> constructorCache;
    private final PropertyFactory propertyFactory;
    private final ClassStructureFactory structureFactory;
    private final NameDecider decider;
    private final InfoFactory factory;

    @Inject
    public ClassSearcher(
            @Named("FieldSearcher") Searcher fieldSearcher,
            Options options,
            ReflectionHelper reflectionHelper,
            PropertyFactory propertyFactory,
            ClassStructureFactory structureFactory,
            NameDecider decider,
            InfoFactory factory) {
        this.options = options;
        this.fieldSearcher = fieldSearcher;
        this.propertyFactory = propertyFactory;
        this.structureFactory = structureFactory;
        this.decider = decider;
        this.factory = factory;

        CacheBuilder cacheBuilder = CacheBuilder.newBuilder();
        structureCache = cacheBuilder.build(CacheLoader.from(this::search0));
        getterCache = cacheBuilder.build(CacheLoader.from(reflectionHelper::getGetterFor));
        setterCache = cacheBuilder.build(CacheLoader.from(reflectionHelper::getSetterFor));
        constructorCache = cacheBuilder.build(CacheLoader.from((Function<Class, Set<InstanceConstructor>>) reflectionHelper::getConstructors));
    }

    public ClassStructure search(Class clazz) {
        return structureCache.getUnchecked(clazz);
    }

    private ClassStructure search0(Class clazz) {
        Set<Field> fields = fieldSearcher.search(clazz); //find all fields in class
        Set<Property> properties = new TreeSet<>(Comparator.comparing(Property::getName));
        Set<InstanceConstructor> constructors = constructorCache.getUnchecked(clazz);
        for (Field field : fields) {
            Optional<Method> getter = getterCache.getUnchecked(field); //workaround as guava's caches don't allow null values
            Optional<Method> setter = setterCache.getUnchecked(field);
            Property property = getProperty(field, getter.orElse(null), setter.orElse(null));
            if (property != null)
                properties.add(property);
        }
        return structureFactory.createStructure(clazz, ImmutableSet.copyOf(properties), constructors);
    }

    private Property getProperty(Field field, Method getter, Method setter) {
        String name = decider.makeName(field);
        if (getter != null && setter != null) {
            return propertyFactory.createProperty(name, field, getter, setter,
                    factory.createInfo(field, getter, setter));
        }
        if (getter != null) {
            return propertyFactory.createProperty(name, field, getter,
                    factory.createInfo(field, getter));
        }
        if (options.fieldAccessLevel().test(field) //field must have a set modifier
                || field.isAnnotationPresent(ReflectorExpose.class)) { //or can be forced with an annotation
            return propertyFactory.createProperty(name, field,
                    factory.createInfo(field));
        }
        return null; //TODO implement a "no properties found" return value
    }

}
