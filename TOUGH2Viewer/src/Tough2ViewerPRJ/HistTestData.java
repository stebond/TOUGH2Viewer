/*
 * Copyright 2009 - Areeda Associates Ltd.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Areeda Associates designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package Tough2ViewerPRJ;

import java.awt.geom.Point2D;
import javax.vecmath.Point3f;

/**
 * Several functions to generate test data for 3D surface plots
 *
 * @author areeda
 */
public class HistTestData extends HistData {

    private double[][] hist;

    private Point2D.Float lowLeft, upRight;
    private Point3f lowLeft3D, upRight3D;
    private int actual_time = Tough2Viewer.dataobj.get_actualTimeToPlot();
    private int actualVariableToPlot = Tough2Viewer.dataobj.get_actualVariableToPlot();
    private float actualZtoPlot = Tough2Viewer.dataobj.get_actualZToPlot();
    private int nx = 20, ny = 20, nz = 20;
    private int neighbord = 5;

    public void set_nxny(int nxx, int nyy, int nzz) {
        nx = nxx;
        ny = nyy;
        nz = nzz;
    }

    public void setlowLeftupRight(float xmin, float ymin, float xmax, float ymax) {

        lowLeft = new Point2D.Float(xmin, ymin);
        upRight = new Point2D.Float(xmax, ymax);
    }

    public void makeTough2Data2D() {

        hist = new double[ny][nx];

        lowLeft = new Point2D.Float((float) Tough2Viewer.dataobj.get_xmin(), (float) Tough2Viewer.dataobj.get_ymin());
        upRight = new Point2D.Float((float) Tough2Viewer.dataobj.get_xmax(), (float) Tough2Viewer.dataobj.get_ymax());
        float dx = (upRight.x - lowLeft.x) / (nx - 1);
        float dy = (upRight.y - lowLeft.y) / (ny - 1);

        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                float xo = lowLeft.x + x * dx;
                float yo = lowLeft.y + y * dy;
                float zo = actualZtoPlot;

                hist[y][x] = makeinterpolation2D(xo, yo, zo, actualZtoPlot, neighbord);

            }
        }

    }

    public void setData(double[][] ht2d) {
        hist = new double[ny][nx];
        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {

                hist[y][x] = ht2d[y][x];

            }
        }

    }

    private double makeinterpolation2D(float xo, float yo, float zo, float quote, int n_neighbord) {
        float z = 0;

        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        float[][] distance = new float[nxyz][2];

        for (int ib = 0; ib < nxyz; ib++) {
            float x1 = Tough2Viewer.dataobj.get_Xo(ib);
            float y1 = Tough2Viewer.dataobj.get_Yo(ib);
            float z1 = Tough2Viewer.dataobj.get_Zo(ib);
            distance[ib][0] = dist(xo, yo, zo, x1, y1, z1);
            distance[ib][1] = ib;
        }
        sort(distance, nxyz);
        if (distance[0][0] == 0) {
            z = Tough2Viewer.dataobj.get_dataArray((int) (distance[0][1]), actual_time, actualVariableToPlot);
        } else {
            z = 0.0f;
            float weight = 0;
            for (int ib = 0; ib < n_neighbord; ib++) {
                z = z + Tough2Viewer.dataobj.get_dataArray((int) (distance[ib][1]), actual_time, actualVariableToPlot) / distance[ib][0];
                weight = weight + 1.0f / distance[ib][0];
            }
            z = z / weight;
        }

        return z;
    }

    private double makeinterpolation3D(float xo, float yo, float zo, int n_neighbord) {
        //per ora questa semplice interpolazione prende i primi n punti vicini e poi ...stima

        float zstimato = 0;

        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        int nxy = Tough2Viewer.dataobj.get_nxy();

        float[][] distance = new float[nxyz][2];

        for (int ib = 0; ib < nxyz; ib++) {
            int iz = ib / nxy;
            int ixy = ib - iz * nxy;
            float x1 = Tough2Viewer.dataobj.get_Xo(ixy);
            float y1 = Tough2Viewer.dataobj.get_Yo(ixy);
            float z1 = Tough2Viewer.dataobj.get_Zo(iz);
            distance[ib][0] = dist(xo, yo, zo, x1, y1, z1);
            distance[ib][1] = ib;
        }
        sort(distance, nxy);
        if (distance[0][0] == 0) {
            zstimato = Tough2Viewer.dataobj.get_dataArray((int) (distance[0][1]), actual_time, actualVariableToPlot);
        } else {
            zstimato = 0.0f;
            float weight = 0;
            for (int ib = 0; ib < n_neighbord; ib++) {
                zstimato = zstimato + Tough2Viewer.dataobj.get_dataArray((int) (distance[ib][1]), actual_time, actualVariableToPlot) / distance[ib][0];
                weight = weight + 1.0f / distance[ib][0];
            }
            zstimato = zstimato / weight;
        }

        return zstimato;
    }

    private float dist(float xo, float yo, float x1, float y1) {
        return (float) Math.sqrt((x1 - xo) * (x1 - xo) + (y1 - yo) * (y1 - yo));
    }

    private float dist(float xo, float yo, float zo, float x1, float y1, float z1) {
        return (float) Math.sqrt((x1 - xo) * (x1 - xo) + (y1 - yo) * (y1 - yo) + (z1 - zo) * (z1 - zo));
    }

    void sort(float a[][], int lenght) {
        for (int i1 = 0; i1 < lenght; i1++) {
            for (int i2 = i1 + 1; i2 < lenght; i2++) {
                if (a[i2][0] < a[i1][0]) {
                    for (int i3 = 0; i3 < 2; i3++) {
                        float temp = a[i1][i3];
                        a[i1][i3] = a[i2][i3];
                        a[i2][i3] = temp;
                    }
                }
            }
        }
    }

    /**
     * Generate hyperbolic sine actually 1-sinh(d) around (0,0)
     *
     */
    public void makeHyperbolicSine() {
//        int nx=256,ny=256;
        int nx = 25, ny = 25;
        double ef = 4. / Math.sqrt(nx * nx + ny * ny);
        hist = new double[ny][nx];
        double min = 0., max = 0.;

        float midx = nx / 2.f;
        float midy = ny / 2.f;
        double fact = Math.sqrt(midx * midx + midy * midy);

        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                double d = Math.abs(Math.sqrt((x - midx) * (x - midx) + (y - midy) * (y - midy)));
                double z = 1 - Math.sinh(d / fact);
                hist[y][x] = z;
                if ((x == 0 && y == 0) || z < min) {
                    min = z;
                }
                if ((x == 0 && y == 0) || z > max) {
                    max = z;
                }
            }
        }
        lowLeft = new Point2D.Float((float) -fact * nx, (float) -fact * ny);
        upRight = new Point2D.Float((float) fact * nx, (float) fact * ny);

    }

    /**
     * Generate a 2D normal distribution
     */
    public void makeGauss() {
//        int nx=256,ny=256;
        int nx = 25, ny = 25;

        double ef = 4. / Math.sqrt(nx * nx + ny * ny);
        hist = new double[ny][nx];
        double min = 0., max = 0.;

        float midx = nx / 2.f;
        float midy = ny / 2.f;
        double fact = Math.sqrt(midx * midx + midy * midy);

        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                double sigma = 32;
                double d = Math.abs(Math.sqrt((x - midx) * (x - midx) + (y - midy) * (y - midy)));
                double z = 1 / (sigma * Math.sqrt(Math.PI)) * Math.exp(-(d * d) / (2 * sigma * sigma));
                hist[y][x] = z;
                if ((x == 0 && y == 0) || z < min) {
                    min = z;
                }
                if ((x == 0 && y == 0) || z > max) {
                    max = z;
                }
            }
        }
        lowLeft = new Point2D.Float((float) -fact * nx, (float) -fact * ny);
        upRight = new Point2D.Float((float) fact * nx, (float) fact * ny);

    }

    public double[][] getHist() {
        return hist;
    }

    public Point2D.Float getLowLeft() {
        return lowLeft;
    }

    public Point2D.Float getUpRight() {
        return upRight;
    }
}
