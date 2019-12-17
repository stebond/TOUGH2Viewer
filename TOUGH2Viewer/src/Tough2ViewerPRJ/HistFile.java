/*
 * Copyright 2009 - Areeda Associates Ltd.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Areeda Associates designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package Tough2ViewerPRJ;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HistFile is a class representing a 2D array of z-values stored in a comma
 * separated text (CSV) file. The format of the file is: each line contains a
 * row of numeric values separated by commas all lines much have the same number
 * of values there are no limits (other than memory available) on the number of
 * values in a row or the number of rows
 *
 * @author areeda
 */
public class HistFile extends HistData {

    File in;
    Double[][] hist;

    /**
     * Construct the object spcifying the file to read
     *
     * @param f File object to read (CSV format)
     */
    public HistFile(File f) {
        in = f;
        this.readCSV();
    }

    private void readCSV() {
        FileReader fr = null;
        try {
            ArrayList<Double[]> inHist = new ArrayList<Double[]>();
            fr = new FileReader(in);
            String line = "";
            int c;
            Integer lineCnt = 0;
            while ((c = fr.read()) != -1) {
                char ch = (char) c;
                if (ch == '\n') {
                    String[] fld = JoeStringUtils1.parseCSV(line);
                    line = "";
                    lineCnt++;
                    if (fld.length > 0) {
                        ArrayList<Double> vals = new ArrayList<Double>();
                        for (String s : fld) {
                            Double v = Double.parseDouble(s);
                            vals.add(v);
                        }
                        Double[] row = new Double[vals.size()];
                        vals.toArray(row);
                        inHist.add(row);
                    }
                } else {
                    line += (char) c;
                }
            }
            // we need to rotate this, currently each row is one air index
            // and one column is a fuel flow,
            int nc = inHist.get(0).length;
            int nr = inHist.size();
            this.hist = new Double[nc][nr];

            for (int rowN = 0; rowN < nr; rowN++) {
                Double[] row = inHist.get(rowN);
                if (row.length != nc) {
                    throw (new Exception("Rows not constant length"));
                }
                for (int col = 0; col < nc; col++) {
                    hist[col][rowN] = row[col];
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public double[][] getHist() {
        int ny = this.hist.length;
        int nx = this.hist[0].length;
        double[][] ret = new double[ny][nx];
        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                double z = hist[y][x];
                ret[y][x] = z;
            }
        }
        return ret;
    }

    @Override
    public Point2D.Float getLowLeft() {
        return new Point2D.Float(0.f, 0.f);
    }

    @Override
    public Point2D.Float getUpRight() {
        return new Point2D.Float(100.f, 28.1f);
    }

}
