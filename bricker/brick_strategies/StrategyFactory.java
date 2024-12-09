package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;

import java.util.Random;

public class StrategyFactory {
    private final int MAX_DEPTH = 3;
    BrickerGameManager brickerGameManager;
    public StrategyFactory(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    public CollisionStrategy generateCollisionStrategy() {
        Random random = new Random();
        boolean special = random.nextBoolean();
        if (special) {
            return randomlyDecorate(new BasicCollisionStrategy(brickerGameManager));
        }
        return new BasicCollisionStrategy(brickerGameManager);
    }
    private CollisionStrategy randomlyDecorate(CollisionStrategy strategy) {
        if (strategy.getStrategyDepth() >= 3) {
            return strategy;
        }
        Random random = new Random();
        int choice = random.nextInt(5);
        switch (choice) {
            case 0:
                return randomlyDecorate(randomlyDecorate(strategy));
            case 1:
                return new ExtraLifeStrategy(strategy, brickerGameManager);
            case 2:
                return new TurboStrategy(strategy, brickerGameManager);
            case 3:
                return new AnotherPaddleStrategy(strategy, brickerGameManager);
            default:
                return new PucksStrategy(strategy, brickerGameManager);
        }
    }
}
