package bricker.gameobjects;
import danogl.*;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * the paddle in a bricker game, a base model for the bonus paddle
 * @author Tomer Zilberman
 */
public class Paddle extends GameObject {
    //constants of the paddle default speed and size
    private static final float MOVEMENT_SPEED = 30f;
    static final Vector2 DEFAULT_SIZE = new Vector2(100, 15);
    // reads the input for moving the paddle around
    private UserInputListener inputListener;
    // the window dimensions so that the paddle won't exit
    private Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener listener for the input from user
     * @param windowDimensions the dimensions of the game window
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener listener for the input from user
     * @param windowDimensions the dimensions of the game window
     */
    public Paddle(Vector2 topLeftCorner, Renderable renderable, UserInputListener inputListener,
                  Vector2 windowDimensions) {
        this(topLeftCorner, DEFAULT_SIZE, renderable, inputListener, windowDimensions);
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
    }

    /**
     * what to do each tick of the game, used for controlling the Paddle
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&  getTopLeftCorner().x() > 0) {
            movementDir = movementDir.add(Vector2.LEFT.mult(MOVEMENT_SPEED));
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && getTopLeftCorner().x() + getDimensions().x()
                < windowDimensions.x()) {
            movementDir = movementDir.add(Vector2.RIGHT.mult(MOVEMENT_SPEED));
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
