/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.weltraumschaf.juberblog.jvfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class which infers generic types for collections.
 *
 * @author Sven.Strittmatter
 */
final class JVFSCollections {

    /**
     * Default size for lists.
     */
    private static final int DEFAULT_LIST_SIZE = 10;

    /**
     * Hidden for pure static factory.
     */
    private JVFSCollections() {
        super();
    }

    /**
     * Create new array list with {@link #DEFAULT_LIST_SIZE size}.
     *
     * @param <T> type of list entries
     * @return never {@code null}, always new instance
     */
    public static <T> List<T> newArrayList() {
        return newArrayList(DEFAULT_LIST_SIZE);
    }

    /**
     * Create new array list with given size.
     *
     * @param <T> type of list entries
     * @param size must be non negative
     * @return never {@code null}, always new instance
     */
    public static <T> List<T> newArrayList(final int size) {
        JVFSAssertions.greaterThan(size, -1, "size");
        return new ArrayList<T>(size);
    }

    /**
     * Creates new hash map.
     *
     * @param <K> type of map keys
     * @param <V> type of map values
     * @return never {@code null}, always new instance
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * Creates new hash set.
     * 
     * @param <T> type of set entries
     * @return never {@code null}, always new instance
     */
    public static <T> Set<T> newHashSet() {
        return new HashSet<T>();
    }
}
