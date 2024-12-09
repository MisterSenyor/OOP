package bricker.gameobjects;
import bricker.main.BrickerGameManager;
import danogl.*;
import danogl.collisions.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * the falling heart that one of the strategies generates.
 * @author Ido Minha
 */
public class FallingExtraLife extends GameObject {
    // the default size and velocity of the object
    public static final Vector2 DEFAULT_SIZE = new Vector2(25, 25);
    static final Vector2 DEFAULT_VELOCITY = Vector2.DOWN.mult(30f);
    // the game manager used to delete the object from game ones it was touched
    private BrickerGameManager brickerGameManager;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param brickerGameManager the game manager of the game we run
     */
    public FallingExtraLife(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                            BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.brickerGameManager = brickerGameManager;
        this.setVelocity(DEFAULT_VELOCITY);
    }

    /**
     * decides whether the heart should collide and disappear with the object it touched
     * @param other The other GameObject.
     * @return whether the heart should collide and disappear with the object it touched
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        // needs to be only the original Paddle
        return other instanceof Paddle && !(other instanceof BonusPaddle);
    }

    /**
     * what to do in a collision. we check if we collided with the original Paddle and if so we disappear and
     * increase the life.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        brickerGameManager.increaseLives();
        brickerGameManager.deleteObject(this);
    }
}
