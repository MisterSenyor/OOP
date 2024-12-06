package bricker.main;

import bricker.gameobjects.Ball;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BrickerGameManager extends GameManager {
    static final int WALL_WIDTH = 5;
    public BrickerGameManager(String name, Vector2 pos) {super(name, pos);}

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        // Create background
        Renderable bgImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject bg = new GameObject(Vector2.ZERO, windowDimensions, bgImage);
        bg.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(bg, Layer.BACKGROUND);

        // Create ball
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        GameObject ball = new Ball(Vector2.ZERO, new Vector2(50, 50), ballImage);
        ball.setVelocity(Vector2.DOWN.mult(100f));
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball, Layer.UI);

        // Create paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new GameObject(Vector2.ZERO, new Vector2(100, 15), paddleImage);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle, Layer.UI);

        // Create walls
        GameObject wall = new GameObject(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(wall, Layer.BACKGROUND);
        wall = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), WALL_WIDTH), null);
        gameObjects().addGameObject(wall, Layer.BACKGROUND);
        wall = new GameObject(new Vector2(windowDimensions.x() - WALL_WIDTH, 0), new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(wall, Layer.BACKGROUND);

    }

//    public void update(float deltaTime) {}

    public static void main(String[] args) {
        GameManager manager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        manager.run();
    }
}
