package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public interface CollisionStrategy {
    public void onCollision(GameObject thisObj, GameObject otherObj);
    public int getStrategyDepth();
}
