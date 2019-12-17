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

package Tough2ViewerPRJ.MeshGen;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.*;
import Tough2ViewerPRJ.BoundingBox3D;
import Tough2ViewerPRJ.JoeStringUtils1;
import Tough2ViewerPRJ.PickHighlightBehaviorVoroPlusPlus;
import Tough2ViewerPRJ.SetVoi;
import Tough2ViewerPRJ.Tough2Viewer;
import static Tough2ViewerPRJ.NewOkCancelDialog.RET_OK;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.GraphicsConfiguration;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Simple Java 3D test program created in NetBeans to illustrate interacting
 * with a Java 3D scene graph from an Swing-based program.
 */
public class MeshGenerator3D extends javax.swing.JFrame {
    public float xmin,xmax,ymin,ymax,zmin,zmax,xmin_v,ymin_v,zmin_v;
    private SimpleUniverse univ = null;
    private BranchGroup scene = null;
    private Switch posSwitchBox;
    private BitSet posMaskBox;
    private TransformGroup objTrans;
    private RotateBehavior awtBehavior;
    private Group group;
    private int totalshape=0;
    private SetVoi SET_VOIWindow;
    private BoundingBox3D BoundingBox;
    //ArrayList myLocalData;
    PickHighlightBehaviorVoroPlusPlus pickBeh;
    public float scalefactor;
    private BranchGroup BoundingBoxBranchGroup;
    
    Canvas3D ModelCanvas3D;
    ViewingPlatform ModelViewingPlatform;
    private DirectionalLight lgt1;
    private DirectionalLight lgt2;
    private AmbientLight aLgt;
    private ArrayList surfacearray;
    String FilePathString;
    int[] rocktype;
    ArrayList TotalLinePoints=new ArrayList();
    public MeshGenerator3D() {
        // Initialize the GUI components
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initComponents();
        this.setTitle("MeshGenerator3D");
        
        // Create Canvas3D and SimpleUniverse; add canvas to drawing panel
        //Default values:
        xmin=0.0f;
        xmax=1000.0f;
        ymin=0.0f;
        ymax=1000.0f;
        zmin=0.0f;
        zmax=1000.0f;
        xmin_v=-0.5f;
        ymin_v=-0.5f;
        zmin_v=-0.5f;
        scalefactor=1.0f/xmax;
        //myLocalData=AllData;
        surfacearray=new ArrayList();
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
        objTrans.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        objTrans.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        objTrans.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
	
        objRoot.addChild(objTrans);

	// Create a simple shape leaf node, add it to the scene graph.
	Transform3D BoxMat = new Transform3D();
        BoxMat.set(new Vector3d(0.0f,0.0f,0.0f));
        TransformGroup BoxTrans = new TransformGroup(BoxMat);
        BoundingBox=new BoundingBox3D(xmin,ymin,zmin,xmax,ymax,zmax,scalefactor);
        BoundingBoxBranchGroup = new BranchGroup();
        BoundingBoxBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        BoundingBoxBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        BoundingBoxBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        
        BoxTrans.addChild(BoundingBox);
        BoundingBoxBranchGroup.addChild(BoxTrans);
        objTrans.addChild(BoundingBoxBranchGroup);
        //objTrans.addChild(new ColorCube(0.1));
        //qui aggiungo la mia shape.........
        posSwitchBox = new Switch(Switch.CHILD_MASK);  //
        posSwitchBox.setCapability(Switch.ALLOW_SWITCH_READ);
        posSwitchBox.setCapability(Switch.ALLOW_SWITCH_WRITE);
       // Create the bit masks
        posMaskBox = new BitSet();

        double lx,ly,lz,lmax,riduzione;
        lx=xmax-xmin;
        ly=ymax-ymin;
        lz=zmax-zmin;
        lmax=lx;
        if(lmax<=ly)
        {
            lmax=ly;
        }
        if(lmax<=lz)
        {
            lmax=lz;
        }
        if(lmax==0)
        {
            String error="L max=0. Check IN file or MESH file";
            JOptionPane.showMessageDialog(null, error);
      
        }
        double contrazione=0.9f;
        riduzione=1.0f/lmax;
        double xminV,xmaxV,yminV,ymaxV,zminV,zmaxV;
        float campo=1.0f;
        xminV=-0.5f*lx/lmax;
        xmaxV=0.5f*lx/lmax;
        yminV=-0.5f*ly/lmax;
        ymaxV=0.5f*ly/lmax;
        zminV=-0.5f*lz/lmax;
        zmaxV=0.5f*lz/lmax;
   
             
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
        
        
        awtBehavior = new RotateBehavior(objTrans);
	BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
						   100.0);
	awtBehavior.setSchedulingBounds(bounds);
	objRoot.addChild(awtBehavior);

        
        //ADD LIGHTS
        Color3f lColor1 = new Color3f(0.9f, 0.9f, 0.9f);
        Color3f lColor2 = new Color3f(0.7f, 0.7f, 0.7f);
        
        Vector3f lDir1  = new Vector3f(-2.0f, -1.0f, -1.0f);//era -1,-1,-1
        Vector3f lDir2  = new Vector3f(0.0f, 0.0f, -1.0f);
        
        
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
        
        
        lgt1.setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE );
	lgt1.setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE );
        lgt2.setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE );
	lgt2.setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE );
        
        //lgt1.setCapability(DirectionalLight.)
      //  textTransformGroup.addChild(lgt1);
      //  textTransformGroup.addChild(lgt2);
        Color3f alColor = new Color3f(0.9f, 0.9f, 0.9f);
        aLgt = new AmbientLight(alColor);
        aLgt.setInfluencingBounds(bounds);
        objRoot.addChild(lgt1);
        objRoot.addChild(lgt2);
        
        objRoot.addChild(aLgt); 
        pickBeh= new PickHighlightBehaviorVoroPlusPlus(ModelCanvas3D, objRoot, bounds);
	return objRoot;
        
    }

   public void pick(int pos)
   {
       String str_id=Integer.toString(pos);
       //ArrayList singleBox=(ArrayList)Tough2Viewer.dataobj.VoroPPData.get(pos);
       //double[] centerD=(double[])singleBox.get(1);
       //String str_xyz=Double.toString(centerD[0])+";"+Double.toString(centerD[1])+";"+Double.toString(centerD[2]);
       //jLabel1.setText("Block:"+str_id+";"+str_xyz);
   }
           
    private Canvas3D createUniverse() {
	GraphicsConfiguration config =
	    SimpleUniverse.getPreferredConfiguration();

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
    
    public void Repaint()
    {
        
    }

 /**
     * Behavior class that waits for a behavior post from the AWT event handler
     */
    class RotateBehavior extends Behavior {

        private TransformGroup transformGroup;
        private Transform3D trans = new Transform3D();
        private WakeupCriterion criterion;
        private float angle = 0.0f;

        private final int ROTATE = 1;

        // create a new RotateBehavior
        RotateBehavior(TransformGroup tg) {
            transformGroup = tg;
        }

        // initialize behavior to wakeup on a behavior post with id = ROTATE
        public void initialize() {
            criterion = new WakeupOnBehaviorPost(this, ROTATE);
            wakeupOn(criterion);
        }

        // processStimulus to rotate the cube
        public void processStimulus(Enumeration criteria) {
            angle += Math.toRadians(10.0);
            trans.rotY(angle);
            transformGroup.setTransform(trans);
            wakeupOn(criterion);
        }

        // when the mouse is clicked, postId for the behavior
        void rotateY() {
            postId(ROTATE);
        }
    }

    // ----------------------------------------------------------------
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        guiPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        rotateButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboAction = new javax.swing.JComboBox();
        drawingPanel = new javax.swing.JPanel();
        jPanelButton = new javax.swing.JPanel();
        jButtonSetBound = new javax.swing.JButton();
        jButtonAddPoints = new javax.swing.JButton();
        jButtonAddSurfaces = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jButtonOptions = new javax.swing.JButton();
        jButtonGenerateMesh3D = new javax.swing.JButton();
        jButtonTestAddSphere = new javax.swing.JButton();
        jButtonTestRemove = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MeshGenerator3D");

        guiPanel.setLayout(new java.awt.GridLayout(1, 0));

        jButton1.setText("RotateX");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        guiPanel.add(jButton1);

        rotateButton.setText("RotateY");
        rotateButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rotateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateButtonActionPerformed(evt);
            }
        });
        guiPanel.add(rotateButton);

        jButton2.setText("RotateZ");
        guiPanel.add(jButton2);

        jComboAction.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "SetROI", "Item 3", "Item 4" }));
        jComboAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboActionActionPerformed(evt);
            }
        });
        guiPanel.add(jComboAction);

        drawingPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        drawingPanel.setLayout(new java.awt.BorderLayout());

        jPanelButton.setLayout(new java.awt.GridLayout(8, 1, 0, 1));

        jButtonSetBound.setText("Set Bound");
        jButtonSetBound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSetBoundActionPerformed(evt);
            }
        });
        jPanelButton.add(jButtonSetBound);

        jButtonAddPoints.setText("Add points");
        jButtonAddPoints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPointsActionPerformed(evt);
            }
        });
        jPanelButton.add(jButtonAddPoints);

        jButtonAddSurfaces.setText("Add Surfaces");
        jButtonAddSurfaces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddSurfacesActionPerformed(evt);
            }
        });
        jPanelButton.add(jButtonAddSurfaces);

        jButtonReset.setText("Reset");
        jPanelButton.add(jButtonReset);

        jButtonOptions.setText("Options");
        jButtonOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsActionPerformed(evt);
            }
        });
        jPanelButton.add(jButtonOptions);

        jButtonGenerateMesh3D.setText("GenerateMesh3D");
        jButtonGenerateMesh3D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateMesh3DActionPerformed(evt);
            }
        });
        jPanelButton.add(jButtonGenerateMesh3D);

        jButtonTestAddSphere.setText("Test(add something)");
        jButtonTestAddSphere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTestAddSphereActionPerformed(evt);
            }
        });
        jPanelButton.add(jButtonTestAddSphere);

        jButtonTestRemove.setText("RemoveSomething");
        jButtonTestRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTestRemoveActionPerformed(evt);
            }
        });
        jPanelButton.add(jButtonTestRemove);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("3DRoot");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Bound");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Points");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Set1");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Set2");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Set3l");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Surfaces");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Surf1");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("surf2i");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Surf3");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(jTree1);

        jLabel1.setText("Block:");
        jPanel1.add(jLabel1);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(guiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(guiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(drawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addComponent(jPanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rotateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateButtonActionPerformed
        awtBehavior.rotateY();
    }//GEN-LAST:event_rotateButtonActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jComboActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboActionActionPerformed
        //if(jComboAction)
        if(SET_VOIWindow==null)
                    {
                        SET_VOIWindow = new SetVoi(); 
                    }
                
        SET_VOIWindow.setVisible(true);
        Repaint();
    }//GEN-LAST:event_jComboActionActionPerformed

    private void jButtonAddSurfacesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddSurfacesActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButtonAddSurfacesActionPerformed

    private void jButtonSetBoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetBoundActionPerformed
       //set the bounds..
        SetBounds SetBoundsDialog=new SetBounds(this,true);
        SetBoundsDialog.setVisible(true);
        if(SetBoundsDialog.returnStatus==RET_OK)
        {
            float lx=xmax-xmin;
            float ly=ymax-ymin;
            float lz=zmax-zmin;
            float lmax=lx;
            if(lmax<=ly)
            {
                lmax=ly;
            }
            if(lmax<=lz)
            {
                lmax=lz;
            }
            scalefactor=1.0f/lmax;
            objTrans.removeChild(BoundingBoxBranchGroup);
            BoundingBoxBranchGroup=new BranchGroup();
            BoundingBoxBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
            BoundingBoxBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
            BoundingBoxBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
            Transform3D BoxMat = new Transform3D();
            BoxMat.set(new Vector3d(0.0f,0.0f,0.0f));
            TransformGroup BoxTrans = new TransformGroup(BoxMat);
            BoundingBox=new BoundingBox3D(xmin,ymin,zmin,xmax,ymax,zmax,scalefactor);
            BoxTrans.addChild(BoundingBox);
            BoundingBoxBranchGroup.addChild(BoxTrans);
            objTrans.addChild(BoundingBoxBranchGroup);
        
        }
    }//GEN-LAST:event_jButtonSetBoundActionPerformed

    private void jButtonOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonOptionsActionPerformed

    private void jButtonGenerateMesh3DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateMesh3DActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonGenerateMesh3DActionPerformed

    private void jButtonTestAddSphereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTestAddSphereActionPerformed
    BranchGroup boxBranch = new BranchGroup();  
    boxBranch.setCapability(BranchGroup.ALLOW_DETACH);  
     
    //live scene ...  
       
    Transform3D BoxMat = new Transform3D();
    double x=Math.random();
    double y=Math.random();
    double z=Math.random();
    
    BoxMat.set(new Vector3d(x,y,z));
    TransformGroup    BoxTrans = new TransformGroup(BoxMat);
    BoxTrans.addChild(new ColorCube(0.1f));
    boxBranch.addChild(BoxTrans);
    surfacearray.add(boxBranch);
    objTrans.addChild(boxBranch); 
    }//GEN-LAST:event_jButtonTestAddSphereActionPerformed

    private void jButtonTestRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTestRemoveActionPerformed
        int i=surfacearray.size();
        Enumeration pippo=objTrans.getAllChildren();
        
        if(i>0){
            BranchGroup tmp=(BranchGroup)surfacearray.get(10);
            BranchGroup tmp1=new BranchGroup();
            objTrans.removeChild(tmp);
            surfacearray.remove(10);
        }
        
    }//GEN-LAST:event_jButtonTestRemoveActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonAddPointsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPointsActionPerformed
       final JFileChooser fc =new JFileChooser(FilePathString);
        fc.setDialogTitle("Open points file");
        int returnVal =fc.showOpenDialog(MeshGenerator3D.this);

        if (returnVal==JFileChooser.APPROVE_OPTION)
        {//start IF
            File file=fc.getSelectedFile();
            String fileName=file.getName();
            //iTough2Viewer.dataobj.set_DataFileName(fileName);
            String FilePath=file.getAbsolutePath();
            
            FilePathString=FilePath;
           
            if(read_in(FilePath))
            {
                add_points_to_virtual_scene();
            }
           
           
        }//END IF

    }//GEN-LAST:event_jButtonAddPointsActionPerformed
    private boolean read_in(String fileName)
    {
        
        TotalLinePoints=new ArrayList();
        FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        String linea;
    
        try
        {
            fis= new FileInputStream(fileName);
            bis= new BufferedInputStream(fis);
            dis=new DataInputStream(bis);
            linea=(String) dis.readLine();
            boolean end_elem=true;
            int nxyz_temp=0;
            
            while (dis.available()!=0)
            {
                //skip first line
                //linea=(String) dis.readLine();
                //
                   
                    linea=(String) dis.readLine();
                    if(linea.contains("#"))continue;
                    TotalLinePoints.add(linea);
                   
                
            }
        }
        catch(FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
    private boolean add_points_to_virtual_scene()
    {
        BranchGroup boxBranch = new BranchGroup();  
        boxBranch.setCapability(BranchGroup.ALLOW_DETACH);  
     
        for(int i=0;i<TotalLinePoints.size();i++)
        {
            String singolLine=(String)TotalLinePoints.get(i);
            String[] singol_line_parsed=JoeStringUtils1.parseSpace(singolLine);
            Transform3D BoxMat = new Transform3D();
            double x=xmin_v+Double.parseDouble(singol_line_parsed[1])*scalefactor;
            double y=ymin_v+Double.parseDouble(singol_line_parsed[2])*scalefactor;
            double z=zmin_v+Double.parseDouble(singol_line_parsed[3])*scalefactor;
    
            BoxMat.set(new Vector3d(x,y,z));
            TransformGroup    BoxTrans = new TransformGroup(BoxMat);
            BoxTrans.addChild(new ColorCube(0.001f));
            boxBranch.addChild(BoxTrans);
        
        }
        surfacearray.add(boxBranch);
        objTrans.addChild(boxBranch); 
        
        return true;
    }
        /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new VoroPlusPlus3D(new ArrayList(),0.0f,0.0f,0.0f,0.0f,0.0f,new Color3f()).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel drawingPanel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel guiPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAddPoints;
    private javax.swing.JButton jButtonAddSurfaces;
    private javax.swing.JButton jButtonGenerateMesh3D;
    private javax.swing.JButton jButtonOptions;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JButton jButtonSetBound;
    private javax.swing.JButton jButtonTestAddSphere;
    private javax.swing.JButton jButtonTestRemove;
    private javax.swing.JComboBox jComboAction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton rotateButton;
    // End of variables declaration//GEN-END:variables
    
}
