package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;

public class PucksStrategy implements CollisionStrategyDecorator {
    private CollisionStrategy strategy;
    private BrickerGameManager brickerGameManager;
    public PucksStrategy(CollisionStrategy strategy, BrickerGameManager brickerGameManager) {
        this.strategy = strategy;
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.addPucks(2, thisObj.getCenter());
        strategy.onCollision(thisObj, otherObj);
    }

    public int getStrategyDepth() {
        return strategy.getStrategyDepth() + 1;
    }
}

