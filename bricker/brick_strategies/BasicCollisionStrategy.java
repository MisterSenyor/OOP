package bricker.brick_strategies;
import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.*;

public class BasicCollisionStrategy implements CollisionStrategy {
    private BrickerGameManager brickerGameManager;
    private int brickIndex;

    public BasicCollisionStrategy(BrickerGameManager brickerGameManager, int brickIndex) {
        this.brickerGameManager = brickerGameManager;
        this.brickIndex = brickIndex;
    }

    public void onCollision(GameObject thisObj, GameObject otherObj) {
        System.out.println("this " + thisObj.getTopLeftCorner() + " other " + otherObj.getTopLeftCorner());
        brickerGameManager.deleteBrick(brickIndex);

    }
}
