package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;

/**
 * the strategy that creates extra balls.
 * @author Tomer Zilberman
 */
public class PucksStrategy implements CollisionStrategyDecorator {
    // the number of pucks we add
    private static final int PUCK_TO_ADD = 2;
    // strategy that used in addition to the extra pucks
    private CollisionStrategy strategy;
    // brick game manager used to handle creation and deletion of gam objects
    private BrickerGameManager brickerGameManager;
    public PucksStrategy(CollisionStrategy strategy, BrickerGameManager brickerGameManager) {
        this.strategy = strategy;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * what to do in a collision. here we do in addition to the strategy the addition of w balls
     * @param thisObj one of the object in the collision
     * @param otherObj the other object in the collision
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.addPucks(PUCK_TO_ADD, thisObj.getCenter());
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

