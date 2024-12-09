package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * strategy that makes the ball move faster.
 * @author Ido Minha
 */
public class TurboStrategy implements CollisionStrategyDecorator {
    // the other strategies we use
    private CollisionStrategy strategy;
    // the manager of the game, used to create or delete objects
    private BrickerGameManager brickerGameManager;

    /**
     * creates another TurboStrategy object
     * @param strategy the strategy that deletes the brick to be used
     * @param brickerGameManager the game manager to handle game stuff
     */
    public TurboStrategy(CollisionStrategy strategy, BrickerGameManager brickerGameManager) {
        this.strategy = strategy;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * what to do in a collision, here it changes the ball to turbo and does the other strategies
     * @param thisObj one of the object in the collision
     * @param otherObj the other object in the collision
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (brickerGameManager.isMainBall(otherObj)) {
            brickerGameManager.enterTurboMode();
        }
        strategy.onCollision(thisObj, otherObj);
    }

    /**
     * gets the strategy depth (how many other strategies we have)
     * @return the strategy depth
     */
    public int getStrategyDepth() {
        return strategy.getStrategyDepth() + 1;
    }
}
