package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class LifeCounter {
    private int numOfLives, maxLives;
    private GameObject[] hearts;
    private GameObject numericalLifeCount;
    private BrickerGameManager brickerGameManager;
    private GameObject numericalCount;
    private Vector2 numericalPos, numericalSize;

    public LifeCounter(int numOfLives,int maxLives, GameObject[] hearts, BrickerGameManager brickerGameManager, Vector2 numericalPos,
                       Vector2 numericalSize) {
        this.numOfLives = numOfLives;
        this.hearts = hearts;
        this.maxLives = maxLives;
        this.brickerGameManager = brickerGameManager;
        this.numericalPos = numericalPos;
        this.numericalSize = numericalSize;
        TextRenderable numericalCountRenderable = new TextRenderable(String.valueOf(numOfLives));
        switch (numOfLives) {
            case 1:
                numericalCountRenderable.setColor(Color.RED);
                break;
            case 2:
                numericalCountRenderable.setColor(Color.YELLOW);
                break;
            default:
                numericalCountRenderable.setColor(Color.GREEN);
        }
        numericalCount = new GameObject(numericalPos, numericalSize, numericalCountRenderable);
        brickerGameManager.addUIObject(numericalCount);
    }

    public boolean loss() {
        if (numOfLives <= 1) {
            return true;
        }
        numOfLives--;
        brickerGameManager.deleteUIObject(hearts[numOfLives]);
        brickerGameManager.deleteUIObject(numericalCount);
        TextRenderable numericalCountRenderable = new TextRenderable(String.valueOf(numOfLives));
        switch (numOfLives) {
            case 1:
                numericalCountRenderable.setColor(Color.RED);
                break;
            case 2:
                numericalCountRenderable.setColor(Color.YELLOW);
                break;
            default:
                numericalCountRenderable.setColor(Color.GREEN);
        }
        numericalCount = new GameObject(numericalPos, numericalSize, numericalCountRenderable);
        brickerGameManager.addUIObject(numericalCount);
        return false;
    }

    public void increaseLives() {
        if (numOfLives >= maxLives) {
            numOfLives = maxLives;
            return;
        }
        numOfLives++;
        brickerGameManager.deleteUIObject(numericalCount);
        TextRenderable numericalCountRenderable = new TextRenderable(String.valueOf(numOfLives));
        switch (numOfLives) {
            case 1:
                numericalCountRenderable.setColor(Color.RED);
                break;
            case 2:
                numericalCountRenderable.setColor(Color.YELLOW);
                break;
            default:
                numericalCountRenderable.setColor(Color.GREEN);
        }
        numericalCount = new GameObject(numericalPos, numericalSize, numericalCountRenderable);
        brickerGameManager.addUIObject(numericalCount);
        brickerGameManager.addUIObject(hearts[numOfLives - 1]);
    }

}
