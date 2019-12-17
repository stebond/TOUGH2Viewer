/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

/**
 *
 * @author stebond
 */
import javax.vecmath.*;
import javax.media.j3d.*;

public class polygonSurface3D {

//    private static Color3f shapeColor = new Color3f(0f, .6f, .6f );
    private static Color3f black = new Color3f(0f, 0f, 0f);
    private static Color3f white = new Color3f(1f, 1f, 1f);
    private static Color3f white1 = new Color3f(.1f, .1f, .1f);
    private static Color3f white2 = new Color3f(.8f, .8f, .8f);

    private QuadArray quad;
    private Material material;
    Shape3D shape;

    public polygonSurface3D(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, Color3f shapeColor) {

        Appearance app = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setBackFaceNormalFlip(true);
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        app.setPolygonAttributes(pa);
        material = new Material(shapeColor, black, white1, white2, 30.0f);
        material.setLightingEnable(true);
        material.setCapability(Material.ALLOW_COMPONENT_READ);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);

        app.setMaterial(material);

        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.NONE, 0f);
        app.setTransparencyAttributes(ta);
//        setAppearance(app);
        Point3f p1 = new Point3f(x1, y1, z1);
        Point3f p2 = new Point3f(x2, y2, z2);
        Point3f p3 = new Point3f(x3, y3, z3);
        Point3f p4 = new Point3f(x4, y4, z4);

        Point3f[] verts = new Point3f[4];
        verts[0] = p1;
        verts[1] = p2;
        verts[2] = p3;
        verts[3] = p4;
        quad.setCoordinates(0, verts);

        Point3f[] pts = new Point3f[4];
        Vector3f normal = new Vector3f();
        Vector3f v1 = new Vector3f();
        Vector3f v2 = new Vector3f();

        //compute normals for lighting
        for (int i = 0; i < 4; i++) {
            pts[i] = new Point3f();
        }
        int numG = 4;
        for (int face = 0; face < numG; face++) {
            quad.getCoordinates(face * 4, pts);
            v1.sub(pts[1], pts[0]);
            v2.sub(pts[2], pts[0]);
            normal.cross(v1, v2);
            normal.normalize();
            for (int i = 0; i < 4; i++) {
                quad.setNormal((face * 4 + i), normal);
            }
        }
//	setGeometry(quad);

    }

    Shape3D getShape() {
        return shape;
    }
}
