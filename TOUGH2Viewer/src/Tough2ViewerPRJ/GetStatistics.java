/*
 * GetStatistics.java
 *
 * Created on 10 novembre 2009, 10.14
 */
package Tough2ViewerPRJ;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author stebond
 */
public class GetStatistics extends javax.swing.JDialog {

    /**
     * Creates new form GetStatistics
     */
//    public DesktopApplication1AboutBox(java.awt.Frame parent) {
//        super(parent);
//        initComponents();
//        getRootPane().setDefaultButton(closeButton);
//    }
    public GetStatistics() {
        super();
        initComponents();
        jPanel1.setLayout(new java.awt.BorderLayout());
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getVariableName()));
        String[] jComboBox2String = new String[Tough2Viewer.dataobj.get_TimeSteps() + 1];
        jComboBox2String[0] = "All";
        for (int i = 1; i < Tough2Viewer.dataobj.get_TimeSteps() + 1; i++) {
            jComboBox2String[i] = Float.toString(Tough2Viewer.dataobj.get_Times(i - 1));
        }
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(jComboBox2String));

        preparaTabella();
        riempiTabella();
        plotGraphXY();

        // Force the header to resize and repaint itself
        jTable1.getTableHeader().resizeAndRepaint();

    }

    private void riempiTabella() {
        //massimieminimi
        for (int i = 0; i < Tough2Viewer.dataobj.get_number_of_variables(); i++) {
            float variableMinMax[];
            variableMinMax = new float[4];
            String VariableNameUM[] = Tough2Viewer.dataobj.getVariablesUM();
            if (jComboBox2.getSelectedIndex() == 0) {
                Tough2Viewer.dataobj.minmaxDataArray(i, variableMinMax);

                jTable1.setValueAt(VariableNameUM[i], i, 1);
                jTable1.setValueAt(variableMinMax[0], i, 2);
                jTable1.setValueAt(variableMinMax[1], i, 3);

            } else {
                Tough2Viewer.dataobj.minmaxDataArray(jComboBox2.getSelectedIndex() - 1, i, variableMinMax);

                jTable1.setValueAt(VariableNameUM[i], i, 1);
                jTable1.setValueAt(variableMinMax[0], i, 2);
                jTable1.setValueAt(variableMinMax[1], i, 3);

            }

        }

    }

    private void plotGraphXY() {

        int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
        double y_of_x;
        String Title = " ";
        XYSeries series = new XYSeries(Title);

        int VariableY = jComboBox1.getSelectedIndex();
        if (VariableY < 0) {
            VariableY = 0;
        }
        String VariableXName = "";
        String VariableYName = "";
        String VariableName[] = Tough2Viewer.dataobj.getVariableName();
        String VariableNameUM[] = Tough2Viewer.dataobj.getVariablesUM();
        VariableYName = VariableName[VariableY] + " (" + VariableNameUM[VariableY] + ")";

        VariableXName = "t (S)";
        float variableMinMax[];
        variableMinMax = new float[4];
        for (int it = 0; it < timesteps; it++) {
            Tough2Viewer.dataobj.minmaxDataArray(it, jComboBox1.getSelectedIndex(), variableMinMax);
            y_of_x = variableMinMax[2];
            series.add((double) Tough2Viewer.dataobj.get_Times(it), y_of_x);
        }

        //Title="XY Scatter Plot";
        XYDataset dataset = new XYSeriesCollection(series);
        JFreeChart chart;

        chart = ChartFactory.createXYLineChart(
                Title,
                VariableXName,
                VariableYName,
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                false,
                false,
                false);

//Scientific notation
//    XYPlot myPlot = chart.getXYPlot();
//    ValueAxis axis = myPlot.getRangeAxis();
//
//    axis.setAutoRange(false);
//    double lower;
//    double upper;
//
//    lower=Tough2Viewer.dataobj.get_GlobalScale(VariableY, 0);
//    upper=Tough2Viewer.dataobj.get_GlobalScale(VariableY, 1);
//    axis.setRange(lower, upper);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);
        Tough2Viewer.dataobj.ScientificNotationAxis(plot);
        //        NumberAxis  XAxis=(NumberAxis)plot.getDomainAxis();
//        setScientificXAxis(XAxis);

        plot.setDomainGridlinePaint(Tough2Viewer.dataobj.GridAxisColor);
        plot.setRangeGridlinePaint(Tough2Viewer.dataobj.GridAxisColor);
        BasicStroke theGridLineStroke = (BasicStroke) plot.getDomainGridlineStroke();
        float linewidth = theGridLineStroke.getLineWidth();
        linewidth = linewidth * Tough2Viewer.dataobj.GridAxisThickness;
        float[] theDashArray = theGridLineStroke.getDashArray();
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
        ChartPanel panelchart1 = new ChartPanel(chart);

        panelchart1.setSize(jPanel1.getSize());
        panelchart1.setVisible(true);
        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.add(panelchart1, BorderLayout.CENTER);
        jPanel1.setVisible(true);
        jPanel1.repaint();
//

    }

    /**
     * Function to set up the X axis with scientific notation.
     */
//	public void setScientificXAxis() {
//		setScientificXAxis(new DecimalFormat("0.000E00"));
//	}
    /**
     * Function to set up the X axis with scientific notation. With the
     * specified number Format.
     *
     * @param inFormat the new formating for the X axis
     */
    public void setScientificXAxis(NumberAxis XAxis) {
        NumberFormat inFormat = new DecimalFormat(".000E00");
        XAxis.setNumberFormatOverride(inFormat);

    }

    private void creaGrafico() {
        int variabile = jComboBox1.getSelectedIndex();
        int timestep = jComboBox2.getSelectedIndex();
        String variabilenome = Tough2Viewer.dataobj.get_variables_name(variabile);
        IntervalXYDataset dataset = createDataset(variabile, timestep);
        JFreeChart chart = ChartFactory.createHistogram(
                variabilenome,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                false,//la legend? 
                false,
                false
        );
        XYPlot plot = chart.getXYPlot();
        plot.setDomainGridlinePaint(Tough2Viewer.dataobj.GridAxisColor);
        plot.setRangeGridlinePaint(Tough2Viewer.dataobj.GridAxisColor);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesStroke(0, new BasicStroke(Tough2Viewer.dataobj.LineThicness, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
        BasicStroke theGridLineStroke = (BasicStroke) plot.getDomainGridlineStroke();
        float linewidth = theGridLineStroke.getLineWidth();
        linewidth = linewidth * Tough2Viewer.dataobj.GridAxisThickness;
        float[] theDashArray = theGridLineStroke.getDashArray();
        int cap = theGridLineStroke.getEndCap();
        int join = theGridLineStroke.getLineJoin();
        float miterlimit = theGridLineStroke.getMiterLimit();
        float dashphase = theGridLineStroke.getDashPhase();
        for (int i = 0; i < theDashArray.length; i++) {
            theDashArray[i] = theDashArray[i] * Tough2Viewer.dataobj.StretchDashlines;
        }
        BasicStroke myGridLineStroke = new BasicStroke(linewidth, cap, join, miterlimit, theDashArray, dashphase);
        float offset = Tough2Viewer.dataobj.GridOffSet;
        plot.setAxisOffset(new RectangleInsets(offset, offset, offset, offset));
        plot.setDomainGridlineStroke(myGridLineStroke);
        plot.setRangeGridlineStroke(myGridLineStroke);
        plot.setBackgroundPaint(Tough2Viewer.dataobj.PlotAreaColor);
        plot.setRenderer(renderer);
        Tough2Viewer.dataobj.ScientificNotationAxis(plot);

        chart.getXYPlot().setForegroundAlpha(0.75f);
        ChartPanel panelchart = new ChartPanel(chart);

        Dimension mydim = new Dimension();
        jPanel1.getSize(mydim);
        panelchart.setSize(mydim);
        panelchart.setVisible(true);
        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.add(panelchart, BorderLayout.CENTER);
        jPanel1.setVisible(true);
        jPanel1.repaint();
    }

    private IntervalXYDataset createDataset(int variabile, int timestep) {
        HistogramDataset dataset = new HistogramDataset();
        if (timestep == 0) {
            int dimarray = Tough2Viewer.dataobj.get_nxyz() * Tough2Viewer.dataobj.get_TimeSteps();
            double[] values = new double[dimarray];// = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
            int z = 0;
            for (int i = 0; i < Tough2Viewer.dataobj.get_nxyz(); i++) {
                for (int j = 0; j < Tough2Viewer.dataobj.get_TimeSteps(); j++) {
                    values[z] = Tough2Viewer.dataobj.get_dataArray(i, j, variabile);
                    z = z + 1;
                }
            }
            String minS = jTable1.getValueAt(variabile, 2).toString();
            double min = Double.parseDouble(minS);
            String maxS = jTable1.getValueAt(variabile, 3).toString();
            double max = Double.parseDouble(maxS);
            dataset.addSeries("H1", values, 10, min, max);
        } else {
            int dimarray = Tough2Viewer.dataobj.get_nxyz();
            double[] values = new double[dimarray];// = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
            int z = 0;
            for (int i = 0; i < Tough2Viewer.dataobj.get_nxyz(); i++) {
                values[z] = Tough2Viewer.dataobj.get_dataArray(i, timestep - 1, variabile);
                z = z + 1;
            }

            String minS = jTable1.getValueAt(variabile, 2).toString();
            double min = Double.parseDouble(minS);
            String maxS = jTable1.getValueAt(variabile, 3).toString();
            double max = Double.parseDouble(maxS);
            dataset.addSeries("H1", values, 10, min, max);
        }

        return dataset;
    }

    private void preparaTabella() {
        int n_righe = jTable1.getRowCount();
        int n_colonne = jTable1.getColumnCount();
        String[] titoli = new String[]{
            "Variable", "Unit", "Min", "Max"};
        if (n_colonne < titoli.length) {
            DefaultTableModel model = new DefaultTableModel();

            model = (DefaultTableModel) jTable1.getModel();
            TableColumn col = new TableColumn(model.getColumnCount());
            // Ensure that auto-create is off
            if (jTable1.getAutoCreateColumnsFromModel()) {
                jTable1.setAutoCreateColumnsFromModel(false);
            }
            int delta = titoli.length - n_colonne;
            for (int vColIndex = 0; vColIndex < delta; vColIndex++) {
                col.setHeaderValue("Col3");
                jTable1.addColumn(col);
                model.addColumn("Col3", new Object[]{"v3"});
            }
        }
        for (int vColIndex = 0; vColIndex < titoli.length; vColIndex++) {
            jTable1.getColumnModel().getColumn(vColIndex).setHeaderValue(titoli[vColIndex]);
        }
        ///////////////////Riempire prima colonna

        if (n_righe < Tough2Viewer.dataobj.get_number_of_variables() + 1) {

            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable1.getModel();
            int delta = Tough2Viewer.dataobj.get_number_of_variables() + 1 - n_righe;
            for (int vRowIndex = 0; vRowIndex < delta; vRowIndex++) {
                model.addRow(new Object[]{"v1"});
            }

        }
        n_righe = jTable1.getRowCount();
        n_colonne = jTable1.getColumnCount();
        for (int vRowIndex = 0; vRowIndex < Tough2Viewer.dataobj.get_number_of_variables(); vRowIndex++) {
            jTable1.setValueAt(Tough2Viewer.dataobj.get_variables_name(vRowIndex), vRowIndex, 0);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Statistics");
        setName("Form123"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setHeaderValue("Title 1");
            jTable1.getColumnModel().getColumn(1).setHeaderValue("Title 2");
            jTable1.getColumnModel().getColumn(2).setHeaderValue("Title 3");
            jTable1.getColumnModel().getColumn(3).setHeaderValue("Title 4");
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Plot Area"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );

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

        jLabel1.setText("Variable:");

        jLabel2.setText("Time Filter:");

        jLabel3.setText("         ");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, 0, 139, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, 139, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
// TODO add your handling code here:
    riempiTabella();
    plotGraphXY();

}//GEN-LAST:event_jComboBox1ActionPerformed

private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
// TODO add your handling code here:
    riempiTabella();
    plotGraphXY();
}//GEN-LAST:event_jComboBox2ActionPerformed

private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    // TODO add your handling code here:

}//GEN-LAST:event_formWindowClosed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        creaGrafico();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}
