package me.bristermitten.reflector.constructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceConstructor<T> {
    private final Constructor<T> constructor;

    public InstanceConstructor(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public T create(Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean argsMatches(Class... types) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (!types[i].equals(parameterType)) {
                return false;
            }
        }
        return true;
    }
}
