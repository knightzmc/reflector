package me.bristermitten.reflector.helper;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple helper class for merging arrays
 * Due to API limitations, this cannot be a singleton and must be re-instantiated per type of array
 * eg an {@code ArrayHelper<String>} CANNOT be called with a {@code int[]}
 *
 * @param <T> The type of array that is merged
 */
public class ArrayHelper<T> {
    private Class<T> type;

    public ArrayHelper(Class<T> type) {
        this.type = type;
    }

    @SafeVarargs
    public static <T> T[] add(Class<T> type, T[]... ts) {
        Set<T> set = new HashSet<>();
        for (T[] tArr : ts) {
            Collections.addAll(set, tArr);
        }

        T[] a = (T[]) Array.newInstance(type, set.size());
        Object[] toArray = set.toArray();
        for (int i = 0; i < toArray.length; i++) {
            Object o = toArray[i];
            a[i] = (T) o;
        }
        return a;
    }

    /**
     * Add multiple arrays
     * The array types must match {@link ArrayHelper#type}
     *
     * @param ts arrays to add
     * @return the added array
     */
    @SafeVarargs
    public final T[] add(T[]... ts) {
        return add(this.type, ts);
    }
}
