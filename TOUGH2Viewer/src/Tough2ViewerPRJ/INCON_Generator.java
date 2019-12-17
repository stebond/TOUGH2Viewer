/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author stefano.bondua
 */
public class INCON_Generator extends javax.swing.JFrame {

    /**
     * Creates new form INCON_Generator
     */
    ArrayList SelectedItemElements;
    ChartPanel panelchart1;
    JFreeChart chart;
    int VariableY;
    String FilePathString;
    String tough2viewer_dat_file_name = "none";
    String MESH_file_name = "none";
    String T2_data_file_name = "none";
    String INCON_file_name = "none";
    ArrayList xyz_csv_file = new ArrayList();
    int n_rows_csv;
    double[][] csv_val_array;
    double[] estimated_V_array;

    public INCON_Generator() {
        initComponents();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getVariableName()));

        SelectedItemElements = new ArrayList();
        jComboBox2.setSelectedIndex(2);

        ArrayList rocksnames = Tough2Viewer.dataobj.get_rocknames();
        int tipirocce = rocksnames.size();
        String[] rocknames_str = new String[tipirocce];
        for (int i = 0; i < tipirocce; i++) {
            rocknames_str[i] = (String) rocksnames.get(i);
        }
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(rocknames_str));
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(rocknames_str));

    }

    public void plot_graph() {
        String Title = "";
        XYSeries series = new XYSeries(Title);
        String VariableName[] = Tough2Viewer.dataobj.getVariableName();
        String VariableNameUM[] = Tough2Viewer.dataobj.getVariablesUM();
        int i = jList2.getSelectedIndex();
        VariableY = (Integer) SelectedItemElements.get(i);
        String VariableYName = VariableName[VariableY] + " (" + VariableNameUM[VariableY] + ")";
        int VariableX = jComboBox2.getSelectedIndex();
        double x;
        double y_of_x;
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        int time = 0;
        String VariableXName = (String) jComboBox2.getSelectedItem();
        for (int i_b = 0; i_b < nxyz; i_b++) {
            y_of_x = (double) Tough2Viewer.dataobj.get_dataArray(i_b, time, VariableY);
            if (VariableX == 0) {
                x = (double) Tough2Viewer.dataobj.get_Xo(i_b);
            } else if (VariableX == 1) {
                x = (double) Tough2Viewer.dataobj.get_Yo(i_b);
            } else {
                x = (double) Tough2Viewer.dataobj.get_Zo(i_b);
            }
            series.add(x, y_of_x);
        }
        XYDataset dataset = new XYSeriesCollection(series);

        {
            chart = ChartFactory.createXYLineChart(
                    Title,
                    VariableXName,
                    VariableYName,
                    dataset,
                    org.jfree.chart.plot.PlotOrientation.HORIZONTAL,
                    false,
                    false,
                    false);
        }

        panelchart1 = new ChartPanel(chart);
        Dimension d = jPanel4.getSize();
        if (d.width > 80) {
            d.width = (int) d.width - 10;
        }

        panelchart1.setSize(d);
        panelchart1.setVisible(true);
        jPanel4.removeAll();
        jPanel4.repaint();
        jPanel4.add(panelchart1, BorderLayout.CENTER);
        jPanel4.setVisible(true);
        jPanel4.repaint();
    }

    public void plot_graph2() {

        String Title = "";
        XYSeries series = new XYSeries(Title);
        XYSeries series_preview = new XYSeries("preview");
        String VariableName[] = Tough2Viewer.dataobj.getVariableName();
        String VariableNameUM[] = Tough2Viewer.dataobj.getVariablesUM();
        int i = jList2.getSelectedIndex();
        VariableY = (Integer) SelectedItemElements.get(i);
        String VariableYName = VariableName[VariableY] + " (" + VariableNameUM[VariableY] + ")";
        int VariableX = jComboBox2.getSelectedIndex();
        double x;
        double y_of_x;
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        int time = 0;
        String VariableXName = (String) jComboBox2.getSelectedItem();
        for (int i_b = 0; i_b < nxyz; i_b++) {
            y_of_x = (double) Tough2Viewer.dataobj.get_dataArray(i_b, time, VariableY);
            if (VariableX == 0) {
                x = (double) Tough2Viewer.dataobj.get_Xo(i_b);
            } else if (VariableX == 1) {
                x = (double) Tough2Viewer.dataobj.get_Yo(i_b);
            } else {
                x = (double) Tough2Viewer.dataobj.get_Zo(i_b);
            }
            series.add(x, y_of_x);
            series_preview.add(x, estimated_V_array[i_b]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        dataset.addSeries(series_preview);
        {
            chart = ChartFactory.createXYLineChart(
                    Title,
                    VariableXName,
                    VariableYName,
                    dataset,
                    org.jfree.chart.plot.PlotOrientation.HORIZONTAL,
                    false,
                    false,
                    false);
        }

        panelchart1 = new ChartPanel(chart);
        Dimension d = jPanel4.getSize();
        if (d.width > 80) {
            d.width = (int) d.width - 10;
        }

        panelchart1.setSize(d);
        panelchart1.setVisible(true);
        jPanel4.removeAll();
        jPanel4.repaint();
        jPanel4.add(panelchart1, BorderLayout.CENTER);
        jPanel4.setVisible(true);
        jPanel4.repaint();
    }

    public void update_variable_combobox() {
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getVariableName()));

    }

    void format_plot() {
        XYPlot myPlot = chart.getXYPlot();
        //aggiungere qui per ymin,ymax...
        NumberAxis YAxis = (NumberAxis) myPlot.getRangeAxis();
        float[] variableMinMax = new float[4];
        Tough2Viewer.dataobj.minmaxDataArray(VariableY, variableMinMax);
        //YAxis.setRange(variableMinMax[0], variableMinMax[1]);
        Tough2Viewer.dataobj.ScientificNotationAxis(myPlot);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesStroke(0, new BasicStroke(Tough2Viewer.dataobj.LineThicness, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
        double Width = Tough2Viewer.dataobj.ShapeDimension;
        double Height = Tough2Viewer.dataobj.ShapeDimension;

        if (Tough2Viewer.dataobj.seriesShape == 0) {
            renderer.setSeriesShape(0, new Ellipse2D.Double(-Width, -Height, 2 * Width, 2 * Height));
        } else {
            renderer.setSeriesShape(0, new Rectangle2D.Double(-Width, -Height, 2 * Width, 2 * Height));
        }

        renderer.setSeriesPaint(0, Tough2Viewer.dataobj.SeriesColor);
        if (Tough2Viewer.dataobj.fillSeriesShape) {
            renderer.setUseFillPaint(true);
        } else {
            renderer.setUseFillPaint(false);
        }
        plot.setDomainGridlinePaint(Tough2Viewer.dataobj.GridAxisColor);
        plot.setRangeGridlinePaint(Tough2Viewer.dataobj.GridAxisColor);
        BasicStroke theGridLineStroke = (BasicStroke) plot.getDomainGridlineStroke();
        float linewidth = theGridLineStroke.getLineWidth();
        linewidth = linewidth * Tough2Viewer.dataobj.GridAxisThickness;
        float[] theDashArray = theGridLineStroke.getDashArray();
        for (int i = 0; i < theDashArray.length; i++) {
            theDashArray[i] = theDashArray[i] * Tough2Viewer.dataobj.StretchDashlines;
        }
        int cap = theGridLineStroke.getEndCap();
        int join = theGridLineStroke.getLineJoin();
        float miterlimit = theGridLineStroke.getMiterLimit();
        float dashphase = theGridLineStroke.getDashPhase();
        BasicStroke myGridLineStroke = new BasicStroke(linewidth, cap, join, miterlimit, theDashArray, dashphase);
        float offset = Tough2Viewer.dataobj.GridOffSet;
        plot.setAxisOffset(new RectangleInsets(offset, offset, offset, offset));
        plot.setDomainGridlineStroke(myGridLineStroke);
        plot.setRangeGridlineStroke(myGridLineStroke);
        plot.setBackgroundPaint(Tough2Viewer.dataobj.PlotAreaColor);
        plot.setRenderer(renderer);

    }

    void format_plot2() {
        XYPlot myPlot = chart.getXYPlot();
        //aggiungere qui per ymin,ymax...
        NumberAxis YAxis = (NumberAxis) myPlot.getRangeAxis();
        float[] variableMinMax = new float[4];
        Tough2Viewer.dataobj.minmaxDataArray(VariableY, variableMinMax);
        //YAxis.setRange(variableMinMax[0], variableMinMax[1]);
        Tough2Viewer.dataobj.ScientificNotationAxis(myPlot);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);
        //renderer.setSeriesStroke(0, new BasicStroke(Tough2Viewer.dataobj.LineThicness, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
        double Width = Tough2Viewer.dataobj.ShapeDimension;
        double Height = Tough2Viewer.dataobj.ShapeDimension;

//    if(Tough2Viewer.dataobj.seriesShape==0)
//    {
//        renderer.setSeriesShape(0, new Ellipse2D.Double(-Width,-Height,2*Width,2*Height)); 
//    }
//    else
//    {
//        renderer.setSeriesShape(0,new Rectangle2D.Double(-Width,-Height,2*Width,2*Height));
//    }
        renderer.setSeriesPaint(0, Tough2Viewer.dataobj.SeriesColor);
        if (Tough2Viewer.dataobj.fillSeriesShape) {
            renderer.setUseFillPaint(true);
        } else {
            renderer.setUseFillPaint(false);
        }
        plot.setDomainGridlinePaint(Tough2Viewer.dataobj.GridAxisColor);
        plot.setRangeGridlinePaint(Tough2Viewer.dataobj.GridAxisColor);
        BasicStroke theGridLineStroke = (BasicStroke) plot.getDomainGridlineStroke();
        float linewidth = theGridLineStroke.getLineWidth();
        linewidth = linewidth * Tough2Viewer.dataobj.GridAxisThickness;
        float[] theDashArray = theGridLineStroke.getDashArray();
        for (int i = 0; i < theDashArray.length; i++) {
            theDashArray[i] = theDashArray[i] * Tough2Viewer.dataobj.StretchDashlines;
        }
        int cap = theGridLineStroke.getEndCap();
        int join = theGridLineStroke.getLineJoin();
        float miterlimit = theGridLineStroke.getMiterLimit();
        float dashphase = theGridLineStroke.getDashPhase();
        BasicStroke myGridLineStroke = new BasicStroke(linewidth, cap, join, miterlimit, theDashArray, dashphase);
        float offset = Tough2Viewer.dataobj.GridOffSet;
        plot.setAxisOffset(new RectangleInsets(offset, offset, offset, offset));
        plot.setDomainGridlineStroke(myGridLineStroke);
        plot.setRangeGridlineStroke(myGridLineStroke);
        plot.setBackgroundPaint(Tough2Viewer.dataobj.PlotAreaColor);
        plot.setRenderer(renderer);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButton14 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jComboBox8 = new javax.swing.JComboBox<>();
        jButton15 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("INCON Generator");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Plot"));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setText("INCON VARIABLES");

        jScrollPane2.setViewportView(jList2);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "X", "Y", "Z" }));

        jButton4.setText("UpdatePlot");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton8.setText("Change Orientation");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTabbedPane1.setToolTipText("Basic");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Add new ...");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextField1.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jComboBox1, 0, 131, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(547, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Variable Selector", jPanel1);

        jButton13.setText("CSV file");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel15.setText("x");

        jLabel16.setText("y");

        jLabel17.setText("z");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel18.setText("Variable");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("IDW options"));

        jLabel19.setText("N. of Points");

        jTextField7.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextField7.setText("10");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Use x");

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Use y");

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Use z");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton14.setText("Preview");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Apply mode"));

        buttonGroup3.add(jRadioButton4);
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("Apply to all blocks");

        buttonGroup3.add(jRadioButton5);
        jRadioButton5.setText("Apply to selected blocks");

        buttonGroup3.add(jRadioButton6);
        jRadioButton6.setText("Apply to selected rocktypes");

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jRadioButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox8, 0, 67, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton4)
                            .addComponent(jRadioButton5))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton6)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton15.setText("Apply");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton13)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton13)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15)
                .addContainerGap(156, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("From XYZ Data", jPanel3);

        jLabel2.setText("Linear INCON generation");

        jLabel3.setText("New Var=a*X+b*Y+c*Z+Cost");

        jLabel4.setText("a=");

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.setText("0.0");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel5.setText("Cost=");

        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.setText("1.013E5");

        jLabel6.setText("b=");

        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.setText("0.0");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.setText("-1.013E4");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel7.setText("c=");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Apply to all blocks");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Apply to selected blocks");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Apply to selected rocktypes");

        jButton5.setText("Preview");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Apply");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Zero to top");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel8.setText("Zero");

        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField6.setText("1.013E5");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton9.setText("P");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("T");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel6)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel7)))
                                .addGap(65, 65, 65)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                    .addComponent(jTextField3))
                                .addGap(0, 29, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton9)
                            .addComponent(jButton10)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton5)
                                    .addComponent(jRadioButton1)
                                    .addComponent(jRadioButton2))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jButton9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jButton10))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap(289, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Basic", jPanel2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(7, 7, 7)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Prepare INCON", jPanel6);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Write INCON to a file..."));

        jButton11.setText("Choose File to save as...");
        jButton11.setToolTipText("Choose a file in the correct path. The INCON file exstension will be added to the file name..");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(80);
        jTextArea1.setFont(new java.awt.Font("Courier", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("INCON----1----*----2----*----3----*----4----*----5----*----6----*----7----*----8\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        jScrollPane1.setViewportView(jTextArea1);

        jLabel9.setText("Preview:");

        jButton12.setText("Update Preview");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jTextField8.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jTextField8.setText("\\n");

        jLabel10.setText("Format Strings:");

        jLabel11.setText("BLOCKNAME (5 CHARS)+");

        jLabel12.setText("INCON VARIABLES");

        jTextField9.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jTextField9.setText(" %19e");

        jTextField10.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jTextField10.setText("\\n");

        jLabel13.setText("END OF LINE");

        jLabel14.setText("Select a file name for saving:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(29, 29, 29)
                        .addComponent(jButton12)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addGap(8, 8, 8)
                                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap(423, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jLabel14))
                .addContainerGap(106, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Write OUT", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1028, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    DefaultListModel listModel = new DefaultListModel();
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String name_to_add = jTextField1.getText();
        if (name_to_add == "") {
            return;
        }
        listModel.addElement(name_to_add);
        //jList1.setModel(listModel);
        jList2.setModel(listModel);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int index_to_add = jComboBox1.getSelectedIndex();
        String name_to_add = (String) jComboBox1.getSelectedItem();
        for (int i = 0; i < SelectedItemElements.size(); i++) {
            if (index_to_add == (int) SelectedItemElements.get(i)) {
                return;
            }
        }
        SelectedItemElements.add(index_to_add);
        listModel.addElement(name_to_add);
        //jList1.setModel(listModel);
        jList2.setModel(listModel);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (jList2.getSelectedIndex() > -1) {
            plot_graph();        // TODO add your handling code here:
            format_plot();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int index_to_remove = jList2.getSelectedIndex();
        if (index_to_remove > -1) {
            SelectedItemElements.remove(index_to_remove);
            listModel.remove(index_to_remove);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        float z_max = (float) Tough2Viewer.dataobj.zmax_node;
        float var_top = (float) Double.parseDouble(jTextField6.getText());
        float m = (float) Double.parseDouble(jTextField5.getText());
        float Cost = var_top - m * z_max;
        //var_top=m*z_max+Cost;
        //cost=var_top-m*z_max;
        jTextField3.setText(Double.toString(Cost));
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        float z_max = (float) Tough2Viewer.dataobj.zmax_node;
        float z_min = (float) Tough2Viewer.dataobj.zmin_node;
        float var_top = var(0.0f, 0.0f, z_max);
        float var_bottom = var(0.0f, 0.0f, z_min);
        String Title = "Preview";
        XYSeries series = new XYSeries(Title);
        series.add(z_min, var_bottom);
        series.add(z_max, var_top);
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        XYPlot plot = chart.getXYPlot();
        plot.setDataset(1, dataset);

        plot.getDataset();

        int num_dataset = plot.getDatasetCount();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        XYItemRenderer renderer1 = plot.getRenderer(0);

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(Tough2Viewer.dataobj.LineThicness, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
        renderer.setSeriesPaint(0, Tough2Viewer.dataobj.SeriesColor);

        plot.setRenderer(1, renderer);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        XYPlot plot = chart.getXYPlot();
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == org.jfree.chart.plot.PlotOrientation.HORIZONTAL) {
            orientation = org.jfree.chart.plot.PlotOrientation.VERTICAL;
        } else {
            orientation = org.jfree.chart.plot.PlotOrientation.HORIZONTAL;
        }
        plot.setOrientation(orientation);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        applyproperties();
        if (jList2.getSelectedIndex() > -1) {
            plot_graph();        // TODO add your handling code here:
            format_plot();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jTextField5.setText("-1.013E4");
        jTextField6.setText("1.013E5");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        jTextField5.setText("-0.03");
        jTextField6.setText("15.0");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        String first_line = "INCON----1----*----2----*----3----*----4----*----5----*----6----*----7----*----8\n";
        String afterblock = jTextField8.getText();
        afterblock = afterblock.replace("\\n", "\n");
        String aftervariables = jTextField10.getText();
        aftervariables = aftervariables.replace("\\n", "\n");
        String total = first_line;
        String formatvariables = jTextField9.getText();
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        for (int i_b = 0; i_b < nxyz; i_b++) {
            total = total + Tough2Viewer.dataobj.get_BlockName(i_b) + afterblock;

            for (int i = 0; i < SelectedItemElements.size(); i++) {
                int variable_index = (int) SelectedItemElements.get(i);
                int time_step = 0;
                float var = Tough2Viewer.dataobj.get_dataArray(i_b, time_step, variable_index);

                String var_string = String.format(formatvariables, var).replaceAll(",", ".");
                total = total + var_string;

            }
            total = total + aftervariables;
        }
        total = total + "\n";

        jTextArea1.setText(total);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
        final JFileChooser fc = new JFileChooser(FilePathString);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String FileNameINCON = fc.getSelectedFile().toString();
            FileNameINCON = FileNameINCON + ".INCON";

            try {
                FileOutputStream fos = new FileOutputStream(FileNameINCON, false);

                PrintStream ps;

                ps = new PrintStream(fos);

                ps.println(jTextArea1.getText());
            } catch (IOException e) {
                String error = "Export_INCON-> Unable to write file";
                Tough2Viewer.toLogFile(error);
                JOptionPane.showMessageDialog(null, error);
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        String delimiter = ";";
        FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
        final JFileChooser fc = new JFileChooser(FilePathString);
        fc.setDialogTitle("Open tough2viewer.dat");
        int returnVal = fc.showOpenDialog(INCON_Generator.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            FilePathString = fc.getCurrentDirectory().toString();
            File file = fc.getSelectedFile();
            String fileName = file.getName();
            xyz_csv_file = read_csv_file(file, delimiter);
        }
        int n_rows = xyz_csv_file.size() - 1;
        String header = (String) xyz_csv_file.get(0);
        String[] header_cols = JoeStringUtils1.parseString(header, delimiter);
        int n_cols = header_cols.length;
        csv_val_array = new double[n_rows][n_cols];
        for (int i = 1; i < n_rows; i++) {
            String linea = (String) xyz_csv_file.get(i);
            String[] vars_string = JoeStringUtils1.parseString(linea, delimiter);
            if (vars_string.length != n_cols) {
                //uhm error here

            } else {
                for (int j = 0; j < n_cols; j++) {
                    csv_val_array[i - 1][j] = Double.parseDouble(vars_string[j]);
                }
            }
        }
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(header_cols));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(header_cols));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(header_cols));
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(header_cols));
        n_rows_csv = n_rows;
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        int var_x_index = jComboBox4.getSelectedIndex();
        int var_y_index = jComboBox5.getSelectedIndex();
        int var_z_index = jComboBox6.getSelectedIndex();
        int var_V_index = jComboBox7.getSelectedIndex();
        double use_x = 1.0;
        double use_y = 1.0;
        double use_z = 1.0;
        int n_neighbourgh = Integer.parseInt(jTextField7.getText());
        if (!jCheckBox1.isSelected()) {
            use_x = 0.0;
        }
        if (!jCheckBox2.isSelected()) {
            use_y = 0.0;
        }
        if (!jCheckBox3.isSelected()) {
            use_z = 0.0;
        }
        estimated_V_array = new double[nxyz];
        for (int i_b = 0; i_b < nxyz; i_b++) {
            double[][] array = new double[n_rows_csv][2];
            double xo = Tough2Viewer.dataobj.get_Xo(i_b);
            double yo = Tough2Viewer.dataobj.get_Yo(i_b);
            double zo = Tough2Viewer.dataobj.get_Zo(i_b);
            for (int j = 0; j < n_rows_csv; j++) {
                double xp = csv_val_array[j][var_x_index];
                double yp = csv_val_array[j][var_y_index];
                double zp = csv_val_array[j][var_z_index];
                double dx2 = use_x * (xo - xp) * (xo - xp);
                double dy2 = use_y * (yo - yp) * (yo - yp);
                double dz2 = use_z * (zo - zp) * (zo - zp);
                double d = Math.sqrt(dx2 + dy2 + dz2);
                array[j][0] = d;
                array[j][1] = j;
            }
            Tough2Viewer.dataobj.sort(array, n_rows_csv);

            double estimatedV = 0.0;
            if (array[0][0] < 1.0E-4) {
                int index_neigh = (int) array[0][1];

                estimatedV = csv_val_array[index_neigh][var_V_index];
            } else {
                double sumw = 0.0;
                for (int j = 0; j < Math.min(n_rows_csv, n_neighbourgh); j++) {

                    double w = 1.0 / array[j][0];
                    estimatedV = csv_val_array[(int) array[j][1]][var_V_index] * w;
                    sumw = sumw + w;
                }
                estimatedV = estimatedV / sumw;

            }
            estimated_V_array[i_b] = estimatedV;
        }
        plot_graph2();
        format_plot2();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        applyproperties2();
        if (jList2.getSelectedIndex() > -1) {
            plot_graph();        // TODO add your handling code here:
            format_plot();
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private ArrayList read_csv_file(File file, String delimiter) {

        ArrayList file_array = new ArrayList();
        FileInputStream fis;
        InputStreamReader InStrReader;
        BufferedReader dis;
        String linea;

        try {
            fis = new FileInputStream(file);
            InStrReader = new InputStreamReader(fis);
            dis = new BufferedReader(InStrReader);

            while ((linea = dis.readLine()) != null) {
                file_array.add(linea);

            }
            dis.close();
            int ok = 1;
        } catch (Exception ex) {
            return null;
        }
        return file_array;
    }

    private void applyproperties() {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        int time_step = 0;
        int index = jList2.getSelectedIndex();
        int variable_to_be_changed = (int) SelectedItemElements.get(index);
        for (int i_b = 0; i_b < nxyz; i_b++) {
            float x = Tough2Viewer.dataobj.get_Xo(i_b);
            float y = Tough2Viewer.dataobj.get_Yo(i_b);
            float z = Tough2Viewer.dataobj.get_Zo(i_b);
            float new_var = var(x, y, z);
            if (jRadioButton1.isSelected()) {
                Tough2Viewer.dataobj.set_dataArray(i_b, time_step, variable_to_be_changed, new_var);
            } else if (jRadioButton2.isSelected()) {
                if (Tough2Viewer.dataobj.Block_is_selected[i_b]) {
                    Tough2Viewer.dataobj.set_dataArray(i_b, time_step, variable_to_be_changed, new_var);
                }
            } else {
                int rocktype = Tough2Viewer.dataobj.get_RockType(i_b);
                if (rocktype == jComboBox3.getSelectedIndex()) {
                    Tough2Viewer.dataobj.set_dataArray(i_b, time_step, variable_to_be_changed, new_var);
                }
            }

        }

    }

    private void applyproperties2() {
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        int time_step = 0;
        int index = jList2.getSelectedIndex();
        int variable_to_be_changed = (int) SelectedItemElements.get(index);
        for (int i_b = 0; i_b < nxyz; i_b++) {
            float new_var = (float) estimated_V_array[i_b];
            if (jRadioButton4.isSelected()) {
                Tough2Viewer.dataobj.set_dataArray(i_b, time_step, variable_to_be_changed, new_var);
            } else if (jRadioButton5.isSelected()) {
                if (Tough2Viewer.dataobj.Block_is_selected[i_b]) {
                    Tough2Viewer.dataobj.set_dataArray(i_b, time_step, variable_to_be_changed, new_var);
                }
            } else {
                int rocktype = Tough2Viewer.dataobj.get_RockType(i_b);
                if (rocktype == jComboBox8.getSelectedIndex()) {
                    Tough2Viewer.dataobj.set_dataArray(i_b, time_step, variable_to_be_changed, new_var);
                }
            }

        }

    }

    private float var(double x, double y, double z) {
        float var;
        float a = (float) Double.parseDouble(jTextField4.getText());
        float b = (float) Double.parseDouble(jTextField2.getText());
        float c = (float) Double.parseDouble(jTextField5.getText());
        float cost = (float) Double.parseDouble(jTextField3.getText());
        var = (float) (a * x + b * y + c * z + cost);
        return var;
    }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
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
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
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
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
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
