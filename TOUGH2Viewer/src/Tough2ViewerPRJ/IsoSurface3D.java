/*
VisAD Tutorial
Copyright (C) 2000 Ugo Taddei
 */
package Tough2ViewerPRJ;

// Import needed classes
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import visad.*;
import visad.util.*;
import visad.java3d.DisplayImplJ3D;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;

/**
 * VisAD Tutorial example 4_13 Use ContourWidget with 3D data We have the
 * function rgbVal = h(RED, GREEN, BLUE)
 *
 * represented by the MathType ( (RED, GREEN, BLUE) -> rgbVal ) Run program with
 * "java P4_13"
 */
public class IsoSurface3D {

    // Declare variables
    // The domain quantities longitude and latitude
    // and the dependent quantity rgbVal
    private RealType red, green, blue;
    private RealType rgbVal;

    // Tuple to pack longitude and latitude together
    private RealTupleType domain_tuple;

    // The function (domain_tuple -> rgbVal )
    private FunctionType func_domain_rgbVal;

    // Our Data values for the domain are represented by the Set
    private Set domain_set;

    // The Data class FlatField
    private FlatField vals_ff;

    // The DataReference from data to display
    private DataReferenceImpl data_ref;

    // The 2D display, and its the maps
    DisplayImpl display;
    private ScalarMap redXMap, greenYMap, blueZMap;
    private ScalarMap rgbMap, rgbIsoMap;
    private ContourWidget contWid;
    int NCOLS;
    int NROWS;
    int NLEVS;
    double[][][][] temp;
    JFrame jframe;
    String variablename;
    String timeis;
    IsoSurfaceProperties prop = null;

    public IsoSurface3D(int nx, int ny, int nz, int neigh) throws RemoteException, VisADException {

        // Create the quantities
        // Use RealType(String name, Unit unit, Set set);
        JSlider slider;
        red = RealType.getRealType("X", null, null);
        green = RealType.getRealType("Y", null, null);
        blue = RealType.getRealType("Z", null, null);
        domain_tuple = new RealTupleType(red, green, blue);
        // The independent variable
        int actual_time_to_plot = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int actual_variable_to_plot = Tough2Viewer.dataobj.get_actualVariableToPlot();
        variablename = Tough2Viewer.dataobj.get_variables_name(actual_variable_to_plot);

        String newVariableName;

        newVariableName = variablename.replace('.', '_');
        timeis = Float.toString(Tough2Viewer.dataobj.get_Times(0));
        rgbVal = RealType.getRealType(newVariableName, null, null);
        // Create a FunctionType (domain_tuple -> range_tuple )
        // Use FunctionType(MathType domain, MathType range)
        func_domain_rgbVal = new FunctionType(domain_tuple, rgbVal);
        // Create the domain Set
        // Integer3DSet(MathType type, int lengthX, int lengthY, int lengthZ)
        NCOLS = nx;
        NROWS = ny;
        NLEVS = nz;
        float xmin = Tough2Viewer.dataobj.get_xmin();
        float xmax = Tough2Viewer.dataobj.get_xmax();
        float ymin = Tough2Viewer.dataobj.get_ymin();
        float ymax = Tough2Viewer.dataobj.get_ymax();
        float zmin = Tough2Viewer.dataobj.get_zmin();
        float zmax = Tough2Viewer.dataobj.get_zmax();
        float lx = xmax - xmin;
        float ly = ymax - ymin;
        float lz = zmax - zmin;
        float lmax = lx;
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
        // Our 'flat' array
        double[][] flat_samples = new double[1][NCOLS * NROWS * NLEVS];
        // Fill our 'flat' array with the rgbVal values
        // by looping over NCOLS and NROWS
        // but first get the Set samples to help with the calculations
        double stat[] = new double[4];
        Date x = new Date();
        long start = x.getTime();
        temp = Tough2Viewer.dataobj.makeTough2Data4D(neigh, NROWS, NCOLS, NLEVS, stat);
        Date x2 = new Date();

        long stop = x2.getTime();
        long duration = stop - start;
        System.out.println(duration);
        // Note the use of an index variable, indicating the order of the samples
        domain_set = new Linear3DSet(domain_tuple, xmin, xmax, NROWS,
                ymin, ymax, NCOLS,
                zmin, zmax, NLEVS);
        float[][] set_samples = domain_set.getSamples(true);//QUESTA SERviva nell?
        int index = 0;
        for (int l = 0; l < NLEVS; l++) {
            for (int c = 0; c < NCOLS; c++) {
                for (int r = 0; r < NROWS; r++) {
                    // set value for RealType rgbVal
                    flat_samples[0][index] = (float) temp[c][r][l][0];
                    // increment index
                    index++;
                }
            }
        }

        //    20/03/2012
        //    float[] values = ... // values for 'value' RealType
        //  // repeat same values for 'value' and 'value2'
        //  float[][] field_values = {values, values};
        //  field.setSamples(field_values, false); // 'false' for don't copy
        // Create a FlatField
        // Use FlatField(FunctionType type, Set domain_set)
        vals_ff = new FlatField(func_domain_rgbVal, domain_set);
        // ...and put the rgbVal values above into it
        // Note the argument false, meaning that the array won't be copied
        vals_ff.setSamples(flat_samples, false);
        // Create Display and its maps
        // This is new: a 3D display
        display = new DisplayImplJ3D("display1");
        // Get display's graphics mode control and draw scales
        GraphicsModeControl dispGMC = (GraphicsModeControl) display.getGraphicsModeControl();
        dispGMC.setScaleEnable(true);
        dispGMC.setProjectionPolicy(DisplayImplJ3D.PARALLEL_PROJECTION);
        if (Tough2Viewer.dataobj.isosurfacePerspective) {
            dispGMC.setProjectionPolicy(DisplayImplJ3D.PERSPECTIVE_PROJECTION);
        }
        //Put aspect ratio
        ProjectionControl projCont = display.getProjectionControl();
        double[] aspect = new double[]{lx / lmax, ly / lmax, lz / lmax};
        projCont.setAspect(aspect);
        // Create the ScalarMaps: latitude to XAxis, longitude to YAxis and
        // rgbVal to ZAxis and to RGB
        // Use ScalarMap(ScalarType scalar, DisplayRealType display_scalar)
        redXMap = new ScalarMap(red, Display.XAxis);
        greenYMap = new ScalarMap(green, Display.YAxis);
        blueZMap = new ScalarMap(blue, Display.ZAxis);
        rgbMap = new ScalarMap(rgbVal, Display.RGB);
        // The IsoContour map to be used with the widget
        rgbIsoMap = new ScalarMap(rgbVal, Display.IsoContour);
        // Add maps to display

        display.addMap(redXMap);
        display.addMap(greenYMap);
        display.addMap(blueZMap);
        display.addMap(rgbMap);
        display.addMap(rgbIsoMap);
        // Create a data reference and set the FlatField as our data
        data_ref = new DataReferenceImpl("data_ref");
        data_ref.setData(vals_ff);
        // Add reference to display
        display.addReference(data_ref);
        double min = stat[0];
        double max = stat[1];

        //blueZMap.setRange(stat[0], stat[1] );
        // Create ContourWidget
        ContourControl RangeControl = (ContourControl) rgbIsoMap.getControl();
        double[] myrange = rgbIsoMap.getRange();
        myrange[0] = min;
        myrange[1] = max;
        rgbIsoMap.setRange(min, max);
        //qui forzo o min e i max..chissa' se funzia

        contWid = new ContourWidget(rgbIsoMap);

        // Create application window and add display to window
        jframe = new JFrame("IsoSurface for " + variablename + " at " + timeis + " S");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentY(JPanel.TOP_ALIGNMENT);
        panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        jframe.getContentPane().add(panel);
        // add s l i d e r and di spl a y to JPanel
        panel.add(display.getComponent());
        panel.add(contWid);
        // Set window size and make it visible

        ////
        slider = new JSlider(JSlider.HORIZONTAL, 0, 2, 0);
        slider.setPaintTicks(true);

        slider.setMinimum(0);
        slider.setMaximum(Tough2Viewer.dataobj.get_TimeSteps() - 1);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setValue(0);
        Font font = new Font("Serif", Font.ITALIC, 15);
        slider.setFont(font);
        slider.setSnapToTicks(true);
        Hashtable labelTable = new Hashtable();
        for (int i = 0; i < Tough2Viewer.dataobj.get_TimeSteps(); i++) {
            String myTickLabel = String.valueOf(i);
            labelTable.put(i, new JLabel(myTickLabel));
        }
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        String Tooltip = Float.toString(Tough2Viewer.dataobj.get_Times(0)) + " S";
        slider.setToolTipText(Tooltip);
        slider.addChangeListener(new SliderListener());

//        slider.setUnitIncrement(1);
        panel.add(slider);
        JButton b1;
        b1 = new JButton("Properties");
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.CENTER);
        b1.addActionListener(new ButtonListener());
        panel.add(b1);

        JButton b2;
        b2 = new JButton("Close");
        b2.setVerticalTextPosition(AbstractButton.CENTER);
        b2.setHorizontalTextPosition(AbstractButton.CENTER);
        b2.addActionListener(new ButtonListener2());
        b2.setSize(b1.getSize());
        panel.add(b2);

        ////
        jframe.setSize(350, 450);
        jframe.setVisible(true);
    }

    public void change_background(Color3f c) throws RemoteException, VisADException {
        DisplayRenderer dRenderer = display.getDisplayRenderer();
        dRenderer.setBackgroundColor(c.x, c.y, c.z);
    }

    public void change_boxcolor(Color3f c) throws RemoteException, VisADException {
        DisplayRenderer dRenderer = display.getDisplayRenderer();
        dRenderer.setForegroundColor(c.x, c.y, c.z);
    }

    public void close() {
        jframe.setVisible(false);
    }

    public void show_prop() {
        if (prop == null) {
            prop = new IsoSurfaceProperties(this);
        }
        prop.setVisible(true);
    }

    class SliderListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int timestep = (int) source.getValue();
                String Tooltip = Float.toString(Tough2Viewer.dataobj.get_Times(source.getValue())) + " S";
                source.setToolTipText(Tooltip);
                int index = 0;
                double[][] flat_samples = new double[1][NCOLS * NROWS * NLEVS];
                for (int l = 0; l < NLEVS; l++) {
                    for (int c = 0; c < NCOLS; c++) {
                        for (int r = 0; r < NROWS; r++) {
                            // set value for RealType rgbVal
                            flat_samples[0][index] = (float) temp[c][r][l][timestep];
                            // increment index
                            index++;
                        }
                    }
                }
                try {
                    vals_ff.setSamples(flat_samples, false);
                    timeis = Float.toString(Tough2Viewer.dataobj.get_Times(timestep));
                    jframe.setTitle("IsoSurface for " + variablename + " at " + timeis + " S");
//                contWid = new ContourWidget(rgbIsoMap);
                } catch (Exception ee) {

                }

            }
        }
    }

    class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            show_prop();

        }
    }

    class ButtonListener2 implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            close();
        }
    }
} //end of Visad Tutorial Program 4_13
