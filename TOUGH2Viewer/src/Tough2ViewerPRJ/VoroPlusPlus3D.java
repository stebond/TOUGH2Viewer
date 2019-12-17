/*
 * $RCSfile: SwingInteraction.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2007/02/09 17:21:53 $
 * $State: Exp $
 */
package Tough2ViewerPRJ;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Simple Java 3D test program created in NetBeans to illustrate interacting
 * with a Java 3D scene graph from an Swing-based program.
 */
public class VoroPlusPlus3D extends javax.swing.JFrame {

    private double xmin, xmax, ymin, ymax, zmin, zmax;
    private SimpleUniverse univ = null;
    private BranchGroup scene = null;
    private Switch posSwitchBox;
    private BitSet posMaskBox;
    private TransformGroup objTrans;

    private RotateBehaviorX awtBehaviorX;
    private RotateBehaviorZ awtBehaviorY;
    private RotateBehaviorZ awtBehaviorZ;
    private Group group;
    private int totalshape = 0;
    private SetVoi SET_VOIWindow;
    //ArrayList myLocalData;
    PickHighlightBehaviorVoroPlusPlus pickBeh;

    Canvas3D ModelCanvas3D;
    ViewingPlatform ModelViewingPlatform;
    private DirectionalLight lgt1;
    private DirectionalLight lgt2;
    private AmbientLight aLgt;
    private float viewanglex = 0.0f;
    double anglez = 0;

    public VoroPlusPlus3D(ArrayList AllData, double xminE, double xmaxE, double yminE, double ymaxE, double zminE, double zmaxE) {
        // Initialize the GUI components
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initComponents();
        this.setTitle("VoroPlusPlus preview 3D");
        // Create Canvas3D and SimpleUniverse; add canvas to drawing panel
        xmin = xminE;
        xmax = xmaxE;
        ymin = yminE;
        ymax = ymaxE;
        zmin = zminE;
        zmax = zmaxE;
        //myLocalData=AllData;
        Tough2Viewer.dataobj.VoroPPData = AllData;
        ModelCanvas3D = createUniverse();
        drawingPanel.add(ModelCanvas3D, java.awt.BorderLayout.CENTER);

        // Create the content branch and add it to the universe
        scene = createSceneGraph();

        univ.addBranchGraph(scene);

    }

    public BranchGroup createSceneGraph() {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();

        // Create the transform group node and initialize it to the
        // identity.  Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at runtime.  Add it to the
        // root of the subgraph.
        objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        objRoot.addChild(objTrans);

        // Create a simple shape leaf node, add it to the scene graph.
        Transform3D BoxMat = new Transform3D();
        BoxMat.set(new Vector3d(5.0f, 5.0f, 5.0f));
        TransformGroup BoxTrans = new TransformGroup(BoxMat);
        BoxTrans.addChild(new ColorCube(0.1));
        objTrans.addChild(BoxTrans);
        //objTrans.addChild(new ColorCube(0.1));
        //qui aggiungo la mia shape.........
        posSwitchBox = new Switch(Switch.CHILD_MASK);  //
        posSwitchBox.setCapability(Switch.ALLOW_SWITCH_READ);
        posSwitchBox.setCapability(Switch.ALLOW_SWITCH_WRITE);
        // Create the bit masks
        posMaskBox = new BitSet();

        double lx, ly, lz, lmax, riduzione;
        lx = xmax - xmin;
        ly = ymax - ymin;
        lz = zmax - zmin;
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
        double contrazione = 0.9f;
        riduzione = 1.0f / lmax;
        double xminV, xmaxV, yminV, ymaxV, zminV, zmaxV;
        float campo = 1.0f;
        xminV = -0.5f * lx / lmax;
        xmaxV = 0.5f * lx / lmax;
        yminV = -0.5f * ly / lmax;
        ymaxV = 0.5f * ly / lmax;
        zminV = -0.5f * lz / lmax;
        zmaxV = 0.5f * lz / lmax;

        //int number_of_box=myLocalData.size();
        int number_of_box = Tough2Viewer.dataobj.VoroPPData.size();
        for (int i = 0; i < number_of_box; i++) {
            Color3f mycolor = new Color3f((float) Math.random(), (float) Math.random(), (float) Math.random());
            ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(i);
            VoronoiPlusPlusBox myVorPlusPlusBox = new VoronoiPlusPlusBox(singleBox, contrazione, riduzione, xminV, yminV, zminV, xmin, ymin, zmin, mycolor, 0, 1.0);
            myVorPlusPlusBox.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
            myVorPlusPlusBox.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
            Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
            Color3f objColor = mycolor;
            Material matMybox = new Material(objColor, black, objColor, white, 100.f);
            matMybox.setLightingEnable(true);
            Appearance appMyBox = new Appearance();
            appMyBox.setMaterial(matMybox);
            myVorPlusPlusBox.setAppearance(appMyBox);
            ID id = new ID(i);
            myVorPlusPlusBox.setUserData(id);
            posMaskBox.set(i);//per ora tutti visibili
            posSwitchBox.addChild(myVorPlusPlusBox);//13-05-03
            //objTrans.addChild(myVorPlusPlusBox);
        }
        // Set the positions mask
        posSwitchBox.setChildMask(posMaskBox);
        // Throw everything into a single group
        group = new Group();
        group.addChild(posSwitchBox);
        objTrans.addChild(group);
        OrbitBehavior ModelOrbit = new OrbitBehavior(ModelCanvas3D, OrbitBehavior.REVERSE_ALL);
        BoundingSphere ModelBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        ModelOrbit.setSchedulingBounds(ModelBounds);
        ModelViewingPlatform.setViewPlatformBehavior(ModelOrbit);
        // create the RotateBehavior	

        awtBehaviorX = new RotateBehaviorX(objTrans);
        awtBehaviorY = new RotateBehaviorZ(objTrans);
        awtBehaviorZ = new RotateBehaviorZ(objTrans);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                100.0);
        awtBehaviorX.setSchedulingBounds(bounds);
        awtBehaviorY.setSchedulingBounds(bounds);
        awtBehaviorZ.setSchedulingBounds(bounds);
        objRoot.addChild(awtBehaviorX);
        objRoot.addChild(awtBehaviorY);
        objRoot.addChild(awtBehaviorZ);

        //ADD LIGHTS
        Color3f lColor1 = new Color3f(0.9f, 0.9f, 0.9f);
        Color3f lColor2 = new Color3f(0.7f, 0.7f, 0.7f);

        Vector3f lDir1 = new Vector3f(-2.0f, -1.0f, -1.0f);//era -1,-1,-1
        Vector3f lDir2 = new Vector3f(0.0f, 0.0f, -1.0f);

        lgt1 = new DirectionalLight(lColor1, lDir1);
        lgt2 = new DirectionalLight(lColor2, lDir2);

        lgt1.setEnable(true);
        lgt2.setEnable(true);

        lgt1.setInfluencingBounds(bounds);
        lgt2.setInfluencingBounds(bounds);

        lgt1.setCapability(DirectionalLight.ALLOW_STATE_READ);
        lgt1.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
        lgt2.setCapability(DirectionalLight.ALLOW_STATE_READ);
        lgt2.setCapability(DirectionalLight.ALLOW_STATE_WRITE);

        lgt1.setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE);
        lgt1.setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE);
        lgt2.setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE);
        lgt2.setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE);

        //lgt1.setCapability(DirectionalLight.)
        //  textTransformGroup.addChild(lgt1);
        //  textTransformGroup.addChild(lgt2);
        Color3f alColor = new Color3f(0.9f, 0.9f, 0.9f);
        aLgt = new AmbientLight(alColor);
        aLgt.setInfluencingBounds(bounds);
        objRoot.addChild(lgt1);
        objRoot.addChild(lgt2);

        objRoot.addChild(aLgt);
        pickBeh = new PickHighlightBehaviorVoroPlusPlus(ModelCanvas3D, objRoot, bounds);
        return objRoot;

    }

    public void pick(int pos) {
        String str_id = Integer.toString(pos);
        ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(pos);
        double[] centerD = (double[]) singleBox.get(1);
        String str_xyz = Double.toString(centerD[0]) + ";" + Double.toString(centerD[1]) + ";" + Double.toString(centerD[2]);
        jLabel1.setText("Block:" + str_id + ";" + str_xyz);
    }

    private Canvas3D createUniverse() {
        GraphicsConfiguration config
                = SimpleUniverse.getPreferredConfiguration();

        Canvas3D c = new Canvas3D(config);

        univ = new SimpleUniverse(c);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        univ.getViewingPlatform().setNominalViewingTransform();

        // Ensure at least 5 msec per frame (i.e., < 200Hz)
        univ.getViewer().getView().setMinimumFrameCycleTime(5);
        ModelViewingPlatform = univ.getViewingPlatform();
        return c;
    }

    /**
     * Creates new form SwingInteraction
     */
    public void Repaint() {
        int number_of_box = Tough2Viewer.dataobj.VoroPPData.size();
        for (int i_b = 0; i_b < number_of_box; i_b++) {

            //per ora lo prendo di qui, anche se potre prenderlo da dataobj...
            ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(i_b);
            double[] centerD = (double[]) singleBox.get(1);//POS=1
            double Xo = centerD[0];
            double Yo = centerD[1];
            double Zo = centerD[2];
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
            boolean visible = false;
            boolean useHideROI = Tough2Viewer.dataobj.get_useHideROI();
            if (Xo >= ROI_xmin & Xo <= ROI_xmax) {
                if (Yo >= ROI_ymin & Yo <= ROI_ymax) {
                    if (Zo >= ROI_zmin & Zo <= ROI_zmax) {
                        visible = true;
                        if (useHideROI) {
                            if (Xo >= HideROI_xmin & Xo <= HideROI_xmax) {
                                if (Yo >= HideROI_ymin & Yo <= HideROI_ymax) {
                                    if (Zo >= HideROI_zmin & Zo <= HideROI_zmax) {
                                        visible = false;
                                    }

                                }

                            }
                        }
                    }
                }
            }
            if (visible) {

                posMaskBox.set(i_b);
            } else {
                posMaskBox.clear(i_b);
            }
            posSwitchBox.setChildMask(posMaskBox);

        }
    }

    /**
     * Behavior class that waits for a behavior post from the AWT event handler
     */
    /*class RotateBehaviorY extends Behavior {

        private TransformGroup transformGroup;
        private Transform3D trans = new Transform3D();
        private WakeupCriterion criterion;
        private float angleY = 0.0f;
        private final int ROTATE = 1;

        // create a new RotateBehavior
        RotateBehaviorY(TransformGroup tg) {
            transformGroup = tg;
        }

        // initialize behavior to wakeup on a behavior post with id = ROTATE
        public void initialize() {
            criterion = new WakeupOnBehaviorPost(this, ROTATE);
            wakeupOn(criterion);
        }

        // processStimulus to rotate the cube
        public void processStimulus(Enumeration criteria) {
            angleY += Math.toRadians(10.0);
            trans.rotY(angleY);
            transformGroup.setTransform(trans);
            wakeupOn(criterion);
        }

        // when the mouse is clicked, postId for the behavior
        void rotate() {
            postId(ROTATE);
        }
    }*/
    class RotateBehaviorX extends Behavior {

        private TransformGroup transformGroup;
        private Transform3D trans = new Transform3D();
        private WakeupCriterion criterion;
        private float angleX = 0.0f;
        private final int ROTATE = 1;

        // create a new RotateBehavior
        RotateBehaviorX(TransformGroup tg) {
            transformGroup = tg;
        }

        // initialize behavior to wakeup on a behavior post with id = ROTATE
        public void initialize() {
            criterion = new WakeupOnBehaviorPost(this, ROTATE);
            wakeupOn(criterion);
        }

        // processStimulus to rotate the cube
        public void processStimulus(Enumeration criteria) {
            angleX += Math.toRadians(10.0);
            trans.rotX(angleX);
            transformGroup.setTransform(trans);
            wakeupOn(criterion);
        }

        // when the mouse is clicked, postId for the behavior
        void rotate() {
            postId(ROTATE);
        }
    }

    // ----------------------------------------------------------------
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jButton1 = new javax.swing.JButton();
        drawingPanel = new javax.swing.JPanel();
        guiPanel = new javax.swing.JPanel();
        rotateButtonX = new javax.swing.JButton();
        rotateButtonY = new javax.swing.JButton();
        rotateButtonZ = new javax.swing.JButton();
        jComboAction = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButtonTOPView = new javax.swing.JButton();
        RotateViewX = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Swing Interaction Test");

        drawingPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        drawingPanel.setLayout(new java.awt.BorderLayout());

        guiPanel.setLayout(new java.awt.GridBagLayout());

        rotateButtonX.setText("RotateX");
        rotateButtonX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateButtonXActionPerformed(evt);
            }
        });
        guiPanel.add(rotateButtonX, new java.awt.GridBagConstraints());

        rotateButtonY.setText("RotateY");
        rotateButtonY.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rotateButtonY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateButtonYActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        guiPanel.add(rotateButtonY, gridBagConstraints);

        rotateButtonZ.setText("RotateZ");
        rotateButtonZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateButtonZActionPerformed(evt);
            }
        });
        guiPanel.add(rotateButtonZ, new java.awt.GridBagConstraints());

        jComboAction.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "SetROI", "Item 3", "Item 4" }));
        jComboAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboActionActionPerformed(evt);
            }
        });
        guiPanel.add(jComboAction, new java.awt.GridBagConstraints());

        jLabel1.setText("Block:");
        guiPanel.add(jLabel1, new java.awt.GridBagConstraints());

        jButtonTOPView.setText("Top View");
        jButtonTOPView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTOPViewActionPerformed(evt);
            }
        });
        guiPanel.add(jButtonTOPView, new java.awt.GridBagConstraints());

        RotateViewX.setText("RotateViewX");
        RotateViewX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RotateViewXActionPerformed(evt);
            }
        });
        guiPanel.add(RotateViewX, new java.awt.GridBagConstraints());

        jButton2.setText("Test");
        guiPanel.add(jButton2, new java.awt.GridBagConstraints());

        drawingPanel.add(guiPanel, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(drawingPanel, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rotateButtonYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateButtonYActionPerformed
        awtBehaviorY.rotate();
    }//GEN-LAST:event_rotateButtonYActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jComboActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboActionActionPerformed
        //if(jComboAction)
        if (SET_VOIWindow == null) {
            SET_VOIWindow = new SetVoi();
        }

        SET_VOIWindow.setVisible(true);
        Repaint();
    }//GEN-LAST:event_jComboActionActionPerformed

    private void rotateButtonXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateButtonXActionPerformed
        awtBehaviorX.rotate();
    }//GEN-LAST:event_rotateButtonXActionPerformed

    private void rotateButtonZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateButtonZActionPerformed
        awtBehaviorZ.rotate();
    }//GEN-LAST:event_rotateButtonZActionPerformed

    private void jButtonTOPViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTOPViewActionPerformed
        awtBehaviorZ.reset();
        ChangeView(0);
    }//GEN-LAST:event_jButtonTOPViewActionPerformed

    private void RotateViewXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RotateViewXActionPerformed
        awtBehaviorZ.setStepAngle(0.1);
        RotateX(0);
    }//GEN-LAST:event_RotateViewXActionPerformed
    void RotateX(int index) {
//        TransformGroup viewTransformGroup=univ.getViewingPlatform().getViewPlatformTransform();
//         Transform3D t3dRotation = new Transform3D();
//         Transform3D t3dInitial = new Transform3D();
//         Vector3f InitialPointView = new Vector3f();
//         Vector3f FinalPointView = new Vector3f();
//         viewTransformGroup.getTransform(t3dInitial);
//         t3dInitial.get(InitialPointView);
//         viewanglex =viewanglex+(float)Math.toRadians(1.0);
//         Matrix3f m3d=new Matrix3f();
//         t3dInitial.get(m3d);  
//         m3d.rotZ(viewanglex);
//         t3dRotation.setRotation(m3d);
//         //t3dInitial.mul(t3dRotation);
//         float d=InitialPointView.length();
//         FinalPointView.x=InitialPointView.x;
//         FinalPointView.y=InitialPointView.y;
//         FinalPointView.z=InitialPointView.z;
//         
//         Transform3D t3dTranslation=new Transform3D();
//         t3dTranslation.setTranslation(FinalPointView);
//         t3dTranslation.mul(t3dRotation);
////         
////         
////         viewTransformGroup.setTransform(t3dTranslation);
//            anglez += Math.toRadians(5.0);
//            
//            trans.rotZ(anglez);
//            transformGroup.setTransform(trans);
//    

    }

    public void ChangeView(int index) {

        TransformGroup viewTransformGroup = univ.getViewingPlatform().getViewPlatformTransform();

        Transform3D t3dInitial = new Transform3D();
        Transform3D t3dTranslation = new Transform3D();
        Transform3D t3dRotation = new Transform3D();

        viewTransformGroup.getTransform(t3dInitial);

        Vector3f InitialPointView = new Vector3f();

        Vector3f FinalPointView[] = new Vector3f[7];
        FinalPointView[0] = new Vector3f(0.0f, 0.0f, (float) zmax + 2.47f);//topview
        FinalPointView[1] = new Vector3f(-2.47f + (float) xmin, 0.0f, 0.0f);//left
        FinalPointView[2] = new Vector3f(0.0f, -2.47f + (float) ymin, 0.0f);//front
        FinalPointView[3] = new Vector3f(2.47f + (float) xmax, 0.0f, 0.0f);//rigt
        FinalPointView[4] = new Vector3f(0.0f, 2.47f + (float) ymax, 0.0f);//back
        FinalPointView[5] = new Vector3f(0.0f, 0.0f, -2.47f + (float) zmin);//Bottom
        FinalPointView[6] = new Vector3f(2.47f + (float) xmax, -2.47f + (float) ymin, 2.47f + (float) zmax);//Bottom
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
        m3dtest[6] = new Matrix3f(1f, 0f, -.433f,
                0f, 0f, 1f,
                .25f, 0f, .0f);//Bottom
        t3dRotation.setRotation(m3dtest[index]);
        t3dTranslation.setTranslation(FinalPointView[index]);
        t3dTranslation.mul(t3dRotation);

        viewTransformGroup.setTransform(t3dTranslation);

    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton RotateViewX;
    private javax.swing.JPanel drawingPanel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel guiPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonTOPView;
    private javax.swing.JComboBox jComboAction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JButton rotateButtonX;
    private javax.swing.JButton rotateButtonY;
    private javax.swing.JButton rotateButtonZ;
    // End of variables declaration//GEN-END:variables

}
