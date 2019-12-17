package Tough2ViewerPRJ;

import javax.vecmath.*;
import javax.media.j3d.*;

//Draws arrow for the 3D flow/flow model
public class Arrow extends Shape3D {

    LineArray line1;

    public Arrow(float lenght, Color3f ArrowColor) {

        //Make Arrow
        LineAttributes la1 = new LineAttributes();
        la1.setLineWidth(1.5f);
        Appearance app = new Appearance();
        app.setLineAttributes(la1);

        ColoringAttributes ca = new ColoringAttributes(ArrowColor, ColoringAttributes.FASTEST);
        app.setColoringAttributes(ca);
        setAppearance(app);
        //CORPO;ci vogliono 6 punti per ogni linea
        line1 = new LineArray(6 * 5, LineArray.COORDINATES);
        line1.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
        line1.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_WRITE);
        line1.setCapability(LineArray.ALLOW_COORDINATE_READ);
        line1.setCapability(LineArray.ALLOW_COORDINATE_WRITE);

        Point3f[] lineVerts = new Point3f[10];
        lineVerts[0] = new Point3f(0.0f, 0.0f, 0.0f);
        lineVerts[1] = new Point3f(0.0f, lenght, 0.0f);

        lineVerts[2] = new Point3f(0.0f, lenght, 0.0f);
        lineVerts[3] = new Point3f(-0.2f * lenght, lenght * 0.8f, 0.0f);

        lineVerts[4] = new Point3f(0.0f, lenght, 0.0f);
        lineVerts[5] = new Point3f(0.2f * lenght, lenght * 0.8f, 0.0f);

        lineVerts[6] = new Point3f(0.0f, lenght, 0.0f);
        lineVerts[7] = new Point3f(0.0f, lenght * 0.8f, -0.2f * lenght);

        lineVerts[8] = new Point3f(0.0f, lenght, 0.0f);
        lineVerts[9] = new Point3f(0.0f, lenght * 0.8f, 0.2f * lenght);

        line1.setCoordinates(0, lineVerts);
        setGeometry(line1);

    }

    /**
     * try to modify lenght
     */
    public void modify_lenght(float lenght) {
        Point3f[] lineVerts = new Point3f[10];
        lineVerts[0] = new Point3f(0.0f, 0.0f, 0.0f);
        lineVerts[1] = new Point3f(0.0f, lenght, 0.0f);

        lineVerts[2] = new Point3f(0.0f, lenght, 0.0f);
        lineVerts[3] = new Point3f(-0.2f * lenght, lenght * 0.8f, 0.0f);

        lineVerts[4] = new Point3f(0.0f, lenght, 0.0f);
        lineVerts[5] = new Point3f(0.2f * lenght, lenght * 0.8f, 0.0f);

        lineVerts[6] = new Point3f(0.0f, lenght, 0.0f);
        lineVerts[7] = new Point3f(0.0f, lenght * 0.8f, -0.2f * lenght);

        lineVerts[8] = new Point3f(0.0f, lenght, 0.0f);
        lineVerts[9] = new Point3f(0.0f, lenght * 0.8f, 0.2f * lenght);
        line1.setCoordinates(0, lineVerts);

    }

}
