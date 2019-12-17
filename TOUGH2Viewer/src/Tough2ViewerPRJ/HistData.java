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

/**
 * Abstract class for different forms of histogram data Here we care how to get
 * the data out of the class, subclasses care how to get the data into the
 * class.
 *
 * @author areeda
 */
public abstract class HistData {

    /**
     * Acess data array
     *
     * @return 2D array of z-values to plot
     */
    abstract double[][] getHist();

    /**
     * Get real world coordinates of lower left (minimum x,y)
     *
     * @return the external (real) coordinates of the lower left (0,0) point of
     * the histogram array (min X, min Y)
     */
    abstract Point2D.Float getLowLeft();

    /**
     * Get real world coordinates of upper left (maximum x,y)
     *
     * @return the external (real) coordinates of the upper right point of the
     * histogram array (max X, max Y)
     */
    abstract Point2D.Float getUpRight();
}
