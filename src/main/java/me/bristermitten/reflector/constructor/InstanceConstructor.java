package me.bristermitten.reflector.constructor;

import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.info.Informational;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Simple wrapper of {@link Constructor}
 *
 * @param <T> the type that the constructor creates
 */
public class InstanceConstructor<T> implements Informational {
    private final Constructor<T> constructor;
    private final Info info;
    private final boolean lenient;

    public InstanceConstructor(Constructor<T> constructor, Info info, boolean lenient) {
        this.constructor = constructor;
        this.info = info;
        this.lenient = lenient;
    }

    /**
     * Call the internal constructor with the given arguments
     *
     * @param args arguments
     * @return the newly created instance, or null if an exception was thrown
     */
    public T create(Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            if (!lenient) return null;
            return create(Arrays.copyOf(args, args.length - 1));
        }
    }

    /**
     * Check that the argument types for the internal constructor match
     * the given array of types
     *
     * @param types types to compare
     * @return if the types are equal
     */
    public boolean argsMatches(Class<?>... types) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (!types[i].equals(parameterType)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Info getInfo() {
        return info;
    }
}
