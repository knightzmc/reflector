package me.bristermitten.reflector.helper;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.bristermitten.reflector.searcher.AccessorMatcher;
import me.bristermitten.reflector.searcher.Searcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("ALL")
@Singleton
public class ReflectionHelper {
    private static final Set<Class<?>> PRIMITIVE_AND_WRAPPER_TYPES = ImmutableSet.of(
            boolean.class,
            byte.class,
            char.class,
            double.class,
            float.class,
            int.class,
            long.class,
            short.class,
            void.class,
            Boolean.class,
            Byte.class,
            Character.class,
            Double.class,
            Float.class,
            Integer.class,
            Long.class,
            Short.class,
            Void.class
    );

    private static final BiMap<Class, Class> PRIMITIVES_TO_WRAPPERS =
            ImmutableBiMap.<Class, Class>builder()
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


    @Inject
    @Named("MethodSearcher")
    private Searcher methodSearcher;
    @Inject
    private AccessorMatcher matcher;

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

    public <T> T setFieldValue(Field f, Object on, T newValue) {
        try {
            f.setAccessible(true);
            T previous = getFieldValue(f, on);
            f.set(on, newValue);
            f.setAccessible(false);
            return previous;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public Method getMethod(Class declarer, String name, Class... args) {
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

    public Optional<Method> getGetterFor(Field f) throws RuntimeException {
        Set<Method> allMethods = methodSearcher.search(f.getDeclaringClass());
        for (Method method : allMethods) {
            if (matcher.isGetter(method, f)) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    public Optional<Method> getSetterFor(Field f) throws RuntimeException {
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
     * First  checks the JDK {@link Class#isPrimitive()}, then falls back to a Set of all primitive and wrapper classes
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
    public boolean represents(Class clazz, Class type) {
        if (!isPrimitive(type)) return false;
        if (!isPrimitive(clazz)) return false;
        if (clazz == type) return true;
        return PRIMITIVES_TO_WRAPPERS.get(clazz) == type
                || PRIMITIVES_TO_WRAPPERS.inverse().get(clazz) == type;
    }


    public Annotation[] getAnnotations(AnnotatedElement member) {
        return helper.add(member.getAnnotations(), member.getDeclaredAnnotations());
    }

    public ArrayHelper<Annotation> annotationHelper(){
        return helper;
    }
}
