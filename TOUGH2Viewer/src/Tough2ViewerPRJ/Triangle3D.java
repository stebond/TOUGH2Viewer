/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

/**
 *
 * @author stebond
 */
import javax.media.j3d.*;
import javax.vecmath.*;

public class Triangle3D {

    float verts[];
    float normals[];
    float div = 3.0f;
    Shape3D shape;

    public Triangle3D(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, Appearance a) {

        Point3f p1 = new Point3f(x1, y1, z1);
        Point3f p2 = new Point3f(x2, y2, z2);
        Point3f p3 = new Point3f(x3, y3, z3);
        Point3f[] verts = {p1, p2, p3};
        TriangleArray triangle3D = new TriangleArray(3, TriangleArray.COORDINATES
                | TriangleArray.NORMALS);
        triangle3D.setCoordinates(0, verts);
        Vector3f normal = new Vector3f();
        Vector3f v1 = new Vector3f();
        Vector3f v2 = new Vector3f();
        Point3f[] pts = new Point3f[3];
        int i;
        for (i = 0; i < 3; i++) {
            pts[i] = new Point3f();
        }
        triangle3D.getCoordinates(0, pts);
        v1.sub(pts[1], pts[0]);
        v2.sub(pts[2], pts[0]);
        normal.cross(v1, v2);
        normal.normalize();
        triangle3D.setNormal(0, normal);
        shape = new Shape3D(triangle3D, a);

    }

    Shape3D getShape() {
        return shape;
    }
}
