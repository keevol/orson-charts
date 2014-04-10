/* ============
 * Orson Charts
 * ============
 * 
 * (C)opyright 2013, 2014, by Object Refinery Limited.
 * 
 * http://www.object-refinery.com/orsoncharts/index.html
 * 
 * Redistribution of this source file is prohibited.
 * 
 */

package com.orsoncharts.data;

import java.io.Serializable;
import com.orsoncharts.util.ArgChecks;

/**
 * An object that references one data item in a {@link KeyedValues3D} data
 * structure.
 * 
 * @since 1.3
 */
public class KeyedValuesItemKey implements ItemKey, Serializable {
    
    /** The key for the item. */
    Comparable<? extends Object> key;
    
    /**
     * Creates a new instance.
     * 
     * @param key  the key (<code>null</code> not permitted).
     */
    public KeyedValuesItemKey(Comparable<? extends Object> key) {
        ArgChecks.nullNotPermitted(key, "key");
        this.key = key;
    }
    
    /**
     * Returns the key.
     * 
     * @return The key (never <code>null</code>). 
     */
    public Comparable<?> getKey() {
        return this.key;
    }
    
    /**
     * Tests this instance for equality with an arbitrary object.
     * 
     * @param obj  the object (<code>null</code> not permitted).
     * 
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KeyedValuesItemKey)) {
            return false;
        }
        KeyedValuesItemKey that = (KeyedValuesItemKey) obj;
        if (!this.key.equals(that.key)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toJSONString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"key\": \"").append(this.key.toString()).append("\"}");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ValuesItemKey[");
        sb.append(this.key.toString());
        sb.append("]");
        return sb.toString();
    }
}