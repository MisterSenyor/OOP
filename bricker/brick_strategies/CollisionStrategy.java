package bricker.brick_strategies;
import danogl.GameObject;

/**
 * the general collision strategy, used to group in polymorphism all the strategies
 * @author Ido Minha
 */
public interface CollisionStrategy {
    /**
     * what to do on collision, the strategy thing
     * @param thisObj one of the object in the collision
     * @param otherObj the other object in the collision
     */
    public void onCollision(GameObject thisObj, GameObject otherObj);
    /**
     * gets the depth of the strategy (how many layer deep)
     * @return how many layers of decorators are there
     */
    public int getStrategyDepth();
}
