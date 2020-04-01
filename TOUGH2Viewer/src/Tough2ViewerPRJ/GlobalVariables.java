/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author stebond
 */
public class GlobalVariables {

    float big_float = 1.0e38f;
    private int nx = 0;
    private int ny = 0;
    private int nz = 0;
    private int nxyz = 0;
    private float[][][] dataArray;
    private String[] BlockNames;
    private String[] BlockNames_ordered;
    private float xmin, xmax, ymin, ymax, zmin, zmax;
    double xmin_node = +1.0E50;
    double xmax_node = -1.0E50;
    double ymin_node = +1.0E50;
    double ymax_node = -1.0E50;
    double zmin_node = +1.0E50;
    double zmax_node = -1.0E50;
    private boolean dataLoaded = false;
    private float deltaZGV = 4;
    private int TimeSteps;
    private float Times[];
    private int nxy = 0;//for voronoi layer number of layer=nz
    private String DataFilePath;
    private String DataFileName;
    private String InconFileName;
    private String SegmtFileName;
    private String InFileName;
    private float[] ThicknessBlock;
    private float[] DimBlockX;
    private float[] DimBlockY;
    private float[] DimBlockZ;
    public boolean[] Block_is_selected;

    private int number_of_variables = -1;
    private int number_of_flow_variables = -1;
    private boolean KperActivate = false;
    private String[] variables_name_eos;

    private String[] variables_unit_eos;

    private String[] FLOW_name_eos;
    private String[] FLOW_variables_unit_eos;
    private float VectorFlowLenght = .1f;

    public String[] grid_type = {"Structured grid", "Unstructured grid", "Voronoi grid"};
    public int ID_grid_type;//0=STRUCTURED,1=UNSTUCTURED,2=RADIAL
    private float[] Xo;
    private float[] Yo;
    private float[] Zo;
    private double[] BlockVolume;
    float[] Block_n_conne;
    private int actualVariableToPlot = 0;
    private int actualFluxToPlot = 0;
    private int actualTimeToPlot = 0;
    private float actuaZToPlot;
    private float globalStatistics[][];
    private float GlobalScale[][];
    private float VectorModuleStatistics[];
    private int n_connections = 0;
    private float ConnectionData[][];//[index][0=teta]
    private int ConnectionBlocks[][];//[index][0=block1][0=block2]
    private float[][][] FluxDataArray;
    private float[][][] VectorDataArray;
    private float ROI_xmin, ROI_ymin, ROI_xmax, ROI_ymax, ROI_zmin, ROI_zmax;
    private float HideROI_xmin = 1.0f, HideROI_ymin = 1.0f, HideROI_xmax = 0.0f, HideROI_ymax = 0.0f,
            HideROI_zmin = 1.0f, HideROI_zmax = 0.0f;
    private boolean useHideROI = false;
    private Color3f colore[];//=new Color3f[240];
    public Color3f selection_color = new Color3f(1.0f, 1.0f, 1.0f);
    private Color3f[] colore1 = new Color3f[101];
    private int scalascelta = 0;
    private ArrayList voronoiGeometryBox[];
    public ArrayList rocksnames = new ArrayList();
    public int[] RockType;
    private int selectedindex = -1;
    private boolean xoyoloaded = false;
    private boolean voronoi = false;
    private String WorkingPath = null;
    private boolean useMoreRealisticColor = true;
    private ArrayList Gener = new ArrayList();
    public boolean[] rocktype_visible;
    public ArrayList Surfaces_Data = new ArrayList();
    private ArrayList shapes = new ArrayList();
    private ArrayList shapesColor = new ArrayList();
    private ArrayList PolyShapesVisible = new ArrayList();
    private ArrayList shapesFileName = new ArrayList();
    private ArrayList shapesSurface = new ArrayList();
    private ArrayList SurfaceShapesVisible = new ArrayList();
    private ArrayList SurfaceShapesVisiblePLY = new ArrayList();
    public ArrayList myPlyFiles = new ArrayList();
    private double contrazione = 0.85d;
    private double Zfactor = 1.0;
    private boolean hideoutofrange = false;
    private boolean normalizeVector = true;
    String gridType;
    private double exp_factor_estimation = 1.0f;
    private String[] SciNotX = {"0.0E00", "0.00E00", "0.000E00", "0.000E00", "0.0000E00", "0.0E000", "0.00E000", "0.000E000", "0.000E000", "0.0000E000"};
    private String[] SciNotY = {"0.0E00", "0.00E00", "0.000E00", "0.000E00", "0.0000E00", "0.0E000", "0.00E000", "0.000E000", "0.000E000", "0.0000E000"};
    private int SciNotIndexX = 0;
    private int SciNotIndexY = 0;
    private boolean UseScientificNotationX = true;
    private boolean UseScientificNotationY = true;
    private ArrayList[][][] cubecontainer = null;
    private int ni = 10;
    private int nj = 10;
    private int nk = 10;
    private boolean cubecontainerdone = false;
    int searchmethod = 0;//0=hipercube,1=bruteforce
    private int[][] ConnectionTable = new int[0][0];
    private boolean ConnectionTableToBeCreate = true;
    private boolean blockname_ordered = false;
    boolean FluxFound = false;
    private float[] InterfaceArea;
    private int[] FlowOrVel;//if flow=1,  else =0 (vel)
    boolean isosurfacePerspective = false;
    float LineThicness = 1.0f;
    double ShapeDimension = (double) 1.0f;
    float GridAxisThickness = 1.0f;
    Color GridAxisColor = Color.darkGray;
    float StretchDashlines = 1.0f;
    Color PlotAreaColor = Color.white;
    float GridOffSet = 5.0f;
    Color SeriesColor = Color.red;
    int seriesShape = 0;
    boolean fillSeriesShape = false;
    private ArrayList balla = new ArrayList();
    boolean[] plotFlow = {false, false, true, false};
    boolean[] light_3D = {false, false, true, false};
    private int[] Indexs;
    private float campo = 1.0f;
    private boolean FlowError = false;
    ArrayList VoroPPData;
    boolean logaritmic_scale = false;
    boolean use_multiProcessors = true;
    boolean axis3dvisible = true;
    int MultiSelectionLevel = 2;
    Font ScaleFont = new Font("SansSerif", Font.PLAIN, 16);
    String FormatDouble = "%8.3f";
    boolean mesh_file_is_loaded = false;
    int eleme_lenght = 5;//DEFAULT TOUGH2 value. IF MOMOP(2)>5 then eleme_lenght=MOMOP(2)
    int scale_color_orientation = 0;//0:growth from bottom to top; 1:from top to bottom
    Color3f bgColor = new Color3f(1, 1, 1);
    ArrayList conneArrayList;
    ArrayList tough2viewerdatfile;
    String tough2viewer_dat_file_name = "";

    GlobalVariables() {
        //constructor

    }
    /**
     * 
     * @param value 
     */
    void set_VectorFlowLenght(Float value) {
        VectorFlowLenght = value;
    }

    float get_VectorFlowLenght() {
        return VectorFlowLenght;
    }

    void set_logaritmicscale(boolean value) {
        logaritmic_scale = value;
    }

    boolean get_logaritmicscale() {
        return logaritmic_scale;
    }

    void ScientificNotationAxis(XYPlot plot) {
        NumberAxis XAxis = (NumberAxis) plot.getDomainAxis();

        NumberFormat inFormatX = new DecimalFormat(SciNotX[SciNotIndexX]);
        if (UseScientificNotationX) {
            XAxis.setNumberFormatOverride(inFormatX);
        }
        NumberAxis YAxis = (NumberAxis) plot.getRangeAxis();
        NumberFormat inFormatY = new DecimalFormat(SciNotY[SciNotIndexY]);
        if (UseScientificNotationY) {
            YAxis.setNumberFormatOverride(inFormatY);
        }

        NumberAxis Y2Axis = (NumberAxis) plot.getRangeAxis(1);

        if (Y2Axis != null) {

            if (UseScientificNotationY) {
                Y2Axis.setNumberFormatOverride(inFormatY);
            }
        }
    }

    ArrayList get_Balla() {
        return balla;
    }

    void set_SciNotX(int value) {
        SciNotIndexX = value;
    }

    void set_SciNotY(int value) {
        SciNotIndexY = value;
    }

    void set_UseScientificNotationX(boolean value) {
        UseScientificNotationX = value;
    }

    void set_UseScientificNotationY(boolean value) {
        UseScientificNotationY = value;
    }

    void set_normalizeVector(boolean value) {
        normalizeVector = value;
    }

    void set_exp_factor_estimation(double value) {
        exp_factor_estimation = value;
    }

    double get_exp_factor_estimation() {
        return exp_factor_estimation;
    }

    boolean get_normalizeVector() {
        return normalizeVector;
    }

    String[] get_ScientificNotationX() {
        return SciNotX;
    }

    String[] get_ScientificNotationY() {
        return SciNotY;
    }

    int[] get_position_ix_iy_iz(int i_b) {
        int[] pos = new int[3];
        int nxz = nx * nz;
        int ix = (int) ((i_b) / nxz);
        int iy = (int) ((i_b - (ix) * nxz) / nz);
        int iz = i_b - (ix) * nxz - (iy) * nz;
        pos[0] = ix;
        pos[1] = iy;
        pos[2] = iz;
        return pos;
    }

    double get_ShrinkFactor() {
        return contrazione;
    }

    double get_Zfactor() {
        return Zfactor;
    }

    float get_Campo() {
        return campo;
    }

    void set_Campo(float value) {
        campo = value;
    }

    void set_ContrazioneFactor(double value) {
        contrazione = value;
    }

    void set_Zfactor(double value) {
        Zfactor = value;
    }

    void set_hideoutofrange(boolean value) {
        hideoutofrange = value;
    }

    boolean get_hideoutofrange() {
        return hideoutofrange;
    }

    boolean get_useMoreRealisticColor() {
        return useMoreRealisticColor;
    }

    void addShape(ArrayList newshape) {
        shapes.add(newshape);
    }

    void addShapesSurface(GraphData newSurfaceshape) {
        shapesSurface.add(newSurfaceshape);
    }

    ArrayList get_shapes() {
        return shapes;
    }

    ArrayList get_ShapesSurface() {
        return shapesSurface;
    }

    void addShapeColor(float r, float g, float b) {
        Color3f newvalue = new Color3f(r, g, b);
        shapesColor.add(newvalue);
    }

    void addShapesFileName(String newvalue) {

        shapesFileName.add(newvalue);
    }

    void addPolyShapeVisible(boolean value) {
        PolyShapesVisible.add(value);
    }

    void addSurfaceShapeVisible(boolean value) {
        SurfaceShapesVisible.add(value);
    }

    void addSurfaceShapeVisiblePLY(boolean value) {
        SurfaceShapesVisiblePLY.add(value);
    }

    void set_ColorPolyShape(int index, float r, float g, float b) {
        Color3f c = new Color3f(r, g, b);
        shapesColor.set(index, c);
    }

    void set_ColorPolyShape2(int index, float r, float g, float b) {
        Color3f c = new Color3f(r, g, b);
        SurfaceData a = (SurfaceData) Surfaces_Data.get(index);
        a.set_shape_color3f(c);
        Surfaces_Data.set(index, a);
    }

    Color3f[] get_ShapesColor() {
        Color3f[] color3fShape = new Color3f[shapesColor.size()];
        for (int j = 0; j < shapesColor.size(); j++) {
            color3fShape[j] = new Color3f();
            color3fShape[j] = (Color3f) shapesColor.get(j);
        }
        return color3fShape;
    }

    Color3f[] get_ShapesColor2() {
        Color3f[] color3fShape = new Color3f[Surfaces_Data.size()];

        for (int j = 0; j < Surfaces_Data.size(); j++) {
            SurfaceData a = (SurfaceData) Surfaces_Data.get(j);
            color3fShape[j] = a.get_shape_color3f();
        }
        return color3fShape;
    }

    String[] get_shapeFileNames() {
        String[] nameShape = new String[shapesFileName.size()];
        for (int j = 0; j < shapesFileName.size(); j++) {
            nameShape[j] = new String();
            nameShape[j] = (String) shapesFileName.get(j);
        }
        return nameShape;
    }

    String[] get_shapeFileNames2() {

        String[] nameShape = new String[Surfaces_Data.size()];
        for (int j = 0; j < Surfaces_Data.size(); j++) {
            SurfaceData a = (SurfaceData) Surfaces_Data.get(j);
            nameShape[j] = a.get_file_name();
        }
        return nameShape;
    }

    boolean[] get_PolyShapesVisible() {
        boolean[] VisibleShape = new boolean[PolyShapesVisible.size()];
        for (int j = 0; j < PolyShapesVisible.size(); j++) {
            // PolyShapesVisible[j]=new boolean();
            VisibleShape[j] = Boolean.valueOf(PolyShapesVisible.get(j).toString());
        }
        return VisibleShape;
    }

    boolean[] get_PolyShapesVisible2() {
        boolean[] VisibleShape = new boolean[Surfaces_Data.size()];
        for (int j = 0; j < Surfaces_Data.size(); j++) {
            // PolyShapesVisible[j]=new boolean();
            SurfaceData a = (SurfaceData) Surfaces_Data.get(j);
            VisibleShape[j] = a.isPolyVisible();
        }
        return VisibleShape;
    }

    double ComputeZ_surface(int index, double x, double y, double z) {
        double z_estimated = -1.0E20;
        SurfaceData S = (SurfaceData) Surfaces_Data.get(index);
        if (S.get_type() == 0) {
            z_estimated = estimated_z_surface(S.get_GraphData(), x, y, z);
        } else if (S.get_type() == 1) {
            z_estimated = estimated_z_surface(S.get_PLYParser(), x, y, z);
        }
        return z_estimated;
    }

    double estimated_z_surface(PLYParser mySurface, double x, double y, double z) {
        double z_estimated = -1.0E20;
        Point3d P = new Point3d(x, y, z);
        List ply_coord_vector3D = mySurface.getVertices();
        int n_vertex_of_ply = ply_coord_vector3D.size();
        Point3d[] vertex3d = new Point3d[n_vertex_of_ply];
        for (int i = 0; i < n_vertex_of_ply; i++) {
            Vector3D tmp = (Vector3D) ply_coord_vector3D.get(i);
            vertex3d[i] = new Point3d(tmp.getX(), tmp.getY(), tmp.getZ());
        }
        List ply_face_indices = mySurface.getFaces();
        int n_of_faces = ply_face_indices.size();
        for (int i = 0; i < n_of_faces; i++) {
            int[] tmp = (int[]) ply_face_indices.get(i);
            if (tmp.length != 3) {
                //warning, we handle just triangulate faces
            }
            Point3d[] verts = new Point3d[3];
            for (int j = 0; j < tmp.length; j++) {

                verts[j] = vertex3d[tmp[j]];
            }
            double[] lambda = barycentric_coordinate(P, verts);
            boolean is_inside = true;
            for (int i_l = 0; i_l < 3; i_l++) {
                if (lambda[i_l] < 0 || lambda[i_l] > 1.0) {
                    is_inside = false;
                }
            }
            if (is_inside) {
                z_estimated = verts[0].z * lambda[0] + verts[1].z * lambda[1] + verts[2].z * lambda[2];
                return z_estimated;
            }

        }
        return z_estimated;
    }

    double[] barycentric_coordinate(Point3d P, Point3d[] verts) {
        double[] lambda = new double[3];
        double num1 = (verts[1].y - verts[2].y) * (P.x - verts[2].x) + (verts[2].x - verts[1].x) * (P.y - verts[2].y);
        double den = (verts[1].y - verts[2].y) * (verts[0].x - verts[2].x) + (verts[2].x - verts[1].x) * (verts[0].y - verts[2].y);
        double num2 = (verts[2].y - verts[0].y) * (P.x - verts[2].x) + (verts[0].x - verts[2].x) * (P.y - verts[2].y);
        if (den != 0.0) {
            lambda[0] = num1 / den;
            lambda[1] = num2 / den;
            lambda[2] = 1.0 - lambda[0] - lambda[1];
        } else {
            lambda[0] = -1.0;
            lambda[1] = -1.0;
            lambda[2] = -1.0;
        }

        return lambda;
    }

    double estimated_z_surface(GraphData mySurface, double x, double y, double z) {

        double z_estimated = -1.0E20;
        if (x <= mySurface.get_X_Max() && x >= mySurface.get_X_Min() && y <= mySurface.get_Y_Max() && y >= mySurface.get_Y_Min()) {

            double[][] z_data = mySurface.getData();
            double x_rel = x - mySurface.get_X_Min();
            double y_rel = y - mySurface.get_Y_Min();
            double dx = (mySurface.get_X_Max() - mySurface.get_X_Min()) / (mySurface.nx - 1);
            double dy = (mySurface.get_Y_Max() - mySurface.get_Y_Min()) / (mySurface.ny - 1);
            int i_x = (int) ((x_rel) / dx);
            int i_y = (int) ((y_rel) / dy);
            double z00 = z_data[i_y][i_x];
            double z10 = z_data[i_y][i_x + 1];
            double z11 = z_data[i_y + 1][i_x + 1];
            double z01 = z_data[i_y + 1][i_x];
            if (z00 == -1.0e20 || z10 == -1.0e20 || z11 == -1.0e20 || z01 == -1.0e20) {
                return z_estimated;
            }

            double z_x_top;
            double z_x_bot;
            if (i_x == mySurface.nx) {
                z_x_top = z11;
                z_x_bot = z10;
            } else {
                z_x_top = ((i_x + 1) * dx - x_rel) / (dx) * z01 + (x_rel - i_x * dx) / (dx) * z11;
                z_x_bot = ((i_x + 1) * dx - x_rel) / (dx) * z00 + (x_rel - i_x * dx) / (dx) * z10;
            }

            z_estimated = ((i_y + 1) * dy - y_rel) / (dy) * z_x_top + (y_rel - i_y * dy) / (dy) * z_x_bot;

        }

        return z_estimated;

    }

    void set_PolyShapesVisible(int index, boolean newvalue) {
        PolyShapesVisible.set(index, newvalue);
    }

    void set_PolyShapesVisible2(int index, boolean newvalue) {
        SurfaceData i_s = (SurfaceData) Surfaces_Data.get(index);
        i_s.set_isPolyVisible(newvalue);
        Surfaces_Data.set(index, i_s);
    }

    boolean[] get_SurfaceShapesVisible2() {
        boolean[] VisibleShape = new boolean[Surfaces_Data.size()];
        for (int j = 0; j < Surfaces_Data.size(); j++) {
            // PolyShapesVisible[j]=new boolean();
            SurfaceData a = (SurfaceData) Surfaces_Data.get(j);
            VisibleShape[j] = a.isSurfaceVisible();
        }
        return VisibleShape;
    }

    boolean[] get_SurfaceShapesVisible() {
        boolean[] VisibleShape = new boolean[SurfaceShapesVisible.size()];
        for (int j = 0; j < SurfaceShapesVisible.size(); j++) {
            // PolyShapesVisible[j]=new boolean();
            VisibleShape[j] = Boolean.valueOf(SurfaceShapesVisible.get(j).toString());
        }
        return VisibleShape;
    }

    boolean[] get_SurfaceShapesVisiblePLY() {
        boolean[] VisibleShapePLY = new boolean[SurfaceShapesVisiblePLY.size()];
        for (int j = 0; j < SurfaceShapesVisiblePLY.size(); j++) {
            // PolyShapesVisible[j]=new boolean();
            VisibleShapePLY[j] = Boolean.valueOf(SurfaceShapesVisiblePLY.get(j).toString());
        }
        return VisibleShapePLY;
    }

    void set_SurfaceShapesVisible(int index, boolean newvalue) {
        SurfaceShapesVisible.set(index, newvalue);
    }

    void set_SurfaceShapesVisible2(int index, boolean newvalue) {
        SurfaceData i_s = (SurfaceData) Surfaces_Data.get(index);
        i_s.set_isSurfaceVisible(newvalue);
        Surfaces_Data.set(index, i_s);
    }

    void set_SurfaceShapesVisiblePLY(int index, boolean newvalue) {
        SurfaceShapesVisiblePLY.set(index, newvalue);

    }

    void set_SurfaceShapesVisiblePLY2(int index, boolean newvalue) {
        SurfaceData i_s = (SurfaceData) Surfaces_Data.get(index);
        i_s.set_isPolyVisible(newvalue);
        Surfaces_Data.set(index, i_s);

    }

    int get_nx() {
        return nx;
    }

    int get_selectedIndex() {
        return selectedindex;
    }

    int get_ny() {
        return ny;
    }

    int get_nz() {
        return nz;
    }

    int get_number_of_flux_variables() {
        return number_of_flow_variables;
    }

    int get_nxy() {
        return nxy;
    }

    int get_nxyz() {
        return nxyz;
    }

    int get_actualVariableToPlot() {
        return actualVariableToPlot;
    }

    int get_actualFluxToPlot() {
        return actualFluxToPlot;
    }

    int get_actualTimeToPlot() {
        return actualTimeToPlot;
    }

    float get_actualZToPlot() {
        return actuaZToPlot;
    }

    float get_xmin() {
        return xmin;
    }

    float get_ymin() {
        return ymin;
    }

    float get_zmin() {
        return zmin;
    }

    float get_xmax() {
        return xmax;
    }

    float get_ymax() {
        return ymax;
    }

    float get_zmax() {
        return zmax;
    }

    float get_ROI_xmin() {
        return ROI_xmin;
    }

    float get_ROI_ymin() {
        return ROI_ymin;
    }

    float get_ROI_zmin() {
        return ROI_zmin;
    }

    float get_ROI_xmax() {
        return ROI_xmax;
    }

    float get_ROI_ymax() {
        return ROI_ymax;
    }

    float get_ROI_zmax() {
        return ROI_zmax;
    }

    float get_HideROI_xmin() {
        return HideROI_xmin;
    }

    float get_HideROI_ymin() {
        return HideROI_ymin;
    }

    float get_HideROI_zmin() {
        return HideROI_zmin;
    }

    float get_HideROI_xmax() {
        return HideROI_xmax;
    }

    float get_HideROI_ymax() {
        return HideROI_ymax;
    }

    float get_HideROI_zmax() {
        return HideROI_zmax;
    }

    boolean get_useHideROI() {
        return useHideROI;
    }

    boolean get_voronoi() {
        return voronoi;
    }

    float get_Xo(int index) {
        return Xo[index];
    }

    float get_DimBlockX(int index) {
        return DimBlockX[index];
    }

    float get_DimBlockY(int index) {
        return DimBlockY[index];
    }

    float get_DimBlockZ(int index) {
        return DimBlockZ[index];
    }

    float get_Yo(int index) {
        return Yo[index];
    }

    float get_deltaZGV() {
        return deltaZGV;
    }

    int get_TimeSteps() {
        return TimeSteps;
    }

    int get_n_connections() {
        return n_connections;
    }

    int get_number_of_variables() {
        return number_of_variables;
    }

    float get_Times(int index) {
        return Times[index];
    }

    float get_Zo(int index) {
        return Zo[index];
    }

    double get_BlockVolume(int index) {
        return BlockVolume[index];
    }

    void set_BlockVolume(int index, double vol) {
        BlockVolume[index] = vol;
    }

    float get_ThicknessLayer(int index) {
        return ThicknessBlock[index];
    }

    float get_globalStatistics(int index1, int index2) {
        return globalStatistics[index1][index2];
    }

    float get_GlobalScale(int index1, int index2) {
        return GlobalScale[index1][index2];
    }

    ArrayList get_rocknames() {
        return rocksnames;
    }

    void set_useMoreRealisticColor(boolean value) {
        useMoreRealisticColor = value;
    }

    void set_GlobalScale(int index1, int index2, float value) {
        GlobalScale[index1][index2] = value;
    }

    void set_WorkingPath(String value) {
        WorkingPath = value;
    }

    void addGener(String blockName) {
        Gener.add(blockName);
    }

    void set_Kper(boolean value) {
        KperActivate = value;
    }

    boolean get_dataLoaded() {
        return dataLoaded;
    }

    String get_BlockName(int BlockIndex) {
        return BlockNames[BlockIndex];
    }

    int get_RockType(int BlockIndex) {
        return RockType[BlockIndex];
    }

    boolean get_Kper() {
        return KperActivate;
    }

    String get_WorkingPath() {
        return WorkingPath;
    }

    String get_RockType_name(int index) {
        String name = (String) rocksnames.get(index);
        return name;
    }

    String[] get_RockType_name_str() {
        String[] name = new String[rocksnames.size()];
        for (int i = 0; i < rocksnames.size(); i++) {
            name[i] = (String) rocksnames.get(i);
        }
        return name;
    }

    String get_variables_name(int Index) {
        return variables_name_eos[Index];
    }

    String get_variables_unit(int Index) {
        return variables_unit_eos[Index];
    }

    void set_variables_UM(String[] value) {
        variables_unit_eos = value;
    }

    void set_variables_UMFLOW(String[] value) {
        FLOW_variables_unit_eos = value;
    }

    void set_variables_FLOW(String[] value) {
        FLOW_name_eos = value;
        number_of_flow_variables = FLOW_name_eos.length;
    }

    float min(float x, float y) {
        if (x <= y) {
            return x;
        } else {
            return y;
        }
    }

    float max(float x, float y) {
        if (x >= y) {
            return x;
        } else {
            return y;
        }
    }

    int min(int x, int y) {
        if (x <= y) {
            return x;
        } else {
            return y;
        }
    }

    int max(int x, int y) {
        if (x >= y) {
            return x;
        } else {
            return y;
        }
    }

    String get_DataFileName() {
        return DataFileName;
    }

    String get_InconFileName() {
        return InconFileName;
    }

    String get_DataFilePath() {
        return DataFilePath;
    }

    String get_SegmtFileName() {
        return SegmtFileName;
    }

    ArrayList[] GetvoronoiGeometryBox() {
        return voronoiGeometryBox;
    }

    String getGridType() {
        String uno = "";
        String due = "";
        String tre = "";
        int d = 0;
        if (xmax - xmin > 0) {
            uno = "x";
            d++;
        }
        if (ymax - ymin > 0) {
            due = "y";
            d++;
        }
        if (zmax - zmin > 0) {
            tre = "z";
            d++;
        }
        if (d == 0) {
            gridType = "";
        } else {
            gridType = uno + due + tre;
        }

        return gridType;
    }

    ArrayList[] GetRegularGeometryBox() {

        voronoiGeometryBox = new ArrayList[nxyz];
        //facciamo un precheck.
        //se una dimensione is sempre zero, allora la poniamo uguale al massimo dell'altra..
        float dxmax = 0;
        float dymax = 0;
        float dzmax = 0;

        for (int i = 0; i < nxyz; i++) {
            dxmax = max(dxmax, DimBlockX[i]);
            dymax = max(dymax, DimBlockY[i]);
            dzmax = max(dzmax, DimBlockZ[i]);
        }
        float dmax = max(xmax - xmin, ymax - ymin);
        dmax = max(dmax, zmax - zmin);

        for (int i = 0; i < nxyz; i++) {
            voronoiGeometryBox[i] = new ArrayList();
            double x = get_Xo(i);
            double y = get_Yo(i);
            Point2d vertice = new Point2d(x, y);
            voronoiGeometryBox[i].add(vertice);
            //1
            float dimx = DimBlockX[i] / 2;
            float dimy = DimBlockY[i] / 2;
            //Questa serie di istruzione abilita la creazione di una terza dimensione che non c'� per i modelli 2d e 1d
            if (gridType.length() == 1)//solo caso 1D
            {
                if (dxmax == 0) {
                    dimx = dmax / 10.0f;
                }
                if (dymax == 0) {
                    dimy = dmax / 10.0f;
                }
                if (dzmax == 0) {
                    DimBlockZ[i] = dmax / 10.0f;
                }
            }
            if (gridType.length() == 2)// caso 2D
            {
                if (dxmax == 0) {
                    dimx = dmax / 10.0f;
                }
                if (dymax == 0) {
                    dimy = dmax / 10.0f;
                }
                if (dzmax == 0) {
                    DimBlockZ[i] = dmax / 10.0f;
                }
            }
            x = get_Xo(i) - dimx;
            y = get_Yo(i) - dimy;
            vertice = new Point2d(x, y);
            voronoiGeometryBox[i].add(vertice);
            //2
            x = get_Xo(i) + dimx;
            y = get_Yo(i) - dimy;
            vertice = new Point2d(x, y);
            voronoiGeometryBox[i].add(vertice);
            //3
            x = get_Xo(i) + dimx;
            y = get_Yo(i) + dimy;
            vertice = new Point2d(x, y);
            voronoiGeometryBox[i].add(vertice);
            //4
            x = get_Xo(i) - dimx;
            y = get_Yo(i) + dimy;
            vertice = new Point2d(x, y);
            voronoiGeometryBox[i].add(vertice);
        }
        return voronoiGeometryBox;
    }

    String get_InFileName() {
        return InFileName;
    }

    void set_BlockName(String BlockName, int BlockIndex) {
        BlockNames[BlockIndex] = BlockName;
    }

    void set_DataFileName(String Value) {
        DataFileName = Value;
    }

    void set_InconFileName(String Value) {
        InconFileName = Value;
    }

    void set_DataFilePath(String Value) {
        DataFilePath = Value;
    }

    void set_SegmtFileName(String Value) {
        SegmtFileName = Value;
    }

    void set_InFileName(String Value) {
        InFileName = Value;
    }

    void set_voronoi(boolean Value) {
        voronoi = Value;
    }

    void set_Zo(float value, int index) {
        Zo[index] = value;
    }

    void set_ThicknessLayer(int index, float value) {
        ThicknessBlock[index] = value;
    }

    void set_selectedindex(int value) {
        selectedindex = value;
    }

    void set_Times(int index, float value) {
        Times[index] = value;
    }

    void set_n_connections(int value) {
        n_connections = value;
    }

    void set_xmin(float value) {
        xmin = value;
    }

    void set_ymin(float value) {
        ymin = value;
    }

    void set_zmin(float value) {
        zmin = value;
    }

    void set_xmax(float value) {
        xmax = value;
    }

    void set_ymax(float value) {
        ymax = value;
    }

    void set_zmax(float value) {
        zmax = value;
    }

    void set_ROI_xmin(float value) {
        ROI_xmin = value;
    }

    void set_ROI_ymin(float value) {
        ROI_ymin = value;
    }

    void set_ROI_zmin(float value) {
        ROI_zmin = value;
    }

    void set_ROI_xmax(float value) {
        ROI_xmax = value;
    }

    void set_ROI_ymax(float value) {
        ROI_ymax = value;
    }

    void set_ROI_zmax(float value) {
        ROI_zmax = value;
    }

    void set_HideROI_xmin(float value) {
        HideROI_xmin = value;
    }

    void set_HideROI_ymin(float value) {
        HideROI_ymin = value;
    }

    void set_HideROI_zmin(float value) {
        HideROI_zmin = value;
    }

    void set_HideROI_xmax(float value) {
        HideROI_xmax = value;
    }

    void set_HideROI_ymax(float value) {
        HideROI_ymax = value;
    }

    void set_HideROI_zmax(float value) {
        HideROI_zmax = value;
    }

    void set_UseHideROI(boolean value) {
        useHideROI = value;
    }

    void set_INIT_ROI() {
        ROI_xmin = xmin;
        ROI_ymin = ymin;
        ROI_zmin = zmin;
        ROI_xmax = xmax;
        ROI_ymax = ymax;
        ROI_zmax = zmax;
    }

    void set_default_HideROI() {
        HideROI_xmin = xmin;
        HideROI_ymin = ymin;
        HideROI_zmin = (zmin + zmax) / 2;
        HideROI_xmax = (xmax + xmin) / 2;
        HideROI_ymax = (ymin + ymax) / 2;
        HideROI_zmax = zmax;
    }

    void set_Xo(float value, int index) {
        Xo[index] = value;
    }

    void set_DimBlockX(float value, int index) {
        DimBlockX[index] = value;
    }

    void set_DimBlockY(float value, int index) {
        DimBlockY[index] = value;
    }

    void set_DimBlockZ(float value, int index) {
        DimBlockZ[index] = value;
    }

    void set_Yo(float value, int index) {
        Yo[index] = value;
    }

    void set_nx(int value) {
        nx = value;
    }

    void set_number_of_flux_variables(int value) {
        number_of_flow_variables = value;
    }

    void set_ConnectionBlocks(int value1, int value2, int value3) {
        ConnectionBlocks[value1][0] = value2;
        ConnectionBlocks[value1][1] = value3;
    }

    int[][] get_ConnectionBlocks() {
        return ConnectionBlocks;
    }

    int[][] get_ConnectionTable() {
        return ConnectionTable;
    }

    void set_actualVariableToPlot(int value) {
        actualVariableToPlot = value;
    }

    void set_actualFluxToPlot(int value) {
        actualFluxToPlot = value;
    }

    void set_actualTimeToPlot(int value) {
        actualTimeToPlot = value;
    }

    void set_actualLayerToPlot(int value) {
        actuaZToPlot = value;
    }

    void set_eos_number(int value) {

    }

    private boolean checkDuplicateNames(String[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[i].contentEquals(values[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    void set_VariableName(String[] values) {

        if (checkDuplicateNames(values)) {
            for (int i = 0; i < values.length; i++) {
                values[i] = Integer.toString(i) + "_" + values[i];
            }
        }

        variables_name_eos = values;
        number_of_variables = variables_name_eos.length;
    }

    void addVariableName(String[] values) {
        if (checkDuplicateNames(values)) {
            for (int i = 0; i < values.length; i++) {
                values[i] = Integer.toString(i) + "_" + values[i];
            }
        }

        int old_n = variables_name_eos.length;
        String[] temp = new String[old_n + values.length];
        for (int i = 0; i < old_n; i++) {
            temp[i] = variables_name_eos[i];
        }
        for (int i = 0; i < values.length; i++) {
            temp[i + old_n] = values[i];
        }
        variables_name_eos = new String[old_n + values.length];
        for (int i = 0; i < old_n + values.length; i++) {
            variables_name_eos[i] = temp[i];
        }
        number_of_variables = variables_name_eos.length;
    }

    void addVariablesFLOW(String[] values) {
        if (checkDuplicateNames(values)) {
            for (int i = 0; i < values.length; i++) {
                values[i] = Integer.toString(i) + "_" + values[i];
            }
        }

        int old_n = FLOW_name_eos.length;
        String[] temp = new String[old_n + values.length];
        for (int i = 0; i < old_n; i++) {
            temp[i] = FLOW_name_eos[i];
        }
        for (int i = 0; i < values.length; i++) {
            temp[i + old_n] = values[i];
        }
        FLOW_name_eos = new String[old_n + values.length];
        for (int i = 0; i < old_n + values.length; i++) {
            FLOW_name_eos[i] = temp[i];
        }
        number_of_flow_variables = FLOW_name_eos.length;
    }

    void addVariableNameUM(String[] values) {
        int old_n = variables_unit_eos.length;
        String[] temp = new String[old_n + values.length];
        for (int i = 0; i < old_n; i++) {
            temp[i] = variables_unit_eos[i];
        }
        for (int i = 0; i < values.length; i++) {
            temp[i + variables_unit_eos.length] = values[i];
        }
        variables_unit_eos = new String[old_n + values.length];
        for (int i = 0; i < old_n + values.length; i++) {
            variables_unit_eos[i] = temp[i];
        }

    }

    void addFLOWNameUM(String[] values) {
        int old_n = FLOW_variables_unit_eos.length;
        String[] temp = new String[old_n + values.length];
        for (int i = 0; i < old_n; i++) {
            temp[i] = FLOW_variables_unit_eos[i];
        }
        for (int i = 0; i < values.length; i++) {
            temp[i + FLOW_variables_unit_eos.length] = values[i];
        }
        FLOW_variables_unit_eos = new String[old_n + values.length];
        for (int i = 0; i < old_n + values.length; i++) {
            FLOW_variables_unit_eos[i] = temp[i];
        }

    }

    void set_scalasceta(int value) {
        scalascelta = value;
    }

    void set_nxy(int value) {
        nxy = value;
    }

    void set_nxyz(int value) {
        nxyz = value;
    }

    void set_ny(int value) {
        ny = value;
    }

    void set_nz(int value) {
        nz = value;
    }

    void set_number_of_variables(int value) {
        number_of_variables = value;
    }

    void set_dataLoaded(boolean value) {
        dataLoaded = value;
        CreateGlobalStatistics();
        CreateColorArray3f();
    }

    void set_deltaZGV(float value) {
        deltaZGV = value;
    }

    void set_dataArray(int index1, int index2, int index3, float value) {
        //index1: n_block
        //index2: timestep
        //index3: variable
        dataArray[index1][index2][index3] = value;
    }

    void set_dataFluxArray(int index1, int index2, int index3, float value) {
        //index1: n_block
        //index2: timestep
        //index3: variable
        FluxDataArray[index1][index2][index3] = value;
    }

    String[] getVariableName() {
        return variables_name_eos;
    }

    String[] getFLOWName() {
        return FLOW_name_eos;
    }

    String[] getVariablesUM() {

        return variables_unit_eos;

    }

    String[] getFluxUM() {

        return FLOW_variables_unit_eos;

    }

    float get_dataArray(int index1, int index2, int index3) {
        return dataArray[index1][index2][index3];
    }

    float get_VectorDataArray(int index1, int index2, int index3) {
        return VectorDataArray[index1][index2][index3];
    }

    float get_VectorModuleStatistics(int index1) {
        return VectorModuleStatistics[index1];
    }

    void BlockNamesArrayCreate(int n_blocks) {
        BlockNames = new String[n_blocks];
    }

    void RockTypeArrayCreate(int n_blocks) {

        RockType = new int[n_blocks];
    }

    void XoYoZoArrayCreate(int n_blocks) {

        Xo = new float[n_blocks];
        Yo = new float[n_blocks];
        Zo = new float[n_blocks];
        Block_is_selected = new boolean[n_blocks];
        for (int i = 0; i < n_blocks; i++) {
            Block_is_selected[i] = false;
        }

    }

    void dataArrayCreate(int n_blocks, int timesteps) {
        TimeSteps = timesteps;
        dataArray = new float[n_blocks][timesteps][number_of_variables];
        Times = new float[timesteps];
        nxyz = n_blocks;
        globalStatistics = new float[number_of_variables][4]; //0=min,1=max,2=mean,3=std;
        GlobalScale = new float[number_of_variables][2];//0=min,1=max
//         RockType=new int[n_blocks];
        createcolorRockTypes();
    }

    Boolean is_rocktype_visible(int index) {
        return rocktype_visible[index];
    }

    void set_rocktype_visible(int index, boolean value) {
        rocktype_visible[index] = value;
    }

    int getRockTypesNumber() {
        return rocksnames.size();
    }

    void connectionsArrayCreate(int n_blocks) {
        ConnectionData = new float[n_connections][1];//[index][0=teta]
        ConnectionBlocks = new int[n_connections][2];//[index][0=block1][0=block2]
        FluxDataArray = new float[n_connections][TimeSteps][number_of_flow_variables];

        VectorModuleStatistics = new float[number_of_flow_variables];
    }

    void dimXArrayCreate(int nx) {
        DimBlockX = new float[nx];
    }

    void dimYArrayCreate(int ny) {
        DimBlockY = new float[ny];
    }

    void dimZArrayCreate(int nz) {
        DimBlockZ = new float[nz];
    }

    void CreateColorArray3f() {
        if (scalascelta == 0) {

            float rgb[] = new float[3];
            if (colore != null) {
                colore = (Color3f[]) resizeArray(colore, 240);
            } else {
                colore = new Color3f[240];
            }

            for (int i = 0; i < 240; i = i + 1) {

                hsv2rgb((float) ((239.0f - (float) i)), 100.0f, 100.0f, rgb);

                colore[i] = new Color3f(rgb[0], rgb[1], rgb[2]);

            }
        } // JET COLOR SCALE (copiato da ..mtlb hehe
        if (scalascelta == 1) {
            if (colore != null) {
                colore = (Color3f[]) resizeArray(colore, 64);
            } else {
                colore = new Color3f[64];
            }

            colore[0] = new Color3f(0.0000000f, 0.0000000f, 0.5546875f);
            colore[1] = new Color3f(0.0000000f, 0.0000000f, 0.6210938f);
            colore[2] = new Color3f(0.0000000f, 0.0000000f, 0.6835938f);
            colore[3] = new Color3f(0.0000000f, 0.0000000f, 0.7460938f);
            colore[4] = new Color3f(0.0000000f, 0.0000000f, 0.8085938f);
            colore[5] = new Color3f(0.0000000f, 0.0000000f, 0.8710938f);
            colore[6] = new Color3f(0.0000000f, 0.0000000f, 0.9335938f);
            colore[7] = new Color3f(0.0000000f, 0.0000000f, 0.9960938f);
            colore[8] = new Color3f(0.0000000f, 0.0585938f, 0.9960938f);
            colore[9] = new Color3f(0.0000000f, 0.1210938f, 0.9960938f);
            colore[10] = new Color3f(0.0000000f, 0.1835938f, 0.9960938f);
            colore[11] = new Color3f(0.0000000f, 0.2460938f, 0.9960938f);
            colore[12] = new Color3f(0.0000000f, 0.3085938f, 0.9960938f);
            colore[13] = new Color3f(0.0000000f, 0.3710938f, 0.9960938f);
            colore[14] = new Color3f(0.0000000f, 0.4335938f, 0.9960938f);
            colore[15] = new Color3f(0.0000000f, 0.4960938f, 0.9960938f);
            colore[16] = new Color3f(0.0000000f, 0.5585938f, 0.9960938f);
            colore[17] = new Color3f(0.0000000f, 0.6210938f, 0.9960938f);
            colore[18] = new Color3f(0.0000000f, 0.6835938f, 0.9960938f);
            colore[19] = new Color3f(0.0000000f, 0.7460938f, 0.9960938f);
            colore[20] = new Color3f(0.0000000f, 0.8085938f, 0.9960938f);
            colore[21] = new Color3f(0.0000000f, 0.8710938f, 0.9960938f);
            colore[22] = new Color3f(0.0000000f, 0.9335938f, 0.9960938f);
            colore[23] = new Color3f(0.0000000f, 0.9960938f, 0.9960938f);
            colore[24] = new Color3f(0.0585938f, 0.9960938f, 0.9335938f);
            colore[25] = new Color3f(0.1210938f, 0.9960938f, 0.8710938f);
            colore[26] = new Color3f(0.1835938f, 0.9960938f, 0.8085938f);
            colore[27] = new Color3f(0.2460938f, 0.9960938f, 0.7460938f);
            colore[28] = new Color3f(0.3085938f, 0.9960938f, 0.6835938f);
            colore[29] = new Color3f(0.3710938f, 0.9960938f, 0.6210938f);
            colore[30] = new Color3f(0.4335938f, 0.9960938f, 0.5585938f);
            colore[31] = new Color3f(0.4960938f, 0.9960938f, 0.4960938f);
            colore[32] = new Color3f(0.5585938f, 0.9960938f, 0.4335938f);
            colore[33] = new Color3f(0.6210938f, 0.9960938f, 0.3710938f);
            colore[34] = new Color3f(0.6835938f, 0.9960938f, 0.3085938f);
            colore[35] = new Color3f(0.7460938f, 0.9960938f, 0.2460938f);
            colore[36] = new Color3f(0.8085938f, 0.9960938f, 0.1835938f);
            colore[37] = new Color3f(0.8710938f, 0.9960938f, 0.1210938f);
            colore[38] = new Color3f(0.9335938f, 0.9960938f, 0.0585938f);
            colore[39] = new Color3f(0.9960938f, 0.9960938f, 0.0000000f);
            colore[40] = new Color3f(0.9960938f, 0.9335938f, 0.0000000f);
            colore[41] = new Color3f(0.9960938f, 0.8710938f, 0.0000000f);
            colore[42] = new Color3f(0.9960938f, 0.8085938f, 0.0000000f);
            colore[43] = new Color3f(0.9960938f, 0.7460938f, 0.0000000f);
            colore[44] = new Color3f(0.9960938f, 0.6835938f, 0.0000000f);
            colore[45] = new Color3f(0.9960938f, 0.6210938f, 0.0000000f);
            colore[46] = new Color3f(0.9960938f, 0.5585938f, 0.0000000f);
            colore[47] = new Color3f(0.9960938f, 0.4960938f, 0.0000000f);
            colore[48] = new Color3f(0.9960938f, 0.4335938f, 0.0000000f);
            colore[49] = new Color3f(0.9960938f, 0.3710938f, 0.0000000f);
            colore[50] = new Color3f(0.9960938f, 0.3085938f, 0.0000000f);
            colore[51] = new Color3f(0.9960938f, 0.2460938f, 0.0000000f);
            colore[52] = new Color3f(0.9960938f, 0.1835938f, 0.0000000f);
            colore[53] = new Color3f(0.9960938f, 0.1210938f, 0.0000000f);
            colore[54] = new Color3f(0.9960938f, 0.0585938f, 0.0000000f);
            colore[55] = new Color3f(0.9960938f, 0.0000000f, 0.0000000f);
            colore[56] = new Color3f(0.9335938f, 0.0000000f, 0.0000000f);
            colore[57] = new Color3f(0.8710938f, 0.0000000f, 0.0000000f);
            colore[58] = new Color3f(0.8085938f, 0.0000000f, 0.0000000f);
            colore[59] = new Color3f(0.7460938f, 0.0000000f, 0.0000000f);
            colore[60] = new Color3f(0.6835938f, 0.0000000f, 0.0000000f);
            colore[61] = new Color3f(0.6210938f, 0.0000000f, 0.0000000f);
            colore[62] = new Color3f(0.5585938f, 0.0000000f, 0.0000000f);
            colore[63] = new Color3f(0.4960938f, 0.0000000f, 0.0000000f);

        }
        if (scalascelta == 2) {
            if (colore != null) {
                colore = (Color3f[]) resizeArray(colore, 24);
            } else {
                colore = new Color3f[24];
            }

            colore[0] = new Color3f(0.0000000f, 0.0000000f, 0.6484375f);
            colore[1] = new Color3f(0.0000000f, 0.0000000f, 0.8203125f);
            colore[2] = new Color3f(0.0000000f, 0.0000000f, 0.9960938f);
            colore[3] = new Color3f(0.0000000f, 0.1640625f, 0.9960938f);
            colore[4] = new Color3f(0.0000000f, 0.3320313f, 0.9960938f);
            colore[5] = new Color3f(0.0000000f, 0.4960938f, 0.9960938f);
            colore[6] = new Color3f(0.0000000f, 0.6640625f, 0.9960938f);
            colore[7] = new Color3f(0.0000000f, 0.8281250f, 0.9960938f);
            colore[8] = new Color3f(0.0000000f, 0.9960938f, 0.9960938f);
            colore[9] = new Color3f(0.1679688f, 0.9960938f, 0.9960938f);
            colore[10] = new Color3f(0.3320313f, 0.9960938f, 0.8281250f);
            colore[11] = new Color3f(0.4960938f, 0.9960938f, 0.6640625f);
            colore[12] = new Color3f(0.6640625f, 0.9960938f, 0.4960938f);
            colore[13] = new Color3f(0.8281250f, 0.9960938f, 0.3320313f);
            colore[14] = new Color3f(0.9960938f, 0.9960938f, 0.1679688f);
            colore[15] = new Color3f(0.9960938f, 0.9960938f, 0.0000000f);
            colore[16] = new Color3f(0.9960938f, 0.8281250f, 0.0000000f);
            colore[17] = new Color3f(0.9960938f, 0.6640625f, 0.0000000f);
            colore[18] = new Color3f(0.9960938f, 0.4960938f, 0.0000000f);
            colore[19] = new Color3f(0.9960938f, 0.3320313f, 0.0000000f);
            colore[20] = new Color3f(0.9960938f, 0.1640625f, 0.0000000f);
            colore[21] = new Color3f(0.9960938f, 0.0000000f, 0.0000000f);
            colore[22] = new Color3f(0.8203125f, 0.0000000f, 0.0000000f);
            colore[23] = new Color3f(0.6484375f, 0.0000000f, 0.0000000f);

        }
        if (scalascelta == 3) {
            if (colore != null) {
                colore = (Color3f[]) resizeArray(colore, 256);
            } else {
                colore = new Color3f[256];
            }

            colore[0] = new Color3f(0.0000000f, 0.0000000f, 0.0000000f);
            colore[1] = new Color3f(0.0039216f, 0.0039216f, 0.0039216f);
            colore[2] = new Color3f(0.0078431f, 0.0078431f, 0.0078431f);
            colore[3] = new Color3f(0.0117647f, 0.0117647f, 0.0117647f);
            colore[4] = new Color3f(0.0156863f, 0.0156863f, 0.0156863f);
            colore[5] = new Color3f(0.0196078f, 0.0196078f, 0.0196078f);
            colore[6] = new Color3f(0.0235294f, 0.0235294f, 0.0235294f);
            colore[7] = new Color3f(0.0274510f, 0.0274510f, 0.0274510f);
            colore[8] = new Color3f(0.0313725f, 0.0313725f, 0.0313725f);
            colore[9] = new Color3f(0.0352941f, 0.0352941f, 0.0352941f);
            colore[10] = new Color3f(0.0392157f, 0.0392157f, 0.0392157f);
            colore[11] = new Color3f(0.0431373f, 0.0431373f, 0.0431373f);
            colore[12] = new Color3f(0.0470588f, 0.0470588f, 0.0470588f);
            colore[13] = new Color3f(0.0509804f, 0.0509804f, 0.0509804f);
            colore[14] = new Color3f(0.0549020f, 0.0549020f, 0.0549020f);
            colore[15] = new Color3f(0.0588235f, 0.0588235f, 0.0588235f);
            colore[16] = new Color3f(0.0627451f, 0.0627451f, 0.0627451f);
            colore[17] = new Color3f(0.0666667f, 0.0666667f, 0.0666667f);
            colore[18] = new Color3f(0.0705882f, 0.0705882f, 0.0705882f);
            colore[19] = new Color3f(0.0745098f, 0.0745098f, 0.0745098f);
            colore[20] = new Color3f(0.0784314f, 0.0784314f, 0.0784314f);
            colore[21] = new Color3f(0.0823529f, 0.0823529f, 0.0823529f);
            colore[22] = new Color3f(0.0862745f, 0.0862745f, 0.0862745f);
            colore[23] = new Color3f(0.0901961f, 0.0901961f, 0.0901961f);
            colore[24] = new Color3f(0.0941176f, 0.0941176f, 0.0941176f);
            colore[25] = new Color3f(0.0980392f, 0.0980392f, 0.0980392f);
            colore[26] = new Color3f(0.1019608f, 0.1019608f, 0.1019608f);
            colore[27] = new Color3f(0.1058824f, 0.1058824f, 0.1058824f);
            colore[28] = new Color3f(0.1098039f, 0.1098039f, 0.1098039f);
            colore[29] = new Color3f(0.1137255f, 0.1137255f, 0.1137255f);
            colore[30] = new Color3f(0.1176471f, 0.1176471f, 0.1176471f);
            colore[31] = new Color3f(0.1215686f, 0.1215686f, 0.1215686f);
            colore[32] = new Color3f(0.1254902f, 0.1254902f, 0.1254902f);
            colore[33] = new Color3f(0.1294118f, 0.1294118f, 0.1294118f);
            colore[34] = new Color3f(0.1333333f, 0.1333333f, 0.1333333f);
            colore[35] = new Color3f(0.1372549f, 0.1372549f, 0.1372549f);
            colore[36] = new Color3f(0.1411765f, 0.1411765f, 0.1411765f);
            colore[37] = new Color3f(0.1450980f, 0.1450980f, 0.1450980f);
            colore[38] = new Color3f(0.1490196f, 0.1490196f, 0.1490196f);
            colore[39] = new Color3f(0.1529412f, 0.1529412f, 0.1529412f);
            colore[40] = new Color3f(0.1568627f, 0.1568627f, 0.1568627f);
            colore[41] = new Color3f(0.1607843f, 0.1607843f, 0.1607843f);
            colore[42] = new Color3f(0.1647059f, 0.1647059f, 0.1647059f);
            colore[43] = new Color3f(0.1686275f, 0.1686275f, 0.1686275f);
            colore[44] = new Color3f(0.1725490f, 0.1725490f, 0.1725490f);
            colore[45] = new Color3f(0.1764706f, 0.1764706f, 0.1764706f);
            colore[46] = new Color3f(0.1803922f, 0.1803922f, 0.1803922f);
            colore[47] = new Color3f(0.1843137f, 0.1843137f, 0.1843137f);
            colore[48] = new Color3f(0.1882353f, 0.1882353f, 0.1882353f);
            colore[49] = new Color3f(0.1921569f, 0.1921569f, 0.1921569f);
            colore[50] = new Color3f(0.1960784f, 0.1960784f, 0.1960784f);
            colore[51] = new Color3f(0.2000000f, 0.2000000f, 0.2000000f);
            colore[52] = new Color3f(0.2039216f, 0.2039216f, 0.2039216f);
            colore[53] = new Color3f(0.2078431f, 0.2078431f, 0.2078431f);
            colore[54] = new Color3f(0.2117647f, 0.2117647f, 0.2117647f);
            colore[55] = new Color3f(0.2156863f, 0.2156863f, 0.2156863f);
            colore[56] = new Color3f(0.2196078f, 0.2196078f, 0.2196078f);
            colore[57] = new Color3f(0.2235294f, 0.2235294f, 0.2235294f);
            colore[58] = new Color3f(0.2274510f, 0.2274510f, 0.2274510f);
            colore[59] = new Color3f(0.2313725f, 0.2313725f, 0.2313725f);
            colore[60] = new Color3f(0.2352941f, 0.2352941f, 0.2352941f);
            colore[61] = new Color3f(0.2392157f, 0.2392157f, 0.2392157f);
            colore[62] = new Color3f(0.2431373f, 0.2431373f, 0.2431373f);
            colore[63] = new Color3f(0.2470588f, 0.2470588f, 0.2470588f);
            colore[64] = new Color3f(0.2509804f, 0.2509804f, 0.2509804f);
            colore[65] = new Color3f(0.2549020f, 0.2549020f, 0.2549020f);
            colore[66] = new Color3f(0.2588235f, 0.2588235f, 0.2588235f);
            colore[67] = new Color3f(0.2627451f, 0.2627451f, 0.2627451f);
            colore[68] = new Color3f(0.2666667f, 0.2666667f, 0.2666667f);
            colore[69] = new Color3f(0.2705882f, 0.2705882f, 0.2705882f);
            colore[70] = new Color3f(0.2745098f, 0.2745098f, 0.2745098f);
            colore[71] = new Color3f(0.2784314f, 0.2784314f, 0.2784314f);
            colore[72] = new Color3f(0.2823529f, 0.2823529f, 0.2823529f);
            colore[73] = new Color3f(0.2862745f, 0.2862745f, 0.2862745f);
            colore[74] = new Color3f(0.2901961f, 0.2901961f, 0.2901961f);
            colore[75] = new Color3f(0.2941176f, 0.2941176f, 0.2941176f);
            colore[76] = new Color3f(0.2980392f, 0.2980392f, 0.2980392f);
            colore[77] = new Color3f(0.3019608f, 0.3019608f, 0.3019608f);
            colore[78] = new Color3f(0.3058824f, 0.3058824f, 0.3058824f);
            colore[79] = new Color3f(0.3098039f, 0.3098039f, 0.3098039f);
            colore[80] = new Color3f(0.3137255f, 0.3137255f, 0.3137255f);
            colore[81] = new Color3f(0.3176471f, 0.3176471f, 0.3176471f);
            colore[82] = new Color3f(0.3215686f, 0.3215686f, 0.3215686f);
            colore[83] = new Color3f(0.3254902f, 0.3254902f, 0.3254902f);
            colore[84] = new Color3f(0.3294118f, 0.3294118f, 0.3294118f);
            colore[85] = new Color3f(0.3333333f, 0.3333333f, 0.3333333f);
            colore[86] = new Color3f(0.3372549f, 0.3372549f, 0.3372549f);
            colore[87] = new Color3f(0.3411765f, 0.3411765f, 0.3411765f);
            colore[88] = new Color3f(0.3450980f, 0.3450980f, 0.3450980f);
            colore[89] = new Color3f(0.3490196f, 0.3490196f, 0.3490196f);
            colore[90] = new Color3f(0.3529412f, 0.3529412f, 0.3529412f);
            colore[91] = new Color3f(0.3568627f, 0.3568627f, 0.3568627f);
            colore[92] = new Color3f(0.3607843f, 0.3607843f, 0.3607843f);
            colore[93] = new Color3f(0.3647059f, 0.3647059f, 0.3647059f);
            colore[94] = new Color3f(0.3686275f, 0.3686275f, 0.3686275f);
            colore[95] = new Color3f(0.3725490f, 0.3725490f, 0.3725490f);
            colore[96] = new Color3f(0.3764706f, 0.3764706f, 0.3764706f);
            colore[97] = new Color3f(0.3803922f, 0.3803922f, 0.3803922f);
            colore[98] = new Color3f(0.3843137f, 0.3843137f, 0.3843137f);
            colore[99] = new Color3f(0.3882353f, 0.3882353f, 0.3882353f);
            colore[100] = new Color3f(0.3921569f, 0.3921569f, 0.3921569f);
            colore[101] = new Color3f(0.3960784f, 0.3960784f, 0.3960784f);
            colore[102] = new Color3f(0.4000000f, 0.4000000f, 0.4000000f);
            colore[103] = new Color3f(0.4039216f, 0.4039216f, 0.4039216f);
            colore[104] = new Color3f(0.4078431f, 0.4078431f, 0.4078431f);
            colore[105] = new Color3f(0.4117647f, 0.4117647f, 0.4117647f);
            colore[106] = new Color3f(0.4156863f, 0.4156863f, 0.4156863f);
            colore[107] = new Color3f(0.4196078f, 0.4196078f, 0.4196078f);
            colore[108] = new Color3f(0.4235294f, 0.4235294f, 0.4235294f);
            colore[109] = new Color3f(0.4274510f, 0.4274510f, 0.4274510f);
            colore[110] = new Color3f(0.4313725f, 0.4313725f, 0.4313725f);
            colore[111] = new Color3f(0.4352941f, 0.4352941f, 0.4352941f);
            colore[112] = new Color3f(0.4392157f, 0.4392157f, 0.4392157f);
            colore[113] = new Color3f(0.4431373f, 0.4431373f, 0.4431373f);
            colore[114] = new Color3f(0.4470588f, 0.4470588f, 0.4470588f);
            colore[115] = new Color3f(0.4509804f, 0.4509804f, 0.4509804f);
            colore[116] = new Color3f(0.4549020f, 0.4549020f, 0.4549020f);
            colore[117] = new Color3f(0.4588235f, 0.4588235f, 0.4588235f);
            colore[118] = new Color3f(0.4627451f, 0.4627451f, 0.4627451f);
            colore[119] = new Color3f(0.4666667f, 0.4666667f, 0.4666667f);
            colore[120] = new Color3f(0.4705882f, 0.4705882f, 0.4705882f);
            colore[121] = new Color3f(0.4745098f, 0.4745098f, 0.4745098f);
            colore[122] = new Color3f(0.4784314f, 0.4784314f, 0.4784314f);
            colore[123] = new Color3f(0.4823529f, 0.4823529f, 0.4823529f);
            colore[124] = new Color3f(0.4862745f, 0.4862745f, 0.4862745f);
            colore[125] = new Color3f(0.4901961f, 0.4901961f, 0.4901961f);
            colore[126] = new Color3f(0.4941176f, 0.4941176f, 0.4941176f);
            colore[127] = new Color3f(0.4980392f, 0.4980392f, 0.4980392f);
            colore[128] = new Color3f(0.5019608f, 0.5019608f, 0.5019608f);
            colore[129] = new Color3f(0.5058824f, 0.5058824f, 0.5058824f);
            colore[130] = new Color3f(0.5098039f, 0.5098039f, 0.5098039f);
            colore[131] = new Color3f(0.5137255f, 0.5137255f, 0.5137255f);
            colore[132] = new Color3f(0.5176471f, 0.5176471f, 0.5176471f);
            colore[133] = new Color3f(0.5215686f, 0.5215686f, 0.5215686f);
            colore[134] = new Color3f(0.5254902f, 0.5254902f, 0.5254902f);
            colore[135] = new Color3f(0.5294118f, 0.5294118f, 0.5294118f);
            colore[136] = new Color3f(0.5333333f, 0.5333333f, 0.5333333f);
            colore[137] = new Color3f(0.5372549f, 0.5372549f, 0.5372549f);
            colore[138] = new Color3f(0.5411765f, 0.5411765f, 0.5411765f);
            colore[139] = new Color3f(0.5450980f, 0.5450980f, 0.5450980f);
            colore[140] = new Color3f(0.5490196f, 0.5490196f, 0.5490196f);
            colore[141] = new Color3f(0.5529412f, 0.5529412f, 0.5529412f);
            colore[142] = new Color3f(0.5568627f, 0.5568627f, 0.5568627f);
            colore[143] = new Color3f(0.5607843f, 0.5607843f, 0.5607843f);
            colore[144] = new Color3f(0.5647059f, 0.5647059f, 0.5647059f);
            colore[145] = new Color3f(0.5686275f, 0.5686275f, 0.5686275f);
            colore[146] = new Color3f(0.5725490f, 0.5725490f, 0.5725490f);
            colore[147] = new Color3f(0.5764706f, 0.5764706f, 0.5764706f);
            colore[148] = new Color3f(0.5803922f, 0.5803922f, 0.5803922f);
            colore[149] = new Color3f(0.5843137f, 0.5843137f, 0.5843137f);
            colore[150] = new Color3f(0.5882353f, 0.5882353f, 0.5882353f);
            colore[151] = new Color3f(0.5921569f, 0.5921569f, 0.5921569f);
            colore[152] = new Color3f(0.5960784f, 0.5960784f, 0.5960784f);
            colore[153] = new Color3f(0.6000000f, 0.6000000f, 0.6000000f);
            colore[154] = new Color3f(0.6039216f, 0.6039216f, 0.6039216f);
            colore[155] = new Color3f(0.6078431f, 0.6078431f, 0.6078431f);
            colore[156] = new Color3f(0.6117647f, 0.6117647f, 0.6117647f);
            colore[157] = new Color3f(0.6156863f, 0.6156863f, 0.6156863f);
            colore[158] = new Color3f(0.6196078f, 0.6196078f, 0.6196078f);
            colore[159] = new Color3f(0.6235294f, 0.6235294f, 0.6235294f);
            colore[160] = new Color3f(0.6274510f, 0.6274510f, 0.6274510f);
            colore[161] = new Color3f(0.6313725f, 0.6313725f, 0.6313725f);
            colore[162] = new Color3f(0.6352941f, 0.6352941f, 0.6352941f);
            colore[163] = new Color3f(0.6392157f, 0.6392157f, 0.6392157f);
            colore[164] = new Color3f(0.6431373f, 0.6431373f, 0.6431373f);
            colore[165] = new Color3f(0.6470588f, 0.6470588f, 0.6470588f);
            colore[166] = new Color3f(0.6509804f, 0.6509804f, 0.6509804f);
            colore[167] = new Color3f(0.6549020f, 0.6549020f, 0.6549020f);
            colore[168] = new Color3f(0.6588235f, 0.6588235f, 0.6588235f);
            colore[169] = new Color3f(0.6627451f, 0.6627451f, 0.6627451f);
            colore[170] = new Color3f(0.6666667f, 0.6666667f, 0.6666667f);
            colore[171] = new Color3f(0.6705882f, 0.6705882f, 0.6705882f);
            colore[172] = new Color3f(0.6745098f, 0.6745098f, 0.6745098f);
            colore[173] = new Color3f(0.6784314f, 0.6784314f, 0.6784314f);
            colore[174] = new Color3f(0.6823529f, 0.6823529f, 0.6823529f);
            colore[175] = new Color3f(0.6862745f, 0.6862745f, 0.6862745f);
            colore[176] = new Color3f(0.6901961f, 0.6901961f, 0.6901961f);
            colore[177] = new Color3f(0.6941176f, 0.6941176f, 0.6941176f);
            colore[178] = new Color3f(0.6980392f, 0.6980392f, 0.6980392f);
            colore[179] = new Color3f(0.7019608f, 0.7019608f, 0.7019608f);
            colore[180] = new Color3f(0.7058824f, 0.7058824f, 0.7058824f);
            colore[181] = new Color3f(0.7098039f, 0.7098039f, 0.7098039f);
            colore[182] = new Color3f(0.7137255f, 0.7137255f, 0.7137255f);
            colore[183] = new Color3f(0.7176471f, 0.7176471f, 0.7176471f);
            colore[184] = new Color3f(0.7215686f, 0.7215686f, 0.7215686f);
            colore[185] = new Color3f(0.7254902f, 0.7254902f, 0.7254902f);
            colore[186] = new Color3f(0.7294118f, 0.7294118f, 0.7294118f);
            colore[187] = new Color3f(0.7333333f, 0.7333333f, 0.7333333f);
            colore[188] = new Color3f(0.7372549f, 0.7372549f, 0.7372549f);
            colore[189] = new Color3f(0.7411765f, 0.7411765f, 0.7411765f);
            colore[190] = new Color3f(0.7450980f, 0.7450980f, 0.7450980f);
            colore[191] = new Color3f(0.7490196f, 0.7490196f, 0.7490196f);
            colore[192] = new Color3f(0.7529412f, 0.7529412f, 0.7529412f);
            colore[193] = new Color3f(0.7568627f, 0.7568627f, 0.7568627f);
            colore[194] = new Color3f(0.7607843f, 0.7607843f, 0.7607843f);
            colore[195] = new Color3f(0.7647059f, 0.7647059f, 0.7647059f);
            colore[196] = new Color3f(0.7686275f, 0.7686275f, 0.7686275f);
            colore[197] = new Color3f(0.7725490f, 0.7725490f, 0.7725490f);
            colore[198] = new Color3f(0.7764706f, 0.7764706f, 0.7764706f);
            colore[199] = new Color3f(0.7803922f, 0.7803922f, 0.7803922f);
            colore[200] = new Color3f(0.7843137f, 0.7843137f, 0.7843137f);
            colore[201] = new Color3f(0.7882353f, 0.7882353f, 0.7882353f);
            colore[202] = new Color3f(0.7921569f, 0.7921569f, 0.7921569f);
            colore[203] = new Color3f(0.7960784f, 0.7960784f, 0.7960784f);
            colore[204] = new Color3f(0.8000000f, 0.8000000f, 0.8000000f);
            colore[205] = new Color3f(0.8039216f, 0.8039216f, 0.8039216f);
            colore[206] = new Color3f(0.8078431f, 0.8078431f, 0.8078431f);
            colore[207] = new Color3f(0.8117647f, 0.8117647f, 0.8117647f);
            colore[208] = new Color3f(0.8156863f, 0.8156863f, 0.8156863f);
            colore[209] = new Color3f(0.8196078f, 0.8196078f, 0.8196078f);
            colore[210] = new Color3f(0.8235294f, 0.8235294f, 0.8235294f);
            colore[211] = new Color3f(0.8274510f, 0.8274510f, 0.8274510f);
            colore[212] = new Color3f(0.8313725f, 0.8313725f, 0.8313725f);
            colore[213] = new Color3f(0.8352941f, 0.8352941f, 0.8352941f);
            colore[214] = new Color3f(0.8392157f, 0.8392157f, 0.8392157f);
            colore[215] = new Color3f(0.8431373f, 0.8431373f, 0.8431373f);
            colore[216] = new Color3f(0.8470588f, 0.8470588f, 0.8470588f);
            colore[217] = new Color3f(0.8509804f, 0.8509804f, 0.8509804f);
            colore[218] = new Color3f(0.8549020f, 0.8549020f, 0.8549020f);
            colore[219] = new Color3f(0.8588235f, 0.8588235f, 0.8588235f);
            colore[220] = new Color3f(0.8627451f, 0.8627451f, 0.8627451f);
            colore[221] = new Color3f(0.8666667f, 0.8666667f, 0.8666667f);
            colore[222] = new Color3f(0.8705882f, 0.8705882f, 0.8705882f);
            colore[223] = new Color3f(0.8745098f, 0.8745098f, 0.8745098f);
            colore[224] = new Color3f(0.8784314f, 0.8784314f, 0.8784314f);
            colore[225] = new Color3f(0.8823529f, 0.8823529f, 0.8823529f);
            colore[226] = new Color3f(0.8862745f, 0.8862745f, 0.8862745f);
            colore[227] = new Color3f(0.8901961f, 0.8901961f, 0.8901961f);
            colore[228] = new Color3f(0.8941176f, 0.8941176f, 0.8941176f);
            colore[229] = new Color3f(0.8980392f, 0.8980392f, 0.8980392f);
            colore[230] = new Color3f(0.9019608f, 0.9019608f, 0.9019608f);
            colore[231] = new Color3f(0.9058824f, 0.9058824f, 0.9058824f);
            colore[232] = new Color3f(0.9098039f, 0.9098039f, 0.9098039f);
            colore[233] = new Color3f(0.9137255f, 0.9137255f, 0.9137255f);
            colore[234] = new Color3f(0.9176471f, 0.9176471f, 0.9176471f);
            colore[235] = new Color3f(0.9215686f, 0.9215686f, 0.9215686f);
            colore[236] = new Color3f(0.9254902f, 0.9254902f, 0.9254902f);
            colore[237] = new Color3f(0.9294118f, 0.9294118f, 0.9294118f);
            colore[238] = new Color3f(0.9333333f, 0.9333333f, 0.9333333f);
            colore[239] = new Color3f(0.9372549f, 0.9372549f, 0.9372549f);
            colore[240] = new Color3f(0.9411765f, 0.9411765f, 0.9411765f);
            colore[241] = new Color3f(0.9450980f, 0.9450980f, 0.9450980f);
            colore[242] = new Color3f(0.9490196f, 0.9490196f, 0.9490196f);
            colore[243] = new Color3f(0.9529412f, 0.9529412f, 0.9529412f);
            colore[244] = new Color3f(0.9568627f, 0.9568627f, 0.9568627f);
            colore[245] = new Color3f(0.9607843f, 0.9607843f, 0.9607843f);
            colore[246] = new Color3f(0.9647059f, 0.9647059f, 0.9647059f);
            colore[247] = new Color3f(0.9686275f, 0.9686275f, 0.9686275f);
            colore[248] = new Color3f(0.9725490f, 0.9725490f, 0.9725490f);
            colore[249] = new Color3f(0.9764706f, 0.9764706f, 0.9764706f);
            colore[250] = new Color3f(0.9803922f, 0.9803922f, 0.9803922f);
            colore[251] = new Color3f(0.9843137f, 0.9843137f, 0.9843137f);
            colore[252] = new Color3f(0.9882353f, 0.9882353f, 0.9882353f);
            colore[253] = new Color3f(0.9921569f, 0.9921569f, 0.9921569f);
            colore[254] = new Color3f(0.9960784f, 0.9960784f, 0.9960784f);
            colore[255] = new Color3f(1.0000000f, 1.0000000f, 1.0000000f);

        }

    }

    public void set_rock_type(String rockname, int blocknumber) {
        boolean nuovo = true;
        int type = -1;
        if (blocknumber == 0) {
            rocksnames.add(rockname);
            type = rocksnames.size();
            RockType[blocknumber] = type;
        } else {
            for (int i = 0; i < rocksnames.size(); i++) {
                String test = (String) rocksnames.get(i);
                if (rockname.equals(test)) {
                    nuovo = false;
                    type = i + 1;
                }
            }
            if (nuovo) {
                rocksnames.add(rockname);
                type = rocksnames.size();
                RockType[blocknumber] = type;
            } else {
                RockType[blocknumber] = type;
            }
        }

//Collections.sort(rocksnames);
        RockType[blocknumber] = RockType[blocknumber] - 1;
    }

    public ArrayList mylineAL(String lineainput) {
        ArrayList myline = new ArrayList();
        int tot_lung = lineainput.length();
        int inizio = 0;
        int fine = 0;
        String chiave = ",";
        while (fine != -1) {
            fine = lineainput.indexOf(chiave, inizio);
            if (fine > 0) {
                String sottostringa = lineainput.substring(inizio, fine);
                myline.add(sottostringa);
                inizio = fine + 1;
            }
        }
        String sottostringa = lineainput.substring(inizio, tot_lung);
        myline.add(sottostringa);
        return myline;
    }

    public boolean read_INCON_file(File file, int incon_type) {
        FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        String filename = file.getName();
        String linea;
        String linea2 = "";
        String linea_tmp = "";
        //scansione file per vedere di quanti blocchi � composto....
        double[][] tmp_values = new double[1][1];
        int n_var = 0;
        boolean read2lines = true;
        String[] INCON_name = new String[1];
        String[] INCON_MU = new String[1];;
        Boolean header = false;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            linea = (String) dis.readLine();
            if (!linea.contains("INCON")) {
                String error = "The file is not a valid INCON file";
                JOptionPane.showMessageDialog(null, error);
                return false;
            }

            int n_var_incon = 0;
            int pos_var_name = -1;

            if (linea.contains("TOUGH2ViewerHeader")) {
                header = true;
                pos_var_name = linea.lastIndexOf("TOUGH2ViewerHeader") + 18;
                String[] tmp = JoeStringUtils1.parseSpace(linea.substring(pos_var_name));
                n_var_incon = tmp.length / 2;
                INCON_name = new String[n_var_incon];
                INCON_MU = new String[n_var_incon];
                for (int i = 0; i < n_var_incon; i++) {
                    INCON_name[i] = tmp[i * 2];
                    INCON_MU[i] = tmp[i * 2 + 1];
                }
            }
            boolean endfile = true;

            int i_incon = 0;
            while (dis.available() != 0 || endfile || i_incon == nxyz) {
                if (i_incon > nxyz) {
                    String error = "Number of INCON is greather then number of MESH elements";
                    JOptionPane.showMessageDialog(null, error);
                    return false;
                }
                linea = (String) dis.readLine();
                if (linea.length() < 5 || linea.contains("+++") || linea == null || linea.startsWith("     ")) {
                    break;
                }
                if (linea.startsWith(" ") && linea.substring(6, 7) != " ") {
                    linea = linea.substring(1);//this remove the first space..
                }
                if (read2lines) {
                    linea2 = (String) dis.readLine();
                }
                if (i_incon == 0) {
                    if (read2lines && linea2.contains(BlockNames[i_incon + 1])) {
                        linea_tmp = linea2;
                        if (linea_tmp.startsWith(" ")) {
                            linea_tmp = linea_tmp.substring(1);//this remove the first space..
                        }
                        read2lines = false;
                    }
                }
                if (read2lines) {
                    linea = linea + " " + linea2;
                }
                String[] lineaarray = JoeStringUtils1.parseSpace(linea.substring(5));

                if (i_incon == 0) {
                    n_var = lineaarray.length;
                    tmp_values = new double[nxyz][n_var];
                    for (int i = 0; i < n_var; i++) {
                        tmp_values[i_incon][i] = Double.valueOf(lineaarray[i]);
                    }
                } else {
                    if (n_var != lineaarray.length) {
                        //
                        String error = "At eleme=[" + Integer.toString(i_incon) + "]we have different number of values...";
                        JOptionPane.showMessageDialog(null, error);
                        JOptionPane.showMessageDialog(null, linea);
                        return false;
                    }
                    for (int i = 0; i < n_var; i++) {
                        tmp_values[i_incon][i] = Double.valueOf(lineaarray[i]);
                    }
                }

                i_incon++;
                //here we recover the lost line...just the for the first losted one
                if (i_incon == 1 && read2lines == false) {
                    String[] lineaarray2 = JoeStringUtils1.parseSpace(linea_tmp.substring(5));
                    for (int i = 0; i < n_var; i++) {
                        tmp_values[i_incon][i] = Double.valueOf(lineaarray2[i]);
                    }
                    i_incon++;
                }

            }

            if (i_incon != nxyz) {
                String error = "Number of INCON differ from number of MESH file";
                JOptionPane.showMessageDialog(null, error);
                return false;
            }
            //adding points..
            String var_name[] = new String[n_var];

            for (int i = 0; i < n_var; i++) {
                var_name[i] = filename + "_" + Integer.toString(i);
            }
            int offset = var_name.length;
            
            int timesteps = Tough2Viewer.dataobj.get_TimeSteps();
            float[][][] dataArray_tmp = new float[nxyz][timesteps][number_of_variables];
            for (int i_b = 0; i_b < nxyz; i_b++) {
                for (int i = 0; i < offset; i++) {
                    for (int i_t = 0; i_t < timesteps; i_t++) {
                        dataArray_tmp[i_b][i_t][i] = dataArray[i_b][i_t][i];
                    }

                }
            }
            if (header && n_var == n_var_incon) {
                Tough2Viewer.dataobj.addVariableName(INCON_name);
                Tough2Viewer.dataobj.addVariableNameUM(INCON_MU);
            } else {
                Tough2Viewer.dataobj.addVariableName(var_name);
                Tough2Viewer.dataobj.addVariableNameUM(var_name);
            }

            Tough2Viewer.dataobj.dataArrayCreate(nxyz, timesteps);//qui si può fare di meglio
            for (int i_b = 0; i_b < nxyz; i_b++) {
                for (int i = 0; i < offset; i++) {
                    for (int i_t = 0; i_t < timesteps; i_t++) {
                        dataArray[i_b][i_t][i] = dataArray_tmp[i_b][i_t][i];
                    }

                }
            }
            for (int i_b = 0; i_b < nxyz; i_b++) {
                for (int i = 0; i < n_var; i++) {
                    for (int i_t = 0; i_t < timesteps; i_t++) {
                        Tough2Viewer.dataobj.set_dataArray(i_b, i_t, i + offset, (float) tmp_values[i_b][i]);
                    }
                }
            }
            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
            String error = "FileNotFoundException...";
            JOptionPane.showMessageDialog(null, error);
            return false;
        } catch (IOException e) {
            String error = "IOException...";
            JOptionPane.showMessageDialog(null, error);
            return false;
        }
        Tough2Viewer.dataobj.set_dataLoaded(true);
        Tough2Viewer.dataobj.set_voronoi(true);
        Tough2Viewer.tough2viewerGUI.UpdateFileInformation();
        Tough2Viewer.dataobj.set_INIT_ROI();
        return true;
    }

    public boolean read_tough2viewer_settings() {
        File file = new File("tough2viewer.ini");
        boolean exists = (new File("tough2viewer.ini")).exists();
        if (!exists) {
            write_tough2viewer_settings();
        }
        FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        String linea;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            String key;
            while (dis.available() != 0) {
                linea = dis.readLine();
                key = "Shrink_factor=";
                if (linea.contains(key)) {
                    String value_str = linea.substring(linea.lastIndexOf(key) + key.length());
                    contrazione = Double.parseDouble(value_str);
                }
                key = "3DBackground=";
                if (linea.contains(key)) {
                    String value_str = linea.substring(linea.lastIndexOf(key) + key.length());
                    String[] str_tmp = JoeStringUtils1.parseSpace(value_str);
                    bgColor.x = (float) Double.parseDouble(str_tmp[0]);
                    bgColor.y = (float) Double.parseDouble(str_tmp[1]);
                    bgColor.z = (float) Double.parseDouble(str_tmp[2]);
                }
                key = "3D_Lights=";
                if (linea.contains(key)) {
                    String value_str = linea.substring(linea.lastIndexOf(key) + key.length());
                    String[] str_tmp = JoeStringUtils1.parseSpace(value_str);
                    for (int i = 0; i < 4; i++) {
                        if (Integer.parseInt(str_tmp[i]) == 1) {
                            light_3D[i] = true;
                        }
                    }
                }
                key = "3D_axis=";
                if (linea.contains(key)) {
                    String value_str = linea.substring(linea.lastIndexOf(key) + key.length());
                    if (Integer.parseInt(value_str) == 1) {
                        axis3dvisible = true;
                    } else {
                        axis3dvisible = false;
                    }
                }
                key = "FormatDouble=";
                if (linea.contains(key)) {
                    String value_str = linea.substring(linea.lastIndexOf(key) + key.length());
                    Tough2Viewer.dataobj.FormatDouble = value_str;
                }

            }

        } catch (IOException pippo) {

        }
        return true;
    }

    public boolean write_tough2viewer_settings() {
        String strFilePath = "tough2viewer.ini";
        try {
            // Open an output stream
            FileOutputStream fos = new FileOutputStream(strFilePath, false);
            PrintStream ps;
            // Print a line of text
            ps = new PrintStream(fos);
            String linea_out;
            linea_out = "Shrink_factor=" + Double.toString(contrazione) + "\n";
            ps.println(linea_out);
            linea_out = "3DBackground=" + Double.toString(bgColor.x) + " " + Double.toString(bgColor.y) + " " + Double.toString(bgColor.z) + "\n";
            ps.println(linea_out);
            linea_out = "3D_Lights=";
            for (int i = 0; i < 4; i++) {
                int value = 0;
                if (light_3D[i]) {
                    value = 1;
                }
                linea_out = linea_out + " " + Integer.toString(value);
            }
            linea_out = linea_out + "\n";
            ps.println(linea_out);
            int value = 0;
            if (axis3dvisible) {
                value = 1;
            }
            linea_out = "3D_axis=" + Integer.toString(value) + "\n";
            ps.println(linea_out);
            linea_out = "FormatDouble=" + Tough2Viewer.dataobj.FormatDouble + "\n";
            ps.println(linea_out);

            fos.close();
        } // Catches any error conditions
        catch (IOException e) {
            String output = "Unable to write file tough2viewer.ini";
            Tough2Viewer.toLogFile(output);
            System.exit(-1);
        }

        return true;
    }

    public boolean read_MESH_file(File file, int gridtype)//structured:gridtype=0;unstructured:gridtype=1;voro++=2
    {
        boolean read_index = false;
        String FilePath = file.getAbsolutePath();
        String FilePathString = file.getPath();

        set_WorkingPath(FilePath);
        float xmin_l = +1.0e20f;
        float xmax_l = -1.0e20f;
        float ymin_l = +1.0e20f;
        float ymax_l = -1.0e20f;
        float zmin_l = +1.0e20f;
        float zmax_l = -1.0e20f;
        FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        String linea;
        //scansione file per vedere di quanti blocchi � composto....
        ArrayList TotalLine = new ArrayList();
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            linea = (String) dis.readLine();
            boolean end_elem = true;
            int nxyz_temp = 0;
            boolean end_conne = true;
            while (dis.available() != 0 || end_conne) {
                while (end_elem) {
                    linea = (String) dis.readLine();
                    String sottostringa;
                    ArrayList SingolLine = new ArrayList();
                    if (linea.length() < 10) {
                        nxyz_temp = TotalLine.size();
                        set_nxyz(nxyz_temp);
                        XoYoZoArrayCreate(nxyz_temp);
                        dimXArrayCreate(nxyz_temp);
                        dimYArrayCreate(nxyz_temp);
                        dimZArrayCreate(nxyz_temp);
                        BlockNamesArrayCreate(nxyz_temp);
                        RockTypeArrayCreate(nxyz_temp);
                        BlockVolume = new double[nxyz_temp];
                        Block_n_conne = new float[nxyz_temp];
                        if (gridtype == 1) {
                            //inizializzazioni come in read_IN_file
                            XoYoZoArrayCreate(nxyz);
                            dimZArrayCreate(nxyz);
                            BlockNamesArrayCreate(nxyz);
                            RockTypeArrayCreate(nxyz);
                            voronoiGeometryBox = new ArrayList[nxyz];
                        }
                        for (int i = 0; i < nxyz_temp; i++) {
                            ArrayList subline = (ArrayList) TotalLine.get(i);
                            String blockName = (String) subline.get(0);
                            String blockMaterialType = (String) subline.get(1);
                            String xos = (String) subline.get(2);
                            String yos = (String) subline.get(3);
                            String zos = (String) subline.get(4);
                            String volume_s = (String) subline.get(5);
                            if (xos.contains("          ")) {
                                xos = "0.0";
                            }
                            if (yos.contains("          ")) {
                                yos = "0.0";
                            }
                            if (zos.contains("          ")) {
                                zos = "0.0";
                            }
                            Float xo;
                            Float yo;
                            Float zo;
                            Double volume_f;
                            try {
                                xo = Float.parseFloat(xos);
                                yo = Float.parseFloat(yos);
                                zo = Float.parseFloat(zos);
                                volume_f = Double.parseDouble(volume_s);
                            } catch (Exception es) {
                                String a = es.toString();
                                String error = "String Conversion error of" + xos + ", " + yos + ", " + zos + ", " + volume_s;
                                JOptionPane.showMessageDialog(null, error);
                                JOptionPane.showMessageDialog(null, a);
                                return false;
                            }
                            xmin_l=min(xo,xmin_l);
                            ymin_l=min(yo,ymin_l);
                            zmin_l=min(zo,zmin_l);
                            xmax_l=max(xo,xmax_l);
                            ymax_l=max(yo,ymax_l);
                            zmax_l=max(zo,zmax_l);
                            
                           

                            set_BlockName(blockName, i);
                            set_rock_type(blockMaterialType, i);
                            BlockVolume[i] = volume_f;
//                        if(volume_f>=big_float){BlockVolume[i]=big_float;
//                        }else{BlockVolume[i]=volume_f;}

                            if (gridtype != 2) {
                                set_Xo(xo, i);
                                set_Yo(yo, i);
                                set_Zo(zo, i);
                            } else {
                                ArrayList singleBox = (ArrayList) Tough2Viewer.dataobj.VoroPPData.get(i);
                                double[] centerD = (double[]) singleBox.get(1);
                                set_Xo((float) centerD[0], i);
                                set_Yo((float) centerD[1], i);
                                set_Zo((float) centerD[2], i);
                            }
                            if (gridtype == 1) {
                                voronoiGeometryBox[i] = new ArrayList();
                                Point2d punto = new Point2d(xo, yo);
                                voronoiGeometryBox[i].add(punto);
                            }

                        }
                        end_elem = false;
                    } else {
                        sottostringa = linea.substring(0, eleme_lenght);
                        SingolLine.add(sottostringa);
                        sottostringa = linea.substring(15, 20);
                        SingolLine.add(sottostringa);
                        if (linea.length() < 60) {
                            String error = "Check MESH file. Line lenght<60 (" + Integer.toString(linea.length()) + ")";
                            JOptionPane.showMessageDialog(null, error);
                            return false;
                        } else {
                            sottostringa = linea.substring(50, 60);
                            SingolLine.add(sottostringa);
                        }
                        if (linea.length() < 70) {
                            sottostringa = "0.0";
                            SingolLine.add(sottostringa);
                        } else {
                            sottostringa = linea.substring(60, 70);
                            SingolLine.add(sottostringa);
                        }
                        if (linea.length() < 80) {
                            sottostringa = "0.0";
                            SingolLine.add(sottostringa);
                        } else {
                            sottostringa = linea.substring(70, 80);
                            SingolLine.add(sottostringa);
                        }
                        sottostringa = linea.substring(20, 30);
                        SingolLine.add(sottostringa);
                        TotalLine.add(SingolLine);
                    }
                }//fine while2

                // DA qui leggo CONNE:
                ////lettura CONNE....
                //conneArrayList.clear();
                conneArrayList = new ArrayList();
                linea = (String) dis.readLine();
                if (linea.contains("CONNE") || linea.contains("conne")) {
                    int i = 0;
                } else {
                    String error = "Check MESH file. CONNE or conne keyword not found";
                    JOptionPane.showMessageDialog(null, error);
                }
                //

                while (end_conne) {
                    linea = (String) dis.readLine();
                    //System.out.print(linea);
                    if (linea == null) {
                        end_conne = false;
                    } else if (linea.contains("+++")) {
                        end_conne = false;
                        read_index = true;
                        ///se c'� questo sembrano esserci anche gli indici delle connessioni
                    } else if (linea.contains("Connection information after")) {
                        end_conne = false;
                        read_index = false;
                        ///se c'� questo sembrano esserci anche gli indici delle connessioni
                    } else if (linea.length() < 10) {
                        end_conne = false;
                    } else {

                        //qui sarebbe da mettere che legge le righe
                        //poi si fa il controllo di chi sono....
                        conneArrayList.add(linea);
                    }
                }
                //qui ho finito di leggere le conne!!!!
                //da qui in poi devo leggere tanti integer(n connesioni per due) per sapere gli indici
                int n_con = conneArrayList.size();
                ConnectionTable = new int[n_con][2];
                ConnectionTableToBeCreate = false;
                InterfaceArea = new float[n_con];
                int i1 = 0;
                if (read_index)//ho trovato +++. Eseguo lettura degli indici
                {
                    boolean read_test = true;
                    int number_of_ints = 0;
                    int len_int = 0;
                    while (dis.available() != 0) {
                        linea = dis.readLine();
                        if (linea.contains("Connection information after")) {
                            read_test = false;
                            read_index = false;
                            ///se c'� questo sembrano esserci anche gli indici delle connessioni
                        }
                        if (read_test) {
                            String[] test = JoeStringUtils1.parseSpace(linea);
                            number_of_ints = test.length;
                            read_test = false;
                            if (number_of_ints == 0) {
                                read_index = false;
                                break;
                            }
                            len_int = (int) linea.length() / number_of_ints;
                        }
                        for (int i = 0; i < linea.length(); i = i + len_int * 2) {
                            int i_first;
                            int i_second;
                            try {
                                i_first = (int) Float.parseFloat(linea.substring(i, i + len_int));
                                i_second = (int) Float.parseFloat(linea.substring(i + len_int, i + len_int * 2));
                            } catch (Exception e) {
                                read_index = false;
                                break;
                            }
                            if (i1 >= n_con) {
                                read_index = false;
                                break;
                            }
                            ConnectionTable[i1][0] = i_first - 1;
                            ConnectionTable[i1][1] = i_second - 1;
                            Block_n_conne[i_first - 1]++;
                            Block_n_conne[i_second - 1]++;
                            i1++;
                        }
                        if (linea.length() < 80) {
                            end_conne = false;
                        }
                    }
                    if (i1 == n_con) {
                        //ok, abbiamo letto tutti n_con indici 
                    } else {
                        //azzo, mancano alcuni n_con indici
                        read_index = false;
                    }
                }
                if (read_index == false) {
                    //qui devo trovare tutte gli indici dei blocchi
                    //per le connessioni. Lo far� o con il brute force
                    //o con ricerca binaria

                    for (int i = 0; i < n_con; i++) {
                        int[] n = new int[2];
                        String temp = (String) conneArrayList.get(i);
                        String block1 = temp.substring(0, eleme_lenght);
                        String block2 = temp.substring(eleme_lenght, 2 * eleme_lenght);
                        if (TwoBlockSearch(block1, block2, n) == false) {

                        } else {
                            ConnectionTable[i][0] = n[0];
                            ConnectionTable[i][1] = n[1];
                            Block_n_conne[n[0]]++;
                            Block_n_conne[n[1]]++;
                        }
                    }
                }
                //gridtype==0   >>> griglia strutturata
                if (gridtype == 0) {

                    //da qui ora devo ciclare per trovare le distanze (dimensioni)
                    //occorre anche vedere quante volte un blocco � considerato
                    //per fare il raddoppio dimensione solo di quelli con una dim sola
                    //
                    int[][] dimxyz = new int[nxyz_temp][3];
                    for (int i = 0; i < n_con; i++) {
                        linea = (String) conneArrayList.get(i);
                        String sottostringa = linea.substring(30, 40);
                        float dimBlock1 = Float.parseFloat(sottostringa);
                        sottostringa = linea.substring(40, 50);
                        float dimBlock2 = Float.parseFloat(sottostringa);
                        sottostringa = linea.substring(50, 60);
                        InterfaceArea[i] = Float.parseFloat(sottostringa);
                        //vedere quale � la direzione!!!!!
                        int index_block1 = ConnectionTable[i][0];
                        int index_block2 = ConnectionTable[i][1];
                        int griglia_cartesiana = 0;
                        if (get_Xo(index_block1) != get_Xo(index_block2)) {
                            //iTough2Viewer.dataobj.set_DimBlockX(dimBlock2,index_block2);
                            DimBlockX[index_block1] = DimBlockX[index_block1] + dimBlock1;
                            DimBlockX[index_block2] = DimBlockX[index_block2] + dimBlock2;
                            dimxyz[index_block1][0]++;
                            dimxyz[index_block2][0]++;
                            griglia_cartesiana++;
                        }
                        if (get_Yo(index_block1) != get_Yo(index_block2)) {
                            DimBlockY[index_block1] = DimBlockY[index_block1] + dimBlock1;
                            DimBlockY[index_block2] = DimBlockY[index_block2] + dimBlock2;
                            dimxyz[index_block1][1]++;
                            dimxyz[index_block2][1]++;
                            griglia_cartesiana++;
                        }
                        if (get_Zo(index_block1) != get_Zo(index_block2)) {
                            DimBlockZ[index_block1] = DimBlockZ[index_block1] + dimBlock1;
                            DimBlockZ[index_block2] = DimBlockZ[index_block2] + dimBlock2;
                            dimxyz[index_block1][2]++;
                            dimxyz[index_block2][2]++;
                            griglia_cartesiana++;
                        }
                        if (griglia_cartesiana > 1) {
                            String error = "Check MESH file. Not cartesian grid?";
                            JOptionPane.showMessageDialog(null, error);
                            return false;
                        }
                        if (griglia_cartesiana == 0) {
                            String error = "BlockName not found in CONNE table";
                            JOptionPane.showMessageDialog(null, error);
                            error = linea;
                            JOptionPane.showMessageDialog(null, error);
                            return false;
                        }
                    }
                    //Questo ciclo ora verifica se un blocco � stato considerato 2 volte.
                    // se un blocco � stato preso solo una volta(blocchi di bordo) allora 
                    //la dimesione � raddoppiata. Discutibile ma al momento niente di meglio
                    for (int i = 0; i < nxyz_temp; i++) {
                        if (dimxyz[i][0] == 1) {
                            DimBlockX[i] = DimBlockX[i] * 2;
                        }
                        if (dimxyz[i][1] == 1) {
                            DimBlockY[i] = DimBlockY[i] * 2;
                        }
                        if (dimxyz[i][2] == 1) {
                            DimBlockZ[i] = DimBlockZ[i] * 2;
                        }
                    }
                } else if (gridtype == 1) {
                    //griglia non strutturata
                    int[][] dimxyz = new int[nxyz_temp][3];
                    for (int i = 0; i < n_con; i++) {
                        linea = (String) conneArrayList.get(i);
                        String sottostringa = linea.substring(30, 40);
                        float dimBlock1 = Float.parseFloat(sottostringa);
                        sottostringa = linea.substring(40, 50);
                        float dimBlock2 = Float.parseFloat(sottostringa);
                        //vedere quale � la direzione!!!!!
                        int index_block1 = ConnectionTable[i][0];
                        int index_block2 = ConnectionTable[i][1];
                        int griglia_cartesiana = 0;
                        if (get_Zo(index_block1) != get_Zo(index_block2)) {
                            DimBlockZ[index_block1] = DimBlockZ[index_block1] + dimBlock1;
                            DimBlockZ[index_block2] = DimBlockZ[index_block2] + dimBlock2;
                            dimxyz[index_block1][2]++;
                            dimxyz[index_block2][2]++;
                            griglia_cartesiana++;
                        }
                    }
                    //Questo ciclo ora verifica se un blocco � stato considerato 2 volte.
                    // se un blocco � stato preso solo una volta(blocchi di bordo) allora 
                    //la dimesione � raddoppiata. Discutibile ma al momento niente di meglio
                    for (int i = 0; i < nxyz_temp; i++) {

                        if (dimxyz[i][2] == 1) {
                            DimBlockZ[i] = DimBlockZ[i] * 2;
                        }

                    }
                }
                set_n_connections(n_con);
                break;
            }
            if (gridtype != 2) {
                set_xmin(xmin_l);
                set_xmax(xmax_l);
                set_ymin(ymin_l);
                set_ymax(ymax_l);
                set_zmin(zmin_l);
                set_zmax(zmax_l);
            }
            fis.close();
            bis.close();
            dis.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        initializeRockType();
        mesh_file_is_loaded = true;
        return true;
    }

    public boolean read_SEGMT_file() {
        File fileSegmt = new File(SegmtFileName);
        FileInputStream fisSegmt;
        BufferedInputStream bisSegmt;
        DataInputStream disSegmt;
        String linea;
        int[] n;
        n = new int[12];
        n[1] = 0;
        n[2] = 15;
        n[3] = 30;
        n[4] = 45;
        n[5] = 60;
        n[6] = 63;
        n[7] = 65;
        n[8] = 70;
        n[9] = 73;
        n[10] = 75;
        n[11] = 89;
        try {
            fisSegmt = new FileInputStream(fileSegmt);
            bisSegmt = new BufferedInputStream(fisSegmt);
            disSegmt = new DataInputStream(bisSegmt);
            int box_index = 0;
            //qui al posto di while devo mettere di leggere nxy blocchi....di sicuro..con controllo block names...
            //meglio leggere solo la seconda coopia di cooppie che tanto � uguale. Controllare poi l'ordine antiorario???
            String currentBlockName = "";
            int start = 0;
            while (disSegmt.available() != 0) {

                int inizio = 0;
                int fine = 0;
                String sottostringa;
                linea = (String) disSegmt.readLine();

                inizio = n[7];
                fine = n[8];
                sottostringa = linea.substring(inizio, fine);
                if (start == 0) {
                    currentBlockName = sottostringa;
                    start++;
                } else if (sottostringa.equals(currentBlockName)) {
                    if (currentBlockName.equals(Tough2Viewer.dataobj.get_BlockName(box_index))) {
                        int WeAreOk = 1;
                    }
                } else {
                    box_index++;
                    currentBlockName = sottostringa;
                    if (currentBlockName.equals(Tough2Viewer.dataobj.get_BlockName(box_index))) {
                        int WeAreOk = 2;
                    }
                }

                /**
                 * **************************************
                 */
                inizio = n[3];
                fine = n[4];
                double value1 = 0.0f;
                double value2 = 0.0f;
                sottostringa = linea.substring(inizio, fine);
                try {
                    value1 = Double.parseDouble(sottostringa);
                    inizio = n[4];
                    fine = n[5];
                    sottostringa = linea.substring(inizio, fine);
                    value2 = Double.parseDouble(sottostringa);

                } catch (Exception e) {
                    String error = "Check SEGMT file. Double.parseDouble error";
                    JOptionPane.showMessageDialog(null, error);
                    JOptionPane.showMessageDialog(null, sottostringa);
                    return false;
                }
                Point2d punto = new Point2d(value1, value2);
                if (box_index < nxyz) {
                    voronoiGeometryBox[box_index].add(punto);
                }

            }
        } catch (IOException e) {
            String error = "Warning: File IOException";
            JOptionPane.showMessageDialog(null, error);
            error = e.toString();
            JOptionPane.showMessageDialog(null, error);
            return false;

        }

        return true;
    }

    boolean readDataFile3(File file, boolean readKper, JProgressBar myProgressBar) {
        boolean readOK = false;
        int timestep = 0;
        int endblock = 6;
        //inizio to read file
        FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        String linea;
        String UMString;
        String UMStringX1X2;
        String UMStringFLUX;
        String PT_line = "";
        String X1X2_line = "";
        int[] n_array = new int[0];
        int[] n_arrayX1X2 = new int[0];
        int[] n_arrayFLUX = new int[0];
        FluxFound = false;
        boolean X1X2Found = false;
        boolean continueread = false;
        myProgressBar.setValue(10);
        boolean Balla_reading_ok = true;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            //String PathTemp=file.getCanonicalPath();
            int riga;
            riga = 0;
            //primo ciclo per vedere quanti timestep abbiamo
            int timesteptotal = 0;
            int fine_elem2 = -1;
            linea = (String) dis.readLine();
            while (dis.available() != 0) {
                linea = (String) dis.readLine();
                int trovato;
                String chiave = "TOTAL TIME";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    linea = (String) dis.readLine();
                    timesteptotal = timesteptotal + 1;
                    chiave = "IND";
                    trovato = -1;
                    while (trovato < 0) {
                        linea = (String) dis.readLine();
                        trovato = linea.lastIndexOf(chiave);
                        if (linea.contains("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")) {
                            if (linea.startsWith(" @@@@@@@")) {
                                endblock = 6;
                            } else if (linea.startsWith("@@@@@@@")) {
                                endblock = 5;
                            }
                        }
                    }
                    if (timesteptotal == 1) {
                        //Lettura variabili...
                        PT_line = linea;
                        int startvariabili = -1;
                        for (int i = trovato; i < linea.length(); i++) {
                            if (linea.regionMatches(i, " ", 0, 1)) {
                                startvariabili = i;
                                break;
                            }
                        }
                        String variabili = linea.substring(startvariabili);
                        String[] VariableNames = JoeStringUtils1.parseSpace(variabili);
                        Tough2Viewer.dataobj.set_VariableName(VariableNames);
                        UMString = (String) dis.readLine();
                        UMString = UMString.substring(startvariabili);
                        String[] UM = JoeStringUtils1.subDivision2Lines(variabili, UMString);
                        Tough2Viewer.dataobj.set_variables_UM(UM);
                        if (UM.length != VariableNames.length) {
                            String error = "Number of Var Name don't match number of Mes.Unit";
                            JOptionPane.showMessageDialog(null, error);
                            return readOK;
                        }
                        boolean emptyline = true;
                        while (emptyline) {
                            linea = (String) dis.readLine();
                            if (linea.length() > 5) {
                                emptyline = false;
                            }
                        }
                        String sottostringa = linea.substring(endblock);
                        n_array = JoeStringUtils1.subDivision(sottostringa);
                        if (n_array.length - 2 != VariableNames.length) {
                            String error = "Number of Var don't match number of Var Name";
                            JOptionPane.showMessageDialog(null, error);
                            return readOK;
                        }
                        Tough2Viewer.dataobj.set_number_of_variables(n_array.length - 2);
                        //CONTROLLO NOME BLOCCHI:::::
                        String nomeprimoblocco = linea.substring(endblock - 5, endblock);
                        String temp = Tough2Viewer.dataobj.get_BlockName(0);
                        if (nomeprimoblocco.contentEquals(temp)) {
                        } else if (continueread == false) {
                            String error = "Block name don't match";
                            JOptionPane.showMessageDialog(null, error);
                            error = "First:" + nomeprimoblocco + " Second:" + temp;
                            JOptionPane.showMessageDialog(null, error);
                            error = "Continue?";
                            int i = JOptionPane.showConfirmDialog(null, error);
                            if (i == JOptionPane.YES_OPTION) {
                                continueread = true;
                            }
                            if (i == JOptionPane.NO_OPTION) {
                                continueread = false;
                                return readOK;
                            }
                            if (i == JOptionPane.CANCEL_OPTION) {
                                return readOK;
                            }
                        }
                        for (int i = 1; i < nxyz; i++) {
                            linea = dis.readLine();
                            if (linea.length() < 3) {
                                while (linea.length() < 3) {
                                    linea = (String) dis.readLine();
                                }
                            }
                            chiave = "IND";
                            trovato = linea.lastIndexOf(chiave);
                            if (trovato >= 0) {
                                linea = (String) dis.readLine();
                                linea = (String) dis.readLine();
                                linea = (String) dis.readLine();
                            }
                            if (linea.contains("@@@@@@@@@@@")) {
                                String error = "Rows don't match number of Blocks";
                                JOptionPane.showMessageDialog(null, error);
                                return readOK;
                            }
                            nomeprimoblocco = linea.substring(endblock - 5, endblock);
                            if (nomeprimoblocco.contentEquals(Tough2Viewer.dataobj.get_BlockName(i))) {
                            } else if (continueread == false) {
                                String error = "Block name don't match with MESH or IN file";
                                JOptionPane.showMessageDialog(null, error);
                                return readOK;
                            }
                        }
                    }
                }
                chiave = "+++++++++ CONVERGENCE FAILURE";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    Tough2Viewer.tough2viewerGUI.UpdateInfoLabel(linea);
                }
                chiave = "+++++++++ FOR  10 CONSECUTIVE TIME STEPS HAVE CONVERGENCE ON ITER = 1";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    Tough2Viewer.tough2viewerGUI.UpdateInfoLabel(linea);
                }
                trovato = -1;
                chiave = "ELEM2";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0 && timesteptotal == 1 && FlowError == false) {
                    if (FluxFound == true) {
                        //FlowError=true;//questa istruzione è stata inserita MOMENTANEAMENTE
                        //perchè nel caso di blocchi di flussi multipli
                        //essi non vengono letti.
                        //Invece sarebbe il caso di leggerli
                        //FluxFound=false;
                    } else {
                        //linea=(String) dis.readLine();//legge le solite X righe vuote
                        //DA QUI DEVO RIFARE IL RAGIONAMENTO PER LA LETTURA FLUSSI E VARIABILI....
                        //siccome non � detto che i flussi siano allineati con ELEM2,
                        //allora occorre trovare il secondo elemento della prima connessione....
                        //e se per caso ritrovo un altro???
                        //Faccio cosi: guardo dove cadono i nomi dei blocchi.
                        //quando li trovo entrambi per la prima connessione allora li do buoni per tutti
                        FluxFound = true;
                        chiave = "INDEX";
                        trovato = linea.lastIndexOf(chiave);
                        String variabiliFLUX = linea.substring(trovato + chiave.length());
                        String[] VariableNamesFLUX = JoeStringUtils1.parseSpace(variabiliFLUX);
                        int n_temp = VariableNamesFLUX.length;
                        if (number_of_flow_variables > 0) {

                        }
                        set_variables_FLOW(VariableNamesFLUX);
                        //qui guardo se nel nome della variabile c'�
                        //Flow o vel. Se c'� vel metto=0, altrimenti 1.
                        FlowOrVel = new int[VariableNamesFLUX.length];
                        for (int i = 0; i < VariableNamesFLUX.length; i++) {
                            String temp = VariableNamesFLUX[i].toLowerCase();
                            if (temp.lastIndexOf("vel") >= 0) {
                                FlowOrVel[i] = 0;
                            } else {
                                FlowOrVel[i] = 1;
                            }
                        }
                        linea = (String) dis.readLine();
                        UMStringFLUX = linea.substring(trovato + chiave.length());
                        String[] UMFLUX = JoeStringUtils1.subDivision2Lines(variabiliFLUX, UMStringFLUX);
                        Tough2Viewer.dataobj.set_variables_UMFLOW(UMFLUX);
                        if (UMFLUX.length != VariableNamesFLUX.length) {
                            String error = "Number of Var Name FLUX don't match number of Mes.Unit FLUX";
                            JOptionPane.showMessageDialog(null, error);
                            FlowError = true;
                            return readOK;
                        }
                        linea = (String) dis.readLine();
                        if (linea.length() < 3) {
                            while (linea.length() < 3) {
                                linea = (String) dis.readLine();
                            }
                        }
                        int[] temp_Array = new int[4];
                        temp_Array[0] = 0;
                        temp_Array[1] = 0;
                        temp_Array[2] = 0;
                        temp_Array[3] = 0;
                        ///////////////////////////////////////
                        boolean elem1elem2 = true;
                        int first = -1;
                        for (int j = 0; j < 2; j++) {
                            for (int i = 0; i < nxyz; i++) {
                                if (first != i) {
                                    String blockname1 = BlockNames[i];
                                    trovato = linea.indexOf(blockname1, 0);
                                    if (trovato >= 0) {
                                        if (linea.substring(trovato - 1, trovato).contentEquals(" ")) //questo ulteriore controllo verifca se per caso il carattere precedente non � un o spazio
                                        //infatti se i nomi sono "2    " e "30   " si verifica che potrei trovare la stringa in
                                        //un nome simile, tipo "130  " e quindi sbagliare il tutto.
                                        //poi c'� anche il caso degli allineati a destra ma non lo guardiamo per ora..'
                                        {
                                            temp_Array[j * 2] = trovato;
                                            temp_Array[j * 2 + 1] = trovato + blockname1.length();
                                            first = i;
                                            if (j == 1) {
                                                elem1elem2 = false;
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (elem1elem2) {
                            String error = "can't find two valid block name in first line of flow";
                            JOptionPane.showMessageDialog(null, error);
                            JOptionPane.showMessageDialog(null, linea);
                            FluxFound = false;
                            FlowError = true;
                        }
                        fine_elem2 = max(temp_Array[1], temp_Array[3]);
                        sort(temp_Array);
                        /////////////////////////////////////////////////////////////////////////////////
                        String sottostringa = linea.substring(fine_elem2);
                        n_arrayFLUX = JoeStringUtils1.subDivision(sottostringa);
                        if (n_arrayFLUX.length - 2 != VariableNamesFLUX.length) {

                            String error = "Number of Var FLUX don't match number of Var Name FLUX";
                            JOptionPane.showMessageDialog(null, error);
                            FlowError = true;
                            FluxFound = false;
                            return readOK;
                        }

                        //ora devo considerare di prendere tutta la riga perch� mi servono le connessioni...
                        int[] temp_Array2 = new int[n_arrayFLUX.length + 4];
                        for (int i = 0; i < 4; i++) {
                            temp_Array2[i] = temp_Array[i];
                        }
                        for (int i = 0; i < n_arrayFLUX.length; i++) {
                            temp_Array2[i + 4] = n_arrayFLUX[i] + temp_Array[3];
                        }
                        n_arrayFLUX = new int[temp_Array2.length];
                        System.arraycopy(temp_Array2, 0, n_arrayFLUX, 0, n_arrayFLUX.length);
                        ///facciamo la tabella delle connessioni qui un a volta per tutte!!!!
                        int nc = get_n_connections();
                        String bl1;
                        String bl2;
                        int n[] = new int[2];
                        if (ConnectionTableToBeCreate) {
                            ConnectionTable = new int[nc][2];
                            bl1 = linea.substring(n_arrayFLUX[0], n_arrayFLUX[1]);
                            bl2 = linea.substring(n_arrayFLUX[2], n_arrayFLUX[3]);
                            if (TwoBlockSearch(bl1, bl2, n) == false) {
                                String error = "Connection Table problem for " + bl1 + " and " + bl2;
                                JOptionPane.showMessageDialog(null, error);
                                FlowError = true;
                                //return readOK;
                            }
                            ConnectionTable[0][0] = n[0];
                            ConnectionTable[0][1] = n[1];
                        }
                        //Partiamo da uno perch� siamo gi� sui dati
                        for (int i = 1; i < nc; i++) {
                            linea = (String) dis.readLine();
                            if (linea.length() < 3) {
                                while (linea.length() < 3) {
                                    linea = (String) dis.readLine();
                                }
                            }
                            chiave = "ELEM2";
                            trovato = linea.lastIndexOf(chiave);
                            if (trovato >= 0) {
                                linea = (String) dis.readLine();
                                boolean emptyline = true;
                                while (emptyline) {
                                    linea = (String) dis.readLine();
                                    if (linea.length() > 5) {
                                        emptyline = false;
                                    }
                                }

                                //linea=(String) dis.readLine();
                            }
                            if (linea.contains("@@@@@@@@@@@")) {
                                String error = "Flow rows don't match number of connections";
                                JOptionPane.showMessageDialog(null, error);
                                return readOK;
                            }
                            if (ConnectionTableToBeCreate) {
                                bl1 = linea.substring(n_arrayFLUX[0], n_arrayFLUX[1]);
                                bl2 = linea.substring(n_arrayFLUX[2], n_arrayFLUX[3]);
                                if (TwoBlockSearch(bl1, bl2, n) == false) {
                                    String error = "Connection Table problem for " + bl1 + " and " + bl2;
                                    JOptionPane.showMessageDialog(null, error);
                                    FlowError = true;
                                    //return readOK;
                                }
                                ConnectionTable[i][0] = n[0];
                                ConnectionTable[i][1] = n[1];
                            }
                        }
                        linea = (String) dis.readLine();
                        if (linea.length() < 3) {
                            while (linea.length() < 3) {
                                linea = (String) dis.readLine();
                            }
                        }
                        if (linea.contains("@@@@@@@@@@@")) {

                        } else {
                            String error = "Flow rows don't match number of connections";
                            JOptionPane.showMessageDialog(null, error);
                            return readOK;
                        }
                    }
                }
                if (readKper) {
                    if (X1X2Found == false) {
                        if (linea.lastIndexOf("ELEM") >= 0 & linea.lastIndexOf("INDEX") >= 0 & linea.lastIndexOf("GENER") < 0 & linea.lastIndexOf("ELEM2") < 0 & linea.lastIndexOf("GEN. RATE") < 0) {
                            if (linea.contentEquals(PT_line) || linea.lastIndexOf("GETINDEX") > 0) {
                            } else {
                                //abbiamo anche X1,DX1,...
                                //allora ripetiamo quanto fatto per le P,T,..
                                X1X2Found = true;
                                trovato = linea.lastIndexOf("INDEX");
                                String variabiliX1X2 = linea.substring(trovato + chiave.length());
                                X1X2_line = linea;
                                String[] VariableNamesX1X2 = JoeStringUtils1.parseSpace(variabiliX1X2);
                                Tough2Viewer.dataobj.addVariableName(VariableNamesX1X2);
                                linea = (String) dis.readLine();
                                String[] UMX1X2;
                                //aggiunto controllo:che non ci sia gi�
                                // il nome del primo blocco...questo per il well test sample, eos 9
                                int controllo = linea.lastIndexOf(BlockNames[0]);
                                if (linea.length() < 3 || controllo > 0) {
                                    UMX1X2 = new String[VariableNamesX1X2.length];
                                    for (int i = 0; i < VariableNamesX1X2.length; i++) {
                                        UMX1X2[i] = "number";
                                    }
                                } else {
                                    UMStringX1X2 = linea;
                                    UMStringX1X2 = UMStringX1X2.substring(trovato + chiave.length());
                                    UMX1X2 = JoeStringUtils1.subDivision2Lines(variabiliX1X2, UMStringX1X2);
                                }
                                Tough2Viewer.dataobj.addVariableNameUM(UMX1X2);
                                if (UMX1X2.length != VariableNamesX1X2.length) {
                                    String error = "Number of VarX1X2 Name don't match number of Mes.UniX1X2t";
                                    JOptionPane.showMessageDialog(null, error);
                                    return readOK;
                                }
                                if (controllo < 0) {
                                    linea = (String) dis.readLine();
                                    linea = (String) dis.readLine();
                                }
                                String sottostringa = linea.substring(endblock);
                                n_arrayX1X2 = JoeStringUtils1.subDivision(sottostringa);
                                if (n_arrayX1X2.length - 2 != VariableNamesX1X2.length) {
                                    String error = "Number of VarX1X2 don't match number of Var NameX1X2";

                                    JOptionPane.showMessageDialog(null, error);
                                    error = "Continue?";
                                    int i = JOptionPane.showConfirmDialog(null, error);
                                    if (i == JOptionPane.YES_OPTION) {

                                    }
                                    if (i == JOptionPane.NO_OPTION) {
                                        continueread = false;
                                        return readOK;
                                    }
                                    if (i == JOptionPane.CANCEL_OPTION) {
                                        return readOK;
                                    }

                                }
                                Tough2Viewer.dataobj.set_number_of_variables(n_array.length - 2 + n_arrayX1X2.length - 2);
                            }//
                        }
                    }
                }
                chiave = "NCON =";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    String sottostringa;
                    sottostringa = linea.substring(trovato + 6, trovato + 7 + 6);
                    int n_c_D = (int) Float.parseFloat(sottostringa);
                    Tough2Viewer.dataobj.set_n_connections(n_c_D);
                }
            }
            fis.close();
            bis.close();
            dis.close();
            if (readKper) {
                if (X1X2Found) {
                    Tough2Viewer.dataobj.set_Kper(true);
                } else {

//                String error="Warning: Kr not found.";
//                JOptionPane.showMessageDialog(null, error);
                }
            }
            Tough2Viewer.dataobj.dataArrayCreate(nxyz, timesteptotal);
            ///////aggiunta per flussi...
            if (FluxFound) {
                connectionsArrayCreate(get_n_connections());
            } else {

//            String error="Warning: Flow information not found.";
//            JOptionPane.showMessageDialog(null, error);
            }
            try {
                myProgressBar.setValue(50);
                Thread.sleep(100);
                myProgressBar.setValue(50);

            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            myProgressBar.setValue(50);

            //
            //inizio lettura vera
            //
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                linea = (String) dis.readLine();
                int trovato;
                String chiave = "OUTPUT DATA AFTER";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    String sottostringa;
                    String inizioLetturaTempoString = "THE TIME IS";
                    String fineLetturaTempoString = "DAY";
                    int inizio = linea.lastIndexOf(inizioLetturaTempoString) + inizioLetturaTempoString.length();
                    int fine = linea.lastIndexOf(fineLetturaTempoString);
                    sottostringa = linea.substring(inizio, fine);
                    Tough2Viewer.dataobj.set_Times(timestep, Float.parseFloat(sottostringa));
                    timestep = timestep + 1;
                    int progressbar = (int) (((float) timestep) / ((float) timesteptotal) * 50.0f) + 50;
                    myProgressBar.setValue(progressbar);
                    try {
                        myProgressBar.setValue(progressbar);
                        Thread.sleep(100);
                        myProgressBar.setValue(progressbar);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    chiave = "ELEM";
                    trovato = -1;
                    while (trovato < 0) {
                        linea = (String) dis.readLine();
                        trovato = linea.lastIndexOf(chiave);
                    }
                    linea = (String) dis.readLine();//le variabili
                    //linea=(String) dis.readLine();//le unit� di misura
                    for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
                        linea = (String) dis.readLine();

                        //Facciamo un controllo su ripetitori di riga?SI!!!
                        if (linea.length() < 3) {
                            while (linea.length() < 3) {
                                linea = (String) dis.readLine();
                            }
                        }
                        chiave = "ELEM";
                        trovato = linea.lastIndexOf(chiave);
                        if (trovato >= 0) {
                            linea = (String) dis.readLine();
                            linea = (String) dis.readLine();
                            linea = (String) dis.readLine();
                        }
                        if (linea.contains("@@@@@@@@@@@")) {
                            String error = "Rows don't match number of Blocks";
                            JOptionPane.showMessageDialog(null, error);
                            return readOK;
                        }
                        linea = linea.substring(endblock);
                        if (traduci(i_b, timestep, linea, n_array, 0) > 0) {
                            String error = "Variable. String.ToFloat conversion error of:";
                            JOptionPane.showMessageDialog(null, error);
                            JOptionPane.showMessageDialog(null, linea);
                            return readOK;
                        }
                        riga = riga + 1;
                    }
                    /////facciamo la ricerca di ELEM2 come da esempio di EXT scaricato dal sito di TOUGH2
                    if (FluxFound && FlowError == false) {
                        trovato = -1;
                        chiave = "ELEM2";
                        trovato = linea.lastIndexOf(chiave);
                        while (trovato < 0) {
                            linea = (String) dis.readLine();
                            trovato = linea.lastIndexOf(chiave);
                            //legge le solite X righe vuote
                        }
                        linea = (String) dis.readLine();
//                        linea=(String) dis.readLine();
///////////////////////////////qui leggo i flussi...
                        for (int ic = 0; ic < Tough2Viewer.dataobj.get_n_connections(); ic++) {
                            linea = (String) dis.readLine();
                            if (linea.length() < 3) {
                                while (linea.length() < 3) {
                                    linea = (String) dis.readLine();
                                }
                            }
                            if (linea.contains("ELEM2")) {
                                linea = (String) dis.readLine();
                                linea = (String) dis.readLine();
                                linea = (String) dis.readLine();
                            }
                            if (traduciFLUX(ic, timestep, linea, n_arrayFLUX, ConnectionTable, 0, 0) > 0) {
                                String error = "Flow. String.ToFloat conversion error of:";
                                JOptionPane.showMessageDialog(null, error);
                                JOptionPane.showMessageDialog(null, linea);
                                return readOK;
                            }

                            riga++;
                        }
                    }
                    //da qui tento la lettura delle permeabilit�...se le avevo trovate...
                    if (readKper) {
                        if (X1X2Found) {
                            linea = dis.readLine();
                            while (linea.contentEquals(X1X2_line) == false) {
                                linea = dis.readLine();
                            }
                            for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
                                linea = dis.readLine();
                                while (linea.lastIndexOf(BlockNames[i_b]) < 0) {
                                    linea = dis.readLine();
                                }
                                linea = linea.substring(endblock);
                                int offset = n_array.length - 2;
                                if (traduciKper(i_b, timestep, linea, n_arrayX1X2, offset) > 0) {
                                    String error = "Kper. String.ToFloat conversion error of:";
                                    JOptionPane.showMessageDialog(null, error);
                                    JOptionPane.showMessageDialog(null, linea);
                                    return readOK;
                                }
                                riga = riga + 1;
                            }
                        }
                    }
                    chiave = "";
                }//fine istruzioni di OUPUT DATA AFTER=true
                if (Balla_reading_ok) {
                    chiave = "********** VOLUME- AND MASS-BALANCES";
                    trovato = linea.lastIndexOf(chiave);
                    if (trovato >= 0) {
                        ArrayList Balla_name = new ArrayList();
                        ArrayList Balla_Value = new ArrayList();
                        ArrayList Balla_UM = new ArrayList();
                        Double time = -1.0;
                        boolean reading_balla = true;
                        while (reading_balla) {
                            boolean todo = true;
                            linea = dis.readLine();
                            if (linea.length() > 1) {
                                int begin = linea.lastIndexOf("THE TIME IS");
                                int end = linea.lastIndexOf("SECONDS");
                                if (begin > 0 & end > 0) {
                                    String temptime = linea.substring(begin + "THE TIME IS".length(), end);
                                    try {
                                        time = Double.parseDouble(temptime);
                                    } catch (Exception e) {
                                        String error = "BALLA problem. Double.parseDouble error";
                                        JOptionPane.showMessageDialog(null, error);
                                        JOptionPane.showMessageDialog(null, temptime);
                                        Balla_reading_ok = false;
                                    }
                                    todo = false;
                                }
                                if (linea.contains(";") & todo) {
                                    String[] test = JoeStringUtils1.parseString(linea, ";");
                                    int number_of_numbers = test.length;
                                    for (int i = 0; i < number_of_numbers; i++) {
                                        //spezzare ancora la stringa con gli spazi
                                        //trovare il numero
                                        //trovare l'unit� di misura
                                        String[] test1 = JoeStringUtils1.parseSpace(test[i]);
                                        Double value = -1.0;
                                        if (test1.length >= 2) {
                                            try {
                                                value = Double.parseDouble(test1[test1.length - 2]);
                                            } catch (Exception e) {
                                                String error = "BALLA problem. Double.parseDouble error";
                                                JOptionPane.showMessageDialog(null, error);
                                                JOptionPane.showMessageDialog(null, test1[test1.length - 2]);
                                                Balla_reading_ok = false;
                                            }
                                            String name = "";
                                            for (int k = 0; k < test1.length - 2; k++) {
                                                name = name + test1[k];
                                            }
                                            Balla_name.add(name);
                                            int trim = test1[test1.length - 1].lastIndexOf(";");
                                            if (trim > 0) {
                                                test1[test1.length - 1] = test1[test1.length - 1].substring(0, trim);
                                            }
                                            Balla_UM.add(test1[test1.length - 1]);
                                            Balla_Value.add(value);
                                            todo = false;
                                        }
                                    }
                                }
                                //verificare che non ci sia un numero anche in linee senza ";"
                                String[] test = JoeStringUtils1.parseString(linea, " ");
                                if (test.length > 3 & todo) {
                                    if (isDouble(test[test.length - 2])) {
                                        double value = Double.parseDouble(test[test.length - 2]);
                                        String name = "";
                                        for (int k = 0; k < test.length - 2; k++) {
                                            name = name + test[k];
                                        }
                                        Balla_name.add(name);
                                        Balla_UM.add(test[test.length - 1]);
                                        Balla_Value.add(value);
                                    }
                                }
                                if (linea.lastIndexOf("*****************************************************") > 0) {
                                    balla.add(time);
                                    balla.add(Balla_name);
                                    balla.add(Balla_Value);
                                    balla.add(Balla_UM);
                                    reading_balla = false;
                                }
                            }
                        }
                    }
                }
                chiave = "ELEMENT SOURCE INDEX";
                trovato = linea.lastIndexOf(chiave);
//                    if (trovato>=0){
//                        for (int i= 0; i < 2; i = i + 1){
//                                linea=(String) dis.readLine();//legge le solite 3 righe vuote
//                        }
//                        boolean finegener=false;
//                        while(finegener==false){
//                            linea=(String) dis.readLine();
//                            String sottostringa;
//                            if(linea.length()<6){
//                                linea=(String) dis.readLine();
//                                linea=(String) dis.readLine();
//                                linea=(String) dis.readLine();
//                                linea=(String) dis.readLine();
//                            }
//                            sottostringa=linea.substring(1, 6);
//                            if(sottostringa.lastIndexOf("@")<0){
//                                 sottostringa=linea.substring(1, 6);
//                                 Tough2Viewer.dataobj.addGener(sottostringa);
//                            }else{
//                                finegener=true;
//                            }
//                        }
//
//                    }
            }
            fis.close();
            bis.close();
            dis.close();
            Tough2Viewer.dataobj.set_dataLoaded(true);

            Tough2Viewer.dataobj.set_voronoi(true);
            Tough2Viewer.tough2viewerGUI.UpdateFileInformation();
            Tough2Viewer.dataobj.set_INIT_ROI();
            readOK = true;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            String error = "File not found";
            JOptionPane.showMessageDialog(null, error);
        } catch (IOException e) {
            String error = "IOException";
            JOptionPane.showMessageDialog(null, error);
        }
        return readOK;
    }

    boolean readDataFile4(File file, JProgressBar myProgressBar) {
        boolean readOK = false;
        int timestep = 0;
        int endblock = 6;
        ArrayList skip_index_flowArrayList = new ArrayList();
        //inizio to read file
        FileInputStream fis;
        BufferedInputStream bis;
        DataInputStream dis;
        String linea;
        String UMString;
        String UMStringFLOW;
        String PT_line;
        ArrayList PT_linesArrayList = new ArrayList();
        ArrayList Cumulative_variable_count = new ArrayList();
        ArrayList Cumulative_Flow_count = new ArrayList();
        ArrayList UMStringArrayList = new ArrayList();

        int[] n_arrayFLUX = new int[0];
        ArrayList n_arrayArrayList = new ArrayList();
        ArrayList n_arrayFLOWArrayList = new ArrayList();;
        ArrayList FLOW_linesArrayList = new ArrayList();
        ArrayList UMStringFLOWArrayList = new ArrayList();
        FluxFound = false;
        boolean continueread = false;
        myProgressBar.setValue(10);
        boolean Balla_reading_ok = true;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int riga = 0;
            riga = 0;
            //primo ciclo per vedere quanti timestep abbiamo
            int timesteptotal = 0;
            int fine_elem2 = -1;
            linea = (String) dis.readLine();
            while (dis.available() != 0) {
                linea = (String) dis.readLine();
                int trovato;
                String chiave = "OUTPUT DATA AFTER";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    timesteptotal = timesteptotal + 1;
                }
                chiave = "INDEX";
                trovato = linea.lastIndexOf(chiave);
                if ((linea.lastIndexOf("INDEX") > 0 || linea.lastIndexOf("IND.") > 0)) {
                    int dbg = 1;
                }
                if ((linea.lastIndexOf("INDEX") > 0 || linea.lastIndexOf("IND.") > 0) && linea.lastIndexOf("ELEM2") < 0 && timesteptotal == 1 && linea.lastIndexOf("GENER") < 0 && linea.lastIndexOf("GEN. RATE") < 0) {
                    //Lettura variabili di una qualche sezione

                    //forse dovremmo verificare se l'abbiamo già trovata?
                    if (linea.lastIndexOf("IND.") > 0) {
                        trovato = linea.lastIndexOf("IND.");
                    }
                    PT_linesArrayList.add(linea);
                    int startvariabili = -1;
                    for (int i = trovato; i < linea.length(); i++) {
                        if (linea.regionMatches(i, " ", 0, 1)) {
                            startvariabili = i;
                            break;
                        }
                    }
                    String variabili = linea.substring(startvariabili);
                    String[] VariableNames = JoeStringUtils1.parseSpace(variabili);
                    Cumulative_variable_count.add(VariableNames.length);
                    if (PT_linesArrayList.size() == 1) {
                        set_VariableName(VariableNames);
                    } else {
                        addVariableName(VariableNames);
                    }
                    linea = (String) dis.readLine();
                    if (linea.lastIndexOf(Tough2Viewer.dataobj.get_BlockName(0)) < 0) {
                        UMString = linea;
                        if (UMString.length() < 3) {
                            //this is very strange=we haven't the umstring?
                            //then built it
                            UMString = "";
                            for (int i = 0; i < VariableNames.length; i++) {
                                UMString = UMString + "n/a ";
                            }

                        } else {
                            UMString = UMString.substring(startvariabili);
                        }
                        //UMString=UMString.substring(startvariabili);
                        UMStringArrayList.add(UMString);
                        String[] UM = JoeStringUtils1.subDivision2Lines(variabili, UMString);
                        if (UMStringArrayList.size() == 1) {
                            set_variables_UM(UM);

                        } else {
                            Tough2Viewer.dataobj.addVariableNameUM(UM);
                        }
                        if (UM.length != VariableNames.length) {
                            String error = "Number of Var Name don't match number of Mes.Unit";
                            JOptionPane.showMessageDialog(null, error);
                            return readOK;
                        }
                        boolean emptyline = true;
                        while (emptyline) {
                            linea = (String) dis.readLine();
                            if (linea.length() > 5) {
                                emptyline = false;
                            }
                        }
                    } else {
                        String[] UM = new String[VariableNames.length];
                        String tmp = new String();
                        for (int i = 0; i < VariableNames.length; i++) {
                            UM[i] = "number";
                            tmp = tmp + UM[i] + " ";
                        }
                        UMStringArrayList.add(tmp);
                        if (UMStringArrayList.size() == 1) {
                            Tough2Viewer.dataobj.set_variables_UM(UM);

                        } else {
                            Tough2Viewer.dataobj.addVariableNameUM(UM);
                        }
                    }

                    String sottostringa = linea.substring(endblock);

                    int[] n_array = JoeStringUtils1.subDivision(sottostringa);
                    n_arrayArrayList.add(n_array);
                    if (n_array.length - 2 != VariableNames.length) {
                        String error = "Number of Var don't match number of Var Name";

                        JOptionPane.showMessageDialog(null, error);
                        error = "sottostringa=" + sottostringa;
                        JOptionPane.showMessageDialog(null, error);
                        return readOK;
                    }
                    int n_var = 0;

                    //iTough2Viewer.dataobj.set_number_of_variables(n_array.length-2);
                    //CONTROLLO NOME BLOCCHI:::::
                    String nomeprimoblocco = linea.substring(endblock - 5, endblock);
                    String temp = Tough2Viewer.dataobj.get_BlockName(0);
                    if (nomeprimoblocco.contentEquals(temp)) {
                    } else if (continueread == false) {
                        String error = "Block name don't match";
                        JOptionPane.showMessageDialog(null, error);
                        error = "First:" + nomeprimoblocco + " Second:" + temp;
                        JOptionPane.showMessageDialog(null, error);
                        error = "Continue?";
                        int i = JOptionPane.showConfirmDialog(null, error);
                        if (i == JOptionPane.YES_OPTION) {
                            continueread = true;
                        }
                        if (i == JOptionPane.NO_OPTION) {
                            continueread = false;
                            return readOK;
                        }
                        if (i == JOptionPane.CANCEL_OPTION) {
                            return readOK;
                        }
                    }
                    for (int i = 1; i < nxyz; i++) {
                        linea = dis.readLine();
                        if (linea.length() < 3) {
                            while (linea.length() < 3) {
                                linea = (String) dis.readLine();
                            }
                        }
//[15-05-29]era il vecchio controllo sui ripetirori di riga!!!
//Ma attenzione protremmo avere nomi bocchi che iniziano per IND
                        chiave = variabili;
                        trovato = linea.lastIndexOf(chiave);
                        if (trovato >= 0) {
                            linea = (String) dis.readLine();
                            linea = (String) dis.readLine();
                            if (linea.length() < 3) {
                                while (linea.length() < 3) {
                                    linea = (String) dis.readLine();
                                }
                            }
                            // linea=(String) dis.readLine();
                        }
                        if (linea.contains("@@@@@@@@@@@")) {
                            String error = "Rows don't match number of Blocks";
                            JOptionPane.showMessageDialog(null, error);
                            return readOK;
                        }
                        nomeprimoblocco = linea.substring(endblock - 5, endblock);
                        if (nomeprimoblocco.contentEquals(Tough2Viewer.dataobj.get_BlockName(i))) {
                        } else if (continueread == false) {
                            String error = "Block name don't match with MESH or IN file";
                            JOptionPane.showMessageDialog(null, error);
                            error = "line=" + linea;
                            JOptionPane.showMessageDialog(null, error);
                            error = "NameFirstBlock=" + nomeprimoblocco;
                            JOptionPane.showMessageDialog(null, error);
                            error = "NameBlockMESHfile=" + Tough2Viewer.dataobj.get_BlockName(i);
                            JOptionPane.showMessageDialog(null, error);
                            return readOK;
                        }
                    }
                }

                chiave = "+++++++++ CONVERGENCE FAILURE";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    Tough2Viewer.tough2viewerGUI.UpdateInfoLabel(linea);
                }
                chiave = "+++++++++ FOR  10 CONSECUTIVE TIME STEPS HAVE CONVERGENCE ON ITER = 1";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    Tough2Viewer.tough2viewerGUI.UpdateInfoLabel(linea);
                }
                trovato = -1;
                chiave = "ELEM2";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    int dbg = 0;

                }

                if (trovato >= 0 && timesteptotal == 1 && FlowError == false) {
                    int skip_index_flow = 0;
//                 if(FluxFound==true)
//                {
//                    //FlowError=true;//questa istruzione è stata inserita MOMENTANEAMENTE
//                                    //perchè nel caso di blocchi di flussi multipli
//                                    //essi non vengono letti.
//                                    //Invece sarebbe il caso di leggerli
//                    //FluxFound=false;
//                }
//                else
                    {
                        //linea=(String) dis.readLine();//legge le solite X righe vuote
                        //DA QUI DEVO RIFARE IL RAGIONAMENTO PER LA LETTURA FLUSSI E VARIABILI....
                        //siccome non � detto che i flussi siano allineati con ELEM2,
                        //allora occorre trovare il secondo elemento della prima connessione....
                        //e se per caso ritrovo un altro???
                        //Faccio cosi: guardo dove cadono i nomi dei blocchi.
                        //quando li trovo entrambi per la prima connessione allora li do buoni per tutti
                        FluxFound = true;
                        chiave = "INDEX";
                        trovato = linea.lastIndexOf(chiave);
                        if (trovato < 0)//[20-01-2016]There is some cases where in the second flow table, there is not "INDEX" keyword.
                        //in this case we consider that the flow variables starts from elem2
                        {
                            trovato = linea.lastIndexOf("ELEM2");
                            skip_index_flow = 1;
                        }

                        String variabiliFLOW = linea.substring(trovato + chiave.length());

                        FLOW_linesArrayList.add(linea);
                        skip_index_flowArrayList.add(skip_index_flow);
                        String[] VariableNamesFLUX = JoeStringUtils1.parseSpace(variabiliFLOW);

                        if (FLOW_linesArrayList.size() == 1) {
                            set_variables_FLOW(VariableNamesFLUX);
                        } else {
                            addVariablesFLOW(VariableNamesFLUX);
                        }
                        Cumulative_Flow_count.add(VariableNamesFLUX.length);
                        //qui guardo se nel nome della variabile c'�
                        //Flow o vel. Se c'� vel metto=0, altrimenti 1.
//                    FlowOrVel=new int[number_of_flow_variables];
//                    
//                    for(int i=0;i<number_of_flow_variables;i++)
//                    {
//                        String temp=VariableNamesFLUX[i].toLowerCase();
//                        if(temp.lastIndexOf("vel")>=0)
//                        {
//                            FlowOrVel[i]=0;
//                        }
//                        else
//                        {
//                            FlowOrVel[i]=1;
//                        }
//                    }
                        linea = (String) dis.readLine();

                        UMStringFLOW = linea.substring(trovato + chiave.length());
                        UMStringFLOWArrayList.add(UMStringFLOW);

                        String[] UMFLUX = JoeStringUtils1.subDivision2Lines(variabiliFLOW, UMStringFLOW);
                        if (FLOW_linesArrayList.size() == 1) {
                            set_variables_UMFLOW(UMFLUX);
                        } else {
                            addFLOWNameUM(UMFLUX);
                        }

                        if (UMFLUX.length != VariableNamesFLUX.length) {
                            String error = "Number of Var Name FLUX don't match number of Mes.Unit FLUX";
                            JOptionPane.showMessageDialog(null, error);
                            FlowError = true;
                            return readOK;
                        }
                        linea = (String) dis.readLine();
                        if (linea.length() < 3) {
                            while (linea.length() < 3) {
                                linea = (String) dis.readLine();
                            }
                        }
                        int[] temp_Array = new int[4];
                        temp_Array[0] = 0;
                        temp_Array[1] = 0;
                        temp_Array[2] = 0;
                        temp_Array[3] = 0;
                        ///////////////////////////////////////
                        boolean elem1elem2 = true;
                        int first = -1;
                        for (int j = 0; j < 2; j++) {
                            for (int i = 0; i < nxyz; i++) {
                                if (first != i) {
                                    String blockname1 = BlockNames[i];
                                    trovato = linea.indexOf(blockname1, 0);
                                    if (trovato >= 0) {
                                        //if(linea.substring(trovato-1, trovato).contentEquals(" "))//eliminato:non tutti hanno uno spazio..
                                        //questo ulteriore controllo verifca se per caso il carattere precedente non � un o spazio
                                        //infatti se i nomi sono "2    " e "30   " si verifica che potrei trovare la stringa in
                                        //un nome simile, tipo "130  " e quindi sbagliare il tutto.
                                        //poi c'� anche il caso degli allineati a destra ma non lo guardiamo per ora..'
                                        {
                                            temp_Array[j * 2] = trovato;
                                            temp_Array[j * 2 + 1] = trovato + blockname1.length();
                                            first = i;
                                            if (j == 1) {
                                                elem1elem2 = false;
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (elem1elem2) {
                            String error = "can't find two valid block name in first line of flow";
                            JOptionPane.showMessageDialog(null, error);
                            JOptionPane.showMessageDialog(null, linea);
                            FluxFound = false;
                            FlowError = true;
                        }
                        fine_elem2 = max(temp_Array[1], temp_Array[3]);
                        sort(temp_Array);
                        /////////////////////////////////////////////////////////////////////////////////
                        String sottostringa = linea.substring(fine_elem2);
                        n_arrayFLUX = JoeStringUtils1.subDivision(sottostringa);

                        if (n_arrayFLUX.length - 2 + skip_index_flow != VariableNamesFLUX.length) {

                            String error = "Number of Var FLUX don't match number of Var Name FLUX";
                            JOptionPane.showMessageDialog(null, error);
                            FlowError = true;
                            FluxFound = false;
                            return readOK;
                        }

                        //ora devo considerare di prendere tutta la riga perch� mi servono le connessioni...
                        int[] temp_Array2 = new int[n_arrayFLUX.length + 4];
                        for (int i = 0; i < 4; i++) {
                            temp_Array2[i] = temp_Array[i];
                        }
                        for (int i = 0; i < n_arrayFLUX.length; i++) {
                            temp_Array2[i + 4] = n_arrayFLUX[i] + temp_Array[3];
                        }
                        n_arrayFLUX = new int[temp_Array2.length];
                        System.arraycopy(temp_Array2, 0, n_arrayFLUX, 0, n_arrayFLUX.length);
                        ///facciamo la tabella delle connessioni qui un a volta per tutte!!!!
                        n_arrayFLOWArrayList.add(n_arrayFLUX);
                        int nc = get_n_connections();
                        String bl1;
                        String bl2;
                        int n[] = new int[2];
                        if (ConnectionTableToBeCreate) {
                            ConnectionTable = new int[nc][2];
                            Block_n_conne = new float[nxyz];
                            bl1 = linea.substring(n_arrayFLUX[0], n_arrayFLUX[1]);
                            bl2 = linea.substring(n_arrayFLUX[2], n_arrayFLUX[3]);
                            if (TwoBlockSearch(bl1, bl2, n) == false) {
                                String error = "Connection Table problem for " + bl1 + " and " + bl2;
                                JOptionPane.showMessageDialog(null, error);
                                FlowError = true;
                                //return readOK;
                            }
                            ConnectionTable[0][0] = n[0];
                            ConnectionTable[0][1] = n[1];
                            Block_n_conne[n[0]]++;
                            Block_n_conne[n[1]]++;
                        }
                        //Partiamo da uno perch� siamo gi� sui dati

                        for (int i = 1; i < nc; i++) {
                            linea = (String) dis.readLine();
                            if (linea.length() < 3) {
                                while (linea.length() < 3) {
                                    linea = (String) dis.readLine();
                                }
                            }
                            chiave = "ELEM2";
                            trovato = linea.lastIndexOf(chiave);
                            if (trovato >= 0) {
                                linea = (String) dis.readLine();
                                boolean emptyline = true;
                                while (emptyline) {
                                    linea = (String) dis.readLine();
                                    if (linea.length() > 5) {
                                        emptyline = false;
                                    }
                                }
                            }
                            if (linea.contains("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")) {
                                //String error="Flow rows don't match number of connections";
                                //JOptionPane.showMessageDialog(null, error);
                                //return readOK;

                                break;
                            }
                            if (ConnectionTableToBeCreate) {
                                bl1 = linea.substring(n_arrayFLUX[0], n_arrayFLUX[1]);
                                bl2 = linea.substring(n_arrayFLUX[2], n_arrayFLUX[3]);
                                if (TwoBlockSearch(bl1, bl2, n) == false) {
                                    String error = "Connection Table problem for " + bl1 + " and " + bl2;
                                    JOptionPane.showMessageDialog(null, error);
                                    FlowError = true;
                                }
                                ConnectionTable[i][0] = n[0];
                                ConnectionTable[i][1] = n[1];
                                Block_n_conne[n[0]]++;
                                Block_n_conne[n[1]]++;
                            }
                        }
//                    linea=(String) dis.readLine();
//                    if (linea.length()<3)
//                    {
//                        while(linea.length()<3)
//                        {
//                            linea=(String) dis.readLine();
//                        }
//                    }
//                    if(linea.contains("@@@@@@@@@@@"))
//                    {
//
//                    }
//                    else
//                    {
//                        String error="Flow rows don't match number of connections";
//                        JOptionPane.showMessageDialog(null, error);
//                        return readOK;
//                    }
                    }
                }
                chiave = "NCON =";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
                    String sottostringa;
                    sottostringa = linea.substring(trovato + 6, trovato + 7 + 6);
                    int n_c_D = (int) Float.parseFloat(sottostringa);
                    set_n_connections(n_c_D);
                }
            }
            fis.close();
            bis.close();
            dis.close();
            //
            //[2015:02:17]lettura blocchi con problemi di convergenza....

//        String[] convergenceProblems={"volume","conne","noConv","evolves","disap."};
//        addVariableName(convergenceProblems);
//        String[] convergenceProblemsUM={"M^3","number","number","number","number"};
//        addVariableNameUM(convergenceProblemsUM);
            //2019: adding number of vertex and face number...
            String[] convergenceProblems = {"volume", "conne", "num_vertex", "num_faces", "noConv", "evolves", "disap."};
            addVariableName(convergenceProblems);
            String[] convergenceProblemsUM = {"M^3", "number", "number", "number", "number", "number", "number"};
            addVariableNameUM(convergenceProblemsUM);

            dataArrayCreate(nxyz, timesteptotal);

            if (BlockVolume != null) {
                for (int i = 0; i < nxyz; i++) {
                    for (int i_t = 0; i_t < timesteptotal; i_t++) {
                        //VOLUME
                        if (BlockVolume[i] < big_float) {
                            dataArray[i][i_t][number_of_variables - 7] = (float) BlockVolume[i];
                        } else {
                            dataArray[i][i_t][number_of_variables - 7] = big_float;
                        }
                        //CONNE
                        dataArray[i][i_t][number_of_variables - 6] = Block_n_conne[i];
                        //
                        if (ID_grid_type == 2) {
                            ArrayList singleBox = (ArrayList) VoroPPData.get(i);
                            int num_vertex = (Integer) singleBox.get(2);
                            int number_of_faces = (Integer) singleBox.get(3 + num_vertex);
//                    Tough2Viewer.dataobj.set_dataArray(i,i_t,4, (float) num_vertex );
//                    Tough2Viewer.dataobj.set_dataArray(i,i_t,5, (float) number_of_faces );
                            //NUM_VERTEX
                            dataArray[i][i_t][number_of_variables - 5] = num_vertex;
                            //NUM_FACES
                            dataArray[i][i_t][number_of_variables - 4] = number_of_faces;
                        } else {
                            //NUM_VERTEX
                            dataArray[i][i_t][number_of_variables - 5] = 8;
                            //NUM_FACES
                            dataArray[i][i_t][number_of_variables - 4] = 6;
                        }
                    }
                }
            }

            if (FluxFound) {
                connectionsArrayCreate(get_n_connections());
                FlowOrVel = new int[number_of_flow_variables];
                String[] VariableNamesFLUX = getFLOWName();
                for (int i = 0; i < number_of_flow_variables; i++) {
                    String temp = VariableNamesFLUX[i].toLowerCase();
                    if (temp.lastIndexOf("vel") >= 0) {
                        FlowOrVel[i] = 0;
                    } else {
                        FlowOrVel[i] = 1;
                    }
                }
            } else {

//            String error="Warning: Flow information not found.";
//            JOptionPane.showMessageDialog(null, error);
            }
            try {
                myProgressBar.setValue(50);
                Thread.sleep(100);
                myProgressBar.setValue(50);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myProgressBar.setValue(50);

            //
            //
            //inizio lettura vera
            //
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                linea = (String) dis.readLine();
                int trovato;
                String chiave = "TOTAL TIME";
                trovato = linea.lastIndexOf(chiave);
                if (trovato >= 0) {
//                String sottostringa;
//                String inizioLetturaTempoString="THE TIME IS";
//                String fineLetturaTempoString="DAY";
//                int inizio= linea.lastIndexOf(inizioLetturaTempoString)+inizioLetturaTempoString.length();
//                int fine= linea.lastIndexOf(fineLetturaTempoString);
                    linea = (String) dis.readLine();
                    String sottostringa = linea.substring(0, 14);
                    Tough2Viewer.dataobj.set_Times(timestep, Float.parseFloat(sottostringa));
                    timestep = timestep + 1;
                    int progressbar = (int) (((float) timestep) / ((float) timesteptotal) * 50.0f) + 50;
                    myProgressBar.setValue(progressbar);
                    try {
                        myProgressBar.setValue(progressbar);
                        Thread.sleep(100);
                        myProgressBar.setValue(progressbar);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String tmp_ptline;
                if ((linea.lastIndexOf("INDEX") > 0 || linea.lastIndexOf("IND.") > 0) && linea.lastIndexOf("ELEM2") < 0 && linea.lastIndexOf("GENER") < 0 && linea.lastIndexOf("GEN. RATE") < 0) {
                    tmp_ptline = linea;
                    linea = (String) dis.readLine();
                    if (linea.lastIndexOf(Tough2Viewer.dataobj.get_BlockName(0)) < 0) {
                        linea = (String) dis.readLine();
                    }

                    int ptline_index = -1;
                    for (int i = 0; i < PT_linesArrayList.size(); i++) {
                        String tmp = (String) PT_linesArrayList.get(i);
                        if (tmp.lastIndexOf(tmp_ptline) >= 0) {
                            ptline_index = i;
                            break;
                        }
                    }
                    if (ptline_index < 0) {
                        int houston_we_get_a_problem = 1;
                        break;
                    }
                    int offset = 0;
                    for (int i = 0; i < ptline_index; i++) {

                        int tmp = (int) Cumulative_variable_count.get(i);
                        offset = offset + tmp;
                    }
                    int[] n_array = (int[]) n_arrayArrayList.get(ptline_index);
                    for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
                        if (i_b == 0 && linea.lastIndexOf(Tough2Viewer.dataobj.get_BlockName(0)) > 0) {
                            //non leggiamo, ci siamo già
                        } else {
                            linea = (String) dis.readLine();
                        }

                        //Facciamo un controllo su ripetitori di riga?SI!!!
                        if (linea.length() < 3) {
                            while (linea.length() < 3) {
                                linea = (String) dis.readLine();
                            }
                        }
                        chiave = "ELEM";
                        trovato = linea.lastIndexOf(chiave);
                        if (trovato >= 0) {
                            linea = (String) dis.readLine();
                            linea = (String) dis.readLine();
                            if (linea.length() < 3) {
                                while (linea.length() < 3) {
                                    linea = (String) dis.readLine();
                                }
                            }
                        }
                        if (linea.contains("@@@@@@@@@@@")) {
                            String error = "Rows don't match number of Blocks";
                            JOptionPane.showMessageDialog(null, error);
                            return readOK;
                        }
                        linea = linea.substring(endblock);

                        if (traduci(i_b, timestep, linea, n_array, offset) > 0) {
                            String error = "Variable. String.ToFloat conversion error of:";
                            JOptionPane.showMessageDialog(null, error);
                            JOptionPane.showMessageDialog(null, linea);
                            return readOK;
                        }
                        riga = riga + 1;
                    }
                    /////Ora facciamo una nuova ricerca.
                    /////non sappiamo se troveremo Flow oppure Krel oppure DX1 etc.
                    /////nella versione per CO2 potremo avre anche diversi flows....
                    /////tanto siamo dentro timestep=1.
                }

                chiave = "ELEM2";
                trovato = linea.lastIndexOf(chiave);

                if (trovato > 0) {
                    String tmp_flow_line = linea;
                    linea = (String) dis.readLine();
                    int Flow_line_index = -1;
                    for (int i = 0; i < FLOW_linesArrayList.size(); i++) {
                        String tmp = (String) FLOW_linesArrayList.get(i);
                        if (tmp.lastIndexOf(tmp_flow_line) >= 0) {
                            Flow_line_index = i;
                            break;
                        }
                    }
                    if (Flow_line_index < 0) {
                        int houston_we_get_a_problem = 1;
                        break;
                    }
                    int offset = 0;
                    for (int i = 0; i < Flow_line_index; i++) {

                        int tmp = (int) Cumulative_Flow_count.get(i);
                        offset = offset + tmp;
                    }
                    int[] n_arrayFLOW = (int[]) n_arrayFLOWArrayList.get(Flow_line_index);
////////////////////////qui leggo i flussi...
                    int skip_index = (int) skip_index_flowArrayList.get(Flow_line_index);
                    for (int ic = 0; ic < n_connections; ic++) {
                        linea = (String) dis.readLine();
                        while (linea.length() < 3) {
                            linea = (String) dis.readLine();
                        }
                        if (linea.contains("ELEM2")) {
                            linea = (String) dis.readLine();
                            linea = (String) dis.readLine();
                            linea = (String) dis.readLine();
                        }
                        if (linea.contains("@@@@@@@@@@@")) {
                            //questo perchè è possibile che ci siano tabelle 
                            //con numero di connessioni inferiori di connessioni,
                            //come nel caso di T2Well-ewasg
                            break;
                        }

                        //if(traduciFLUX(ic,timestep,linea,n_arrayFLUX,ConnectionTable)>0)
                        if (traduciFLUX(ic, timestep, linea, n_arrayFLOW, ConnectionTable, offset, skip_index) > 0) {
                            String error = "Flow. String.ToFloat conversion error of:";
                            JOptionPane.showMessageDialog(null, error);
                            JOptionPane.showMessageDialog(null, linea);
                            return readOK;
                        }

                    }

                }

                if (Balla_reading_ok) {
                    chiave = "********** VOLUME- AND MASS-BALANCES";
                    trovato = linea.lastIndexOf(chiave);
                    if (trovato >= 0) {
                        ArrayList Balla_name = new ArrayList();
                        ArrayList Balla_Value = new ArrayList();
                        ArrayList Balla_UM = new ArrayList();
                        Double time = -1.0;
                        boolean reading_balla = true;
                        while (reading_balla) {
                            boolean todo = true;
                            linea = dis.readLine();
                            if (linea.length() > 1) {
                                int begin = linea.lastIndexOf("THE TIME IS");
                                int end = linea.lastIndexOf("SECONDS");
                                if (begin > 0 & end > 0) {
                                    String temptime = linea.substring(begin + "THE TIME IS".length(), end);
                                    try {
                                        time = Double.parseDouble(temptime);
                                    } catch (Exception e) {
                                        String error = "BALLA problem. Double.parseDouble error";
                                        JOptionPane.showMessageDialog(null, error);
                                        JOptionPane.showMessageDialog(null, temptime);
                                        Balla_reading_ok = false;
                                    }
                                    todo = false;
                                }
                                if (linea.contains(";") & todo) {
                                    String[] test = JoeStringUtils1.parseString(linea, ";");
                                    int number_of_numbers = test.length;
                                    for (int i = 0; i < number_of_numbers; i++) {
                                        //spezzare ancora la stringa con gli spazi
                                        //trovare il numero
                                        //trovare l'unit� di misura
                                        String[] test1 = JoeStringUtils1.parseSpace(test[i]);
                                        Double value = -1.0;
                                        if (test1.length >= 2) {
                                            try {
                                                value = Double.parseDouble(test1[test1.length - 2]);
                                            } catch (Exception e) {
                                                String error = "BALLA problem. Double.parseDouble error";
                                                JOptionPane.showMessageDialog(null, error);
                                                JOptionPane.showMessageDialog(null, test1[test1.length - 2]);
                                                Balla_reading_ok = false;
                                            }
                                            String name = "";
                                            for (int k = 0; k < test1.length - 2; k++) {
                                                name = name + test1[k];
                                            }
                                            Balla_name.add(name);
                                            int trim = test1[test1.length - 1].lastIndexOf(";");
                                            if (trim > 0) {
                                                test1[test1.length - 1] = test1[test1.length - 1].substring(0, trim);
                                            }
                                            Balla_UM.add(test1[test1.length - 1]);
                                            Balla_Value.add(value);
                                            todo = false;
                                        }
                                    }
                                }
                                //verificare che non ci sia un numero anche in linee senza ";"
                                String[] test = JoeStringUtils1.parseString(linea, " ");
                                if (test.length > 3 & todo) {
                                    if (isDouble(test[test.length - 2])) {
                                        double value = Double.parseDouble(test[test.length - 2]);
                                        String name = "";
                                        for (int k = 0; k < test.length - 2; k++) {
                                            name = name + test[k];
                                        }
                                        Balla_name.add(name);
                                        Balla_UM.add(test[test.length - 1]);
                                        Balla_Value.add(value);
                                    }
                                }
                                if (linea.lastIndexOf("*****************************************************") > 0) {
                                    balla.add(time);
                                    balla.add(Balla_name);
                                    balla.add(Balla_Value);
                                    balla.add(Balla_UM);
                                    reading_balla = false;
                                }
                            }
                        }
                    }
                }
                //lettura blocco con MAX. RES.
                if (linea.regionMatches(eleme_lenght + 1, "(", 0, 1)) {
                    int n1 = OneBlockSearch(linea.substring(1, eleme_lenght + 1));
                    if (n1 >= 0) {
                        dataArray[n1][timestep][number_of_variables - 3]++;
                    }
                }
                if (linea.lastIndexOf("EVOLVES") > 0) {
                    int start = linea.indexOf("*") + 1;
                    int n1 = OneBlockSearch(linea.substring(start, start + eleme_lenght));
                    if (n1 >= 0) {
                        dataArray[n1][timestep][number_of_variables - 2]++;
                    }
                }
                if (linea.lastIndexOf("DISAPP") > 0) {
                    int start = linea.indexOf("*") + 1;
                    int n1 = OneBlockSearch(linea.substring(start, start + eleme_lenght));
                    if (n1 >= 0) {
                        dataArray[n1][timestep][number_of_variables - 1]++;
                    }
                }
            }
            fis.close();
            bis.close();
            dis.close();
            Tough2Viewer.dataobj.set_dataLoaded(true);
            Tough2Viewer.dataobj.set_voronoi(true);
            Tough2Viewer.tough2viewerGUI.UpdateFileInformation();
            Tough2Viewer.dataobj.set_INIT_ROI();
            readOK = true;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            String error = "File not found";
            JOptionPane.showMessageDialog(null, error);
        } catch (IOException e) {
            String error = "IOException";
            JOptionPane.showMessageDialog(null, error);
        }
        return readOK;
    }

    private boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private int OneBlockSearch(String blockname) {
        if (blockname_ordered == false) {
            create_BlockNames_ordered();
        }
        int n = -1;
        int start = 0;
        int end = nxyz;
        int midle;
        boolean continue_search = true;
        while (continue_search) {
            midle = (int) ((start + end) / 2);
            if (BlockNames_ordered[midle].compareTo(blockname) == 0) {
                return Indexs[midle];
            }
            if (midle == start || midle == end) {
                continue_search = false;
            }
            if (BlockNames_ordered[midle].compareTo(blockname) > 0) {
                end = midle;
            } else {
                start = midle;
            }

        }

        return n;
    }

    private boolean TwoBlockSearch_bisect(String blockname1, String blockname2, int[] n) {
        boolean ok = false;
        n[0] = OneBlockSearch(blockname1);
        n[1] = OneBlockSearch(blockname2);
        if (n[0] >= 0 & n[1] >= 0) {
            ok = true;
        }
        return ok;
    }

    private void create_BlockNames_ordered() {
        if (blockname_ordered == false) {
            //al momento fa questo.
            //ok=TwoBlockSearch_BruteForce(blockname1,blockname2,n);
            //ora invece gli faccio copiare il vettore, lo ordino e poi faccio
            //la ricerca su questo
            BlockNames_ordered = new String[nxyz];
            System.arraycopy(BlockNames, 0, BlockNames_ordered, 0, java.lang.reflect.Array.getLength(BlockNames));
            Indexs = new int[nxyz];
            for (int i = 0; i < nxyz; i++) {
                Indexs[i] = i;
            }
            //12-06-18-ordinamento
            Date x = new Date();
            long start = x.getTime();
            //Quicksort.myquicksort(BlockNames_ordered,Indexs);
            Quicksort.quicksort(BlockNames_ordered, Indexs);//[14-07-01 stebond per ovviare stack overflow]
            //QuickSortNonRec.Quick(BlockNames_ordered,0, nxyz-1,Indexs);
            Date x2 = new Date();
            long stop = x2.getTime();
            long duration = stop - start;
            System.out.println("Elapsed quicksort time:");
            System.out.println(duration);

            blockname_ordered = true;
        }
    }

    private boolean TwoBlockSearch(String blockname1, String blockname2, int[] n) {
        boolean ok;
        if (blockname_ordered) {
            ok = TwoBlockSearch_bisect(blockname1, blockname2, n);

        } else {
            //al momento fa questo.
            //ok=TwoBlockSearch_BruteForce(blockname1,blockname2,n);
            //ora invece gli faccio copiare il vettore, lo ordino e poi faccio
            //la ricerca su questo
            BlockNames_ordered = new String[nxyz];
            System.arraycopy(BlockNames, 0, BlockNames_ordered, 0, java.lang.reflect.Array.getLength(BlockNames));
            Indexs = new int[nxyz];
            for (int i = 0; i < nxyz; i++) {
                Indexs[i] = i;
            }
            //12-06-18-ordinamento
            Date x = new Date();
            long start = x.getTime();
            //Quicksort.myquicksort(BlockNames_ordered,Indexs);
            Quicksort.quicksort(BlockNames_ordered, Indexs);//[14-07-01 stebond per ovviare stack overflow]
            //QuickSortNonRec.Quick(BlockNames_ordered,0, nxyz-1,Indexs);
            Date x2 = new Date();
            long stop = x2.getTime();
            long duration = stop - start;
            System.out.println("Elapsed quicksort time:");
            System.out.println(duration);

            ok = TwoBlockSearch_bisect(blockname1, blockname2, n);
            blockname_ordered = true;
        }
        return ok;

    }

    private boolean TwoBlockSearch_BruteForce(String blockname1, String blockname2, int[] n) {
        boolean ok = false;
        int controllo = 0;
        for (int i = 0; i < nxyz; i++) {

            if (BlockNames[i].equals(blockname1)) {
                n[0] = i;
                controllo++;
                if (controllo > 1) {
                    ok = true;
                    break;
                }
            }
            if (BlockNames[i].equals(blockname2)) {
                n[1] = i;
                controllo++;
                if (controllo > 1) {
                    ok = true;
                    break;
                }
            }
        }
        return ok;
    }

    private int traduciFLUX(int blockIndex, int timestep, String linea1, int[] n, int[][] conne, int offset, int skip_index) {
        String sottostringa;
        set_ConnectionBlocks(blockIndex, conne[blockIndex][0], conne[blockIndex][1]);
        for (int i = 5 - skip_index; i < n.length - 1; i++) {
            sottostringa = linea1.substring(n[i], n[i + 1]);
            float value = -1;
            try {
                value = Float.parseFloat(sottostringa);
            } catch (Exception es) {
                if (sottostringa.contains("-")) {
                    value = -1;
                } else {
                    String a = es.toString();
                    return i;
                }
            }
            set_dataFluxArray(blockIndex, timestep - 1, i - (5 - skip_index) + offset, value);
        }
        return -1;
    }

    private int traduci(int blockIndex, int timestep, String linea1, int[] n, int offset) {
        int ok = -1;
        String sottostringa;
        for (int i = 1; i < n.length - 1; i++) {
            sottostringa = linea1.substring(n[i], n[i + 1]);
            float value = -1;
            if (sottostringa.contains("***")) {
                value = -1;
            } else {
                try {
                    value = Float.parseFloat(sottostringa);
                } catch (Exception es) {
                    if (sottostringa.contains("-")) {
                        value = -1;
                    } else {
                        String a = es.toString();
                        return i;
                    }

                }
            }

            Tough2Viewer.dataobj.set_dataArray(blockIndex, timestep - 1, i - 1 + offset, value);
        }
        return ok;
    }

    private int traduciKper(int blockIndex, int timestep, String linea1, int[] n, int offset) {
        String sottostringa;
        for (int i = 1; i < n.length - 1; i++) {

            sottostringa = linea1.substring(n[i], n[i + 1]);
            int len = sottostringa.length();
            if (sottostringa.substring(len - 4, len - 3).contains("-")) {
                String temp = sottostringa.substring(0, len - 4) + "E" + sottostringa.substring(len - 4);
                sottostringa = temp;
            }
            float value = -1;;
            try {
                value = Float.parseFloat(sottostringa);
            } catch (Exception es) {

                if (sottostringa.contains("-")) {
                    value = -1;
                } else {
                    String a = es.toString();
                    return i;
                }

            }
            Tough2Viewer.dataobj.set_dataArray(blockIndex, timestep - 1, offset + i - 1, Float.parseFloat(sottostringa));
        }
        return -1;
    }

    boolean read_IN_file() {
        if (xoyoloaded == false) {
            String linea;
            File fileIn = new File(InFileName);
            FileInputStream fisIn;
            BufferedInputStream bisIn;
            DataInputStream disIn;
            try {

                fisIn = new FileInputStream(fileIn);
                bisIn = new BufferedInputStream(fisIn);
                disIn = new DataInputStream(bisIn);
                int inizio = 0;
                int fine = 0;
                String chiave;
                int start = 1;
                boolean locat = false;
                while (locat == false) {
                    linea = (String) disIn.readLine();//questa � la prima linea
                    linea = linea.toUpperCase();
                    String chiave1 = "LOCAT";
                    int trovato = linea.lastIndexOf(chiave1);
                    if (trovato >= 0) {
                        locat = true;
                    }
                    chiave1 = "ELEME";
                    if (linea.lastIndexOf(chiave1) >= 0) {
                        return read_MESH_file(fileIn, 1);
                    }

                }
                int[] rxyzbs = new int[6];

                boolean end_IN_file = true;
                ArrayList totalLines = new ArrayList();
                while (end_IN_file) {
                    linea = (String) disIn.readLine();
                    if (linea.length() < 5) {
                        end_IN_file = false;
                    } else {
                        totalLines.add(linea);
                    }
                }
                //
                nxyz = totalLines.size();
                XoYoZoArrayCreate(nxyz);
                dimZArrayCreate(nxyz);
                BlockNamesArrayCreate(nxyz);
                RockTypeArrayCreate(nxyz);
                voronoiGeometryBox = new ArrayList[nxyz];
                //qui guardiamo se la versione dell'IN file � la vecchia o la nuova
                ArrayList lineaALTEMP = mylineAL((String) totalLines.get(0));
                if (lineaALTEMP.size() <= 7) {
                    //vecchioformato
                    rxyzbs[0] = 2;//rcktype
                    rxyzbs[1] = 3;//x
                    rxyzbs[2] = 4;//y
                    rxyzbs[3] = 5;//z
                    rxyzbs[4] = 6;//Thickness
                    rxyzbs[5] = 0;//blockname
                } else {
                    rxyzbs[0] = 3;//rcktype
                    rxyzbs[1] = 4;//x
                    rxyzbs[2] = 5;//y
                    rxyzbs[3] = 6;//z
                    rxyzbs[4] = 7;//Thickness
                    rxyzbs[5] = 1;//blockname
                }
                String sottostringaTEMP = lineaALTEMP.get(rxyzbs[3]).toString();
                float value1TEMP = Float.parseFloat(sottostringaTEMP);
                zmin = (float) value1TEMP;
                zmax = (float) value1TEMP;
                for (int i1 = 0; i1 < totalLines.size(); i1++) {
                    voronoiGeometryBox[i1] = new ArrayList();
                    ArrayList lineaAL = mylineAL((String) totalLines.get(i1));
                    String sottostringa;
                    //rocktype??
                    sottostringa = lineaAL.get(rxyzbs[0]).toString();
                    set_rock_type(sottostringa, i1);
                    //Quarto valore
                    sottostringa = lineaAL.get(rxyzbs[1]).toString();
                    Float value1 = Float.parseFloat(sottostringa);
                    //quinto valore
                    sottostringa = lineaAL.get(rxyzbs[2]).toString();
                    Float value2 = Float.parseFloat(sottostringa);
                    Point2d punto = new Point2d(value1, value2);
                    voronoiGeometryBox[i1].add(punto);
                    sottostringa = lineaAL.get(rxyzbs[3]).toString();
                    value1 = Float.parseFloat(sottostringa);
                    zmin = min(zmin, (float) value1);
                    zmax = max(zmax, (float) value1);
                    Zo[i1] = value1;
                    sottostringa = lineaAL.get(rxyzbs[4]).toString();
                    value1 = Float.parseFloat(sottostringa);
                    DimBlockZ[i1] = value1;
                    sottostringa = lineaAL.get(rxyzbs[5]).toString();
                    if (sottostringa.length() < 5) {
                        for (int i = sottostringa.length(); i < 5; i++) {
                            sottostringa = sottostringa + " ";
                        }
                    }
                    BlockNames[i1] = sottostringa;
//                if(i1>0)
//                {
//                    if(BlockNames[i1].compareTo(BlockNames[i1-1])<0)
//                        {
//                            blocknameIsAlphabeticalOrdered=false;
//                        }
//                }
                }
                //bound
                boolean mybound = false;
                while (mybound == false) {
                    linea = (String) disIn.readLine();
                    if (linea.contains("bound")) {
                        mybound = true;
                    }
                }
                float v1, v2;
                linea = (String) disIn.readLine();//primo spigolo
                ArrayList lineaAL = mylineAL(linea);
                String sottostringa = lineaAL.get(0).toString();
                xmin = Float.parseFloat(sottostringa);
                sottostringa = lineaAL.get(1).toString();
                ymin = Float.parseFloat(sottostringa);
                xmax = xmin;
                ymax = ymin;
                linea = (String) disIn.readLine();//secondo spigolo
                lineaAL = mylineAL(linea);
                sottostringa = lineaAL.get(0).toString();
                v1 = Float.parseFloat(sottostringa);
                sottostringa = lineaAL.get(1).toString();
                v2 = Float.parseFloat(sottostringa);
                xmin = min(xmin, v1);
                ymin = min(ymin, v2);
                xmax = max(xmax, v1);
                ymax = max(ymax, v2);
                linea = (String) disIn.readLine();//terzo spigolo
                lineaAL = mylineAL(linea);
                sottostringa = lineaAL.get(0).toString();
                v1 = Float.parseFloat(sottostringa);
                sottostringa = lineaAL.get(1).toString();
                v2 = Float.parseFloat(sottostringa);
                xmin = min(xmin, v1);
                ymin = min(ymin, v2);
                xmax = max(xmax, v1);
                ymax = max(ymax, v2);
                linea = (String) disIn.readLine();//terzo spigolo
                lineaAL = mylineAL(linea);
                sottostringa = lineaAL.get(0).toString();
                v1 = Float.parseFloat(sottostringa);
                sottostringa = lineaAL.get(1).toString();
                v2 = Float.parseFloat(sottostringa);
                xmin = min(xmin, v1);
                ymin = min(ymin, v2);
                xmax = max(xmax, v1);
                ymax = max(ymax, v2);
                Tough2Viewer.tough2viewerGUI.UpdateFileInformation();

                set_INIT_ROI();
            } catch (IOException e) {
                String error = "Warning: File IOException";
                JOptionPane.showMessageDialog(null, error);
                error = e.toString();
                JOptionPane.showMessageDialog(null, error);
                return false;
            }

            for (int i1 = 0; i1 < nxyz; i1++) {

                Point2d temp = (Point2d) voronoiGeometryBox[i1].get(0);
                set_Xo((float) temp.x, i1);
                set_Yo((float) temp.y, i1);

            }

            xoyoloaded = true;
            initializeRockType();

        }//fine dell'end if'

        return true;
    }

    void initializeRockType() {
        ArrayList temp = new ArrayList();
        int nr = rocksnames.size();
        for (int i = 0; i < nr; i++) {
            temp.add(rocksnames.get(i));
        }
        Collections.sort(temp);
        int[] conversione = new int[nr];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nr; j++) {
                if (rocksnames.get(i).equals(temp.get(j))) {
                    conversione[i] = j;
                }
            }
        }
        for (int i = 0; i < nxyz; i++) {
            RockType[i] = conversione[RockType[i]];
        }

        boolean[] rocktype_visible_tmp = null;
        if (rocktype_visible != null) {
            rocktype_visible_tmp = new boolean[rocktype_visible.length];
            System.arraycopy(rocktype_visible, 0, rocktype_visible_tmp, 0, rocktype_visible.length);
        }

        rocktype_visible = new boolean[rocksnames.size()];
        for (int i = 0; i < rocksnames.size(); i++) {
            rocktype_visible[i] = true;
        }
        if (rocktype_visible_tmp != null) {
            for (int i = 0; i < rocktype_visible_tmp.length; i++) {
                rocktype_visible[conversione[i]] = rocktype_visible_tmp[i];
            }
        }
        Collections.sort(rocksnames);
    }

    void setColorRockTypes(int index, float r, float g, float b) {
        colore1[index] = new Color3f(r, g, b);
    }

    void createcolorRockTypes() {
        colore1[0] = new Color3f(1.00000f, 0.74902f, 0.50196f);
        colore1[1] = new Color3f(1.00000f, 0.50196f, 0.00000f);
        colore1[2] = new Color3f(1.00000f, 1.00000f, 0.60000f);
        colore1[3] = new Color3f(1.00000f, 1.00000f, 0.20000f);
        colore1[4] = new Color3f(0.70196f, 1.00000f, 0.54902f);
        colore1[5] = new Color3f(0.20000f, 1.00000f, 0.00000f);
        colore1[6] = new Color3f(0.65098f, 0.92941f, 1.00000f);
        colore1[7] = new Color3f(0.10196f, 0.70196f, 1.00000f);
        colore1[8] = new Color3f(0.40000f, 0.30196f, 1.00000f);
        colore1[9] = new Color3f(0.80000f, 0.74902f, 1.00000f);
        colore1[10] = new Color3f(1.00000f, 0.60000f, 0.74902f);
        colore1[11] = new Color3f(0.90196f, 0.10196f, 0.20000f);
        colore1[12] = new Color3f(0.60000f, 0.20000f, 0.00000f);
        colore1[13] = new Color3f(0.20000f, 0.20000f, 0.00000f);
        colore1[14] = new Color3f(0.00000f, 0.20000f, 0.20000f);
        colore1[15] = new Color3f(1.00000f, 0.60000f, 0.80000f);
        colore1[16] = new Color3f(1.00000f, 0.00000f, 1.00000f);
        colore1[17] = new Color3f(1.00000f, 0.50196f, 0.50196f);
        colore1[18] = new Color3f(0.75294f, 0.75294f, 0.75294f);
        colore1[19] = new Color3f(0.80000f, 0.60000f, 1.00000f);
        colore1[20] = new Color3f(0.20000f, 0.80000f, 0.80000f);
        colore1[21] = new Color3f(0.00000f, 0.20000f, 0.40000f);
        colore1[22] = new Color3f(0.00000f, 0.20000f, 0.80000f);
        colore1[23] = new Color3f(0.10196f, 0.20000f, 1.00000f);
        colore1[24] = new Color3f(0.20000f, 0.40000f, 0.00000f);
        colore1[25] = new Color3f(1.00000f, 1.00000f, 0.80000f);
        colore1[26] = new Color3f(0.43445f, 0.08608f, 0.22735f);
        colore1[27] = new Color3f(0.63024f, 0.95289f, 0.64855f);
        colore1[28] = new Color3f(0.60882f, 0.08869f, 0.03344f);
        colore1[29] = new Color3f(0.61914f, 0.93719f, 0.86545f);
        colore1[30] = new Color3f(0.51696f, 0.21286f, 0.23129f);
        colore1[31] = new Color3f(0.70248f, 0.56254f, 0.35990f);
        colore1[32] = new Color3f(0.42927f, 0.38545f, 0.90398f);
        colore1[33] = new Color3f(0.37334f, 0.61547f, 0.13429f);
        colore1[34] = new Color3f(0.28684f, 0.38464f, 0.52471f);
        colore1[35] = new Color3f(0.03407f, 0.74311f, 0.58253f);
        colore1[36] = new Color3f(0.41615f, 0.95919f, 0.35997f);
        colore1[37] = new Color3f(0.10324f, 0.77909f, 0.34997f);
        colore1[38] = new Color3f(0.19003f, 0.62955f, 0.42763f);
        colore1[39] = new Color3f(0.79653f, 0.44369f, 0.74210f);
        colore1[40] = new Color3f(0.18791f, 0.68422f, 0.35165f);
        colore1[41] = new Color3f(0.83874f, 0.40539f, 0.38885f);
        colore1[42] = new Color3f(0.32574f, 0.92852f, 0.33984f);
        colore1[43] = new Color3f(0.45021f, 0.41641f, 0.83488f);
        colore1[44] = new Color3f(0.01469f, 0.74427f, 0.14698f);
        colore1[45] = new Color3f(0.27969f, 0.40558f, 0.08684f);
        colore1[46] = new Color3f(0.23909f, 0.29448f, 0.82467f);
        colore1[47] = new Color3f(0.63787f, 0.04933f, 0.11586f);
        colore1[48] = new Color3f(0.64071f, 0.98457f, 0.21055f);
        colore1[49] = new Color3f(0.66659f, 0.66306f, 0.00022f);
        colore1[50] = new Color3f(0.28876f, 0.19947f, 0.42732f);
        colore1[51] = new Color3f(0.14060f, 0.96325f, 0.71375f);
        colore1[52] = new Color3f(0.23972f, 0.98886f, 0.83557f);
        colore1[53] = new Color3f(0.03333f, 0.91825f, 0.57241f);
        colore1[54] = new Color3f(0.62008f, 0.27886f, 0.22163f);
        colore1[55] = new Color3f(0.42428f, 0.36180f, 0.02599f);
        colore1[56] = new Color3f(0.63181f, 0.57760f, 0.08265f);
        colore1[57] = new Color3f(0.57869f, 0.84134f, 0.68761f);
        colore1[58] = new Color3f(0.33176f, 0.43882f, 0.44136f);
        colore1[59] = new Color3f(0.07424f, 0.68425f, 0.70684f);
        colore1[60] = new Color3f(0.65626f, 0.80904f, 0.83737f);
        colore1[61] = new Color3f(0.93282f, 0.36673f, 0.31575f);
        colore1[62] = new Color3f(0.46427f, 0.81205f, 0.54973f);
        colore1[63] = new Color3f(0.18074f, 0.74101f, 0.26380f);
        colore1[64] = new Color3f(0.92609f, 0.19754f, 0.93292f);
        colore1[65] = new Color3f(0.12621f, 0.27623f, 0.62557f);
        colore1[66] = new Color3f(0.46163f, 0.16781f, 0.41707f);
        colore1[67] = new Color3f(0.70818f, 0.15488f, 0.71168f);
        colore1[68] = new Color3f(0.76835f, 0.07718f, 0.09175f);
        colore1[69] = new Color3f(0.31705f, 0.86949f, 0.01125f);
        colore1[70] = new Color3f(0.88514f, 0.28202f, 0.33776f);
        colore1[71] = new Color3f(0.24061f, 0.35583f, 0.90108f);
        colore1[72] = new Color3f(0.09351f, 0.88404f, 0.82083f);
        colore1[73] = new Color3f(0.27237f, 0.78283f, 0.12797f);
        colore1[74] = new Color3f(0.65478f, 0.61283f, 0.99890f);
        colore1[75] = new Color3f(0.67399f, 0.19232f, 0.18721f);
        colore1[76] = new Color3f(0.23979f, 0.77399f, 0.80450f);
        colore1[77] = new Color3f(0.71059f, 0.50535f, 0.78251f);
        colore1[78] = new Color3f(0.26026f, 0.45666f, 0.52163f);
        colore1[79] = new Color3f(0.15745f, 0.97340f, 0.81000f);
        colore1[80] = new Color3f(0.16009f, 0.24551f, 0.42671f);
        colore1[81] = new Color3f(0.98991f, 0.69083f, 0.66694f);
        colore1[82] = new Color3f(0.32376f, 0.41104f, 0.95334f);
        colore1[83] = new Color3f(0.90738f, 0.10118f, 0.66467f);
        colore1[84] = new Color3f(0.38119f, 0.13664f, 0.38443f);
        colore1[85] = new Color3f(0.84125f, 0.83225f, 0.80647f);
        colore1[86] = new Color3f(0.15678f, 0.04043f, 0.83376f);
        colore1[87] = new Color3f(0.44710f, 0.21846f, 0.81037f);
        colore1[88] = new Color3f(0.69502f, 0.88235f, 0.25650f);
        colore1[89] = new Color3f(0.33298f, 0.87411f, 0.59181f);
        colore1[90] = new Color3f(0.72983f, 0.12502f, 0.19209f);
        colore1[91] = new Color3f(0.98244f, 0.83308f, 0.12097f);
        colore1[92] = new Color3f(0.75405f, 0.30945f, 0.53906f);
        colore1[93] = new Color3f(0.79267f, 0.71037f, 0.37122f);
        colore1[94] = new Color3f(0.17466f, 0.31069f, 0.37385f);
        colore1[95] = new Color3f(0.58167f, 0.88441f, 0.81037f);
        colore1[96] = new Color3f(0.16196f, 0.01133f, 0.61180f);
        colore1[97] = new Color3f(0.03901f, 0.33545f, 0.38067f);
        colore1[98] = new Color3f(0.94809f, 0.56262f, 0.40480f);
        colore1[99] = new Color3f(0.70882f, 0.81764f, 0.01607f);
        colore1[100] = new Color3f(0.11945f, 0.74162f, 0.28593f);

    }

    Color3f[] getColo3fRocksTypes() {

        return colore1;
    }

    Color3f[] getColo3fscale() {
        return colore;
    }

    Color[] getColorScale() {
        Color[] mycolor = new Color[colore.length];
        for (int i = 0; i < colore.length; i = i + 1) {
            mycolor[i] = new Color(colore[i].x, colore[i].y, colore[i].z);
        }
        return mycolor;
    }

    void hsv2rgb(float h, float ss, float v, float rgb[]) {
        float r_color = 0;
        float g_color = 0;
        float b_color = 0;
        float hue;
        float sat;
        float val_1;
        float base_1;
        int decisione;
        hue = h / 100.0f;
        sat = ss / 100.0f;
        val_1 = v / 100.0f;
        if (h > 360) {
            h = h - 360;
        }
        /*Acromatic color (gray). Hue doesn't mind.*/
        if (ss == 0) {
            float conv;
            conv = val_1;
            r_color = conv;
            b_color = conv;
            g_color = conv;
            return;
        }

        base_1 = (1.0f - sat) * val_1;
        decisione = (int) (h / 60.0f);
        switch (decisione) {
            case 0:
                r_color = (1.0f * val_1);
                g_color = (1.0f * val_1 - base_1) * (h / 60.0f) + base_1;
                b_color = base_1;
                break;
            case 1:
                r_color = ((1.0f * val_1 - base_1) * (1.0f - (modolus(h, 60.0f) / 60.0f)) + base_1);
                g_color = (1.0f * val_1);
                b_color = base_1;
                break;
            case 2:
                r_color = base_1;
                g_color = (1.0f * val_1);
                b_color = ((1.0f * val_1 - base_1) * (modolus(h, 60.0f) / 60.0f) + base_1);
                break;
            case 3:
                r_color = base_1;
                g_color = ((1.0f * val_1 - base_1) * (1.0f - (modolus(h, 60.0f) / 60.0f)) + base_1);
                b_color = (1.0f * val_1);
                break;
            case 4:
                r_color = ((1.0f * val_1 - base_1) * (modolus(h, 60.0f) / 60.0f) + base_1);
                g_color = base_1;
                b_color = (1.0f * val_1);
                break;
            case 5:
                r_color = (1.0f * val_1);
                g_color = base_1;
                b_color = ((1.0f * val_1 - base_1) * (1.0f - (modolus(h, 60.0f) / 60.0f)) + base_1);
            default:
                break;
        }
        rgb[0] = r_color;
        rgb[1] = g_color;
        rgb[2] = b_color;

    }

    int modolus(float e_1, float e_2) {
        float e_3 = (int) (e_1 / e_2);
        int modolus = (int) (e_1 - e_3 * e_2);
        return modolus;
    }

    void CreateVectorData2D() {

        VectorDataArray = new float[nxyz][TimeSteps][number_of_flow_variables * 2];
        for (int ic = 0; ic < nxyz; ic++) {
            for (int iv = 0; iv < number_of_flow_variables; iv++) {
                for (int it = 0; it < TimeSteps; it++) {
                    VectorDataArray[ic][it][iv * 2] = 0;
                    VectorDataArray[ic][it][iv * 2 + 1] = 0;
                }
            }
        }

        for (int ic = 0; ic < n_connections; ic++) {
            for (int iv = 0; iv < number_of_flow_variables; iv++) {
                for (int it = 0; it < TimeSteps; it++) {

                    int destinazione = -1;
                    int partenza = -1;
                    if (FluxDataArray[ic][it][iv] > 0) {
                        destinazione = 1;
                        partenza = 0;
                    } else {
                        partenza = 1;
                        destinazione = 0;
                    }

                    float compx = 0;
                    float compy = 0;
                    float x1 = Xo[ConnectionBlocks[ic][partenza]];
                    float y1 = Yo[ConnectionBlocks[ic][partenza]];
                    float x2 = Xo[ConnectionBlocks[ic][destinazione]];
                    float y2 = Yo[ConnectionBlocks[ic][destinazione]];
                    //debug
                    if (it == 5) {
                        if (iv == 0) {
                            if (ConnectionBlocks[ic][partenza] == 16 || ConnectionBlocks[ic][destinazione] == 16) {
                                int startdebug = 0;
                            }
                        }
                    }
                    double teta = Math.atan2((float) (y1 - y2), (float) (x1 - x2));
                    ////////
                    compx = (float) (Math.abs(FluxDataArray[ic][it][iv]) * Math.cos(teta));
                    compy = (float) (Math.abs(FluxDataArray[ic][it][iv]) * Math.sin(teta));
                    VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 2] = VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 2] + compx;
                    VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 2 + 1] = VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 2 + 1] + compy;
                }
            }
        }
        for (int iv = 0; iv < number_of_flow_variables; iv++) {
            for (int it = 0; it < TimeSteps; it++) {
                VectorModuleStatistics[iv] = 0;
            }
        }
        for (int iv = 0; iv < number_of_flow_variables; iv++) {
            for (int it = 0; it < TimeSteps; it++) {
                for (int ib = 0; ib < nxyz; ib++) {
                    double x = (double) VectorDataArray[ib][it][iv * 2];
                    double y = (double) VectorDataArray[ib][it][iv * 2 + 1];
                    double module = Math.sqrt(x * x + y * y);
                    VectorModuleStatistics[iv] = (float) Math.max(module, (double) VectorModuleStatistics[iv]);
                }
            }
        }
    }

    void CreateVectorData3D() {

        VectorDataArray = new float[nxyz][TimeSteps][number_of_flow_variables * 3];
        for (int ic = 0; ic < nxyz; ic++) {
            for (int iv = 0; iv < number_of_flow_variables; iv++) {
                for (int it = 0; it < TimeSteps; it++) {
                    VectorDataArray[ic][it][iv * 3] = 0;
                    VectorDataArray[ic][it][iv * 3 + 1] = 0;
                    VectorDataArray[ic][it][iv * 3 + 2] = 0;
                }
            }
        }

        for (int ic = 0; ic < n_connections; ic++) {
            for (int iv = 0; iv < number_of_flow_variables; iv++) {
                for (int it = 0; it < TimeSteps; it++) {

                    float compx = 0.0f;
                    float compy = 0.0f;
                    float compz = 0.0f;
                    int i1 = ConnectionBlocks[ic][0];
                    int i2 = ConnectionBlocks[ic][1];
                    float x1 = Xo[i1];
                    float y1 = Yo[i1];
                    float z1 = Zo[i1];
                    float x2 = Xo[i2];
                    float y2 = Yo[i2];
                    float z2 = Zo[i2];
                    float dx = x1 - x2;
                    float dy = y1 - y2;
                    float dz = z1 - z2;
                    float d = dist(x1, y1, z1, x2, y2, z2);
                    float flow = FluxDataArray[ic][it][iv];
                    //debug
                    if (i1 == 355 || i2 == 355) {
                        if (iv == 4 & it == 6) {

                            int startdebug = 0;

                        }
                    }

                    ////////
                    if (d > 0) {
                        compx = flow * (dx) / (2 * d);
                        compy = flow * (dy) / (2 * d);
                        compz = flow * (dz) / (2 * d);
                    }
                    if (FlowOrVel[iv] == 1) {
                        if (InterfaceArea != null) {
                            if (InterfaceArea[ic] != 0) {
                                compx = compx / InterfaceArea[ic];
                                compy = compy / InterfaceArea[ic];
                                compz = compz / InterfaceArea[ic];
                            }
                        }

                    }

                    VectorDataArray[i1][it][iv * 3] = VectorDataArray[i1][it][iv * 3] + compx;
                    VectorDataArray[i1][it][iv * 3 + 1] = VectorDataArray[i1][it][iv * 3 + 1] + compy;
                    VectorDataArray[i1][it][iv * 3 + 2] = VectorDataArray[i1][it][iv * 3 + 2] + compz;
                    VectorDataArray[i2][it][iv * 3] = VectorDataArray[i2][it][iv * 3] + compx;
                    VectorDataArray[i2][it][iv * 3 + 1] = VectorDataArray[i2][it][iv * 3 + 1] + compy;
                    VectorDataArray[i2][it][iv * 3 + 2] = VectorDataArray[i2][it][iv * 3 + 2] + compz;
                }
            }
        }
        for (int iv = 0; iv < number_of_flow_variables; iv++) {
            for (int it = 0; it < TimeSteps; it++) {
                VectorModuleStatistics[iv] = 0;
            }
        }
        for (int iv = 0; iv < number_of_flow_variables; iv++) {
            for (int it = 0; it < TimeSteps; it++) {
                for (int ib = 0; ib < nxyz; ib++) {
                    double x = (double) VectorDataArray[ib][it][iv * 3];
                    double y = (double) VectorDataArray[ib][it][iv * 3 + 1];
                    double z = (double) VectorDataArray[ib][it][iv * 3 + 2];
                    double module = Math.sqrt(x * x + y * y + z * z);
                    VectorModuleStatistics[iv] = (float) Math.max(module, (double) VectorModuleStatistics[iv]);
                }
            }
        }
    }

    void CreateVectorData3D_2() {

        VectorDataArray = new float[nxyz][TimeSteps][number_of_flow_variables * 3];
        for (int ic = 0; ic < nxyz; ic++) {
            for (int iv = 0; iv < number_of_flow_variables; iv++) {
                for (int it = 0; it < TimeSteps; it++) {
                    VectorDataArray[ic][it][iv * 3] = 0;
                    VectorDataArray[ic][it][iv * 3 + 1] = 0;
                    VectorDataArray[ic][it][iv * 3 + 2] = 0;
                }
            }
        }

        for (int ic = 0; ic < n_connections; ic++) {
            for (int iv = 0; iv < number_of_flow_variables; iv++) {
                for (int it = 0; it < TimeSteps; it++) {

                    int destinazione = -1;
                    int partenza = -1;
                    if (FluxDataArray[ic][it][iv] > 0) {
                        destinazione = 1;
                        partenza = 0;
                    } else {
                        partenza = 1;
                        destinazione = 0;
                    }

                    float compx = 0.0f;
                    float compy = 0.0f;
                    float compz = 0.0f;
                    int iz1 = ConnectionBlocks[ic][partenza] / nxy;
                    int ixy1 = ConnectionBlocks[ic][partenza] - iz1 * nxy;
                    int iz2 = ConnectionBlocks[ic][destinazione] / nxy;
                    int ixy2 = ConnectionBlocks[ic][destinazione] - iz2 * nxy;
                    float x1 = Xo[ixy1];
                    float y1 = Yo[ixy1];
                    float x2 = Xo[ixy2];
                    float y2 = Yo[ixy2];

                    //debug
//                    if(it==2) {
//                        if(iv==8){
//                        if   (ConnectionBlocks[ic][partenza]==146||ConnectionBlocks[ic][destinazione]==146){
//                         int startdebug=0;
//                        }
//                     }
//                    }
                    if (Zo[iz1] < Zo[iz2]) {
                        compz = (float) (-Math.abs(FluxDataArray[ic][it][iv]));
                    }
                    if (Zo[iz1] > Zo[iz2]) {
                        compz = (float) (+Math.abs(FluxDataArray[ic][it][iv]));
                    }
                    if (iz1 == iz2) {
                        double teta = Math.atan2((float) (y1 - y2), (float) (x1 - x2));
                        ////////
                        compx = (float) (Math.abs(FluxDataArray[ic][it][iv]) * Math.cos(teta));
                        compy = (float) (Math.abs(FluxDataArray[ic][it][iv]) * Math.sin(teta));
                    }

                    VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 3] = VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 3] + compx;
                    VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 3 + 1] = VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 3 + 1] + compy;
                    VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 3 + 2] = VectorDataArray[ConnectionBlocks[ic][destinazione]][it][iv * 3 + 2] + compz;
                }
            }
        }
        for (int iv = 0; iv < number_of_flow_variables; iv++) {
            for (int it = 0; it < TimeSteps; it++) {
                VectorModuleStatistics[iv] = 0;
            }
        }
        for (int iv = 0; iv < number_of_flow_variables; iv++) {
            for (int it = 0; it < TimeSteps; it++) {
                for (int ib = 0; ib < nxyz; ib++) {
                    double x = (double) VectorDataArray[ib][it][iv * 3];
                    double y = (double) VectorDataArray[ib][it][iv * 3 + 1];
                    double z = (double) VectorDataArray[ib][it][iv * 3 + 2];
                    double module = Math.sqrt(x * x + y * y + z * z);
                    VectorModuleStatistics[iv] = (float) Math.max(module, (double) VectorModuleStatistics[iv]);
                }
            }
        }
    }

    void CreateGlobalStatistics() {
        float variableMinMax[];
        variableMinMax = new float[4];
        for (int i = 0; i < number_of_variables; i++) {
            minmaxDataArray(i, variableMinMax);
            for (int j = 0; j < 4; j++) {
                globalStatistics[i][j] = variableMinMax[j];
            }
        }
        CreateGlobalScale();
    }

    void CreateGlobalScale() {
        for (int i = 0; i < number_of_variables; i++) {
            for (int j = 0; j < 2; j++) {
                GlobalScale[i][j] = globalStatistics[i][j];
            }
        }
    }

    void minmaxDataArray(int variable, float stat[]) {

        double mean = 0;
        double std = 0;
        stat[0] = 1.0E35f;//dataArray[0][0][variable];
        stat[1] = -1.0E35f;//dataArray[0][0][variable];
        int n_p = 0;
        for (int timestep = 0; timestep < TimeSteps; timestep++) {
            for (int i_b = 0; i_b < nxyz; i_b = i_b + 1) {
                if (dataArray[i_b][timestep][variable] >= big_float) {
                    continue;
                }
                n_p++;
                mean = mean + dataArray[i_b][timestep][variable];
                std = std + dataArray[i_b][timestep][variable] * dataArray[i_b][timestep][variable];
                if (stat[0] >= dataArray[i_b][timestep][variable]) {
                    stat[0] = dataArray[i_b][timestep][variable];
                }
                if (stat[1] <= dataArray[i_b][timestep][variable]) {
                    stat[1] = dataArray[i_b][timestep][variable];
                }
            }
        }
        mean = mean / (n_p);
        std = std / (n_p);
        std = std - mean * mean;
        std = Math.sqrt(std);
        stat[2] = (float) mean;
        stat[3] = (float) std;
    }

    public HistTestData[] makeHTD(int numisosurfaces, int numvicini, int n_x, int n_y, int n_z, float isovalue) {
        //creo la stima di tutti...
        double[] stat = new double[2];
//         n_x=20;
//         n_y=20;
//         n_z=35;
        double[][][] hist3D = makeTough2Data3D(numvicini, n_x, n_y, n_z, stat);
        // ora decido le isolinee...
        double range = stat[1] - stat[0];
        double deltaiso = range / (double) (numisosurfaces + 2);

        HistTestData[] htd = new HistTestData[numisosurfaces];
        if (isovalue > 0) {
            numisosurfaces = 1;
        }
        for (int i = 0; i < numisosurfaces; i++) {
            float isolinea;
            if (isovalue > 0) {
                isolinea = isovalue;
            } else {
                isolinea = (float) (stat[0] + deltaiso * (double) (i + 1));
            }

            double[][] ht2d = makeIsoSurface(isolinea, hist3D, n_x, n_y, n_z);
            htd[i] = new HistTestData();
            htd[i].set_nxny(n_x, n_y, n_z);
            htd[i].setlowLeftupRight(xmin, ymin, xmax, ymax);
            htd[i].setData(ht2d);

        }

        return htd;
    }

    public double[][][] makeTough2Data3D(int numvicini, int n_x, int n_y, int n_z, double stat[]) {
        double[][][] hist3D;
        hist3D = new double[n_y][n_x][n_z];
        Point3f[][][] grid = new Point3f[n_y][n_x][n_z];
        Point3f lowLeft3D = new Point3f(xmin, ymin, zmin);
        Point3f upRight3D = new Point3f(xmax, ymax, zmax);
        float dx = (upRight3D.x - lowLeft3D.x) / (n_x - 1);
        float dy = (upRight3D.y - lowLeft3D.y) / (n_y - 1);
        float dz = (upRight3D.z - lowLeft3D.z) / (n_z - 1);
        stat[0] = 1e45;
        stat[1] = -1e45;

        for (int x = 0; x < n_x; x++) {
            for (int y = 0; y < n_y; y++) {
                for (int z = 0; z < n_z; z++) {
                    float xo = lowLeft3D.x + x * dx;
                    float yo = lowLeft3D.y + y * dy;
                    float zo = lowLeft3D.z + z * dz;
//                    String out="y="+Float.toString(y)+"x="+Float.toString(x)+"z="+Float.toString(z);
//                    System.out.print(out);

                    grid[y][x][z] = new Point3f(xo, yo, zo);
                    hist3D[y][x][z] = makeinterpolation3D(xo, yo, zo, numvicini);
                    stat[0] = Math.min(stat[0], hist3D[y][x][z]);
                    stat[1] = Math.max(stat[1], hist3D[y][x][z]);

                }
            }

        }

        return hist3D;
    }

    public double[][][][] makeTough2Data4D(int numvicini, int n_x, int n_y, int n_z, double stat[]) {
        double[][][][] hist3D;
        hist3D = new double[n_y][n_x][n_z][TimeSteps];
        Point3f[][][] grid = new Point3f[n_y][n_x][n_z];
        Point3f lowLeft3D = new Point3f(xmin, ymin, zmin);
        Point3f upRight3D = new Point3f(xmax, ymax, zmax);
        float dx = (upRight3D.x - lowLeft3D.x) / (n_x - 1);
        float dy = (upRight3D.y - lowLeft3D.y) / (n_y - 1);
        float dz = (upRight3D.z - lowLeft3D.z) / (n_z - 1);
        stat[0] = 1e45;
        stat[1] = -1e45;

        for (int x = 0; x < n_x; x++) {
            for (int y = 0; y < n_y; y++) {
                for (int z = 0; z < n_z; z++) {

                    float xo = lowLeft3D.x + x * dx;
                    float yo = lowLeft3D.y + y * dy;
                    float zo = lowLeft3D.z + z * dz;
//                    String out="y="+Float.toString(y)+"x="+Float.toString(x)+"z="+Float.toString(z);
//                    System.out.print(out);
                    grid[y][x][z] = new Point3f(xo, yo, zo);
                    double[] temp = makeinterpolation4D(xo, yo, zo, numvicini);
                    for (int t = 0; t < TimeSteps; t++) {

                        hist3D[y][x][z][t] = temp[t];
                        stat[0] = Math.min(stat[0], hist3D[y][x][z][t]);
                        stat[1] = Math.max(stat[1], hist3D[y][x][z][t]);
                    }
                }
            }

        }

        return hist3D;
    }

    public double[][] makeTough2Data3DXY(int numvicini, int n_x, int n_y, double stat[], float z) {
        double[][] hist3D;
        hist3D = new double[n_y][n_x];

        Point3f lowLeft3D = new Point3f(xmin, ymin, zmin);
        Point3f upRight3D = new Point3f(xmax, ymax, zmax);
        float dx = (upRight3D.x - lowLeft3D.x) / (n_x - 1);
        float dy = (upRight3D.y - lowLeft3D.y) / (n_y - 1);

        for (int x = 0; x < n_x; x++) {
            for (int y = 0; y < n_y; y++) {

                float xo = lowLeft3D.x + x * dx;
                float yo = lowLeft3D.y + y * dy;
                float zo = z;
                hist3D[y][x] = makeinterpolation3D(xo, yo, zo, numvicini);

            }

        }
        stat[0] = hist3D[0][0];
        stat[1] = hist3D[0][0];
        for (int x = 0; x < n_x; x++) {
            for (int y = 0; y < n_y; y++) {
                if (stat[0] >= hist3D[y][x]) {
                    stat[0] = hist3D[y][x];
                }
                if (stat[1] <= hist3D[y][x]) {
                    stat[1] = hist3D[y][x];
                }
            }

        }

        return hist3D;
    }

    public double[][] makeTough2Data3DXZ(int numvicini, int n_x, int n_z, double stat[], float y) {
        double[][] hist3D;
        hist3D = new double[n_z][n_x];

        Point3f lowLeft3D = new Point3f(xmin, ymin, zmin);
        Point3f upRight3D = new Point3f(xmax, ymax, zmax);
        float dx = (upRight3D.x - lowLeft3D.x) / (n_x - 1);
        float dz = (upRight3D.z - lowLeft3D.z) / (n_z - 1);

        for (int x = 0; x < n_x; x++) {
            for (int z = 0; z < n_z; z++) {

                float xo = lowLeft3D.x + x * dx;
                float zo = lowLeft3D.z + z * dz;
                float yo = y;
                hist3D[n_z - 1 - z][x] = makeinterpolation3D(xo, yo, zo, numvicini);
            }

        }
        stat[0] = hist3D[0][0];
        stat[1] = hist3D[0][0];
        for (int x = 0; x < n_x; x++) {
            for (int z = 0; z < n_z; z++) {
                if (stat[0] >= hist3D[z][x]) {
                    stat[0] = hist3D[z][x];
                }
                if (stat[1] <= hist3D[z][x]) {
                    stat[1] = hist3D[z][x];
                }
            }

        }

        return hist3D;
    }

    public double[][] makeTough2Data3DYZ(int numvicini, int n_y, int n_z, double stat[], float x) {
        double[][] hist3D;
        hist3D = new double[n_z][n_y];

        Point3f lowLeft3D = new Point3f(xmin, ymin, zmin);
        Point3f upRight3D = new Point3f(xmax, ymax, zmax);
        float dy = (upRight3D.y - lowLeft3D.y) / (n_y - 1);
        float dz = (upRight3D.z - lowLeft3D.z) / (n_z - 1);

        for (int y = 0; y < n_y; y++) {
            for (int z = 0; z < n_z; z++) {

                float yo = lowLeft3D.y + y * dy;
                float zo = lowLeft3D.z + z * dz;
                float xo = x;
//                    int layervicino=0;
//                    float mindist=Math.abs(Zo[0]-zo);
//                    for(int iz=0;iz<nz;iz++)
//                    {
//                        float distz=Math.abs(Zo[iz]-zo);
//                        if(mindist>distz){
//                            mindist=distz;
//                            layervicino=iz;
//                        }
//                    }
                hist3D[n_z - 1 - z][y] = makeinterpolation3D(xo, yo, zo, numvicini);

            }

        }
        stat[0] = hist3D[0][0];
        stat[1] = hist3D[0][0];
        for (int y = 0; y < n_y; y++) {
            for (int z = 0; z < n_z; z++) {
                if (stat[0] >= hist3D[z][y]) {
                    stat[0] = hist3D[z][y];
                }
                if (stat[1] <= hist3D[z][y]) {
                    stat[1] = hist3D[z][y];
                }
            }

        }

        return hist3D;
    }

    public double[][][] makeTough2Data2D(int numvicini, int n_x, int n_y, int n_z, float xyz, double stat[]) {
        double[][][] hist3D;
        hist3D = new double[n_y][n_x][n_z];

        Point3f lowLeft3D = new Point3f(xmin, ymin, zmin);
        Point3f upRight3D = new Point3f(xmax, ymax, zmax);
        float dx, dy, dz;
        dx = 0;
        dy = 0;
        dz = 0;
        float xo = 0;
        float yo = 0;
        float zo = 0;
        if (n_x == 1) {
            xo = xyz;

        } else {
            dx = (upRight3D.x - lowLeft3D.x) / (n_x - 1);
        }
        if (n_y == 1) {
            yo = xyz;
        } else {
            dy = (upRight3D.y - lowLeft3D.y) / (n_y - 1);
        }
        if (n_z == 1) {
            zo = xyz;
        } else {
            dz = (upRight3D.z - lowLeft3D.z) / (n_z - 1);
        }

        for (int x = 0; x < n_x; x++) {
            for (int y = 0; y < n_y; y++) {
                for (int z = 0; z < n_z; z++) {

                    if (n_x == 1) {
                        xo = xyz;
                    } else {
                        xo = lowLeft3D.x + x * dx;
                    }
                    if (n_y == 1) {
                        yo = xyz;
                    } else {
                        yo = lowLeft3D.y + y * dy;
                    }
                    if (n_z == 1) {
                        zo = xyz;
                    } else {
                        zo = lowLeft3D.z + z * dz;
                    }

                    hist3D[y][x][z] = makeinterpolation3D(xo, yo, zo, numvicini);

                }
            }

        }
        stat[0] = hist3D[0][0][0];
        stat[1] = hist3D[0][0][0];
        for (int x = 0; x < n_x; x++) {
            for (int y = 0; y < n_y; y++) {
                for (int z = 0; z < n_z; z++) {
                    if (stat[0] >= hist3D[y][x][z]) {
                        stat[0] = hist3D[y][x][z];
                    }
                    if (stat[1] <= hist3D[y][x][z]) {
                        stat[1] = hist3D[y][x][z];
                    }
                }
            }

        }

        return hist3D;
    }

    private double makeinterpolation2D(float xo, float yo, int layer, int n_neighbord) {
        float z = 0;

        float[][] distance = new float[nxy][2];

        for (int ib = 0; ib < nxy; ib++) {
            float x1 = Xo[ib];
            float y1 = Yo[ib];
            distance[ib][0] = dist(xo, yo, x1, y1);
            distance[ib][1] = ib;
        }
        sort(distance, nxy);
        if (distance[0][0] == 0) {
            z = dataArray[(int) (distance[0][1] + nxy * layer)][actualTimeToPlot][actualVariableToPlot];
        } else {
            z = 0.0f;
            float weight = 0;
            for (int ib = 0; ib < n_neighbord; ib++) {
                z = z + dataArray[(int) (distance[ib][1] + nxy * layer)][actualTimeToPlot][actualVariableToPlot] / distance[ib][0];
                weight = weight + 1.0f / distance[ib][0];
            }
            z = z / weight;
        }

        return z;
    }

    private float[][] neighbord_brute_force(float xo, float yo, float zo, int n_neighbord) {
        float[][] vicinaggio = new float[n_neighbord + 1][2];
        for (int ib = 0; ib < n_neighbord; ib++) {

            float x1 = Xo[ib];
            float y1 = Yo[ib];
            float z1 = Zo[ib];
            vicinaggio[ib][0] = dist(xo, yo, zo, x1, y1, z1);
            vicinaggio[ib][1] = ib;
        }
        sort(vicinaggio, n_neighbord);
        for (int ib = n_neighbord; ib < nxyz; ib++) {
            float x1 = Xo[ib];
            float y1 = Yo[ib];
            float z1 = Zo[ib];
            float distancetemp = dist(xo, yo, zo, x1, y1, z1);
            if (distancetemp < vicinaggio[n_neighbord - 1][0]) {//questo nel caso sia minore del primo:� SBAGLIATO!!!!!devo vedere
                //se � compreso dentro i primi n_vicini, non solo IL PRIMO..!!!!

                vicinaggio[n_neighbord][0] = distancetemp;
                vicinaggio[n_neighbord][1] = ib;
                sort(vicinaggio, n_neighbord + 1);
            }

        }
        return vicinaggio;
    }

    private void create_cubes() {
        if (cubecontainerdone == false) {
            //scegliamo quanti parallelepipedi fare. proviamo 10,10,10
            //tanto poi devo fare la spiral search...
            float lmin = min(xmax - xmin, ymax - ymin);
            lmin = min(lmin, zmax - zmin);
            //nx*ny*nz=2000/1000;a=2
            //nx/a*ny/a*nz/a=2000/a^3
            //
            //
            if (lmin <= 0.f) {
                ni = 10;
                nj = 10;
                nk = 10;
            } else {
                float dim_min = lmin / 10;
                ni = (int) ((xmax - xmin) / dim_min);
                nj = (int) ((ymax - ymin) / dim_min);
                nk = (int) ((zmax - zmin) / dim_min);
            }
            int limit = 1000;
            int tot_n = ni * nj * nk;
            if (tot_n > limit) {   //questo da migliorare

                ni = (int) ((float) ni / ((int) Math.pow((float) tot_n / (float) limit, .33f)));
                nj = (int) ((float) nj / ((int) Math.pow((float) tot_n / (float) limit, .33f)));
                nk = (int) ((float) nk / ((int) Math.pow((float) tot_n / (float) limit, .33f)));
            }
            int tot_nuovo = ni * nj * nk;

            float dim_x = (xmax - xmin) / ni;
            float dim_y = (ymax - ymin) / nj;
            float dim_z = (zmax - zmin) / nk;

            //creiamo [nx][ny][nz]container
            //questo � meglio farlo globale perch� va fatto una volta sola
            //anzi, andrebbe fatto in background quando ho i dati....
            //per ora lo sbatto qui
            cubecontainer = new ArrayList[ni][nj][nk];
            for (int i_1 = 0; i_1 < ni; i_1++) {
                for (int j_1 = 0; j_1 < nj; j_1++) {
                    for (int k_1 = 0; k_1 < nk; k_1++) {
                        cubecontainer[i_1][j_1][k_1] = new ArrayList();
                    }
                }
            }
            for (int ib = 0; ib < nxyz; ib++) {
                int i = (int) (((Xo[ib] - xmin) / (xmax - xmin)) * (ni - 1));
                int j = (int) (((Yo[ib] - ymin) / (ymax - ymin)) * (nj - 1));
                int k = (int) (((Zo[ib] - zmin) / (zmax - zmin)) * (nk - 1));
                cubecontainer[i][j][k].add(ib);
            }
        }
        cubecontainerdone = true;
    }

    private float[][] neighbord_Hipercube(float xo, float yo, float zo, int n_neighbord) {
        float[][] vicinaggio = new float[n_neighbord + 1][2];
        //determinare quale cubo �...
        int i = (int) (((xo - xmin) / (xmax - xmin)) * (ni - 1));
        int j = (int) (((yo - ymin) / (ymax - ymin)) * (nj - 1));
        int k = (int) (((zo - zmin) / (zmax - zmin)) * (nk - 1));

        int start_i = 0;
        int stop_i = 0;
        int start_j = 0;
        int stop_j = 0;
        int start_k = 0;
        int stop_k = 0;
        getRing();
        boolean done = false;
        int distance_ring = 3;
        while (!done) {
            int n_point = 0;
            start_i = max(0, i - distance_ring);
            stop_i = min(i + distance_ring, ni - 1);
            start_j = max(0, j - distance_ring);
            stop_j = min(j + distance_ring, nj - 1);
            start_k = max(0, k - distance_ring);
            stop_k = min(k + distance_ring, nk - 1);
            for (int i_1 = start_i; i_1 <= stop_i; i_1++) {
                for (int j_1 = start_j; j_1 <= stop_j; j_1++) {
                    for (int k_1 = start_k; k_1 <= stop_k; k_1++) {
                        n_point = n_point + cubecontainer[i_1][j_1][k_1].size();
                    }
                }
            }
            if (n_point < n_neighbord) {
                distance_ring++;
            } else {
                done = true;
            }
        }
        //inizio ricerca vera

        int n_iniz = 0;
        for (int i_1 = start_i; i_1 <= stop_i; i_1++) {
            for (int j_1 = start_j; j_1 <= stop_j; j_1++) {
                for (int k_1 = start_k; k_1 <= stop_k; k_1++) {
                    int super_temp = cubecontainer[i_1][j_1][k_1].size();
                    for (int i_cube = 0; i_cube < cubecontainer[i_1][j_1][k_1].size(); i_cube++) {
                        int ib = (Integer) cubecontainer[i_1][j_1][k_1].get(i_cube);
                        float x1 = Xo[ib];
                        float y1 = Yo[ib];
                        float z1 = Zo[ib];
                        float distancetemp = dist(xo, yo, zo, x1, y1, z1);
                        if (n_iniz < n_neighbord) {
                            vicinaggio[n_iniz][0] = distancetemp;
                            vicinaggio[n_iniz][1] = ib;
                            n_iniz++;
                            if (n_iniz >= n_neighbord) {
                                sort(vicinaggio, n_neighbord);
                            }
                        } else if (distancetemp < vicinaggio[n_neighbord - 1][0]) {
                            vicinaggio[n_neighbord][0] = distancetemp;
                            vicinaggio[n_neighbord][1] = ib;
                            sort(vicinaggio, n_neighbord + 1);
                        }
                    }
                }
            }
        }
        if (n_iniz < n_neighbord) {
            //houston, we get a problem!

        }
        return vicinaggio;
    }

    private void getRing() {

    }

    private double makeinterpolation3D(float xo, float yo, float zo, int n_neighbord) {
        double z = 0;
        float[][] distance;
        if (searchmethod == 0) {
            create_cubes();
            distance = neighbord_Hipercube(xo, yo, zo, n_neighbord);

        } else {
            distance = neighbord_brute_force(xo, yo, zo, n_neighbord);
        }
        //confronto

//        for (int ii=0;ii<n_neighbord;ii++){
//            if(distance[ii][1]!=distance1[ii][1])
//                {
//                    String out="hiper="+Float.toString(distance[ii][1])+"brute="+Float.toString(distance1[ii][1]);
//                    System.out.print(out);
//                    System.out.print("");
//                    String out2=Float.toString(xo)+Float.toString(yo)+Float.toString(zo);
//                    System.out.println(out2);
//                    float[][] temp=neighbord_Hipercube(xo,yo,zo,n_neighbord);
//                }
//            
//        }
        /////////////////
        if (distance[0][0] == 0) {
            z = dataArray[(int) (distance[0][1])][actualTimeToPlot][actualVariableToPlot];
        } else {
            z = 0.0f;
            double weight = 0;
            for (int ib = 0; ib < n_neighbord; ib++) {
                z = z + dataArray[(int) (distance[ib][1])][actualTimeToPlot][actualVariableToPlot] / Math.pow(distance[ib][0], exp_factor_estimation);
                weight = weight + 1.0f / Math.pow((double) distance[ib][0], exp_factor_estimation);
            }
            z = z / weight;
        }
        return z;
    }

    private double[] makeinterpolation4D(float xo, float yo, float zo, int n_neighbord) {
        double[] z = new double[TimeSteps];
        float[][] distance;
        if (searchmethod == 0) {
            create_cubes();
            distance = neighbord_Hipercube(xo, yo, zo, n_neighbord);

        } else {
            distance = neighbord_brute_force(xo, yo, zo, n_neighbord);
        }
        //confronto

//        for (int ii=0;ii<n_neighbord;ii++){
//            if(distance[ii][1]!=distance1[ii][1])
//                {
//                    String out="hiper="+Float.toString(distance[ii][1])+"brute="+Float.toString(distance1[ii][1]);
//                    System.out.print(out);
//                    System.out.print("");
//                    String out2=Float.toString(xo)+Float.toString(yo)+Float.toString(zo);
//                    System.out.println(out2);
//                    float[][] temp=neighbord_Hipercube(xo,yo,zo,n_neighbord);
//                }
//            
//        }
        /////////////////
        for (int t = 0; t < TimeSteps; t++) {
            if (distance[0][0] == 0) {
                z[t] = dataArray[(int) (distance[0][1])][t][actualVariableToPlot];
            } else {
                z[t] = 0.0f;
                double weight = 0;
                for (int ib = 0; ib < n_neighbord; ib++) {
                    z[t] = z[t] + dataArray[(int) (distance[ib][1])][t][actualVariableToPlot] / Math.pow(distance[ib][0], exp_factor_estimation);
                    weight = weight + 1.0f / Math.pow((double) distance[ib][0], exp_factor_estimation);
                }
                if (weight == 0) {
                    z[t] = -1234.1234;
                } else {
                    z[t] = z[t] / weight;
                }

            }
        }
        return z;

    }

    private float dist(float xo, float yo, float x1, float y1) {
        return (float) Math.sqrt((x1 - xo) * (x1 - xo) + (y1 - yo) * (y1 - yo));
    }

    private float dist(float xo, float yo, float zo, float x1, float y1, float z1) {
        return (float) Math.sqrt((x1 - xo) * (x1 - xo) + (y1 - yo) * (y1 - yo) + (z1 - zo) * (z1 - zo));
    }

    void sort(int a[]) {
        int lenght = a.length;

        for (int i1 = 0; i1 < lenght; i1++) {
            for (int i2 = i1 + 1; i2 < lenght; i2++) {
                if (a[i2] < a[i1]) {

                    int temp = a[i1];
                    a[i1] = a[i2];
                    a[i2] = temp;

                }
            }
        }
    }

    void sort(float a[][], int lenght) {
        for (int i1 = 0; i1 < lenght; i1++) {
            for (int i2 = i1 + 1; i2 < lenght; i2++) {
                if (a[i2][0] < a[i1][0]) {
                    for (int i3 = 0; i3 < 2; i3++) {
                        float temp = a[i1][i3];
                        a[i1][i3] = a[i2][i3];
                        a[i2][i3] = temp;
                    }
                }
            }
        }
    }

    void sort(double a[][], int lenght) {
        for (int i1 = 0; i1 < lenght; i1++) {
            for (int i2 = i1 + 1; i2 < lenght; i2++) {
                if (a[i2][0] < a[i1][0]) {
                    for (int i3 = 0; i3 < 2; i3++) {
                        double temp = a[i1][i3];
                        a[i1][i3] = a[i2][i3];
                        a[i2][i3] = temp;
                    }
                }
            }
        }
    }

    private double[][] makeIsoSurface(float isoquota, double[][][] hist3D, int n_x, int n_y, int n_z) {
        double[][] ht2d = new double[n_y][n_x];
        ArrayList famiglietutte = new ArrayList();
        Point3f lowLeft3D = new Point3f(xmin, ymin, zmin);
        Point3f upRight3D = new Point3f(xmax, ymax, zmax);
        float dx = (upRight3D.x - lowLeft3D.x) / (n_x - 1);
        float dy = (upRight3D.y - lowLeft3D.y) / (n_y - 1);
        float dz = (upRight3D.z - lowLeft3D.z) / (n_z - 1);
        for (int x = 0; x < n_x; x++) {
            for (int y = 0; y < n_y; y++) {
                ht2d[y][x] = zmin;
                for (int z = 0; z < n_z - 1; z++) {
                    float zo = zmin + dz * z;

                    if (hist3D[y][x][z] >= isoquota && hist3D[y][x][z + 1] < isoquota) {
                        double delta = Math.abs(hist3D[y][x][z] - hist3D[y][x][z + 1]);
                        double start = hist3D[y][x][z + 1];
                        double percent = (isoquota - start) / delta;
                        if (percent < 0 || percent > 1.0f) {
                            int houstonwegetaproblem = 1;
                        }
                        double deltaz = dz * percent;

                        double zz = zo + deltaz;
                        ht2d[y][x] = zz;
                        int[] pointsindex = {y, x, z, y, x, z + 1};
                        famiglietutte.add(pointsindex);
                        famiglietutte.add(zz);

                    }
                    if (hist3D[y][x][z + 1] >= isoquota && hist3D[y][x][z] < isoquota) {
                        double delta = Math.abs(hist3D[y][x][z] - hist3D[y][x][z + 1]);
                        double start = hist3D[y][x][z];
                        double deltaz = dz * (isoquota - start) / delta;

                        double zz = zo - deltaz;
                        ht2d[y][x] = zz;
                        int[] pointsindex = {y, x, z, y, x, z + 1};
                        famiglietutte.add(pointsindex);
                        famiglietutte.add(zz);
                    }

                }
            }

        }
        int n_fami = famiglietutte.size();
        return ht2d;
    }

    void minmaxDataArray(int timestep, int variable, float stat[]) {
        //minmax=new float[2];
        //index1: n_block
        //index2: timestep
        //index3: variable
        //stat[0]=min
        //stat[1]=max
        //stat[2]=mean
        //stat[3]=std
        double mean = 0;
        double std = 0;
        stat[0] = dataArray[0][0][variable];
        stat[1] = dataArray[0][0][variable];

        for (int i_b = 1; i_b < nxyz; i_b = i_b + 1) {
            mean = mean + dataArray[i_b][timestep][variable];
            std = std + dataArray[i_b][timestep][variable] * dataArray[i_b][timestep][variable];
            if (stat[0] >= dataArray[i_b][timestep][variable]) {
                stat[0] = dataArray[i_b][timestep][variable];
            }
            if (stat[1] <= dataArray[i_b][timestep][variable]) {
                stat[1] = dataArray[i_b][timestep][variable];
            }
        }

        mean = mean / (nxyz);
        std = std / (nxyz);
        std = std - mean * mean;
        std = Math.sqrt(std);
        stat[2] = (float) mean;
        stat[3] = (float) std;
    }

    private static Object resizeArray(Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(
                elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) {
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        // USE//
//   int[] a = {1,2,3};
//   a = (int[])resizeArray(a,5);
//   a[3] = 4;
//   a[4] = 5;
//   for (int i=0; i<a.length; i++)
//      System.out.println (a[i]);
        return newArray;

    }

    void minmaxXYPlaneDataArray(int timestep, int variable, float minZ[], float maxZ[]) {
        //minmax=new float[2];
        int i_b = 0;

        for (int iz = 0; iz < nz; iz = iz + 1) {
            minZ[iz] = dataArray[iz][timestep][variable];
            maxZ[iz] = dataArray[iz][timestep][variable];

            for (int ix = 0; ix < nx; ix = ix + 1) {

                for (int iy = 0; iy < ny; iy = iy + 1) {

                    i_b = (iz) + (iy) * nz + ix * ny * nz;
                    if (minZ[iz] >= dataArray[i_b][timestep][variable]) {
                        minZ[iz] = dataArray[i_b][timestep][variable];
                    }
                    if (maxZ[iz] <= dataArray[i_b][timestep][variable]) {
                        maxZ[iz] = dataArray[i_b][timestep][variable];
                    }
                }
            }
        }
    }
}
