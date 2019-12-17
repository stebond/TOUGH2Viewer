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
public class RotateBehaviorY extends Behavior {

    private TransformGroup transformGroup;
    private Transform3D trans = new Transform3D();
    private WakeupCriterion criterion;
    private float angleY = 0.0f;
    private final int ROTATE = 1;

    // create a new RotateBehavior
    RotateBehaviorY(TransformGroup tg) {
        transformGroup = tg;
    }

    // initialize behavior to wakeup on a behavior post with id = ROTATE
    public void initialize() {
        criterion = new WakeupOnBehaviorPost(this, ROTATE);
        wakeupOn(criterion);
    }

    // processStimulus to rotate the cube
    public void processStimulus(Enumeration criteria) {
        angleY += Math.toRadians(10.0);
        trans.rotY(angleY);
        transformGroup.setTransform(trans);
        wakeupOn(criterion);
    }

    // when the mouse is clicked, postId for the behavior
    void rotate() {
        postId(ROTATE);
    }
}
