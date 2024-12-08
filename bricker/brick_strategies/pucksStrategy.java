package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;

public class pucksStrategy extends BasicCollisionStrategy {
    private Renderable puckImage;
    private Sound puckCollisionSound;


    public pucksStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.addPucks(2, thisObj.getCenter());
    }
}

