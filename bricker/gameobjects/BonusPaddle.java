package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * the paddle added by a special strategy in the bricker game
 * @author Tomer Zilberman
 * @see Paddle the base model
 */
public class BonusPaddle extends Paddle {
    //hit counter to see when to delete
    private int hitLeft;
    //the number of hits it can take
    private static final int DEFAULT_HIT_LEFT = 4;
    // an instance of game manager, used for deletion and so on.
    private BrickerGameManager brickerGameManager;

    /**
     * Construct a new BonusPaddle instance.
     *
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener listener for the input from user
     * @param windowDimensions the dimensions of the game window
     * @param brickerGameManager the bricker game manager where we want to add the paddle
     */
    public BonusPaddle(Renderable renderable, UserInputListener inputListener, Vector2 windowDimensions, BrickerGameManager brickerGameManager) {
        super(Vector2.ZERO, renderable, inputListener, windowDimensions);
        this.brickerGameManager = brickerGameManager;
        this.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
        this.hitLeft = DEFAULT_HIT_LEFT;
    }

    /**
     * what to do in a collision, it counts it and deletes itself after 4 hits
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (--hitLeft <= 0)
        {
            this.brickerGameManager.deleteBonusPaddle();
        }
    }
}
