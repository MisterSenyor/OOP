package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class ExtraLifeStrategy extends BasicCollisionStrategy {
    public ExtraLifeStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.addExtraLife(thisObj.getCenter());
    }
}
