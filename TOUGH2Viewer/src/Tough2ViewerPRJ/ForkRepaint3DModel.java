/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.util.concurrent.RecursiveAction;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

/**
 *
 * @author stefano.bondua
 */
public class ForkRepaint3DModel extends RecursiveAction {

    private int mStart;
    private int mLength;
    private VoronoiBlockModel3D mVBM3D;

    public ForkRepaint3DModel(int start, int length, VoronoiBlockModel3D VBM3D) {

        mStart = start;
        mLength = length;
        mVBM3D = VBM3D;
    }
    protected static int sThreshold = 10000;

    protected void computeDirectly() {
        int timestep = Tough2Viewer.dataobj.get_actualTimeToPlot();
        int variablename = Tough2Viewer.dataobj.get_actualVariableToPlot();
        int nxyz = Tough2Viewer.dataobj.get_nxyz();
        boolean logaritmic_scale = Tough2Viewer.dataobj.get_logaritmicscale();
        float variableMinMax[];
        variableMinMax = new float[4];
        if (logaritmic_scale == false) {
            variableMinMax[0] = Tough2Viewer.dataobj.get_GlobalScale(variablename, 0);
            variableMinMax[1] = Tough2Viewer.dataobj.get_GlobalScale(variablename, 1);
        } else {
            variableMinMax[0] = (float) Math.log10(Tough2Viewer.dataobj.get_GlobalScale(variablename, 0));
            variableMinMax[1] = (float) Math.log10(Tough2Viewer.dataobj.get_GlobalScale(variablename, 1));
        }
        String variable_name = Tough2Viewer.dataobj.get_variables_name(variablename);
        mVBM3D.myscale1.set_text(variable_name + " " + Float.toString(variableMinMax[0]), variable_name + " " + Float.toString(variableMinMax[1]));
        float rangeVariable;

        rangeVariable = variableMinMax[1] - variableMinMax[0];
        if (rangeVariable <= 0) {
            rangeVariable = 1.0f;
        }
        if (mVBM3D.appMaterialColor.getSelectedIndex() == 0) {
            mVBM3D.colore = Tough2Viewer.dataobj.getColo3fscale();
        } else {
            mVBM3D.colore = Tough2Viewer.dataobj.getColo3fRocksTypes();
        }
        double ROI_xmin = Tough2Viewer.dataobj.get_ROI_xmin();
        double ROI_ymin = Tough2Viewer.dataobj.get_ROI_ymin();
        double ROI_zmin = Tough2Viewer.dataobj.get_ROI_zmin();
        double ROI_xmax = Tough2Viewer.dataobj.get_ROI_xmax();
        double ROI_ymax = Tough2Viewer.dataobj.get_ROI_ymax();
        double ROI_zmax = Tough2Viewer.dataobj.get_ROI_zmax();
        double HideROI_xmin = Tough2Viewer.dataobj.get_HideROI_xmin();
        double HideROI_ymin = Tough2Viewer.dataobj.get_HideROI_ymin();
        double HideROI_zmin = Tough2Viewer.dataobj.get_HideROI_zmin();
        double HideROI_xmax = Tough2Viewer.dataobj.get_HideROI_xmax();
        double HideROI_ymax = Tough2Viewer.dataobj.get_HideROI_ymax();
        double HideROI_zmax = Tough2Viewer.dataobj.get_HideROI_zmax();
        float scalecolor = (float) (mVBM3D.colore.length - 1.0f);
//        if(Tough2Viewer.dataobj.ID_grid_type==2){
//       int t2vdat=Tough2Viewer.dataobj.VoroPPData.size();
//       nxyz=Math.min(t2vdat,nxyz);
//   }
        for (int i_b = mStart; i_b < mStart + mLength; i_b++) {
            //index1: n_block
            //index2: timestep
            //index3: variable
            boolean visible_i_b = true;
            int color_index = 0;
            Color3f finalColor = new Color3f();
            boolean isOutOfRange = false;
            if (mVBM3D.appMaterialColor.getSelectedIndex() == 0) {
                if (Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename) < variableMinMax[0]) {
                    finalColor = new Color3f(.1f, .1f, .1f);
                    isOutOfRange = true;
                }
                if (Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename) > variableMinMax[1]) {
                    finalColor = new Color3f(.9f, .9f, .9f);
                    isOutOfRange = true;
                }
                double val1;
                if (logaritmic_scale == false) {
                    val1 = Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename);

                } else {
                    val1 = Math.log10(Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename));
                }
                if (val1 <= variableMinMax[1] && val1 >= variableMinMax[0]) {
                    color_index = (int) ((val1 - variableMinMax[0]) / (rangeVariable) * scalecolor);

                    //color_index=(int)((Tough2Viewer.dataobj.get_dataArray(i_b, timestep, variablename)-variableMinMax[0])/(rangeVariable)*scalecolor);
                    finalColor = new Color3f(mVBM3D.colore[color_index].x, mVBM3D.colore[color_index].y, mVBM3D.colore[color_index].z);
                }
                visible_i_b = Tough2Viewer.dataobj.is_rocktype_visible(Tough2Viewer.dataobj.get_RockType(i_b));
            } else {
                color_index = Tough2Viewer.dataobj.get_RockType(i_b);
                finalColor = new Color3f(mVBM3D.colore[color_index].x, mVBM3D.colore[color_index].y, mVBM3D.colore[color_index].z);
                visible_i_b = Tough2Viewer.dataobj.is_rocktype_visible(color_index);
            }
            Appearance appMyBox = new Appearance();
            ColoringAttributes ca = new ColoringAttributes();
            if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
                // Globally used colors
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
                //Color3f objColor=colore[color_index];
                Material matMybox = new Material(finalColor, black, finalColor, white, 100.f);
                matMybox.setLightingEnable(true);
                appMyBox.setMaterial(matMybox);
//            //only for test
//          TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.FASTEST,0.30f);
//            ta.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
//            ta.setCapability(TransparencyAttributes.ALLOW_BLEND_FUNCTION_WRITE);
//            ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
//            appMyBox.setTransparencyAttributes(ta);
//            //end only for test
                if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                    mVBM3D.shapeBox[i_b].setAppearance(appMyBox);

                } else {
                    mVBM3D.myVorPlusPlusBox[i_b].setAppearance(appMyBox);
                }

            } else {
                appMyBox = mVBM3D.shapeBox[i_b].getAppearance();
                ca.setColor(finalColor);
                appMyBox.setColoringAttributes(ca);
            }
            double Xo = Tough2Viewer.dataobj.get_Xo(i_b);
            double Yo = Tough2Viewer.dataobj.get_Yo(i_b);
            double Zo = Tough2Viewer.dataobj.get_Zo(i_b);
            boolean isInROI = false;
            boolean isInHideROI = false;
            if (Xo >= ROI_xmin & Xo <= ROI_xmax) {
                if (Yo >= ROI_ymin & Yo <= ROI_ymax) {
                    if (Zo >= ROI_zmin & Zo <= ROI_zmax) {
                        isInROI = true;
                    }
                }
            }
            if (isInROI) {
                if (visible_i_b) {
                    mVBM3D.posMaskBox.set(i_b);
                } else {
                    mVBM3D.posMaskBox.clear(i_b);
                }
            } else {
                mVBM3D.posMaskBox.clear(i_b);
            }
            if (Tough2Viewer.dataobj.get_useHideROI()) {
                if (Xo >= HideROI_xmin & Xo <= HideROI_xmax) {
                    if (Yo >= HideROI_ymin & Yo <= HideROI_ymax) {
                        if (Zo >= HideROI_zmin & Zo <= HideROI_zmax) {
                            isInHideROI = true;
                        }
                    }
                }
                if (isInHideROI) {
                    mVBM3D.posMaskBox.clear(i_b);
                }
            }
            if (Tough2Viewer.dataobj.get_hideoutofrange()) {
                if (isOutOfRange) {
                    mVBM3D.posMaskBox.clear(i_b);
                }
            }
            if (Tough2Viewer.dataobj.Block_is_selected[i_b]) {
                Tough2Viewer.tough2viewerGUI.VoronoiModel3D.highLightBlock(i_b, false);
            }
        }
        mVBM3D.posSwitchBox.setChildMask(mVBM3D.posMaskBox);
        int sel = Tough2Viewer.dataobj.get_selectedIndex();
        if (sel >= 0) {
            mVBM3D.update_block_Info(sel);
            Appearance appMyBox = new Appearance();
            ColoringAttributes ca = new ColoringAttributes();
            if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
                // Globally used colors
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

                Material matMybox = new Material(white, black, white, white, 100.f);
                matMybox.setLightingEnable(true);
                appMyBox.setMaterial(matMybox);
                if (Tough2Viewer.dataobj.ID_grid_type < 2) {
                    mVBM3D.shapeBox[sel].setAppearance(appMyBox);
                } else {
                    mVBM3D.myVorPlusPlusBox[sel].setAppearance(appMyBox);
                }
                //shapeBox[sel].setAppearance(appMyBox);
            } else {
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                appMyBox = mVBM3D.shapeBox[sel].getAppearance();
                ca.setColor(black);
                appMyBox.setColoringAttributes(ca);
            }
        }

//        
//        
//        
//        
//        
//        
//        
//        
//        
//        for (int index = mStart; index < mStart + mLength; index++)
//        {
//        
//        }
//
//        
    }

    @Override
    protected void compute() {
        if (mLength < sThreshold) {
            computeDirectly();
            return;
        }

        int split = mLength / 2;

        invokeAll(new ForkRepaint3DModel(mStart, split, mVBM3D),
                new ForkRepaint3DModel(mStart + split, mLength - split,
                        mVBM3D));
    }

}
