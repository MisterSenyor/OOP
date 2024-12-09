package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BonusPaddle extends Paddle {
    private int hitLeft;
    private static final int DEFAULT_HIT_LEFT = 4;
    private BrickerGameManager brickerGameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener
     * @param windowDimensions
     */
    public BonusPaddle(Renderable renderable, UserInputListener inputListener, Vector2 windowDimensions, BrickerGameManager brickerGameManager) {
        super(Vector2.ZERO, renderable, inputListener, windowDimensions);
        this.brickerGameManager = brickerGameManager;
        this.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
        this.hitLeft = DEFAULT_HIT_LEFT;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (--hitLeft <= 0)
        {
            this.brickerGameManager.deleteBonusPaddle();
        }
    }
}
