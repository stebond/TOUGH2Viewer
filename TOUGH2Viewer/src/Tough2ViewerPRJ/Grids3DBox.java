package Tough2ViewerPRJ;

import javax.vecmath.*;
import javax.media.j3d.*;

//Draws the axis & lines for translucent bounding box
public class Grids3DBox extends Shape3D {

    private static Color3f axisColor = new Color3f(1f, 1f, 0f);
    private float verts[];
    //improved da stebond
//    private float verts[] = {    -.5f,1f,0f,    -.5f,0f,0f,
//                                 -.5f,0f,1f,     .5f,0f,1f,
//                                 -.55f,.5f,0f,   .5f,.5f,0f,
//                                 -.55f,1f,0f,    .50f,1f,0f,
//                                 -.5f,0f,0f,    -.5f,0f,1.05f,
//                                  0f,0f,0f,      0f,0f,1.05f,
//                                  .5f,0f,1.05f,  .5f,0f,0f,
//                                 -.55f,0f,.5f,   .50f,0f,.5f,
//                                 -.55f,0f,1f,    .50f,0f,1f,
//                                  0f, 0f, 0f,    0f, 1f, 0f,
//                                  0.5f, 0f, 0f,  0.5f, 1f, 0f,
//                                 -.55f, 0f, 0f,  .5f, 0f, 0f,
//                                 .5f, .5f, 0f,   .5f, .5f, 1f,
//                                 .5f, 1f, 0f,    .5f, 1f, 1f,
//                                 .5f, 1f, 1f,    .5f, 0f, 1f,
//                                 .5f, 1f, .5f,   .5f, 0f, .5f };

    public Grids3DBox(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax, int nx, int ny, int nz) {
        int index = 0;

        verts = new float[nx * 3 * 2 * 2 + ny * 3 * 2 * 2 + nz * 3 * 2 * 2];
        float dx = (xmax - xmin) / (nx - 1);
        float dy = (ymax - ymin) / (ny - 1);
        float dz = (zmax - zmin) / (nz - 1);

        for (int ix = 0; ix < nx; ix++) {

            //bottomface      xy  
            verts[index] = xmin + ix * dx;
            verts[index + 1] = ymin;
            verts[index + 2] = zmin;
            verts[index + 3] = xmin + ix * dx;
            verts[index + 4] = ymax;
            verts[index + 5] = zmin;
            index = index + 6;
            //lateral face,yz
            verts[index] = xmin + ix * dx;
            verts[index + 1] = ymin;
            verts[index + 2] = zmin;
            verts[index + 3] = xmin + ix * dx;
            verts[index + 4] = ymin;
            verts[index + 5] = zmax;
            index = index + 6;
        }
        for (int iy = 0; iy < ny; iy++) {
            //botom face xy    
            verts[index] = xmin;
            verts[index + 1] = ymin + iy * dy;
            verts[index + 2] = zmin;
            verts[index + 3] = xmax;
            verts[index + 4] = ymin + iy * dy;
            verts[index + 5] = zmin;
            index = index + 6;
            //laterla fce yz
            verts[index] = xmin;
            verts[index + 1] = ymin + iy * dy;
            verts[index + 2] = zmin;
            verts[index + 3] = xmin;
            verts[index + 4] = ymin + iy * dy;
            verts[index + 5] = zmax;
            index = index + 6;

        }

        for (int iz = 0; iz < nz; iz++) {
//  //bottom face xz    
            verts[index] = xmin;
            verts[index + 1] = ymin;
            verts[index + 2] = zmin + iz * dz;
            verts[index + 3] = xmax;
            verts[index + 4] = ymin;
            verts[index + 5] = zmin + iz * dz;
            index = index + 6;
            //lateral face yz
            verts[index] = xmin;
            verts[index + 1] = ymin;
            verts[index + 2] = zmin + iz * dz;
            verts[index + 3] = xmin;
            verts[index + 4] = ymax;
            verts[index + 5] = zmin + iz * dz;
            index = index + 6;
        }
//            
//       

        //Make Grids3DBox
        LineAttributes la1 = new LineAttributes();
        la1.setLineWidth(1.5f);
        Appearance app = new Appearance();
        app.setLineAttributes(la1);

        ColoringAttributes ca = new ColoringAttributes(axisColor, ColoringAttributes.FASTEST);
        app.setColoringAttributes(ca);
        setAppearance(app);
        LineArray lines = new LineArray(verts.length, LineArray.COORDINATES);
        Point3f[] lineVerts = new Point3f[verts.length / 3];

        int cnt = 0;
        for (int i = 0; i < verts.length; i += 3) {
            lineVerts[cnt] = new Point3f(verts[i], verts[i + 1], verts[i + 2]);
            cnt++;
        }

        lines.setCoordinates(0, lineVerts);

        setGeometry(lines);

    }

    /**
     * Return axis color
     */
    public Color3f getAxisColor() {
        return axisColor;
    }

}
