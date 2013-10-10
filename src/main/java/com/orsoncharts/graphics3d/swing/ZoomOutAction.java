/* ===========
 * OrsonCharts
 * ===========
 * 
 * (C)opyright 2013, by Object Refinery Limited.
 * 
 */

package com.orsoncharts.graphics3d.swing;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import com.orsoncharts.graphics3d.ViewPoint3D;

/**
 * Zoom out.
 */
public class ZoomOutAction extends AbstractAction {

    private Panel3D panel;
  
    public ZoomOutAction(Panel3D panel3D) {
        super("\uf010");
        this.panel = panel3D;
        putValue(Action.ACTION_COMMAND_KEY, "ZOOM_OUT");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ViewPoint3D viewPt = this.panel.getViewPoint();
        float valRho = Math.max(10.0f, viewPt.getRho() + 5.0f);
        float valTheta = viewPt.getTheta();
        float valPhi = viewPt.getPhi();
        panel.setViewPoint(new ViewPoint3D(valTheta, valPhi, valRho));
    }
    
}
