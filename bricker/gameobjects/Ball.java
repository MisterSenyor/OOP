package bricker.gameobjects;
import danogl.*;
import danogl.collisions.*;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * class that represents the ball in the bricker game
 * @author Tomer Zilberman
 *
 */
public class Ball extends GameObject {
    //constants of the ball default size, speed and turbo speed factor
    static final Vector2 DEFAULT_SIZE = new Vector2(50, 50);
    static final float BALL_SPEED = 300f;
    private final float TURBO_MULTIPLIER = 1.4f;
    // the sound the ball makes while in collision
    private Sound collisionSound;
    // counts the number of collision in normal and turbo mode
    private int collisionCounter, turboModeCollisions;
    // keeps track on turbo and normal mode
    private boolean isInTurboMode = false;
    // the image of the ball
    private Renderable renderable;


    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound  The sound the ball makes while colliding with another object
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = 0;
        this.renderable = renderable;
        //the ball is generated at one of 4 45 degrees angles
        float ballVelX = BALL_SPEED, ballVelY = BALL_SPEED;
        Random random = new Random();
        if (random.nextBoolean()) {
            ballVelY = -ballVelY;
        }
        if (random.nextBoolean()) {
            ballVelX = -ballVelX;
        }
        this.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound  The sound the ball makes while colliding with another object
     */
    public Ball(Vector2 topLeftCorner, Renderable renderable, Sound collisionSound) {
        this(topLeftCorner, DEFAULT_SIZE, renderable, collisionSound);
    }

    /**
     * what to do in collision
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionCounter++;
        if (isInTurboMode) {
            if (--turboModeCollisions == 0) {
                setNormalMode();
            }
        }
    }

    /**
     * gets the collision counter
     * @return the collision counter
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

    /**
     * makes the ball into turbo mode
     * @param renderable the image the ball transform into while in turbo mode
     */
    public void setTurboMode(Renderable renderable) {
        renderer().setRenderable(renderable);
        setVelocity(getVelocity().mult(TURBO_MULTIPLIER));
        turboModeCollisions = 6;
        isInTurboMode = true;
    }

    /**
     * turns the ball back into normal mode
     */
    public void setNormalMode() {
        renderer().setRenderable(renderable);
        setVelocity(getVelocity().mult(1 / TURBO_MULTIPLIER));
        isInTurboMode = false;
    }

}
