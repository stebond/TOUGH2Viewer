/*
 * CylinderTransformer.java
 *
 * Created on 25 July 2003, 11:04
 */
package Tough2ViewerPRJ;

import javax.vecmath.*;

/**
 * A class to calculate the length and transformations necessary to produce a
 * cylinder to connect two points. Useful for Java3D and VRML where a cylinder
 * object is created aligned along the y-axis.
 *
 * @author Alastair Hill
 */
public class CylinderTransformer {

    /**
     * point A
     */
    private final Point3f pointA;
    /**
     * point B
     */
    private final Point3f pointB;
    /**
     * the angle through which to rotate the cylinder
     */
    private float angle;
    /**
     * the axis around which to rotate the cylinder
     */
    private Vector3f axis;
    /**
     * The translation required to translate the cylinder to the midpoint of the
     * two points
     */
    private Vector3f translation;
    /**
     * The length of the cylinder required to join the two points
     */
    private float length;

    /**
     * Creates a new instance of CylinderTransformer
     *
     * @param a point a
     * @param b point b
     */
    public CylinderTransformer(Point3f a, Point3f b) {
        pointA = a;
        pointB = b;

        //carry out the calculations
        doCalculations();
    }

    /**
     * Carries out the necessary calculations so that values may be returned
     */
    private void doCalculations() {
        length = pointA.distance(pointB);

        float[] arrayA = new float[3];
        pointA.get(arrayA);
        float[] arrayB = new float[3];
        pointB.get(arrayB);
        float[] arrayMid = new float[3];

        for (int i = 0; i < arrayA.length; i++) {
            arrayMid[i] = (arrayA[i] + arrayB[i]) / 2f;
        }

        //the translation needed is
        translation = new Vector3f(arrayMid);

        //the initial orientation of the bond is in the y axis
        Vector3f init = new Vector3f(0.0f, 1.0f, 0.0f);

        //the vector needed is the same as that from a to b
        Vector3f needed = new Vector3f(pointB.x - pointA.x, pointB.y - pointA.y, pointB.z - pointA.z);

        //so the angle to rotate the bond by is:
        angle = needed.angle(init);

        //and the axis to rotate by is the cross product of the initial and
        //needed vectors - ie the vector orthogonal to them both
        axis = new Vector3f();
        axis.cross(init, needed);
    }

    /**
     * Returns the angle (in radians) through which to rotate the cylinder
     *
     * @return the angle.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * The axis around which the cylinder must be rotated
     *
     * @return the axis
     */
    public javax.vecmath.Vector3f getAxis() {
        return axis;
    }

    /**
     * The length required for the cylinder to join the two points
     *
     * @return the length
     */
    public float getLength() {
        return length;
    }

    /**
     * The translation required to move the cylinder to the midpoint of the two
     * points
     *
     * @return the translation
     */
    public javax.vecmath.Vector3f getTranslation() {
        return translation;
    }

    /**
     * Generates a (pretty) string representation of the the CylinderTransformer
     *
     * @return the string representation
     */
    public String toString() {
        return "tr: " + translation + ", ax: " + axis + ", an: " + angle + ", le: " + length;
    }

    /**
     * Generates the required axis and angle combined into an AxisAngle4f object
     *
     * @return the axis and angle
     */
    public javax.vecmath.AxisAngle4f getAxisAngle() {
        return new AxisAngle4f(axis.x, axis.y, axis.z, angle);
    }
}
