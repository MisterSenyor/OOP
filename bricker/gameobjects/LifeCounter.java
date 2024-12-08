package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class LifeCounter {
    private int numOfLives, maxLives;
    private GameObject[] hearts;
    private GameObject numericalLifeCount;
    private BrickerGameManager brickerGameManager;

    public LifeCounter(int numOfLives,int maxLives, GameObject[] hearts, GameObject
            numericalLifeCount, BrickerGameManager brickerGameManager) {
        this.numOfLives = numOfLives;
        this.numericalLifeCount = numericalLifeCount;
        this.hearts = hearts;
        this.maxLives = maxLives;
        this.brickerGameManager = brickerGameManager;
    }

    public boolean loss() {
        if (numOfLives <= 1) {
            return true;
        }
        numOfLives--;
        brickerGameManager.deleteBackgroundObject(hearts[numOfLives]);
        return false;
    }

}
