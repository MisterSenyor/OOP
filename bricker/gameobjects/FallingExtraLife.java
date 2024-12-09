package bricker.gameobjects;
import bricker.main.BrickerGameManager;
import danogl.*;
import danogl.collisions.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class FallingExtraLife extends GameObject {
    public static final Vector2 DEFAULT_SIZE = new Vector2(25, 25);
    static final Vector2 DEFAULT_VELOCITY = Vector2.DOWN.mult(30f);
    private BrickerGameManager brickerGameManager;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public FallingExtraLife(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                            BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.brickerGameManager = brickerGameManager;
        Random random = new Random();
        this.setVelocity(DEFAULT_VELOCITY);
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle && !(other instanceof BonusPaddle);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        brickerGameManager.increaseLives();
        brickerGameManager.deleteObject(this);
    }
}
