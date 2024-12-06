package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.LifeCounter;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class BrickerGameManager extends GameManager {
    static final int BRICKS_ROW_DEFAULT = 7;
    static final int BRICKS_COLUMN_DEFAULT = 8;
    static final int BRICKS_DISTANCE = 1;
    static final float BRICK_HEIGHT = 15;
    static final int WALL_WIDTH = 1;
    private Ball ball;
    private Brick[] bricks;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private LifeCounter lifeCounter;

    public BrickerGameManager(String name, Vector2 pos) {super(name, pos);}

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        // Create background
        Renderable bgImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject bg = new GameObject(Vector2.ZERO, windowDimensions, bgImage);
        bg.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(bg, Layer.BACKGROUND);

        // Create ball
        createBall(imageReader, soundReader);

        // create brick
        createBricks(imageReader, 7, 8);

        //create LifeCounter
        lifeCounter=new LifeCounter(3, null, null);

        // Create paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15), paddleImage, inputListener,
                windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);

        // Create walls
        GameObject wall = new GameObject(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(wall);
        wall = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), WALL_WIDTH), null);
        gameObjects().addGameObject(wall);
        wall = new GameObject(new Vector2(windowDimensions.x() - WALL_WIDTH, 0), new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(wall);

    }

    private void createBricks(ImageReader imageReader, int numOfRows, int numOfCols) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        bricks = new Brick[numOfRows * numOfCols];
        float brickSizeX = (windowDimensions.x()-BRICKS_DISTANCE*(numOfCols+2))/numOfCols;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                bricks[i*numOfCols + j] = new Brick(new Vector2((brickSizeX+BRICKS_DISTANCE) * j + BRICKS_DISTANCE,
                        (BRICK_HEIGHT+BRICKS_DISTANCE) * i + BRICKS_DISTANCE), new Vector2(brickSizeX,
                        BRICK_HEIGHT), brickImage, new BasicCollisionStrategy(this,
                        i*numOfCols + j));
                gameObjects().addGameObject(bricks[i*numOfCols + j]);
            }
        }
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(50, 50), ballImage, collisionSound);
        ball.setCenter(this.windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);

        float ballVelX = 300f, ballVelY = 300f;
        Random random = new Random();
        if (random.nextBoolean()) {
            ballVelY = -ballVelY;
        }
        if (random.nextBoolean()) {
            ballVelX = -ballVelX;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForEndgame();
    }

    private void checkForEndgame() {
        double ballHeight = this.ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            String prompt = "You lost! Play again?";
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    public void deleteBrick(int brickIndex) {
        gameObjects().removeGameObject(bricks[brickIndex]);
    }


    public static void main(String[] args) {
        GameManager manager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        manager.run();
    }
}
