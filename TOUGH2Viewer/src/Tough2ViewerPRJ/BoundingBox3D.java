package Tough2ViewerPRJ;

import java.util.ArrayList;
import javax.media.j3d.*;
import javax.vecmath.*;

//Draws the axis & lines for translucent bounding box
public class BoundingBox3D extends Shape3D {

    LineArray line1;
    ColoringAttributes ca;
    Color3f ShapeColor;
    float xmin_l, ymin_l, zmin_l, xmax_l, ymax_l, zmax_l;

    public BoundingBox3D(float xmin, float ymin, float zmin, float xmax, float ymax, float zmax, float scalefactor) {
        super();
        float xc = (xmax + xmin) / 2.0f;
        float yc = (ymax + ymin) / 2.0f;
        float zc = (zmax + zmin) / 2.0f;
        xmin_l = (-xc + xmin) * scalefactor;
        ymin_l = (-yc + ymin) * scalefactor;
        zmin_l = (-zc + zmin) * scalefactor;
        xmax_l = (-xc + xmax) * scalefactor;
        ymax_l = (-yc + ymax) * scalefactor;
        zmax_l = (-zc + zmax) * scalefactor;

        LineAttributes la1 = new LineAttributes();
        la1.setLineWidth(1.5f);
        Appearance app = new Appearance();
        app.setLineAttributes(la1);
        ShapeColor = new Color3f(1.0f, 1.0f, 1.0f);
        ca = new ColoringAttributes(ShapeColor, ColoringAttributes.FASTEST);
        ca.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        app.setColoringAttributes(ca);
        setAppearance(app);

        this.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        //quadrato superiore

        Point3f[] myPoint3f = new Point3f[8];
        myPoint3f[0] = new Point3f(xmin_l, ymin_l, zmax_l);
        myPoint3f[1] = new Point3f(xmax_l, ymin_l, zmax_l);
        myPoint3f[2] = new Point3f(xmax_l, ymax_l, zmax_l);
        myPoint3f[3] = new Point3f(xmin_l, ymax_l, zmax_l);
        myPoint3f[4] = new Point3f(xmin_l, ymin_l, zmin_l);
        myPoint3f[5] = new Point3f(xmax_l, ymin_l, zmin_l);
        myPoint3f[6] = new Point3f(xmax_l, ymax_l, zmin_l);
        myPoint3f[7] = new Point3f(xmin_l, ymax_l, zmin_l);

        Point3f[] myPoint3fUp = new Point3f[5];
        myPoint3fUp[0] = myPoint3f[0];
        myPoint3fUp[1] = myPoint3f[1];
        myPoint3fUp[2] = myPoint3f[2];
        myPoint3fUp[3] = myPoint3f[3];
        myPoint3fUp[4] = myPoint3f[0];

        LineArray tempLineArrayUp = myLineArray(myPoint3fUp);
        tempLineArrayUp.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
        tempLineArrayUp.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_WRITE);
        tempLineArrayUp.setCapability(LineArray.ALLOW_COORDINATE_READ);
        tempLineArrayUp.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
        addGeometry(tempLineArrayUp);

        Point3f[] myPoint3fDown = new Point3f[5];
        myPoint3fDown[0] = myPoint3f[4];
        myPoint3fDown[1] = myPoint3f[5];
        myPoint3fDown[2] = myPoint3f[6];
        myPoint3fDown[3] = myPoint3f[7];
        myPoint3fDown[4] = myPoint3f[4];
        LineArray tempLineArrayDown = myLineArray(myPoint3fDown);
        tempLineArrayDown.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
        tempLineArrayDown.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_WRITE);
        tempLineArrayDown.setCapability(LineArray.ALLOW_COORDINATE_READ);
        tempLineArrayDown.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
        addGeometry(tempLineArrayDown);

        for (int i = 0; i < 4; i++) {
            Point3f[] myPoint3f1 = new Point3f[2];
            myPoint3f1[0] = myPoint3f[i];
            myPoint3f1[1] = myPoint3f[i + 4];

            LineArray tempLineArrayI = myLineArray(myPoint3f1);
            tempLineArrayI.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
            tempLineArrayI.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_WRITE);
            tempLineArrayI.setCapability(LineArray.ALLOW_COORDINATE_READ);
            tempLineArrayI.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
            addGeometry(tempLineArrayI);
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
