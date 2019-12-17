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

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Swing JPanel subclass that contains a 3D surface plot, with interactive mouse
 * controls.
 *
 * Typical usage: create set options show(HistData) add Chart3DPanel to a Swing
 * Container
 *
 * @author areeda
 */
public class Chart3DPanel extends JPanel {

    public static final long serialVersionUID = 234L;

    private Canvas3D canvas = null;
    private SimpleUniverse univ = null;
    private BranchGroup scene = null;

    private boolean addLine = false;
    private boolean wireFrame = false;
    private String colorTableName = "nih";
    private HistData myData = null;
    private HistData[] myData1 = null;
    private float myzmin, myzmax;
    private boolean myautoZscale = false;
    private boolean multipleplots = false;
    private float dx, dy;
    boolean dxdysetted = false;

    /**
     * Display histogram data in the panel. Set any options then call show
     *
     * @param hd - Class derived from HistData contains 2D table of values
     */
    public void setDxDy(float dx1, float dy1) {
        dx = dx1;
        dy = dy1;
        dxdysetted = true;

    }

    public void show(HistData hd) {
        setData(hd);
        makeScene();
    }

    public void show(HistData[] hd) {
        setData(hd);
        makeScene();
    }

    /**
     * supply the 2D array of z-values for display
     *
     * @param hd data used for the plot
     * @see HistData
     */
    public void setData(HistData hd) {
        myData = hd;
    }

    public void setData(HistData[] hd) {
        myData1 = hd;
        multipleplots = true;

    }

    private void makeScene() {
        canvas = createUniverse();

        // Create the content branch and add it to the universe
        if (multipleplots) {
            scene = createSceneGraph(myData1);
        } else {
            scene = createSceneGraph(myData);
        }
        univ.addBranchGraph(scene);

        this.setLayout(new BorderLayout());
        this.add(canvas);
    }

    /**
     * Polygons can be rendered as wireframe or filled
     *
     * @param wf - true -> wire frame only, false -> shaded surface
     */
    public void setWireFrame(boolean wf) {
        wireFrame = wf;
    }

    /**
     * Control color coding of height values
     *
     * @param ctName String passed to ColorTables object, a default is used if
     * specified table not found
     */
    public void setColorTable(String ctName) {
        this.colorTableName = ctName;
    }

    private Canvas3D createUniverse() {
        // Get the preferred graphics configuration for the default screen
        GraphicsConfiguration config
                = SimpleUniverse.getPreferredConfiguration();

        // Create a Canvas3D using the preferred configuration
        Canvas3D c = new Canvas3D(config);

        // Create simple universe with view branch
        univ = new SimpleUniverse(c);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        univ.getViewingPlatform().setNominalViewingTransform();
        ViewingPlatform ModelViewingPlatform = univ.getViewingPlatform();
        OrbitBehavior ModelOrbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        BoundingSphere ModelBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        ModelOrbit.setSchedulingBounds(ModelBounds);
        ModelViewingPlatform.setViewPlatformBehavior(ModelOrbit);
        // Ensure at least 5 msec per frame (i.e., < 200Hz)
        univ.getViewer().getView().setMinimumFrameCycleTime(5);
        //canvas.getView().setBackClipDistance(10000.);
        //canvas.getView().setFrontClipDistance(10000.);

        return c;
    }

    public void setZminZmax(float zminSet, float zmaxSet) {
        myzmin = zminSet;
        myzmax = zmaxSet;
        myautoZscale = true;
    }

    private BranchGroup createSceneGraph(HistData hd) {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();

        // Create the TransformGroup node and initialize it to the
        // identity. Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at run time. Add it to
        // the root of the subgraph.
        TransformGroup myGraph = new TransformGroup();
        myGraph.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        myGraph.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objRoot.addChild(myGraph);

        // Create a simple Shape3D node; add it to the scene graph.
        float zmin = 0, zmax = 1.f;
        if (hd != null) {
            Plot3D plot = new Plot3D(hd);
            plot.setWireFrame(this.wireFrame);
            plot.setColorTable(colorTableName);
            if (myautoZscale) {
                plot.setZminZmax(myzmin, myzmax);
            }
            if (dxdysetted) {
                plot.setDxDy(dx, dy);
            }
            plot.makePlot();
            zmin = plot.getMin();
            zmax = plot.getMax();
            myGraph.addChild(plot);
        }

        Point3f origin = new Point3f(-0.5f, -0.5f, 0.f);
        Point3f loleft = new Point3f(-0.5f, -0.5f, -0.5f);
        Point3f upright = new Point3f(0.5f, 0.5f, 0.5f);
        if (zmin < 0) {
            float z0 = -zmin / (zmax - zmin) - .5f;
            origin.z = z0;
            /*loleft.z = zmin/(zmax-zmin);
            upright.z = zmax/(zmax-zmin);*/
        }
        Vector3f[] axisVectors = {new Vector3f(1.f, 0.f, 0.f), new Vector3f(0.f, 1.f, 0.f), new Vector3f(0.f, 0.f, 1.f)};
        Axis3D2 ourAxes = new Axis3D2();
        ourAxes.setAxisSpecs(axisVectors, origin, loleft, upright);
        ourAxes.drawThem();
        myGraph.addChild(ourAxes);

        // Create a new Behavior object that will perform the
        // desired operation on the specified transform and add
        // it into the scene graph.
        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, 8000);

        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
        AmbientLight lightA = new AmbientLight();
        lightA.setInfluencingBounds(bounds);
        //objRoot.addChild(lightA);
        DirectionalLight lightD1 = new DirectionalLight();
        lightD1.setInfluencingBounds(new BoundingSphere());
        // customize DirectionalLight object
        lightD1.setDirection(1.f, 1.f, -1000.f);
        // objRoot.addChild(lightD1);

        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(myGraph);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseRotate);

        MouseTranslate myMouseTranslate = new MouseTranslate();
        myMouseTranslate.setTransformGroup(myGraph);
        myMouseTranslate.setSchedulingBounds(bounds);
        objRoot.addChild(myMouseTranslate);

        MouseZoom myMouseZoom = new MouseZoom();
        myMouseZoom.setTransformGroup(myGraph);
        myMouseZoom.setSchedulingBounds(bounds);
        objRoot.addChild(myMouseZoom);

        // Have Java 3D perform optimizations on this scene graph.
        objRoot.compile();

        return objRoot;
    }

    private BranchGroup createSceneGraph(HistData[] hd) {
        // Create the root of the branch graph
        float zmin = 0, zmax = 1.f;
        BranchGroup objRoot = new BranchGroup();
        TransformGroup[] myGraph = new TransformGroup[hd.length];
        for (int i = 0; i < hd.length; i++) {

            myGraph[i] = new TransformGroup();
            // Create the TransformGroup node and initialize it to the
            // identity. Enable the TRANSFORM_WRITE capability so that
            // our behavior code can modify it at run time. Add it to
            // the root of the subgraph.

            myGraph[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            myGraph[i].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            objRoot.addChild(myGraph[i]);

            // Create a simple Shape3D node; add it to the scene graph.
            if (hd[i] != null) {
                Plot3D plot = new Plot3D(hd[i]);
                plot.setWireFrame(this.wireFrame);
                plot.setColorTable(colorTableName);
                if (myautoZscale) {
                    plot.setZminZmax(myzmin, myzmax);
                }
                plot.makePlot();
                zmin = plot.getMin();
                zmax = plot.getMax();
                myGraph[i].addChild(plot);
            }
        }
        Point3f origin = new Point3f(-0.5f, -0.5f, 0.f);
        Point3f loleft = new Point3f(-0.5f, -0.5f, -0.5f);
        Point3f upright = new Point3f(0.5f, 0.5f, 0.5f);
        if (zmin < 0) {
            float z0 = -zmin / (zmax - zmin) - .5f;
            origin.z = z0;
            /*loleft.z = zmin/(zmax-zmin);
            upright.z = zmax/(zmax-zmin);*/
        }

        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, 8000);

        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
        AmbientLight lightA = new AmbientLight();
        lightA.setInfluencingBounds(bounds);
        //objRoot.addChild(lightA);
        DirectionalLight lightD1 = new DirectionalLight();
        lightD1.setInfluencingBounds(new BoundingSphere());
        // customize DirectionalLight object
        lightD1.setDirection(1.f, 1.f, -1000.f);
        // objRoot.addChild(lightD1);
        for (int i = 0; i < hd.length; i++) {
            MouseRotate myMouseRotate = new MouseRotate();
            myMouseRotate.setTransformGroup(myGraph[i]);
            myMouseRotate.setSchedulingBounds(new BoundingSphere());
            objRoot.addChild(myMouseRotate);

            MouseTranslate myMouseTranslate = new MouseTranslate();
            myMouseTranslate.setTransformGroup(myGraph[i]);
            myMouseTranslate.setSchedulingBounds(bounds);
            objRoot.addChild(myMouseTranslate);

            MouseZoom myMouseZoom = new MouseZoom();
            myMouseZoom.setTransformGroup(myGraph[i]);
            myMouseZoom.setSchedulingBounds(bounds);
            objRoot.addChild(myMouseZoom);
        }
        // Have Java 3D perform optimizations on this scene graph.
        objRoot.compile();
        return objRoot;
    }
}
