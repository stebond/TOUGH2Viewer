package Tough2ViewerPRJ;

import java.awt.Font;
import javax.vecmath.*;
import javax.media.j3d.*;


//Draws the axis & lines for translucent bounding box
public class Scale3D extends Group {

    Shape3D[] shapeBox;
    TransformGroup BoxTrans[];
    Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
    Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
    Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
    Color3f[] colors = {red, green, blue};
    Text3D txtMin;
    Text3D txtMax;
    Shape3D txtShape3DMin;
    Shape3D txtShape3DMax;
    Color3f[] colore2;
    int nbox;

    public Scale3D(Point3f center, float lenght, Color3f[] colore) {
        float Xcenter = center.x;
        float Ycenter = center.y;
        float Zcenter = center.z;
        float H_cone = 0.06f;
        float R_cone = 0.02f;
        String fontName = "Times";
        Point3f pointZmin = new Point3f(center.x, center.y, center.z - lenght / 2);
        Point3f pointZmax = new Point3f(center.x, center.y, center.z + lenght / 2);
        Float centramentocarattere = 0.025f;
        nbox = colore.length;
        colore2 = colore;
        int numvertex = 4;
        double[][] vertices = new double[numvertex][2];
        vertices[0][0] = .1f;
        vertices[0][1] = .1f;
        vertices[1][0] = -.1f;
        vertices[1][1] = .1f;
        vertices[2][0] = -.1f;
        vertices[2][1] = -.1f;
        vertices[3][0] = .1f;
        vertices[3][1] = -.1f;

        float dimz = lenght / nbox;
        shapeBox = new VoronoiBox[nbox];
        BoxTrans = new TransformGroup[nbox];
        for (int i = 0; i < nbox; i = i + 1) {
            Transform3D BoxMat = new Transform3D();
            BoxTrans[i] = new TransformGroup(BoxMat);
            BoxMat.set(new Vector3d(center.x, center.y, center.z + lenght / 2 - i * lenght / nbox));
            BoxTrans[i].setTransform(BoxMat);
            BoxTrans[i].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);// qui da errore
            BoxTrans[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            shapeBox[i] = new VoronoiBox(numvertex, vertices, dimz);
            shapeBox[i].setCapability(shapeBox[i].ALLOW_APPEARANCE_READ);
            shapeBox[i].setCapability(shapeBox[i].ALLOW_APPEARANCE_WRITE);
            ID id = new ID(i);
            shapeBox[i].setUserData(id);
            //Appearance appMyBoxMin = shapeBox[num_block].getAppearance();
            Appearance appMyBox = new Appearance();
            ColoringAttributes ca = new ColoringAttributes();
            if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
                // Globally used colors
                Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
                Color3f objColor = colore[nbox - i - 1];
                if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
                    objColor = colore[i];
                }
                Material matMybox = new Material(objColor, black, objColor, white, 100.f);
                matMybox.setLightingEnable(true);
                appMyBox.setMaterial(matMybox);
                shapeBox[i].setAppearance(appMyBox);
            } else {
                appMyBox = shapeBox[i].getAppearance();
                ca.setColor(colore[i]);
                appMyBox.setColoringAttributes(ca);
            }
            appMyBox.setCapability(appMyBox.ALLOW_COLORING_ATTRIBUTES_WRITE);
            BoxTrans[i].addChild(shapeBox[i]);
            addChild(BoxTrans[i]);
        }
        //min

        Transform3D transformTxtMin = new Transform3D();
        transformTxtMin.setTranslation(new Vector3f(center.x + 0.1f, center.y, lenght / 2));
        Font3D f3d = new Font3D(new Font(fontName, Font.PLAIN, 1),
                new FontExtrusion());
        txtMin = new Text3D(f3d, "min",
                new Point3f(.0f, .0f, .0f));
        txtMin.setCapability(Text3D.ALLOW_STRING_WRITE);
        txtMin.setCapability(Text3D.ALLOW_ALIGNMENT_WRITE);

        txtMin.setAlignment(1);
        transformTxtMin.setScale(.1f);
        TransformGroup txtTransGrpMin = new TransformGroup(transformTxtMin);
        txtShape3DMin = new Shape3D();

        txtShape3DMin.setGeometry(txtMin);
        txtShape3DMin.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        txtShape3DMin.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        Appearance appMyBoxMin = new Appearance();
        ColoringAttributes caMin = new ColoringAttributes();
        txtShape3DMin.setAppearance(new Appearance());
        if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
            // Globally used colors
            Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
            Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
            Color3f objColor = colore[colore.length - 1];
            if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
                objColor = colore[0];
            }
            Material matMybox = new Material(objColor, black, objColor, white, 100.f);
            matMybox.setLightingEnable(true);
            appMyBoxMin.setMaterial(matMybox);
            //shapeBox[i].setAppearance(appMyBoxMin);
            txtShape3DMin.setAppearance(appMyBoxMin);
        } else {
            appMyBoxMin = txtShape3DMin.getAppearance();
            caMin.setColor(colore[0]);
            appMyBoxMin.setColoringAttributes(caMin);
        }
        txtTransGrpMin.addChild(txtShape3DMin);
        addChild(txtTransGrpMin);
        ///max
        Transform3D transformTxtMax = new Transform3D();
        transformTxtMax.setTranslation(new Vector3f(center.x + 0.1f, center.y, -lenght / 2 - .1f));
        txtMax = new Text3D(f3d, "max",
                new Point3f(.0f, .0f, .0f));
        txtMax.setCapability(Text3D.ALLOW_STRING_WRITE);
        txtMax.setCapability(Text3D.ALLOW_ALIGNMENT_WRITE);
        txtMax.setAlignment(1);
        transformTxtMax.setScale(.1f);
        TransformGroup txtTransGrpMax = new TransformGroup(transformTxtMax);
        txtShape3DMax = new Shape3D();
        txtShape3DMax.setGeometry(txtMax);
        txtShape3DMax.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        txtShape3DMax.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        Appearance appMyBoxMax = new Appearance();
        ColoringAttributes caMax = new ColoringAttributes();
        txtShape3DMax.setAppearance(new Appearance());
        if (Tough2Viewer.dataobj.get_useMoreRealisticColor()) {
            // Globally used colors
            Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
            Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
            Color3f objColor = colore[0];

            if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
                objColor = colore[colore.length - 1];
            }
            Material matMybox = new Material(objColor, black, objColor, white, 100.f);
            matMybox.setLightingEnable(true);
            appMyBoxMax.setMaterial(matMybox);

            txtShape3DMax.setAppearance(appMyBoxMax);
        } else {
            appMyBoxMax = txtShape3DMin.getAppearance();
            caMax.setColor(colore[nbox - 1]);
            appMyBoxMax.setColoringAttributes(caMax);
        }
        txtTransGrpMax.addChild(txtShape3DMax);
        addChild(txtTransGrpMax);

    }

    Shape3D[] getShapes() {
        return shapeBox;
    }

    void set_text(String min, String max) {
        if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
            txtMin.setString(min);
            txtMax.setString(max);
        } else {
            txtMin.setString(max);
            txtMax.setString(min);
        }
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f objColor = colore2[0];
        if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
            objColor = colore2[colore2.length - 1];
        }
        Material matMybox = new Material(objColor, black, objColor, white, 100.f);
        matMybox.setLightingEnable(true);
        Appearance appMyBoxMax = new Appearance();
        appMyBoxMax.setMaterial(matMybox);
        txtShape3DMax.setAppearance(appMyBoxMax);

        Color3f objColor2 = colore2[colore2.length - 1];
        if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
            objColor2 = colore2[0];
        }
        Material matMybox2 = new Material(objColor2, black, objColor2, white, 100.f);
        matMybox2.setLightingEnable(true);
        Appearance appMyBoxMax2 = new Appearance();
        appMyBoxMax2.setMaterial(matMybox2);
        txtShape3DMin.setAppearance(appMyBoxMax2);
        update_blocks_colors();
    }

    void update_blocks_colors() {

        for (int i = 0; i < nbox; i++) {
            Appearance appMyBox = new Appearance();
            ColoringAttributes ca = new ColoringAttributes();
            Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
            Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
            Color3f objColor = colore2[nbox - i - 1];
            if (Tough2Viewer.dataobj.scale_color_orientation == 1) {
                objColor = colore2[i];
            }
            Material matMybox = new Material(objColor, black, objColor, white, 100.f);
            matMybox.setLightingEnable(true);
            appMyBox.setMaterial(matMybox);
            shapeBox[i].setAppearance(appMyBox);
        }
    }

}
