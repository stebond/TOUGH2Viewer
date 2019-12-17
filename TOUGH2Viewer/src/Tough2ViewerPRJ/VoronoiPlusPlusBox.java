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

import java.util.ArrayList;
import javax.media.j3d.*;
import javax.vecmath.*;

public class VoronoiPlusPlusBox extends Shape3D {

    int num_vertex;

    public VoronoiPlusPlusBox(ArrayList BoxData, double contrazione, double riduzione, double xminV, double yminV, double zminV, double xminR, double yminR, double zminR, Color3f mycolor, int translation_mode, double z_factor) {
        //super();
        //xminV,yminV,zminV:coordinate of the virtual universe
        //xminR,yminR,zminR:coordinate of the real space
        int id_label = (int) BoxData.get(0);
        double[] centerD = (double[]) BoxData.get(1);//POS=1
        Point3d center = new Point3d((centerD[0] - xminR) * riduzione, (centerD[1] - yminR) * riduzione, (centerD[2] - zminR) * riduzione * z_factor);
        num_vertex = (Integer) BoxData.get(2);
        Point3d[] vertex = new Point3d[num_vertex];
        for (int i = 0; i < num_vertex; i++) {
            double[] v = (double[]) BoxData.get(3 + i);
            vertex[i] = new Point3d(v[0], v[1], v[2] * z_factor);
        }
        int number_of_faces = (Integer) BoxData.get(3 + num_vertex);
        Vector3f[] norm_faces = new Vector3f[number_of_faces];
        for (int i = 0; i < number_of_faces; i++) {
            double[] v = (double[]) BoxData.get(4 + num_vertex + number_of_faces + i);
            //Gli cambio di segno perche' altrimenti vedo il dentro della scatola..
            //2. ma non e' questo mannaggia. Soluzione:era l'ordinamento dei vertici.
//            for(int k=0;k<3;k++)
//            {
//                v[k]=-v[k];
//            }
            norm_faces[i] = new Vector3f((float) v[0], (float) v[1], (float) v[2]);
        }

        for (int i = 0; i < number_of_faces; i++) {
            int[] indexs = (int[]) BoxData.get(4 + num_vertex + i);
            //ora so quanti vertici ha questa faccia:
            int n_vertex_of_face = indexs.length;
            Point3d[] vertex3d = new Point3d[n_vertex_of_face];
            for (int j = 0; j < n_vertex_of_face; j++) {
                vertex3d[j] = new Point3d(vertex[indexs[j]].x * riduzione * contrazione, vertex[indexs[j]].y * riduzione * contrazione, vertex[indexs[j]].z * riduzione * contrazione);
            }
            //ora devo ordinarli. Faccio cos�: creo un vettore fra il primo e il secondo, fra il primo e il terzo etc
            Vector3d[] V_ij = new Vector3d[n_vertex_of_face - 1];
            for (int j = 0; j < n_vertex_of_face - 1; j++) {
                V_ij[j] = new Vector3d(vertex3d[j + 1].x - vertex3d[0].x, vertex3d[j + 1].y - vertex3d[0].y, vertex3d[j + 1].z - vertex3d[0].z);

            }
            //calculate the angle between vectors
            double[] angle = new double[n_vertex_of_face - 1];
            int[] order = new int[n_vertex_of_face - 1];
            for (int j = 0; j < n_vertex_of_face - 1; j++) {
                angle[j] = V_ij[j].angle(V_ij[0]);
                order[j] = j;
            }
            //crescent order
            //sort_cre(angle,order);
            //decre
            sort_cre(angle, order);//14-03-26 test
            int[] ind_ver_ord = new int[n_vertex_of_face];
            //now, we have do get the first as first.
            //other points are Vindex+1
            ind_ver_ord[0] = 0;
            for (int j = 1; j < n_vertex_of_face; j++) {
                ind_ver_ord[j] = order[j - 1] + 1;
            }

            TriangleFanArray face = new TriangleFanArray(n_vertex_of_face, GeometryArray.COORDINATES | QuadArray.NORMALS, new int[]{n_vertex_of_face});
            Point3d[] vertsTriangle = new Point3d[n_vertex_of_face];
            Vector3f normTriangle[] = new Vector3f[n_vertex_of_face];
            //at this point we are able to set the bounding vector in senso antiorario.
            for (int j = 0; j < n_vertex_of_face; j++) {
                if (translation_mode == 0) {
                    vertsTriangle[j] = new Point3d(vertex3d[ind_ver_ord[j]].x + center.x + xminV, vertex3d[ind_ver_ord[j]].y + center.y + yminV, vertex3d[ind_ver_ord[j]].z + center.z + zminV);
                } else {
                    //vertsTriangle[j] = new Point3d(vertex3d[ind_ver_ord[j]].x+center.x,vertex3d[ind_ver_ord[j]].y+center.y,vertex3d[ind_ver_ord[j]].z+center.z);
                    //vertsTriangle[j] = new Point3d(vertex3d[ind_ver_ord[j]].x+xminV,vertex3d[ind_ver_ord[j]].y+yminV,vertex3d[ind_ver_ord[j]].z+zminV);//buono ma shiftato
                    vertsTriangle[j] = new Point3d(vertex3d[ind_ver_ord[j]].x, vertex3d[ind_ver_ord[j]].y, vertex3d[ind_ver_ord[j]].z);
                }
                normTriangle[j] = norm_faces[i];

            }
            face.setCoordinates(0, vertsTriangle);
            face.setCapability(QuadArray.ALLOW_COORDINATE_READ);
            face.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
            face.setNormals(0, normTriangle);

            addGeometry(face);

        }

        setAppearance(new Appearance());

    }

    void sort_cre(double a[], int[] index) {
        for (int i1 = 0; i1 < a.length; i1++) {
            for (int i2 = i1 + 1; i2 < a.length; i2++) {
                if (a[i2] > a[i1]) {
                    double temp = a[i1];
                    int ind_tmp = index[i1];
                    a[i1] = a[i2];
                    index[i1] = index[i2];
                    a[i2] = temp;
                    index[i2] = ind_tmp;
                }
            }
        }
    }

    void sort_decre(double a[], int[] index) {
        for (int i1 = 0; i1 < a.length; i1++) {
            for (int i2 = i1 + 1; i2 < a.length; i2++) {
                if (a[i2] < a[i1]) {
                    double temp = a[i1];
                    int ind_tmp = index[i1];
                    a[i1] = a[i2];
                    index[i1] = index[i2];
                    a[i2] = temp;
                    index[i2] = ind_tmp;
                }
            }
        }
    }

}
