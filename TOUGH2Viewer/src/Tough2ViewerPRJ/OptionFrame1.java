/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.vecmath.Color3f;

/**
 *
 * @author stebond
 */
public class OptionFrame1 extends javax.swing.JFrame {

    String metal = "Metal";
    String metalClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
    String motif = "Motif";
    String motifClassName
            = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    String windows = "Windows";
    String windowsClassName
            = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    String[] LAF = new String[3];
    String[] LAFClassName = new String[3];

    /**
     * Creates new form OptionFrame1
     */
    public OptionFrame1() {
        LAF[0] = windows;
        LAF[1] = metal;
        LAF[2] = motif;
        LAFClassName[0] = windowsClassName;
        LAFClassName[1] = metalClassName;
        LAFClassName[2] = motifClassName;
        initComponents();
        int dimension = (int) Tough2Viewer.dataobj.ShapeDimension;
        float thick = Tough2Viewer.dataobj.LineThicness;
        jComboBoxLineThicness.setSelectedIndex((int) thick);
        jComboBoxShapeDimension.setSelectedIndex(dimension);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"HSV240", "JET64", "JET24", "B/W256"}));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getVariableName()));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(LAF));

        preparaTabella1();
        float zmin = Tough2Viewer.dataobj.get_GlobalScale(0, 0);
        float zmax = Tough2Viewer.dataobj.get_GlobalScale(0, 1);
        float campo = Tough2Viewer.dataobj.get_Campo();
        double contrazione = Tough2Viewer.dataobj.get_ShrinkFactor();
        jTextField1.setText(Float.toString(zmin));
        jTextField2.setText(Float.toString(zmax));
        jTextField3.setText(Double.toString(contrazione));
        double Zfactor = 1.0f;
        jTextField4.setText(Double.toString(Zfactor));
        jTextField6.setText(Double.toString(campo));
        //preparaTabella2();
        AggiornaTabella2();
        jTextField5.setText(Double.toString(Tough2Viewer.dataobj.get_exp_factor_estimation()));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.get_ScientificNotationX()));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.get_ScientificNotationY()));
        jTableMaterialType.getModel().addTableModelListener(new InteractiveTableModelListenerMaterial());
        jTable1.getModel().addTableModelListener(new InteractiveTableModelListenerSurface());
        jCheckBox7.setSelected(Tough2Viewer.dataobj.plotFlow[0]);
        jCheckBox10.setSelected(Tough2Viewer.dataobj.plotFlow[1]);
        jCheckBox11.setSelected(Tough2Viewer.dataobj.plotFlow[2]);
        jCheckBox12.setSelected(Tough2Viewer.dataobj.plotFlow[3]);
        jTextField9.setText(Tough2Viewer.dataobj.FormatDouble);

        if (Tough2Viewer.dataobj.light_3D[0]) {
            jCheckBox1.setSelected(true);
        } else {
            jCheckBox1.setSelected(false);
        }
        if (Tough2Viewer.dataobj.light_3D[1]) {
            jCheckBox2.setSelected(true);
        } else {
            jCheckBox2.setSelected(false);
        }
        if (Tough2Viewer.dataobj.light_3D[2]) {
            jCheckBoxLGT3.setSelected(true);
        } else {
            jCheckBoxLGT3.setSelected(false);
        }
        if (Tough2Viewer.dataobj.light_3D[3]) {
            jCheckBox3.setSelected(true);
        } else {
            jCheckBox3.setSelected(false);
        }

    }
    //my code

    public class InteractiveTableModelListenerMaterial implements TableModelListener {

        public void tableChanged(TableModelEvent e)//one clas for two tables...
        {
            Object target = e.getSource();
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 2) {
                TableModel model = (TableModel) e.getSource();
                Object data = model.getValueAt(row, column);
                String temp = data.toString();
                boolean newvalue = Boolean.parseBoolean(temp);
                Tough2Viewer.dataobj.set_rocktype_visible(row, newvalue);
                // Do something with the data...
                if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
                    Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();
                }

            }

        }

    }

    public class InteractiveTableModelListenerSurface implements TableModelListener {

        public void tableChanged(TableModelEvent e)//one clas for two tables...
        {
            Object target = e.getSource();
            int row = e.getFirstRow();
            int column = e.getColumn();
            TableModel model = (TableModel) e.getSource();

            if (column == 2)//la colonna delle poly lines
            {
                Object data = model.getValueAt(row, column);
                String temp = data.toString();
                boolean newvalue = Boolean.parseBoolean(temp);
                //iTough2Viewer.dataobj.set_PolyShapesVisible(row, newvalue);
                Tough2Viewer.dataobj.set_PolyShapesVisible2(row, newvalue);
                // Do something with the data...
                if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
                    Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_shapes2();
                }
            }
            if (column == 3) {//colonna surfaces
                Object data = model.getValueAt(row, column);
                String temp = data.toString();
                boolean newvalue = Boolean.parseBoolean(temp);

                //iTough2Viewer.dataobj.set_SurfaceShapesVisible(row, newvalue);
                Tough2Viewer.dataobj.set_SurfaceShapesVisible2(row, newvalue);
                if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
                    Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_shapes2();
                }
            }

        }

    }

    private void coloracolonna1Tabella2() {
        jTable1.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Color3f colore[] = Tough2Viewer.dataobj.get_ShapesColor2();
                Color c;
                if (colore.length > row) {
                    c = new Color(colore[row].x, colore[row].y, colore[row].z);
                } else {
                    c = new Color(1.0f, 1.0f, 1.0f);
                }

                if (row < 0) {
                    cell.setBackground(c);
                } else {
                    cell.setBackground(c);
                }
                return cell;

            }
        });
    }

    void coloracolonna1() {
        jTableMaterialType.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Color3f colore[] = Tough2Viewer.dataobj.getColo3fRocksTypes();
                Color c = new Color(colore[row].x, colore[row].y, colore[row].z);
                if (row < 0) {
                    cell.setBackground(c);
                } else {
                    cell.setBackground(c);
                }
                return cell;

            }
        });
    }

    public void preparaTabella1() {
        int n_righe = jTableMaterialType.getRowCount();
        int n_colonne = jTableMaterialType.getColumnCount();
        String[] titoli = new String[]{"Material", "Color", "Visible"};
        DefaultTableModel model;
        if (n_colonne < titoli.length) {

            // Ensure that auto-create is off
            if (jTableMaterialType.getAutoCreateColumnsFromModel()) {
                jTableMaterialType.setAutoCreateColumnsFromModel(false);
            }
        }
        for (int vColIndex = 0; vColIndex < titoli.length; vColIndex++) {
            jTableMaterialType.getColumnModel().getColumn(vColIndex).setHeaderValue(titoli[vColIndex]);
        }
        ///////////////////Riempire prima colonna
        ArrayList rocksnames = Tough2Viewer.dataobj.get_rocknames();
        int tipirocce = rocksnames.size();
        if (n_righe < tipirocce + 1) {

            model = (DefaultTableModel) jTableMaterialType.getModel();
            int delta = tipirocce - n_righe;
            for (int vRowIndex = 0; vRowIndex < delta; vRowIndex++) {
                model.addRow(new Object[]{"v1"});
            }
        }
        n_righe = jTableMaterialType.getRowCount();

        Boolean[] rock_visible = new Boolean[n_righe];
        for (int vRowIndex = 0; vRowIndex < n_righe; vRowIndex++) {
            rock_visible[vRowIndex] = Tough2Viewer.dataobj.is_rocktype_visible(vRowIndex);
        }
        if (jTableMaterialType.getAutoCreateColumnsFromModel()) {
            jTableMaterialType.setAutoCreateColumnsFromModel(false);
        }
        for (int vRowIndex = 0; vRowIndex < n_righe; vRowIndex++) {
            String tiporoccia = rocksnames.get(vRowIndex).toString();
            jTableMaterialType.setValueAt(tiporoccia, vRowIndex, 0);
            jTableMaterialType.setValueAt(rock_visible[vRowIndex], vRowIndex, 2);
        }
        /////////Riempire seconda colonna
        coloracolonna1();
        //////////Aggiungere terza colonna
    }

    public void AggiornaTabella2() {
        String[] ShapeFileName = Tough2Viewer.dataobj.get_shapeFileNames2();
        int n_righe = jTable1.getRowCount();
        int n_colonne = jTable1.getColumnCount();
        String[] titoli = new String[]{
            "Shapefile", "Color", "Contour line visible", "Surface visible"
        };
        DefaultTableModel model;
        if (n_colonne < titoli.length) {

            // Ensure that auto-create is off
            if (jTable1.getAutoCreateColumnsFromModel()) {
                jTable1.setAutoCreateColumnsFromModel(false);
            }
        }
        for (int vColIndex = 0; vColIndex < titoli.length; vColIndex++) {
            jTable1.getColumnModel().getColumn(vColIndex).setHeaderValue(titoli[vColIndex]);
        }
        ///////////////////Riempire prima colonna

        int numerofile = ShapeFileName.length;
        if (n_righe < numerofile) {
            model = (DefaultTableModel) jTable1.getModel();
            int delta = numerofile - n_righe;
            for (int vRowIndex = 0; vRowIndex < delta; vRowIndex++) {
                model.addRow(new Object[]{"v1"});
            }
        }
        boolean[] Poly_shape_visible = Tough2Viewer.dataobj.get_PolyShapesVisible2();
        boolean[] Surface_shape_visible = Tough2Viewer.dataobj.get_SurfaceShapesVisible2();
        if (jTable1.getAutoCreateColumnsFromModel()) {
            jTable1.setAutoCreateColumnsFromModel(false);
        }
        for (int vRowIndex = 0; vRowIndex < ShapeFileName.length; vRowIndex++) {
            String shapefilename = ShapeFileName[vRowIndex];
            jTable1.setValueAt(shapefilename, vRowIndex, 0);
            jTable1.setValueAt(Poly_shape_visible[vRowIndex], vRowIndex, 2);
            jTable1.setValueAt(Surface_shape_visible[vRowIndex], vRowIndex, 3);
        }

        coloracolonna1Tabella2();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jCheckBox4 = new javax.swing.JCheckBox();
        jButton6 = new javax.swing.JButton();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox17 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBoxLGT3 = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButtonReduceBlockFactor = new javax.swing.JButton();
        jButtonZStretchFactor = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButtonCampo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMaterialType = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jTextField7 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jTextField8 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jTextField10 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jComboBox6 = new javax.swing.JComboBox();
        jPanel10 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jTextField9 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxLineThicness = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxShapeDimension = new javax.swing.JComboBox();
        jButton14 = new javax.swing.JButton();
        jComboBox9 = new javax.swing.JComboBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jButtonColorGridAxis = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBoxGridThickness = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jComboBox8 = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Options");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Scale Value"));

        jLabel1.setText("ColorMap");

        jLabel5.setText("Variable");

        jLabel6.setText("min");

        jLabel7.setText("max");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextField1.setText("jTextField1");
        jTextField1.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextField1.setPreferredSize(new java.awt.Dimension(60, 20));

        jTextField2.setText("jTextField2");
        jTextField2.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextField2.setPreferredSize(new java.awt.Dimension(60, 20));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton2.setText("Reset");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Reset all");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jCheckBox4.setText("Hide out of range");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jButton6.setText("Set new value");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jCheckBox14.setText("Use logaritmic scale");
        jCheckBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox14ActionPerformed(evt);
            }
        });

        jCheckBox17.setText("Reverse scale");
        jCheckBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1, 0, 87, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jCheckBox14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox17))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addContainerGap(172, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox14)
                    .addComponent(jCheckBox17))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jCheckBox4)
                .addContainerGap(312, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Visualization Filter", jPanel2);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Appearance"));

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Light one");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Light two");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Ambient light");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jButton1.setText("Choose Background");
        jButton1.setToolTipText("Choose Background Color");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox13.setSelected(true);
        jCheckBox13.setText("Axis");
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });

        jCheckBoxLGT3.setSelected(true);
        jCheckBoxLGT3.setText("Light three");
        jCheckBoxLGT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLGT3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addComponent(jCheckBox13))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBoxLGT3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jCheckBox3))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox2)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBoxLGT3)))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Block Stretch"));

        jLabel2.setText("Reduce Block Factor (0.5-1.0)");

        jLabel3.setText("Z Stretch Factor");

        jTextField3.setText("jTextField3");

        jTextField4.setText("jTextField4");

        jButtonReduceBlockFactor.setText("Apply");
        jButtonReduceBlockFactor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReduceBlockFactorActionPerformed(evt);
            }
        });

        jButtonZStretchFactor.setText("Apply");
        jButtonZStretchFactor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZStretchFactorActionPerformed(evt);
            }
        });

        jLabel16.setText("Zoom factor");

        jTextField6.setText("jTextField6");

        jButtonCampo.setText("Apply");
        jButtonCampo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCampoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonReduceBlockFactor, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonZStretchFactor, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonCampo, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextField3, jTextField4, jTextField6});

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonCampo, jButtonReduceBlockFactor, jButtonZStretchFactor});

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonReduceBlockFactor)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonZStretchFactor)
                            .addComponent(jLabel3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCampo)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableMaterialType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Material Type", "Title 2", "Title 3"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMaterialType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMaterialTypeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableMaterialType);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Shapefile", "Contour line Color", "Contour line Visible", "Surface visible"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jButton8.setText("Print");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton8)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jTabbedPane1.addTab("3D Block model Object", jPanel3);

        jCheckBox5.setSelected(true);
        jCheckBox5.setText("Normalize vector");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("2DPlot"));

        jCheckBox7.setText("CompX");
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });

        jCheckBox10.setText("CompY");
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });

        jCheckBox11.setText("CompZ");
        jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox11ActionPerformed(evt);
            }
        });

        jCheckBox12.setText("Module");
        jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox10)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox11)
                    .addComponent(jCheckBox12))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jCheckBox7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox12)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField7.setText("0.1");

        jLabel17.setText("Vector lenght");

        jButton10.setText("Apply");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jCheckBox5)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addGap(6, 6, 6)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10))
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(333, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox5)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jButton10))
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(289, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("3D Flow model", jPanel4);

        jLabel4.setText("Exp. for inverse distance est.");

        jTextField5.setText("jTextField5");

        jButton4.setText("Apply");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "hipercube", "bruteforce" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel10.setText("SearchMethod");

        jCheckBox6.setText("IsoSurface Perspective");
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        jCheckBox15.setSelected(true);
        jCheckBox15.setText("Use MultiProcessors");
        jCheckBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox15ActionPerformed(evt);
            }
        });

        jTextField8.setText("2");
        jTextField8.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel18.setText("MultiSelectionLevel");

        jButton11.setText("Apply");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton15.setText("Selection color");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jTextField10.setText("jTextField10");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox6)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jCheckBox15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jButton15)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(297, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jButton11))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(297, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Misc.", jPanel5);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Choose skins"));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Application Font"));

        jButton5.setText("Choose Font...");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Scale font"));

        jButton12.setText("Choose Font...");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jTextField9.setText("jTextField9");

        jLabel19.setText("Format string");

        jButton13.setText("Apply");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jButton12))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jButton13))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(408, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Skins", jPanel8);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Plot Styles"));

        jCheckBox8.setSelected(true);
        jCheckBox8.setText("X axis with scientific notation");
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });

        jCheckBox9.setSelected(true);
        jCheckBox9.setText("Yaxis with scientific notation");
        jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox9ActionPerformed(evt);
            }
        });

        jLabel8.setText("Precision X");

        jLabel9.setText("Precision Y");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.0E00", "0.00E00", "0.000E00", "0.0000E00", "0.00000E00" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.0E00", "0.00E00", "0.000E00", "0.0000E00", "0.00000E00" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setText("Line Thickness");

        jComboBoxLineThicness.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" }));
        jComboBoxLineThicness.setSelectedIndex(1);
        jComboBoxLineThicness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLineThicnessActionPerformed(evt);
            }
        });

        jLabel12.setText("Symbol Dimension");

        jComboBoxShapeDimension.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "12", "14", "20" }));
        jComboBoxShapeDimension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxShapeDimensionActionPerformed(evt);
            }
        });

        jButton14.setText("Line Color ...");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circle", "Square" }));
        jComboBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });

        jCheckBox16.setSelected(true);
        jCheckBox16.setText("unfilled shaped");
        jCheckBox16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxShapeDimension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox16))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxLineThicness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxLineThicness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jButton14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxShapeDimension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox16))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonColorGridAxis.setText("Grid Color...");
        jButtonColorGridAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonColorGridAxisActionPerformed(evt);
            }
        });

        jLabel14.setText("Stretch Grid dash");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jComboBoxGridThickness.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" }));
        jComboBoxGridThickness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGridThicknessActionPerformed(evt);
            }
        });

        jLabel13.setText("Grid Thickness");

        jButton7.setText("Plot Area color ...");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", " " }));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jLabel15.setText("AxisOffset");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxGridThickness, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButtonColorGridAxis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(17, 17, 17))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxGridThickness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonColorGridAxis)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox9)
                            .addComponent(jCheckBox8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox3, 0, 87, Short.MAX_VALUE)
                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)))
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(317, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox8)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox9)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jTabbedPane1.addTab("Plot Format", jPanel1);

        jButton9.setText("Close");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton9)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(496, 496, 496)
                .addComponent(jButton9)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        Tough2Viewer.dataobj.light_3D[1] = jCheckBox2.isSelected();
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.set_light(jCheckBox2.isSelected(), 1);
        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        float r, g, b;
        Color c = JColorChooser.showDialog(this, "Choose a color...", getBackground());
        if (c != null) {
            r = (float) c.getRed();
            g = (float) c.getGreen();
            b = (float) c.getBlue();
            r = r / 255.0f;
            g = g / 255.0f;
            b = b / 255.0f;
            Tough2Viewer.dataobj.bgColor.x = r;
            Tough2Viewer.dataobj.bgColor.y = g;
            Tough2Viewer.dataobj.bgColor.z = b;
        }
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.setbackground();
        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        Tough2Viewer.dataobj.light_3D[0] = jCheckBox1.isSelected();
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.set_light(jCheckBox1.isSelected(), 0);
        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int a = jComboBox2.getSelectedIndex();
        float zmin = Tough2Viewer.dataobj.get_globalStatistics(a, 0);
        float zmax = Tough2Viewer.dataobj.get_globalStatistics(a, 1);
        Tough2Viewer.dataobj.set_GlobalScale(a, 0, zmin);
        Tough2Viewer.dataobj.set_GlobalScale(a, 1, zmax);
        jTextField1.setText(Float.toString(zmin));
        jTextField2.setText(Float.toString(zmax));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8ActionPerformed
        Tough2Viewer.dataobj.set_UseScientificNotationX(jCheckBox8.isSelected());
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jCheckBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox9ActionPerformed
        Tough2Viewer.dataobj.set_UseScientificNotationY(jCheckBox9.isSelected());
    }//GEN-LAST:event_jCheckBox9ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        Tough2Viewer.dataobj.set_SciNotX(jComboBox3.getSelectedIndex());
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        Tough2Viewer.dataobj.set_SciNotY(jComboBox4.getSelectedIndex());
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        int a = jComboBox2.getSelectedIndex();
        float zmin = Tough2Viewer.dataobj.get_GlobalScale(a, 0);
        float zmax = Tough2Viewer.dataobj.get_GlobalScale(a, 1);
        jTextField1.setText(Float.toString(zmin));
        jTextField2.setText(Float.toString(zmax));
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        float zmin = Float.parseFloat((String) jTextField1.getText());
        float zmax = Float.parseFloat((String) jTextField2.getText());
        int a = jComboBox2.getSelectedIndex();
        Tough2Viewer.dataobj.set_GlobalScale(a, 0, zmin);
        Tough2Viewer.dataobj.set_GlobalScale(a, 1, zmax);
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Tough2Viewer.dataobj.CreateGlobalScale();
        int a = jComboBox2.getSelectedIndex();
        float zmin = Tough2Viewer.dataobj.get_GlobalScale(a, 0);
        float zmax = Tough2Viewer.dataobj.get_GlobalScale(a, 1);
        jTextField1.setText(Float.toString(zmin));
        jTextField2.setText(Float.toString(zmax));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        Tough2Viewer.dataobj.set_hideoutofrange(jCheckBox4.isSelected());
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();
        }
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        Tough2Viewer.dataobj.set_normalizeVector(jCheckBox5.isSelected());
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        double a = Double.parseDouble(jTextField5.getText());
        Tough2Viewer.dataobj.set_exp_factor_estimation(a);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButtonReduceBlockFactorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReduceBlockFactorActionPerformed
        double contrazioneNew = Double.parseDouble((String) jTextField3.getText());
        double contrazioneOld = Tough2Viewer.dataobj.get_ShrinkFactor();
        Tough2Viewer.dataobj.set_ContrazioneFactor(contrazioneNew);
        double f = contrazioneNew / contrazioneOld;
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.update_model_scene();
        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();

    }//GEN-LAST:event_jButtonReduceBlockFactorActionPerformed

    private void jTableMaterialTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMaterialTypeMouseClicked
        int row = jTableMaterialType.getSelectedRow();
        int col = jTableMaterialType.getSelectedColumn();
        if (col == 1) {
            Color c = JColorChooser.showDialog(this, "Choose a color...", getBackground());
            if (c != null) {
                float r = (float) c.getRed();
                float g = (float) c.getGreen();
                float b = (float) c.getBlue();
                r = r / 255.0f;
                g = g / 255.0f;
                b = b / 255.0f;
                Tough2Viewer.dataobj.setColorRockTypes(row, r, g, b);
                int i = 1;
            }
            coloracolonna1();
            if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
                Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();
            }

        }
    }//GEN-LAST:event_jTableMaterialTypeMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int row = jTable1.getSelectedRow();
        int col = jTable1.getSelectedColumn();

        if (col == 1) {
            Color c = JColorChooser.showDialog(this, "Choose a color...", getBackground());
            if (c != null) {
                float r = (float) c.getRed();
                float g = (float) c.getGreen();
                float b = (float) c.getBlue();
                r = r / 255.0f;
                g = g / 255.0f;
                b = b / 255.0f;
                //iTough2Viewer.dataobj.set_ColorPolyShape(row, r, g, b);
                Tough2Viewer.dataobj.set_ColorPolyShape2(row, r, g, b);
                Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_shapes2();
                int i = 1111;
            }
            coloracolonna1Tabella2();
        }
        if (col == 2) {
//            Object data = model.getValueAt(row, column);
//            String temp=data.toString();
//            boolean newvalue=Boolean.parseBoolean(temp);

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButtonZStretchFactorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZStretchFactorActionPerformed
        double Zfactor = Double.parseDouble((String) jTextField4.getText());
        double Zfactor_old = Tough2Viewer.dataobj.get_Zfactor();
        double Z_f = Zfactor / Zfactor_old;
        Tough2Viewer.dataobj.set_Zfactor(Zfactor);
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.update_model_scene();
            // Tough2Viewer.tough2viewerGUI.ReBuild3DVoronoi();(not working)
        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();
    }//GEN-LAST:event_jButtonZStretchFactorActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        int a = jComboBox1.getSelectedIndex();
        Tough2Viewer.dataobj.set_scalasceta(a);
        Tough2Viewer.dataobj.CreateColorArray3f();
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.update_scale_scene();//questa e' nuova nuova21/12/11
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            jTableMaterialType.print();
        } catch (Exception e) {

        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        Tough2Viewer.dataobj.searchmethod = jComboBox5.getSelectedIndex();
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        Tough2Viewer.dataobj.isosurfacePerspective = jCheckBox6.isSelected();
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Font f = FontChooser.showDialog(this, "FontChooser", new Font("Dialog", 0, 12));
        if (f != null) {
            FontUIResource test = new FontUIResource(f);
            setUIFont(test);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        int n = jComboBox6.getSelectedIndex();
        try {
            javax.swing.UIManager.setLookAndFeel(LAFClassName[n]);
            SwingUtilities.updateComponentTreeUI(Tough2Viewer.tough2viewerGUI);
            Tough2Viewer.tough2viewerGUI.pack();
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
            if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
                SwingUtilities.updateComponentTreeUI(Tough2Viewer.tough2viewerGUI.VoronoiModel3D);
                Tough2Viewer.tough2viewerGUI.VoronoiModel3D.pack();
            }
            if (Tough2Viewer.tough2viewerGUI.FluxModel3DWindowArrow != null) {
                SwingUtilities.updateComponentTreeUI(Tough2Viewer.tough2viewerGUI.FluxModel3DWindowArrow);
                Tough2Viewer.tough2viewerGUI.FluxModel3DWindowArrow.pack();
            }
            if (Tough2Viewer.tough2viewerGUI.getStatisticWindow != null) {
                SwingUtilities.updateComponentTreeUI(Tough2Viewer.tough2viewerGUI.getStatisticWindow);
                Tough2Viewer.tough2viewerGUI.getStatisticWindow.pack();
            }

        } catch (Exception e) {

        }

    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBoxLineThicnessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLineThicnessActionPerformed
        float thick = (float) jComboBoxLineThicness.getSelectedIndex();
        Tough2Viewer.dataobj.LineThicness = thick;
    }//GEN-LAST:event_jComboBoxLineThicnessActionPerformed

    private void jComboBoxShapeDimensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxShapeDimensionActionPerformed
        int n = jComboBoxShapeDimension.getSelectedIndex();
        Double dimension = (double) 1.0f;
        try {
            dimension = Double.parseDouble((String) jComboBoxShapeDimension.getSelectedItem());
        } catch (Exception e) {

        }

        Tough2Viewer.dataobj.ShapeDimension = dimension;
    }//GEN-LAST:event_jComboBoxShapeDimensionActionPerformed

    private void jComboBoxGridThicknessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGridThicknessActionPerformed
        float thick = (float) jComboBoxGridThickness.getSelectedIndex();
        Tough2Viewer.dataobj.GridAxisThickness = thick;
    }//GEN-LAST:event_jComboBoxGridThicknessActionPerformed

    private void jButtonColorGridAxisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonColorGridAxisActionPerformed
        Color c = JColorChooser.showDialog(OptionFrame1.this, "Choose a color...", getBackground());
        if (c != null) {
            Tough2Viewer.dataobj.GridAxisColor = c;
        }
    }//GEN-LAST:event_jButtonColorGridAxisActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        int n = jComboBox7.getSelectedIndex();
        float dimension = 1.0f;
        try {
            dimension = Float.parseFloat((String) jComboBox7.getSelectedItem());
        } catch (Exception e) {

        }
        Tough2Viewer.dataobj.StretchDashlines = (float) dimension;
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        Color c = JColorChooser.showDialog(OptionFrame1.this, "Choose a color...", getBackground());
        if (c != null) {
            Tough2Viewer.dataobj.PlotAreaColor = c;
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        int n = jComboBox8.getSelectedIndex();
        float dimension = 1.0f;
        try {
            dimension = Float.parseFloat((String) jComboBox8.getSelectedItem());
        } catch (Exception e) {

        }
        Tough2Viewer.dataobj.GridOffSet = (float) dimension;
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        Tough2Viewer.dataobj.light_3D[3] = jCheckBox3.isSelected();
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.set_light(jCheckBox3.isSelected(), 3);
        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();

    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jCheckBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox11ActionPerformed
        Tough2Viewer.dataobj.plotFlow[2] = jCheckBox11.isSelected();
    }//GEN-LAST:event_jCheckBox11ActionPerformed

    private void jCheckBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox12ActionPerformed
        Tough2Viewer.dataobj.plotFlow[3] = jCheckBox12.isSelected();
    }//GEN-LAST:event_jCheckBox12ActionPerformed

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed
        Tough2Viewer.dataobj.plotFlow[0] = jCheckBox7.isSelected();
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jCheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox10ActionPerformed
        Tough2Viewer.dataobj.plotFlow[1] = jCheckBox10.isSelected();
    }//GEN-LAST:event_jCheckBox10ActionPerformed

    private void jButtonCampoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCampoActionPerformed

        Tough2Viewer.dataobj.set_Campo(Float.parseFloat((String) jTextField6.getText()));
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.update_model_scene();
            // Tough2Viewer.tough2viewerGUI.ReBuild3DVoronoi();(not working)
        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();
    }//GEN-LAST:event_jButtonCampoActionPerformed

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed
        Tough2Viewer.dataobj.axis3dvisible = jCheckBox13.isSelected();
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.setAxisVisibility(jCheckBox13.isSelected());

        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox13ActionPerformed

    private void jCheckBoxLGT3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLGT3ActionPerformed
        Tough2Viewer.dataobj.light_3D[2] = jCheckBoxLGT3.isSelected();
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.set_light(jCheckBoxLGT3.isSelected(), 2);
        }
        Tough2Viewer.dataobj.write_tough2viewer_settings();
    }//GEN-LAST:event_jCheckBoxLGT3ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        Tough2Viewer.dataobj.set_VectorFlowLenght(Float.parseFloat((String) jTextField7.getText()));
        if (Tough2Viewer.tough2viewerGUI.FluxModel3DWindowArrow != null) {
            Tough2Viewer.tough2viewerGUI.FluxModel3DWindowArrow.RepaintModel();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jCheckBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox14ActionPerformed
        Tough2Viewer.dataobj.set_logaritmicscale(jCheckBox14.isSelected());

    }//GEN-LAST:event_jCheckBox14ActionPerformed

    private void jCheckBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox15ActionPerformed
        Tough2Viewer.dataobj.use_multiProcessors = jCheckBox15.isSelected();
    }//GEN-LAST:event_jCheckBox15ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        Tough2Viewer.dataobj.MultiSelectionLevel = Integer.parseInt((String) jTextField8.getText());
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        Font f = FontChooser.showDialog(this, "FontChooser", new Font("Dialog", 0, 12));
        if (f != null) {
            Tough2Viewer.dataobj.ScaleFont = f;
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        Tough2Viewer.dataobj.FormatDouble = jTextField9.getText();
        Tough2Viewer.dataobj.write_tough2viewer_settings();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        Color c = JColorChooser.showDialog(OptionFrame1.this, "Choose a color...", getBackground());
        if (c != null) {
            Tough2Viewer.dataobj.SeriesColor = c;
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        Tough2Viewer.dataobj.seriesShape = jComboBox9.getSelectedIndex();
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jCheckBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox16ActionPerformed
        Tough2Viewer.dataobj.fillSeriesShape = jCheckBox16.isSelected();      // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox16ActionPerformed

    private void jCheckBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox17ActionPerformed
        if (jCheckBox17.isSelected()) {
            Tough2Viewer.dataobj.scale_color_orientation = 1;
        } else {
            Tough2Viewer.dataobj.scale_color_orientation = 0;

        }
        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D != null) {
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.repaint_model();
            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.update_scale_scene();//questa e' nuova nuova21/12/11
        }
    }//GEN-LAST:event_jCheckBox17ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        Color c = JColorChooser.showDialog(this, "Choose a color...", getBackground());
        if (c != null) {
            float r = (float) c.getRed();
            float g = (float) c.getGreen();
            float b = (float) c.getBlue();
            r = r / 255.0f;
            g = g / 255.0f;
            b = b / 255.0f;
            Color3f Color1 = new Color3f(r, g, b);
            //jButton1.setBackground(c);
            //jButton1.setForeground(c);
            jTextField10.setForeground(c);
            jTextField10.setBackground(c);

            Tough2Viewer.dataobj.selection_color = Color1;
        } else {

        }
        // TO
    }//GEN-LAST:event_jButton15ActionPerformed
    private static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OptionFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OptionFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OptionFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OptionFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new OptionFrame1().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonCampo;
    private javax.swing.JButton jButtonColorGridAxis;
    private javax.swing.JButton jButtonReduceBlockFactor;
    private javax.swing.JButton jButtonZStretchFactor;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JCheckBox jCheckBoxLGT3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JComboBox jComboBoxGridThickness;
    private javax.swing.JComboBox jComboBoxLineThicness;
    private javax.swing.JComboBox jComboBoxShapeDimension;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableMaterialType;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
