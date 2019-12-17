package Tough2ViewerPRJ;

import javax.swing.*;
import javax.vecmath.*;
import javax.media.j3d.*;

//Draws the axis & lines for translucent bounding box
public class PolyLine3D extends Shape3D {

    LineArray line1;

    public PolyLine3D(Point3f[] myPoint, Color3f ArrowColor) {
        LineAttributes la1 = new LineAttributes();
        la1.setLineWidth(1.5f);
        Appearance app = new Appearance();
        app.setLineAttributes(la1);
        ColoringAttributes ca = new ColoringAttributes(ArrowColor, ColoringAttributes.FASTEST);
        app.setColoringAttributes(ca);
        setAppearance(app);
        //CORPO;ci vogliono 6 punti per ogni linea
        int dimension = 6 * (myPoint.length - 1);
        line1 = new LineArray(dimension, LineArray.COORDINATES);
        line1.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
        line1.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_WRITE);
        line1.setCapability(LineArray.ALLOW_COORDINATE_READ);
        line1.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
        Point3f[] lineVerts = new Point3f[(myPoint.length - 1) * 2];
        for (int i = 0; i < myPoint.length - 1; i++) {
            lineVerts[i * 2] = myPoint[i];
            lineVerts[i * 2 + 1] = myPoint[i + 1];
        }
        line1.setCoordinates(0, lineVerts);
        setGeometry(line1);
    }
}
