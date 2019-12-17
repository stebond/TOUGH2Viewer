package Tough2ViewerPRJ;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import java.util.List;
import javax.vecmath.*;
import javax.media.j3d.*;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

//Computes the plot shape
public class Surface_PLY_Shape3D extends Shape3D {

    private static Color3f shapeColor = new Color3f(0f, 1.0f, 1.0f);
    private static Color3f black = new Color3f(0f, 0f, 0f);
    private static Color3f white = new Color3f(1f, 1f, 1f);
    private static Color3f white1 = new Color3f(.7f, .7f, .7f);
    private static Color3f white2 = new Color3f(.8f, .8f, .8f);

    private QuadArray quad;
    private Material material;
    TransparencyAttributes ta;
    Appearance app;
    PolygonAttributes pa;
    GeometryInfo mioGI;
    NormalGenerator mioNG;

    public Surface_PLY_Shape3D(PLYParser ply_data, float xmin, float ymin, float zmin, double xminR, double yminR, double zminR, float scala, double Zfactor) {

        //Graph attributes
        app = new Appearance();
        pa = new PolygonAttributes();
        pa.setCapability(PolygonAttributes.ALLOW_CULL_FACE_READ);
        pa.setCapability(PolygonAttributes.ALLOW_CULL_FACE_WRITE);
        pa.setCapability(PolygonAttributes.ALLOW_MODE_READ);
        pa.setCapability(PolygonAttributes.ALLOW_MODE_WRITE);
        pa.setCapability(PolygonAttributes.ALLOW_NORMAL_FLIP_READ);
        pa.setCapability(PolygonAttributes.ALLOW_NORMAL_FLIP_WRITE);

        pa.setBackFaceNormalFlip(false);
        pa.setCullFace(PolygonAttributes.CULL_NONE);

        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        app.setPolygonAttributes(pa);

        material = new Material(shapeColor, black, white1, white2, 60.0f);
        material.setLightingEnable(true);
        material.setCapability(Material.ALLOW_COMPONENT_READ);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);

        app.setMaterial(material);

        ta = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.3f);
        ta.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
        ta.setCapability(TransparencyAttributes.ALLOW_BLEND_FUNCTION_WRITE);
        ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);

        app.setTransparencyAttributes(ta);
        setAppearance(app);

        List ply_coord_vector3D = ply_data.getVertices();
        int n_vertex_of_ply = ply_coord_vector3D.size();
        Point3d[] vertex3d = new Point3d[n_vertex_of_ply];
        for (int i = 0; i < n_vertex_of_ply; i++) {
            Vector3D tmp = (Vector3D) ply_coord_vector3D.get(i);
            vertex3d[i] = new Point3d(tmp.getX(), tmp.getY(), tmp.getZ());
        }
        List ply_face_indices = ply_data.getFaces();
        int n_of_faces = ply_face_indices.size();
        int[] face_index = new int[n_of_faces];

        for (int i = 0; i < n_of_faces; i++) {
            int[] tmp = (int[]) ply_face_indices.get(i);
            if (tmp.length != 3) {
                //warning, we handle just triangulate faces
            }
            Point3f[] verts = new Point3f[3];
            for (int j = 0; j < tmp.length; j++) {
                float x_v = (float) (vertex3d[tmp[j]].x - xminR) * scala + xmin;
                float y_v = (float) (vertex3d[tmp[j]].y - yminR) * scala + ymin;
                float z_v = (float) (vertex3d[tmp[j]].z - zminR) * scala * (float) Zfactor + zmin;

                verts[j] = new Point3f(x_v, y_v, z_v);

            }

            TriangleArray triangle3D = new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.NORMALS);
            //TriangleStripArray surface   = new TriangleStripArray(n_vertex_of_ply,GeometryArray.COORDINATES|QuadArray.NORMALS,new int[] { n_vertex_of_ply });
            triangle3D.setCoordinates(0, verts);
            triangle3D.setCapability(QuadArray.ALLOW_COORDINATE_READ);
            triangle3D.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
            //surface.setNormals(0, normTriangle);
            Vector3f normal = new Vector3f();
            Vector3f v1 = new Vector3f();
            Vector3f v2 = new Vector3f();
            Point3f[] pts = new Point3f[3];

            for (int j = 0; j < 3; j++) {
                pts[j] = new Point3f();
            }
            triangle3D.getCoordinates(0, pts);
            v1.sub(pts[1], pts[0]);
            v2.sub(pts[2], pts[0]);
            normal.cross(v1, v2);
            normal.normalize();

            Vector3f normTriangle[] = new Vector3f[3];
            for (int j = 0; j < 3; j++) {
                normTriangle[j] = normal;
            }

            //triangle3D.setNormal(0,normal);
            triangle3D.setNormals(0, normTriangle);
            //tentativeù

            addGeometry(triangle3D);

        }

        //Inizializazzione normali
//        for(int i=0;i<n_vertex_of_ply;i++)
//        {
//            //norm_faces[i]=new Vector3f((float)v[0],(float)v[1],(float)v[2]);
//        }
        //at this point we are able to set the bounding vector in senso antiorario.
//        for(int j=0;j<n_vertex_of_ply;j++)
//        {
//            vertsTriangle[j] = new Point3d(vertex3d[j].x,vertex3d[j].y,vertex3d[j].z);
//            normTriangle[j]=norm_faces[j];
//
//        }
    }

    public QuadArray getQuads() {
        return quad;
    }

    public void setCreaseAngle(double angle) {
        mioNG.setCreaseAngle(angle);
        mioNG.generateNormals(mioGI);
    }

    public double getCreaseAngle() {
        return mioNG.getCreaseAngle();
    }

    /**
     * Turn data plot on/off
     */
    public void setNewColor(Color3f color1, Color3f color2, Color3f color3, Color3f color4, float Shininess, float Transparency, PolygonAttributes pb) {

        material.setAmbientColor(color1);
        material.setEmissiveColor(color2);
        material.setDiffuseColor(color3);
        material.setSpecularColor(color4);
        float testshines = material.getShininess();
        material.setShininess(Shininess);
        testshines = material.getShininess();
        ta.setTransparency(Transparency);

    }

    public void setPolygonAttributes(boolean BackFaceNormalFlip, int CullFace, int PolygonMode) {
        pa.setBackFaceNormalFlip(BackFaceNormalFlip);
        pa.setCullFace(CullFace);
        pa.setPolygonMode(PolygonMode);

    }

    public void setGraphOn(boolean b) {
        //If off, change all its colors so that they match background color. This wiil allow for
        //nice wire frame appearance.   
        if (!b) {
            Color3f bc = new Color3f(0f, 0f, 0f);
            material.setAmbientColor(bc);
            material.setEmissiveColor(bc);
            material.setDiffuseColor(bc);
            material.setSpecularColor(bc);
        } else {
            material.setAmbientColor(shapeColor);
            material.setEmissiveColor(black);
            material.setDiffuseColor(white1);
            material.setSpecularColor(white2);
        }
    }
}
