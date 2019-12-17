/*
 * $RCSfile: PickHighlightBehaviorRegular.java,v $
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
 * $Date: 2007/02/09 17:21:48 $
 * $State: Exp $
 */
package Tough2ViewerPRJ;

import javax.media.j3d.*;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import java.util.*;
import java.awt.*;
import java.awt.Event;
import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import javax.vecmath.*;

public class PickHighlightBehaviorVoronoi extends PickMouseBehavior {

    Appearance savedAppearance = null;
    Shape3D oldShape = null;
    Appearance highlightAppearance;
    SceneGraphPath sceneGraphPath[];
    BranchGroup mybranchgroup;
    PickShape mypickshape;

    public PickHighlightBehaviorVoronoi(Canvas3D canvas, BranchGroup root, Bounds bounds) {
        super(canvas, root, bounds);
        this.setSchedulingBounds(bounds);
        root.addChild(this);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f highlightColor = new Color3f(0.5f, 0.5f, 0.5f);
        Material highlightMaterial = new Material(highlightColor, black,
                highlightColor, white,
                80.0f);
        highlightAppearance = new Appearance();
        highlightAppearance.setMaterial(new Material(highlightColor, black,
                highlightColor, white,
                80.0f));
        pickCanvas.setMode(PickTool.GEOMETRY);
        mybranchgroup = root;
    }

    public void updateScene(int xpos, int ypos) {
        PickResult pickResult = null;
        Shape3D shape = null;

        pickCanvas.setShapeLocation(xpos, ypos);
        pickResult = pickCanvas.pickClosest();

        //forse ci siamo?????
        sceneGraphPath = mybranchgroup.pickAllSorted(mypickshape);
        if (sceneGraphPath != null) {
            for (int j = 0; j < sceneGraphPath.length; j++) {
                if (sceneGraphPath[j] != null) {
                    Node node = sceneGraphPath[j].getObject();
                    if (node instanceof Shape3D) {
                        try {
                            ID posID = (ID) node.getUserData();
                            if (posID != null) {
                                int pos = posID.get();
                                if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() < 4) {
                                    Tough2Viewer.tough2viewerGUI.VoronoiModel3D.shift(pos);
                                }
                                if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 4) {
                                    Tough2Viewer.tough2viewerGUI.VoronoiModel3D.get2D(pos);
                                }

//                                    positions.set(pos, Positions.HUMAN);
//                                    canvas2D.repaint();
//qui faccio il caso di rendere trasparenti i cubi con la z superiore al cubo cliccato...
                                break;
                            }
                        } catch (CapabilityNotSetException e) {
                            // Catch all CapabilityNotSet exceptions and
                            // throw them away, prevents renderer from
                            // locking up when encountering "non-selectable"
                            // objects.
                        }
                    }
                }
            }
        }

        if (pickResult != null) {
            Node node = pickResult.getObject();
//            shape = (Shape3D) pickResult.getNode(PickResult.SHAPE3D);
            if (node instanceof Shape3D) {
                try {
                    ID posID = (ID) node.getUserData();
                    if (posID != null) {
                        int pos = posID.get();
                        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() < 3) {
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.shift(pos);
                        }
                        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 4) {
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.get2D(pos);
                        }
                        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 13) {
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.pick_VOI(pos);
                        }
                        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 17) {
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.update_block_Info(pos);
                        }
                        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 22) {
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.select_neighbord(pos);
                        }
                        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 23) {

                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.add_or_remove_multiple_selection(pos);
                        }
                        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 25) {

                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.add_or_remove_multiple_selection_advanced(pos);
                        }

                    }
                } catch (CapabilityNotSetException e) {
                    // Catch all CapabilityNotSet exceptions and
                    // throw them away, prevents renderer from
                    // locking up when encountering "non-selectable"
                    // objects.
                }
            }

        }
    }

    public void updateScene2(int xpos, int ypos) {
        PickResult pickResult = null;
        Shape3D shape = null;
        pickCanvas.setShapeLocation(xpos, ypos);
        pickResult = pickCanvas.pickClosest();

        //forse ci siamo?????
        sceneGraphPath = mybranchgroup.pickAllSorted(mypickshape);
        if (sceneGraphPath != null) {
            for (int j = 0; j < sceneGraphPath.length; j++) {
                if (sceneGraphPath[j] != null) {
                    Node node = sceneGraphPath[j].getObject();
                    if (node instanceof Shape3D) {
                        try {
                            ID posID = (ID) node.getUserData();
                            if (posID != null) {
                                int pos = posID.get();
                                if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 18) {
                                    Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(pos, true);
                                }
//                                    positions.set(pos, Positions.HUMAN);
//                                    canvas2D.repaint();
//qui faccio il caso di rendere trasparenti i cubi con la z superiore al cubo cliccato...
                                break;
                            }
                        } catch (CapabilityNotSetException e) {
                            // Catch all CapabilityNotSet exceptions and
                            // throw them away, prevents renderer from
                            // locking up when encountering "non-selectable"
                            // objects.
                        }
                    }
                }
            }
        }
        if (pickResult != null) {
            Node node = pickResult.getObject();
//            shape = (Shape3D) pickResult.getNode(PickResult.SHAPE3D);
            if (node instanceof Shape3D) {
                try {
                    ID posID = (ID) node.getUserData();
                    if (posID != null) {
                        int pos = posID.get();
                        if (Tough2Viewer.tough2viewerGUI.VoronoiModel3D.ShiftMode.getSelectedIndex() == 18) {
                            Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(pos, true);
                        }
//                                    positions.set(pos, Positions.HUMAN);
//                                    canvas2D.repaint();
//qui faccio il caso di rendere trasparenti i cubi con la z superiore al cubo cliccato...

                    }
                } catch (CapabilityNotSetException e) {
                    // Catch all CapabilityNotSet exceptions and
                    // throw them away, prevents renderer from
                    // locking up when encountering "non-selectable"
                    // objects.
                }
            }
        }
    }
}
