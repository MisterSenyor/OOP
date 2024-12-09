package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class ExtraLifeStrategy implements CollisionStrategyDecorator {
    private CollisionStrategy strategy;
    private BrickerGameManager brickerGameManager;

    public ExtraLifeStrategy(CollisionStrategy strategy, BrickerGameManager brickerGameManager) {
        this.strategy = strategy;
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.addExtraLife(thisObj.getCenter());
        strategy.onCollision(thisObj, otherObj);
    }

    public int getStrategyDepth() {
        return strategy.getStrategyDepth() + 1;
    }
}
