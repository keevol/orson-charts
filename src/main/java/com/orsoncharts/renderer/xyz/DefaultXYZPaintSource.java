/* ===========
 * OrsonCharts
 * ===========
 * 
 * (C)opyright 2013 by Object Refinery Limited.
 * 
 */

package com.orsoncharts.renderer.xyz;

import java.awt.Color;
import java.awt.Paint;

/**
 * Default paint source.
 */
public class DefaultXYZPaintSource implements XYZPaintSource {

    private Paint[] paint;
    
    public DefaultXYZPaintSource() {
        this.paint = new Paint[] { Color.RED, Color.BLUE, Color.YELLOW, 
            Color.GRAY, Color.GREEN};    
    }
    
    @Override
    public Paint getPaint(int series, int item) {
        return this.paint[series % this.paint.length];
    }
    
}