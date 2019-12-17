/*
 * $RCSfile: VoronoiBlockModel3D.java,v $
 *
 * Copyright (ModelCanvas3D) 2007 Sun Microsystems, Inc. All rights reserved.
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
import javax.media.j3d.Shape3D;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.vp.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.BitSet;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

import java.util.concurrent.ForkJoinPool;

public class VoronoiBlockModel3D extends javax.swing.JFrame implements ActionListener,
        ChangeListener {

    public XYScatterPlotVoronoi Model2D;

    MouseInput1 mouse;
    KeyboardInput keyboard;
    PickHighlightBehaviorVoronoi pickBeh;
    Scale3D myscale;
    Scale3D myscale1;
    JComboBox altAppMaterialColor;
    JComboBox appMaterialColor;
    JComboBox chooseVariable;
    JComboBox ShiftMode;
    JSlider slider;
    JButton b1;
    JLabel jLabel1;
    JLabel jLabel2;
    JLabel jLabel3;
    JLabel jLabel4;
    JLabel jLabel5;
    JLabel jLabel6;
    JLabel jLabel7;
    JLabel jLabel8;
// setup font stuff
    private String fontName = "TestFont";
    private String textString = " ";//OrientedShape3D by stebond
    private String textString1 = " ";
    float sl = textString.length();
//Roba del 3D//
    private Canvas3D ModelCanvas3D;
    private Canvas3D ScaleCanvas3D;
    private SimpleUniverse ModelUniverse;
    private SimpleUniverse ScaleUniverse;
    private BranchGroup ModelobjRoot;
    private BranchGroup ScaleobjRoot;
    private BranchGroup ScaleScene;
    private BranchGroup ModelScene;
    private TransformGroup BoxTrans[];
    VoronoiBox shapeBox[];
    VoronoiPlusPlusBox myVorPlusPlusBox[];
    Switch posSwitchBox;
    BitSet posMaskBox;
    Switch posSwitchPolyShapes;
    BitSet posMaskPolyShapes;
    Switch posSwitchSurfaceShapes;
    BitSet posMaskSurfaceShapes;

    Switch posSwitchLigthModel;
    BitSet posMaskLigthModel;
    Switch posSwitchLigthScale;
    BitSet posMaskLigthScale;
    BitSet posMaskAxis;
    Switch posSwitchAxis;

    private MultiplePolyLine3D PolyShape3D[];
    private Surface_Generic_Shape3D[] Surfaces_Generic_Shape3D;
    private Surface_Raster_Shape3D[] SurfaceShape3D;
    private Surface_PLY_Shape3D[] SurfaceShape3DPLY;

    Group group;
    private OffScreenCanvas3D offScreenCanvas3D;
    private static final int OFF_SCREEN_SCALE = 2;
    private boolean initcompleted = false;
    Color3f colore[];
    float xmin, xmax, ymin, ymax, zmin, zmax;
    float riduzione;
    float campo = 1.0f;
    private int num_vertex = 0;
    private DirectionalLight lgt1;
    private DirectionalLight lgt2;
    private DirectionalLight lgt3;
    private AmbientLight aLgt;
    private AmbientLight aLgtScale;
    private float[][] myIndexArray;
    Background bgNode;
    private SetVoi SET_VOIWindow;
    public AdvancedBlockSelection3D AdvancedBlockSelection3D_window;
    private SurfaceControl mySrfCtrl;
    private RotateBehaviorZ awtBehaviorZ;
    public ArrayList selected_blocks;
    private int[] surface_index;

    public VoronoiBlockModel3D() {
//        super(parent);
        init();
        getRootPane().setDefaultButton(closeButton);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setTitle("3D Block model");
        this.setPreferredSize(new Dimension(600, 600));
        this.setMinimumSize(new Dimension(400, 400));
        initcompleted = true;
        selected_blocks = new ArrayList();
    }

    public void setAxisVisibility(Boolean visible) {
        if (visible) {
            posMaskAxis.set(0);
        } else {
            posMaskAxis.clear(0);
        }
        posSwitchAxis.setChildMask(posMaskAxis);
    }

    public void update_scale_scene() {

        ScaleUniverse.getLocale().removeBranchGraph(ScaleScene);
        ScaleScene = null;
        ScaleScene = createSceneGraphScale(ScaleCanvas3D);
        ScaleUniverse.addBranchGraph(ScaleScene);

    }

    public void update_model_scene() {

        ModelUniverse.getLocale().removeBranchGraph(ModelScene);
        ModelScene = null;
        ModelScene = createSceneGraphModel(ModelCanvas3D);
        ModelUniverse.addBranchGraph(ModelScene);//12-06-04 (rimossa linea)
        VoronoiBlockModel3D.this.repaint_model();

    }

    public void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        ModelCanvas3D = new Canvas3D(config);
        mouse = new MouseInput1();
        keyboard = new KeyboardInput();
        ModelCanvas3D.addKeyListener(keyboard);
        addKeyListener(keyboard);
        ModelCanvas3D.addMouseMotionListener(mouse);
        ModelCanvas3D.addMouseListener(mouse);
        JPanel ModelPanel = new JPanel();
        ModelPanel.setLayout(new BorderLayout());
//        BoxLayout boxlayout0 = new BoxLayout(ModelPanel,  BoxLayout.X_AXIS);
        ModelPanel.add(ModelCanvas3D);
        //12-06-22
        //GraphicsConfiguration config  =GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice( ).getDefaultConfiguration( );
        //old
        ScaleCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        //NEW
        // ScaleCanvas3D = new Canvas3D(config); NON FUNZIONA
        Dimension p = new Dimension(300, 300);

        ScaleCanvas3D.setPreferredSize(p);
        // ScaleCanvas3D.setMaximumSize(p);
        //ScaleCanvas3D.setSize(50, 400);

        //ModelPanel.add( ScaleCanvas3D);
        JPanel ScalePanel = new JPanel();
        ScalePanel.setLayout(new BorderLayout());
        ScalePanel.add(ScaleCanvas3D, BorderLayout.CENTER);

//        contentPane.add( ScalePanel,BorderLayout.CENTER);//25/02/2010
        //ModelPanel.setLayout(boxlayout0);
        //contentPane.add( ModelPanel,BorderLayout.CENTER);
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ModelPanel, ScalePanel);
        splitpane.setResizeWeight(1);
        int iii = splitpane.getDividerSize();
        splitpane.setDividerSize(iii * 3);
        splitpane.setOneTouchExpandable(true);
        splitpane.setContinuousLayout(true);
        contentPane.add(splitpane, BorderLayout.CENTER);
        // Create GUI
        JPanel CommandPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(CommandPanel, BoxLayout.Y_AXIS);
        CommandPanel.add(createSelectionPanel());
        CommandPanel.add(createMaterialPanel());
        CommandPanel.setLayout(boxlayout);
        CommandPanel.addKeyListener(keyboard);
        contentPane.add(CommandPanel, BorderLayout.SOUTH);

        ////////////////Roba del 3D
        // Create a simple ModelScene and attach it to the virtual universe
        ModelScene = createSceneGraphModel(ModelCanvas3D);
        ModelUniverse = new SimpleUniverse(ModelCanvas3D);
        // add mouse behaviors to the ViewingPlatform
        ViewingPlatform ModelViewingPlatform = ModelUniverse.getViewingPlatform();
        // This will move the ViewPlatform back a bit so the
        // objects in the ModelScene can be viewed.
        ModelUniverse.getViewingPlatform().setNominalViewingTransform();
        // add ModelOrbit behavior to the viewing platform
        OrbitBehavior ModelOrbit = new OrbitBehavior(ModelCanvas3D, OrbitBehavior.REVERSE_ALL);
        //ModelOrbit.setZoomFactor(.001f);
        campo = Tough2Viewer.dataobj.get_Campo();
        BoundingSphere ModelBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0 * campo * 2.0);
        ModelOrbit.setSchedulingBounds(ModelBounds);
        ModelViewingPlatform.setViewPlatformBehavior(ModelOrbit);
//        createOffScreenCanvas(ModelCanvas3D);
        ModelUniverse.addBranchGraph(ModelScene);

        //////////////////////////////////////////////////
        //////////////////////////////////////////////////
        ScaleScene = createSceneGraphScale(ScaleCanvas3D);
        ScaleUniverse = new SimpleUniverse(ScaleCanvas3D);
        // add mouse behaviors to the ViewingPlatform
        ViewingPlatform ScaleViewingPlatform = ScaleUniverse.getViewingPlatform();
        // This will move the ViewPlatform back a bit so the
        // objects in the ModelScene can be viewed.
        ScaleUniverse.getViewingPlatform().setNominalViewingTransform();
        // add ModelOrbit behavior to the viewing platform
        OrbitBehavior ScaleOrbit = new OrbitBehavior(ScaleCanvas3D, OrbitBehavior.REVERSE_ALL);
        BoundingSphere ScaleBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0 * campo * 2.0);
        ScaleOrbit.setSchedulingBounds(ScaleBounds);
        ScaleViewingPlatform.setViewPlatformBehavior(ScaleOrbit);
        ScaleUniverse.addBranchGraph(ScaleScene);
        ////////////////////////////////////////////
        ////////////////////////////////////////////

    }
//@Action public void closeAboutBox() {
//        setVisible(false);
//    }
//public class BlockModel3D extends Applet {

    public BranchGroup createSceneGraphModel(Canvas3D c) {

        // Create the root of the branch graph
        ModelobjRoot = new BranchGroup();
        ModelobjRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        ModelobjRoot.setCapability(BranchGroup.ALLOW_DETACH);

        //27/07/2011
        ModelobjRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        ModelobjRoot.setCapability(Group.ALLOW_CHILDREN_READ);
        ModelobjRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);

        TransformGroup textTransformGroup = new TransformGroup();
        Transform3D textTransform = new Transform3D();
        // Assuming uniform size chars, set scale to fit string in view
        float f_test;
        f_test = 1.20f / sl;
        textTransform.setScale(f_test);
        textTransformGroup.setTransform(textTransform);

        // Create the transform group node and initialize it to the
        // identity.  Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at runtime.  Add it to the
        // root of the subgraph.
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objTrans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        ModelobjRoot.addChild(objTrans);
        campo = Tough2Viewer.dataobj.get_Campo();
        awtBehaviorZ = new RotateBehaviorZ(objTrans);
        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0 * campo * 2);
        awtBehaviorZ.setSchedulingBounds(bounds);
        ModelobjRoot.addChild(awtBehaviorZ);
        Appearance apText = new Appearance();
        Material m = new Material();
        m.setLightingEnable(true);
        apText.setMaterial(m);
// create 3D text
        Font3D f3d = new Font3D(new Font(fontName, Font.PLAIN, 2), new FontExtrusion());

        Point3f textPt = new Point3f(-1.0f, -1.0f, -1.0f);
        Text3D textText3D = new Text3D(f3d, textString, textPt);
        OrientedShape3D textShape = new OrientedShape3D();
        textShape.setGeometry(textText3D);
        textShape.setAppearance(apText);

        textShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
        // text is centered around 0, 3, 0.  Make it rotate around 0,5,0
        Point3f rotationPt = new Point3f(0.0f, 5.0f, 0.0f);
        textShape.setRotationPoint(rotationPt);
        textTransformGroup.addChild(textShape);

        colore = Tough2Viewer.dataobj.getColo3fscale();
        Tough2Viewer.dataobj.set_INIT_ROI();
        posSwitchBox = new Switch(Switch.CHILD_MASK);  //
        posSwitchBox.setCapability(Switch.ALLOW_SWITCH_READ);
        posSwitchBox.setCapability(Switch.ALLOW_SWITCH_WRITE);
        // Create the bit masks
        posMaskBox = new BitSet();

        double contrazione = Tough2Viewer.dataobj.get_ShrinkFactor();
        double Zfactor = Tough2Viewer.dataobj.get_Zfactor();
        campo = Tough2Viewer.dataobj.get_Campo();
        float lx, ly, lz, lmax;
        lx = Tough2Viewer.dataobj.get_xmax() - Tough2Viewer.dataobj.get_xmin();
        ly = Tough2Viewer.dataobj.get_ymax() - Tough2Viewer.dataobj.get_ymin();
        lz = Tough2Viewer.dataobj.get_zmax() - Tough2Viewer.dataobj.get_zmin();
        double xminR = Tough2Viewer.dataobj.get_xmin();
        double yminR = Tough2Viewer.dataobj.get_ymin();
        double zminR = Tough2Viewer.dataobj.get_zmin();
        lmax = lx;
        if (lmax <= ly) {
            lmax = ly;
        }
        if (lmax <= lz) {
            lmax = lz;
        }
        if (lmax == 0) {
            String error = "L max=0. Check IN file or MESH file";
            JOptionPane.showMessageDialog(null, error);

        }
        riduzione = campo / lmax;
        xmin = -0.5f * campo * lx / lmax;
        xmax = 0.5f * campo * lx / lmax;
        ymin = -0.5f * campo * ly / lmax;
        ymax = 0.5f * campo * ly / lmax;
        zmin = -0.5f * campo * lz / lmax * (float) Zfactor;
        zmax = 0.5f * campo * lz / lmax * (float) Zfactor;

        int nxyz = Tough2Viewer.dataobj.get_nxyz();

        // nz=1;
        if (Tough2Viewer.dataobj.ID_grid_type < 2) {
            shapeBox = new VoronoiBox[nxyz];
        } else {
            myVorPlusPlusBox = new VoronoiPlusPlusBox[nxyz];
        }

        BoxTrans = new TransformGroup[nxyz];

        float variableMinMax[];
        variableMinMax = new float[4];
        float rangeVariable;
        variableMinMax[0] = Tough2Viewer.dataobj.get_GlobalScale(0, 0);
        variableMinMax[1] = Tough2Viewer.dataobj.get_GlobalScale(0, 1);

        rangeVariable = variableMinMax[1] - variableMinMax[0];
        if (rangeVariable <= 0) {
            rangeVariable = 1.0f;
        }
        ArrayList[] voronoiGeometryBox = new ArrayList[0];
        if (Tough2Viewer.dataobj.ID_grid_type == 0) {
            voronoiGeometryBox = Tough2Viewer.dataobj.GetRegularGeometryBox();

        } else if (Tough2Viewer.dataobj.ID_grid_type == 1) {
            voronoiGeometryBox = Tough2Viewer.dataobj.GetvoronoiGeometryBox();
        }

        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int variablename = Tough2Viewer.dataobj.get_actualVariableToPlot();
        int num_block = 0;
//   if(Tough2Viewer.dataobj.ID_grid_type==2){
//       int t2vdat=Tough2Viewer.dataobj.VoroPPData.size();
//       nxyz=Math.min(t2vdat,nxyz);
//   }

        for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
            float scalecolor = (float) (colore.length - 1);
            int color_index = (int) ((Tough2Viewer.dataobj.get_dataArray(num_block, timestep, variablename) - variableMinMax[0]) / (rangeVariable) * scalecolor);
            if (color_index > scalecolor) {
                int errorequi = 1;
                color_index = colore.length - 1;
            }
            if (color_index < 0) {
                int errorequi = 1;
                color_index = 0;
            }

            double xo = (double) ((Tough2Viewer.dataobj.get_Xo(i_b) - Tough2Viewer.dataobj.get_xmin()) * riduzione + xmin);
            double yo = (double) ((Tough2Viewer.dataobj.get_Yo(i_b) - Tough2Viewer.dataobj.get_ymin()) * riduzione + ymin);
            double zo = (Tough2Viewer.dataobj.get_Zo(i_b) - Tough2Viewer.dataobj.get_zmin()) * riduzione * (float) Zfactor + zmin;
            Transform3D BoxMat = new Transform3D();
            BoxTrans[num_block] = new TransformGroup(BoxMat);
            BoxMat.set(new Vector3d(xo, yo, zo));
            BoxTrans[num_block].setTransform(BoxMat);
            BoxTrans[num_block].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);// qui da errore
            BoxTrans[num_block].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            //
            // Questo codice evita il problema di creare un shape Box tradizionale che � fortemente limitativo
            //
            //
            //numero vertici=numvertex
            // coordinate vertici vertices[]
            //Da qui leggo la geometria.....
            if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                int numvertex = voronoiGeometryBox[i_b].size() - 1;
                num_vertex = num_vertex + numvertex;
                double[][] vertices = new double[numvertex][2];
                double[] theta = new double[numvertex];
                for (int i = 0; i < numvertex; i++) {
                    Point2d temp = (Point2d) voronoiGeometryBox[i_b].get(i + 1);
                    vertices[i][0] = (temp.x - Tough2Viewer.dataobj.get_xmin()) * riduzione + xmin;
                    vertices[i][1] = (temp.y - Tough2Viewer.dataobj.get_ymin()) * riduzione + ymin;
                    double deltax = vertices[i][0] - xo;
                    double deltay = vertices[i][1] - yo;
                    double r = Math.sqrt(deltax * deltax + deltay * deltay);
                    theta[i] = Math.atan2(deltay, deltax);
                    if (theta[i] < 0) {
                        theta[i] = theta[i] + 2 * Math.PI;
                    }
                    vertices[i][0] = r * Math.cos(theta[i]) * contrazione;
                    vertices[i][1] = r * Math.sin(theta[i]) * contrazione;
                }
                //////////////////////////////////////////////////////////////////
                //effettuare controllo e ordinamento antiorario vertici........
                ////////////////////////////////////////////////////
                for (int jj = 0; jj < numvertex; jj++) {
                    for (int ii = jj; ii < numvertex; ii++) {
                        if (theta[jj] > theta[ii]) {
                            double temptheta = theta[jj];
                            theta[jj] = theta[ii];
                            theta[ii] = temptheta;
                            double tempX = vertices[jj][0];
                            vertices[jj][0] = vertices[ii][0];
                            vertices[ii][0] = tempX;
                            double tempY = vertices[jj][1];
                            vertices[jj][1] = vertices[ii][1];
                            vertices[ii][1] = tempY;
                        }//fine if
                    }//fine for
                }//fine for
                float dimz = Tough2Viewer.dataobj.get_DimBlockZ(i_b) * riduzione * (float) contrazione * (float) Zfactor;

                if (dimz < 0.001) {//correzione per spessori troppo piccoli
                    dimz = 0.001f;
                }

                shapeBox[num_block] = new VoronoiBox(numvertex, vertices, dimz);
                shapeBox[num_block].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
                shapeBox[num_block].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                shapeBox[num_block].setCapability(Shape3D.ALLOW_GEOMETRY_READ);
                shapeBox[num_block].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
                ID id = new ID(num_block);
                shapeBox[num_block].setUserData(id);
            } else if (Tough2Viewer.dataobj.ID_grid_type == 2) {
                ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(i_b);
                Color3f objColor = colore[color_index];
                myVorPlusPlusBox[num_block] = new VoronoiPlusPlusBox(singleBox, contrazione, riduzione, xmin, ymin, zmin, xminR, yminR, zminR, objColor, 1, Zfactor);  //era 0      
                myVorPlusPlusBox[num_block].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
                myVorPlusPlusBox[num_block].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                myVorPlusPlusBox[num_block].setCapability(Shape3D.ALLOW_GEOMETRY_READ);
                myVorPlusPlusBox[num_block].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
                ID id = new ID(num_block);
                myVorPlusPlusBox[num_block].setUserData(id);                                                                                                                       // ^:make box centered in 0,0,0                                                          
            }

            Appearance appMyBox = new Appearance();
            appMyBox.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
            appMyBox.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
            appMyBox.setCapability(Appearance.ALLOW_MATERIAL_READ);
            appMyBox.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
            ColoringAttributes ca = new ColoringAttributes();
            if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
                // Globally used colors
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
                Color3f objColor = colore[color_index];
                Material matMybox = new Material(objColor, black, objColor, white, 100.f);
                //Material matMybox=new Material(objColor,black,objColor,white,100.f);
                matMybox.setLightingEnable(true);
                appMyBox.setMaterial(matMybox);
                if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                    shapeBox[num_block].setAppearance(appMyBox);
                } else {
                    myVorPlusPlusBox[num_block].setAppearance(appMyBox);
                }

            } else {
                appMyBox = shapeBox[num_block].getAppearance();
                ca.setColor(colore[color_index]);
                appMyBox.setColoringAttributes(ca);
            }

            if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                BoxTrans[num_block].addChild(shapeBox[num_block]);
            } else {
                BoxTrans[num_block].addChild(myVorPlusPlusBox[num_block]);
            }
            //BoxTrans[num_block].addChild(shapeBox[num_block]);
            posSwitchBox.addChild(BoxTrans[num_block]);
            boolean visible_i_b = Tough2Viewer.dataobj.is_rocktype_visible(Tough2Viewer.dataobj.get_RockType(num_block));
            if (visible_i_b) {
                posMaskBox.set(num_block);
            } else {
                posMaskBox.clear(num_block);
            }
            num_block = num_block + 1;
        }

        /////////////////////////////////////////////////
        // FINE CICLO INSERIMENTO Vettori ///////////////
        /////////////////////////////////////////////////
        // Set the positions mask
        posSwitchBox.setChildMask(posMaskBox);
        // Throw everything into a single group
        group = new Group();
        group.addChild(posSwitchBox);
        Point3f center = new Point3f(xmin - .02f, ymin - 0.02f, 0.0f);

        posSwitchAxis = new Switch(Switch.CHILD_MASK);  //
        posSwitchAxis.setCapability(Switch.ALLOW_SWITCH_READ);
        posSwitchAxis.setCapability(Switch.ALLOW_SWITCH_WRITE);
        Axis3D axis3d = new Axis3D(center, campo * 1.2f);
        posMaskAxis = new BitSet();

        posMaskAxis.set(0);
        posSwitchAxis.setChildMask(posMaskAxis);
        posSwitchAxis.addChild(axis3d);
        group.addChild(posSwitchAxis);

        //objTrans.addChild(axis3d); //2013-11-25
        //////////////////INSERIMENTO SHAPES
        //////////////////
        ArrayList myShapes = Tough2Viewer.dataobj.get_shapes();
        int numShapes = myShapes.size();

        ArrayList myShapes2 = Tough2Viewer.dataobj.Surfaces_Data;
        int numShapes2 = myShapes2.size();
        PolyShape3D = new MultiplePolyLine3D[numShapes2];
        posSwitchPolyShapes = new Switch(Switch.CHILD_MASK);  //
        posSwitchPolyShapes.setCapability(Switch.ALLOW_SWITCH_READ);
        posSwitchPolyShapes.setCapability(Switch.ALLOW_SWITCH_WRITE);

        // Create the bit masks
        posMaskPolyShapes = new BitSet();
        /////////////////////////////////////
        posSwitchSurfaceShapes = new Switch(Switch.CHILD_MASK);  //
        posSwitchSurfaceShapes.setCapability(Switch.ALLOW_SWITCH_READ);
        posSwitchSurfaceShapes.setCapability(Switch.ALLOW_SWITCH_WRITE);
        // Create the bit masks
        posMaskSurfaceShapes = new BitSet();
        //////////////////////////////////////

        //adding shape surfaces and polylines using the new method
        Surfaces_Generic_Shape3D = new Surface_Generic_Shape3D[numShapes2];
        for (int i1 = 0; i1 < numShapes2; i1++) {
            //adding polyline
            SurfaceData mySurfaceData = (SurfaceData) myShapes2.get(i1);
            ArrayList myAllPointAL = (ArrayList) mySurfaceData.get_PolyShape();
            Color3f myColorPoly = mySurfaceData.get_shape_color3f();
            float x_min = Tough2Viewer.dataobj.get_xmin();
            float y_min = Tough2Viewer.dataobj.get_ymin();
            float z_min = Tough2Viewer.dataobj.get_zmin();
            PolyShape3D[i1] = new MultiplePolyLine3D(myAllPointAL, x_min, y_min, z_min, riduzione, xmin, ymin, zmin, myColorPoly, Zfactor);
            PolyShape3D[i1].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
            PolyShape3D[i1].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            Appearance appMyBox = new Appearance();
            ColoringAttributes ca = new ColoringAttributes();
            if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                appMyBox = shapeBox[i1].getAppearance();

            } else {
                appMyBox = myVorPlusPlusBox[i1].getAppearance();
            }
            ca.setColor(myColorPoly);
            appMyBox.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
            appMyBox.setColoringAttributes(ca);
            posSwitchPolyShapes.addChild(PolyShape3D[i1]);
            if (mySurfaceData.isPolyVisible()) {
                posMaskPolyShapes.set(i1);
            } else {
                posMaskPolyShapes.clear(i1);
            }
            //
            //Adding Surface Shapes
            //
            if (mySurfaceData.get_type() == 0) {
                GraphData temp = (GraphData) mySurfaceData.get_GraphData();
                double x_offset = (temp.get_X_Min() - Tough2Viewer.dataobj.get_xmin()) * riduzione;
                double y_offset = (temp.get_Y_Min() - Tough2Viewer.dataobj.get_ymin()) * riduzione;
                Surfaces_Generic_Shape3D[i1] = new Surface_Generic_Shape3D(temp, riduzione, xmin + (float) x_offset, ymin + (float) y_offset, zmin, Zfactor);
            } else {
                PLYParser myPlyData = mySurfaceData.get_PLYParser();
                Surfaces_Generic_Shape3D[i1] = new Surface_Generic_Shape3D(myPlyData, xmin, ymin, zmin, xminR, yminR, zminR, riduzione, Zfactor);
            }
            posSwitchSurfaceShapes.addChild(Surfaces_Generic_Shape3D[i1]);
            if (mySurfaceData.isSurfaceVisible()) {
                posMaskSurfaceShapes.set(i1);
            } else {
                posMaskSurfaceShapes.clear(i1);
            }

            if (mySurfaceData.isSurfaceVisible()) {
                posMaskSurfaceShapes.set(i1);
            } else {
                posMaskSurfaceShapes.clear(i1);
            }

        }

        posSwitchPolyShapes.setChildMask(posMaskPolyShapes);
        group.addChild(posSwitchPolyShapes);

        posSwitchSurfaceShapes.setChildMask(posMaskSurfaceShapes);
        group.addChild(posSwitchSurfaceShapes);

        //ending adding surface new method
        //Adding polylines ... old method
        if (false) {
            Color3f[] mycolor = Tough2Viewer.dataobj.get_ShapesColor();
            boolean[] visiblePolyShapes = Tough2Viewer.dataobj.get_PolyShapesVisible();
            boolean[] visibleSurfaceShapes = Tough2Viewer.dataobj.get_SurfaceShapesVisible();
            for (int i = 0; i < numShapes2; i++) {
                ArrayList myAllPointAL = (ArrayList) myShapes.get(i);
                float x_min = Tough2Viewer.dataobj.get_xmin();
                float y_min = Tough2Viewer.dataobj.get_ymin();
                float z_min = Tough2Viewer.dataobj.get_zmin();
                PolyShape3D[i] = new MultiplePolyLine3D(myAllPointAL, x_min, y_min, z_min, riduzione, xmin, ymin, zmin, mycolor[i], Zfactor);
                PolyShape3D[i].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
                PolyShape3D[i].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                Appearance appMyBox = new Appearance();
                ColoringAttributes ca = new ColoringAttributes();
                if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                    appMyBox = shapeBox[i].getAppearance();

                } else {
                    appMyBox = myVorPlusPlusBox[i].getAppearance();
                }

                ca.setColor(mycolor[i]);
                appMyBox.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
                appMyBox.setColoringAttributes(ca);
                posSwitchPolyShapes.addChild(PolyShape3D[i]);
                if (visiblePolyShapes[i]) {
                    posMaskPolyShapes.set(i);
                } else {
                    posMaskPolyShapes.clear(i);
                }

            }
            posSwitchPolyShapes.setChildMask(posMaskPolyShapes);
            group.addChild(posSwitchPolyShapes);

            //END ADDING POLYLINES
            //ADDING THE SURFACES
            ArrayList ShapeSurfaces = Tough2Viewer.dataobj.get_ShapesSurface();
            SurfaceShape3D = new Surface_Raster_Shape3D[ShapeSurfaces.size()];
            for (int i = 0; i < ShapeSurfaces.size(); i++) {
                GraphData temp = (GraphData) ShapeSurfaces.get(i);
                double x_offset = (temp.get_X_Min() - Tough2Viewer.dataobj.get_xmin()) * riduzione;
                double y_offset = (temp.get_Y_Min() - Tough2Viewer.dataobj.get_ymin()) * riduzione;
                SurfaceShape3D[i] = new Surface_Raster_Shape3D(temp, riduzione, xmin + (float) x_offset, ymin + (float) y_offset, zmin, Zfactor);
                posSwitchSurfaceShapes.addChild(SurfaceShape3D[i]);
                if (visibleSurfaceShapes[i]) {
                    posMaskSurfaceShapes.set(i);
                } else {
                    posMaskSurfaceShapes.clear(i);
                }

            }
            //    posSwitchSurfaceShapes.setChildMask(posMaskSurfaceShapes);
            //    group.addChild(posSwitchSurfaceShapes);
            //    //////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////
            ///////////END ADDING RASTER SURFACES/////////////////////////////
            //////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////
            ///////////ADDING PLY SURFACES/////////////////////////////
            //////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////
            //    Switch posSwitchSurfaceShapesPLY;
            //    BitSet posMaskSurfaceShapesPLY;
            //   
            SurfaceShape3DPLY = new Surface_PLY_Shape3D[Tough2Viewer.dataobj.myPlyFiles.size()];
            boolean[] visibleSurfaceShapesPLY = Tough2Viewer.dataobj.get_SurfaceShapesVisiblePLY();
            for (int i = 0; i < Tough2Viewer.dataobj.myPlyFiles.size(); i++) {
                PLYParser myPlyFile = (PLYParser) Tough2Viewer.dataobj.myPlyFiles.get(i);
                //double x_offset=(temp.get_X_Min()-Tough2Viewer.dataobj.get_xmin())*riduzione;
                //double y_offset=(temp.get_Y_Min()-Tough2Viewer.dataobj.get_ymin())*riduzione;
                //public Surface_PLY_Shape3D(PLYParser ply_data,float xmin,float ymin,float zmin,float scala,double Zfactor ){
                SurfaceShape3DPLY[i] = new Surface_PLY_Shape3D(myPlyFile, xmin, ymin, zmin, xminR, yminR, zminR, riduzione, Zfactor);

                posSwitchSurfaceShapes.addChild(SurfaceShape3DPLY[i]);
                if (visibleSurfaceShapesPLY[i]) {
                    posMaskSurfaceShapes.set(i + ShapeSurfaces.size());
                } else {
                    posMaskSurfaceShapes.clear(i + ShapeSurfaces.size());
                }
            }

            //////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////
            ///////////END ADDING PLY SURFACES/////////////////////////////
            //////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////
            posSwitchSurfaceShapes.setChildMask(posMaskSurfaceShapes);
            group.addChild(posSwitchSurfaceShapes);
        }
////////////////////////////////////////////////////////////////////////////////    
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
        objTrans.addChild(group);
        objTrans.addChild(textTransformGroup);
        textTransform.setScale(f_test);
        textTransformGroup.setTransform(textTransform);
        // Set up the background

        bgNode = new Background(Tough2Viewer.dataobj.bgColor);
        bgNode.setApplicationBounds(bounds);
        bgNode.setCapability(Background.ALLOW_COLOR_READ);
        bgNode.setCapability(Background.ALLOW_COLOR_WRITE);
        ModelobjRoot.addChild(bgNode);

        Tough2Viewer.toLogFile("number of vertex= " + Integer.toString(num_vertex));
        if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
            // Shine it with two lights.
            Color3f lColor1 = new Color3f(0.9f, 0.9f, 0.9f);
            Color3f lColor2 = new Color3f(0.7f, 0.7f, 0.7f);
            Color3f lColor3 = new Color3f(0.7f, 0.7f, 0.7f);
            Vector3f lDir1 = new Vector3f(-2.0f, -1.0f, -1.0f);//era -1,-1,-1
            Vector3f lDir2 = new Vector3f(0.0f, 0.0f, -1.0f);
            Vector3f lDir3 = new Vector3f(2.0f, 1.0f, +1.0f);

            lgt1 = new DirectionalLight(lColor1, lDir1);
            lgt2 = new DirectionalLight(lColor2, lDir2);
            lgt3 = new DirectionalLight(lColor3, lDir3);

            lgt1.setEnable(true);
            lgt2.setEnable(true);
            lgt3.setEnable(true);

            lgt1.setInfluencingBounds(bounds);
            lgt2.setInfluencingBounds(bounds);
            lgt3.setInfluencingBounds(bounds);

            lgt1.setCapability(DirectionalLight.ALLOW_STATE_READ);
            lgt2.setCapability(DirectionalLight.ALLOW_STATE_READ);
            lgt3.setCapability(DirectionalLight.ALLOW_STATE_READ);

            lgt1.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
            lgt2.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
            lgt3.setCapability(DirectionalLight.ALLOW_STATE_WRITE);

            lgt1.setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE);
            lgt1.setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE);
            lgt2.setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE);
            lgt2.setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE);
            lgt3.setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE);
            lgt3.setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE);

            Color3f alColor = new Color3f(0.9f, 0.9f, 0.9f);
            aLgt = new AmbientLight(alColor);
            aLgt.setInfluencingBounds(bounds);
            posSwitchLigthModel = new Switch(Switch.CHILD_MASK);  //
            posSwitchLigthModel.setCapability(Switch.ALLOW_SWITCH_READ);
            posSwitchLigthModel.setCapability(Switch.ALLOW_SWITCH_WRITE);
            posMaskLigthModel = new BitSet();
            posSwitchLigthModel.addChild(lgt1);
            posSwitchLigthModel.addChild(lgt2);
            posSwitchLigthModel.addChild(lgt3);
            posSwitchLigthModel.addChild(aLgt);
            for (int i_light = 0; i_light < 4; i_light++) {
                if (Tough2Viewer.dataobj.light_3D[i_light]) {
                    posMaskLigthModel.set(i_light);
                } else {
                    posMaskLigthModel.clear(i_light);
                }
            }
            posSwitchLigthModel.setChildMask(posMaskLigthModel);
            ModelobjRoot.addChild(posSwitchLigthModel);
        } else {
            Color3f lColor1 = new Color3f(0.7f, 0.7f, 0.7f);
            Vector3f lDir1 = new Vector3f(-1.0f, -1.0f, -1.0f);
            Color3f alColor = new Color3f(0.2f, 0.2f, 0.2f);
            AmbientLight aLgt = new AmbientLight(alColor);
            aLgt.setInfluencingBounds(bounds);
            lgt1 = new DirectionalLight(lColor1, lDir1);
            lgt1.setInfluencingBounds(bounds);
            ModelobjRoot.addChild(aLgt);
            ModelobjRoot.addChild(lgt1);
        }
        ///////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////
        //////////////////////////////////////////////////////

        pickBeh = new PickHighlightBehaviorVoronoi(c, ModelobjRoot, bounds);

        //qui inserisco un timer per ora, ma poi lo tolgo....
        // Have Java 3D perform optimizations on this ModelScene graph.
        float xo = Tough2Viewer.dataobj.get_Xo(0);
        float yo = Tough2Viewer.dataobj.get_Yo(0);
        ArrayList myindex = new ArrayList();
        myindex.add(0);
        for (int i_b = 1; i_b < nxyz; i_b++) {
            float xi = Tough2Viewer.dataobj.get_Xo(i_b);
            float yi = Tough2Viewer.dataobj.get_Yo(i_b);
            if (xi == xo) {
                if (yi == yo) {
                    myindex.add(i_b);
                }

            }
        }
        myIndexArray = new float[myindex.size()][2];
        for (int i = 0; i < myindex.size(); i++) {
            myIndexArray[i][0] = Tough2Viewer.dataobj.get_Zo((Integer) myindex.get(i));
            myIndexArray[i][1] = (float) ((Integer) myindex.get(i));
        }
        Tough2Viewer.dataobj.sort(myIndexArray, myIndexArray.length);
        setAxisVisibility(Tough2Viewer.dataobj.axis3dvisible);
        ModelobjRoot.compile();
        return ModelobjRoot;
    }

    public BranchGroup createSceneGraphScale(Canvas3D c) {
        ScaleobjRoot = new BranchGroup();
        ScaleobjRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        ScaleobjRoot.setCapability(BranchGroup.ALLOW_DETACH);
        TransformGroup textTransformGroup = new TransformGroup();
        Transform3D textTransform = new Transform3D();
        // Assuming uniform size chars, set scale to fit string in view
        float f_test;
        f_test = 1.20f / sl;
        textTransform.setScale(f_test);
        textTransformGroup.setTransform(textTransform);
        // Create the transform group node and initialize it to the
        // identity.  Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at runtime.  Add it to the
        // root of the subgraph.
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objTrans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        ScaleobjRoot.addChild(objTrans);
        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0 * campo * 2);
        Appearance apText = new Appearance();
        Material m = new Material();
        m.setLightingEnable(true);
        apText.setMaterial(m);
// create 3D text
        Font3D f3d = new Font3D(new Font(fontName, Font.PLAIN, 2), new FontExtrusion());
        Point3f textPt = new Point3f(-1.0f, -1.0f, -1.0f);
        Text3D textText3D = new Text3D(f3d, textString1, textPt);
        OrientedShape3D textShape = new OrientedShape3D();
        textShape.setGeometry(textText3D);
        textShape.setAppearance(apText);
        textShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
        // text is centered around 0, 3, 0.  Make it rotate around 0,5,0
        Point3f rotationPt = new Point3f(0.0f, 5.0f, 0.0f);
        textShape.setRotationPoint(rotationPt);
        textTransformGroup.addChild(textShape);

        colore = Tough2Viewer.dataobj.getColo3fscale();
        float variableMinMax[];
        variableMinMax = new float[4];
//   Tough2Viewer.dataobj.minmaxDataArray( 0, variableMinMax);
        float rangeVariable;
        variableMinMax[0] = Tough2Viewer.dataobj.get_GlobalScale(0, 0);
        variableMinMax[1] = Tough2Viewer.dataobj.get_GlobalScale(0, 1);

        Point3f centerScale = new Point3f(0.f, 0.f, 0.f);
        Point3f endScale = new Point3f(0.0f, 0.0f, -1.0f);
        myscale1 = new Scale3D(centerScale, 1.0f, colore);

        objTrans.addChild(myscale1);
        String variable_name = Tough2Viewer.dataobj.get_variables_name(0);
        myscale1.set_text(variable_name + " " + Float.toString(variableMinMax[0]), variable_name + " " + Float.toString(variableMinMax[1]));
        objTrans.addChild(textTransformGroup);
        textTransform.setScale(f_test);
        textTransformGroup.setTransform(textTransform);
        Color3f bgColor = new Color3f();
        if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
            bgColor = new Color3f(1.0f, 1.0f, 1.0f);
        } else {
            bgColor = new Color3f(1.0f, 1.0f, 1.0f);
        }

        Background ScalebgNode = new Background(bgColor);
        ScalebgNode.setApplicationBounds(bounds);

        ScaleobjRoot.addChild(ScalebgNode);
        if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
            // Shine it with two lights.
            Color3f lColor1 = new Color3f(0.9f, 0.9f, 0.9f);
            Color3f lColor2 = new Color3f(0.7f, 0.7f, 0.7f);
            Color3f lColor3 = new Color3f(0.7f, 0.7f, 0.7f);
            Vector3f lDir1 = new Vector3f(-2.0f, -1.0f, -1.0f);//era -1,-1,-1
            Vector3f lDir2 = new Vector3f(0.0f, 0.0f, -1.0f);
            Vector3f lDir3 = new Vector3f(1.0f, 1.0f, +1.0f);
            lgt1 = new DirectionalLight(lColor1, lDir1);
            lgt2 = new DirectionalLight(lColor2, lDir2);
            lgt3 = new DirectionalLight(lColor3, lDir3);

            lgt1.setInfluencingBounds(bounds);
            lgt2.setInfluencingBounds(bounds);
            lgt3.setInfluencingBounds(bounds);

            lgt3.setEnable(true);

            lgt3.setCapability(DirectionalLight.ALLOW_STATE_READ);
            lgt3.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
            lgt3.setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE);
            lgt3.setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE);

            Color3f alColor = new Color3f(0.9f, 0.9f, 0.9f);
            aLgtScale = new AmbientLight(alColor);
            aLgtScale.setInfluencingBounds(bounds);
            posSwitchLigthScale = new Switch(Switch.CHILD_MASK);  //
            posSwitchLigthScale.setCapability(Switch.ALLOW_SWITCH_READ);
            posSwitchLigthScale.setCapability(Switch.ALLOW_SWITCH_WRITE);
            posMaskLigthScale = new BitSet();
            posSwitchLigthScale.addChild(lgt1);
            posSwitchLigthScale.addChild(lgt2);
            posSwitchLigthScale.addChild(lgt3);
            posSwitchLigthScale.addChild(aLgtScale);

            posMaskLigthScale.set(0);
            posMaskLigthScale.set(1);
            posMaskLigthScale.set(2);
            posMaskLigthScale.clear(3);
            posSwitchLigthScale.setChildMask(posMaskLigthScale);

            ScaleobjRoot.addChild(posSwitchLigthScale);
//            textTransformGroup.addChild(lgt1);
//            textTransformGroup.addChild(lgt2);

        } else {
            Color3f lColor1 = new Color3f(0.7f, 0.7f, 0.7f);
            Vector3f lDir1 = new Vector3f(-1.0f, -1.0f, -1.0f);

            lgt1 = new DirectionalLight(lColor1, lDir1);
            lgt1.setInfluencingBounds(bounds);
            ScaleobjRoot.addChild(aLgt);
            ScaleobjRoot.addChild(lgt1);
        }
        ScaleobjRoot.compile();
        return ScaleobjRoot;
    }

    public void set_light(boolean value, int i) {
        if (value) {
            posMaskLigthModel.set(i);
            posSwitchLigthModel.setChildMask(posMaskLigthModel);
            posMaskLigthScale.set(i);
            posSwitchLigthScale.setChildMask(posMaskLigthScale);
        } else {
            posMaskLigthModel.clear(i);
            posSwitchLigthModel.setChildMask(posMaskLigthModel);
            posMaskLigthScale.clear(i);
            posSwitchLigthScale.setChildMask(posMaskLigthScale);
        }

    }

    public void pickUpBlock(int x, int y) {
        pickBeh.updateScene2(x, y);
    }

    public void highLightBlock(int pos, boolean clear_prev) {
        keyboard.poll();
        boolean shiftDown = keyboard.keyDown(KeyEvent.VK_SHIFT);
        if (shiftDown) {
            //fai niente
            int imhere = 1;
        } else {
            int old_selection = Tough2Viewer.dataobj.get_selectedIndex();
            if (pos != old_selection) {
                if (old_selection >= 0 && clear_prev) {
                    repaint_model(old_selection);
                }
                Appearance appMyBox = new Appearance();
                ColoringAttributes ca = new ColoringAttributes();
                if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
                    // Globally used colors
                    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

                    Color3f objColor = Tough2Viewer.dataobj.selection_color;

                    Material matMybox = new Material(objColor, black, objColor, objColor, 100.f);
                    matMybox.setLightingEnable(true);
                    appMyBox.setMaterial(matMybox);
                    if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                        shapeBox[pos].setAppearance(appMyBox);
                    } else {
                        myVorPlusPlusBox[pos].setAppearance(appMyBox);
                    }
                    //shapeBox[pos].setAppearance(appMyBox);
                } else {
                    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                    appMyBox = shapeBox[pos].getAppearance();
                    ca.setColor(black);
                    appMyBox.setColoringAttributes(ca);
                }
                Tough2Viewer.dataobj.set_selectedindex(pos);
                update_block_Info(pos);
            }
        }
    }

    public void setbackground() {
        bgNode.setColor(Tough2Viewer.dataobj.bgColor);
    }

    public void shiftLayerXYBlock(int position, float Zshift) {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        Transform3D BoxMat = new Transform3D();
        BoxTrans[position].getTransform(BoxMat);
        Vector3d posXYZ = new Vector3d();
        BoxMat.get(posXYZ);
        double z_location = posXYZ.z;
        for (int i = 0; i < nxyz; i++) {
            BoxTrans[i].getTransform(BoxMat);
            Vector3d posXYZ_i = new Vector3d();
            BoxMat.get(posXYZ_i);
            if (z_location <= posXYZ_i.z) {
                posXYZ_i.setZ(posXYZ_i.getZ() + Zshift);
                BoxMat.set(posXYZ_i);
                BoxTrans[i].setTransform(BoxMat);
            }
        }
    }

    public void shiftLayerXZBlock(int position, float Yshift) {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        Transform3D BoxMat = new Transform3D();
        BoxTrans[position].getTransform(BoxMat);
        Vector3d posXYZ = new Vector3d();
        BoxMat.get(posXYZ);
        double y_location = posXYZ.y;
        for (int i = 0; i < nxyz; i++) {
            BoxTrans[i].getTransform(BoxMat);
            Vector3d posXYZ_i = new Vector3d();
            BoxMat.get(posXYZ_i);
            if (y_location < posXYZ_i.y) {
                posXYZ_i.setY(posXYZ_i.getY() + Yshift);
                BoxMat.set(posXYZ_i);
                BoxTrans[i].setTransform(BoxMat);
            }
        }
    }

    public void shiftLayerYZBlock(int position, float Xshift) {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        Transform3D BoxMat = new Transform3D();
        BoxTrans[position].getTransform(BoxMat);
        Vector3d posXYZ = new Vector3d();

        BoxMat.get(posXYZ);
        double x_location = posXYZ.x;

        for (int i = 0; i < nxyz; i++) {
            BoxTrans[i].getTransform(BoxMat);
            Vector3d posXYZ_i = new Vector3d();
            BoxMat.get(posXYZ_i);
            if (x_location < posXYZ_i.x) {
                posXYZ_i.setX(posXYZ_i.getX() + Xshift);
                BoxMat.set(posXYZ_i);
                BoxTrans[i].setTransform(BoxMat);
            }
        }

    }

    public void reset_block_positions() {

        int nxyz;
        nxyz = Tough2Viewer.dataobj.get_nxyz();
        int nxy = Tough2Viewer.dataobj.get_nxy();

        double Zfactor = Tough2Viewer.dataobj.get_Zfactor();
        for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
            double xo = (double) ((Tough2Viewer.dataobj.get_Xo(i_b) - Tough2Viewer.dataobj.get_xmin()) * riduzione + xmin);
            double yo = (double) ((Tough2Viewer.dataobj.get_Yo(i_b) - Tough2Viewer.dataobj.get_ymin()) * riduzione + ymin);
            double zo = (Tough2Viewer.dataobj.get_Zo(i_b) - Tough2Viewer.dataobj.get_zmin()) * riduzione * Zfactor + zmin;
            Transform3D BoxMat = new Transform3D();
            BoxTrans[i_b].getTransform(BoxMat);
            Vector3d posXYZ = new Vector3d();
            BoxMat.get(posXYZ);
            posXYZ.setX(xo);
            posXYZ.setY(yo);
            posXYZ.setZ(zo);
            BoxMat.set(posXYZ);
            BoxTrans[i_b].setTransform(BoxMat);
            //ModelobjRoot.addChild(BoxTrans[i_b]);
            int yes = 0;
        }
    }

    public void repaint_shape_surface(int index, Color3f color1, Color3f color2, Color3f color3, Color3f color4, float value, float Transparency, PolygonAttributes pa) {

        Surfaces_Generic_Shape3D[index].setNewColor(color1, color2, color3, color4, value, Transparency, pa);
        Surfaces_Generic_Shape3D[index].setPolygonAttributes(pa.getBackFaceNormalFlip(), pa.getCullFace(), pa.getPolygonMode());
    }

    public Appearance get_color_surface(int index) {
        return Surfaces_Generic_Shape3D[index].getAppearance();
    }

    double get_crease_angle(int index) {
        return Surfaces_Generic_Shape3D[index].getCreaseAngle();
    }

    public void set_crease_angle(int index, double creaseangle) {
        Surfaces_Generic_Shape3D[index].setCreaseAngle(creaseangle);
    }

    public void repaint_model() {
        if (Tough2Viewer.dataobj.use_multiProcessors) {
            //use multple processor
            int nxyz = Tough2Viewer.dataobj.get_nxyz();
            ForkRepaint3DModel fb = new ForkRepaint3DModel(0, nxyz, this);

            ForkJoinPool pool = new ForkJoinPool();

            long startTime = System.currentTimeMillis();
            pool.invoke(fb);
            long endTime = System.currentTimeMillis();

            System.out.println("Repaint MP took " + (endTime - startTime)
                    + " milliseconds.");
            return;
        }

        long startTime = System.currentTimeMillis();

        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int variablename = Tough2Viewer.dataobj.get_actualVariableToPlot();
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        boolean logaritmic_scale = Tough2Viewer.dataobj.get_logaritmicscale();
        float variableMinMax[];
        variableMinMax = new float[4];
        if (logaritmic_scale == false) {
            variableMinMax[0] = Tough2Viewer.dataobj.get_GlobalScale(variablename, 0);
            variableMinMax[1] = Tough2Viewer.dataobj.get_GlobalScale(variablename, 1);
        } else {
            variableMinMax[0] = (float) Math.log10(Tough2Viewer.dataobj.get_GlobalScale(variablename, 0));
            variableMinMax[1] = (float) Math.log10(Tough2Viewer.dataobj.get_GlobalScale(variablename, 1));
        }
        String variable_name = Tough2Viewer.dataobj.get_variables_name(variablename);
        myscale1.set_text(variable_name + " " + Float.toString(variableMinMax[0]), variable_name + " " + Float.toString(variableMinMax[1]));
        float rangeVariable;

        rangeVariable = variableMinMax[1] - variableMinMax[0];
        if (rangeVariable <= 0) {
            rangeVariable = 1.0f;
        }
        if (appMaterialColor.getSelectedIndex() == 0) {
            colore = Tough2Viewer.dataobj.getColo3fscale();
        } else {
            colore = Tough2Viewer.dataobj.getColo3fRocksTypes();
        }
        double ROI_xmin = Tough2Viewer.dataobj.get_ROI_xmin();
        double ROI_ymin = Tough2Viewer.dataobj.get_ROI_ymin();
        double ROI_zmin = Tough2Viewer.dataobj.get_ROI_zmin();
        double ROI_xmax = Tough2Viewer.dataobj.get_ROI_xmax();
        double ROI_ymax = Tough2Viewer.dataobj.get_ROI_ymax();
        double ROI_zmax = Tough2Viewer.dataobj.get_ROI_zmax();
        double HideROI_xmin = Tough2Viewer.dataobj.get_HideROI_xmin();
        double HideROI_ymin = Tough2Viewer.dataobj.get_HideROI_ymin();
        double HideROI_zmin = Tough2Viewer.dataobj.get_HideROI_zmin();
        double HideROI_xmax = Tough2Viewer.dataobj.get_HideROI_xmax();
        double HideROI_ymax = Tough2Viewer.dataobj.get_HideROI_ymax();
        double HideROI_zmax = Tough2Viewer.dataobj.get_HideROI_zmax();
        float scalecolor = (float) (colore.length - 1.0f);
//        if(Tough2Viewer.dataobj.ID_grid_type==2){
//       int t2vdat=Tough2Viewer.dataobj.VoroPPData.size();
//       nxyz=Math.min(t2vdat,nxyz);
//   }
        for (int i_b = 0; i_b < nxyz; i_b++) {
            //index1: n_block
            //index2: timestep
            //index3: variable
            boolean visible_i_b = true;
            int color_index = 0;
            Color3f finalColor = new Color3f();
            boolean isOutOfRange = false;
            if (appMaterialColor.getSelectedIndex() == 0) {
                if (Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename) < variableMinMax[0]) {
                    finalColor = new Color3f(.1f, .1f, .1f);
                    isOutOfRange = true;
                }
                if (Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename) > variableMinMax[1]) {
                    finalColor = new Color3f(.9f, .9f, .9f);
                    isOutOfRange = true;
                }
                double val1;
                if (logaritmic_scale == false) {
                    val1 = Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename);

                } else {
                    val1 = Math.log10(Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename));
                }
                if (val1 <= variableMinMax[1] && val1 >= variableMinMax[0]) {
                    color_index = (int) ((val1 - variableMinMax[0]) / (rangeVariable) * scalecolor);

                    //color_index=(int)((Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename)-variableMinMax[0])/(rangeVariable)*scalecolor);
                    finalColor = new Color3f(colore[color_index].x, colore[color_index].y, colore[color_index].z);
                }
                visible_i_b = Tough2Viewer.dataobj.is_rocktype_visible(Tough2Viewer.dataobj.get_RockType(i_b));
            } else {
                color_index = Tough2Viewer.dataobj.get_RockType(i_b);
                finalColor = new Color3f(colore[color_index].x, colore[color_index].y, colore[color_index].z);
                visible_i_b = Tough2Viewer.dataobj.is_rocktype_visible(color_index);
            }
            Appearance appMyBox = new Appearance();
            ColoringAttributes ca = new ColoringAttributes();
            if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
                // Globally used colors
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
                //Color3f objColor=colore[color_index];
                Material matMybox = new Material(finalColor, black, finalColor, white, 100.f);
                matMybox.setLightingEnable(true);
                appMyBox.setMaterial(matMybox);
//            //only for test
//          TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.FASTEST,0.30f);
//            ta.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
//            ta.setCapability(TransparencyAttributes.ALLOW_BLEND_FUNCTION_WRITE);
//            ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
//            appMyBox.setTransparencyAttributes(ta);
//            //end only for test
                if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                    shapeBox[i_b].setAppearance(appMyBox);

                } else {
                    myVorPlusPlusBox[i_b].setAppearance(appMyBox);
                }

            } else {
                appMyBox = shapeBox[i_b].getAppearance();
                ca.setColor(finalColor);
                appMyBox.setColoringAttributes(ca);
            }
            double Xo = Tough2Viewer.dataobj.get_Xo(i_b);
            double Yo = Tough2Viewer.dataobj.get_Yo(i_b);
            double Zo = Tough2Viewer.dataobj.get_Zo(i_b);
            boolean isInROI = false;
            boolean isInHideROI = false;
            if (Xo >= ROI_xmin & Xo <= ROI_xmax) {
                if (Yo >= ROI_ymin & Yo <= ROI_ymax) {
                    if (Zo >= ROI_zmin & Zo <= ROI_zmax) {
                        isInROI = true;
                    }
                }
            }
            if (isInROI) {
                if (visible_i_b) {
                    posMaskBox.set(i_b);
                } else {
                    posMaskBox.clear(i_b);
                }
            } else {
                posMaskBox.clear(i_b);
            }
            if (Tough2Viewer.dataobj.get_useHideROI()) {
                if (Xo >= HideROI_xmin & Xo <= HideROI_xmax) {
                    if (Yo >= HideROI_ymin & Yo <= HideROI_ymax) {
                        if (Zo >= HideROI_zmin & Zo <= HideROI_zmax) {
                            isInHideROI = true;
                        }
                    }
                }
                if (isInHideROI) {
                    posMaskBox.clear(i_b);
                }
            }
            if (Tough2Viewer.dataobj.get_hideoutofrange()) {
                if (isOutOfRange) {
                    posMaskBox.clear(i_b);
                }
            }
            if (Tough2Viewer.dataobj.Block_is_selected[i_b]) {
                highLightBlock(i_b, false);
            }
        }
        posSwitchBox.setChildMask(posMaskBox);

        int sel = Tough2Viewer.dataobj.get_selectedIndex();
        if (sel >= 0) {
            update_block_Info(sel);
            Appearance appMyBox = new Appearance();
            ColoringAttributes ca = new ColoringAttributes();
            if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
                // Globally used colors
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

                Material matMybox = new Material(white, black, white, white, 100.f);
                matMybox.setLightingEnable(true);
                appMyBox.setMaterial(matMybox);
                if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                    shapeBox[sel].setAppearance(appMyBox);
                } else {
                    myVorPlusPlusBox[sel].setAppearance(appMyBox);
                }
                //shapeBox[sel].setAppearance(appMyBox);
            } else {
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                appMyBox = shapeBox[sel].getAppearance();
                ca.setColor(black);
                appMyBox.setColoringAttributes(ca);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("RepaintModel recursive took " + (endTime - startTime)
                + " milliseconds.");
    }

    public void repaint_model_Z_factor(float z_factor) {
        int nxyz = Tough2Viewer.dataobj.get_nxy();
        for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
            shapeBox[i_b].ExpandZ(z_factor);
        }
    }

    public void repaint_model_factor(float factor) {
        int nxyz = Tough2Viewer.dataobj.get_nxy();
        for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
            shapeBox[i_b].Expand(factor);
        }

    }

    public void repaint_shapes() {
        int num_shapes = PolyShape3D.length;
        boolean[] PolyShapesVisible = Tough2Viewer.dataobj.get_PolyShapesVisible();
        boolean[] SurfaceShapesVisible = Tough2Viewer.dataobj.get_SurfaceShapesVisible();
        Color3f[] ColorShapes = Tough2Viewer.dataobj.get_ShapesColor();
        for (int i = 0; i < num_shapes; i++) {
            PolyShape3D[i].setNewColor(ColorShapes[i]);
            if (PolyShapesVisible[i]) {
                posMaskPolyShapes.set(i);
            } else {
                posMaskPolyShapes.clear(i);
            }
            if (SurfaceShapesVisible[i]) {
                posMaskSurfaceShapes.set(i);
            } else {
                posMaskSurfaceShapes.clear(i);
            }
        }
        posSwitchPolyShapes.setChildMask(posMaskPolyShapes);
        posSwitchSurfaceShapes.setChildMask(posMaskSurfaceShapes);
    }

    public void repaint_shapes2() {

        for (int j = 0; j < Tough2Viewer.dataobj.Surfaces_Data.size(); j++) {
            SurfaceData a = (SurfaceData) Tough2Viewer.dataobj.Surfaces_Data.get(j);
            PolyShape3D[j].setNewColor(a.get_shape_color3f());
            if (a.isPolyVisible()) {
                posMaskPolyShapes.set(j);
            } else {
                posMaskPolyShapes.clear(j);
            }
            if (a.isSurfaceVisible()) {
                posMaskSurfaceShapes.set(j);
            } else {
                posMaskSurfaceShapes.clear(j);
            }
        }
        posSwitchPolyShapes.setChildMask(posMaskPolyShapes);
        posSwitchSurfaceShapes.setChildMask(posMaskSurfaceShapes);
    }

    public void repaint_model(int pos) {
        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int variablename = Tough2Viewer.dataobj.get_actualVariableToPlot();
        float variableMinMax[];
        variableMinMax = new float[4];
        variableMinMax[0] = Tough2Viewer.dataobj.get_GlobalScale(variablename, 0);
        variableMinMax[1] = Tough2Viewer.dataobj.get_GlobalScale(variablename, 1);
        float rangeVariable;
        rangeVariable = variableMinMax[1] - variableMinMax[0];
        if (rangeVariable <= 0) {
            rangeVariable = 1.0f;
        }
        boolean visible_i_b = Tough2Viewer.dataobj.is_rocktype_visible(Tough2Viewer.dataobj.get_RockType(pos));
        if (appMaterialColor.getSelectedIndex() == 0) {
            colore = Tough2Viewer.dataobj.getColo3fscale();
        } else {
            colore = Tough2Viewer.dataobj.getColo3fRocksTypes();
        }
        double ROI_xmin = Tough2Viewer.dataobj.get_ROI_xmin();
        double ROI_ymin = Tough2Viewer.dataobj.get_ROI_ymin();
        double ROI_zmin = Tough2Viewer.dataobj.get_ROI_zmin();
        double ROI_xmax = Tough2Viewer.dataobj.get_ROI_xmax();
        double ROI_ymax = Tough2Viewer.dataobj.get_ROI_ymax();
        double ROI_zmax = Tough2Viewer.dataobj.get_ROI_zmax();
        double HideROI_xmin = Tough2Viewer.dataobj.get_HideROI_xmin();
        double HideROI_ymin = Tough2Viewer.dataobj.get_HideROI_ymin();
        double HideROI_zmin = Tough2Viewer.dataobj.get_HideROI_zmin();
        double HideROI_xmax = Tough2Viewer.dataobj.get_HideROI_xmax();
        double HideROI_ymax = Tough2Viewer.dataobj.get_HideROI_ymax();
        double HideROI_zmax = Tough2Viewer.dataobj.get_HideROI_zmax();
        float scalecolor = (float) (colore.length - 1.0f);

        //index1: n_block
        //index2: timestep
        //index3: variable
        int color_index = 0;
        Color3f finalColor = new Color3f();
        if (appMaterialColor.getSelectedIndex() == 0) {
            if (Tough2Viewer.dataobj.get_dataArray(pos, timestep, variablename) < variableMinMax[0]) {
                finalColor = new Color3f(.1f, .1f, .1f);
            }
            if (Tough2Viewer.dataobj.get_dataArray(pos, timestep, variablename) > variableMinMax[1]) {
                finalColor = new Color3f(.9f, .9f, .9f);
            }
            if (Tough2Viewer.dataobj.get_dataArray(pos, timestep, variablename) <= variableMinMax[1] && Tough2Viewer.dataobj.get_dataArray(pos, timestep, variablename) >= variableMinMax[0]) {
                color_index = (int) ((Tough2Viewer.dataobj.get_dataArray(pos, timestep, variablename) - variableMinMax[0]) / (rangeVariable) * scalecolor);
                finalColor = new Color3f(colore[color_index].x, colore[color_index].y, colore[color_index].z);
            }
        } else {
            color_index = Tough2Viewer.dataobj.get_RockType(pos);
            finalColor = new Color3f(colore[color_index].x, colore[color_index].y, colore[color_index].z);
        }
        Appearance appMyBox = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
            // Globally used colors
            Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
            Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
            Material matMybox = new Material(finalColor, black, finalColor, white, 100.f);
            matMybox.setLightingEnable(true);
            appMyBox.setMaterial(matMybox);
            if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                shapeBox[pos].setAppearance(appMyBox);
            } else {
                myVorPlusPlusBox[pos].setAppearance(appMyBox);
            }
            //shapeBox[pos].setAppearance(appMyBox);
        } else {
            appMyBox = shapeBox[pos].getAppearance();
            ca.setColor(finalColor);
            appMyBox.setColoringAttributes(ca);
        }
        double Xo = Tough2Viewer.dataobj.get_Xo(pos);
        double Yo = Tough2Viewer.dataobj.get_Yo(pos);
        double Zo = Tough2Viewer.dataobj.get_Zo(pos);
        boolean isInROI = false;
        boolean isInHideROI = false;
        if (Xo >= ROI_xmin & Xo <= ROI_xmax) {
            if (Yo >= ROI_ymin & Yo <= ROI_ymax) {
                if (Zo >= ROI_zmin & Zo <= ROI_zmax) {
                    isInROI = true;
                }
            }
        }
        if (isInROI) {
            if (visible_i_b) {
                posMaskBox.set(pos);
            } else {
                posMaskBox.clear(pos);
            }
        } else {
            posMaskBox.clear(pos);
        }
        if (Tough2Viewer.dataobj.get_useHideROI()) {
            if (Xo >= HideROI_xmin & Xo <= HideROI_xmax) {
                if (Yo >= HideROI_ymin & Yo <= HideROI_ymax) {
                    if (Zo >= HideROI_zmin & Zo <= HideROI_zmax) {
                        isInHideROI = true;
                    }
                }
            }
            if (isInHideROI) {
                posMaskBox.clear(pos);
            }
        }
        posSwitchBox.setChildMask(posMaskBox);
        if (Tough2Viewer.dataobj.Block_is_selected[pos]) {
            highLightBlock(pos, false);
        }
    }

    private OffScreenCanvas3D createOffScreenCanvas(Canvas3D onScreenCanvas3D) {
        // Create the off-screen Canvas3D object
        // request an offscreen Canvas3D with a single buffer configuration
        GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
        template.setDoubleBuffer(GraphicsConfigTemplate3D.UNNECESSARY);
        GraphicsConfiguration gc
                = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getBestConfiguration(template);

        offScreenCanvas3D = new OffScreenCanvas3D(gc, true);
        // Set the off-screen size based on a scale factor times the
        // on-screen size
        Screen3D sOn = onScreenCanvas3D.getScreen3D();
        Screen3D sOff = offScreenCanvas3D.getScreen3D();
        Dimension dim = sOn.getSize();
        dim.width *= OFF_SCREEN_SCALE;
        dim.height *= OFF_SCREEN_SCALE;
        sOff.setSize(dim);
        sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth()
                * OFF_SCREEN_SCALE);
        sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight()
                * OFF_SCREEN_SCALE);

        // attach the offscreen canvas to the view
        ModelUniverse.getViewer().getView().addCanvas3D(offScreenCanvas3D);

        return offScreenCanvas3D;

    }

    JPanel createSelectionPanel() {

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Command panel"));
        String values[] = Tough2Viewer.dataobj.getVariableName();
        chooseVariable = new JComboBox(values);
        chooseVariable.setSelectedIndex(0);
        chooseVariable.setLightWeightPopupEnabled(false);
        chooseVariable.addActionListener(this);
        panel.add(new JLabel("Variable"));
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
        String Tooltip = Float.toString(Tough2Viewer.dataobj.get_Times(0)) + " S";
        slider.setToolTipText(Tooltip);
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
//        slider.setUnitIncrement(1);

        panel.add(slider);

        String ShiftLabels[] = {"ShiftXY", "ShiftXZ", "ShiftYZ", "ResetPositions", "Get 2D Plot", "TopView", "LeftView", "FrontView", "RightView", "BackView", "BottomView", "ParallelView", "PerspectiveView", "setVOI", "SnapShot", "FindBlocks", "ExpandZ", "QuickInfo", "RealTimeInfo", "SurfaceControl", "Directional profile", "AutoSnapShot", "SelectNeighbord", "MultipleSelection", "ModifySelection", "AdvancedBlocksSelection"};
//                                  00          01	02              03              04         05        06           07          08           09       10               11           12               13      14         15           16       17           18               19                   20                 21              22               23                 24                 25
        ShiftMode = new JComboBox(ShiftLabels);
        ShiftMode.addActionListener(this);
        ShiftMode.setSelectedIndex(0);
        ShiftMode.setLightWeightPopupEnabled(false);
        panel.add(new JLabel("Action"));
        panel.add(ShiftMode);

        panel.addKeyListener(keyboard);
        return panel;
    }

    JPanel createMaterialPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Appearance Attributes"));

        String colorVals[] = {"Variable", "RockTypes", "----", "----"};

        altAppMaterialColor = new JComboBox(colorVals);
        altAppMaterialColor.addActionListener(this);
        altAppMaterialColor.setLightWeightPopupEnabled(false);
        ActionListener l;

        altAppMaterialColor.setSelectedIndex(0);
        //panel.add(new JLabel("Alternate Appearance MaterialColor"));
        //panel.add(altAppMaterialColor);

////////////////////////////////////////////////////////////////////////
        appMaterialColor = new JComboBox(colorVals);
        appMaterialColor.addActionListener(this);
        appMaterialColor.setSelectedIndex(0);
        panel.add(new JLabel("Block Appearance "));
        panel.add(appMaterialColor);
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        panel.add(jLabel1);
        panel.add(jLabel2);
        panel.add(jLabel3);
        panel.add(jLabel4);
        panel.add(jLabel5);
        panel.add(jLabel6);
        panel.add(jLabel7);
        panel.add(jLabel8);

        b1 = new JButton("Close");
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.RIGHT); //aka LEFT, for left-to-right locales
        b1.setMnemonic(KeyEvent.VK_D);
        b1.setActionCommand("disable");
        b1.addActionListener(this);
        b1.setToolTipText("Close the current window");
        panel.add(b1);
        panel.addKeyListener(keyboard);
        return panel;

    }

    public void get2D(int position) {

        Model2D = new XYScatterPlotVoronoi(position);

        Model2D.setVisible(true);
    }

    public void pick_VOI(int pos) {
        float ROI_xmin, ROI_ymin, ROI_xmax, ROI_ymax, ROI_zmin, ROI_zmax;
        float xmin_tmp, ymin_tmp, zmin_tmp, xmax_tmp, ymax_tmp, zmax_tmp;
        if (SET_VOIWindow.bottom_left) {
            ROI_xmin = Tough2Viewer.dataobj.get_Xo(pos);
            ROI_ymin = Tough2Viewer.dataobj.get_Yo(pos);
            ROI_zmin = Tough2Viewer.dataobj.get_zmin();
            xmin_tmp = ROI_xmin;
            ymin_tmp = ROI_ymin;
            if (SET_VOIWindow.use_xyz) {
                ROI_zmin = Tough2Viewer.dataobj.get_Zo(pos);
            }
            zmin_tmp = ROI_zmin;
            if (Tough2Viewer.dataobj.ID_grid_type == 2) {

                ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(pos);
                double[] centerD = (double[]) singleBox.get(1);//POS=1
                Point3d center = new Point3d(centerD[0], centerD[1], centerD[2]);
                int num_vertex_box = (Integer) singleBox.get(2);
                Point3d[] vertex = new Point3d[num_vertex_box];
                for (int i = 0; i < num_vertex_box; i++) {
                    double[] v = (double[]) singleBox.get(3 + i);
                    vertex[i] = new Point3d(v[0] + center.x, v[1] + center.y, v[2] + center.z);
                    if (vertex[i].x < xmin_tmp) {
                        xmin_tmp = (float) vertex[i].x;
                    }
                    if (vertex[i].y < ymin_tmp) {
                        ymin_tmp = (float) vertex[i].y;
                    }
                    if (vertex[i].z < zmin_tmp) {
                        zmin_tmp = (float) vertex[i].z;
                    }
                }
                ROI_xmin = xmin_tmp;
                ROI_ymin = ymin_tmp;
                if (SET_VOIWindow.use_xyz) {
                    ROI_zmin = zmin_tmp;
                }

            }

            Tough2Viewer.dataobj.set_ROI_xmin(ROI_xmin);
            Tough2Viewer.dataobj.set_ROI_ymin(ROI_ymin);
            Tough2Viewer.dataobj.set_ROI_zmin(ROI_zmin);
            VoronoiBlockModel3D.this.repaint_model();
            SET_VOIWindow.setVisible(true);
        }
        if (SET_VOIWindow.top_right) {
            ROI_xmax = Tough2Viewer.dataobj.get_Xo(pos);
            ROI_ymax = Tough2Viewer.dataobj.get_Yo(pos);
            ROI_zmax = Tough2Viewer.dataobj.get_zmax();
            xmax_tmp = ROI_xmax;
            ymax_tmp = ROI_ymax;
            if (SET_VOIWindow.use_xyz) {
                ROI_zmax = Tough2Viewer.dataobj.get_Zo(pos);
            }
            zmax_tmp = ROI_zmax;
            if (SET_VOIWindow.use_xyz) {
                ROI_zmax = Tough2Viewer.dataobj.get_Zo(pos);
            }
            if (Tough2Viewer.dataobj.ID_grid_type == 2) {

                ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(pos);
                double[] centerD = (double[]) singleBox.get(1);//POS=1
                Point3d center = new Point3d(centerD[0], centerD[1], centerD[2]);
                int num_vertex_box = (Integer) singleBox.get(2);
                Point3d[] vertex = new Point3d[num_vertex_box];
                for (int i = 0; i < num_vertex_box; i++) {
                    double[] v = (double[]) singleBox.get(3 + i);
                    vertex[i] = new Point3d(v[0] + center.x, v[1] + center.y, v[2] + center.z);
                    if (vertex[i].x > xmax_tmp) {
                        xmax_tmp = (float) vertex[i].x;
                    }
                    if (vertex[i].y > ymax_tmp) {
                        ymax_tmp = (float) vertex[i].y;
                    }
                    if (vertex[i].z > zmax_tmp) {
                        zmax_tmp = (float) vertex[i].z;
                    }
                }
                ROI_xmax = xmax_tmp;
                ROI_ymax = ymax_tmp;
                if (SET_VOIWindow.use_xyz) {
                    ROI_zmax = zmax_tmp;
                }

            }
            Tough2Viewer.dataobj.set_ROI_xmax(ROI_xmax);
            Tough2Viewer.dataobj.set_ROI_ymax(ROI_ymax);
            Tough2Viewer.dataobj.set_ROI_zmax(ROI_zmax);
            VoronoiBlockModel3D.this.repaint_model();
            SET_VOIWindow.setVisible(true);

        }

    }

    public void update_block_Info(int position) {

        int i_b = position;

        jLabel1.setText("n=" + Integer.toString(i_b));
        jLabel2.setText("");
        jLabel3.setText("Bl.Name=" + Tough2Viewer.dataobj.get_BlockName(position));
        jLabel4.setText("X=" + Float.toString((Tough2Viewer.dataobj.get_Xo(i_b))));
        jLabel5.setText("Y=" + Float.toString((Tough2Viewer.dataobj.get_Yo(i_b))));
        jLabel6.setText("Z=" + Float.toString((Tough2Viewer.dataobj.get_Zo(i_b))));
        int rocktype_n = Tough2Viewer.dataobj.get_RockType(position);
        String rocktype = Tough2Viewer.dataobj.get_RockType_name(rocktype_n);
        jLabel7.setText("RockType=" + rocktype);
        highLightBlock(position, true);
        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int variable = Tough2Viewer.dataobj.get_actualVariableToPlot();
        float value = Tough2Viewer.dataobj.get_dataArray(position, timestep, variable);
        String variablename = Tough2Viewer.dataobj.get_variables_name(variable);
        String variableUM = Tough2Viewer.dataobj.get_variables_unit(variable);
        jLabel8.setText(variablename + "=" + Float.toString(value) + " " + variableUM);

    }

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

    public void set_parallel_view() {
        View view = ModelUniverse.getViewer().getView();
        view.setProjectionPolicy(View.PARALLEL_PROJECTION);

    }

    public void set_perspective_view() {
        View view = ModelUniverse.getViewer().getView();
        view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);

    }

    public void change_view(int index) {

        awtBehaviorZ.reset();
        TransformGroup viewTransformGroup = ModelUniverse.getViewingPlatform().getViewPlatformTransform();

        Transform3D t3dInitial = new Transform3D();
        Transform3D t3dTranslation = new Transform3D();
        Transform3D t3dRotation = new Transform3D();

        viewTransformGroup.getTransform(t3dInitial);

        Vector3f InitialPointView = new Vector3f();

        Vector3f FinalPointView[] = new Vector3f[7];
        FinalPointView[0] = new Vector3f(0.0f, 0.0f, zmax + 2.47f);//topview
        FinalPointView[1] = new Vector3f(-2.47f + xmin, 0.0f, 0.0f);//left
        FinalPointView[2] = new Vector3f(0.0f, -2.47f + ymin, 0.0f);//front
        FinalPointView[3] = new Vector3f(2.47f + xmax, 0.0f, 0.0f);//rigt
        FinalPointView[4] = new Vector3f(0.0f, 2.47f + ymax, 0.0f);//back
        FinalPointView[5] = new Vector3f(0.0f, 0.0f, -2.47f + zmin);//Bottom
        FinalPointView[6] = new Vector3f(2.47f + xmax, -2.47f + ymin, 2.47f + zmax);//Bottom
        t3dInitial.get(InitialPointView);//iniziale
        Matrix3f m3d = new Matrix3f();
        t3dInitial.get(m3d);

        Matrix3f m3dtest[] = new Matrix3f[7];
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
//          m3dtest[6]=new Matrix3f(.71f,0f,-.71f,
//                                  .41f,.58f,.41f,
//                                  .58f,-.58f,.58f);//iso/don't work
        m3dtest[6] = new Matrix3f(1f, 0f, -.433f,
                0f, 0f, 1f,
                .25f, 0f, .0f);//Bottom
        t3dRotation.setRotation(m3dtest[index]);
        t3dTranslation.setTranslation(FinalPointView[index]);
        t3dTranslation.mul(t3dRotation);

        viewTransformGroup.setTransform(t3dTranslation);

    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int timestep = (int) source.getValue();
            Tough2Viewer.dataobj.set_actualTimeToPlot(timestep);
            int variable = chooseVariable.getSelectedIndex();
            Tough2Viewer.dataobj.set_actualVariableToPlot(chooseVariable.getSelectedIndex());
            String Tooltip = Float.toString(Tough2Viewer.dataobj.get_Times(timestep)) + " S";
            slider.setToolTipText(Tooltip);
            VoronoiBlockModel3D.this.repaint_model();

        }
    }

    public void actionPerformed(ActionEvent e) {
        int yeswearehere = -1;
        Object target = e.getSource();
        if (target == b1) {
            this.setVisible(false);
        }

        if (target == altAppMaterialColor) {
//	    altMat.setDiffuseColor(colors[altAppMaterialColor.getSelectedIndex()]);
        } else if (target == chooseVariable) {

            Tough2Viewer.dataobj.set_actualTimeToPlot(slider.getValue());
            int variable = chooseVariable.getSelectedIndex();

            Tough2Viewer.dataobj.set_actualVariableToPlot(chooseVariable.getSelectedIndex());

            VoronoiBlockModel3D.this.repaint_model();

        } else if (target == ShiftMode) {
            int i;
            if (ShiftMode.getSelectedIndex() == 0) {
                yeswearehere = 1;
            } else if (ShiftMode.getSelectedIndex() == 1) {
                yeswearehere = 1;
            } else if (ShiftMode.getSelectedIndex() == 2) {
                yeswearehere = 1;
            } else if (ShiftMode.getSelectedIndex() == 3) {
                reset_block_positions();
            } else if (ShiftMode.getSelectedIndex() == 4) {
            } else if (ShiftMode.getSelectedIndex() == 5) {
                change_view(0);
            } else if (ShiftMode.getSelectedIndex() == 6) {
                change_view(1);
            } else if (ShiftMode.getSelectedIndex() == 7) {
                change_view(2);
            } else if (ShiftMode.getSelectedIndex() == 8) {
                change_view(3);
            } else if (ShiftMode.getSelectedIndex() == 9) {
                change_view(4);
            } else if (ShiftMode.getSelectedIndex() == 10) {
                change_view(5);
            } else if (ShiftMode.getSelectedIndex() == 11) {
                set_parallel_view();
            } else if (ShiftMode.getSelectedIndex() == 12) {
                set_perspective_view();
            } else if (ShiftMode.getSelectedIndex() == 13) {

                if (SET_VOIWindow == null) {
                    SET_VOIWindow = new SetVoi();
                }

                SET_VOIWindow.setVisible(true);

            } else if (ShiftMode.getSelectedIndex() == 14) {
                //getsnapshot
                createOffScreenCanvas(ModelCanvas3D);
                Point loc = ModelCanvas3D.getLocationOnScreen();
                offScreenCanvas3D.setOffScreenLocation(loc);
                Dimension dim = ModelCanvas3D.getSize();
                dim.width *= OFF_SCREEN_SCALE;
                dim.height *= OFF_SCREEN_SCALE;
                BufferedImage bImage = offScreenCanvas3D.doRender(dim.width, dim.height);

                new ImageDisplayer(bImage, false, 0);
            } else if (ShiftMode.getSelectedIndex() == 15) {

                FindBlockVoronoi3D FindBlockVoronoi3DWindow = new FindBlockVoronoi3D();
                FindBlockVoronoi3DWindow.setVisible(true);
            } else if (ShiftMode.getSelectedIndex() == 20) {

                XYScatterDirectional XYScatterDirectionalWindow = new XYScatterDirectional(-1, -1);
                XYScatterDirectionalWindow.setVisible(true);
            } else if (ShiftMode.getSelectedIndex() == 21) {

                //autosnapshot
                AutoSnap AutoSnapFRM = new AutoSnap();
                AutoSnapFRM.setVisible(true);
            } else if (ShiftMode.getSelectedIndex() == 22) {
                Tough2Viewer.dataobj.set_selectedindex(-1);
                Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();

            } else if (ShiftMode.getSelectedIndex() == 23) {
                if (selected_blocks.size() > 0) {
                    String title = "Clear previous selection confirmation";
                    String message = "Clear previous block selection?";

                    int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        Tough2Viewer.dataobj.set_selectedindex(-1);
                        for (int iii = 0; iii < selected_blocks.size(); iii++) {
                            int pos = (Integer) selected_blocks.get(iii);
                            Tough2Viewer.dataobj.Block_is_selected[pos] = false;
                        }

                        selected_blocks.clear();

                        Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();

                    } else {

                    }
                }

            } else if (ShiftMode.getSelectedIndex() == 24) {
                PropertyModifier_Jform prop = new PropertyModifier_Jform();
                prop.setVisible(true);
            } else if (ShiftMode.getSelectedIndex() == 25) {
                if (AdvancedBlockSelection3D_window == null) {
                    AdvancedBlockSelection3D_window = new AdvancedBlockSelection3D();
                }

                AdvancedBlockSelection3D_window.setVisible(true);
            } else if (ShiftMode.getSelectedIndex() == 16) {
                int nz = myIndexArray.length;
                for (int ii = 1; ii < nz; ii++) {
                    shiftLayerXYBlock((int) myIndexArray[ii][1], 0.4f);
                }
            } else if (ShiftMode.getSelectedIndex() == 17) {
                if (selected_blocks.size() > 0) {
                    String title = "Clear previous selection confirmation";
                    String message = "Clear previous block selection?";

                    int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        Tough2Viewer.dataobj.set_selectedindex(-1);
                        selected_blocks.clear();
                        Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();
                    } else {

                    }
                }
            } else if (ShiftMode.getSelectedIndex() == 18) {

            } else if (ShiftMode.getSelectedIndex() == 19) {
                if (mySrfCtrl == null) {
                    mySrfCtrl = new SurfaceControl();
                }

                mySrfCtrl.setVisible(true);

//                SurfaceControl mySrfCtrl=new SurfaceControl();
//                mySrfCtrl.setVisible(true);
            } else if (ShiftMode.getSelectedIndex() == 5) {

            }
        } else if (target == appMaterialColor) {
            if (initcompleted) {
                Tough2Viewer.dataobj.set_actualTimeToPlot(slider.getValue());
                Tough2Viewer.dataobj.set_actualVariableToPlot(chooseVariable.getSelectedIndex());
                VoronoiBlockModel3D.this.repaint_model();
            }
        }
    }

    public void rotateZ() {
        awtBehaviorZ.rotate();
    }

    public void autosnap(int n_snap4timestep, float total_anglez) {
        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
        int total_snap = timesteps * n_snap4timestep;
        double stepanglez = total_anglez / (float) total_snap;
        awtBehaviorZ.setStepAngle(stepanglez);
        for (int i = 0; i < timesteps; i++) {
            Tough2Viewer.dataobj.set_actualTimeToPlot(i);
            for (int i_s = 0; i_s < n_snap4timestep; i_s++) {
                VoronoiBlockModel3D.this.repaint_model();
                createOffScreenCanvas(ModelCanvas3D);
                Point loc = ModelCanvas3D.getLocationOnScreen();
                offScreenCanvas3D.setOffScreenLocation(loc);
                Dimension dim = ModelCanvas3D.getSize();
                dim.width *= OFF_SCREEN_SCALE;
                dim.height *= OFF_SCREEN_SCALE;
                BufferedImage bImage = offScreenCanvas3D.doRender(dim.width, dim.height);

                int progressbarwidth = 200;
                int progressbarheight = 20;
                boolean printprogressbar = true;
                if (printprogressbar) {
                    Graphics g = bImage.getGraphics();

                    Color3f neg = new Color3f();
                    bgNode.getColor(neg);
                    float x = 1.0f - (float) neg.x;
                    float y = 1.0f - neg.y;
                    float z = 1.0f - neg.z;
                    g.setColor(new Color(x, y, z));
                    g.drawRect((int) 60, (int) 60, (int) progressbarwidth, (int) 20);
                    Font myfont = new Font("SansSerif", Font.PLAIN, 20);
                    g.setFont(myfont);
                    timesteps = Tough2Viewer.dataobj.get_TimeSteps();
                    double initial = Tough2Viewer.dataobj.get_Times(0);
                    double current = Tough2Viewer.dataobj.get_Times(i);
                    double finaltime = Tough2Viewer.dataobj.get_Times(timesteps - 1);
                    g.drawString(Double.toString(initial), 20, (int) 60);
                    g.drawString(Double.toString(finaltime), progressbarwidth + 20, (int) 60);
                    g.drawString(Double.toString(current), (progressbarwidth + 20) / 2, (int) 100);
                    double progress_1 = (current - initial) / (finaltime - initial) * progressbarwidth;
                    g.fillRect(60, 60, (int) progress_1, 20);

                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
                Date data = new Date();
                String stringa = formatter.format(data);
                String fileName = "SnapShot" + stringa + ".png";
                String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
                String SuggestedFileName = FilePathString + "\\" + fileName;
                try {
                    File file = new File(SuggestedFileName);
                    ImageIO.write(bImage, "png", file);
                } catch (IOException e) {
                    String error = "File I/O error";
                    JOptionPane.showMessageDialog(null, error);
                    JOptionPane.showMessageDialog(null, e.toString());
                }
                awtBehaviorZ.rotate();
            }
        }
        awtBehaviorZ.reset();
        Tough2Viewer.dataobj.set_actualTimeToPlot(timestep);
        VoronoiBlockModel3D.this.repaint_model();
    }

    public void autosnap2(boolean all_timestep, boolean all_variable, String suffix) {
        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int var = Tough2Viewer.dataobj.get_actualVariableToPlot();
        int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
        int start_t = 0;
        int end_t = timesteps;
        if (all_timestep) {
            start_t = 0;
            end_t = timesteps;
        } else {
            start_t = timestep;
            end_t = timestep + 1;
        }

        int start_v;
        int end_v;
        if (all_variable) {
            start_v = 0;
            end_v = Tough2Viewer.dataobj.get_number_of_variables();
        } else {
            start_v = var;
            end_v = var + 1;
        }
        for (int i = start_t; i < end_t; i++) {
            Tough2Viewer.dataobj.set_actualTimeToPlot(i);
            for (int i_v = start_v; i_v < end_v; i_v++) {
                Tough2Viewer.dataobj.set_actualVariableToPlot(i_v);
                VoronoiBlockModel3D.this.repaint_model();
                createOffScreenCanvas(ModelCanvas3D);
                Point loc = ModelCanvas3D.getLocationOnScreen();
                offScreenCanvas3D.setOffScreenLocation(loc);
                Dimension dim = ModelCanvas3D.getSize();
                dim.width *= OFF_SCREEN_SCALE;
                dim.height *= OFF_SCREEN_SCALE;
                BufferedImage bImage = offScreenCanvas3D.doRender(dim.width, dim.height);

                int progressbarwidth = 200;
                int progressbarheight = 20;
                boolean printprogressbar = false;
                if (printprogressbar) {
                    Graphics g = bImage.getGraphics();

                    Color3f neg = new Color3f();
                    bgNode.getColor(neg);
                    float x = 1.0f - (float) neg.x;
                    float y = 1.0f - neg.y;
                    float z = 1.0f - neg.z;
                    g.setColor(new Color(x, y, z));
                    g.drawRect((int) 60, (int) 60, (int) progressbarwidth, (int) 20);
                    Font myfont = new Font("SansSerif", Font.PLAIN, 20);
                    g.setFont(myfont);
                    timesteps = Tough2Viewer.dataobj.get_TimeSteps();
                    double initial = Tough2Viewer.dataobj.get_Times(0);
                    double current = Tough2Viewer.dataobj.get_Times(i);
                    double finaltime = Tough2Viewer.dataobj.get_Times(timesteps - 1);
                    g.drawString(Double.toString(initial), 20, (int) 60);
                    g.drawString(Double.toString(finaltime), progressbarwidth + 20, (int) 60);
                    g.drawString(Double.toString(current), (progressbarwidth + 20) / 2, (int) 100);
                    double progress_1 = (current - initial) / (finaltime - initial) * progressbarwidth;
                    g.fillRect(60, 60, (int) progress_1, 20);

                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
                Date data = new Date();
                String stringadata = formatter.format(data);
                double current = Tough2Viewer.dataobj.get_Times(i);
                //String var_name=Tough2Viewer.dataobj.get_variables_name(i_v)+"_t"+Integer.toString(i);
                String var_name = Tough2Viewer.dataobj.get_variables_name(i_v) + "_t" + Double.toString(current);
                //var_name=var_name.replace('.', '_');
                String fileName = "SnapShot" + stringadata + "_" + suffix + "_" + var_name + ".png";
                String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
                String SuggestedFileName = FilePathString + "\\" + fileName;
                try {
                    File file = new File(SuggestedFileName);
                    ImageIO.write(bImage, "png", file);
                } catch (IOException e) {
                    String error = "File I/O error";
                    JOptionPane.showMessageDialog(null, error);
                    JOptionPane.showMessageDialog(null, e.toString());
                }

            }
        }
        Tough2Viewer.dataobj.set_actualTimeToPlot(timestep);
        Tough2Viewer.dataobj.set_actualVariableToPlot(var);
        VoronoiBlockModel3D.this.repaint_model();
    }

    public void export_visible_data() {
        String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
        final JFileChooser fc = new JFileChooser(FilePathString);
        String filename = "PickVoi.dat";
        String SuggestedFileName = FilePathString + "\\" + filename;
        File suggestedFile = new File(SuggestedFileName);
        fc.setSelectedFile(suggestedFile);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String strFilePath = fc.getSelectedFile().toString();
            try {
                FileOutputStream fos = new FileOutputStream(strFilePath, false);
                PrintStream ps;
                // Print a line of text
                ps = new PrintStream(fos);

                String lineaOUT;
                lineaOUT = "File generated by Tough2Viewer - by Stebond";
                ps.println(lineaOUT);
                lineaOUT = "File=" + Tough2Viewer.dataobj.get_DataFileName() + " Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(Tough2Viewer.dataobj.get_actualTimeToPlot())) + "S";

                ps.println(lineaOUT);
                String VariableName[] = Tough2Viewer.dataobj.getVariableName();
                String VariableNameUM[] = Tough2Viewer.dataobj.getVariablesUM();
                int n_var = Tough2Viewer.dataobj.get_number_of_variables();
                lineaOUT = "Block name,Rocktype,x,y,z,";
                for (int i_y = 0; i_y < n_var; i_y++) {
                    lineaOUT = lineaOUT + VariableName[i_y] + " (" + VariableNameUM[i_y] + "),";
                }
                ps.println(lineaOUT);
                //end header
                //start write out the rest
                lineaOUT = "";

                for (int i_b = 0; i_b < Tough2Viewer.dataobj.get_nxyz(); i_b++) {
                    if (posMaskBox.get(i_b)) {
                        lineaOUT = Tough2Viewer.dataobj.get_BlockName(i_b) + "," + Tough2Viewer.dataobj.get_RockType(i_b) + "," + Tough2Viewer.dataobj.get_Xo(i_b) + ","
                                + Tough2Viewer.dataobj.get_Yo(i_b) + "," + Tough2Viewer.dataobj.get_Zo(i_b) + ",";
                        for (int i_y = 0; i_y < n_var; i_y++) {
                            lineaOUT = lineaOUT + Float.toString(Tough2Viewer.dataobj.get_dataArray(i_b, Tough2Viewer.dataobj.get_actualTimeToPlot(), i_y)) + ",";
                        }
                        ps.println(lineaOUT);
                    }
                }

                fos.close();
            } catch (IOException e) {
                String output = "Unable to write file";
                Tough2Viewer.toLogFile(output);
                //System.exit(-1);
            }
        }
    }

    public void add_or_remove_multiple_selection_advanced(int actualposition) {
        float xo = Tough2Viewer.dataobj.get_Xo(actualposition);
        float yo = Tough2Viewer.dataobj.get_Yo(actualposition);
        float zo = Tough2Viewer.dataobj.get_Zo(actualposition);

        if (Tough2Viewer.dataobj.ID_grid_type == 2) {
            float xmin_tmp = xo;
            float xmax_tmp = xo;
            float ymin_tmp = yo;
            float ymax_tmp = yo;
            float zmin_tmp = zo;
            float zmax_tmp = zo;

            int nxyz = Tough2Viewer.dataobj.get_nxyz();

            ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(actualposition);
            double[] centerD = (double[]) singleBox.get(1);//POS=1
            Point3d center = new Point3d(centerD[0], centerD[1], centerD[2]);
            int num_vertex_box = (Integer) singleBox.get(2);
            Point3d[] vertex = new Point3d[num_vertex_box];
            for (int i = 0; i < num_vertex_box; i++) {
                double[] v = (double[]) singleBox.get(3 + i);
                vertex[i] = new Point3d(v[0] + center.x, v[1] + center.y, v[2] + center.z);
                if (vertex[i].x < xmin_tmp) {
                    xmin_tmp = (float) vertex[i].x;
                }
                if (vertex[i].y < ymin_tmp) {
                    ymin_tmp = (float) vertex[i].y;
                }
                if (vertex[i].z < zmin_tmp) {
                    zmin_tmp = (float) vertex[i].z;
                }
                if (vertex[i].x > xmax_tmp) {
                    xmax_tmp = (float) vertex[i].x;
                }
                if (vertex[i].y > ymax_tmp) {
                    ymax_tmp = (float) vertex[i].y;
                }
                if (vertex[i].z > zmax_tmp) {
                    zmax_tmp = (float) vertex[i].z;
                }
            }
            for (int i_b = 0; i_b < nxyz; i_b++) {
                float xi = Tough2Viewer.dataobj.get_Xo(i_b);
                float yi = Tough2Viewer.dataobj.get_Yo(i_b);
                float zi = Tough2Viewer.dataobj.get_Zo(i_b);
                if (AdvancedBlockSelection3D_window.jCheckBoxSameX.isSelected()) {
                    if (xi >= xmin_tmp && xi <= xmax_tmp) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }

                    }
                }

                if (AdvancedBlockSelection3D_window.jCheckBoxSameY.isSelected()) {
                    if (yi >= ymin_tmp && yi <= ymax_tmp) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }
                if (AdvancedBlockSelection3D_window.jCheckBoxSameZ.isSelected()) {
                    if (zi >= zmin_tmp && zi <= zmax_tmp) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }

                if (AdvancedBlockSelection3D_window.jCheckBoxPickSameXY.isSelected()) {
                    if (xi >= xmin_tmp && xi <= xmax_tmp && yi >= ymin_tmp && yi <= ymax_tmp) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }
                if (AdvancedBlockSelection3D_window.jCheckBoxPickSameXZ.isSelected()) {
                    if (zi >= zmin_tmp && zi <= zmax_tmp && xi >= xmin_tmp && xi <= xmax_tmp) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }
                if (AdvancedBlockSelection3D_window.jCheckBoxSameYZ.isSelected()) {
                    if (yi >= ymin_tmp && yi <= ymax_tmp && zi >= zmin_tmp && zi <= zmax_tmp) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }

                AdvancedBlockSelection3D_window.update_selected_blocks();
//                if(xi>=xmin_tmp&&xi<=xmax_tmp&&yi>=ymin_tmp&&yi<=ymax_tmp)myindex[2].add(i_b);//THE "VERTICAL PROFILE"
//                if(zi>=zmin_tmp&&zi<=zmax_tmp&&xi>=xmin_tmp&&xi<=xmax_tmp)myindex[1].add(i_b);//ALONG Y
//                if(yi>=ymin_tmp&&yi<=ymax_tmp&&zi>=zmin_tmp&&zi<=zmax_tmp)myindex[0].add(i_b);//Along X
            }
        } else {
            int nxyz = Tough2Viewer.dataobj.get_nxyz();
            //index=new int[nxyz];
            for (int i_b = 0; i_b < nxyz; i_b++) {
                float xi = Tough2Viewer.dataobj.get_Xo(i_b);
                float yi = Tough2Viewer.dataobj.get_Yo(i_b);
                float zi = Tough2Viewer.dataobj.get_Zo(i_b);
                if (AdvancedBlockSelection3D_window.jCheckBoxSameX.isSelected()) {
                    if (xi == xo) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }

                    }
                }

                if (AdvancedBlockSelection3D_window.jCheckBoxSameY.isSelected()) {
                    if (yi == yo) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }
                if (AdvancedBlockSelection3D_window.jCheckBoxSameZ.isSelected()) {
                    if (zi == zo) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }

                if (AdvancedBlockSelection3D_window.jCheckBoxPickSameXY.isSelected()) {
                    if (yi == yo && xi == xo) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }
                if (AdvancedBlockSelection3D_window.jCheckBoxPickSameXZ.isSelected()) {
                    if (zi == zo && xi == xo) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }
                if (AdvancedBlockSelection3D_window.jCheckBoxSameYZ.isSelected()) {
                    if (yi == yo && zi == zo) {
                        if (AdvancedBlockSelection3D_window.jRadioButtonPickAdd.isSelected()) {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = true;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
                        } else {
                            Tough2Viewer.dataobj.Block_is_selected[i_b] = false;
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model(i_b);
                        }
                    }
                }

                AdvancedBlockSelection3D_window.update_selected_blocks();

            }
        }
        //
    }

    public void add_or_remove_multiple_selection(int position) {
        boolean to_add = true;
        for (int i = 0; i < selected_blocks.size(); i++) {
            if ((Integer) selected_blocks.get(i) == position) {
                to_add = false;
                selected_blocks.remove(i);
                Tough2Viewer.dataobj.Block_is_selected[position] = false;
                repaint_model(position);
                break;
            }

        }
        if (to_add) {
            selected_blocks.add(position);
            Tough2Viewer.dataobj.Block_is_selected[position] = true;
            highLightBlock(position, false);
        }

        jLabel1.setText("n=" + Integer.toString(position));
        jLabel2.setText("");
        jLabel3.setText("Bl.Name=" + Tough2Viewer.dataobj.get_BlockName(position));
        jLabel4.setText("X=" + Float.toString((Tough2Viewer.dataobj.get_Xo(position))));
        jLabel5.setText("Y=" + Float.toString((Tough2Viewer.dataobj.get_Yo(position))));
        jLabel6.setText("Z=" + Float.toString((Tough2Viewer.dataobj.get_Zo(position))));
        int rocktype_n = Tough2Viewer.dataobj.get_RockType(position);
        String rocktype = Tough2Viewer.dataobj.get_RockType_name(rocktype_n);
        jLabel7.setText("RockType=" + rocktype);

        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int variable = Tough2Viewer.dataobj.get_actualVariableToPlot();
        float value = Tough2Viewer.dataobj.get_dataArray(position, timestep, variable);
        String variablename = Tough2Viewer.dataobj.get_variables_name(variable);
        String variableUM = Tough2Viewer.dataobj.get_variables_unit(variable);
        jLabel8.setText(variablename + "=" + Float.toString(value) + " " + variableUM);

    }

    public int export_selected() {

        String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
        final JFileChooser fc = new JFileChooser(FilePathString);
        int returnVal = fc.showSaveDialog(this);
        String filenamemesh = "";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String FileNameMesh = fc.getSelectedFile().toString();
            filenamemesh = FileNameMesh;
        } else {
            return 0;
        }
        int[][] connectiontable = Tough2Viewer.dataobj.get_ConnectionTable();
        int n_conne = Tough2Viewer.dataobj.get_n_connections();

        String File_out_mesh = filenamemesh + ".dat";
        String File_out_tough2viewerdat = filenamemesh + "_Tough2Viewer" + ".dat";
        try {
            FileOutputStream fos = new FileOutputStream(File_out_mesh, false);
            FileOutputStream fos2 = new FileOutputStream(File_out_tough2viewerdat, false);
            PrintStream ps;
            PrintStream ps2;

            ps = new PrintStream(fos);
            ps2 = new PrintStream(fos2);
            String lineaOUT;
            lineaOUT = "ELEME - MESH File for " + File_out_mesh + " generated with Tough2Viewer - by Stebond. Note: boundary block rocktype=*ocktype";
            ps.println(lineaOUT);
            ArrayList conne_to_keep = new ArrayList();
            for (int k = 0; k < selected_blocks.size(); k++) {
                int i_block = (int) selected_blocks.get(k);

                lineaOUT = Tough2Viewer.dataobj.get_BlockName(i_block);
                //now we search if this eleme has conne with the other eleme out of this selection

                boolean isboundary = false;
                for (int i = 0; i < n_conne; i++) {
                    if (connectiontable[i][0] == i_block) {
                        int friend_block = connectiontable[i][1];
                        boolean friend_block_is_in_the_list = false;
                        for (int k1 = 0; k1 < selected_blocks.size(); k1++) {
                            if (friend_block == (int) selected_blocks.get(k1)) {
                                friend_block_is_in_the_list = true;
                                boolean to_take = true;
                                for (int i_k = 0; i_k < conne_to_keep.size(); i_k++) {
                                    int tmp_conne = (int) conne_to_keep.get(i_k);
                                    if (tmp_conne == i) {
                                        to_take = false;
                                        break;
                                    }
                                }
                                if (to_take) {
                                    conne_to_keep.add(i);
                                }

                            }
                        }
                        if (!friend_block_is_in_the_list) {
                            isboundary = true;
                        }
                    }
                }
                for (int i = 0; i < n_conne; i++) {
                    if (connectiontable[i][1] == i_block) {
                        int friend_block = connectiontable[i][0];
                        boolean friend_block_is_in_the_list = false;
                        for (int k1 = 0; k1 < selected_blocks.size(); k1++) {
                            if (friend_block == (int) selected_blocks.get(k1)) {
                                friend_block_is_in_the_list = true;
                                boolean to_take = true;
                                for (int i_k = 0; i_k < conne_to_keep.size(); i_k++) {
                                    int tmp_conne = (int) conne_to_keep.get(i_k);
                                    if (tmp_conne == i) {
                                        to_take = false;
                                        break;
                                    }
                                }
                                if (to_take) {
                                    conne_to_keep.add(i);
                                }
                            }
                        }
                        if (!friend_block_is_in_the_list) {
                            isboundary = true;
                        }
                    }
                }
                int rocktype_num = Tough2Viewer.dataobj.get_RockType(i_block);
                String rocktype = Tough2Viewer.dataobj.get_RockType_name(rocktype_num);

                if (isboundary) {
                    rocktype = "*" + rocktype.substring(1);
                }
                lineaOUT = lineaOUT + "          " + rocktype;
                double vol = Tough2Viewer.dataobj.get_BlockVolume(i_block);
                String strvol1 = String.format("%10.3e", vol);
                strvol1 = strvol1.replace(",", ".");
                lineaOUT = lineaOUT + strvol1;
                String Surface = "                    ";
                lineaOUT += Surface;
                String CoordX = String.format("%10.3e", Tough2Viewer.dataobj.get_Xo(i_block)).replace(",", ".");
                String CoordY = String.format("%10.3e", Tough2Viewer.dataobj.get_Yo(i_block)).replace(",", ".");
                String CoordZ = String.format("%10.3e", Tough2Viewer.dataobj.get_Zo(i_block)).replace(",", ".");
                lineaOUT += CoordX + CoordY + CoordZ;
                ps.println(lineaOUT);
                if (Tough2Viewer.dataobj.ID_grid_type == 2) {
                    lineaOUT = (String) Tough2Viewer.dataobj.tough2viewerdatfile.get(i_block);
                    ps2.println(lineaOUT);
                }
            }
            ps.println("");
            ps.println("CONNE");
            for (int i_c = 0; i_c < conne_to_keep.size(); i_c++) {
                String conne_str = (String) Tough2Viewer.dataobj.conneArrayList.get((int) conne_to_keep.get(i_c));
                ps.println(conne_str);
            }
            ps.println("");
            fos.close();
            fos2.close();
        } catch (IOException e) {
            String output = "Unable to write file";
            Tough2Viewer.toLogFile(output);
            //System.exit(-1);
        }
        return 0;
    }

    public boolean read_selection_file(String file) {

        if (selected_blocks.size() > 0) {
            String title = "Clear previous selection confirmation";
            String message = "Clear previous block selection and continue?";

            int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.NO_OPTION) {
                return false;
            }

        }
        for (int i = 0; i < selected_blocks.size(); i++) {
            repaint_model((Integer) selected_blocks.get(i));
        }
        selected_blocks.clear();
        FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        String linea;
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            linea = (String) dis.readLine();//this is the header...value not used
            boolean end_conne = true;
            while (dis.available() != 0 || end_conne) {
                linea = (String) dis.readLine();
                if (linea.contains("CONNE") || linea.contains("conne")) {
                    break;
                }
                if (linea.length() < Tough2Viewer.dataobj.eleme_lenght) {
                    break;
                }
                String sottostringa;
                sottostringa = linea.substring(0, Tough2Viewer.dataobj.eleme_lenght);
                for (int i = 0; i < nxyz; i++) {
                    if (Tough2Viewer.dataobj.get_BlockName(i).equals(sottostringa)) {
                        add_or_remove_multiple_selection(i);

                    }

                }

            }
            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return true;
    }

    public int export_selected_elements(String stringtoadd) {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        int n_elementstoadd = 0;
        for (int j = 0; j < nxyz; j++) {
            if (Tough2Viewer.dataobj.Block_is_selected[j]) {
                n_elementstoadd++;
            }
        }
        int numberof_cancelletti = stringtoadd.lastIndexOf("#") - stringtoadd.indexOf("#") + 1;
        int max_number_of_digit_of_the_string = (int) Math.pow((double) 10.0, (double) numberof_cancelletti);
        if (n_elementstoadd > max_number_of_digit_of_the_string) {
            String message = "Please add more # to the string";
            JOptionPane.showMessageDialog(null, message);
            return 1;
        }

        String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
        final JFileChooser fc = new JFileChooser(FilePathString);
        int returnVal = fc.showSaveDialog(this);
        String filenamemesh = "";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String FileNameMesh = fc.getSelectedFile().toString();
            filenamemesh = FileNameMesh;
        } else {
            return 0;
        }

        String File_out_mesh = filenamemesh + ".selection";
        try {
            FileOutputStream fos = new FileOutputStream(File_out_mesh, false);
            PrintStream ps;
            ps = new PrintStream(fos);
            String lineaOUT;
            lineaOUT = "ELEME - SELECTED ELEMENTS File for " + File_out_mesh + " generated with Tough2Viewer - by Stebond.";
            ps.println(lineaOUT);
            ArrayList conne_to_keep = new ArrayList();
            int progreesive_id = 0;
            for (int j = 0; j < nxyz; j++) {
                if (Tough2Viewer.dataobj.Block_is_selected[j]) {

                    String format = String.format("%%0%dd", numberof_cancelletti);
                    String result = String.format(format, progreesive_id);
                    String tail = stringtoadd.substring(stringtoadd.lastIndexOf("#") + 1);
                    String final_string = stringtoadd.substring(0, stringtoadd.indexOf("#")) + result + tail;
                    lineaOUT = Tough2Viewer.dataobj.get_BlockName(j) + final_string;
                    //now we search if this eleme has conne with the other eleme out of this selection
                    ps.println(lineaOUT);
                    progreesive_id++;
                }
            }
            ps.println("");
            fos.close();
        } catch (IOException e) {
            String output = "Unable to write file";
            Tough2Viewer.toLogFile(output);
            //System.exit(-1);
        }
        return 0;
    }

    public void select_neighbord(int sel) {

        if (sel >= 0) {

            int[][] connectiontable = Tough2Viewer.dataobj.get_ConnectionTable();
            int n_conne = Tough2Viewer.dataobj.get_n_connections();
            for (int i = 0; i < selected_blocks.size(); i++) {
                repaint_model((Integer) selected_blocks.get(i));
            }
            selected_blocks.clear();
            int nxyz = Tough2Viewer.dataobj.get_nxyz();
            for (int j = 0; j < nxyz; j++) {
                Tough2Viewer.dataobj.Block_is_selected[j] = false;
            }
            selected_blocks.add(sel);
            Tough2Viewer.dataobj.Block_is_selected[sel] = true;
            update_block_Info(sel);
            int multi_select_level = Tough2Viewer.dataobj.MultiSelectionLevel;
            ArrayList[] tmp = new ArrayList[multi_select_level];
            for (int j = 0; j < multi_select_level; j++) {
                tmp[j] = new ArrayList();
                for (int k = 0; k < selected_blocks.size(); k++) {
                    sel = (Integer) selected_blocks.get(k);
                    for (int i = 0; i < n_conne; i++) {
                        if (connectiontable[i][0] == sel) {
                            int candidate = connectiontable[i][1];
                            boolean ok = true;
                            for (int i1 = 0; i1 < selected_blocks.size(); i1++) {
                                if (candidate == (Integer) selected_blocks.get(i1)) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok) {
                                tmp[j].add(connectiontable[i][1]);
                            }
                        }
                        if (connectiontable[i][1] == sel) {
                            int candidate = connectiontable[i][0];
                            boolean ok = true;
                            for (int i1 = 0; i1 < selected_blocks.size(); i1++) {
                                if (candidate == (Integer) selected_blocks.get(i1)) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok) {
                                tmp[j].add(connectiontable[i][0]);
                            }

                        }

                    }
                }
                for (int i2 = 0; i2 < tmp[j].size(); i2++) {
                    int candidate = (Integer) tmp[j].get(i2);
                    boolean ok = true;
                    for (int i1 = 0; i1 < selected_blocks.size(); i1++) {
                        if (candidate == (Integer) selected_blocks.get(i1)) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        selected_blocks.add(candidate);
                    }
                    Tough2Viewer.dataobj.Block_is_selected[candidate] = true;
                }
            }
            for (int i = 0; i < selected_blocks.size(); i++) {
                highLightBlock((Integer) selected_blocks.get(i), false);
            }
            highLightBlock((Integer) selected_blocks.get(0), false);
            //writing MESH file related to this selection:
            int the_clicked_block = (int) selected_blocks.get(0);
            String FileNameMesh = "MESH_" + Tough2Viewer.dataobj.get_BlockName(the_clicked_block) + "_Conne_level_" + Integer.toString(multi_select_level) + ".dat";
            String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
            String File_out_mesh = FilePathString + "\\" + FileNameMesh;
            String File_out_tough2viewerdat = FilePathString + "\\" + "Tough2Viewer_" + Tough2Viewer.dataobj.get_BlockName(the_clicked_block) + "_Conne_level_" + Integer.toString(multi_select_level) + ".dat";
            try {
                FileOutputStream fos = new FileOutputStream(File_out_mesh, false);
                FileOutputStream fos2 = new FileOutputStream(File_out_tough2viewerdat, false);
                PrintStream ps;
                PrintStream ps2;

                ps = new PrintStream(fos);
                ps2 = new PrintStream(fos2);
                String lineaOUT;
                lineaOUT = "ELEME - MESH File for " + FileNameMesh + " generated with Tough2Viewer - by Stebond. Note: boundary block rocktype=*ocktype";
                ps.println(lineaOUT);
                ArrayList conne_to_keep = new ArrayList();
                for (int k = 0; k < selected_blocks.size(); k++) {
                    int i_block = (int) selected_blocks.get(k);

                    lineaOUT = Tough2Viewer.dataobj.get_BlockName(i_block);
                    //now we search if this eleme has conne with the other eleme out of this selection

                    boolean isboundary = false;
                    for (int i = 0; i < n_conne; i++) {
                        if (connectiontable[i][0] == i_block) {
                            int friend_block = connectiontable[i][1];
                            boolean friend_block_is_in_the_list = false;
                            for (int k1 = 0; k1 < selected_blocks.size(); k1++) {
                                if (friend_block == (int) selected_blocks.get(k1)) {
                                    friend_block_is_in_the_list = true;
                                    boolean to_take = true;
                                    for (int i_k = 0; i_k < conne_to_keep.size(); i_k++) {
                                        int tmp_conne = (int) conne_to_keep.get(i_k);
                                        if (tmp_conne == i) {
                                            to_take = false;
                                            break;
                                        }
                                    }
                                    if (to_take) {
                                        conne_to_keep.add(i);
                                    }

                                }
                            }
                            if (!friend_block_is_in_the_list) {
                                isboundary = true;
                            }
                        }
                    }
                    for (int i = 0; i < n_conne; i++) {
                        if (connectiontable[i][1] == i_block) {
                            int friend_block = connectiontable[i][0];
                            boolean friend_block_is_in_the_list = false;
                            for (int k1 = 0; k1 < selected_blocks.size(); k1++) {
                                if (friend_block == (int) selected_blocks.get(k1)) {
                                    friend_block_is_in_the_list = true;
                                    boolean to_take = true;
                                    for (int i_k = 0; i_k < conne_to_keep.size(); i_k++) {
                                        int tmp_conne = (int) conne_to_keep.get(i_k);
                                        if (tmp_conne == i) {
                                            to_take = false;
                                            break;
                                        }
                                    }
                                    if (to_take) {
                                        conne_to_keep.add(i);
                                    }
                                }
                            }
                            if (!friend_block_is_in_the_list) {
                                isboundary = true;
                            }
                        }
                    }
                    int rocktype_num = Tough2Viewer.dataobj.get_RockType(i_block);
                    String rocktype = Tough2Viewer.dataobj.get_RockType_name(rocktype_num);

                    if (isboundary) {
                        rocktype = "*" + rocktype.substring(1);
                    }
                    lineaOUT = lineaOUT + "          " + rocktype;
                    double vol = Tough2Viewer.dataobj.get_BlockVolume(i_block);
                    String strvol1 = String.format("%10.3e", vol);
                    strvol1 = strvol1.replace(",", ".");
                    lineaOUT = lineaOUT + strvol1;
                    String Surface = "                    ";
                    lineaOUT += Surface;
                    String CoordX = String.format("%10.3e", Tough2Viewer.dataobj.get_Xo(i_block)).replace(",", ".");
                    String CoordY = String.format("%10.3e", Tough2Viewer.dataobj.get_Yo(i_block)).replace(",", ".");
                    String CoordZ = String.format("%10.3e", Tough2Viewer.dataobj.get_Zo(i_block)).replace(",", ".");
                    lineaOUT += CoordX + CoordY + CoordZ;
                    ps.println(lineaOUT);
                    if (Tough2Viewer.dataobj.ID_grid_type == 2) {
                        lineaOUT = (String) Tough2Viewer.dataobj.tough2viewerdatfile.get(i_block);
                        ps2.println(lineaOUT);
                    }
                }
                ps.println("");
                ps.println("CONNE");
                for (int i_c = 0; i_c < conne_to_keep.size(); i_c++) {
                    String conne_str = (String) Tough2Viewer.dataobj.conneArrayList.get((int) conne_to_keep.get(i_c));
                    ps.println(conne_str);
                }
                ps.println("");
                fos.close();
                fos2.close();
            } catch (IOException e) {
                String output = "Unable to write file";
                Tough2Viewer.toLogFile(output);
                //System.exit(-1);
            }
        }

    }

    public void export_modified_MESH() {
        //writing MESH file related to this selection:
        String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
        final JFileChooser fc = new JFileChooser(FilePathString);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String FileNameMesh = fc.getSelectedFile().toString();
            String File_out_mesh = FileNameMesh + ".dat";
            String File_out_tough2viewerdat = FileNameMesh + "_tough2viewer.dat";
            try {
                FileOutputStream fos = new FileOutputStream(File_out_mesh, false);
                FileOutputStream fos2 = new FileOutputStream(File_out_tough2viewerdat, false);
                PrintStream ps;
                PrintStream ps2;
                ps = new PrintStream(fos);
                ps2 = new PrintStream(fos2);
                String lineaOUT;
                lineaOUT = "ELEME - MESH File for " + FileNameMesh + " generated with Tough2Viewer - by Stebond. Note: boundary block rocktype=*ocktype";
                ps.println(lineaOUT);
                for (int k = 0; k < Tough2Viewer.dataobj.get_nxyz(); k++) {

                    lineaOUT = Tough2Viewer.dataobj.get_BlockName(k);
                    //now we search if this eleme has conne with the other eleme out of this selection
                    int rocktype_num = Tough2Viewer.dataobj.get_RockType(k);
                    String rocktype = Tough2Viewer.dataobj.get_RockType_name(rocktype_num);
                    lineaOUT = lineaOUT + "          " + rocktype;
                    double vol = Tough2Viewer.dataobj.get_BlockVolume(k);
                    String strvol1 = String.format("%10.4e", vol);
                    strvol1 = strvol1.replace(",", ".");
                    lineaOUT = lineaOUT + strvol1;
                    String Surface = "                    ";
                    lineaOUT += Surface;
                    String CoordX = String.format("%10.3e", Tough2Viewer.dataobj.get_Xo(k)).replace(",", ".");
                    String CoordY = String.format("%10.3e", Tough2Viewer.dataobj.get_Yo(k)).replace(",", ".");
                    String CoordZ = String.format("%10.3e", Tough2Viewer.dataobj.get_Zo(k)).replace(",", ".");
                    lineaOUT += CoordX + CoordY + CoordZ;
                    ps.println(lineaOUT);
                    if (Tough2Viewer.dataobj.ID_grid_type == 2) {
                        lineaOUT = (String) Tough2Viewer.dataobj.tough2viewerdatfile.get(k);
                        ps2.println(lineaOUT);
                    }
                }
                ps.println("");
                ps.println("CONNE");
                for (int i_c = 0; i_c < Tough2Viewer.dataobj.conneArrayList.size(); i_c++) {
                    String conne_str = (String) Tough2Viewer.dataobj.conneArrayList.get(i_c);
                    ps.println(conne_str);
                }
                ps.println("");
                fos.close();
                fos2.close();
            } catch (IOException e) {
                String error = "ExportModifiedMesh -> Unable to write file";
                Tough2Viewer.toLogFile(error);
                JOptionPane.showMessageDialog(null, error);
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    public void destroy() {
        ModelUniverse.cleanup();
    }
    private javax.swing.JButton closeButton;

}
