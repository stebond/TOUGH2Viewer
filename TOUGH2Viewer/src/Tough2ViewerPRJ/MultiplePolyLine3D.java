package Tough2ViewerPRJ;

import java.util.ArrayList;
import javax.media.j3d.*;
import javax.vecmath.*;
//Draws the axis & lines for translucent bounding box

public class MultiplePolyLine3D extends Shape3D {

    LineArray line1;
    ColoringAttributes ca;

    public MultiplePolyLine3D(ArrayList myPoint, float x_min, float y_min, float z_min, float scala, float xmin, float ymin, float zmin, Color3f ShapeColor, double Zfactor) {
        super();
        LineAttributes la1 = new LineAttributes();
        la1.setLineWidth(1.5f);
        Appearance app = new Appearance();
        app.setLineAttributes(la1);
        ca = new ColoringAttributes(ShapeColor, ColoringAttributes.FASTEST);
        ca.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        app.setColoringAttributes(ca);
        setAppearance(app);
        this.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        // setGeometry(null);

        int numeropolilinee = myPoint.size();

        for (int k = 0; k < numeropolilinee; k++) {
            Point3f[] myPoint3f = (Point3f[]) myPoint.get(k);
            Point3f[] myPoint3fScalati = new Point3f[myPoint3f.length];
            for (int i1 = 0; i1 < myPoint3f.length; i1++) {
                myPoint3fScalati[i1] = new Point3f();
                myPoint3fScalati[i1].x = (myPoint3f[i1].x - x_min) * scala + xmin;
                myPoint3fScalati[i1].y = (myPoint3f[i1].y - y_min) * scala + ymin;
                myPoint3fScalati[i1].z = (myPoint3f[i1].z - z_min) * scala * (float) Zfactor + zmin;
            }
            if (myPoint3f.length > 1) {
                LineArray tempLineArray = myLineArray(myPoint3fScalati);
                tempLineArray.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
                tempLineArray.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_WRITE);
                tempLineArray.setCapability(LineArray.ALLOW_COORDINATE_READ);
                tempLineArray.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
                addGeometry(tempLineArray);
            }

        }

    }

    void setNewColor(Color3f color1) {
        ca.setColor(color1);
    }

    private LineArray myLineArray(Point3f[] myPoint) {
        int dimension = 6 * (myPoint.length - 1);
        LineArray line2 = new LineArray(dimension, LineArray.COORDINATES);
        Point3f[] lineVerts = new Point3f[(myPoint.length - 1) * 2];
        for (int i = 0; i < myPoint.length - 1; i++) {
            lineVerts[i * 2] = myPoint[i];
            lineVerts[i * 2 + 1] = myPoint[i + 1];
        }
        line2.setCoordinates(0, lineVerts);
        return line2;
    }

}
