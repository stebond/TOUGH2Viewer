/*
 * XYScatterPlotRegular.java
 *
 * Created on 13 luglio 2009, 12.45
 */
package Tough2ViewerPRJ;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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
import javax.imageio.ImageIO;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import org.jfree.chart.axis.Axis;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author stebond
 */
public class XYScatterPlotVoronoi extends javax.swing.JFrame implements AxisChangeListener {

    public int actualposition;
    boolean initcomplete = false;
    ArrayList[] myindex = new ArrayList[3];
    int[] index;
    ArrayList combo = new ArrayList();
    String[] comboX;
    double[][] xy;
    Color3f colore[];
    JFreeChart chart;
    JFreeChart chart_advanced;

    /**
     * Creates new form XYScatterPlotRegular
     */
    public XYScatterPlotVoronoi(int position) {

        actualposition = position;
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
        float xo = Tough2Viewer.dataobj.get_Xo(actualposition);
        float yo = Tough2Viewer.dataobj.get_Yo(actualposition);
        float zo = Tough2Viewer.dataobj.get_Zo(actualposition);
        //found xmin,xmax,ymin,ymax,zmin,zmax..

        //
        if (Tough2Viewer.dataobj.ID_grid_type == 2) {
            float xmin_tmp = xo;
            float xmax_tmp = xo;
            float ymin_tmp = yo;
            float ymax_tmp = yo;
            float zmin_tmp = zo;
            float zmax_tmp = zo;

            int nxyz = Tough2Viewer.dataobj.get_nxyz();
            myindex[0] = new ArrayList();
            myindex[1] = new ArrayList();
            myindex[2] = new ArrayList();
            ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(position);
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
                if (xi >= xmin_tmp && xi <= xmax_tmp && yi >= ymin_tmp && yi <= ymax_tmp) {
                    myindex[2].add(i_b);//THE "VERTICAL PROFILE"
                }
                if (zi >= zmin_tmp && zi <= zmax_tmp && xi >= xmin_tmp && xi <= xmax_tmp) {
                    myindex[1].add(i_b);//ALONG Y
                }
                if (yi >= ymin_tmp && yi <= ymax_tmp && zi >= zmin_tmp && zi <= zmax_tmp) {
                    myindex[0].add(i_b);//Along X
                }
            }
        } else {
            int nxyz = Tough2Viewer.dataobj.get_nxyz();
            myindex[0] = new ArrayList();
            myindex[1] = new ArrayList();
            myindex[2] = new ArrayList();
            //index=new int[nxyz];
            for (int i_b = 0; i_b < nxyz; i_b++) {
                float xi = Tough2Viewer.dataobj.get_Xo(i_b);
                float yi = Tough2Viewer.dataobj.get_Yo(i_b);
                float zi = Tough2Viewer.dataobj.get_Zo(i_b);
                if (xi == xo) {
                    if (yi == yo) {

                        myindex[2].add(i_b);
                    }

                }
                if (zi == zo) {
                    if (yi == yo) {
                        myindex[0].add(i_b);
                    }

                }
                if (zi == zo) {
                    if (xi == xo) {
                        myindex[1].add(i_b);
                    }

                }
            }
        }

        combo.add("x");
        combo.add("y");
        combo.add("z");
        combo.add("t");
        int dimcomboX = combo.size();
        comboX = new String[dimcomboX];
        for (int i = 0; i < dimcomboX; i++) {
            comboX[i] = (String) combo.get(i);
        }
        jComboBox_X.setModel(new javax.swing.DefaultComboBoxModel(comboX));
        initcomplete = true;
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getVariableName()));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getVariableName()));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(Tough2Viewer.dataobj.getVariableName()));
        plotGraphXY();

    }

    public void axisChanged(AxisChangeEvent ace) {
        Axis axis = ace.getAxis();
        if (axis instanceof NumberAxis) {

            updateBackGround();

        }
    }

    private void updateBackGround() {

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundImage(createImageBackGround());

    }

    private BufferedImage createImageBackGround() {
        int VariableX = jComboBox_X.getSelectedIndex();
        int h = 2000;
        int w = 100;
        BufferedImage img;
        if (jCheckBox1.isSelected()) {
            img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        } else {
            img = new BufferedImage(h, w, BufferedImage.TYPE_INT_RGB);
        }

        img.createGraphics();
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setColor(Tough2Viewer.dataobj.PlotAreaColor);
        XYPlot myPlot = chart.getXYPlot();
        NumberAxis XAxis = (NumberAxis) myPlot.getDomainAxis();
        Range domainRange = XAxis.getRange();
        double zmin = domainRange.getLowerBound();
        double zmax = domainRange.getUpperBound();
        double zfactor = (double) h / (zmax - zmin);
        if (jCheckBox1.isSelected()) {
            g.fillRect(0, 0, w, h);
        } else {
            g.fillRect(0, 0, h, w);
        }
        if (jCheckBox3.isSelected()) {

            double z_node = 0;
            if (VariableX <= 2) {
                for (int iz = 0; iz < myindex[VariableX].size(); iz++) {
                    //prendo gli indici della colonna..
                    int i_b = (Integer) myindex[VariableX].get(iz);
                    if (VariableX == 0) {
                        z_node = (double) Tough2Viewer.dataobj.get_Xo(i_b);
                    }
                    if (VariableX == 1) {
                        z_node = (double) Tough2Viewer.dataobj.get_Yo(i_b);
                    }

                    if (VariableX == 2) {
                        z_node = (double) Tough2Viewer.dataobj.get_Zo(i_b);
                    }

                    double xo = 0.0f;
                    double yo;
                    if (jCheckBox1.isSelected()) {
                        yo = (zmax - z_node - (double) Tough2Viewer.dataobj.get_DimBlockZ(i_b) / 2) * zfactor;
                    } else {
                        yo = (-zmin + z_node - (double) Tough2Viewer.dataobj.get_DimBlockZ(i_b) / 2) * zfactor;
                    }

                    double h1 = ((double) Tough2Viewer.dataobj.get_DimBlockZ(i_b)) * zfactor;
                    double w1 = w;
                    colore = Tough2Viewer.dataobj.getColo3fRocksTypes();
                    int color_index = Tough2Viewer.dataobj.get_RockType(i_b);
                    Color thecolor = new Color((int) (colore[color_index].x * 255.0f), (int) (colore[color_index].y * 255.0f), (int) (colore[color_index].z * 255.0f));
                    g.setColor(thecolor);
                    if (jCheckBox1.isSelected()) {
                        g.fillRect((int) Math.round(xo), (int) Math.round(yo), (int) Math.round(w1), (int) Math.round(h1));
                    } else {
                        g.fillRect((int) Math.round(yo), (int) Math.round(xo), (int) Math.round(h1), (int) Math.round(w1));
                    }

                }

            }
        }
        File outputFile = new File("image.png");
        try {
            ImageIO.write(img, "PNG", outputFile);
        } catch (Exception e) {

        }

        return img;
    }

    private void plotGraphXY() {
        if (initcomplete) {
            int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
            double y_of_x;
            String Title = "";
            XYSeries series = new XYSeries(Title);
            int VariableX = jComboBox_X.getSelectedIndex();
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
            if (VariableX < 3) {
                Title = "Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(jSlider1.getValue())) + " S";
                VariableXName = comboX[VariableX] + " (m)";
                xy = new double[myindex[VariableX].size()][2];
                for (int iz = 0; iz < myindex[VariableX].size(); iz++) {
                    int i_b = (Integer) myindex[VariableX].get(iz);
                    y_of_x = (double) Tough2Viewer.dataobj.get_dataArray(i_b, time, VariableY);
                    double x = 0;
                    if (VariableX == 0) {
                        x = (double) Tough2Viewer.dataobj.get_Xo(i_b);
                    }
                    if (VariableX == 1) {
                        x = (double) Tough2Viewer.dataobj.get_Yo(i_b);
                    }
                    if (VariableX == 2) {
                        x = (double) Tough2Viewer.dataobj.get_Zo(i_b);
                    }
                    xy[iz][0] = x;
                    xy[iz][1] = y_of_x;
                    series.add(x, y_of_x);
                }
            } else if (VariableX == 3) {
                Title = Tough2Viewer.dataobj.get_BlockName(actualposition) + " ";
                VariableXName = "t (S)";
                xy = new double[timesteps][2];
                for (int it = 0; it < timesteps; it++) {
                    int i_b = actualposition;
                    y_of_x = (double) Tough2Viewer.dataobj.get_dataArray(i_b, it, VariableY);
                    double t = (double) Tough2Viewer.dataobj.get_Times(it);
                    xy[it][0] = t;
                    xy[it][1] = y_of_x;
                    series.add(t, y_of_x);
                }
            }

            jLabel4.setText(Integer.toString(actualposition));
            jLabel6.setText(Tough2Viewer.dataobj.get_BlockName(actualposition));
            jLabel8.setText(Float.toString((Tough2Viewer.dataobj.get_Xo(actualposition))));
            jLabel10.setText(Float.toString((Tough2Viewer.dataobj.get_Yo(actualposition))));
            jLabel12.setText(Float.toString((Tough2Viewer.dataobj.get_Zo(actualposition))));
            int rocktype_n = Tough2Viewer.dataobj.get_RockType(actualposition);
            String rocktype = Tough2Viewer.dataobj.get_RockType_name(rocktype_n);
            jLabel14.setText(rocktype);
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
            Shape myNewShape;
            if (Tough2Viewer.dataobj.seriesShape == 0) {
                renderer.setSeriesShape(0, new Ellipse2D.Double(-Width, -Height, 2 * Width, 2 * Height));
            } else {
                renderer.setSeriesShape(0, new Rectangle2D.Double(-Width, -Height, 2 * Width, 2 * Height));
            }
            //renderer.setUseOutlinePaint(true);

            renderer.setSeriesPaint(0, Tough2Viewer.dataobj.SeriesColor);
            if (Tough2Viewer.dataobj.fillSeriesShape) {
                renderer.setUseFillPaint(true);
            } else {
                renderer.setUseFillPaint(false);
            }
//(cmbia colore alla serie
//        renderer.setSeriesPaint(1, Color.blue);  
//        renderer.setSeriesPaint(2, Color.blue); 

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
            if (jCheckBox3.isSelected()) {
                plot.setBackgroundImage(createImageBackGround());
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
//

        }
    }

    private void plotGraphXY2() {
        int n_time_step = Tough2Viewer.dataobj.get_TimeSteps();
        int n_series = 2;
        int x_variable = jComboBox1.getSelectedIndex();
        int y_variable = jComboBox2.getSelectedIndex();
        int y1_variable = jComboBox3.getSelectedIndex();

        String Title = Tough2Viewer.dataobj.get_BlockName(actualposition);
        XYSeries[] series = new XYSeries[n_series];
        series[0] = new XYSeries((String) jComboBox2.getSelectedItem());
        series[1] = new XYSeries((String) jComboBox3.getSelectedItem());

        String VariableXName = (String) jComboBox1.getSelectedItem();
        String VariableYName = (String) jComboBox2.getSelectedItem();
        String VariableY1Name = (String) jComboBox3.getSelectedItem();
        for (int it = 0; it < n_time_step; it++) {

            float x = Tough2Viewer.dataobj.get_dataArray(actualposition, it, x_variable);
            float y = Tough2Viewer.dataobj.get_dataArray(actualposition, it, y_variable);
            float y1 = Tough2Viewer.dataobj.get_dataArray(actualposition, it, y1_variable);

            series[0].add(x, y);
            series[1].add(x, y1);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < n_series; i++) {
            dataset.addSeries(series[i]);
        }

        chart_advanced = ChartFactory.createXYLineChart(
                Title,
                VariableXName,
                VariableYName,
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                false,
                false);

        XYPlot plot = chart_advanced.getXYPlot();

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
        ChartPanel panelchart1 = new ChartPanel(chart_advanced);

        panelchart1.setSize(jPanel5.getSize());
        panelchart1.setVisible(true);

        jPanel5.removeAll();
        jPanel5.repaint();
        jPanel5.add(panelchart1, BorderLayout.CENTER);
        jPanel5.setVisible(true);
        jPanel5.getParent().validate();
        jPanel5.repaint();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jComboBox_X = new javax.swing.JComboBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jComboBox_Y = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Profiles");

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setAutoscrolls(true);
        jPanel1.setOpaque(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 894, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("10");

        jLabel16.setText("Domain");

        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("12");

        jLabel11.setText("Zo=");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("4");

        jLabel9.setText("Yo=");

        jButton1.setText("ExportAllData");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("8");

        jLabel7.setText("Xo=");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Invert X with Y");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jComboBox_X.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "z", "t" }));
        jComboBox_X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_XActionPerformed(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("MarkPoint");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("6");

        jLabel3.setText("i=");

        jButton2.setText("ExportCurrentData");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText("BlockName");

        jComboBox_Y.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_YActionPerformed(evt);
            }
        });

        jLabel15.setText("Variable");

        jLabel13.setText("RockType");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("RockType");

        jCheckBox3.setText("BackGround Material");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBox_Y, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox_X, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(45, 45, 45)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jCheckBox2)
                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(jCheckBox3))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_Y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_X, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(44, 44, 44)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox2)
                        .addGap(2, 2, 2)
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2))
                    .addComponent(jLabel14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Classical profiles", jPanel3);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Plot"));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 790, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 467, Short.MAX_VALUE)
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton3.setText("Update plot");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(359, 359, 359)
                        .addComponent(jButton3)))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(248, 248, 248)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Advanced", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonClose)
                .addGap(325, 325, 325))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonClose)
                        .addGap(12, 12, 12))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jComboBox_YActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_YActionPerformed
// TODO add your handling code here:

    plotGraphXY();
}//GEN-LAST:event_jComboBox_YActionPerformed

private void jComboBox_XActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_XActionPerformed
// TODO add your handling code here:
    plotGraphXY();
}//GEN-LAST:event_jComboBox_XActionPerformed

private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
    int timestep = jSlider1.getValue();
    String Tooltip = Float.toString(Tough2Viewer.dataobj.get_Times(timestep)) + " S";
    jSlider1.setToolTipText(Tooltip);
    plotGraphXY();
}//GEN-LAST:event_jSlider1StateChanged

private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    // TODO add your handling code here:
    plotGraphXY();
}//GEN-LAST:event_jCheckBox1ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
    final JFileChooser fc = new JFileChooser(FilePathString);
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
            int VariableX = jComboBox_X.getSelectedIndex();
            int VariableY = jComboBox_Y.getSelectedIndex();
            //aggingere ciclo per beccare la colonna o la rigaX o la rigaY
            if (VariableY < 0) {
                VariableY = 0;
            }
            String VariableXName[] = {"x", "y", "z", "t"};
            String VariableYName = "";
            String values[] = Tough2Viewer.dataobj.getVariableName();
            String valuesUM[] = Tough2Viewer.dataobj.getVariablesUM();
            VariableYName = values[VariableY];
            String lineaOUT;
            lineaOUT = "File generated by iTough2Viewer - by Stebond";
            ps.println(lineaOUT);
            lineaOUT = "File=" + Tough2Viewer.dataobj.get_DataFileName();
            ps.println(lineaOUT);
            if (VariableX < 3) {
                String Title = "Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(jSlider1.getValue())) + "S";

                lineaOUT = VariableXName[VariableX] + ";BlockName;";
                for (int it = 0; it < timesteps; it++) {
                    for (int in = 0; in < numvar; in++) {
                        //lineaOUT=lineaOUT+values[in]+Integer.toString(it)+";";
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
//writeOUT lineaOUT
                ps.println(lineaOUT);

                lineaOUT = "";
                for (int i_b = 0; i_b < myindex[VariableX].size(); i_b++) {
                    int ii = (Integer) myindex[VariableX].get(i_b);
                    if (VariableX == 0) {
                        x = Tough2Viewer.dataobj.get_Xo(ii);
                    }
                    if (VariableX == 1) {
                        x = Tough2Viewer.dataobj.get_Yo(ii);
                    }
                    if (VariableX == 2) {
                        x = Tough2Viewer.dataobj.get_Zo(ii);
                    }

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
                //write out lineaOUT

            } else if (VariableX == 3)//tempo
            {
                //mancaintestazione sul file(spiegazionevariabili)
                lineaOUT = "Values for Block=" + Tough2Viewer.dataobj.get_BlockName(actualposition);
                ps.println(lineaOUT);

                lineaOUT = "time;";
                for (int in = 0; in < numvar; in++) {
                    lineaOUT = lineaOUT + values[in] + ";";
                }
                ps.println(lineaOUT);
                lineaOUT = "day;";
                for (int in = 0; in < numvar; in++) {
                    lineaOUT = lineaOUT + valuesUM[in] + ";";
                }
                for (int it = 0; it < timesteps; it++) {
                    x = Tough2Viewer.dataobj.get_Times(it);
                    lineaOUT = Float.toString(x) + ";";
                    for (int in = 0; in < numvar; in++) {
                        int i_b = actualposition;
                        y_of_x = Tough2Viewer.dataobj.get_dataArray(i_b, it, in);
                        lineaOUT = lineaOUT + Float.toString(y_of_x) + ";";
                    }
                    ps.println(lineaOUT);
                }
            }
            fos.close();
        } catch (IOException e)// Catches any error conditions
        {
            String output = "Unable to write file";
            Tough2Viewer.toLogFile(output);
            //System.exit(-1);
        }
}//GEN-LAST:event_jButton1ActionPerformed
    }
private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
    plotGraphXY();
}//GEN-LAST:event_jCheckBox2ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    String FilePathString = Tough2Viewer.dataobj.get_WorkingPath();
    final JFileChooser fc = new JFileChooser(FilePathString);
    String filename = "XY_values.dat";
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
            int VariableX = jComboBox_X.getSelectedIndex();
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
            lineaOUT = "File=" + Tough2Viewer.dataobj.get_DataFileName() + " Time= " + Float.toString(Tough2Viewer.dataobj.get_Times(jSlider1.getValue())) + "S";
            lineaOUT = lineaOUT + "Block=" + Tough2Viewer.dataobj.get_BlockName(actualposition);
            ps.println(lineaOUT);
            String VariableName[] = Tough2Viewer.dataobj.getVariableName();
            String VariableNameUM[] = Tough2Viewer.dataobj.getVariablesUM();
            VariableYName = VariableName[VariableY] + " (" + VariableNameUM[VariableY] + ")";
            if (VariableX < 3) {
                VariableYName = VariableName[VariableY] + " (" + VariableNameUM[VariableY] + ")";
                lineaOUT = "m;" + variableName[var] + "(" + valuesUM[var] + ");";
                ps.println(lineaOUT);
                lineaOUT = "";
                int n = xy.length;
                for (int i = 0; i < n; i++) {
                    lineaOUT = lineaOUT + Double.toString(xy[i][0]) + ";" + Double.toString(xy[i][1]);
                    ps.println(lineaOUT);
                    lineaOUT = "";
                }
            } else if (VariableX == 3) {
                //mancaintestazione sul file(spiegazionevariabili)
                lineaOUT = "Values for Block=" + Tough2Viewer.dataobj.get_BlockName(actualposition);
                ps.println(lineaOUT);
                lineaOUT = "time;";
                lineaOUT = lineaOUT + variableName[var] + ";";
                ps.println(lineaOUT);
                lineaOUT = "day;";
                lineaOUT = lineaOUT + valuesUM[var] + ";";
                for (int it = 0; it < timesteps; it++) {
                    x = Tough2Viewer.dataobj.get_Times(it);
                    lineaOUT = Float.toString(x) + ";";
                    int i_b = actualposition;
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

        updateBackGround();

    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        plotGraphXY2();

    }//GEN-LAST:event_jButton3ActionPerformed

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
    private javax.swing.JButton jButtonClose;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox jComboBox_X;
    private javax.swing.JComboBox jComboBox_Y;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}
