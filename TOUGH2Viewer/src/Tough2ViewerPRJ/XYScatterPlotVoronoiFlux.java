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
import java.util.Hashtable;
import java.lang.String;
import javax.swing.*;
import java.util.ArrayList;
import org.jfree.data.xy.*;//needed for XYSeries, could have called directly
import org.jfree.chart.*;//needed for createXYLineChart
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author stebond
 */
public class XYScatterPlotVoronoiFlux extends javax.swing.JFrame {

    public int actualposition;
    boolean initcomplete = false;
    ArrayList myindex = new ArrayList();

    /**
     * Creates new form XYScatterPlotRegular
     */
    public XYScatterPlotVoronoiFlux(int position) {
        actualposition = position;
        initComponents();
        jPanel1.setLayout(new java.awt.BorderLayout());
        jSlider1.setMaximum(WIDTH);
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
        jComboBox_Y.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getFLOWName()));
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        UpdateMyIndex();
        //caricare la tendina dei flussi...
        String[] blockname = new String[nxyz];
        for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
            blockname[i_b] = Tough2Viewer.dataobj.get_BlockName(i_b);
        }
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(blockname));
        jComboBox1.setSelectedIndex(position);
        this.setTitle("Vertical Flow Profiles");
        initcomplete = true;
        plotGraphXY();

    }

    private void UpdateMyIndex() {
        myindex.clear();
        float xo = Tough2Viewer.dataobj.get_Xo(actualposition);
        float yo = Tough2Viewer.dataobj.get_Yo(actualposition);
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        for (int i_b = 0; i_b < nxyz; i_b++) {
            float xi = Tough2Viewer.dataobj.get_Xo(i_b);
            float yi = Tough2Viewer.dataobj.get_Yo(i_b);
            if (xi == xo) {
                if (yi == yo) {
                    myindex.add(i_b);
                }

            }
        }
    }

    private void plotGraphXY() {
        if (initcomplete) {
            int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
            double y_of_x;
            double x;
            String TitleModule = "Module";
            String TitleCompX = "CompX";
            String TitleCompY = "CompY";
            String TitleCompZ = "CompZ";
            XYSeries seriesModule = new XYSeries(TitleModule);
            XYSeries seriesCompX = new XYSeries(TitleCompX);
            XYSeries seriesCompY = new XYSeries(TitleCompY);
            XYSeries seriesCompZ = new XYSeries(TitleCompZ);
            int VariableX = jComboBox_X.getSelectedIndex();
            int time = jSlider1.getValue();
            int VariableY = jComboBox_Y.getSelectedIndex();
//aggingere ciclo per beccare la colonna o la rigaX o la rigaY
            if (VariableY < 0) {
                VariableY = 0;
            }
            String VariableXName = "";
            String VariableYName = "";
            String values[] = Tough2Viewer.dataobj.getFLOWName();//{"FHEAT", "FLOH/FLOF", "FLOF","FLO(NaCl)","FLO(CO2)"," FLO(GAS)","FLO(LIQ.)"," VEL(GAS)"," VEL(LIQ.)"};
            VariableYName = values[VariableY];
            if (VariableX == 0) {
                TitleModule = "Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(jSlider1.getValue())) + "S";
                VariableXName = "z (m)";
                for (int i = 0; i < myindex.size(); i++) {
                    int index = (Integer) myindex.get(i);
                    double x1 = Tough2Viewer.dataobj.get_VectorDataArray(index, time, VariableY * 3 + 0);
                    double y1 = Tough2Viewer.dataobj.get_VectorDataArray(index, time, VariableY * 3 + 1);
                    double z1 = Tough2Viewer.dataobj.get_VectorDataArray(index, time, VariableY * 3 + 2);
                    double module = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
                    y_of_x = (double) module;
                    seriesModule.add((double) Tough2Viewer.dataobj.get_Zo(index), y_of_x);
                    seriesCompX.add((double) Tough2Viewer.dataobj.get_Zo(index), x1);
                    seriesCompY.add((double) Tough2Viewer.dataobj.get_Zo(index), y1);
                    seriesCompZ.add((double) Tough2Viewer.dataobj.get_Zo(index), z1);
                }
            } else if (VariableX == 1) {
                TitleModule = Tough2Viewer.dataobj.get_BlockName(actualposition) + " ";
                VariableXName = "t (S)";
                for (int it = 0; it < timesteps; it++) {
                    int i_b = actualposition;
                    double x1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, VariableY * 3 + 0);
                    double y1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, VariableY * 3 + 1);
                    double z1 = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, VariableY * 3 + 2);
                    double module = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
                    y_of_x = (double) module;
                    double t = (double) Tough2Viewer.dataobj.get_Times(it);
                    seriesModule.add(t, y_of_x);
                    seriesCompX.add(t, x1);
                    seriesCompY.add(t, y1);
                    seriesCompZ.add(t, z1);
                }
            } else if (VariableX == 2) {
            }

            jLabel4.setText(Integer.toString(actualposition));
            jLabel6.setText(Tough2Viewer.dataobj.get_BlockName(actualposition));
            //add the computed values to the seriesModule
            jLabel8.setText(Float.toString((Tough2Viewer.dataobj.get_Xo(actualposition))));
            jLabel10.setText(Float.toString((Tough2Viewer.dataobj.get_Yo(actualposition))));
            jLabel12.setText(Float.toString((Tough2Viewer.dataobj.get_Zo(actualposition))));
            XYSeriesCollection dataset = new XYSeriesCollection();
            if (Tough2Viewer.dataobj.plotFlow[3]) {
                dataset.addSeries(seriesModule);
            }
            if (Tough2Viewer.dataobj.plotFlow[0]) {
                dataset.addSeries(seriesCompX);
            }
            if (Tough2Viewer.dataobj.plotFlow[1]) {
                dataset.addSeries(seriesCompY);
            }
            if (Tough2Viewer.dataobj.plotFlow[2]) {
                dataset.addSeries(seriesCompZ);
            }

            JFreeChart chart;
            if (jCheckBox1.isSelected()) {
                chart = ChartFactory.createXYLineChart(
                        TitleModule,
                        VariableXName,
                        VariableYName,
                        dataset,
                        org.jfree.chart.plot.PlotOrientation.HORIZONTAL,
                        true,
                        false,
                        false);
            } else {
                chart = ChartFactory.createXYLineChart(
                        TitleModule,
                        VariableXName,
                        VariableYName,
                        dataset,
                        org.jfree.chart.plot.PlotOrientation.VERTICAL,
                        true,
                        false,
                        false);
            }
            XYPlot plot = chart.getXYPlot();
//        plot.setDataset(1, datasetCompX);
//        plot.setDataset(2, datasetCompY);
//        plot.setDataset(3, datasetCompZ);

//        plot.setRenderer(1, new StandardXYItemRenderer());
//        plot.setRenderer(2, new StandardXYItemRenderer());
//        plot.setRenderer(3, new StandardXYItemRenderer());
            int n_series = dataset.getSeriesCount();
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            double Width = Tough2Viewer.dataobj.ShapeDimension;
            double Height = Tough2Viewer.dataobj.ShapeDimension;
            Shape myNewShape = new Rectangle2D.Double(-Width, -Height, 2 * Width, 2 * Height);
            for (int i = 0; i < n_series; i++) {
                renderer.setSeriesStroke(i, new BasicStroke(Tough2Viewer.dataobj.LineThicness, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
                renderer.setSeriesShape(i, myNewShape);
            }

            //
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
            //
            //

            plot.setRenderer(renderer);
            Tough2Viewer.dataobj.ScientificNotationAxis(plot);
            ChartPanel panelchart1 = new ChartPanel(chart);

            panelchart1.setSize(jPanel1.getSize());
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
        jPanel3 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox_X = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        jComboBox_Y = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("jchart"));
        jPanel1.setAutoscrolls(true);
        jPanel1.setOpaque(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 541, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Invert X with Y");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("8");

        jLabel13.setText("Block");

        jLabel5.setText("BlockName");

        jComboBox_X.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "z", "t" }));
        jComboBox_X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_XActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("4");

        jButton2.setText("Export Current");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel9.setText("Yo=");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("10");

        jLabel1.setText("Variable");

        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("6");

        jComboBox_Y.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_YActionPerformed(evt);
            }
        });

        jButton1.setText("Export All Data");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("i=");

        jLabel11.setText("Zo=");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("12");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Domain");

        jLabel7.setText("Xo=");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox_X, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox_Y, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(54, 54, 54))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addComponent(jSlider1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBox1, jComboBox_X, jComboBox_Y});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox_Y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox_X, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(39, 39, 39)
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClose, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClose)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jComboBox_YActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_YActionPerformed
    plotGraphXY();
}//GEN-LAST:event_jComboBox_YActionPerformed
private void jComboBox_XActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_XActionPerformed

    plotGraphXY();
}//GEN-LAST:event_jComboBox_XActionPerformed
private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
// TODO add your handling code here:
    int timestep = jSlider1.getValue();
    String Tooltip = Float.toString(Tough2Viewer.dataobj.get_Times(timestep)) + " S";
    jSlider1.setToolTipText(Tooltip);
    plotGraphXY();
}//GEN-LAST:event_jSlider1StateChanged
private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    String strFilePath = "ExportedAllFluxData.txt";

    //      dos.writeDouble(d);
    //      dos.close();
    String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
    final JFileChooser fc = new JFileChooser(FilePathString);

    String filename = "XYFlow_All_values.dat";
    String SuggestedFileName = FilePathString + "\\" + filename;
    File suggestedFile = new File(SuggestedFileName);
    fc.setSelectedFile(suggestedFile);
    int returnVal = fc.showSaveDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
        strFilePath = fc.getSelectedFile().toString();

        try {
            // Open an output stream
            FileOutputStream fos = new FileOutputStream(strFilePath, false);
            //                      DataOutputStream dos = new DataOutputStream(fos);
            PrintStream ps;
            // Print a line of text
            ps = new PrintStream(fos);

            int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
            int numvar = Tough2Viewer.dataobj.get_number_of_flux_variables();
            float y_of_x;
            float x;
            String Title = "XY Scatter Plot";
            XYSeries series = new XYSeries(Title);
            int VariableX = jComboBox_X.getSelectedIndex();
            int time = jSlider1.getValue();
            int VariableY = jComboBox_Y.getSelectedIndex();
            //aggingere ciclo per beccare la colonna o la rigaX o la rigaY

            if (VariableY < 0) {
                VariableY = 0;
            }

            String VariableXName = "";
            String VariableYName = "";
            String values[] = Tough2Viewer.dataobj.getFLOWName();
            String valuesUM[] = Tough2Viewer.dataobj.getFluxUM();
            VariableYName = values[VariableY];
            String lineaOUT;
            lineaOUT = "File generated by TOUGH2Viewer - by Stebond - FLOW COMPONENT XYZ";
            ps.println(lineaOUT);
            lineaOUT = "File=" + Tough2Viewer.dataobj.get_DataFileName();
            ps.println(lineaOUT);
            String[] xyz = {"x", "y", "z"};
            if (VariableX == 0) {
                Title = "Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(jSlider1.getValue())) + "S";
                VariableXName = "z";
                lineaOUT = "Z;BlockName;";
                for (int it = 0; it < timesteps; it++) {
                    for (int in = 0; in < numvar; in++) {
                        for (int ixyz = 0; ixyz < 3; ixyz++) {
                            lineaOUT = lineaOUT + values[in] + Integer.toString(it) + "_" + xyz[ixyz] + ";";
                        }
                    }
                }
                ps.println(lineaOUT);
                lineaOUT = "m;BlockName;";
                for (int it = 0; it < timesteps; it++) {
                    for (int in = 0; in < numvar; in++) {
                        for (int ixyz = 0; ixyz < 3; ixyz++) {
                            lineaOUT = lineaOUT + valuesUM[in] + ";";
                        }
                    }
                }
                //writeOUT lineaOUT
                ps.println(lineaOUT);

                lineaOUT = "";
                for (int iz = 0; iz < myindex.size(); iz++) {
                    int i_b = (Integer) myindex.get(iz);
                    x = Tough2Viewer.dataobj.get_Zo(i_b);
                    lineaOUT = Float.toString(x) + ";";
                    lineaOUT = lineaOUT + Tough2Viewer.dataobj.get_BlockName(i_b) + ";";
                    for (int it = 0; it < timesteps; it++) {
                        for (int in = 0; in < numvar; in++) {
                            for (int ixyz = 0; ixyz < 3; ixyz++) {
                                y_of_x = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, in * 3 + ixyz);
                                lineaOUT = lineaOUT + Float.toString(y_of_x) + ";";
                            }
                        }
                    }
                    ps.println(lineaOUT);
                }
                //write out lineaOUT

            } else if (VariableX == 1) {
                //mancaintestazione sul file(spiegazionevariabili)
                lineaOUT = "Values for Block=" + Tough2Viewer.dataobj.get_BlockName(actualposition);
                ps.println(lineaOUT);

                lineaOUT = "time;";
                for (int in = 0; in < numvar; in++) {

                    for (int ixyz = 0; ixyz < 3; ixyz++) {
                        lineaOUT = lineaOUT + values[in] + "_" + xyz[ixyz] + ";";
                    }
                }
                ps.println(lineaOUT);
                lineaOUT = "day;";
                for (int in = 0; in < numvar; in++) {
                    for (int ixyz = 0; ixyz < 3; ixyz++) {
                        lineaOUT = lineaOUT + valuesUM[in] + ";";
                    }
                }
                for (int it = 0; it < timesteps; it++) {
                    x = Tough2Viewer.dataobj.get_Times(it);
                    lineaOUT = Float.toString(x) + ";";
                    for (int in = 0; in < numvar; in++) {
                        int i_b = actualposition;
                        for (int ixyz = 0; ixyz < 3; ixyz++) {
                            y_of_x = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, in * 3 + ixyz);
                            lineaOUT = lineaOUT + Float.toString(y_of_x) + ";";
                        }

                    }
                    ps.println(lineaOUT);
                }
            }
            fos.close();
        } // Catches any error conditions
        catch (IOException e) {
            String output = "Unable to write file";
            Tough2Viewer.toLogFile(output);

        }
    }
}//GEN-LAST:event_jButton1ActionPerformed

private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    plotGraphXY();
}//GEN-LAST:event_jCheckBox1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    String strFilePath = "ExportedAllFluxData.txt";
    String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
    final JFileChooser fc = new JFileChooser(FilePathString);
    String filename = "XYFlow_Single_value.dat";
    String SuggestedFileName = FilePathString + "\\" + filename;
    File suggestedFile = new File(SuggestedFileName);
    fc.setSelectedFile(suggestedFile);
    int returnVal = fc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        strFilePath = fc.getSelectedFile().toString();
        try {
            // Open an output stream
            FileOutputStream fos = new FileOutputStream(strFilePath, true);
            //                      DataOutputStream dos = new DataOutputStream(fos);
            PrintStream ps;
            // Print a line of text
            ps = new PrintStream(fos);

            int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
            int numvar = Tough2Viewer.dataobj.get_number_of_flux_variables();
            float y_of_x;
            float x;
            String Title = "XY Scatter Plot";
            XYSeries series = new XYSeries(Title);
            int VariableX = jComboBox_X.getSelectedIndex();
            int time = jSlider1.getValue();
            int VariableY = jComboBox_Y.getSelectedIndex();
            //aggingere ciclo per beccare la colonna o la rigaX o la rigaY

            if (VariableY < 0) {
                VariableY = 0;
            }
            String VariableXName = "";
            String VariableYName = "";
            String values[] = Tough2Viewer.dataobj.getFLOWName();
            String valuesUM[] = Tough2Viewer.dataobj.getFluxUM();
            VariableYName = values[VariableY];
            String lineaOUT;
            lineaOUT = "File generated by TOUGH2Viewer - by Stebond - FLOW COMPONENT XYZ";
            ps.println(lineaOUT);
            lineaOUT = "File=" + Tough2Viewer.dataobj.get_DataFileName();
            ps.println(lineaOUT);
            String[] xyz = {"x", "y", "z"};
            if (VariableX == 0) {
                Title = "Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(jSlider1.getValue())) + "S";
                VariableXName = "z";
                lineaOUT = "Z;BlockName;";
                int it = time;
                int in = VariableY;
                for (int ixyz = 0; ixyz < 3; ixyz++) {
                    lineaOUT = lineaOUT + values[in] + Integer.toString(it) + "_" + xyz[ixyz] + ";";
                }
                ps.println(lineaOUT);
                lineaOUT = "m;BlockName;";
                for (int ixyz = 0; ixyz < 3; ixyz++) {
                    lineaOUT = lineaOUT + valuesUM[in] + ";";
                }
                ps.println(lineaOUT);
                lineaOUT = "";
                for (int iz = 0; iz < myindex.size(); iz++) {
                    int i_b = (Integer) myindex.get(iz);
                    x = Tough2Viewer.dataobj.get_Zo(i_b);
                    lineaOUT = Float.toString(x) + ";";
                    lineaOUT = lineaOUT + Tough2Viewer.dataobj.get_BlockName(i_b) + ";";
                    for (int ixyz = 0; ixyz < 3; ixyz++) {
                        y_of_x = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, in * 3 + ixyz);
                        lineaOUT = lineaOUT + Float.toString(y_of_x) + ";";
                    }
                    ps.println(lineaOUT);
                }
                //write out lineaOUT

            } else if (VariableX == 1) {
                //mancaintestazione sul file(spiegazionevariabili)
                lineaOUT = "Values for Block=" + Tough2Viewer.dataobj.get_BlockName(actualposition);
                ps.println(lineaOUT);
                lineaOUT = "time;";
                int in = VariableY;
                for (int ixyz = 0; ixyz < 3; ixyz++) {
                    lineaOUT = lineaOUT + values[in] + "_" + xyz[ixyz] + ";";
                }
                ps.println(lineaOUT);
                lineaOUT = "day;";
                //int in =VariableY;
                for (int ixyz = 0; ixyz < 3; ixyz++) {
                    lineaOUT = lineaOUT + valuesUM[in] + ";";
                }
                for (int it = 0; it < timesteps; it++) {
                    x = Tough2Viewer.dataobj.get_Times(it);
                    lineaOUT = Float.toString(x) + ";";
                    int i_b = actualposition;
                    for (int ixyz = 0; ixyz < 3; ixyz++) {
                        y_of_x = Tough2Viewer.dataobj.get_VectorDataArray(i_b, it, in * 3 + ixyz);
                        lineaOUT = lineaOUT + Float.toString(y_of_x) + ";";
                    }
                    ps.println(lineaOUT);
                }
            }
            fos.close();
        } // Catches any error conditions
        catch (IOException e) {
            String output = "Unable to write file";
            Tough2Viewer.toLogFile(output);
        }
    }

}//GEN-LAST:event_jButton2ActionPerformed

private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    actualposition = jComboBox1.getSelectedIndex();
    UpdateMyIndex();
    plotGraphXY();
}//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        this.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCloseActionPerformed

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
    private javax.swing.JButton jButtonClose;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox_X;
    private javax.swing.JComboBox jComboBox_Y;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables

}
