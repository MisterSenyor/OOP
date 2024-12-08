package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;

public class moreBallsStrategy extends BasicCollisionStrategy {
    private ImageReader puckImageReader;
    private Sound puckCollisionSound;


    public moreBallsStrategy(BrickerGameManager brickerGameManager, int brickIndex) {
        super(brickerGameManager, brickIndex);
    }
}

