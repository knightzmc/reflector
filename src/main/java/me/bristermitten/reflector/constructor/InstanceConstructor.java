package me.bristermitten.reflector.constructor;

import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.info.Informational;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceConstructor<T> implements Informational {
    private final Constructor<T> constructor;
    private final Info info;
    public InstanceConstructor(Constructor<T> constructor, Info info) {
        this.constructor = constructor;
        this.info = info;
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

    @Override
    public Info getInfo() {
        return info;
    }
}
