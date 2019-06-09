package me.bristermitten.reflector.searcher;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.bristermitten.reflector.annotation.ReflectorExpose;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.PropertyFactory;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.info.InfoFactory;
import me.bristermitten.reflector.property.structure.ClassStructure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Main internal functionality
 * Scans a given class and returns a ClassStructure, with caching in place
 */
@Singleton
public class ClassSearcher {
    private final Options options;
    private final FieldSearcher fieldSearcher;
    private final LoadingCache<Class, ClassStructure> structureCache;
    private final LoadingCache<Field, Optional<Method>> getterCache, setterCache;
    private final LoadingCache<Class, Set<InstanceConstructor>> constructorCache;
    private final PropertyFactory propertyFactory;
    private final ClassStructureFactory structureFactory;
    private final NameDecider decider;
    private final InfoFactory factory;

    @Inject
    public ClassSearcher(
            FieldSearcher fieldSearcher,
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
        constructorCache = cacheBuilder.build(CacheLoader.from(reflectionHelper::getConstructors));
    }

    /**
     * Search a given class and create or get from cache a ClassStructure
     *
     * @param clazz class to scan
     * @return a matching ClassStructure
     */
    public ClassStructure search(Class clazz) {
        return structureCache.getUnchecked(clazz);
    }

    /**
     * Internal searching functionality if no value is present in cache
     *
     * @param clazz class to scan
     * @return a matching ClassStructure
     */
    private ClassStructure search0(Class clazz) {
        //scan annotations and other info of the class
        Info info = factory.createInfo(clazz);
        //get or find constructors
        Set<InstanceConstructor> constructors = constructorCache.getUnchecked(clazz);

        //create a new sorted set to contain all properties
        Set<Property> properties = new TreeSet<>(Comparator.comparing(Property::getName));
        for (Field field : fieldSearcher.search(clazz)) {
            //workaround as guava's caches don't allow null values
            Optional<Method> getter = getterCache.getUnchecked(field);
            Optional<Method> setter = setterCache.getUnchecked(field);

            Property property = createProperty(field, getter.orElse(null), setter.orElse(null));
            if (property != null)
                properties.add(property);
        }
        //assisted inject the searched data into a new structure
        return structureFactory.createStructure(clazz, ImmutableSet.copyOf(properties), info, constructors);
    }

    /**
     * Create a property from a field, and optional getter and/or setter
     *
     * @param field  field holding the property value
     * @param getter nullable getter for the property
     * @param setter nullable setter for the property
     * @return a Property, or null if getter and setter are null, and the field is not allowed alone
     */
    private Property createProperty(Field field, Method getter, Method setter) {
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
        return null;
    }

}
