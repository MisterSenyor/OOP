package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * Class to handle the player's lives.
 * @author idomi
 */
public class LifeCounter {
    /**
     * numOfLives, maxLives - ints representing current life count and maximum
     * hearts - array of heart GameObjects (to display/hide)
     * brickerGameManager - reference to manager
     * numericalCount - GameObject representing the numerical life count
     * numericalPos, numericalSize - the position and size of the numerical count
     */
    private int numOfLives, maxLives;
    private GameObject[] hearts;
    private BrickerGameManager brickerGameManager;
    private GameObject numericalCount;
    private Vector2 numericalPos, numericalSize;

    /**
     * Constructor.
     * @param numOfLives - amount of lives to start with
     * @param maxLives - life maximum
     * @param hearts - array of hearts to display/hide
     * @param brickerGameManager - reference to game manager
     * @param numericalPos - position of numericalCount
     * @param numericalSize - size of numericalCount
     */
    public LifeCounter(int numOfLives,int maxLives, GameObject[] hearts, BrickerGameManager brickerGameManager, Vector2 numericalPos,
                       Vector2 numericalSize) {
        this.numOfLives = numOfLives;
        this.hearts = hearts;
        this.maxLives = maxLives;
        this.brickerGameManager = brickerGameManager;
        this.numericalPos = numericalPos;
        this.numericalSize = numericalSize;
        // Render count
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

    /**
     * handle life loss
     * @return whether the game is over or not
     */
    public boolean loss() {
        if (numOfLives <= 1) { // finish game
            return true;
        }
        // decrease life count
        numOfLives--;
        // delete old objects and render new
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

    /**
     * Add life.
     */
    public void increaseLives() {
        // Don't do anything if lives exceed maximum
        if (numOfLives >= maxLives) {
            numOfLives = maxLives;
            return;
        }
        // increase lives
        numOfLives++;
        // render new life and counter
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
