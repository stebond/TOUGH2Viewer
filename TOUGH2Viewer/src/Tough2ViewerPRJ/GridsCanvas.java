/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

/**
 *
 * @author stebond
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Program to draw grids.
 *
 * @author Ian Darwin, http://www.darwinsys.com/
 */
class GridsCanvas extends Canvas {

    int width, height;

    int rows;

    int cols;

    GridsCanvas(int w, int h, int r, int c) {
        setSize(width = w, height = h);
        rows = r;
        cols = c;
    }

    public void paint(Graphics g) {

        width = getSize().width;
        height = getSize().height;
        Color colore[] = Tough2Viewer.dataobj.getColorScale();
        int timeToPlot = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int variableToPlot = Tough2Viewer.dataobj.get_actualVariableToPlot();
        float VarMin = Tough2Viewer.dataobj.get_GlobalScale(variableToPlot, 0);
        float VarMax = Tough2Viewer.dataobj.get_GlobalScale(variableToPlot, 1);
        float rangeVariable;
        rangeVariable = VarMax - VarMin;
        if (rangeVariable <= 0) {
            rangeVariable = 1.0f;
        }

        int nx, nz;
        nx = Tough2Viewer.dataobj.get_nx();
        nz = Tough2Viewer.dataobj.get_nz();
        float xmin = Tough2Viewer.dataobj.get_Xo(0) - Tough2Viewer.dataobj.get_DimBlockX(0) / 2;
        float xmax = Tough2Viewer.dataobj.get_Xo(nx * nz - 1) + Tough2Viewer.dataobj.get_DimBlockX(nx - 1) / 2;
        float ymin = Tough2Viewer.dataobj.get_Yo(0) - Tough2Viewer.dataobj.get_ThicknessLayer(0) / 2;
        float ymax = Tough2Viewer.dataobj.get_Yo(nx * nz - 1) + Tough2Viewer.dataobj.get_ThicknessLayer(nz - 1) / 2;
        for (int ix = 0; ix < nx; ix = ix + 1) {
            for (int iz = 0; iz < nz; iz = iz + 1) {
                int i_b = iz + ix * nz;
                float dx = Tough2Viewer.dataobj.get_DimBlockX(ix);
                float dy = Tough2Viewer.dataobj.get_ThicknessLayer(iz);
                float xo = Tough2Viewer.dataobj.get_Xo(i_b) - dx / 2;
                float yo = -Tough2Viewer.dataobj.get_Yo(i_b) - dy / 2;
                xmin = Math.min(xmin, xo);
                xmax = Math.max(xmax, xo);
                ymin = Math.min(ymin, yo);
                ymax = Math.max(ymax, yo);
            }
        }
        float lmax = Math.max(xmax - xmin, ymax - ymin);
        for (int ix = 0; ix < nx; ix = ix + 1) {
            for (int iz = 0; iz < nz; iz = iz + 1) {
                int i_b = iz + ix * nz;
                float dx = Tough2Viewer.dataobj.get_DimBlockX(ix);
                float dy = Tough2Viewer.dataobj.get_ThicknessLayer(iz);
                float xo = Tough2Viewer.dataobj.get_Xo(i_b) - dx / 2;
                float yo = -Tough2Viewer.dataobj.get_Yo(i_b) - dy / 2;

                float scala = ((float) width) / lmax;
                xo = xo * scala;
                yo = yo * scala;
                dx = dx * scala;
                dy = dy * scala;
                if (ix < nx - 1) {
                    dx = dx + 4;
                }
                if (iz < nz - 1) {
                    dy = dy + 4;
                }

                int n_color = colore.length - 1;
                int index = (int) ((Tough2Viewer.dataobj.get_dataArray(i_b, timeToPlot, variableToPlot) - VarMin) / (rangeVariable) * ((float) n_color));
                if (index > n_color) {
                    int errorequi = 1;
                    index = colore.length - 1;
                }
                if (index < 0) {
                    int errorequi = 1;
                    index = 0;
                }
                g.setColor(colore[index]);
                g.fillRect((int) xo, (int) yo, (int) dx, (int) dy);

            }
        }

    }

}

/**
 * This is the demo class.
 */
