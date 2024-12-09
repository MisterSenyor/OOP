package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;


/**
 * a special strategy that creates another paddle.
 * @author Tomer Zilberman
 */
public class AnotherPaddleStrategy implements CollisionStrategyDecorator {
    // the strategy it uses to delete the brick
    private CollisionStrategy strategy;
    // an instance of game brick manager to handle game objects
    private BrickerGameManager brickerGameManager;


    /**
     * creates another AnotherPaddleStrategy object
     * @param strategy the strategy that deletes the brick to be used
     * @param brickerGameManager the game manager to handle game stuff
     */
    public AnotherPaddleStrategy(CollisionStrategy strategy, BrickerGameManager brickerGameManager) {
        this.strategy = strategy;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * what to do in a collision. does the usual stuff and creates another paddle
     * @param thisObj one of the objects that was in the collision
     * @param otherObj the other object that was in the collision
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.createBonusPaddle();
        strategy.onCollision(thisObj, otherObj);
    }

    /**
     * gets the depth of the strategy (how many layer deep)
     * @return how many layers of decorators are there
     */
    public int getStrategyDepth() {
        return strategy.getStrategyDepth() + 1;
    }
}
