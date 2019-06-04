package me.bristermitten.reflector.helper;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ArrayHelper<T> {
    private Class<T> type;

    public ArrayHelper(Class<T> type) {
        this.type = type;
    }

    public ArrayHelper() {

    }

    @SafeVarargs
    public final T[] add(T[]... ts) {
        return add(this.type, ts);
    }

    @SafeVarargs
    public final T[] add(Class<T> type, T[]... ts) {
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
}
