package me.bristermitten.reflector.searcher;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import me.bristermitten.reflector.config.Options;

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
                .maximumSize(100).weakKeys().build(
                        CacheLoader.from(this::find)
                );
    }

    public Set<T> search(Class clazz) {
        return cache.getUnchecked(clazz);
    }

    protected abstract Set<T> find(Class clazz);
}
