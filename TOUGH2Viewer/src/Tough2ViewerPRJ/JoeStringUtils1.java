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

import java.util.ArrayList;

/**
 * Somewhat random assortment of static string functions
 *
 * @author areeda
 */
public class JoeStringUtils1 {

    /**
     * Parse a comma seprated single line into the individual fields
     *
     * @param in - comma separated single line
     * @return String array of the fields in input string
     *
     */
    public static String[] parseSpace(String in) {   // @todo should strip quotes and ignore commas within quotes

        String[] ret = null;
        String t;
        ArrayList<String> flds = new ArrayList<String>();
        int pos = 0, epos;
        boolean notDone = true;

        String s = in;                 // temporary
        String SPACE = " ";
        t = s.substring(pos);
        int i = 0;
        while (i < in.length()) {
            epos = s.indexOf(SPACE, i);
            if (epos > i) {
                t = s.substring(i, epos + 1);
                t = t.trim();
                char c0 = t.charAt(0), c1 = t.charAt(t.length() - 1);
                if ((c0 == '"' && c1 == '"') || (c0 == '\'' && c1 == '\'')) {
                    t = t.substring(1, t.length() - 1);
                }
                flds.add(t);
                i = epos;
            } else if (epos < 0) {
                t = s.substring(i);
                t = t.trim();
                char c0 = t.charAt(0), c1 = t.charAt(t.length() - 1);
                if ((c0 == '"' && c1 == '"') || (c0 == '\'' && c1 == '\'')) {
                    t = t.substring(1, t.length() - 1);
                }
                flds.add(t);
                break;
            } else {
                i++;
            }

        }
        if (flds.size() > 0) {
            ret = new String[flds.size()];
            ret = flds.toArray(ret);
        }

        return ret;
    }

    public static String[] parseString(String in, String delimiter) {   // @todo should strip quotes and ignore commas within quotes

        String[] ret = null;
        String t;
        ArrayList<String> flds = new ArrayList<String>();
        int pos = 0, epos;
        boolean notDone = true;

        String s = in;                 // temporary

        t = s.substring(pos);
        int i = 0;
        while (i < in.length()) {
            epos = s.indexOf(delimiter, i);
            if (epos > i) {
                t = s.substring(i, epos + 1);
                t = t.trim();
                char c0 = t.charAt(0), c1 = t.charAt(t.length() - 1);
                if ((c0 == '"' && c1 == '"') || (c0 == '\'' && c1 == '\'')) {
                    t = t.substring(1, t.length() - 1);
                }
                flds.add(t);
                i = epos;
            } else if (epos < 0) {
                t = s.substring(i);
                t = t.trim();
                char c0 = t.charAt(0), c1 = t.charAt(t.length() - 1);
                if ((c0 == '"' && c1 == '"') || (c0 == '\'' && c1 == '\'')) {
                    t = t.substring(1, t.length() - 1);
                }
                flds.add(t);
                break;
            } else {
                i++;
            }

        }
        if (flds.size() > 0) {
            ret = new String[flds.size()];
            ret = flds.toArray(ret);
        }
        for (int j = 0; j < flds.size(); j++) {
            if (ret[j].endsWith(delimiter)) {
                ret[j] = ret[j].substring(0, ret[j].length() - 1);
            }
        }
        return ret;
    }

    public static String[] parseCSV(String in) {   // @todo should strip quotes and ignore commas within quotes
        String[] ret = null;
        String t;
        ArrayList<String> flds = new ArrayList<String>();
        int pos = 0, epos;
        boolean notDone = true;
        int i;
        String s = in;                 // temporary

        for (i = 0; notDone; i++) {
            epos = s.indexOf(',', pos);
            if (epos > 0) {
                t = s.substring(pos, epos);
            } else {
                t = s.substring(pos);
                notDone = false;
            }
            t = t.trim();
            char c0 = t.charAt(0), c1 = t.charAt(t.length() - 1);
            if ((c0 == '"' && c1 == '"') || (c0 == '\'' && c1 == '\'')) {
                t = t.substring(1, t.length() - 1);
            }
            pos = epos + 1;
            flds.add(t);
        }
        if (flds.size() > 0) {
            ret = new String[flds.size()];
            ret = flds.toArray(ret);
        }

        return ret;
    }

    public static int[] subDivision(String in) {
        String[] elementi = parseSpace(in);
        int n_eleme = elementi.length;
        int[] n = new int[n_eleme + 1];
        n[0] = 0;
        for (int i = 1; i < n_eleme; i++) {
            n[i] = in.indexOf(elementi[i - 1], n[i - 1] + 1) + elementi[i - 1].length();
        }
        n[n_eleme] = in.length();
        return n;
    }

    public static String[] subDivision2Lines(String in1, String toDivide) {
        String[] elementi = parseSpace(in1);

        int n_eleme = elementi.length;
        int[] n = new int[n_eleme + 1];
        n[0] = in1.indexOf(elementi[0]);
        for (int i = 1; i < n_eleme; i++) {
            n[i] = in1.indexOf(elementi[i], n[i - 1] + 1);
        }
        n[n_eleme] = n[n_eleme - 1] + elementi[n_eleme - 1].length();
        String[] returnString = new String[n_eleme];
        //cerco a sinistra per 5 caratteri
        for (int i = 0; i < n_eleme; i++) {
            boolean foundSinistra = false;
            boolean foundDestra = false;
            int startpos = -1;
            int endpos = -1;
            for (int j = -2; j < 5; j++) {

                int temppos = n[i] - j;
                if (temppos > 0) {
                    if (toDivide.regionMatches(temppos, "(", 0, 1)) {
                        startpos = temppos;
                        foundSinistra = true;
                    }

                }
            }
            if (foundSinistra) {
                endpos = toDivide.indexOf(")", startpos);
                returnString[i] = toDivide.substring(startpos + 1, endpos);
            } else {
                returnString[i] = "number";
            }

        }

        return returnString;
    }
}
