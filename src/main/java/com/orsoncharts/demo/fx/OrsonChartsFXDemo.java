/* ===================
 * Orson Charts - Demo
 * ===================
 * 
 * Copyright (c) 2013, 2014, Object Refinery Limited.
 * All rights reserved.
 *
 * http://www.object-refinery.com/orsoncharts/index.html
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   - Neither the name of the Object Refinery Limited nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL OBJECT REFINERY LIMITED BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Note that the above terms apply to the demo source only, and not the 
 * Orson Charts library.
 * 
 */

package com.orsoncharts.demo.fx;

import static javafx.application.Application.launch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.orsoncharts.Chart3D;
import com.orsoncharts.data.Dataset3D;
import com.orsoncharts.data.category.CategoryDataset3D;
import com.orsoncharts.demo.AreaChart3D1;
import com.orsoncharts.demo.DemoDescription;
import com.orsoncharts.fx.Chart3DCanvas;
import com.orsoncharts.fx.Chart3DViewer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

/**
 * Demo application for Orson Charts in JavaFX.
 */
public class OrsonChartsFXDemo extends Application {

    private static final String PREFIX = "com.orsoncharts.demo.fx.";
    
    private Map<String, DemoDescription> descriptions;
    
    private SplitPane splitter;
        
    private WebView chartDescription;

    /**
     * Creates a new demo instance.
     */
    public OrsonChartsFXDemo() {
        super();    
        this.descriptions = new HashMap<String, DemoDescription>();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
       
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab();
        tab1.setText("Demos");
        tab1.setClosable(false);
        
        SplitPane sp = new SplitPane();
        final StackPane sp1 = new StackPane();
        sp1.getChildren().add(createTreeView());
        final BorderPane sp2 = new BorderPane();
        sp2.setCenter(createChartPane());
 
        sp.getItems().addAll(sp1, sp2);
        sp.setDividerPositions(0.3f, 0.6f);
        tab1.setContent(sp);
        tabPane.getTabs().add(tab1);        
 
        Tab tab2 = new Tab();
        tab2.setText("About");
        tab2.setClosable(false);
        
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(getClass().getResource("about.html").toString());
        tab2.setContent(browser);
        tabPane.getTabs().add(tab2);        

        Scene scene = new Scene(tabPane, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("Orson Charts Demo for JavaFX");
        stage.show();
    }
    
    private StackPane createTreeView() {
        TreeItem<String> rootItem = new TreeItem<String> ("Orson Charts", null);
        rootItem.setExpanded(true);
        TreeItem<String> categoryChartsNode = new TreeItem<String>("Category Charts");
        categoryChartsNode.setExpanded(true);
        rootItem.getChildren().add(categoryChartsNode);
        
        DemoDescription d = new DemoDescription(PREFIX + "AreaChart3DFXDemo1", "AreaChart3DFXDemo1.java", "../AreaChart3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        TreeItem<String> n = new TreeItem<String>(d.getFileName());
        categoryChartsNode.getChildren().add(n);
        
        d = new DemoDescription(PREFIX + "AreaChart3DFXDemo2", "AreaChart3DFXDemo2.java", "../AreaChart3DDemo2.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        categoryChartsNode.getChildren().add(n);
        
        d = new DemoDescription(PREFIX + "BarChart3DFXDemo1", "BarChart3DFXDemo1.java", "../BarChart3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        categoryChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "BarChart3DFXDemo2", "BarChart3DFXDemo2.java", "../BarChart3DDemo2.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        categoryChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "LineChart3DFXDemo1", "LineChart3DFXDemo1.java", "../LineChart3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        categoryChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "LineChart3DFXDemo2", "LineChart3DFXDemo2.java", "../LineChart3DDemo2.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        categoryChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "StackedBarChart3DFXDemo1", "StackedBarChart3DFXDemo1.java", "../StackedBarChart3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        categoryChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "StackedBarChart3DFXDemo2", "StackedBarChart3DFXDemo2.java", "../StackedBarChart3DDemo2.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        categoryChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "StackedBarChart3DFXDemo3", "StackedBarChart3DFXDemo3.java", "../StackedBarChart3DDemo3.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        categoryChartsNode.getChildren().add(n);

        TreeItem<String> pieChartsNode = new TreeItem<String>("Pie Charts");
        rootItem.getChildren().add(pieChartsNode);
        
        d = new DemoDescription(PREFIX + "PieChart3DFXDemo1", "PieChart3DFXDemo1.java", "../PieChart3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        pieChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "PieChart3DFXDemo2", "PieChart3DFXDemo2.java", "../PieChart3DDemo2.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        pieChartsNode.getChildren().add(n);

        TreeItem<String> xyzChartsNode = new TreeItem<String>("XYZ Charts");
        rootItem.getChildren().add(xyzChartsNode);

        d = new DemoDescription(PREFIX + "RangeMarkerFXDemo1", "RangeMarkerFXDemo1.java", "../RangeMarkerDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        xyzChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "ScatterPlot3DFXDemo1", "ScatterPlot3DFXDemo1.java", "../ScatterPlot3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        xyzChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "ScatterPlot3DFXDemo2", "ScatterPlot3DFXDemo2.java", "../ScatterPlot3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        xyzChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "ScatterPlot3DFXDemo3", "ScatterPlot3DFXDemo3.java", "../ScatterPlot3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        xyzChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "SurfaceRendererFXDemo1", "SurfaceRendererFXDemo1.java", "../SurfaceRendererDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        xyzChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "SurfaceRendererFXDemo2", "SurfaceRendererFXDemo2.java", "../SurfaceRendererDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        xyzChartsNode.getChildren().add(n);

        d = new DemoDescription(PREFIX + "XYZBarChart3DFXDemo1", "XYZBarChart3DFXDemo1.java", "../XYZBarChart3DDemo1.html");
        this.descriptions.put(d.getFileName(), d);
        n = new TreeItem<String>(d.getFileName());        
        xyzChartsNode.getChildren().add(n);

        TreeView<String> tree = new TreeView<String> (rootItem);
        tree.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends TreeItem<String>> observableValue, 
                    TreeItem<String> oldItem, TreeItem<String> newItem) -> {
            selectDemo(newItem.getValue());
        });
        StackPane root = new StackPane();
        root.getChildren().add(tree);
        return root;
    }
    
    private Method getMethod(String name, Class c) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method: methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
    
    private void selectDemo(String name) {
        DemoDescription demoDesc = this.descriptions.get(name);
        if (demoDesc != null) {
            try {
                BorderPane borderPane = (BorderPane) this.splitter.getItems().get(0);
                Node oldnode = borderPane.getCenter();
                if (oldnode instanceof Canvas) {
                    Canvas c = (Canvas) oldnode;
                    c.widthProperty().unbind();
                    c.heightProperty().unbind();
                }
                // get the class by reflection
                Class<?> c = Class.forName(demoDesc.getClassName());
                Method m0 = getMethod("createDemoNode", c);
                if (m0 != null) {
                    Node node = (Node) m0.invoke(null, (Object[]) null);
                    borderPane.setCenter(node);
                    if (node instanceof Chart3DViewer) {
                        Chart3DViewer v = (Chart3DViewer) node;
                        v.prefWidthProperty().bind(borderPane.widthProperty());
                        v.prefHeightProperty().bind(borderPane.heightProperty());
                        if (node instanceof Chart3DCanvas) {
                            Chart3DCanvas cc = (Chart3DCanvas) node;
                            cc.zoomToFit(cc.getWidth(), cc.getHeight());
                        }
                    } 
              
                } else {
                    Method m1 = getMethod("createDataset", c);
                    Method m2 = getMethod("createChart", c);

                    Dataset3D dataset = (Dataset3D) m1.invoke(null, (Object[]) null);
                    Chart3D chart = (Chart3D) m2.invoke(null, new Object[] { dataset });
                
                    Chart3DCanvas canvas = new Chart3DCanvas(chart);
                    borderPane.setCenter(canvas);
                    canvas.widthProperty().bind(borderPane.widthProperty());
                    canvas.heightProperty().bind(borderPane.heightProperty());                
                    canvas.zoomToFit(canvas.getWidth(), canvas.getHeight());
                }
                String urlStr = c.getResource(demoDesc.getDescriptionFileName()).toString();
                this.chartDescription.getEngine().load(urlStr);
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(OrsonChartsFXDemo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(OrsonChartsFXDemo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(OrsonChartsFXDemo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(OrsonChartsFXDemo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(OrsonChartsFXDemo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
     
    }
    
    private SplitPane createChartPane() {
        CategoryDataset3D dataset = AreaChart3D1.createDataset();
        Chart3D chart = AreaChart3D1.createChart(dataset);
        Chart3DCanvas canvas = new Chart3DCanvas(chart);
      
        this.splitter = new SplitPane();
        splitter.setOrientation(Orientation.VERTICAL);
        final BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        
       // Bind canvas size to stack pane size.
        canvas.widthProperty().bind(
                       borderPane.widthProperty());
        canvas.heightProperty().bind(
                       borderPane.heightProperty());

        final StackPane sp2 = new StackPane();
        
        this.chartDescription = new WebView();
        WebEngine webEngine = chartDescription.getEngine();
        webEngine.load(AreaChart3D1.class.getResource("AreaChart3DDemo1.html").toString());
        
        sp2.getChildren().add(chartDescription);  
        splitter.getItems().addAll(borderPane, sp2);
        splitter.setDividerPositions(0.70f, 0.30f);
        return splitter;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
