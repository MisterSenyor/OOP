package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class AnotherPaddleStrategy extends BasicCollisionStrategy{
    public AnotherPaddleStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.createBonusPaddle();
    }
}
