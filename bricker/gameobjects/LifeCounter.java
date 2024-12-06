package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class LifeCounter {
    private int numOfLives;
    private GameObject[] hearts;
    private GameObject numericalLifeCount;

    public LifeCounter(int numOfLives,GameObject[] hearts, GameObject numericalLifeCount) {
        this.numOfLives = numOfLives;
        this.numericalLifeCount = numericalLifeCount;
        this.hearts = hearts;
    }

    public void loss() {
        numOfLives--;
        hearts[numOfLives] = null;
    }

}
