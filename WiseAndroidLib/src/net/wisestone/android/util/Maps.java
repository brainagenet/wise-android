/*
 * (#) net.wisestone.android.util.Maps.java
 * Created on 2011. 7. 19. 
 */
package net.wisestone.android.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 
 *
 * @author ms29.seo@hotmail.com
 * @version 0.1
 */
public final class Maps
{

    private Maps() {
    }

    /**
     * Creates a {@code HashMap} instance.
     *
     * @return a newly-created, initially-empty {@code HashMap}
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * Creates a {@code LinkedHashMap} instance.
     *
     * @return a newly-created, initially-empty {@code HashMap}
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

}
