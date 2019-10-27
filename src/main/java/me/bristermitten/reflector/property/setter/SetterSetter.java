package me.bristermitten.reflector.property.setter;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import me.bristermitten.reflector.helper.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * This name was unfortunately inevitable...
 * <p>
 * A SetterSetter implements {@link Setter}, encapsulating {@link Setter#set(Object)}
 * with a Setter method which will be called to set a value
 *
 */
public class SetterSetter implements Setter {
    private final Method setter;
    private final Object setOn;
    private final ReflectionHelper helper;
    private Object cachedValue;

    @AssistedInject
    public SetterSetter(@Assisted Method setter,
                        @Assisted @Nullable Object setOn,
                        ReflectionHelper helper) {
        this.setter = setter;
        this.setOn = setOn;
        this.helper = helper;
    }

    @Override
    public Object set(Object newValue) {
        Object cached = cachedValue;
        helper.invokeMethod(setter, setOn, newValue);
        cachedValue = newValue;
        return cached;
    }
}
