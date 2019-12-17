package Tough2ViewerPRJ;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;
import javax.vecmath.*;
import javax.media.j3d.*;

//Computes the plot shape
public class Surface_Raster_Shape3D extends Shape3D {

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

    public Surface_Raster_Shape3D(GraphData graphData, float scala, float xmin, float ymin, float zmin, double Zfactor) {

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
        //                       (0,1,1)(0,0,0)(0.7,0.7,0.7)(0.8,0.8,0.8),60
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

        int ny = graphData.getRows();
        int nx = graphData.getCols();
        double data[][] = graphData.getData();
        float dx = (float) ((graphData.get_X_Max() - graphData.get_X_Min()) / (float) (nx - 1)) * scala;
        float dy = (float) ((graphData.get_Y_Max() - graphData.get_Y_Min()) / (float) (ny - 1)) * scala;
        float z_min = (float) graphData.get_Z_Min();
        //Make shape quads from data points
        int numVerts = (ny - 1) * (nx - 1) * 4;
        quad = new QuadArray(numVerts, QuadArray.COORDINATES | QuadArray.NORMALS);
        Point3f[] verts = new Point3f[numVerts];
        int i, j;

        int count = 0;
        float Xi, Yj, Z;

        int ii = 0;
        int jj = 0;
        for (j = 0; j < ny - 1; j++) {
            for (i = 0; i < nx - 1; i++) {

                ii = i + 1;
                jj = j + 1;
//            if( ii >= nx) ii = i;
//            if( jj >= ny) jj = j;
                Xi = xmin + (float) i * dx;
                Yj = ymin + (float) j * dy;
                Z = zmin + (float) (data[j][i] - z_min) * scala * (float) Zfactor;
                verts[count] = new Point3f(Xi, Yj, Z);
                count++;

                Xi = xmin + (float) ii * dx;
                Yj = ymin + (float) j * dy;
                Z = zmin + (float) (data[j][ii] - z_min) * scala * (float) Zfactor;

                verts[count] = new Point3f(Xi, Yj, Z);
                count++;

                Xi = xmin + (float) ii * dx;
                Yj = ymin + (float) jj * dy;
                Z = zmin + (float) (data[jj][ii] - z_min) * scala * (float) Zfactor;
                verts[count] = new Point3f(Xi, Yj, Z);
                count++;

                Xi = xmin + (float) i * dx;
                Yj = ymin + (float) jj * dy;
                Z = zmin + (float) (data[jj][i] - z_min) * scala * (float) Zfactor;
                verts[count] = new Point3f(Xi, Yj, Z);
                count++;
            }
        }

        mioGI = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        mioGI.setCoordinates(verts);
        //mioGI.setStripCounts(stripCount);
        mioNG = new NormalGenerator();

        mioNG.setCreaseAngle(2f);
        mioNG.generateNormals(mioGI);
        mioGI.recomputeIndices();
        Stripifier mioSF = new Stripifier();
        mioSF.stripify(mioGI);
        mioGI.recomputeIndices();
        setGeometry(mioGI.getGeometryArray());

//da qui c'e' la roba vecchia buona
//        quad.setCoordinates(0, verts);
//        Point3f [] pts = new Point3f[4];
//	Vector3f normal = new Vector3f();
//	Vector3f v1 = new Vector3f();
//	Vector3f v2 = new Vector3f();
//
//	//compute normals for lighting
//	for (i = 0; i < 4; i++) pts[i] = new Point3f();
//	int numG = ny*ny;
// 	for (int face = 0; face<numG ; face++) {
//	        quad.getCoordinates(face*4, pts);
//	        v1.sub(pts[1], pts[0]);
//	        v2.sub(pts[2], pts[0]);
//	        normal.cross(v1, v2);
//	        normal.normalize();
//	        for (i = 0; i < 4; i++) quad.setNormal((face * 4 + i), normal);
//	}
//
//
//
//
//	setGeometry(quad);
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
