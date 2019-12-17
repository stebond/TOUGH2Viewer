package Tough2ViewerPRJ.resources;
import javax.vecmath.*;
import javax.media.j3d.*;

//Draws the axis & lines for translucent bounding box
public class Well3D extends Shape3D {
    
    LineArray line1;
    public Well3D(Point3f[] myPoint,Color3f[] SegmentColor,float LineThickness){
        LineAttributes la1 = new LineAttributes();
        la1.setLineWidth(LineThickness);
        Appearance app = new Appearance();
        app.setLineAttributes(la1);
        int n_color=myPoint.length-1;
        ColoringAttributes ca[] = new ColoringAttributes[n_color];
        for(int i=0;i<n_color;i++){
           ca[0] = new ColoringAttributes(SegmentColor[i],ColoringAttributes.FASTEST); 
        }
        
        app.setColoringAttributes(ca[0]);
        setAppearance(app);
        //CORPO;ci vogliono 6 punti per ogni linea
        int dimension=6*(myPoint.length-1);
        line1 = new LineArray(dimension,LineArray.COORDINATES);
        line1.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
        line1.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_WRITE);
        line1.setCapability(LineArray.ALLOW_COORDINATE_READ);
        line1.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
        Point3f[] lineVerts = new Point3f[(myPoint.length-1)*2];
        for(int i=0;i<myPoint.length-1;i++){
            lineVerts[i*2]=myPoint[i];
            lineVerts[i*2+1]=myPoint[i+1];
        }
        line1.setCoordinates(0,lineVerts);
	setGeometry(line1);
    }
}
