package me.bristermitten.reflector.helper;

import com.google.inject.Singleton;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

@Singleton
public class UnsafeHelper {

    private Unsafe unsafe;

    public Unsafe getUnsafe() {
        if (unsafe == null) {
            try {
                Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                return unsafe = (Unsafe) unsafeField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return unsafe;
    }
}
