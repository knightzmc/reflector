package me.bristermitten.reflector.helper;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.info.InfoFactory;
import me.bristermitten.reflector.searcher.AccessorMatcher;
import me.bristermitten.reflector.searcher.Searcher;
import sun.misc.Unsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Optional;
import java.util.Set;

/**
 * Helper class for various reflective operations
 */
@Singleton
public class ReflectionHelper {
    private static final Set<Class<?>> PRIMITIVE_AND_WRAPPER_TYPES = ImmutableSet.of(
            boolean.class, byte.class,
            char.class, double.class,
            float.class, int.class,
            long.class, short.class,
            void.class, Boolean.class,
            Byte.class, Character.class,
            Double.class, Float.class,
            Integer.class, Long.class,
            Short.class, Void.class
    );

    private static final BiMap<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS =
            ImmutableBiMap.<Class<?>, Class<?>>builder()
                    .put(boolean.class, Boolean.class)
                    .put(byte.class, Byte.class)
                    .put(char.class, Character.class)
                    .put(double.class, Double.class)
                    .put(float.class, Float.class)
                    .put(int.class, Integer.class)
                    .put(long.class, Long.class)
                    .put(short.class, Short.class)
                    .put(void.class, Void.class)
                    .build();
    private final ArrayHelper<Annotation> helper = new ArrayHelper<>(Annotation.class);
    @SuppressWarnings("rawtypes")
    private final ArrayHelper<Constructor> constructorHelper = new ArrayHelper<>(Constructor.class);

    @Inject
    @Named("MethodSearcher")
    private Searcher<Method> methodSearcher;
    @Inject
    private AccessorMatcher matcher;
    @Inject
    private UnsafeHelper unsafeHelper;
    @Inject
    private Provider<InfoFactory> infoFactory;
    @Inject
    private Options options;

    /**
     * Invoke a method
     *
     * @param m    the method to invoke
     * @param on   the object to invoke on
     * @param args the method arguments
     * @param <T>  the type that the method returns
     * @return the method's return value, or null if anything went wrong
     */
    @SuppressWarnings("unchecked")
    public <T> T invokeMethod(Method m, Object on, Object... args) {
        if (on == null || m == null) return null;
        try {
            m.setAccessible(true);
            T invoke = (T) m.invoke(on, args);
            m.setAccessible(false);
            return invoke;
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /**
     * Get the value of a field
     *
     * @param f   the field to get the value of
     * @param on  the object to get the value from
     * @param <T> the type of the field
     * @return the field value, or null if anything went wrong
     */
    @SuppressWarnings("unchecked")
    public <T> T getFieldValue(Field f, Object on) {
        try {
            f.setAccessible(true);
            T t = (T) f.get(on);
            f.setAccessible(false);
            return t;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Set a field value
     * This method uses {@link Unsafe} in cases where reflection will not work
     *
     * @param f        the field to set
     * @param on       the object to set on
     * @param newValue the new value to set
     * @param <T>      the type of field
     * @return the previous value of the field
     */
    public <T> T setFieldValue(Field f, Object on, T newValue) {
        f.setAccessible(true);
        T previous = getFieldValue(f, on);
        try {
            f.set(on, newValue);
        } catch (IllegalAccessException e) {
            //let's try unsafe!
            Unsafe unsafe = unsafeHelper.getUnsafe();
            long offset = unsafe.objectFieldOffset(f);
            unsafe.putObject(on, offset, newValue);
        }
        f.setAccessible(false);
        return previous;
    }

    /**
     * Get a method
     *
     * @param declarer the class that holds the method
     * @param name     the name of the method
     * @param args     the arguments of the method
     * @return the method if found, or null if no method was found or something went wrong
     */
    public Method getMethod(Class<?> declarer, String name, Class<?>... args) {
        Method method = null;
        try {
            method = declarer.getMethod(name, args);
        } catch (NoSuchMethodException e) {
            try {
                method = declarer.getDeclaredMethod(name, args);
            } catch (NoSuchMethodException ignored) {
            }
        }
        return method;
    }

    /**
     * Get a getter method for a given field
     *
     * @param f the field to get a getter of
     * @return an optional of the getter, or empty if none was found
     */
    public Optional<Method> getGetterFor(Field f) {
        Set<Method> allMethods = methodSearcher.search(f.getDeclaringClass());
        for (Method method : allMethods) {
            if (matcher.isGetter(method, f)) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    /**
     * Get a setter method for a given field
     *
     * @param f the field to get a setter of
     * @return an optional of the setter, or empty if none was found
     */
    public Optional<Method> getSetterFor(Field f) {
        Set<Method> allMethods = methodSearcher.search(f.getDeclaringClass());
        for (Method method : allMethods) {
            if (matcher.isSetter(method, f)) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    /**
     * Check if a given type is a primitive or primitive wrapper
     * First checks the JDK {@link Class#isPrimitive()}, then falls back to a Set of all primitive and wrapper classes
     *
     * @param type The type to check
     * @return If the given type is primitive or a primitive wrapper
     */
    public boolean isPrimitive(Class<?> type) {
        return (type.isPrimitive() && type.getSuperclass() == null) || PRIMITIVE_AND_WRAPPER_TYPES.contains(type);
    }

    /**
     * Check if a given type is simple
     * We define simple in that it can be easily converted to a human readable String
     * In short, this checks if the given type is a String, or a primitive type
     *
     * @param type The class to check
     * @return if the given class is simple
     */
    public boolean isSimple(Class<?> type) {
        return isPrimitive(type) || String.class == type || type.isEnum();
    }

    /**
     * Check if a given class represents a primitive class type
     * That is, if the given class is equal to the boxed or primitive class
     * For example, if {type} is Integer.class and {type} is int.class,
     * true will be returned
     *
     * @param clazz the class to check
     * @param type  the primitive or boxed class to check
     * @return if the given class represents a primitive class of {type}
     */
    public boolean represents(Class<?> clazz, Class<?> type) {
        if (!isPrimitive(type)) return false;
        if (!isPrimitive(clazz)) return false;
        if (clazz == type) return true;
        return PRIMITIVES_TO_WRAPPERS.get(clazz) == type
                || PRIMITIVES_TO_WRAPPERS.inverse().get(clazz) == type;
    }

    /**
     * Get all constructors in a given class
     * //todo Convert to a {@link Searcher}
     *
     * @param clazz the class to get constructors in
     * @return an ImmutableSet of all constructors
     */
    public Set<InstanceConstructor<?>> getConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = constructorHelper.add(clazz.getConstructors(), clazz.getDeclaredConstructors());
        ImmutableSet.Builder<InstanceConstructor<?>> builder = ImmutableSet.builder();
        for (Constructor<?> constructor : constructors) {
            Info info = infoFactory.get().createInfo(constructor);
            builder.add(new InstanceConstructor<>(constructor, info, options.lenientConstructorSearch()));
        }
        return builder.build();
    }

    /**
     * Get all annotations on a given {@link AnnotatedElement}
     * Obviously the annotations must be retained at runtime to be found
     *
     * @param element the element to get annotations on
     * @return all annotations on the given element
     */
    public Annotation[] getAnnotations(AnnotatedElement element) {
        return helper.add(element.getAnnotations(), element.getDeclaredAnnotations());
    }

    public ArrayHelper<Annotation> annotationHelper() {
        return helper;
    }
}
