/*
 * (#) net.wisestone.android.util.Lists.java
 * Created on 2011. 7. 19. 
 */
package net.wisestone.android.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 *
 * @author ms29.seo@hotmail.com
 * @version 0.1
 */
public final class Lists
{

    /**
     * 
     */
    private Lists() {
    }
    
    /**
     * Creates an empty {@code ArrayList} instance.
     *
     * <p><b>Note:</b> if you only need an <i>immutable</i> empty List, use
     * {@link Collections#emptyList} instead.
     *
     * @return a newly-created, initially-empty {@code ArrayList}
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Creates a resizable {@code ArrayList} instance containing the given
     * elements.
     *
     * <p><b>Note:</b> due to a bug in javac 1.5.0_06, we cannot support the
     * following:
     *
     * <p>{@code List<Base> list = Lists.newArrayList(sub1, sub2);}
     *
     * <p>where {@code sub1} and {@code sub2} are references to subtypes of
     * {@code Base}, not of {@code Base} itself. To get around this, you must
     * use:
     *
     * <p>{@code List<Base> list = Lists.<Base>newArrayList(sub1, sub2);}
     *
     * @param elements the elements that the list should contain, in order
     * @return a newly-created {@code ArrayList} containing those elements
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        int capacity = (elements.length * 110) / 100 + 5;
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

}
