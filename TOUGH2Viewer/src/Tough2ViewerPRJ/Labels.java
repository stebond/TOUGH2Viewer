package Tough2ViewerPRJ;

import java.text.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;

//Draws the labels for the graph
public class Labels {

    ArrayList labels;
    Appearance labelAppearance;
    double zmax, ymax, xmax;
    String zLabel, xLabel, yLabel, title;
    private static Color3f axisColor;

    public Labels(Grids3DBox axis, float xmin, float xmax, float ymin, float ymax, float zmin, float zmax, int nx, int ny, int nz) {

        xLabel = "X Label";
        zLabel = "Z Label";
        yLabel = "Y Label";
        title = "My Labels";
        axisColor = axis.getAxisColor();
    }

    //Make labels for axis
    public ArrayList makeAxisLabels() {
        Shape3D yLabels[] = new Shape3D[5];

        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        labels = new ArrayList();

        String fontName = "Times";
        labelAppearance = new Appearance();
        ColoringAttributes ca = new ColoringAttributes(axisColor, ColoringAttributes.FASTEST);
        labelAppearance.setColoringAttributes(ca);
        for (int i = 0; i < 5; i++) {
            yLabels[i] = new Shape3D();
            yLabels[i].setAppearance(labelAppearance);
        }
        Shape3D yAxis = new Shape3D();
        Shape3D xAxis = new Shape3D();
        Shape3D titleLabel = new Shape3D();

        titleLabel.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        titleLabel.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

        //y labels
        for (int i = 0; i < 5; i++) {
            Font3D f3d = new Font3D(new Font(fontName, Font.PLAIN, 1),
                    new FontExtrusion());
//         float x=(xmin)/0.05f;

//	Text3D txt = new Text3D(f3d, String.valueOf( nf.format(ymax) ), 
//	                     new Point3f( (xmin)/.05f, (ymin+i*dy)/.05f, 0f));
//	yLabels[i].setGeometry(txt);   
        }

        //y axis label
//	Text3D txtLabel = new Text3D(f3d, yLabel, new Point3f( -1.2f/.05f, .5f/.05f, 0f));
//	yAxis.setGeometry(txtLabel);
//	    
//	    
//	//x labels
//	Text3D txt2 = new Text3D(f3d, "0.0", new Point3f( 0f, -.1f/.05f, 1.05f/.05f));
//	yLabels[2].setGeometry(txt2);
//	    
//	Text3D txt3 = new Text3D(f3d, String.valueOf( nf.format(-xmax) ), 
//	                     new Point3f( -.6f/.05f, -.1f/.05f, 1.05f/.05f));
//	yLabels[3].setGeometry(txt3);
//	    
//	Text3D txt4 = new Text3D(f3d, String.valueOf( nf.format(xmax) ), 
//	                     new Point3f(.4f/.05f, -.1f/.05f, 1.05f/.05f));
//	yLabels[4].setGeometry(txt4);
        Font3D f3d1 = new Font3D(new Font(fontName, Font.PLAIN, 2),
                new FontExtrusion());
        Text3D txt5 = new Text3D(f3d1, title,
                new Point3f(-.1f / .05f, 1.2f / .05f, 0f));
        titleLabel.setGeometry(txt5);

        //x axis label
////////	Text3D txtLabel1 = new Text3D(f3d, xLabel, 
////////	                     new Point3f( -.05f/.05f, -.3f/.05f, 1.05f/.05f));
//	xAxis.setGeometry(txtLabel1);
        for (int i = 0; i < 5; i++) {
            yLabels[i].setAppearance(labelAppearance);
            labels.add(yLabels[i]);
        }
        titleLabel.setAppearance(labelAppearance);
        yAxis.setAppearance(labelAppearance);
        xAxis.setAppearance(labelAppearance);

        labels.add(titleLabel);
        labels.add(yAxis);
        labels.add(xAxis);

        return labels;
    }

    //Make the z axis labels. These need to be rotated for proper display
    public TransformGroup makeLabelsZ() {
        String fontName = "Times";

        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        TransformGroup trans = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.rotY(-1.507f);
        trans.setTransform(t3d);

        Shape3D zLabels[] = new Shape3D[4];
        for (int i = 0; i < 4; i++) {
            zLabels[i] = new Shape3D();
            zLabels[i].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
            zLabels[i].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            zLabels[i].setAppearance(labelAppearance);
        }

        Font3D f3d = new Font3D(new Font(fontName, Font.PLAIN, 1),
                new FontExtrusion());

        //z labels
        Text3D txt1 = new Text3D(f3d, "0.0", new Point3f(.45f / .05f, -.05f / .05f, .6f / .05f));
        zLabels[0].setGeometry(txt1);

        Text3D txt2 = new Text3D(f3d, String.valueOf(nf.format(-zmax)),
                new Point3f(-.1f / .05f, -.05f / .05f, .6f / .05f));
        zLabels[1].setGeometry(txt2);

        Text3D txt3 = new Text3D(f3d, String.valueOf(nf.format(zmax)),
                new Point3f(.9f / .05f, -.05f / .05f, .6f / .05f));
        zLabels[2].setGeometry(txt3);

        //axis label
        Text3D txt4 = new Text3D(f3d, zLabel,
                new Point3f(.35f / .05f, -.3f / .05f, .6f / .05f));
        zLabels[3].setGeometry(txt4);

        for (int i = 0; i < 4; i++) {
            trans.addChild(zLabels[i]);
        }

        return trans;

    }

}
