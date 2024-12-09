package bricker.brick_strategies;
import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.*;
import danogl.collisions.Layer;

/**
 * the basic collision strategy, used to just delete the brick
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    // brick manager used to delete the brick
    BrickerGameManager brickerGameManager;

    /**
     * Creates a new BasicCollisionStrategy object
     * @param brickerGameManager the manager of the game we run
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * what to do in a collision. here deletes the brick (thisObj)
     * @param thisObj one of the object in the collision
     * @param otherObj the other object in the collision
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.deleteBrick(thisObj);
    }

    /**
     * returns the strategy depth. in this case it's zero because there are no extra strategies
     * @return 0, the depth of basic strategy
     */
    public int getStrategyDepth() { return 0; }
}
