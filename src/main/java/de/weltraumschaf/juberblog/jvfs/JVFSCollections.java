/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.weltraumschaf.juberblog.jvfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class which infers generic types for collections.
 *
 * @author Sven.Strittmatter
 */
final class JVFSCollections {

    private JVFSCollections() {
        super();
    }

    public static <T> List<T> newArrayList() {
        return newArrayList(10);
    }

    public static <T> List<T> newArrayList(final int size) {
        return new ArrayList<T>(size);
    }

    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
}
