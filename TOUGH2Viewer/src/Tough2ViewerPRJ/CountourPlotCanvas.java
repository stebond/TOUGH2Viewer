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
import java.util.ArrayList;
import javax.vecmath.Point2d;

/**
 * Program to draw grids.
 *
 * @author Ian Darwin, http://www.darwinsys.com/
 */
class CountourPlotCanvas extends Canvas {

    int width, height;
    int scale_number;
    int nx = 10;
    int ny = 10;
    float dx = 1.0f;
    float dy = 1.0f;
    float[][] data = new float[nx][ny];

    CountourPlotCanvas(int scale) {
        //setSize(width = w, height = h);
        //rows = r;
        //cols = c;
        scale_number = scale;
    }

    public void paint(Graphics g) {

        width = getSize().width;
        height = getSize().height;
        int number_of_labels = 11;
        g.setColor(new Color(1.0f, 1.0f, 1.0f));
        g.fillRect((int) 0, (int) 0, (int) width, (int) height);
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        g.drawRect((int) 0, (int) 0, (int) width - 1, (int) height - 1);

        Color colore[] = Tough2Viewer.dataobj.getColorScale();

        int variableToPlot;

        float VarMin = 0;
        float VarMax = 10;
        float rangeVariable;

        String[] Variable_unit;
        String[] Variable_name;
        int it = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int number_of_levels = 10;

        int[][][] marks = new int[nx][ny][number_of_levels];
        float deltaIsoV = (VarMax - VarMin) / number_of_levels;
        ArrayList[] isopoints = new ArrayList[number_of_levels];
        ArrayList[] points = new ArrayList[number_of_levels];
        for (int il = 0; il < number_of_levels; il = il + 1) {
            float iso_value = VarMin + deltaIsoV * il;

            for (int ii = 0; ii < nx - 1; ii = ii + 1) {
                for (int ij = 0; ij < ny - 1; ij = ij + 1) {

                    //cerca in orizzontale
                    if ((iso_value < data[ii][ij] & iso_value > data[ii][ij + 1]) || (iso_value < data[ii][ij + 1] & iso_value > data[ii][ij])) {
                        isopoints[il].add(getXYPoint(ii, ij, ii, ij + 1, iso_value));
                        points[il].add(ii);
                        points[il].add(ij);
                        points[il].add(ii);
                        points[il].add(ij + 1);
                    }
                    //cerca in verticale
                    if ((iso_value < data[ii][ij] & iso_value > data[ii + 1][ij]) || (iso_value < data[ii + 1][ij] & iso_value > data[ii][ij])) {
                        isopoints[il].add(getXYPoint(ii + 1, ij, ii, ij, iso_value));
                        points[il].add(ii);
                        points[il].add(ij);
                        points[il].add(ii + 1);
                        points[il].add(ij);
                    }

                }//ciclo ij

            }//ciclo ii
        }//ciclo isopoints
        //ora inizio a suddividere le diverse isopoints.....

    }

    private Point2d getXYPoint(int i, int j, int i1, int j1, float isov) {

        int[][] deltaIJ = new int[4][2];
        deltaIJ[0][0] = 1;
        deltaIJ[0][1] = 0;
        deltaIJ[1][0] = 0;
        deltaIJ[1][1] = 1;
        deltaIJ[2][0] = -1;
        deltaIJ[2][1] = 0;
        deltaIJ[3][0] = 0;
        deltaIJ[3][1] = -1;
        float x, y;
        int segno, starti, startj;
        if (data[i][j] < data[i1][j1]) {
            segno = 1;
            starti = i;
            startj = j;
        } else {
            segno = -1;
            starti = i1;
            startj = j1;
        }
        x = starti * dx + Math.abs(i1 - i) * segno * dx * (Math.min(data[i][j], data[i1][j1]) - isov) / (Math.abs(data[i][j] - data[i][j + 1]));
        y = startj * dx + Math.abs(j1 - j) * segno * dy * (Math.min(data[i][j], data[i1][j1]) - isov) / (Math.abs(data[i][j] - data[i][j + 1]));
        Point2d p = new Point2d((double) x, (double) y);
        return p;
    }

}

/**
 * This is the demo class.
 */
