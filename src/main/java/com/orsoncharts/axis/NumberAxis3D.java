/* ===========
 * OrsonCharts
 * ===========
 * 
 * (C)opyright 2013 by Object Refinery Limited.
 * 
 */

package com.orsoncharts.axis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import com.orsoncharts.util.TextUtils;
import com.orsoncharts.util.TextAnchor;
import com.orsoncharts.Range;
import com.orsoncharts.util.ArgChecks;
import com.orsoncharts.plot.CategoryPlot3D;
import com.orsoncharts.plot.XYZPlot;

/**
 * A numerical axis for use with 3D plots.
 */
public class NumberAxis3D extends AbstractAxis3D implements ValueAxis3D {

    /** A flag that determines whether or not the axis will be drawn. */
    private boolean visible;
    
    /** The axis range. */
    private Range range;

    /** 
     * A flag that controls whether or not the axis range is automatically
     * adjusted to display all of the data items in the dataset.
     */
    private boolean autoAdjustRange;
    
    /** The percentage margin to leave at the lower end of the axis. */
    private double lowerMargin;
    
    /** The percentage margin to leave at the upper end of the axis. */
    private double upperMargin;

    /** 
     * A flag indicating whether or not the auto-range calculation should
     * include zero.  FIXME:  how does this interact with the renderer making
     * a decision about whether or not to include zero?
     */
    private boolean autoRangeIncludesZero;
    
    /**
     * A flag that controls how zero is handled when it falls within the
     * margins.  If <code>true</code>, the margin is truncated at zero, if
     * <code>false</code> the margin is not changed.
     */
    private boolean autoRangeStickyZero;
    
    /** 
     * The default range to apply when there is no data in the dataset and the
     * autoAdjustRange flag is true.  A sensible default is going to depend on
     * the context, so the user should change it as necessary.
     */
    private Range defaultAutoRange;
    
    /** 
     * The tick selector (if not <code>null</code>, then auto-tick selection 
     * is used). 
     */
    private TickSelector tickSelector;

    /** 
     * The tick size (never <code>null</code>).  If the tickSelector is not
     * <code>null</code> then it is used to auto-select an appropriate tick
     * size and format.
     */
    private double tickSize;

    /** The tick formatter (never <code>null</code>). */
    private Format tickLabelFormatter;

    private double tickLabelFactor = 1.4;    

    /** The tick label offset (number of Java2D units). */
    private double tickLabelOffset;
    
    /** The length of tick marks (in Java2D units).  Can be set to 0.0. */
    private double tickMarkLength;
    
    /** The tick mark stroke (never <code>null</code>). */
    private Stroke tickMarkStroke;
    
    /** The tick mark paint (never <code>null</code>). */
    private Paint tickMarkPaint;
    
    /**
     * Creates a new axis with the specified label and default attributes.
     * 
     * @param label  the axis label (<code>null</code> permitted). 
     */
    public NumberAxis3D(String label) {
        this(label, new Range(0.0, 1.0));
    }
    
    /**
     * Creates a new axis with the specified label and range.
     *
     * @param label  the axis label (<code>null</code> permitted).
     * @param range  the range (<code>null</code> not permitted).
     */
    public NumberAxis3D(String label, Range range) {
        super(label);
        this.visible = true;
        this.range = range;
        this.autoAdjustRange = true;
        this.lowerMargin = 0.05;
        this.upperMargin = 0.05;
        this.autoRangeIncludesZero = false;
        this.autoRangeStickyZero = true;
        this.defaultAutoRange = new Range(0.0, 1.0);
        this.tickSelector = new NumberTickSelector();
        this.tickLabelFactor = 1.4;
        this.tickSize = range.getLength() / 10.0;  // FIXME
        this.tickLabelFormatter = new DecimalFormat("0.00");
        this.tickLabelOffset = 5.0;
        this.tickMarkLength = 3.0;
        this.tickMarkStroke = new BasicStroke(0.5f);
        this.tickMarkPaint = Color.GRAY;
    }
  
    /**
     * Returns the flag that determines whether or not the axis is drawn 
     * on the chart.
     * 
     * @return A boolean.
     * 
     * @see #setVisible(boolean) 
     */
    @Override
    public boolean isVisible() {
        return this.visible;
    }
    
    /**
     * Sets the flag that determines whether or not the axis is drawn on the
     * chart and sends an {@link Axis3DChangeEvent} to all registerd listeners.
     * 
     * @param visible  the flag.
     * 
     * @see #isVisible() 
     */
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        fireChangeEvent();
    }
    
    /**
     * Returns the axis range.  You can set the axis range manually or you can
     * rely on the autoAdjustRange feature to set the axis range to match
     * the data being plotted.
     * 
     * @return the axis range (never <code>null</code>).
     */
    @Override
    public Range getRange() {
        return this.range;
    }
  
    /**
     * Sets the axis range (bounds) and sends an {@link Axis3DChangeEvent to 
     * all registered listeners.
     * 
     * @param range  the new range (<code>null</code> not permitted).
     */
    @Override
    public void setRange(Range range) {
        ArgChecks.nullNotPermitted(range, "range");
        this.range = range;
        fireChangeEvent();
    }
    
    /**
     * Returns the flag that controls whether or not the axis range is 
     * automatically updated in response to dataset changes.
     * 
     * @return A boolean. 
     */
    public boolean isAutoAdjustRange() {
        return this.autoAdjustRange;
    }
    
    public void setAutoAdjustRange(boolean autoAdjust) {
        this.autoAdjustRange = autoAdjust;
        fireChangeEvent();
    }
    
    /**
     * Returns the size of the lower margin that is added by the auto-range
     * calculation, as a percentage of the data range.  This margin is used to 
     * prevent data items from being plotted right at the edges of the chart.  
     * The default value is <code>0.05</code> (five percent).
     * 
     * @return The lower margin.
     */
    public double getLowerMargin() {
        return this.lowerMargin;
    }
    
    /**
     * Sets the size of the lower margin that will be added by the auto-range
     * calculation and sends an {@link Axis3DChangeEvent} to all registered
     * listeners.
     * 
     * @param margin  the margin as a percentage of the data range 
     *     (0.05 = five percent).
     * 
     * @see #setUpperMargin(double) 
     */
    public void setLowerMargin(double margin) {
        this.lowerMargin = margin;
        fireChangeEvent();
    }
    
    /**
     * Returns the size of the upper margin that is added by the auto-range
     * calculation, as a percentage of the data range.  This margin is used to 
     * prevent data items from being plotted right at the edges of the chart.  
     * The default value is <code>0.05</code> (five percent).
     * 
     * @return The upper margin.
     */
    public double getUpperMargin() {
        return this.upperMargin;
    }
    
    /**
     * Sets the size of the upper margin that will be added by the auto-range
     * calculation and sends an {@link Axis3DChangeEvent} to all registered
     * listeners.
     * 
     * @param margin  the margin as a percentage of the data range 
     *     (0.05 = five percent).
     * 
     * @see #setLowerMargin(double) 
     */
    public void setUpperMargin(double margin) {
        this.upperMargin = margin;
        fireChangeEvent();
    }
    
    public boolean getAutoRangeIncludesZero() {
        return this.autoRangeIncludesZero;
    }
    
    public void setAutoRangeIncludeZero(boolean include) {
        this.autoRangeIncludesZero = include;
        fireChangeEvent();
    }
    
    public boolean getAutoRangeStickyZero() {
        return this.autoRangeStickyZero;
    }
    
    public void setAutoRangeStickyZero(boolean sticky) {
        this.autoRangeStickyZero = sticky;
        fireChangeEvent();
    }
    
    /**
     * Returns the default range used when the <code>autoAdjustRange</code>
     * flag is <code>true</code> but the dataset contains no values.  The
     * default range is <code>(0.0 to 1.0)</code>, depending on the context
     * you may want to change this.
     * 
     * @return The default range (never <code>null</code>).
     * 
     * @see #setDefaultAutoRange(com.orsoncharts.Range) 
     */
    public Range getDefaultAutoRange() {
        return this.defaultAutoRange;
    }
    
    /**
     * Sets the default range used  when the <code>autoAdjustRange</code>
     * flag is <code>true</code> but the dataset contains no values, and sends
     * an {@link Axis3DChangeEvent} to all registered listeners.
     * 
     * @param range  the range (<code>null</code> not permitted).
     *
     * @see #getDefaultAutoRange() 
     */
    public void setDefaultAutoRange(Range range) {
        this.defaultAutoRange = range;
        fireChangeEvent();
    }
  
    public TickSelector getTickSelector() {
        return this.tickSelector;    
    }
    
    public void setTickSelector(TickSelector selector) {
        ArgChecks.nullNotPermitted(selector, "selector");
        this.tickSelector = selector;
        fireChangeEvent();
    }
    
    /**
     * Returns the tick size.
     * 
     * @return The tick size.
     */
    public double getTickSize() {
        return this.tickSize;
    }

    /**
     * Sets the tick size and sends a change event to all registered listeners.
     * 
     * @param tickSize  the new tick size.
     */
    public void setTickSize(double tickSize) {
        this.tickSize = tickSize;
        fireChangeEvent();
    }
    
    /**
     * Returns the tick label formatter.
     * 
     * @return The tick label formatter (never <code>null</code>). 
     */
    public Format getTickLabelFormatter() {
        return this.tickLabelFormatter;
    }
    
    /**
     * Sets the formatter for the tick labels and sends an 
     * {@link Axis3DChangeEvent} to all registered listeners.
     * 
     * @param formatter  the formatter (<code>null</code> not permitted).
     */
    public void setTickLabelFormatter(Format formatter) {
        ArgChecks.nullNotPermitted(formatter, "formatter");
        this.tickLabelFormatter = formatter;
        fireChangeEvent();
    }
    
    public double getTickLabelFactor() {
        return this.tickLabelFactor;
    }
    
    public void setTickLabelFactor(double factor) {
        this.tickLabelFactor = factor;
        fireChangeEvent();
    }
    
    public double getTickLabelOffset() {
        return this.tickLabelOffset;
    }
    
    public void setTickLabelOffset(double offset) {
        this.tickLabelOffset = offset;
    }
    
    public double getTickMarkLength() {
        return this.tickMarkLength;
    }
    
    public void setTickMarkLength(double length) {
        this.tickMarkLength = length;
        fireChangeEvent();
    }

    public Stroke getTickMarkStroke() {
        return this.tickMarkStroke;
    }
    
    public void setTickMarkStroke(Stroke stroke) {
        ArgChecks.nullNotPermitted(stroke, "stroke");
        this.tickMarkStroke = stroke;
        fireChangeEvent();
    }
    
    public Paint getTickMarkPaint() {
        return this.tickMarkPaint;
    }
    
    public void setTickMarkPaint(Paint paint) {
        ArgChecks.nullNotPermitted(paint, "paint");
        this.tickMarkPaint = paint;
        fireChangeEvent();
    }

    /**
     * Adjusts the range by adding the lower and upper margins and taking into
     * account any other settings.
     * 
     * @param range  the range (<code>null</code> not permitted).
     * @return 
     */
    private Range adjustedDataRange(Range range) {
        ArgChecks.nullNotPermitted(range, "range");
        double lm = range.getLength() * this.lowerMargin;
        double um = range.getLength() * this.upperMargin;
        double lowerBound = range.getMin() - lm;
        double upperBound = range.getMax() + um;
        // does zero fall in the margins
        if (this.autoRangeStickyZero) {
            if (0.0 <= range.getMin() && 0.0 > lowerBound) {
                lowerBound = 0.0;
            }
            if (0.0 >= range.getMax() && 0.0 < upperBound) {
                upperBound = 0.0;
            }
        }
        return new Range(lowerBound, upperBound);
    }
    
    @Override
    public void configureAsValueAxis(CategoryPlot3D plot) {
        if (this.autoAdjustRange) {
            Range valueRange = plot.getRenderer().findValueRange(
                    plot.getDataset());
            if (valueRange != null) {
                this.range = adjustedDataRange(valueRange);
            } else {
                this.range = this.defaultAutoRange;
            }
        }
    }
    
    @Override
    public void configureAsXAxis(XYZPlot plot) {
        if (this.autoAdjustRange) {
            Range xRange = plot.getRenderer().findXRange(plot.getDataset());
            if (xRange != null) {
                this.range = adjustedDataRange(xRange);
            } else {
                this.range = this.defaultAutoRange;
            }
        }
    }

    @Override
    public void configureAsYAxis(XYZPlot plot) {
        if (this.autoAdjustRange) {
            Range yRange = plot.getRenderer().findYRange(plot.getDataset());
            if (yRange != null) {
                this.range = adjustedDataRange(yRange);
            } else {
                this.range = this.defaultAutoRange;
            }
        }
    }

    @Override
    public void configureAsZAxis(XYZPlot plot) {
        if (this.autoAdjustRange) {
            Range zRange = plot.getRenderer().findZRange(plot.getDataset());
            if (zRange != null) {
                this.range = adjustedDataRange(zRange);
            } else {
                this.range = this.defaultAutoRange;
            }
        }
    }
    
    /**
     * Draws the axis using the supplied graphics device, with the
     * specified starting and ending points for the line.
     *
     * @param g2
     * @param pt0
     * @param pt1
     * @param opposingPt
     * @param tickData
     */
    @Override
    public void draw(Graphics2D g2, Point2D pt0, Point2D pt1, 
            Point2D opposingPt, boolean labels, List<TickData> tickData) {
        
        if (!isVisible()) {
            return;
        }
        
        // draw a line for the axis
        g2.setStroke(getLineStroke());
        g2.setPaint(getLineColor());
        Line2D axisLine = new Line2D.Float(pt0, pt1);  
        g2.draw(axisLine);
        
        // draw the tick marks and labels
        double maxTickLabelWidth = 0.0;
        for (TickData t : tickData) {
            Line2D perpLine = createPerpendicularLine(axisLine, 
                    t.getAnchorPt(), this.tickMarkLength 
                    + this.tickLabelOffset, opposingPt);
            
            if (this.tickMarkLength > 0.0) {
                Line2D tickLine = createPerpendicularLine(axisLine, 
                       t.getAnchorPt(), this.tickMarkLength, 
                       opposingPt);
                g2.setPaint(this.tickMarkPaint);
                g2.setStroke(this.tickMarkStroke);
                g2.draw(tickLine);
            }
            
            if (getTickLabelsVisible()) {
                double theta = calculateTheta(axisLine);
                double thetaAdj = theta + Math.PI / 2.0;
                if (thetaAdj < -Math.PI / 2.0) {
                    thetaAdj = thetaAdj + Math.PI;
                }
                if (thetaAdj > Math.PI / 2.0) {
                    thetaAdj = thetaAdj - Math.PI;
                }

                double perpTheta = calculateTheta(perpLine);  
                TextAnchor textAnchor = TextAnchor.CENTER_LEFT;
                if (Math.abs(perpTheta) > Math.PI / 2.0) {
                    textAnchor = TextAnchor.CENTER_RIGHT;
                } 
                g2.setFont(getTickLabelFont());
                g2.setPaint(getTickLabelPaint());
                String tickLabel = this.tickLabelFormatter.format(
                        t.getDataValue());
                maxTickLabelWidth = Math.max(maxTickLabelWidth, 
                        g2.getFontMetrics().stringWidth(tickLabel));
                TextUtils.drawRotatedString(tickLabel, g2, 
                        (float) perpLine.getX2(), (float) perpLine.getY2(), 
                        textAnchor, thetaAdj, textAnchor);
            }
        }

        // draw the axis label (if any)...
        if (getLabel() != null) {
            g2.setFont(getLabelFont());
            g2.setPaint(getLabelPaint());
            Line2D labelPosLine = createPerpendicularLine(axisLine, 0.5, 
                    this.tickMarkLength + this.tickLabelOffset 
                    + maxTickLabelWidth + 10.0, 
                    opposingPt);
            double theta = calculateTheta(axisLine);
            if (theta < -Math.PI / 2.0) {
                theta = theta + Math.PI;
            }
            if (theta > Math.PI / 2.0) {
                theta = theta - Math.PI;
            }
            TextUtils.drawRotatedString(getLabel(), g2, 
                    (float) labelPosLine.getX2(), 
                    (float) labelPosLine.getY2(), TextAnchor.CENTER, theta, 
                    TextAnchor.CENTER);
        }
    }

    @Override
    public double translateToWorld(double value, double length) {
        return length * (value - this.range.getMin()) / this.range.getLength();
    }
  
    /**
     * Tests this instance for equality with an arbitrary object.
     * 
     * @param obj  the object to test against (<code>null</code> permitted).
     * 
     * @return A boolean. 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NumberAxis3D)) {
            return false;
        }
        NumberAxis3D that = (NumberAxis3D) obj;
        if (this.visible != that.visible) {
            return false;
        }
        if (!this.range.equals(that.range)) {
            return false;
        }
        if (this.tickSize != that.tickSize) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.range);
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.tickSize) 
                ^ (Double.doubleToLongBits(this.tickSize) >>> 32));
        hash = 59 * hash + Objects.hashCode(this.tickLabelFormatter);
        return hash;
    }

    /**
     * Selects a tick size that is appropriate for drawing the axis from
     * <code>pt0</code> to <code>pt1</code>.
     * 
     * @param g2
     * @param pt0
     * @param pt1
     * @param opposingPt 
     */
    @Override
    public double selectTick(Graphics2D g2, Point2D pt0, Point2D pt1, 
            Point2D opposingPt) {
        
        if (this.tickSelector == null) {
            return Double.NaN; // this should not happen :)
        }
        
        // based on the font height, we can determine roughly how many tick
        // labels will fit in the length available
        double length = pt0.distance(pt1);
        g2.setFont(getTickLabelFont());
        int height = g2.getFontMetrics(getTickLabelFont()).getHeight();
        // the tickLabelFactor allows some control over how dense the labels
        // will be
        int maxTicks = (int) (length / (height * this.tickLabelFactor));
        System.out.println(this.getLabel() + ", " +height + ", " + length + ", " + maxTicks);
        if (maxTicks > 2 && this.tickSelector != null) {
            this.tickSelector.select(this.range.getLength() / 2.0);
            // step through until we have too many ticks OR we run out of 
            // tick sizes
            int tickCount = (int) (this.range.getLength() 
                    / this.tickSelector.getCurrentTickSize());
            while (tickCount < maxTicks) {
                this.tickSelector.previous();
                tickCount = (int) (this.range.getLength() 
                        / this.tickSelector.getCurrentTickSize());
            }
            this.tickSelector.next();
            this.tickSize = this.tickSelector.getCurrentTickSize();
            this.tickLabelFormatter 
                    = this.tickSelector.getCurrentTickLabelFormat();
        } else {
            this.tickSize = Double.NaN;
        }
        return this.tickSize;
    }

    @Override
    public List<TickData> generateTickData(double tickUnit) {
        List<TickData> result = new ArrayList<TickData>();
        if (Double.isNaN(tickUnit)) {
            result.add(new TickData(0, getRange().getMin()));
            result.add(new TickData(1, getRange().getMax()));
        } else {
            double x = this.range.firstStandardTickValue(tickUnit);
            while (x < this.range.getMax()) {
                result.add(new TickData(this.range.percent(x), x));
                x += tickUnit;
            }
        }
        return result;
    }

}
