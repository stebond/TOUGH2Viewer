package Tough2ViewerPRJ;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import java.awt.Font;
import javax.vecmath.*;
import javax.media.j3d.*;

//Draws the axis & lines for the 3D axis, for the 3D block model and for 3D flow/flux model.
public class Axis3D extends Group {

    Shape3D[] shapes = new Shape3D[9];

    Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
    Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
    Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
    Color3f[] colors = {red, green, blue};

    public Axis3D(Point3f center, float lenght) {
        float Xcenter = center.x;
        float Ycenter = center.y;
        float Zcenter = center.z;
        float H_cone = 0.06f * lenght;
        float R_cone = 0.02f * lenght;
        float R_Cylinder = 0.01f * lenght;
        String fontName = "Times";
        Point3f pointX = new Point3f(center.x + lenght, center.y, center.z);
        Point3f pointY = new Point3f(center.x, center.y + lenght, center.z);
        Point3f pointZ = new Point3f(center.x, center.y, center.z + lenght);
        Point3f pointX_cone = new Point3f(center.x + lenght + H_cone / 2.0f, center.y, center.z);
        Point3f pointY_cone = new Point3f(center.x, center.y + lenght + H_cone / 2.0f, center.z);
        Point3f pointZ_cone = new Point3f(center.x, center.y, center.z + lenght + H_cone / 2.0f);
        Float centramentocarattere = 0.025f * lenght;
        Point3f pointX_txt = new Point3f(center.x + lenght + 1.2f * H_cone, center.y + centramentocarattere, center.z);
        Point3f pointY_txt = new Point3f(center.x - centramentocarattere, center.y + lenght + 1.2f * H_cone, center.z);
        Point3f pointZ_txt = new Point3f(center.x - centramentocarattere, center.y, center.z + lenght + 1.2f * H_cone);

        String[] axisName = {"X", "Y", "Z"};
        Point3f[] pointB = {pointX, pointY, pointZ};
        Point3f[] pointB_cone = {pointX_cone, pointY_cone, pointZ_cone};
        Point3f[] pointB_txt = {pointX_txt, pointY_txt, pointZ_txt};
        Point3f pointA = center;
        for (int i = 0; i < 3; i = i + 1) {

            CylinderTransformer cT = new CylinderTransformer(pointA, pointB[i]);

//Float length = cT.getLength();
//holds the translation
            Transform3D transformCylinder = new Transform3D();
//...move the coordinates there
            transformCylinder.setTranslation(cT.getTranslation());
            Transform3D transformCone = new Transform3D();
            Transform3D transformTxt = new Transform3D();
            transformCone.setTranslation(new Vector3f(pointB_cone[i].x, pointB_cone[i].y, pointB_cone[i].z));
            transformTxt.setTranslation(new Vector3f(pointB_txt[i].x, pointB_txt[i].y, pointB_txt[i].z));
//get the axis and angle for rotation
            AxisAngle4f rotation = cT.getAxisAngle();
            Transform3D transform2 = new Transform3D();
            transform2.setRotation(rotation);

//combine the translation and rotation into transformCylinder
            transformCylinder.mul(transform2);
            transformCone.mul(transform2);
            transformTxt.mul(transform2);
            Appearance myApp = new Appearance();

            ColoringAttributes ca = new ColoringAttributes();
            ca.setColor(colors[i]);
            myApp.setColoringAttributes(ca);
            TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, .5f);
            myApp.setTransparencyAttributes(ta);

            Cylinder cyl = new Cylinder(R_Cylinder, lenght, myApp);
            Cone mycone = new Cone(R_cone, H_cone, Cone.GENERATE_NORMALS
                    | Cone.GENERATE_TEXTURE_COORDS | Cone.GENERATE_TEXTURE_COORDS_Y_UP, myApp);
            TransformGroup CylinderTransGrp = new TransformGroup(transformCylinder);
            CylinderTransGrp.addChild(cyl);
            TransformGroup ConeTransGrp = new TransformGroup(transformCone);
            ConeTransGrp.addChild(mycone);
            addChild(CylinderTransGrp);
            addChild(ConeTransGrp);

            Font3D f3d = new Font3D(new Font(fontName, Font.PLAIN, 1),
                    new FontExtrusion());
            Text3D txt = new Text3D(f3d, axisName[i],
                    new Point3f(.0f, .0f, .0f));
            transformTxt.setScale(.1f * lenght);
            TransformGroup txtTransGrp = new TransformGroup(transformTxt);
            Shape3D txtShape3D = new Shape3D();
            txtShape3D.setGeometry(txt);
            txtShape3D.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
            txtShape3D.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            txtShape3D.setAppearance(myApp);

            txtTransGrp.addChild(txtShape3D);

            addChild(txtTransGrp);

//	shapes[i*3+2].setGeometry(txt);
            shapes[i * 3] = cyl.getShape(0);
            shapes[i * 3 + 1] = mycone.getShape(0);
        }
    }

    Shape3D[] getShapes() {
        return shapes;
    }

}
