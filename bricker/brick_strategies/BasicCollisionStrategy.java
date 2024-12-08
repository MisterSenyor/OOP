package bricker.brick_strategies;
import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.*;
import danogl.collisions.Layer;

public class BasicCollisionStrategy implements CollisionStrategy {
    BrickerGameManager brickerGameManager;

    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    public void onCollision(GameObject thisObj, GameObject otherObj) {
        System.out.println("this " + thisObj.getTopLeftCorner() + " other " + otherObj.getTopLeftCorner());
        brickerGameManager.deleteStaticObject(thisObj);
    }
}
