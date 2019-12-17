/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

/**
 *
 * @author stebond
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

class ScaleCanvas extends Canvas {

    int width, height;
    int scale_number;

    ScaleCanvas(int scale) {
        //setSize(width = w, height = h);
        //rows = r;
        //cols = c;
        scale_number = scale;
    }

    public void paint(Graphics g) {

        width = getSize().width;
        height = getSize().height;
        int number_of_labels = 11;
        g.setColor(new Color(1.0f, 1.0f, 1.0f));
        g.fillRect((int) 0, (int) 0, (int) width, (int) height);
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        g.drawRect((int) 0, (int) 0, (int) width - 1, (int) height - 1);

        Color colore[] = Tough2Viewer.dataobj.getColorScale();

        int variableToPlot;

        double VarMin;
        double VarMax;
        double rangeVariable;

        String[] Variable_unit;
        String[] Variable_name;
        int it = Tough2Viewer.dataobj.get_actualTimeToPlot();
        if (it < 0) {
            it = 0;
        }
        if (scale_number == 0) {
            variableToPlot = Tough2Viewer.dataobj.get_actualVariableToPlot();
            if (variableToPlot < 0) {
                variableToPlot = 0;
            }
            Variable_unit = Tough2Viewer.dataobj.getVariablesUM();
            Variable_name = Tough2Viewer.dataobj.getVariableName();
            VarMin = Tough2Viewer.dataobj.get_GlobalScale(variableToPlot, 0);
            VarMax = Tough2Viewer.dataobj.get_GlobalScale(variableToPlot, 1);
        } else {
            if (Tough2Viewer.dataobj.FluxFound == false) {
                return;
            }
            variableToPlot = Tough2Viewer.dataobj.get_actualFluxToPlot();
            if (variableToPlot < 0) {
                variableToPlot = 0;
            }
            Variable_name = Tough2Viewer.dataobj.getFLOWName();
            Variable_unit = Tough2Viewer.dataobj.getFluxUM();
            VarMin = 0;
            VarMax = Tough2Viewer.dataobj.get_VectorModuleStatistics(variableToPlot);
        }
        rangeVariable = VarMax - VarMin;
        if (rangeVariable <= 0) {
            rangeVariable = 1.0f;
        }
        int HLeggenda = 240;
        int hrectcolor = Math.round((float) HLeggenda / ((float) colore.length));
        HLeggenda = hrectcolor * colore.length;
        for (int ic = 0; ic < colore.length; ic = ic + 1) {
            g.setColor(colore[colore.length - 1 - ic]);
            if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
                g.setColor(colore[ic]);
            }

            g.fillRect((int) 50, (int) 50 + ic * hrectcolor, (int) 50, (int) hrectcolor);
        }
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        Font myfont = Tough2Viewer.dataobj.ScaleFont;

        g.setFont(myfont);
        g.drawString(Variable_unit[variableToPlot], 116, (int) 35);
        g.drawString(Variable_name[variableToPlot], 25, 35);
        int deltaY = (int) ((float) HLeggenda / ((float) number_of_labels - 1));
        double deltaLabel = rangeVariable / ((double) number_of_labels - 1);

//    int actual_width = g.getFontMetrics().stringWidth(words[i]);
//    int x = total_width - actual_width - padding;
        int max_lenght = 0;
        int i_max_lenght = 0;
        String[] myLabel = new String[number_of_labels];
        for (int il = 0; il < number_of_labels; il = il + 1) {
            double label = VarMin + (deltaLabel * il);
            //String mylabel=Double.toString(label);
            myLabel[il] = String.format(Tough2Viewer.dataobj.FormatDouble, label);
            if (g.getFontMetrics().stringWidth(myLabel[il]) > max_lenght) {
                i_max_lenght = il;
                max_lenght = g.getFontMetrics().stringWidth(myLabel[il]);
            }

        }

        for (int il = 0; il < number_of_labels; il = il + 1) {
            double label = VarMin + (deltaLabel * il);
            //String mylabel=Double.toString(label);
            String mylabel = String.format(Tough2Viewer.dataobj.FormatDouble, label);
            int actual_width = g.getFontMetrics().stringWidth(myLabel[il]);

            int position = number_of_labels - 1 - il;
            if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
                position = il;
            }
            g.drawString(mylabel, 116 + max_lenght - actual_width, (int) 50 + position * deltaY + 8);
        }
    }

}

/**
 * This is the demo class.
 */
