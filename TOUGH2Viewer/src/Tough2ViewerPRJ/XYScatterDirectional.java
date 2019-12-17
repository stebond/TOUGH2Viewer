/*
 * XYScatterPlotRegular.java
 *
 * Created on 13 luglio 2009, 12.45
 */
package Tough2ViewerPRJ;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.jfree.data.xy.*;//needed for XYSeries, could have called directly
import org.jfree.chart.*;//needed for createXYLineChart
import javax.swing.*;
import java.util.Hashtable;
import org.jfree.chart.ChartPanel;
import java.lang.String;
import java.util.ArrayList;
import javax.vecmath.Color3f;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author stebond
 */
public class XYScatterDirectional extends javax.swing.JFrame implements AxisChangeListener {

    public int startposition;
    public int endposition;
    boolean initcomplete = false;
    ArrayList[] myindex = new ArrayList[3];
    int[] index;
    double[][] xy;
    Color3f colore[];
    JFreeChart chart;
    float x1;
    float y1;
    float z1;

    /**
     * Creates new form XYScatterPlotRegular
     */
    public XYScatterDirectional(int s_position, int e_position) {

        startposition = s_position;
        endposition = e_position;
        initComponents();
        jPanel1.setLayout(new java.awt.BorderLayout());
        //jSlider1.setMaximum(WIDTH);
        jSlider1.setPaintTicks(true);
        jSlider1.setMinimum(0);
        jSlider1.setMaximum(Tough2Viewer.dataobj.get_TimeSteps() - 1);
        jSlider1.setMajorTickSpacing(1);
        jSlider1.setMinorTickSpacing(1);
        jSlider1.setValue(0);
        Font font = new Font("Serif", Font.ITALIC, 15);
        jSlider1.setFont(font);
        jSlider1.setSnapToTicks(true);
        Hashtable labelTable = new Hashtable();
        for (int i = 0; i < Tough2Viewer.dataobj.get_TimeSteps(); i++) {
            String myTickLabel = String.valueOf(i);
            labelTable.put(i, new JLabel(myTickLabel));
        }
        jSlider1.setLabelTable(labelTable);
        jSlider1.setPaintLabels(true);
        jComboBox_Y.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getVariableName()));

        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        String[] blockname = new String[nxyz];
        for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
            blockname[i_b] = Tough2Viewer.dataobj.get_BlockName(i_b);
        }
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(blockname));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(blockname));
        initcomplete = true;
        if (updategraph()) {
            plotGraphXY();
        }

    }

    public boolean updategraph() {
        float eps = Float.parseFloat(jTextField1.getText());
        if (startposition >= 0 && endposition >= 0 && startposition != endposition) {
            x1 = Tough2Viewer.dataobj.get_Xo(startposition);
            y1 = Tough2Viewer.dataobj.get_Yo(startposition);
            z1 = Tough2Viewer.dataobj.get_Zo(startposition);
            float x2 = Tough2Viewer.dataobj.get_Xo(endposition);
            float y2 = Tough2Viewer.dataobj.get_Yo(endposition);
            float z3 = Tough2Viewer.dataobj.get_Zo(endposition);
            int nxyz = Tough2Viewer.dataobj.get_nxyz();
            if (z1 != z3) {
                return false;
            }
            myindex[0] = new ArrayList();
            myindex[1] = new ArrayList();
            myindex[2] = new ArrayList();
            //(y-y1)/(y2-y1)=(x-x1)/(x2-x1)
            //(y-y1)(x2-x1)=(x-x1)*(y2-y1)
            //y(x2-x1)-y1(x2-x1)=x(y2-y1)-x1(y2-y1)
            //x(y1-y2)+y(x2-x1)+x1(y2-y1)+y1(x1-x2)
            //=>
            float a = y1 - y2;
            float b = x2 - x1;
            float c = x1 * (y2 - y1) + y1 * (x1 - x2);
            float xi;
            float yi;
            float zi;
            float d;
            float denom = (float) Math.sqrt(a * a + b * b);
            for (int i_b = 0; i_b < nxyz; i_b++) {
                xi = Tough2Viewer.dataobj.get_Xo(i_b);
                yi = Tough2Viewer.dataobj.get_Yo(i_b);
                zi = Tough2Viewer.dataobj.get_Zo(i_b);
                d = Math.abs(a * xi + b * yi + c) / denom;
                if (d <= eps) {
                    myindex[0].add(i_b);
                }

            }
        } else {
            return false;
        }
        //ordiniamo myindex in base alla distanza...(altrimenti l'esportazione segue un ordine random)
        int n_points = myindex[0].size();
        float temp_d_index[][] = new float[n_points][2];

        for (int i = 0; i < n_points; i++) {
            int t_index = (Integer) myindex[0].get(i);
            float xi = Tough2Viewer.dataobj.get_Xo(t_index);
            float yi = Tough2Viewer.dataobj.get_Yo(t_index);
            double x = Math.sqrt((x1 - xi) * (x1 - xi) + (y1 - yi) * (y1 - yi));
            temp_d_index[i][0] = (float) x;
            temp_d_index[i][1] = (float) t_index;
        }
        Tough2Viewer.dataobj.sort(temp_d_index, n_points);
        myindex[0].clear();
        for (int i = 0; i < n_points; i++) {
            myindex[0].add((int) temp_d_index[i][1]);
        }

        return true;

    }

    public void axisChanged(AxisChangeEvent ace) {
        Axis axis = ace.getAxis();
        if (axis instanceof NumberAxis) {

            //updateBackGround();
        }
    }

    private void plotGraphXY() {
        if (initcomplete) {
            int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
            double y_of_x;
            String Title = "";
            XYSeries series = new XYSeries(Title);

            int time = jSlider1.getValue();
            int VariableY = jComboBox_Y.getSelectedIndex();

            float[] variableMinMax = new float[4];
            Tough2Viewer.dataobj.minmaxDataArray(VariableY, variableMinMax);

            if (VariableY < 0) {
                VariableY = 0;
            }
            String VariableXName = "";
            String VariableYName = "";
            String VariableName[] = Tough2Viewer.dataobj.getVariableName();
            String VariableNameUM[] = Tough2Viewer.dataobj.getVariablesUM();
            VariableYName = VariableName[VariableY] + " (" + VariableNameUM[VariableY] + ")";
            int VariableX = 0;//poi vedremo
            if (VariableX < 3) {
                Title = "Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(jSlider1.getValue())) + " S";
                VariableXName = "distance (m)";
                xy = new double[myindex[VariableX].size()][2];
                for (int i = 0; i < myindex[VariableX].size(); i++) {
                    int i_b = (Integer) myindex[VariableX].get(i);
                    y_of_x = (double) Tough2Viewer.dataobj.get_dataArray(i_b, time, VariableY);
                    double x = 0;
                    if (VariableX == 0) {
                        float xi = Tough2Viewer.dataobj.get_Xo(i_b);
                        float yi = Tough2Viewer.dataobj.get_Yo(i_b);
                        x = Math.sqrt((x1 - xi) * (x1 - xi) + (y1 - yi) * (y1 - yi));
                    }

                    xy[i][0] = x;
                    xy[i][1] = y_of_x;
                    series.add(x, y_of_x);//attenzione potrebbe non essere ordinata per la distanza....
                }
            }

            //Title="XY Scatter Plot";
            XYDataset dataset = new XYSeriesCollection(series);
            //JFreeChart chart;
            if (jCheckBox1.isSelected()) {
                chart = ChartFactory.createXYLineChart(
                        Title,
                        VariableXName,
                        VariableYName,
                        dataset,
                        org.jfree.chart.plot.PlotOrientation.HORIZONTAL,
                        false,
                        false,
                        false);
            } else {
                chart = ChartFactory.createXYLineChart(
                        Title,
                        VariableXName,
                        VariableYName,
                        dataset,
                        org.jfree.chart.plot.PlotOrientation.VERTICAL,
                        false,
                        false,
                        false);

            }
            //unita' di misura formato scientifico...
            XYPlot myPlot = chart.getXYPlot();
            //aggiungere qui per ymin,ymax...
            NumberAxis YAxis = (NumberAxis) myPlot.getRangeAxis();
            YAxis.setRange(variableMinMax[0], variableMinMax[1]);
            Tough2Viewer.dataobj.ScientificNotationAxis(myPlot);
            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesLinesVisible(0, true);
            if (jCheckBox2.isSelected()) {
                renderer.setSeriesShapesVisible(0, true);
            } else {
                renderer.setSeriesShapesVisible(0, false);
            }
            renderer.setSeriesStroke(0, new BasicStroke(Tough2Viewer.dataobj.LineThicness, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            double Width = Tough2Viewer.dataobj.ShapeDimension;
            double Height = Tough2Viewer.dataobj.ShapeDimension;
            Shape myNewShape = new Rectangle2D.Double(-Width, -Height, 2 * Width, 2 * Height);
            renderer.setSeriesShape(0, myNewShape);
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
            if (jCheckBox3.isSelected()) {
                //plot.setBackgroundImage(createImageBackGround());
            }
            //
            //
            NumberAxis XAxis = (NumberAxis) myPlot.getDomainAxis();
            if (XAxis.hasListener(this) == false) {
                XAxis.addChangeListener(this);
            }

            plot.setRenderer(renderer);

            ChartPanel panelchart1;
            panelchart1 = new ChartPanel(chart);
            Dimension d = jPanel1.getSize();
            if (d.width > 80) {
                d.width = (int) d.width - 10;
            }

            panelchart1.setSize(d);
            panelchart1.setVisible(true);
            jPanel1.removeAll();
            jPanel1.repaint();
            jPanel1.add(panelchart1, BorderLayout.CENTER);
            jPanel1.setVisible(true);
            jPanel1.repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jComboBox_Y = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Profiles");

        jPanel1.setAutoscrolls(true);
        jPanel1.setOpaque(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 607, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jButton1.setText("ExportAllData");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Invert X with Y");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("MarkPoint");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jButton2.setText("ExportCurrentData");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox_Y.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_YActionPerformed(evt);
            }
        });

        jLabel15.setText("Variable");

        jCheckBox3.setText("BackGround Material");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("StartBlock"));

        jLabel3.setText("i=");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("4");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("6");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("8");

        jLabel5.setText("BlockName");

        jLabel7.setText("Xo=");

        jLabel9.setText("Yo=");

        jLabel11.setText("Zo=");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("10");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("12");

        jLabel13.setText("RockType");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("RockType");

        jButton3.setText("Select");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(59, 59, 59))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(34, 34, 34)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(jComboBox1, 0, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTabbedPane1.addTab("Start", jPanel3);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("EndBlock"));

        jLabel17.setText("i=");

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("4");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("6");

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("8");

        jLabel21.setText("BlockName");

        jLabel22.setText("Xo=");

        jLabel23.setText("Yo=");

        jLabel24.setText("Zo=");

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("10");

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("12");

        jLabel27.setText("RockType");

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("RockType");

        jButton4.setText("Select");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel18))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel19))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel20))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTabbedPane1.addTab("End", jPanel4);

        jTextField1.setText("0.0001");

        jLabel29.setText("Tolerance");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel15)
                                            .addGap(19, 19, 19)
                                            .addComponent(jComboBox_Y, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jCheckBox2)
                                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addComponent(jCheckBox3)
                                                .addComponent(jCheckBox1))))
                                    .addGap(74, 74, 74)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_Y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(31, 31, 31)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(35, 35, 35)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addGap(2, 2, 2)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonClose)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonClose)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jComboBox_YActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_YActionPerformed
// TODO add your handling code here:

    if (updategraph()) {
        plotGraphXY();
    }
}//GEN-LAST:event_jComboBox_YActionPerformed

private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
    int timestep = jSlider1.getValue();
    String Tooltip = Float.toString(Tough2Viewer.dataobj.get_Times(timestep)) + " S";
    jSlider1.setToolTipText(Tooltip);
    if (updategraph()) {
        plotGraphXY();
    }
}//GEN-LAST:event_jSlider1StateChanged

private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    // TODO add your handling code here:
    plotGraphXY();
}//GEN-LAST:event_jCheckBox1ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
    final JFileChooser fc = new JFileChooser(FilePathString);

    String filename = "XY_all_values" + Tough2Viewer.dataobj.get_BlockName(startposition) + "_to_" + Tough2Viewer.dataobj.get_BlockName(endposition) + ".dat";
    String SuggestedFileName = FilePathString + "\\" + filename;
    File suggestedFile = new File(SuggestedFileName);
    fc.setSelectedFile(suggestedFile);
    int returnVal = fc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        String strFilePath = fc.getSelectedFile().toString();
        try {
            //Open an output stream
            FileOutputStream fos = new FileOutputStream(strFilePath, false);
            //DataOutputStream dos = new DataOutputStream(fos);
            PrintStream ps;
            // Print a line of text
            ps = new PrintStream(fos);
            int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
            int numvar = Tough2Viewer.dataobj.get_number_of_variables();
            float y_of_x;
            float x = 0;
            int VariableX = 0;//jComboBox_X.getSelectedIndex();
            int VariableY = jComboBox_Y.getSelectedIndex();
            //aggingere ciclo per beccare la colonna o la rigaX o la rigaY
            if (VariableY < 0) {
                VariableY = 0;
            }
            String VariableXName[] = {"x", "y", "z", "t"};
            String values[] = Tough2Viewer.dataobj.getVariableName();
            String valuesUM[] = Tough2Viewer.dataobj.getVariablesUM();
            String lineaOUT;
            lineaOUT = "File generated by iTough2Viewer - by Stebond";
            ps.println(lineaOUT);
            lineaOUT = "File=" + Tough2Viewer.dataobj.get_DataFileName();
            ps.println(lineaOUT);
            lineaOUT = "Path=" + Tough2Viewer.dataobj.get_DataFilePath();
            ps.println(lineaOUT);
            lineaOUT = VariableXName[VariableX] + ";BlockName;";
            for (int it = 0; it < timesteps; it++) {
                for (int in = 0; in < numvar; in++) {
                    lineaOUT = lineaOUT + values[in] + "-t=" + Float.toString(Tough2Viewer.dataobj.get_Times(it)) + " S" + ";";
                }
            }
            ps.println(lineaOUT);
            lineaOUT = "m;BlockName;";
            for (int it = 0; it < timesteps; it++) {
                for (int in = 0; in < numvar; in++) {
                    lineaOUT = lineaOUT + valuesUM[in] + ";";
                }
            }
            ps.println(lineaOUT);
            lineaOUT = "";
            for (int i_b = 0; i_b < myindex[VariableX].size(); i_b++) {
                int ii = (Integer) myindex[VariableX].get(i_b);
                float xi = Tough2Viewer.dataobj.get_Xo(ii);
                float yi = Tough2Viewer.dataobj.get_Yo(ii);
                x = (float) Math.sqrt((x1 - xi) * (x1 - xi) + (y1 - yi) * (y1 - yi));
                lineaOUT = Float.toString(x) + ";";
                lineaOUT = lineaOUT + Tough2Viewer.dataobj.get_BlockName(ii) + ";";
                for (int it = 0; it < timesteps; it++) {
                    for (int in = 0; in < numvar; in++) {
                        y_of_x = Tough2Viewer.dataobj.get_dataArray(ii, it, in);
                        lineaOUT = lineaOUT + Float.toString(y_of_x) + ";";
                    }
                }
                ps.println(lineaOUT);
            }
            fos.close();
        } catch (IOException e)// Catches any error condition
        {
            String output = "Unable to write file";
            Tough2Viewer.toLogFile(output);
        }
}//GEN-LAST:event_jButton1ActionPerformed

    }
private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
    plotGraphXY();
}//GEN-LAST:event_jCheckBox2ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
    final JFileChooser fc = new JFileChooser(FilePathString);

    String filename = "XY_values" + Tough2Viewer.dataobj.get_BlockName(startposition) + "_to_" + Tough2Viewer.dataobj.get_BlockName(endposition) + ".dat";
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
            int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
            int var = jComboBox_Y.getSelectedIndex();
            float y_of_x;
            float x;
            String Title = "XY Scatter Plot";
            int VariableX = 0;
            int VariableY = jComboBox_Y.getSelectedIndex();
//aggingere ciclo per beccare la colonna o la rigaX o la rigaY

            if (VariableY < 0) {
                VariableY = 0;
            }
            String VariableXName = "";
            String VariableYName = "";
            String variableName[] = Tough2Viewer.dataobj.getVariableName();
            String valuesUM[] = Tough2Viewer.dataobj.getVariablesUM();
            VariableYName = variableName[VariableY];
            String lineaOUT;
            lineaOUT = "File generated by iTough2Viewer - by Stebond";
            ps.println(lineaOUT);

            lineaOUT = "File= " + Tough2Viewer.dataobj.get_DataFileName() + " Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(jSlider1.getValue())) + " S";
            lineaOUT = lineaOUT + " Path= " + Tough2Viewer.dataobj.get_DataFilePath();
            lineaOUT = lineaOUT + " Block= " + Tough2Viewer.dataobj.get_BlockName(startposition) + " to " + Tough2Viewer.dataobj.get_BlockName(endposition);
            ps.println(lineaOUT);
            String VariableName[] = Tough2Viewer.dataobj.getVariableName();
            String VariableNameUM[] = Tough2Viewer.dataobj.getVariablesUM();
            VariableYName = VariableName[VariableY] + " (" + VariableNameUM[VariableY] + ")";
            if (VariableX < 3) {
                VariableYName = VariableName[VariableY] + " (" + VariableNameUM[VariableY] + ")";
                lineaOUT = "m;" + variableName[var] + "(" + valuesUM[var] + ")" + ";";
                ps.println(lineaOUT);
                lineaOUT = "";
                int n = xy.length;
                //ordinare xy prima dell'output...
                //
                Tough2Viewer.dataobj.sort(xy, n);
                for (int i = 0; i < n; i++) {
                    lineaOUT = lineaOUT + Double.toString(xy[i][0]) + ";" + Double.toString(xy[i][1]);
                    ps.println(lineaOUT);
                    lineaOUT = "";
                }
            } else if (VariableX == 3) {
                //mancaintestazione sul file(spiegazionevariabili)
                lineaOUT = "Values for Block=" + Tough2Viewer.dataobj.get_BlockName(startposition);
                ps.println(lineaOUT);
                lineaOUT = "time;";
                lineaOUT = lineaOUT + variableName[var] + ";";
                ps.println(lineaOUT);
                lineaOUT = "day;";
                lineaOUT = lineaOUT + valuesUM[var] + ";";
                for (int it = 0; it < timesteps; it++) {
                    x = Tough2Viewer.dataobj.get_Times(it);
                    lineaOUT = Float.toString(x) + ";";
                    int i_b = startposition;
                    y_of_x = Tough2Viewer.dataobj.get_dataArray(i_b, it, var);
                    lineaOUT = lineaOUT + Float.toString(y_of_x) + ";";
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
}//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed

        //updateBackGround(); 
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //show the find block
        FindBlockDialog FindBlockDialogWindow = new FindBlockDialog();
        FindBlockDialogWindow.setVisible(true);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        startposition = jComboBox1.getSelectedIndex();
        jLabel4.setText(Integer.toString(startposition));
        jLabel6.setText(Tough2Viewer.dataobj.get_BlockName(startposition));
        jLabel8.setText(Float.toString((Tough2Viewer.dataobj.get_Xo(startposition))));
        jLabel10.setText(Float.toString((Tough2Viewer.dataobj.get_Yo(startposition))));
        jLabel12.setText(Float.toString((Tough2Viewer.dataobj.get_Zo(startposition))));
        int rocktype_n = Tough2Viewer.dataobj.get_RockType(startposition);
        String rocktype = Tough2Viewer.dataobj.get_RockType_name(rocktype_n);
        jLabel14.setText(rocktype);
        if (updategraph()) {
            plotGraphXY();
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        endposition = jComboBox2.getSelectedIndex();
        jLabel18.setText(Integer.toString(endposition));
        jLabel19.setText(Tough2Viewer.dataobj.get_BlockName(endposition));
        jLabel20.setText(Float.toString((Tough2Viewer.dataobj.get_Xo(endposition))));
        jLabel25.setText(Float.toString((Tough2Viewer.dataobj.get_Yo(endposition))));
        jLabel26.setText(Float.toString((Tough2Viewer.dataobj.get_Zo(endposition))));
        int rocktype_n = Tough2Viewer.dataobj.get_RockType(endposition);
        String rocktype = Tough2Viewer.dataobj.get_RockType_name(rocktype_n);
        jLabel28.setText(rocktype);
        if (updategraph()) {
            plotGraphXY();
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    /**
     * @param args the command line arguments
     */
////    public static void main(String args[]) {
////        java.awt.EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                new XYScatterPlotRegular().setVisible(true);
////            }
////        });
////    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox_Y;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
