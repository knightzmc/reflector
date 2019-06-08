package me.bristermitten.reflector.searcher;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

import java.util.HashSet;
import java.util.Set;

/**
 * Generic superclass for all property searchers
 * A property searcher is passed a Class object, and returns a Set of a certain type of property,
 * which can be empty if none are found in the class.
 * A simple example would be a FieldSearcher, which returns a set of all the fields in a given class
 *
 * @param <T> The type of data returned
 */
public abstract class Searcher<T> {
    protected final Options options;
    private final LoadingCache<Class, Set<T>> cache;

    @Inject
    protected Searcher(Options options) {
        this.options = options;
        cache = CacheBuilder.newBuilder()
                .maximumSize(100).weakKeys().build(CacheLoader.from(this::find));
    }

    public Set<T> search(Class clazz) {
        return cache.getUnchecked(clazz);
    }

    protected Set<T> searchSuper(Class clazz, Set<T> parent) {
        Class superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            parent.addAll(searchSuper(superClass.getSuperclass(), parent));
        }
        return parent;
    }

    private Set<T> find(Class clazz) {
        Set<T> set = new HashSet<>();
        find0(clazz, set);
        return (Set<T>) ImmutableSet.of(set);
    }

    protected abstract void find0(Class clazz, Set<T> addTo);
}
