package Tough2ViewerPRJ;

public class GraphData {

    private double data[][];
    private String title;
    private double xmin;
    private double ymin;
    private double zmin;
    private double xmax;
    private double ymax;
    private double zmax;
    int ny;
    int nx;

    private int numPts;

    String xLabel = "X Label";
    String zLabel = "Z Label";
    String yLabel = "Y Label";

    public GraphData(int col, int row) {
        ny = row;
        nx = col;
        data = new double[ny][nx];
    }

    public void set_min(double xm, double ym, double zm) {
        xmin = xm;
        ymin = ym;
        zmin = zm;

    }

    public void set_max(double xm, double ym, double zm) {
        xmax = xm;
        ymax = ym;
        zmax = zm;
    }

    public String getTitle() {
        return title;
    }

    public String get_Z_Label() {
        return zLabel;
    }

    public double get_Z_Max() {
        return zmax;
    }

    public double get_X_Max() {
        return xmax;
    }

    public double get_Y_Max() {
        return ymax;
    }

    public double get_Z_Min() {
        return zmin;
    }

    public double get_X_Min() {
        return xmin;
    }

    public double get_Y_Min() {
        return ymin;
    }

    public String get_Y_Label() {
        return yLabel;
    }

    public String get_X_Label() {
        return xLabel;
    }

    public int getRows() {
        return ny;
    }

    public int getCols() {
        return nx;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] a) {
        for (int j = 0; j < ny; j++) {
            for (int i = 0; i < nx; i++) {
                data[j][i] = a[j][i];

            }
        }
    }

    private String strip(String str) {
        int idx = str.lastIndexOf(" ");
        int idx1 = str.indexOf("//");
        int id = Math.min(idx, idx1);
        return str.substring(0, id);
    }
}
