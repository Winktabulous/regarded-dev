package mgi.utilities;

import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import mgi.Indice;
import mgi.tools.jagcached.cache.Cache;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionUtils {
    @SafeVarargs
    public static <T, V> Map<V, T> populateMap(final T[] array, final Map<V, T> map, final Function<T, V>... funs) {
        for (final T t : array) {
            for (final Function<T, V> fun : funs) {
                map.put(fun.apply(t), t);
            }
        }
        return map;
    }

    public static <T, V> Map<V, T> populateMap(final T[] array, final Map<V, T> map, final Function<T, V> func) {
        for (final T t : array) {
            map.put(func.apply(t), t);
        }
        return map;
    }

    public static <V, T> void populateObjectListMap(T[] array, Map<V, ObjectList<T>> map, Function<T, V> func) {
        final HashSet<V> keys = Arrays.stream(array).map(func).collect(HashSet::new, HashSet::add, HashSet::addAll);
        keys.forEach(key -> {
            ObjectList<T> values = new ObjectArrayList<>();
            for (T value : array) {
                if (func.apply(value) == key) {
                    values.add(value);
                }
            }
            map.put(key, values);
        });
    }

    public static <T, V> Map<V, T> populateMap(final List<T> list, final Map<V, T> map, final Function<T, V> func) {
        for (final T t : list) {
            map.put(func.apply(t), t);
        }
        return map;
    }

    /**
     * Gets the size of the indice.
     *
     * @param indice indice which to retrieve.
     * @return amount of archives/files (depending on indice type).
     */
    public static int getIndiceSize(final Indice indice, final int archiveId) {
        final Cache cache = CacheManager.getCache();
        if (indice.getArchive() == -1 && archiveId == -1) {
            return cache.getArchive(indice.getIndice()).groupCount();
        }
        return cache.getArchive(indice.getIndice()).findGroupByID(archiveId == -1 ? indice.getArchive() : archiveId).fileCount();
    }

    public static int getIndiceSize(final Indice indice) {
        return getIndiceSize(indice, -1);
    }

    /**
     * Finds the first value in the array that matches the predicate. If none is found, returns null.
     *
     * @param array     the array to loop.
     * @param predicate the predicate to test against each value.
     * @return a matching value.
     */
    public static <T> T findMatching(final T[] array, final Predicate<T> predicate) {
        return findMatching(array, predicate, null);
    }

    /**
     * Finds the first value in the array that matches the predicate. If none is found, returns the default value.
     *
     * @param array        the array to loop.
     * @param predicate    the predicate to test against each value.
     * @param defaultValue the default value to return if no value matches the predicate.
     * @return a matching value.
     */
    public static <T> T findMatching(final T[] array, final Predicate<T> predicate, final T defaultValue) {
        for (int i = 0, length = array.length; i < length; i++) {
            final T object = array[i];
            if (predicate.test(object)) {
                return object;
            }
        }
        return defaultValue;
    }

    public static <T> T findMatching(final Collection<T> list, final Predicate<T> predicate) {
        return findMatching(list, predicate, null);
    }

    public static <T> T findMatching(final Collection<T> list, final Predicate<T> predicate, final T defaultValue) {
        for (final T object : list) {
            if (predicate.test(object)) {
                return object;
            }
        }
        return defaultValue;
    }

    public static <T> T findMatching(final Set<T> list, final Predicate<T> predicate) {
        return findMatching(list, predicate, null);
    }

    public static <T> T findMatching(final Set<T> list, final Predicate<T> predicate, final T defaultValue) {
        for (final T object : list) {
            if (predicate.test(object)) {
                return object;
            }
        }
        return defaultValue;
    }
}
