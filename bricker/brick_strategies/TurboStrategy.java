package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class TurboStrategy extends BasicCollisionStrategy {
    public TurboStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
    }
}
