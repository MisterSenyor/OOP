package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class anotherPaddleStrategy extends BasicCollisionStrategy{
    public anotherPaddleStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.createBonusPaddle();
    }
}
