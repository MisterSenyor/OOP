package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * strategy that creates more life to collect.
 * @author Ido Minha
 */
public class ExtraLifeStrategy implements CollisionStrategyDecorator {
    //the other strategies we have
    private CollisionStrategy strategy;
    // used for the addition and creation of objects
    private BrickerGameManager brickerGameManager;

    /**
     * creates another ExtraLifeStrategy object
     * @param strategy the strategy that deletes the brick to be used
     * @param brickerGameManager the game manager to handle game stuff
     */
    public ExtraLifeStrategy(CollisionStrategy strategy, BrickerGameManager brickerGameManager) {
        this.strategy = strategy;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * what to do in collision. here we add extra life and do the other strategies
     * @param thisObj one of the object in the collision
     * @param otherObj the other object in the collision
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (!((Brick) thisObj).isDeleted()) {
            brickerGameManager.addExtraLife(thisObj.getCenter());
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
