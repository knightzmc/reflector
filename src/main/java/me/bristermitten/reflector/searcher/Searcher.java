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
 * <p>
 * This also implements caching and superclass searching
 *
 * @param <T> The type of data returned
 */
public abstract class Searcher<T> {
    protected final Options options;
    private final LoadingCache<Class<?>, Set<T>> cache;

    @Inject
    protected Searcher(Options options) {
        this.options = options;
        cache = CacheBuilder.newBuilder()
                .maximumSize(100).weakKeys().build(CacheLoader.from(this::find));
    }

    /**
     * Search a class for something, and return the result
     * The result may be from cache
     *
     * @param clazz the class to search
     * @return an ImmutableSet containing the found data
     */
    public Set<T> search(Class<?> clazz) {
        return cache.getUnchecked(clazz);
    }


    /**
     * Find all data from a given class, without caching
     * This implements {@link Options#scanSuperClasses()}
     *
     * @param clazz the class to search
     * @return an ImmutableSet of all found data
     */
    private Set<T> find(Class<?> clazz) {
        Set<T> set = new HashSet<>();
        find0(clazz, set);

        if (options.scanSuperClasses()) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                set.addAll(find(superclass));
            }
        }

        return ImmutableSet.copyOf(set);
    }

    /**
     * Abstract method to actually perform finding
     * To avoid repeated set instantiation, we pass a mutable
     * set which the implementation can add its found data to
     *
     * @param clazz The class to search
     * @param addTo a set that all data should be added to
     */
    protected abstract void find0(Class<?> clazz, Set<T> addTo);
}
