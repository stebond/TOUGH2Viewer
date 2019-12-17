/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author stebond. This dialog box allow to display and plot graph about the
 * information readed by the TOUGH output file. The sub BALLA of TOUGH export
 * the total mass and heat balance. This function was tested for the BALLA
 * funtion of EWASG (Battistelli et al.).
 *
 */
public class BallaFrm extends javax.swing.JFrame {

    ArrayList balla = new ArrayList();
    double[][] values;
    boolean initcomplete = false;
    int n_time_step;
    int n_colonne;
    JFreeChart chart;
    String[] titoli;
    DefaultListModel model;
    DefaultListModel model_2;
    boolean first_plot = true;

    /**
     * Creates new form BallaFrm
     */
    public BallaFrm() {
        initComponents();
        balla = Tough2Viewer.dataobj.get_Balla();
        prepareTabel();
        prepareJlist();
        initcomplete = true;
        jPanelChart.setLayout(new java.awt.BorderLayout());
    }

    private void prepareJlist() {
        model = new DefaultListModel();

        for (int i = 1; i < titoli.length; i++) {
            model.add(i - 1, titoli[i]);
        }
        jList1.setModel(model);
        model_2 = new DefaultListModel();
        jList2.setModel(model_2);
        model_2.clear();
    }

    private void prepareTabel() {
        n_time_step = (balla.size() + 1) / 4;
        ArrayList name = (ArrayList) balla.get(1);
        ArrayList UM = (ArrayList) balla.get(3);
        titoli = new String[name.size() + 1];
        values = new double[n_time_step][name.size() + 1];
        titoli[0] = "Timestep(sec)";
        for (int i = 0; i < name.size(); i++) {
            titoli[i + 1] = (String) name.get(i);
            titoli[i + 1] = titoli[i + 1] + "(" + UM.get(i) + ")";
        }
        for (int i = 0; i < n_time_step; i++) {
            ArrayList mydouble = (ArrayList) balla.get(2 + i * 4);
            for (int j = 0; j < mydouble.size(); j++) {
                Double temp = (Double) mydouble.get(j);
                values[i][j + 1] = (double) temp;
            }
        }
        n_colonne = titoli.length;
        // Create a table with 10 rows and 5 columns
        Object[][] myObj = new Object[n_time_step][n_colonne];
        for (int vColIndex = 0; vColIndex < n_colonne; vColIndex++) {
            for (int vRowIndex = 0; vRowIndex < n_time_step; vRowIndex++) {
                myObj[vRowIndex][vColIndex] = null;
            }
        }
        //jTable1 = new JTable(n_time_step, n_colonne);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(myObj, titoli));
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int vColIndex = 0; vColIndex < titoli.length; vColIndex++) {
            jTable1.getColumnModel().getColumn(vColIndex).setHeaderValue(titoli[vColIndex]);
        }
        // Make the table vertically scrollable

        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        for (int vRowIndex = 0; vRowIndex < n_time_step; vRowIndex++) {
            Double time = (Double) balla.get(vRowIndex * 4);

            values[vRowIndex][0] = (double) time;
        }
        for (int vColIndex = 0; vColIndex < n_colonne; vColIndex++) {
            for (int vRowIndex = 0; vRowIndex < n_time_step; vRowIndex++) {
                jTable1.setValueAt(values[vRowIndex][vColIndex], vRowIndex, vColIndex);
            }
        }
    }

    private void plotGraphXY() {
        int n_series = model_2.size();

        for (int i = 0; i < n_series; i++) {

        }
        if (initcomplete) {

            String Title = "BALLA";

            XYSeries[] series = new XYSeries[n_series];
            for (int i = 0; i < n_series; i++) {
                series[i] = new XYSeries((String) model_2.get(i));
            }

            String VariableXName = "Time(sec)";
            String VariableYName = "M^3,Kg,J";
            int[] index = new int[n_series];
            for (int i = 0; i < n_series; i++) {
                for (int j = 0; j < model.size(); j++) {
                    String temp = (String) model.get(j);
                    if (temp.contentEquals((String) model_2.get(i))) {
                        index[i] = j;
                    }
                }

            }
            for (int i = 0; i < n_time_step; i++) {

                for (int j = 0; j < n_series; j++) {
                    series[j].add(values[i][0], values[i][index[j] + 1]);
                }
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            for (int i = 0; i < n_series; i++) {
                dataset.addSeries(series[i]);
            }

            chart = ChartFactory.createXYLineChart(
                    Title,
                    VariableXName,
                    VariableYName,
                    dataset,
                    org.jfree.chart.plot.PlotOrientation.VERTICAL,
                    true,
                    false,
                    false);

            XYPlot plot = chart.getXYPlot();

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

            panelchart1.setSize(jPanelChart.getSize());
            panelchart1.setVisible(true);

            jPanelChart.removeAll();
            jPanelChart.repaint();
            jPanelChart.add(panelchart1, BorderLayout.CENTER);
            jPanelChart.setVisible(true);
            jPanelChart.getParent().validate();
            jPanelChart.repaint();

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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanelPlot = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jPanelChart = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Balla");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTabbedPane1.addTab("Table", jScrollPane1);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jList2);

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        jPanelChart.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanelChartLayout = new javax.swing.GroupLayout(jPanelChart);
        jPanelChart.setLayout(jPanelChartLayout);
        jPanelChartLayout.setHorizontalGroup(
            jPanelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        jPanelChartLayout.setVerticalGroup(
            jPanelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelPlotLayout = new javax.swing.GroupLayout(jPanelPlot);
        jPanelPlot.setLayout(jPanelPlotLayout);
        jPanelPlotLayout.setHorizontalGroup(
            jPanelPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPlotLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelPlotLayout.setVerticalGroup(
            jPanelPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPlotLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Plot", jPanelPlot);

        jButtonClose.setText("Close");
        jButtonClose.setToolTipText("Close this window");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jButtonExport.setText("Export");
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonExport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonClose))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //add element to the jList2
        int element_to_add = jList1.getSelectedIndex();
        String name_element_to_add;
        if (element_to_add > -1) {
            int[] to_add = jList1.getSelectedIndices();
            for (int i = 0; i < to_add.length; i++) {

                name_element_to_add = (String) model.get(to_add[i]);
                boolean insert = true;
                for (int j = 0; j < model_2.getSize(); j++) {
                    String temp = (String) model_2.get(j);
                    if (temp.contentEquals(name_element_to_add)) {
                        insert = false;
                    }
                }
                if (insert) {
                    model_2.addElement(name_element_to_add);
                }
            }
        }
        plotGraphXY();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        model_2.clear();
        plotGraphXY();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int[] to_remove = jList2.getSelectedIndices();
        for (int i = 0; i < to_remove.length; i++) {
            model_2.remove(to_remove[i]);
        }
        plotGraphXY();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jList1MouseClicked

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        this.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed
        String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
        final JFileChooser fc = new JFileChooser(FilePathString);
        String filename = "Balla.dat";
        String SuggestedFileName = FilePathString + "\\" + filename;
        File suggestedFile = new File(SuggestedFileName);
        fc.setSelectedFile(suggestedFile);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String strFilePath = fc.getSelectedFile().toString();
            try {
                //Open an output stream
                FileOutputStream fos = new FileOutputStream(strFilePath, true);
                //DataOutputStream dos = new DataOutputStream(fos);
                PrintStream ps;
                // Print a line of text
                ps = new PrintStream(fos);
                int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
                int numvar = Tough2Viewer.dataobj.get_number_of_variables();
                float y_of_x;
                float x = 0;
                int VariableX = 0;//jComboBox_X.getSelectedIndex();

                String lineaOUT;
                lineaOUT = "File generated by iTough2Viewer - by Stebond";
                ps.println(lineaOUT);
                lineaOUT = "File=" + Tough2Viewer.dataobj.get_DataFileName();
                ps.println(lineaOUT);
                lineaOUT = "Path=" + Tough2Viewer.dataobj.get_DataFilePath();
                ps.println(lineaOUT);
                lineaOUT = "";
                for (int vColIndex = 0; vColIndex < titoli.length; vColIndex++) {
                    lineaOUT = lineaOUT + titoli[vColIndex] + ";";
                }
                ps.println(lineaOUT);
                for (int vRowIndex = 0; vRowIndex < n_time_step; vRowIndex++) {
                    lineaOUT = "";
                    for (int vColIndex = 0; vColIndex < n_colonne; vColIndex++) {
                        lineaOUT = lineaOUT + Double.toString(values[vRowIndex][vColIndex]) + ";";
                    }
                    ps.println(lineaOUT);
                }
                fos.close();
            } catch (IOException e)// Catches any error condition
            {
                String output = "Unable to write file";
                Tough2Viewer.toLogFile(output);
            }
        }

    }//GEN-LAST:event_jButtonExportActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelChart;
    private javax.swing.JPanel jPanelPlot;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
