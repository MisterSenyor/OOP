package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * the special ball that falls from a brick. extends from ball.
 * @author Tomer Zilbermam
 * @see Ball the regulat ball
 */
public class Puck extends Ball{

    // the size factor from the original ball
    private static final float SIZE_FACTOR = 0.75f;

    /**
     * Construct a new Puck instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound  The sound the puck makes while colliding with another object
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        Random random = new Random();
        //creates it in a random angle facing up
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * BALL_SPEED;
        float velocityY = (float) Math.sin(angle) * BALL_SPEED;
        this.setVelocity(new Vector2(velocityX, velocityY));
    }

    /**
     * Construct a new Puck instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound  The sound the puck makes while colliding with another object
     */
    public Puck(Vector2 topLeftCorner, Renderable renderable, Sound collisionSound) {
        this(topLeftCorner, DEFAULT_SIZE.mult(SIZE_FACTOR), renderable, collisionSound);
    }
}
