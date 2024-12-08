package bricker.gameobjects;
import danogl.*;
import danogl.collisions.*;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class Ball extends GameObject {
    static final Vector2 DEFAULT_SIZE = new Vector2(50, 50);
    static final float BALL_SPEED = 400f;
    private Sound collisionSound;
    int collisionCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = 0;
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

    public Ball(Vector2 topLeftCorner, Renderable renderable, Sound collisionSound) {
        this(topLeftCorner, DEFAULT_SIZE, renderable, collisionSound);
    }

        @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionCounter++;
    }

    public int getCollisionCounter() {
        return collisionCounter;
    }
}
