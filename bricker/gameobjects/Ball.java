package bricker.gameobjects;
import danogl.*;
import danogl.collisions.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {
    public Ball(Vector2 topLeft, Vector2 dimensions, Renderable renderable) {
        super(topLeft, dimensions, renderable);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
    }
    public int getCollisionCounter() {
        return 0;
    }
}
