/*
 * $RCSfile: VoronoiBlockModel3D.java,v $
 *
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 * $Revision: 1.5 $
 * $Date: 2007/04/24 18:55:58 $
 * $State: Exp $
 */
package Tough2ViewerPRJ;

import java.awt.*;
import java.awt.event.*;
import javax.vecmath.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.vp.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * A class that generates 3 dimensional vectorial flow Dependency=databoj fo
 * TOUGH2Viewer
 *
 *
 *
 * @author Stebond
 */
public class FluxModel3DArrow extends javax.swing.JFrame implements
        ActionListener,
        ChangeListener {

    /**
     *
     */
    public XYScatterPlotVoronoiFlux Model2D;
    /**
     *
     */

//private ArrayList    voronoiGeometryBox[];
    private Switch posSwitch;
    private BitSet posMask;
    private Group group;

    /**
     *
     * @param parent
     */
    public FluxModel3DArrow() {
        //    super(parent);
        init();
        getRootPane().setDefaultButton(closeButton);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setTitle("3D Flow Vector");
        this.setPreferredSize(new Dimension(600, 600));
        this.setMinimumSize(new Dimension(400, 400));
    }

    /**
     *
     */
//@Action public void closeAboutBox() {
//        setVisible(false);
//    }
//public class BlockModel3D extends Applet {
    JComboBox altAppMaterialColor;
    JComboBox appMaterialColor;
    JComboBox chooseVariable;
    JComboBox ShiftMode;
    JSlider slider;
    JButton b1;
    // setup font stuff
    private String fontName = "TestFont";
    private String textString = " ";//OrientedShape3D by stebond
    float sl = textString.length();
    private SimpleUniverse u;
    private Arrow ArrowShape[];
    private BranchGroup objRoot;
    TransformGroup BoxTransGrp[];
    Color3f colore[];
    double lenghtVector;
    double lenghtVector_old;
    float riduzione;
    float campo = 2.0f;
    float xmin, xmax, ymin, ymax, zmin, zmax;
    ArrayList myindexZ = new ArrayList();
    ArrayList myindexX = new ArrayList();
    ArrayList myindexY = new ArrayList();

    boolean old_value_normalize;

    /**
     *
     * @param c
     * @return
     */
    @SuppressWarnings("static-access")
    public BranchGroup createSceneGraph(Canvas3D c) {

        // Create the root of the branch graph
        old_value_normalize = Tough2Viewer.dataobj.get_normalizeVector();
        objRoot = new BranchGroup();

        TransformGroup objScale = new TransformGroup();
        Transform3D textMat = new Transform3D();
        // Assuming uniform size chars, set scale to fit string in view
        float f_test;
        f_test = 1.20f / sl;
        textMat.setScale(f_test);
        objScale.setTransform(textMat);
        // Create the transform group node and initialize it to the
        // identity.  Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at runtime.  Add it to the
        // root of the subgraph.
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objTrans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        objRoot.addChild(objTrans);
        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        Appearance apText = new Appearance();
        Material m = new Material();
        m.setLightingEnable(true);
        apText.setMaterial(m);
        Appearance apEarth = new Appearance();
        Material mm = new Material();
        mm.setLightingEnable(true);
        apEarth.setMaterial(mm);
        Appearance apStone = new Appearance();
        apStone.setMaterial(mm);

// create 3D text 
        Font3D f3d = new Font3D(new Font(fontName, Font.PLAIN, 2),
                new FontExtrusion());
        Point3f textPt = new Point3f(-sl / 2.0f, 3.0f, 20.0f);
        Text3D txt = new Text3D(f3d, textString, textPt);
        OrientedShape3D textShape = new OrientedShape3D();
        textShape.setGeometry(txt);
        textShape.setAppearance(apText);
        textShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
        // text is centered around 0, 3, 0.  Make it rotate around 0,5,0
        Point3f rotationPt = new Point3f(0.0f, 5.0f, 0.0f);
        textShape.setRotationPoint(rotationPt);
        objScale.addChild(textShape);
        colore = Tough2Viewer.dataobj.getColo3fscale();
        posSwitch = new Switch(Switch.CHILD_MASK);  //
        posSwitch.setCapability(Switch.ALLOW_SWITCH_READ);
        posSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        // Create the bit masks
        posMask = new BitSet();
        Appearance app[];
        app = new Appearance[colore.length];
        for (int index = 0; index < colore.length; index = index + 1) {
            Material mat;
            mat = new Material();
            mat.setDiffuseColor(colore[index]);
            mat.setSpecularColor(colore[index]);
            mat.setShininess(5.0f);
            app[index] = new Appearance();
            app[index].setMaterial(mat);
        }
        float lx, ly, lz, lmax;
        lx = Tough2Viewer.dataobj.get_xmax() - Tough2Viewer.dataobj.get_xmin();
        ly = Tough2Viewer.dataobj.get_ymax() - Tough2Viewer.dataobj.get_ymin();
        lz = Tough2Viewer.dataobj.get_zmax() - Tough2Viewer.dataobj.get_zmin();
        lmax = lx;
        if (lmax <= ly) {
            lmax = ly;
        }
        if (lmax <= lz) {
            lmax = lz;
        }
        xmin = -(campo / 2.0f) * lx / lmax;
        xmax = (campo / 2.0f) * lx / lmax;
        ymin = -(campo / 2.0f) * ly / lmax;
        ymax = (campo / 2.0f) * ly / lmax;
        zmin = -(campo / 2.0f) * lz / lmax;
        zmax = (campo / 2.0f) * lz / lmax;
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        ArrowShape = new Arrow[nxyz];
        BoxTransGrp = new TransformGroup[nxyz];
        Tough2Viewer.dataobj.CreateVectorData3D();
        riduzione = campo / lmax;//
        float xo1 = Tough2Viewer.dataobj.get_Xo(0);
        float yo1 = Tough2Viewer.dataobj.get_Yo(0);
        float zo1 = Tough2Viewer.dataobj.get_Zo(0);
        for (int i_b = 1; i_b < nxyz; i_b++) {
            float xi = Tough2Viewer.dataobj.get_Xo(i_b);
            float yi = Tough2Viewer.dataobj.get_Yo(i_b);
            float zi = Tough2Viewer.dataobj.get_Zo(i_b);
            //ricerca punti con ugual x,y
            if (xi == xo1) {
                if (yi == yo1) {
                    myindexZ.add(i_b);
                }

            }

            if (yi == yo1) {
                if (zi == zo1) {
                    myindexX.add(i_b);
                }

            }

            if (xi == xo1) {
                if (zi == zo1) {
                    myindexY.add(i_b);
                }

            }

        }

        lenghtVector = Tough2Viewer.dataobj.get_VectorFlowLenght();
        if (myindexZ.size() != 0) {
            lenghtVector = (zmax - zmin) / ((double) myindexZ.size());
        } else if (myindexX.size() != 0) {
            lenghtVector = (xmax - xmin) / ((double) myindexX.size());
        } else if (myindexY.size() != 0) {
            lenghtVector = (ymax - ymin) / ((double) myindexY.size());
        }
        lenghtVector_old = lenghtVector;

        for (int i_b = 0; i_b < nxyz; i_b++) {
            double xo = (double) ((Tough2Viewer.dataobj.get_Xo(i_b) - Tough2Viewer.dataobj.get_xmin()) * riduzione + xmin);
            double yo = (double) ((Tough2Viewer.dataobj.get_Yo(i_b) - Tough2Viewer.dataobj.get_ymin()) * riduzione + ymin);
            double zo = (Tough2Viewer.dataobj.get_Zo(i_b) - Tough2Viewer.dataobj.get_zmin()) * riduzione + zmin;
            int iv = 0;
            int it = 0;
            double x1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, iv * 3 + 0);
            double y1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, iv * 3 + 1);
            double z1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, iv * 3 + 2);
            double module = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
            float scalecolor = (float) (colore.length - 1.0f);
            int color_index = (int) ((module / Tough2Viewer.dataobj.get_VectorModuleStatistics(iv) * scalecolor));
            if (color_index > scalecolor) {
                int errorequi = 1;
            }

            if (module > 0) {
                x1 = x1 / Tough2Viewer.dataobj.get_VectorModuleStatistics(iv) * lenghtVector;
                y1 = y1 / Tough2Viewer.dataobj.get_VectorModuleStatistics(iv) * lenghtVector;
                z1 = z1 / Tough2Viewer.dataobj.get_VectorModuleStatistics(iv) * lenghtVector;
            }

            //create a new CylinderTransformer between the two atoms
            Point3f pointA = new Point3f((float) xo, (float) yo, (float) zo);
            Point3f pointB = new Point3f((float) (x1 + xo), (float) (y1 + yo), (float) (z1 + zo));
            CylinderTransformer cT = new CylinderTransformer(pointA, pointB);

            //get the length
            //Float length = cT.getLength();
            Float length = (float) lenghtVector;
            //holds the translation
            Transform3D transform1 = new Transform3D();
            //...move the coordinates there
            transform1.setTranslation(cT.getTranslation());

            //get the grids3dbox and angle for rotation
            AxisAngle4f rotation = cT.getAxisAngle();
            Transform3D transform2 = new Transform3D();
            transform2.setRotation(rotation);

            //combine the translation and rotation into transform1
            if (module > 0) {
                transform1.mul(transform2);
            }

            ArrowShape[i_b] = new Arrow(length * 1.5f, colore[color_index]);
            BoxTransGrp[i_b] = new TransformGroup(transform1);
            BoxTransGrp[i_b].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            BoxTransGrp[i_b].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            ID id = new ID(i_b);
            ArrowShape[i_b].setUserData(id);
            Appearance appMyBox = ArrowShape[i_b].getAppearance();
            ColoringAttributes ca = new ColoringAttributes();
            if (module > 0) {
                ca.setColor(colore[color_index]);
            } else {

                ca.setColor(new Color3f(1.0f, 1.0f, 1.0f));
            }
            appMyBox.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
            appMyBox.setColoringAttributes(ca);
            BoxTransGrp[i_b].addChild(ArrowShape[i_b]);
            posSwitch.addChild(BoxTransGrp[i_b]);
            posMask.set(i_b);
            //////////////////modifiche 21/07/10
            if (module > 0) {
                posMask.set(i_b);
            } else {

                posMask.clear(i_b);
            }
        }

        /////////////////////////////////////////////////
        // FINE CICLO INSERIMENTO Vettori ///////////////
        /////////////////////////////////////////////////
        // Set the positions mask
        posSwitch.setChildMask(posMask);
        // Throw everything into a single group
        group = new Group();
        group.addChild(posSwitch);
        objTrans.addChild(group);
//        aggiungo griglia
//        grids3dbox = new Grids3DBox( xmin, xmax, ymin,ymax, zmin, zmax,2,2,nz+2);
//
//        objTrans.addChild(grids3dbox);
        //   Arrow myArrow=new Arrow(2.0f,new Color3f(0.0f,0.0f,0.0f));
        //   objTrans.addChild(myArrow);
        BoundaryBox myBoundaryBox = new BoundaryBox(xmin, xmax, ymin, ymax, zmin, zmax);
        objTrans.addChild(myBoundaryBox);
        Point3f center = new Point3f(xmin - .02f, ymin - 0.02f, 0.0f);
        axis3d = new Axis3D(center, campo * 1.2f);
        objTrans.addChild(axis3d);
        objTrans.addChild(objScale);
        //
        textMat.setScale(f_test);
        objScale.setTransform(textMat);
        // Set up the background
        Color3f bgColor = new Color3f(1.0f, 1.0f, 1.0f);
        Background bgNode = new Background(bgColor);
        bgNode.setApplicationBounds(bounds);
        objRoot.addChild(bgNode);
        // Set up the ambient light
        Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);
        // Set up the directional lights
        Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
        Color3f light2Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        DirectionalLight light1
                = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);
        DirectionalLight light2
                = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        objRoot.addChild(light2);
        apText.setMaterial(mm);

        PickHighlightBehaviorVorFluxArrow pickBeh;
        pickBeh = new PickHighlightBehaviorVorFluxArrow(c, objRoot, bounds);

        // Have Java 3D perform optimizations on this scene graph.
        objRoot.compile();

        return objRoot;
    }

    /**
     *
     * @param position
     */
    public void removeSingleBlock(int position) {
        Appearance appMyBox = ArrowShape[position].getAppearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(new Color3f(0.0f, 0.0f, 0.f));

        appMyBox.setColoringAttributes(ca);
    }

    /**
     *
     * @param position
     * @param Zshift
     */
    public void shiftLayerXYBlock(int position, float Zshift) {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        Transform3D BoxMat = new Transform3D();
        BoxTransGrp[position].getTransform(BoxMat);
        Vector3d posXYZ = new Vector3d();
        BoxMat.get(posXYZ);
        double z_location = posXYZ.z;

        for (int i_b = 0; i_b < nxyz; i_b++) {
            BoxTransGrp[i_b].getTransform(BoxMat);
            Vector3d posXYZ_i = new Vector3d();
            BoxMat.get(posXYZ_i);
            if (z_location < posXYZ_i.z) {
                Transform3D t3dTraslation = new Transform3D();
                posXYZ_i.setZ(posXYZ_i.getZ() + Zshift);
                t3dTraslation.set(posXYZ_i);
                Matrix3d myMatrix3d = new Matrix3d();
                BoxMat.get(myMatrix3d);
                Transform3D rot = new Transform3D();
                rot.setRotation(myMatrix3d);
                t3dTraslation.mul(rot);

                BoxTransGrp[i_b].setTransform(t3dTraslation);
                //  BoxTransGrp[i_b].setTransform(BoxMat);
            }
        }

    }

    /**
     *
     * @param position
     * @param Yshift
     */
    public void shiftLayerXZBlock(int position, float Yshift) {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        Transform3D BoxMat = new Transform3D();
        BoxTransGrp[position].getTransform(BoxMat);
        Vector3d posXYZ = new Vector3d();
        BoxMat.get(posXYZ);
        double y_location = posXYZ.y;

        for (int i_b = 0; i_b < nxyz; i_b++) {
            BoxTransGrp[i_b].getTransform(BoxMat);
            Vector3d posXYZ_i = new Vector3d();
            BoxMat.get(posXYZ_i);
            if (y_location < posXYZ_i.y) {
                Transform3D t3dTraslation = new Transform3D();
                posXYZ_i.setY(posXYZ_i.getY() + Yshift);
                t3dTraslation.set(posXYZ_i);
                Matrix3d myMatrix3d = new Matrix3d();
                BoxMat.get(myMatrix3d);
                Transform3D rot = new Transform3D();
                rot.setRotation(myMatrix3d);
                t3dTraslation.mul(rot);

                BoxTransGrp[i_b].setTransform(t3dTraslation);
                //  BoxTransGrp[i_b].setTransform(BoxMat);
            }
        }
    }

    /**
     *
     * @param position
     * @param Xshift
     */
    public void shiftLayerYZBlock(int position, float Xshift) {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        Transform3D BoxMat = new Transform3D();
        BoxTransGrp[position].getTransform(BoxMat);
        Vector3d posXYZ = new Vector3d();
        BoxMat.get(posXYZ);
        double x_location = posXYZ.x;

        for (int i_b = 0; i_b < nxyz; i_b++) {
            BoxTransGrp[i_b].getTransform(BoxMat);
            Vector3d posXYZ_i = new Vector3d();
            BoxMat.get(posXYZ_i);
            if (x_location < posXYZ_i.x) {
                Transform3D t3dTraslation = new Transform3D();
                posXYZ_i.setX(posXYZ_i.getX() + Xshift);
                t3dTraslation.set(posXYZ_i);
                Matrix3d myMatrix3d = new Matrix3d();
                BoxMat.get(myMatrix3d);
                Transform3D rot = new Transform3D();
                rot.setRotation(myMatrix3d);
                t3dTraslation.mul(rot);

                BoxTransGrp[i_b].setTransform(t3dTraslation);

            }
        }

    }

    /**
     *
     */
    public void ResetBlockPositions() {
        float x, y, z;
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
            x = 0;
            y = 0;
            z = 0;
            //correggere qui i_b
            x = (Tough2Viewer.dataobj.get_Xo(i_b) - Tough2Viewer.dataobj.get_xmin()) * riduzione + xmin;
            y = (Tough2Viewer.dataobj.get_Yo(i_b) - Tough2Viewer.dataobj.get_ymin()) * riduzione + ymin;
            z = (Tough2Viewer.dataobj.get_Zo(i_b) - Tough2Viewer.dataobj.get_zmin()) * riduzione + zmin;
            Transform3D BoxMat = new Transform3D();
            BoxTransGrp[i_b].getTransform(BoxMat);
            Vector3d posXYZ = new Vector3d();
            BoxMat.get(posXYZ);
            posXYZ.setX(x);
            posXYZ.setY(y);
            posXYZ.setZ(z);
            BoxMat.set(posXYZ);
            BoxTransGrp[i_b].setTransform(BoxMat);
            //objRoot.addChild(BoxTransGrp[i_b]);
            int yes = 0;
        }
    }

    /**
     *
     * @param timestep
     * @param variablename
     */
    public void RepaintModel() {
        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int variablename = Tough2Viewer.dataobj.get_actualFluxToPlot();
        RepaintModel(timestep, variablename);
    }

    public void RepaintModel(int timestep, int variablename) {
        boolean performLenghtModification = true;
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        if (Tough2Viewer.dataobj.get_normalizeVector() == old_value_normalize) {
            performLenghtModification = false;

        }

        old_value_normalize = Tough2Viewer.dataobj.get_normalizeVector();
        if (Tough2Viewer.dataobj.get_VectorFlowLenght() == lenghtVector_old) {
            performLenghtModification = false;
        } else {
            performLenghtModification = true;
            lenghtVector_old = lenghtVector;
        }

        colore = Tough2Viewer.dataobj.getColo3fscale();
        for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
            //index1: n_block
            //index2: timestep
            //index3: variable
            // int index=(int)((Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename)-variableMinMax[0])/(rangeVariable)*239);
            double x1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, timestep, variablename * 3 + 0);
            double y1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, timestep, variablename * 3 + 1);
            double z1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, timestep, variablename * 3 + 2);
            double module = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
            float scalecolor = (float) (colore.length - 1.0f);
            int color_index = (int) ((module / Tough2Viewer.dataobj.get_VectorModuleStatistics(variablename) * scalecolor));

            double xo = (Tough2Viewer.dataobj.get_Xo(i_b) - Tough2Viewer.dataobj.get_xmin()) * (double) riduzione + xmin;
            double yo = (Tough2Viewer.dataobj.get_Yo(i_b) - Tough2Viewer.dataobj.get_ymin()) * (double) riduzione + ymin;
            double zo = (Tough2Viewer.dataobj.get_Zo(i_b) - Tough2Viewer.dataobj.get_zmin()) * (double) riduzione + zmin;
            double Xo = Tough2Viewer.dataobj.get_Xo(i_b);
            double Yo = Tough2Viewer.dataobj.get_Yo(i_b);
            double Zo = Tough2Viewer.dataobj.get_Zo(i_b);
            double ROI_xmin = Tough2Viewer.dataobj.get_ROI_xmin();
            double ROI_ymin = Tough2Viewer.dataobj.get_ROI_ymin();
            double ROI_zmin = Tough2Viewer.dataobj.get_ROI_zmin();
            double ROI_xmax = Tough2Viewer.dataobj.get_ROI_xmax();
            double ROI_ymax = Tough2Viewer.dataobj.get_ROI_ymax();
            double ROI_zmax = Tough2Viewer.dataobj.get_ROI_zmax();
            boolean isInROI = false;
            if (Xo >= ROI_xmin & Xo <= ROI_xmax) {
                if (Yo >= ROI_ymin & Yo <= ROI_ymax) {
                    if (Zo >= ROI_zmin & Zo <= ROI_zmax) {
                        isInROI = true;
                    }
                }
            }
            if (isInROI) {
                posMask.set(i_b);
            } else {
                posMask.clear(i_b);
            }
            if (module > 0) {
                x1 = x1 / module * lenghtVector;
                y1 = y1 / module * lenghtVector;
                z1 = z1 / module * lenghtVector;
            }

            //create a new CylinderTransformer between the two atoms
            Point3f pointA = new Point3f((float) xo, (float) yo, (float) zo);
            Point3f pointB = new Point3f((float) (x1 + xo), (float) (y1 + yo), (float) (z1 + zo));

            CylinderTransformer cT = new CylinderTransformer(pointA, pointB);

            //get the length
            //Float length = cT.getLength();
            Float length = (float) lenghtVector * (float) (module / Tough2Viewer.dataobj.get_VectorModuleStatistics(variablename));
            //holds the translation
            Transform3D transform1 = new Transform3D();
            //...move the coordinates there
            transform1.setTranslation(cT.getTranslation());

            //get the grids3dbox and angle for rotation
            AxisAngle4f rotation = cT.getAxisAngle();
            Transform3D transform2 = new Transform3D();
            transform2.setRotation(rotation);

            //combine the translation and rotation into transform1
            if (module > 0) {
                transform1.mul(transform2);
            }
            BoxTransGrp[i_b].setTransform(transform1);
            Appearance appMyBox = ArrowShape[i_b].getAppearance();
            if (Tough2Viewer.dataobj.get_normalizeVector()) {
                if (performLenghtModification) {
                    ArrowShape[i_b].modify_lenght((float) lenghtVector);
                }
            } else {
                ArrowShape[i_b].modify_lenght(length * 1.5f);
            }
            ColoringAttributes ca = new ColoringAttributes();
            if (module > 0) {
                ca.setColor(colore[color_index]);
            } else {
                posMask.clear(i_b);
                ca.setColor(new Color3f(1.0f, 1.0f, 1.0f));
            }
            appMyBox.setColoringAttributes(ca);
        }
        posSwitch.setChildMask(posMask);
    }

    /**
     *
     */
    public void init() {
        // myTimerShow=new TimerShow(3);
        old_value_normalize = Tough2Viewer.dataobj.get_normalizeVector();
        Container contentPane = getContentPane();
        setLayout(new BorderLayout());
        Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
//	add("Center", c);

        JPanel Test = new JPanel();
        Test.setLayout(new BorderLayout());
        Test.add("Center", c);
        contentPane.add("Center", Test);//25/02/2010
        //contentPane.add("Center", c);
        // Create GUI
        JPanel p = new JPanel();
        BoxLayout boxlayout = new BoxLayout(p,
                BoxLayout.Y_AXIS);
        p.add(createSelectionPanel());
        //p.add(createMaterialPanel());
        p.setLayout(boxlayout);

        contentPane.add("South", p);
        // Create a simple scene and attach it to the virtual universe
        BranchGroup scene = createSceneGraph(c);
        u = new SimpleUniverse(c);

        // add mouse behaviors to the ViewingPlatform
        ViewingPlatform viewingPlatform = u.getViewingPlatform();

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        u.getViewingPlatform().setNominalViewingTransform();

        // add orbit behavior to the viewing platform
        OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        orbit.setSchedulingBounds(bounds);
        viewingPlatform.setViewPlatformBehavior(orbit);

        u.addBranchGraph(scene);
        RepaintModel(0, 0);
    }

    JPanel createSelectionPanel() {

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Selection Variable"));
        //  panel.setPreferredSize(new Dimension(0, 200));//11/11/09
//
//        String values[] = {"FHEAT", "FLOH/FLOF", "FLOF","FLO(NaCl)","FLO(CO2)"," FLO(GAS)","FLO(LIQ.)"," VEL(GAS)"," VEL(LIQ.)"};
        String values[] = Tough2Viewer.dataobj.getFLOWName();
        chooseVariable = new JComboBox(values);
        chooseVariable.setSelectedIndex(0);
        chooseVariable.addActionListener(this);
        chooseVariable.setLightWeightPopupEnabled(false);
        panel.add(new JLabel("Selection variable"));
        panel.add(chooseVariable);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 2, 0);
        slider.setPaintTicks(true);

        slider.setMinimum(0);
        slider.setMaximum(Tough2Viewer.dataobj.get_TimeSteps() - 1);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setValue(0);
        Font font = new Font("Serif", Font.ITALIC, 15);
        slider.setFont(font);
        slider.setSnapToTicks(true);
        Hashtable labelTable = new Hashtable();
        for (int i = 0; i < Tough2Viewer.dataobj.get_TimeSteps(); i++) {
            String myTickLabel = String.valueOf(i);
            labelTable.put(i, new JLabel(myTickLabel));
//        labelTable.put( new Integer( 1 ), new JLabel("Slow") );
//        labelTable.put( new Integer( 2 ), new JLabel("Fast") );
        }
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
//        slider.setUnitIncrement(1);

        panel.add(slider);

        String ShiftLabels[] = {"ShiftXY", "ShiftXZ", "ShiftYZ", "ResetPositions", "Get 2D Plot", "TopView", "LeftView", "FrontView", "RightView", "BackView", "BottomView", "ParallelView", "PerspectiveView", "SetROI", "exportVectorData"};
        ShiftMode = new JComboBox(ShiftLabels);
        ShiftMode.addActionListener(this);
        ShiftMode.setSelectedIndex(0);
        ShiftMode.setLightWeightPopupEnabled(false);
        panel.add(new JLabel("Action"));
        panel.add(ShiftMode);
        ShiftMode.setLightWeightPopupEnabled(false);
        b1 = new JButton("Close");
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.RIGHT); //aka LEFT, for left-to-right locales
        b1.setMnemonic(KeyEvent.VK_D);
        b1.setActionCommand("disable");
        b1.addActionListener(this);
        b1.setToolTipText("Close the current window");
        panel.add(b1);

        return panel;
    }

    JPanel createMaterialPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Appearance Attributes"));

        String colorVals[] = {"WHITE", "RED", "GREEN", "BLUE"};

        altAppMaterialColor = new JComboBox(colorVals);
        altAppMaterialColor.addActionListener(this);

        altAppMaterialColor.setLightWeightPopupEnabled(false);
        ActionListener l;

        altAppMaterialColor.setSelectedIndex(2);
        panel.add(new JLabel("Alternate Appearance MaterialColor"));
        panel.add(altAppMaterialColor);

        appMaterialColor = new JComboBox(colorVals);
        appMaterialColor.addActionListener(this);
        appMaterialColor.setSelectedIndex(1);
        appMaterialColor.setLightWeightPopupEnabled(false);
        panel.add(new JLabel("Normal Appearance MaterialColor"));
        panel.add(appMaterialColor);

        return panel;

    }

    /**
     *
     * @param position
     */
    public void get2D(int position) {

        Model2D = new XYScatterPlotVoronoiFlux(position);
        Model2D.setVisible(true);
    }

    /**
     *
     * @param position
     */
    public void shift(int position) {

        if (ShiftMode.getSelectedIndex() == 0) {
            shiftLayerXYBlock(position, 0.5f);
        }
        if (ShiftMode.getSelectedIndex() == 1) {
            shiftLayerXZBlock(position, 0.5f);
        }
        if (ShiftMode.getSelectedIndex() == 2) {
            shiftLayerYZBlock(position, 0.5f);
        }

    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int timestep = (int) source.getValue();
            String Tooltip = Float.toString(Tough2Viewer.dataobj.get_Times(timestep)) + " S";
            slider.setToolTipText(Tooltip);
            RepaintModel(timestep, chooseVariable.getSelectedIndex());
        }
    }

    /**
     *
     */
    public void ParallelView() {
        View view = u.getViewer().getView();
        view.setProjectionPolicy(View.PARALLEL_PROJECTION);

    }

    /**
     *
     */
    public void PerspectiveView() {
        View view = u.getViewer().getView();
        view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);

    }

    /**
     *
     * @param index
     */
    public void ChangeView(int index) {

        TransformGroup viewTransformGroup = u.getViewingPlatform().getViewPlatformTransform();

        Transform3D t3dInitial = new Transform3D();
        Transform3D t3dTranslation = new Transform3D();
        Transform3D t3dRotation = new Transform3D();

        viewTransformGroup.getTransform(t3dInitial);

        Vector3f InitialPointView = new Vector3f();

        Vector3f FinalPointView[] = new Vector3f[6];
        FinalPointView[0] = new Vector3f(0.0f, 0.0f, 2.47f);//topview
        FinalPointView[1] = new Vector3f(-2.47f, 0.0f, 0.0f);//left
        FinalPointView[2] = new Vector3f(0.0f, -2.47f, 0.0f);//front
        FinalPointView[3] = new Vector3f(2.47f, 0.0f, 0.0f);//rigt
        FinalPointView[4] = new Vector3f(0.0f, 2.47f, 0.0f);//back
        FinalPointView[5] = new Vector3f(0.0f, 0.0f, -2.47f);//Bottom
        t3dInitial.get(InitialPointView);//iniziale
        Matrix3f m3d = new Matrix3f();
        t3dInitial.get(m3d);

        Matrix3f m3dtest[] = new Matrix3f[6];
        m3dtest[0] = new Matrix3f(1f, 0f, 0f,
                0f, 1f, 0f,
                0f, 0f, 1f);//topview
        m3dtest[1] = new Matrix3f(0f, 0f, -1f,
                -1f, 0f, 0f,
                0f, 1f, 0f);//left
        m3dtest[2] = new Matrix3f(1f, 0f, 0f,
                0f, 0f, -1f,
                0f, 1f, 0f);//front
        m3dtest[3] = new Matrix3f(0f, 0f, 1f,
                1f, 0f, 0f,
                0f, 1f, 0f);//rigt
        m3dtest[4] = new Matrix3f(-1f, 0f, 0f,
                0f, 0f, 1f,
                0f, 1f, 0f);//back
        m3dtest[5] = new Matrix3f(1f, 0f, 0f,
                0f, -1f, 0f,
                0f, 0f, -1f);//Bottom

        t3dRotation.setRotation(m3dtest[index]);
        t3dTranslation.setTranslation(FinalPointView[index]);
        t3dTranslation.mul(t3dRotation);

        viewTransformGroup.setTransform(t3dTranslation);
//         u.getViewingPlatform().setViewPlatform(viewTransformGroup);

    }

    public void actionPerformed(ActionEvent e) {
        Object target = e.getSource();
        int yeswearehere;
        yeswearehere = 1;
        if (target == b1) {
            this.setVisible(false);
        }

        if (target == altAppMaterialColor) {
//	    altMat.setDiffuseColor(colors[altAppMaterialColor.getSelectedIndex()]);
        } else if (target == chooseVariable) {
            RepaintModel(slider.getValue(), chooseVariable.getSelectedIndex());
        } else if (target == ShiftMode) {
            int i;
            if (ShiftMode.getSelectedIndex() == 0) {
                yeswearehere = 1;
                //		if (!shape1Enabled) {
            } else if (ShiftMode.getSelectedIndex() == 1) {
                yeswearehere = 1;
            } else if (ShiftMode.getSelectedIndex() == 2) {
                yeswearehere = 1;
            } else if (ShiftMode.getSelectedIndex() == 3) {
                ResetBlockPositions();
            } else if (ShiftMode.getSelectedIndex() == 4) {
            } else if (ShiftMode.getSelectedIndex() == 5) {
                ChangeView(0);
            } else if (ShiftMode.getSelectedIndex() == 6) {
                ChangeView(1);
            } else if (ShiftMode.getSelectedIndex() == 7) {
                ChangeView(2);
            } else if (ShiftMode.getSelectedIndex() == 8) {
                ChangeView(3);
            } else if (ShiftMode.getSelectedIndex() == 9) {
                ChangeView(4);
            } else if (ShiftMode.getSelectedIndex() == 10) {
                ChangeView(5);
            } else if (ShiftMode.getSelectedIndex() == 11) {
                ParallelView();
            } else if (ShiftMode.getSelectedIndex() == 12) {
                PerspectiveView();
            } else if (ShiftMode.getSelectedIndex() == 13) {
                SetVoi SET_ROIWindow = new SetVoi();
                SET_ROIWindow.setVisible(true);
            } else if (ShiftMode.getSelectedIndex() == 14) {
                //data flow exporting
                String lineaOUT;
                int n_times = Tough2Viewer.dataobj.get_TimeSteps();
                int n_flow_var = Tough2Viewer.dataobj.get_number_of_flux_variables();
                int n_of_cells = Tough2Viewer.dataobj.get_nxyz();
                for (int i_t = 0; i_t < n_times; i_t++) {
                    String FilePath = Tough2Viewer.dataobj.get_WorkingPath() + "\\";
                    String Filename = FilePath + "Flow_Data_" + Integer.toString(i_t) + ".dat";
                    //Open an output stream
                    try {

                        FileOutputStream fos = new FileOutputStream(Filename, false);
                        //DataOutputStream dos = new DataOutputStream(fos);
                        PrintStream ps;

                        ps = new PrintStream(fos);
                        lineaOUT = "X Y Z ";
                        String[] flowNames = Tough2Viewer.dataobj.getFLOWName();
                        for (int in = 0; in < n_flow_var; in++) {

                            lineaOUT = lineaOUT + " " + flowNames[in] + "_x " + flowNames[in] + "_y " + flowNames[in] + "_z";
                        }
                        ps.println(lineaOUT);
                        for (int j = 0; j < n_of_cells; j++) {
                            double x = Tough2Viewer.dataobj.get_Xo(j);
                            double y = Tough2Viewer.dataobj.get_Yo(j);
                            double z = Tough2Viewer.dataobj.get_Zo(j);
                            lineaOUT = Double.toString(x) + " " + Double.toString(y) + " " + Double.toString(z) + " ";
                            for (int iv = 0; iv < Tough2Viewer.dataobj.get_number_of_flux_variables(); iv++) {
                                double x1 = Tough2Viewer.dataobj.get_VectorDataArray(j, i_t, iv * 3 + 0);
                                double y1 = Tough2Viewer.dataobj.get_VectorDataArray(j, i_t, iv * 3 + 1);
                                double z1 = Tough2Viewer.dataobj.get_VectorDataArray(j, i_t, iv * 3 + 2);
                                lineaOUT = lineaOUT + " " + Double.toString(x1) + " " + Double.toString(y1) + " " + Double.toString(z1);

                            }
                            ps.println(lineaOUT);
                        }

                        fos.close();

                    } catch (IOException exc)// Catches any error conditions
                    {
                        String output = "Unable to write FLOW file";
                        Tough2Viewer.toLogFile(output);

                    }
                }

            }

        } else if (target == appMaterialColor) {

        }
    }

    /**
     *
     */
    public void destroy() {
        u.cleanup();
    }
    private javax.swing.JButton closeButton;
    private Grids3DBox grids3dbox;
    private Axis3D axis3d;
    private BranchGroup boundaryBox;
}
