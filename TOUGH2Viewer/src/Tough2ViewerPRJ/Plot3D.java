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
 */
package Tough2ViewerPRJ;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.IndexColorModel;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

/**
 * A class to create a 3D surface shape from a 2D array of heights
 *
 * @author areeda
 */
public class Plot3D extends Shape3D {

    private Geometry p3geometry;
    private Appearance p3appearance;
    private IndexColorModel icm;
    private double[][] hist;
    private Point2D.Float lowLeft, upRight;
    private float zmin, zmax;
    private boolean wireFrame = false;
    private ColorTables ct = new ColorTables();
    private HistData myHist;
    private boolean autoZscale = true;
    private float dx1, dy1;
    private boolean dxdysetted = true;

    /**
     * Default constructor. This contructor requires that at least a call to
     * setHistData() be made before makePlot is called.
     */
    public Plot3D() {
        setDefaults();
    }

    /**
     * uses default values for everything
     *
     * @param htd 2D array of z-values
     */
    public Plot3D(HistData htd) {
        setDefaults();
        myHist = htd;
    }

    /**
     * resets all display options to defaults
     */
    public void setDefaults() {
        icm = ct.getIndexColorModel("nih");
        wireFrame = false;
    }

    /**
     * control color coding of z-value
     *
     * @param ctName - valid ColorTables name or default will be used
     * @see ColorTables
     */
    public void setColorTable(String ctName) {
        icm = ct.getIndexColorModel(ctName);
    }

    /**
     *
     * @param htd the data to plot
     * @see HistData
     * @see HistFile
     * @see HistTestData
     */
    public void setHistData(HistData htd) {
        myHist = htd;
    }

    /**
     * control shading
     *
     * @param wf true creates a wire frame, false creates shaded surface
     */
    public void setWireFrame(boolean wf) {
        this.wireFrame = wf;
    }

    /**
     * once all the options have been set the way you want them call this to
     * create the scene
     */
    public void makePlot() {
        hist = myHist.getHist();
        lowLeft = myHist.getLowLeft();
        upRight = myHist.getUpRight();

        createGeometry();
        createAppearance();

        this.setGeometry(p3geometry);
        if (p3appearance != null) {
            this.setAppearance(p3appearance);
        }

    }

    public void setZminZmax(float zminSet, float zmaxSet) {
        zmin = zminSet;
        zmax = zmaxSet;
        autoZscale = false;
    }

    public void setDxDy(float val1, float val2) {
        dx1 = val1;
        dy1 = val2;
        dxdysetted = true;
    }

    private void createGeometry() {
        int ny = hist.length;
        int nx = ny > 0 ? hist[0].length : 0;

        int np = nx * ny;           // number of points
        int nq = (nx - 1) * (ny - 1);   // number of quads
        int nv = 2 * 4 * 3 * nq;  // 2 faces per quad, 4 points per face, 3 coordinates per point
        float[] verts = new float[nv];
        int nc = 2 * 4 * nq;
        Color3f[] colors = new Color3f[nc];
        int v = 0, c = 0;

        float midy = ny / 2.f;
        float midx = nx / 2.f;
        float min = 0, max = 0;
        if (autoZscale) {
            for (int y = 0; y < ny; y++) {
                for (int x = 0; x < nx; x++) {
                    float z = (float) hist[y][x];
                    if ((x == 0 && y == 0) || z < min) {
                        min = z;
                    }
                    if ((x == 0 && y == 0) || z > max) {
                        max = z;
                    }
                }
            }
            zmin = min;
            zmax = max;
        }
        float xScal;
        float yScal;
        float zScal;
        if (dxdysetted) {
            xScal = 1.f / nx;
            yScal = 1.f / ny;
            zScal = 1.f / (zmax - zmin);
        } else {
            xScal = 1.f / nx;
            yScal = 1.f / ny;
            zScal = 1.f / (zmax - zmin);
        }

        int dx[] = {0, 1, 1, 0, 0, 1, 1, 0};
        int dy[] = {0, 0, 1, 1, 1, 1, 0, 0};

        for (int y = 0; y < ny - 1; y++) {
            float fy = (y - midy) * yScal;
            for (int x = 0; x < nx - 1; x++) {
                float fx = (x - midx) * xScal;

                for (int d = 0; d < dx.length; d++) {
                    double z = hist[y + dy[d]][x + dx[d]];
                    verts[v++] = fx + dx[d] * xScal;
                    verts[v++] = fy + dy[d] * yScal;
                    verts[v++] = (float) (z - zmin) * zScal - 0.5f;

                    Color3f cl = getColor(z, zmin, zmax);
                    colors[c++] = cl;
                }

            }
        }
        QuadArray plot = new QuadArray(np * 4 * 2, QuadArray.COORDINATES | QuadArray.COLOR_3);
        plot.setCoordinates(0, verts);
        plot.setColors(0, colors);

        this.p3geometry = plot;

        this.setGeometry(p3geometry);

    }

    /**
     * minimum of data range
     *
     * @return the minimum z value in data plotted (unscaled)
     */
    public float getMin() {
        return zmin;
    }

    /**
     * maximum of data range
     *
     * @return the maximum z value in data plotted (unscaled)
     */
    public float getMax() {
        return zmax;
    }

    private void createAppearance() {
        p3appearance = new Appearance();
        Material m = new Material();
        m.setShininess(128.f);
        this.p3appearance.setMaterial(m);

        if (this.wireFrame) {
            PolygonAttributes pa = new PolygonAttributes();
            pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
            p3appearance.setPolygonAttributes(pa);
        }
        ColoringAttributes ca = new ColoringAttributes(new Color3f(Color.WHITE), ColoringAttributes.SHADE_FLAT);
        p3appearance.setColoringAttributes(null);
    }

    private Color3f getColor(double d, double min, double max) {
        int l = (int) Math.round((d - min) * 255 / (max - min));
        if (l < 0) {
            l = 0;
        } else if (l > 255) {
            l = 255;
        }

        Color c = new Color(icm.getRed(l), icm.getGreen(l), icm.getBlue(l));
        Color3f ret = new Color3f(c);
        return ret;
    }

}
