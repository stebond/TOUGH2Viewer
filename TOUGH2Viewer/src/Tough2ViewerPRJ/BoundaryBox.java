package Tough2ViewerPRJ;

import javax.vecmath.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.Box;

//Draw the translucent bounding box for graph
public class BoundaryBox extends BranchGroup {

    private static Color3f boxColor = new Color3f(.8f, .8f, .8f);

    public BoundaryBox(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax) {
        Shape3D bottom = new Shape3D();
        Shape3D back = new Shape3D();
        Shape3D side = new Shape3D();

        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes(boxColor, ColoringAttributes.FASTEST);
        app.setColoringAttributes(ca);

        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, .5f);
        app.setTransparencyAttributes(ta);

        Box bottomBox = new Box((xmax - xmin) / 2, (ymax - ymin) / 2, .001f, app);
        Box backBox = new Box((xmax - xmin) / 2, .001f, (zmax - zmin) / 2, app);
        Box sideBox = new Box(.001f, (ymax - ymin) / 2, (zmax - zmin) / 2, app);

        //translate to be back of bounding box
        BranchGroup backBG = new BranchGroup();
        TransformGroup backTG = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0f, ymin, 0f));
        backTG.setTransform(t3d);
        backBG.addChild(backTG);
        backTG.addChild(backBox);

        //translate to be side of bounding box
        BranchGroup sideBG = new BranchGroup();
        TransformGroup sideTG = new TransformGroup();
        Transform3D t3ds = new Transform3D();
        t3ds.setTranslation(new Vector3f(xmin, 0, 0));
        sideTG.setTransform(t3ds);
        sideBG.addChild(sideTG);
        sideTG.addChild(sideBox);

        //translate to be botton of bounding box 
        BranchGroup bottomBG = new BranchGroup();
        TransformGroup bottomTG = new TransformGroup();
        Transform3D t3db = new Transform3D();
        t3db.setTranslation(new Vector3f(0f, 0f, zmin));
        bottomTG.setTransform(t3db);
        bottomBG.addChild(bottomTG);
        bottomTG.addChild(bottomBox);

        addChild(backBG);
        addChild(sideBG);
        addChild(bottomBG);
    }

}
