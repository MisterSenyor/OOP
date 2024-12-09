package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class TurboStrategy implements CollisionStrategyDecorator {
    private CollisionStrategy strategy;
    private BrickerGameManager brickerGameManager;
    public TurboStrategy(CollisionStrategy strategy, BrickerGameManager brickerGameManager) {
        this.strategy = strategy;
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (brickerGameManager.isMainBall(otherObj)) {
            brickerGameManager.enterTurboMode();
        }
        strategy.onCollision(thisObj, otherObj);
    }

    public int getStrategyDepth() {
        return strategy.getStrategyDepth() + 1;
    }
}
