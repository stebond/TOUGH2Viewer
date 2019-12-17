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

import java.awt.Color;
import javax.media.j3d.Appearance;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * A class that generates 3 dimensional axes with optional gridlines
 *
 * Typical usage: create object setAxisSpecs [set any options] drawThem() add
 * object to your scene
 *
 * @author areeda
 */
public class Axis3D2 extends TransformGroup {

    /**
     * unit vectors (x,y,z) for the axes being drawn
     */
    protected Vector3f[] unitVectors;
    /**
     * where the 3 axes interscet
     */
    protected Point3f origin;
    /**
     * minimum values for each axis, universe coordinate system
     */
    protected Point3f loleft;
    /**
     * max values for each axis, universe coordinate system
     */
    protected Point3f upright;
    /**
     * format of the axis, 1 = Axis only, 2 = Box
     */
    protected int axisFormat = 2;
    /**
     * width of line used for axis
     */
    protected float axisWidth = 6;
    /**
     * used to draw axis and labels
     */
    protected Color axisColor = Color.GREEN;
    /**
     * used to draw grid lines
     */
    protected Color gridColor = Color.BLUE;
    /**
     * width of a grid line
     */
    protected float gridWidth = 3;
    /**
     * format of grid lines < 2 none, 2 - x and y in z-origin, > 2 - x,y & z
     * lines
     */
    protected int gridFormat = 2;
    /**
     * number of grid lines to draw
     */
    protected int nGridLines = 5;

    /**
     * Default contructor
     */
    public Axis3D2() {
        this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    }

    /**
     * Define the axes, note all parameters are in the universe's coordinate
     * system
     *
     * @param axisV - basis vectors, usually (1,0,0), (0,1,0), (0,0,1) but they
     * can be any orthogonal set. Usually unit vectors but just have to be non
     * zero length.
     * @param origin - where the axes meet
     * @param loleft - minimum (x,y,z)
     * @param upright - maximum (x,y,z)
     */
    public void setAxisSpecs(Vector3f[] axisV, Point3f origin, Point3f loleft, Point3f upright) {
        this.unitVectors = new Vector3f[3];
        for (int d = 0; d < 3; d++) {
            this.unitVectors[d] = new Vector3f(axisV[d]);
            unitVectors[d].normalize();         // make it a unit vector
        }
        //@todo confirm axes are orthogonal
        this.origin = origin;
        this.loleft = loleft;
        this.upright = upright;
    }

    /**
     * Creates the display using the data and options already set
     */
    public void drawThem() {
        // draw grid lines as specified
        if (this.gridFormat > 1) {
            float xinc = (upright.x - loleft.x) / nGridLines;
            float yinc = (upright.y - loleft.y) / nGridLines;
            float zinc = (upright.z - loleft.z) / nGridLines;
            float x0 = loleft.x, y0 = loleft.y, z0 = origin.z;
            float x1 = upright.x, y1 = upright.y, z1 = upright.z;

            int nz = 1;
            if (gridFormat > 2) {
                nz = this.nGridLines + 1;
                z0 = loleft.z;
            }
            for (int z = 0; z < nz; z++) {
                float cz = z0 + z * zinc;
                for (int y = 0; y < nGridLines + 1; y++) {
                    float cy = y0 + y * zinc;
                    Point3f strt = new Point3f(x0, cy, cz);
                    Point3f end = new Point3f(x1, cy, cz);
                    // horizontal grid at this y,z
                    this.drawLine(strt, end, this.gridColor, this.gridWidth);

                    for (int x = 0; x < nGridLines + 1; x++) {
                        float cx = x0 + x * xinc;
                        if (y == 1) {   // draw the vertical grid line at this x, z
                            strt = new Point3f(cx, y0, cz);
                            end = new Point3f(cx, y1, cz);
                            drawLine(strt, end, gridColor, gridWidth);
                        }
                        if (z == 0 && nz > 1) {   // draw the depth line this x,y
                            strt = new Point3f(cx, cy, z0);
                            end = new Point3f(cx, cy, z1);
                            drawLine(strt, end, gridColor, gridWidth);
                        }
                    }

                }
            }

        }
        // draw the x-axis
        drawLine(new Point3f(loleft.x, origin.y, origin.z), new Point3f(upright.x, origin.y, origin.z), axisColor, axisWidth);

        // draw y-axis
        drawLine(new Point3f(origin.x, loleft.y, origin.z), new Point3f(origin.x, upright.y, origin.z), axisColor, axisWidth);

        // draw y-axis
        drawLine(new Point3f(origin.x, origin.y, loleft.z), new Point3f(origin.x, origin.y, upright.z), axisColor, axisWidth);

        if (this.axisFormat == 2) {
            // square at min z
            drawLine(loleft, new Point3f(upright.x, loleft.y, loleft.z), axisColor, axisWidth);
            drawLine(new Point3f(upright.x, loleft.y, loleft.z), new Point3f(upright.x, upright.y, loleft.z), axisColor, axisWidth);
            drawLine(new Point3f(upright.x, upright.y, loleft.z), new Point3f(loleft.x, upright.y, loleft.z), axisColor, axisWidth);
            drawLine(new Point3f(loleft.x, upright.y, loleft.z), loleft, axisColor, axisWidth);

            // other 2 lines for square at origin
            drawLine(new Point3f(upright.x, upright.y, origin.z), new Point3f(loleft.x, upright.y, origin.z), axisColor, axisWidth);
            drawLine(new Point3f(upright.x, upright.y, origin.z), new Point3f(upright.x, loleft.y, origin.z), axisColor, axisWidth);

            // square at max z
            drawLine(new Point3f(loleft.x, loleft.y, upright.z), new Point3f(upright.x, loleft.y, upright.z), axisColor, axisWidth);
            drawLine(new Point3f(upright.x, loleft.y, upright.z), new Point3f(upright.x, upright.y, upright.z), axisColor, axisWidth);
            drawLine(new Point3f(upright.x, upright.y, upright.z), new Point3f(loleft.x, upright.y, upright.z), axisColor, axisWidth);
            drawLine(new Point3f(loleft.x, upright.y, upright.z), new Point3f(loleft.x, loleft.y, upright.z), axisColor, axisWidth);

            // remaining depth lines
            drawLine(new Point3f(upright.x, loleft.y, loleft.z), new Point3f(upright.x, loleft.y, upright.z), axisColor, axisWidth);
            drawLine(new Point3f(upright.x, upright.y, loleft.z), new Point3f(upright.x, upright.y, upright.z), axisColor, axisWidth);
            drawLine(new Point3f(loleft.x, upright.y, loleft.z), new Point3f(loleft.x, upright.y, upright.z), axisColor, axisWidth);
        }
    }

    /**
     * Set the color of the axes
     *
     * @param c Color object
     */
    public void setAxisColor(Color c) {
        this.axisColor = c;
    }

    /**
     * How thick should this axis be drawn
     *
     * @param r line width in pixels
     */
    public void setAxisWidth(float r) {
        this.axisWidth = r;
    }

    /**
     * Adds a line to the axis group
     *
     * @param origin - starting point
     * @param endP - ending point
     * @param lineColor - color to draw the line
     * @param lineWidth - how thick to draw the line
     */
    protected void drawLine(Point3f origin, Point3f endP, Color lineColor, float lineWidth) {
        int nv = 2 * 3;
        float[] axisV = new float[nv];     // 2 points 3 coordinates/point

        int v = 0;
        float ox = origin.x, oy = origin.y, oz = origin.z;

        axisV[v++] = origin.x;
        axisV[v++] = origin.y;
        axisV[v++] = origin.z;
        axisV[v++] = endP.x;
        axisV[v++] = endP.y;
        axisV[v++] = endP.z;

        int r, g, b;
        r = lineColor.getRed();
        g = lineColor.getGreen();
        b = lineColor.getBlue();

        float[] colors = new float[axisV.length];
        for (int c = 0; c < colors.length;) {
            colors[c++] = r;
            colors[c++] = g;
            colors[c++] = b;
        }
        Shape3D line = new Shape3D();
        LineArray plot = new LineArray(nv, QuadArray.COORDINATES | QuadArray.COLOR_3);

        plot.setCoordinates(0, axisV);
        plot.setColors(0, colors);

        line.setGeometry(plot);

        Appearance appearance = new Appearance();

        LineAttributes la = new LineAttributes();
        la.setLineWidth(lineWidth);
        appearance.setLineAttributes(la);
        line.setAppearance(appearance);

        this.addChild(line);
    }
}
