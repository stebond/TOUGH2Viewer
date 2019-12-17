/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnBehaviorPost;

/**
 *
 * @author stefano.bondua
 */
public class RotateBehaviorZ extends Behavior {

    private TransformGroup transformGroup;
    private Transform3D trans = new Transform3D();
    private WakeupCriterion criterion;
    private float angleZ = 0.0f;
    private final int ROTATE = 1;
    private double stepZ = 5.0f;
    // create a new RotateBehavior

    RotateBehaviorZ(TransformGroup tg) {
        transformGroup = tg;
    }

    // initialize behavior to wakeup on a behavior post with id = ROTATE
    public void initialize() {
        criterion = new WakeupOnBehaviorPost(this, ROTATE);
        wakeupOn(criterion);
    }

    // processStimulus to rotate the cube
    public void processStimulus(Enumeration criteria) {
        angleZ += Math.toRadians(stepZ);
        trans.rotZ(angleZ);
        transformGroup.setTransform(trans);
        wakeupOn(criterion);
    }

    // when the mouse is clicked, postId for the behavior
    void rotate() {
        postId(ROTATE);
    }

    void reset() {
        angleZ = -(float) Math.toRadians(stepZ);
        postId(ROTATE);
    }

    void setStepAngle(double value) {
        stepZ = value;
    }
}
