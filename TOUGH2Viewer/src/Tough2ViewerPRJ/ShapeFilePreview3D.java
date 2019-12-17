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
import com.sun.j3d.utils.universe.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.shp.ShapefileReader;

/**
 * Simple Java 3D test program created in NetBeans to illustrate interacting
 * with a Java 3D scene graph from an Swing-based program.
 */
public class ShapeFilePreview3D extends javax.swing.JFrame {

    private SimpleUniverse univ = null;
    private BranchGroup scene = null;

    private TransformGroup objTrans;
    private RotateBehavior awtBehavior;
    File myLocalFile;
    private int totalshape = 0;
    com.vividsolutions.jts.geom.Geometry[] shape;
    ArrayList newshape;
    int zRow;
    double[] table;
    boolean use_z_shape;
    Canvas3D ModelCanvas3D;
    ViewingPlatform ModelViewingPlatform;

    public ShapeFilePreview3D(File file, int index, boolean use_z) {
        // Initialize the GUI components
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initComponents();
        myLocalFile = file;
        zRow = index;
        use_z_shape = use_z;
        try {
            loadAndSaveShapeFileSHP();
        } catch (Exception e) {

        }
        this.setTitle("Shape file preview 3D");
        // Create Canvas3D and SimpleUniverse; add canvas to drawing panel
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
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRoot.addChild(objTrans);

        // Create a simple shape leaf node, add it to the scene graph.
        //objTrans.addChild(new ColorCube(0.1));
        //qui aggiungo la mia shape.........
        float x_min = Tough2Viewer.dataobj.get_xmin();
        float y_min = Tough2Viewer.dataobj.get_ymin();
        float z_min = Tough2Viewer.dataobj.get_zmin();
        float riduzione = 1.0f;
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
        if (lmax == 0) {
            String error = "L max=0. Check IN file or MESH file";
            JOptionPane.showMessageDialog(null, error);

        }
        riduzione = 1.0f / lmax;
        float xmin, xmax, ymin, ymax, zmin, zmax;
        double Zfactor = Tough2Viewer.dataobj.get_Zfactor();
        float campo = 1.0f;
        xmin = -0.5f * lx / lmax;
        xmax = 0.5f * lx / lmax;
        ymin = -0.5f * ly / lmax;
        ymax = 0.5f * ly / lmax;
        zmin = -0.5f * lz / lmax * (float) Zfactor;
        zmax = 0.5f * lz / lmax * (float) Zfactor;
        Color3f mycolor = new Color3f(1.0f, 1.0f, 1.0f);
        MultiplePolyLine3D PolyShape3D = new MultiplePolyLine3D(newshape, x_min, y_min, z_min, riduzione, xmin, ymin, zmin, mycolor, Zfactor);
        PolyShape3D.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        PolyShape3D.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        objTrans.addChild(PolyShape3D);

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

        return objRoot;
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
    public String loadAndSaveShapeFileSHP() throws IOException {
        newshape = new ArrayList();
        String shapename = myLocalFile.getName();
        if (myLocalFile != null) {
            int num_shapes = 0;
            ShpFiles in2 = new ShpFiles(myLocalFile);

            DbaseFileReader rDbf = new DbaseFileReader(in2, false, ShapefileDataStore.DEFAULT_STRING_CHARSET);
            Object[] fields = new Object[rDbf.getHeader().getNumFields()];
            DbaseFileHeader myHeader = rDbf.getHeader();

            //int numfields=myHeader.getNumFields();
            num_shapes = myHeader.getNumRecords();
            totalshape = num_shapes;
            table = new double[num_shapes];
            int row = 0;
            while (rDbf.hasNext()) {
                rDbf.readEntry(fields);
                table[row] = Double.parseDouble(fields[zRow].toString());
                row++;
                // do stuff
            }
            rDbf.close();

            GeometryFactory gf = new GeometryFactory();

            ShapefileReader r = new ShapefileReader(in2, true, true, gf);
            int shapes = 0;
            shape = new com.vividsolutions.jts.geom.Geometry[totalshape];
            while (r.hasNext()) {
                //String myString=r.toString();
                shape[shapes] = (com.vividsolutions.jts.geom.Geometry) r.nextRecord().shape();
                //innt numgeom=shape[shapes].getNumGeometries();
                //int npoint=shape[shapes].getNumPoints();
                Coordinate[] myCord = shape[shapes].getCoordinates();
                int records = myCord.length;
                Point3f[] myPoint = new Point3f[records];
                for (int i = 0; i < records; i++) {
                    myPoint[i] = new Point3f();
                    myPoint[i].x = (float) myCord[i].x;
                    myPoint[i].y = (float) myCord[i].y;
                    if (use_z_shape) {
                        myPoint[i].z = (float) myCord[i].z;
                    } else {
                        myPoint[i].z = (float) table[shapes];
                    }

                }
                newshape.add(myPoint);
                shapes++;
            }
            r.close();

        }

        return shapename;
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

        guiPanel = new javax.swing.JPanel();
        rotateButton = new javax.swing.JButton();
        drawingPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Swing Interaction Test");

        guiPanel.setLayout(new java.awt.GridBagLayout());

        rotateButton.setText("Rotate");
        rotateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        guiPanel.add(rotateButton, gridBagConstraints);

        getContentPane().add(guiPanel, java.awt.BorderLayout.NORTH);

        drawingPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        drawingPanel.setLayout(new java.awt.BorderLayout());
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

    private void rotateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateButtonActionPerformed
        awtBehavior.rotate();
    }//GEN-LAST:event_rotateButtonActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShapeFilePreview3D(null, 0, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel drawingPanel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel guiPanel;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JButton rotateButton;
    // End of variables declaration//GEN-END:variables

}
