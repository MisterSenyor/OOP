package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * the factory of Strategies, used to make and choose strategies.
 */
public class StrategyFactory {
    private static final int NUMBER_OF_STRATEGIES = 5;
    private static final int DOUBLE_STRATEGY = 0;
    private static final int EXTRA_LIFE_STRATEGY = 1;
    private static final int TURBO_STRATEGY = 2;
    private static final int ANOTHER_PADDLE_STRATEGY = 3;
    // the max depth of strategies
    private final int MAX_DEPTH = 3;
    // the brick game manager which all strategies use
    BrickerGameManager brickerGameManager;

    /**
     * Creates a new StrategyFactory object
     * @param brickerGameManager the manager of the game we run
     */
    public StrategyFactory(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * generates the collision strategy based on probability
     * @return the new chosen strategy
     */
    public CollisionStrategy generateCollisionStrategy() {
        Random random = new Random();
        boolean special = random.nextBoolean();
        if (special) {
            return randomlyDecorate(new BasicCollisionStrategy(brickerGameManager));
        }
        return new BasicCollisionStrategy(brickerGameManager);
    }

    /**
     * generate a new strategy and adds it to the input one (can't be null)
     * @param strategy the strategies we already have
     * @return the new strategy, with the old ones and the new generated one together
     */
    private CollisionStrategy randomlyDecorate(CollisionStrategy strategy) {
        // can't add if we pass max depth
        if (strategy.getStrategyDepth() >= MAX_DEPTH) {
            return strategy;
        }
        //randomly chooses and implement the strategy
        Random random = new Random();
        int choice = random.nextInt(NUMBER_OF_STRATEGIES);
        switch (TURBO_STRATEGY) {
            case DOUBLE_STRATEGY:
                return randomlyDecorate(randomlyDecorate(strategy));
            case EXTRA_LIFE_STRATEGY:
                return new ExtraLifeStrategy(strategy, brickerGameManager);
            case TURBO_STRATEGY:
                return new TurboStrategy(strategy, brickerGameManager);
            case ANOTHER_PADDLE_STRATEGY:
                return new AnotherPaddleStrategy(strategy, brickerGameManager);
            default:
                return new PucksStrategy(strategy, brickerGameManager);
        }
    }
}
