/* ===========
 * OrsonCharts
 * ===========
 * 
 * (C)opyright 2013 by Object Refinery Limited.
 * 
 */

package com.orsoncharts.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import com.orsoncharts.axis.Axis3DChangeEvent;
import com.orsoncharts.axis.Axis3DChangeListener;
import com.orsoncharts.axis.ValueAxis3D;
import com.orsoncharts.data.Dataset3DChangeEvent;
import com.orsoncharts.data.Dataset3DChangeListener;
import com.orsoncharts.data.xyz.XYZDataset;
import com.orsoncharts.renderer.xyz.XYZRenderer;
import com.orsoncharts.util.ArgChecks;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.World;
import com.orsoncharts.legend.LegendItemInfo;
import com.orsoncharts.legend.StandardLegendItemInfo;
import com.orsoncharts.renderer.Renderer3DChangeEvent;
import com.orsoncharts.renderer.Renderer3DChangeListener;
import com.orsoncharts.util.ObjectUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * An XYZ plot.
 */
public class XYZPlot extends AbstractPlot3D implements Dataset3DChangeListener, 
        Axis3DChangeListener, Renderer3DChangeListener {

    private static Stroke DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5f, 
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, 
            new float[] { 3f, 3f }, 0f);

    /** The dataset. */
    private XYZDataset dataset;

    /** The renderer. */
    private XYZRenderer renderer;
  
    /** The x-axis. */
    private ValueAxis3D xAxis;

    /** The y-axis. */
    private ValueAxis3D yAxis;
  
    /** The z-axis. */
    private ValueAxis3D zAxis;
    
    /** Are gridlines visible for the x-axis? */
    private boolean gridlinesVisibleX;
    
    /** The paint for the x-axis gridlines. */
    private Paint gridlinePaintX;

    /** The stroke for the x-axis gridlines. */
    private Stroke gridlineStrokeX;
    
    /** Are gridlines visible for the y-axis? */
    private boolean gridlinesVisibleY;

    /** The paint for the y-axis gridlines. */
    private Paint gridlinePaintY;
    
    /** The stroke for the y-axis gridlines. */
    private Stroke gridlineStrokeY;
    
    /** Are gridlines visible for the z-axis? */
    private boolean gridlinesVisibleZ;

    /** The paint for the z-axis gridlines. */
    private Paint gridlinePaintZ;

    /** The stroke for the z-axis gridlines. */
    private Stroke gridlineStrokeZ;
    
    /**
     * Creates a new plot with the specified axes.
     * 
     * @param dataset  the dataset (<code>null</code> not permitted).
     * @param renderer  the renderer (<code>null</code> not permitted).
     * @param xAxis  the x-axis (<code>null</code> not permitted).
     * @param yAxis  the y-axis (<code>null</code> not permitted).
     * @param zAxis  the z-axis (<code>null</code> not permitted).
     */
    public XYZPlot(XYZDataset dataset, XYZRenderer renderer, ValueAxis3D xAxis, 
            ValueAxis3D yAxis, ValueAxis3D zAxis) {
        ArgChecks.nullNotPermitted(dataset, "dataset");
        ArgChecks.nullNotPermitted(renderer, "renderer");
        ArgChecks.nullNotPermitted(xAxis, "xAxis");
        ArgChecks.nullNotPermitted(yAxis, "yAxis");
        ArgChecks.nullNotPermitted(zAxis, "zAxis");
        this.dimensions = new Dimension3D(10, 10, 10);
        this.dataset = dataset;
        this.dataset.addChangeListener(this);
        this.renderer = renderer;
        this.renderer.setPlot(this);
        this.renderer.addChangeListener(this);
        this.xAxis = xAxis;
        this.xAxis.addChangeListener(this);
        this.xAxis.configureAsXAxis(this);
        this.yAxis = yAxis;
        this.yAxis.addChangeListener(this);
        this.yAxis.configureAsYAxis(this);
        this.zAxis = zAxis;
        this.zAxis.addChangeListener(this);
        this.zAxis.configureAsZAxis(this);
        this.gridlinesVisibleX = true;
        this.gridlinePaintX = Color.WHITE;
        this.gridlineStrokeX = DEFAULT_GRIDLINE_STROKE;
        this.gridlinesVisibleY = true;
        this.gridlinePaintY = Color.WHITE;
        this.gridlineStrokeY = DEFAULT_GRIDLINE_STROKE;
        this.gridlinesVisibleZ = true;
        this.gridlinePaintZ = Color.WHITE;
        this.gridlineStrokeZ = DEFAULT_GRIDLINE_STROKE;
    }
  
    /**
     * Sets the dimensions for the plot and notifies registered listeners that
     * the plot dimensions have been changed.
     * 
     * @param dim  the new dimensions (<code>null</code> not permitted).
     */
    public void setDimensions(Dimension3D dim) {
        ArgChecks.nullNotPermitted(dim, "dim");
        this.dimensions = dim;
        fireChangeEvent();
    }

    /**
     * Returns the dataset for the plot.
     * 
     * @return The dataset (never <code>null</code>). 
     */
    public XYZDataset getDataset() {
        return this.dataset;
    }

    /**
     * Sets the dataset and sends a change event notification to all registered
     * listeners.
     * 
     * @param dataset  the new dataset (<code>null</code> not permitted).
     */
    public void setDataset(XYZDataset dataset) {
        ArgChecks.nullNotPermitted(dataset, "dataset");
        this.dataset.removeChangeListener(this);
        this.dataset = dataset;
        this.dataset.addChangeListener(this);
        fireChangeEvent();
    }

    /**
     * Returns the x-axis.
     * 
     * @return The x-axis (never <code>null</code>). 
     */
    public ValueAxis3D getXAxis() {
        return this.xAxis;
    }

    /**
     * Sets the x-axis and sends a {@link Plot3DChangeEvent} to all registered
     * listeners.
     * 
     * @param xAxis  the x-axis (<code>null</code> not permitted). 
     */
    public void setXAxis(ValueAxis3D xAxis) {
        ArgChecks.nullNotPermitted(xAxis, "xAxis");
        this.xAxis = xAxis;
        fireChangeEvent();
    }

    /**
     * Returns the y-axis.
     * 
     * @return The y-axis (never <code>null</code>). 
     */
    public ValueAxis3D getYAxis() {
        return this.yAxis;
    }

    /**
     * Sets the y-axis and sends a {@link Plot3DChangeEvent} to all registered
     * listeners.
     * 
     * @param yAxis  the y-axis (<code>null</code> not permitted). 
     */
    public void setYAxis(ValueAxis3D yAxis) {
        ArgChecks.nullNotPermitted(yAxis, "yAxis");
        this.yAxis = yAxis;
        fireChangeEvent();
    }
    
    /**
     * Returns the z-axis.
     * 
     * @return The z-axis (never <code>null</code>). 
     */
    public ValueAxis3D getZAxis() {
        return this.zAxis;
    }

    /**
     * Sets the z-axis and sends a {@link Plot3DChangeEvent} to all registered
     * listeners.
     * 
     * @param zAxis  the z-axis (<code>null</code> not permitted). 
     */
    public void setZAxis(ValueAxis3D zAxis) {
        ArgChecks.nullNotPermitted(zAxis, "zAxis");
        this.zAxis = zAxis;
        fireChangeEvent();
    }
    
    /**
     * Returns the renderer for the plot.
     * 
     * @return The renderer (possibly <code>null</code>).
     */
    public XYZRenderer getRenderer() {
        return this.renderer;
    }

    /**
     * Sets the renderer for the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     * 
     * @param renderer  the renderer (<code>null</code> not permitted). 
     */
    public void setRenderer(XYZRenderer renderer) {
        this.renderer.setPlot(null);;
        this.renderer.removeChangeListener(this);
        this.renderer = renderer;
        this.renderer.setPlot(this);
        this.renderer.addChangeListener(this);
        fireChangeEvent();
    }

    /**
     * Returns the flag that controls whether or not gridlines are shown for
     * the x-axis.
     * 
     * @return A boolean. 
     */
    public boolean isGridlinesVisibleX() {
        return this.gridlinesVisibleX;
    }

    /**
     * Sets the flag that controls whether or not gridlines are shown for the
     * x-axis and sends a {@link Plot3DChangeEvent} to all registered
     * listeners.
     * 
     * @param visible  the new flag value.
     */
    public void setGridlinesVisibleX(boolean visible) {
        this.gridlinesVisibleX = visible;
        fireChangeEvent();
    }
    
    /**
     * Returns the paint used to draw the gridlines for the x-axis.
     * 
     * @return The paint (<code>null</code> not permitted). 
     */
    public Paint getGridlinePaintX() {
        return this.gridlinePaintX;
    }
    
    /**
     * Sets the paint used to draw the gridlines for the x-axis, and sends 
     * a {@link Plot3DChangeEvent} to all registered listeners.
     * 
     * @param paint  the paint (<code>null</code> not permitted).
     */
    public void setGridlinePaintX(Paint paint) {
        ArgChecks.nullNotPermitted(paint, "paint");
        this.gridlinePaintX = paint;
        fireChangeEvent();
    }
    
    /**
     * Returns the stroke used to draw the gridlines for the x-axis.
     * 
     * @return The stroke (<code>null</code> not permitted). 
     */
    public Stroke getGridlineStrokeX() {
        return this.gridlineStrokeX;
    }

    /**
     * Sets the stroke used to draw the gridlines for the x-axis, and sends 
     * a {@link Plot3DChangeEvent} to all registered listeners.
     * 
     * @param stroke  the stroke (<code>null</code> not permitted).
     */
    public void setGridlineStrokeX(Stroke stroke) {
        ArgChecks.nullNotPermitted(stroke, "stroke");
        this.gridlineStrokeX = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the flag that controls whether or not gridlines are shown for
     * the y-axis.
     * 
     * @return A boolean. 
     */
    public boolean isGridlinesVisibleY() {
        return this.gridlinesVisibleY;
    }
    
    /**
     * Sets the flag that controls whether or not gridlines are shown for the
     * y-axis and sends a {@link Plot3DChangeEvent} to all registered
     * listeners.
     * 
     * @param visible  the new flag value.
     */
    public void setGridlinesVisibleY(boolean visible) {
        this.gridlinesVisibleY = visible;
        fireChangeEvent();
    }

    /**
     * Returns the paint used to draw the gridlines for the y-axis.
     * 
     * @return The paint (<code>null</code> not permitted). 
     */
    public Paint getGridlinePaintY() {
        return this.gridlinePaintY;
    }
    
    /**
     * Sets the paint used to draw the gridlines for the y-axis, and sends 
     * a {@link Plot3DChangeEvent} to all registered listeners.
     * 
     * @param paint  the paint (<code>null</code> not permitted).
     */
    public void setGridlinePaintY(Paint paint) {
        ArgChecks.nullNotPermitted(paint, "paint");
        this.gridlinePaintY = paint;
        fireChangeEvent();
    }

    /**
     * Returns the stroke used to draw the gridlines for the y-axis.
     * 
     * @return The stroke (<code>null</code> not permitted). 
     */
    public Stroke getGridlineStrokeY() {
        return this.gridlineStrokeY;
    }
    
    /**
     * Sets the stroke used to draw the gridlines for the y-axis, and sends 
     * a {@link Plot3DChangeEvent} to all registered listeners.
     * 
     * @param stroke  the stroke (<code>null</code> not permitted).
     */
    public void setGridlineStrokeY(Stroke stroke) {
        ArgChecks.nullNotPermitted(stroke, "stroke");
        this.gridlineStrokeY = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the flag that controls whether or not gridlines are shown for
     * the z-axis.
     * 
     * @return A boolean. 
     */
    public boolean isGridlinesVisibleZ() {
        return this.gridlinesVisibleZ;
    }
    
    /**
     * Sets the flag that controls whether or not gridlines are shown for the
     * z-axis and sends a {@link Plot3DChangeEvent} to all registered
     * listeners.
     * 
     * @param visible  the new flag value.
     */
    public void setGridlinesVisibleZ(boolean visible) {
        this.gridlinesVisibleZ = visible;
        fireChangeEvent();
    }
    
    /**
     * Returns the paint used to draw the gridlines for the z-axis.
     * 
     * @return The paint (<code>null</code> not permitted). 
     */
    public Paint getGridlinePaintZ() {
        return this.gridlinePaintZ;
    }    
    
    /**
     * Sets the paint used to draw the gridlines for the z-axis, and sends 
     * a {@link Plot3DChangeEvent} to all registered listeners.
     * 
     * @param paint  the paint (<code>null</code> not permitted).
     */
    public void setGridlinePaintZ(Paint paint) {
        ArgChecks.nullNotPermitted(paint, "paint");
        this.gridlinePaintZ = paint;
        fireChangeEvent();
    }

    /**
     * Returns the stroke used to draw the gridlines for the z-axis.
     * 
     * @return The stroke (<code>null</code> not permitted). 
     */
    public Stroke getGridlineStrokeZ() {
        return this.gridlineStrokeZ;
    }
    
    /**
     * Sets the stroke used to draw the gridlines for the z-axis, and sends 
     * a {@link Plot3DChangeEvent} to all registered listeners.
     * 
     * @param stroke  the stroke (<code>null</code> not permitted).
     */
    public void setGridlineStrokeZ(Stroke stroke) {
        ArgChecks.nullNotPermitted(stroke, "stroke");
        this.gridlineStrokeZ = stroke;
        fireChangeEvent();
    }

    /**
     * Returns a list containing legend item info, typically one item for
     * each series in the chart.  This is intended for use in the construction
     * of a chart legend.
     * 
     * @return A list containing legend item info.
     */
    @Override
    public List<LegendItemInfo> getLegendInfo() {
        List<LegendItemInfo> result = new ArrayList<LegendItemInfo>();
        List<Comparable> keys = this.dataset.getSeriesKeys();
        for (Comparable key : keys) {
            int series = this.dataset.getSeriesIndex(key);
            Paint paint = this.renderer.getPaintSource().getLegendPaint(series);
            LegendItemInfo info = new StandardLegendItemInfo(key, 
                    key.toString(), paint);
            result.add(info);
        }
        return result;
    }

    @Override
    public void composeToWorld(World world, double xOffset, double yOffset, 
            double zOffset) {
        // for each data point in the dataset
        // figure out if the composed shape intersects with the visible subset
        // of the world, and if so add the object
        int seriesCount = this.dataset.getSeriesCount();
        for (int series = 0; series < seriesCount; series++) {
            int itemCount = this.dataset.getItemCount(series);
            for (int item = 0; item < itemCount; item++) {
                this.renderer.composeItem(this.dataset, series, item, world, 
                this.dimensions, xOffset, yOffset, zOffset);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYZPlot)) {
            return false;
        }
        XYZPlot that = (XYZPlot) obj;
        if (!this.dimensions.equals(that.dimensions)) {
            return false;
        }
        if (this.gridlinesVisibleX != that.gridlinesVisibleX) {
            return false;
        }
        if (this.gridlinesVisibleY != that.gridlinesVisibleY) {
            return false;
        }
        if (this.gridlinesVisibleZ != that.gridlinesVisibleZ) {
            return false;
        }
        if (!ObjectUtils.equalsPaint(this.gridlinePaintX, that.gridlinePaintX)) {
            return false;
        }
        if (!ObjectUtils.equalsPaint(this.gridlinePaintY, that.gridlinePaintY)) {
            return false;
        }
        if (!ObjectUtils.equalsPaint(this.gridlinePaintZ, that.gridlinePaintZ)) {
            return false;
        }
        if (!this.gridlineStrokeX.equals(that.gridlineStrokeX)) {
            return false;
        }
        if (!this.gridlineStrokeY.equals(that.gridlineStrokeY)) {
            return false;
        }
        if (!this.gridlineStrokeZ.equals(that.gridlineStrokeZ)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public void axisChanged(Axis3DChangeEvent event) {
        fireChangeEvent();
    }

    @Override
    public void rendererChanged(Renderer3DChangeEvent event) {
        fireChangeEvent();
    }

    @Override
    public void datasetChanged(Dataset3DChangeEvent event) {
        this.xAxis.configureAsXAxis(this);
        this.yAxis.configureAsYAxis(this);
        this.zAxis.configureAsZAxis(this);
        super.datasetChanged(event);
    }
    
}
