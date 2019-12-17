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

/**
 * Several functions to generate test data for 3D surface plots
 *
 * @author areeda
 */
public class HistGetObjTough2Data extends HistData {

    private double[][] hist;
    private Point2D.Float lowLeft, upRight;

    /**
     * Generate test surface, a damped cos function (I thought it looked better)
     * cos(d) * exp(-(1+d)) around (0,0)
     *
     */
    public void makeDampedCosine() {
        int nx = 256, ny = 256;

        double maxD = Math.sqrt(nx * nx + ny * ny);
        double fact = Math.PI * 9 / maxD;
        double ef = 4. / maxD;
        hist = new double[ny][nx];
        double min = 0., max = 0.;

        float midx = nx / 2.f;
        float midy = ny / 2.f;

        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                double d = Math.abs(Math.sqrt((x - midx) * (x - midx) + (y - midy) * (y - midy)));
                double z = Math.cos(fact * d) * Math.exp(-(d + 1) * ef) * 0.3;
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
     * Generate hyperbolic sine actually 1-sinh(d) around (0,0)
     *
     */
    public void makeHyperbolicSine() {
        int nx = 256, ny = 256;

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
        int nx = 256, ny = 256;

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
