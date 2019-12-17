/*
 * $RCSfile: Box.java,v $
 *
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 * $Revision: 1.2 $
 * $Date: 2007/02/09 17:21:33 $
 * $State: Exp $
 */
package Tough2ViewerPRJ;

import javax.media.j3d.*;
import javax.vecmath.*;

public class VoronoiBox extends Shape3D {

    QuadArray box;
    TriangleFanArray myboxUP;
    TriangleFanArray myboxDOWN;
    Point3d vertsLateral[];
    Point3d vertsTriangleUP[];
    Point3d vertsTriangleDOWN[];
    int num_vertex;

    public VoronoiBox(int numvertex, double[][] vertices, double zsize) {
        //super();
        num_vertex = numvertex;
        double zmin = -zsize / 2.0;
        double zmax = zsize / 2.0;
        int dimensionArray = numvertex * 4;
        box = new QuadArray(dimensionArray, QuadArray.COORDINATES | QuadArray.NORMALS);
        myboxUP = new TriangleFanArray(numvertex, GeometryArray.COORDINATES | QuadArray.NORMALS, new int[]{numvertex});
        myboxDOWN = new TriangleFanArray(numvertex, GeometryArray.COORDINATES | QuadArray.NORMALS, new int[]{numvertex});
        vertsLateral = new Point3d[numvertex * 4];
        vertsTriangleUP = new Point3d[numvertex];
        vertsTriangleDOWN = new Point3d[numvertex];
        Vector3f normTriangleUP[] = new Vector3f[numvertex];
        Vector3f normTriangleDOWN[] = new Vector3f[numvertex];
        Vector3f normLateral[] = new Vector3f[numvertex * 4];

        //   lateral face
        for (int i = 0; i < numvertex - 1; i++) {
            vertsLateral[0 + i * 4] = new Point3d(vertices[i][0], vertices[i][1], zmax);
            vertsLateral[1 + i * 4] = new Point3d(vertices[i][0], vertices[i][1], zmin);
            vertsLateral[2 + i * 4] = new Point3d(vertices[i + 1][0], vertices[i + 1][1], zmin);
            vertsLateral[3 + i * 4] = new Point3d(vertices[i + 1][0], vertices[i + 1][1], zmax);
            //ora calcolo le normali
            Vector3f v1 = new Vector3f();
            v1.setX((float) (vertices[i + 1][0] - vertices[i][0]));
            v1.setY((float) (vertices[i + 1][1] - vertices[i][1]));
            v1.setZ(0.0f);
            Vector3f v2 = new Vector3f();
            v2.setX(0.0f);
            v2.setY(0.0f);
            v2.setZ((float) (zmax - zmin));
            Vector3f normal = new Vector3f();
            normal.cross(v2, v1);
            normal.normalize();
            for (int i1 = 0; i1 < 4; i1++) {
                normLateral[i1 + i * 4] = new Vector3f(Math.abs(normal.x), Math.abs(normal.y), Math.abs(normal.z));

            }

        }
        //ultima faccia
        vertsLateral[4 * numvertex - 4] = new Point3d(vertices[numvertex - 1][0], vertices[numvertex - 1][1], zmax);
        vertsLateral[4 * numvertex - 3] = new Point3d(vertices[numvertex - 1][0], vertices[numvertex - 1][1], zmin);
        vertsLateral[4 * numvertex - 2] = new Point3d(vertices[0][0], vertices[0][1], zmin);
        vertsLateral[4 * numvertex - 1] = new Point3d(vertices[0][0], vertices[0][1], zmax);
        ///Ultima normale
        Vector3f v1 = new Vector3f();
        v1.setX((float) (vertices[numvertex - 1][0] - vertices[0][0]));
        v1.setY((float) (vertices[numvertex - 1][1] - vertices[0][1]));
        v1.setZ(0.0f);
        Vector3f v2 = new Vector3f();
        v2.setX(0.0f);
        v2.setY(0.0f);
        v2.setZ((float) (zmax - zmin));
        Vector3f normal = new Vector3f();
        normal.cross(v1, v2);
        normal.normalize();
        for (int i1 = 0; i1 < 4; i1++) {
            //normLateral[4*numvertex-i1-1]=new Vector3f(Math.abs(normal.x), Math.abs(normal.y),Math.abs( normal.z));
            normLateral[4 * numvertex - i1 - 1] = new Vector3f(normal.x, normal.y, normal.z);
        }

        //box.setCoordinates(0, vertsLateral);questo era l'originale
        //ora ritrasformo tutto in un unico vettore fi coordinate xyz da cacciar dentro
        box.setCoordinates(0, vertsLateral);
        box.setNormals(0, normLateral);
        box.setCapability(QuadArray.ALLOW_COORDINATE_READ);
        box.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
        setGeometry(box);
        // da qui con i triangoli...
//        upperface
        for (int i = 0; i < numvertex; i++) {
            vertsTriangleUP[i] = new Point3d(vertices[i][0], vertices[i][1], zmax);
            normTriangleUP[i] = new Vector3f(0.0f, 0.0f, 1.0f);
        }
        //DOWNface
        for (int i = 0; i < numvertex; i++) {
            vertsTriangleDOWN[numvertex - i - 1] = new Point3d(vertices[i][0], vertices[i][1], zmin);
            //normTriangleDOWN[numvertex-i-1]  =   new Vector3f(0.0f,0.0f,1.0f);
            normTriangleDOWN[numvertex - i - 1] = new Vector3f(0.0f, 0.0f, -1.0f);//14-06-03
        }
        myboxUP.setCoordinates(0, vertsTriangleUP);
        myboxDOWN.setCoordinates(0, vertsTriangleDOWN);
        myboxUP.setCapability(QuadArray.ALLOW_COORDINATE_READ);
        myboxUP.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
        myboxDOWN.setCapability(QuadArray.ALLOW_COORDINATE_READ);
        myboxDOWN.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
        myboxUP.setNormals(0, normTriangleUP);
        myboxDOWN.setNormals(0, normTriangleDOWN);

        addGeometry(myboxUP);
        addGeometry(myboxDOWN);

        setAppearance(new Appearance());

    }

    /**
     * try to modify lenght
     */
    public void ExpandZ(double Zfactor) {

        //vecchio
//        Point3d[] vertsLateral=new Point3d[num_vertex*4];
//        Point3d[] mypoint=new Point3d[4];
//
//         for(int i=0;i<num_vertex*4;i++){
//             Point3d mypoint2=new Point3d();
//             box.getCoordinate(i, mypoint2);
//              mypoint2.z=mypoint2.z*Zfactor;
//              vertsLateral[i]=mypoint2;
//
//        }
        ///nuovo
        for (int i = 0; i < vertsLateral.length; i++) {
            vertsLateral[i].z = vertsLateral[i].z * Zfactor;
        }
        for (int i = 0; i < vertsTriangleUP.length; i++) {
            vertsTriangleUP[i].z = vertsTriangleUP[i].z * Zfactor;
        }
        for (int i = 0; i < vertsTriangleDOWN.length; i++) {
            vertsTriangleDOWN[i].z = vertsTriangleDOWN[i].z * Zfactor;
        }
        box.setCoordinates(0, vertsLateral);
        myboxUP.setCoordinates(0, vertsTriangleUP);
        myboxDOWN.setCoordinates(0, vertsTriangleDOWN);
        /* questo non va
        box.getCoordinates(0, vertsLateral);
        for(int i=0;i<num_vertex*4;i++){
            vertsLateral[i].z=vertsLateral[i].z*Zfactor;
        }
         */
        //box.setCoordinates(0,vertsLateral);
        //this.setGeometry(box);
//        float[]		m_OriginalCoordinateArray;
//        GeometryArray m_GeometryArray = (GeometryArray) this.getGeometry();
//            m_GeometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
//            m_GeometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
//            m_GeometryArray.setCapability(GeometryArray.ALLOW_COUNT_READ);
//         m_OriginalCoordinateArray = new float[3 * m_GeometryArray.getVertexCount()];
//        m_GeometryArray.getCoordinates(0, m_OriginalCoordinateArray);

    }

    public void Expand(double factor) {

        for (int i = 0; i < vertsLateral.length; i++) {
            vertsLateral[i].x = vertsLateral[i].x * factor;
            vertsLateral[i].y = vertsLateral[i].y * factor;
            vertsLateral[i].z = vertsLateral[i].z * factor;
        }
        for (int i = 0; i < vertsTriangleUP.length; i++) {
            vertsTriangleUP[i].x = vertsTriangleUP[i].x * factor;
            vertsTriangleUP[i].y = vertsTriangleUP[i].y * factor;
            vertsTriangleUP[i].z = vertsTriangleUP[i].z * factor;
        }
        for (int i = 0; i < vertsTriangleDOWN.length; i++) {
            vertsTriangleDOWN[i].x = vertsTriangleDOWN[i].x * factor;
            vertsTriangleDOWN[i].y = vertsTriangleDOWN[i].y * factor;
            vertsTriangleDOWN[i].z = vertsTriangleDOWN[i].z * factor;
        }
        box.setCoordinates(0, vertsLateral);
        myboxUP.setCoordinates(0, vertsTriangleUP);
        myboxDOWN.setCoordinates(0, vertsTriangleDOWN);

    }
}
