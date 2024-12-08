package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Iterator;
import java.util.LinkedList;

public class BrickerGameManager extends GameManager {
    private static final int BRICKS_ROW_DEFAULT = 7;
    private static final int BRICKS_COLUMN_DEFAULT = 8;
    private static final int BRICKS_DISTANCE = 1;
    private static final float BRICK_HEIGHT = 15;
    static final int WALL_WIDTH = 20;
    private Ball ball;
    private Brick[] bricks;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private LifeCounter lifeCounter;
    private LinkedList<Puck> puckList;
    private Renderable puckImage;
    private Sound puckSound;

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

        //create Pucks
        createPucks(imageReader, soundReader);

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
        GameObject wall = new GameObject(new Vector2(-WALL_WIDTH, 0), new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        wall = new GameObject(new Vector2(0, -WALL_WIDTH), new Vector2(windowDimensions.x(), WALL_WIDTH), null);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        wall = new GameObject(new Vector2(windowDimensions.x(), 0), new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);

    }

    private void createBricks(ImageReader imageReader, int numOfRows, int numOfCols) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        bricks = new Brick[numOfRows * numOfCols];
        float brickSizeX = (windowDimensions.x()-BRICKS_DISTANCE*(numOfCols+2))/numOfCols;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                bricks[i*numOfCols + j] = new Brick(new Vector2((brickSizeX+BRICKS_DISTANCE) * j + BRICKS_DISTANCE,
                        (BRICK_HEIGHT+BRICKS_DISTANCE) * i + BRICKS_DISTANCE), new Vector2(brickSizeX,
                        BRICK_HEIGHT), brickImage, new BasicCollisionStrategy(this));
                gameObjects().addGameObject(bricks[i*numOfCols + j], Layer.STATIC_OBJECTS);
            }
        }
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        ball = new Ball(Vector2.ZERO, ballImage, collisionSound);
        ball.setCenter(this.windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
    }

    private void createPucks(ImageReader imageReader, SoundReader soundReader) {
        this.puckList = new LinkedList<Puck>();
        this.puckImage = imageReader.readImage("assets/mockBall.png", true);
        this.puckSound = soundReader.readSound("assets/blop.wav");
    }

    public void addPucks(int numOfPucks, Vector2 location) {
        for (int i = 0; i < numOfPucks; i++) {
            puckList.add(new Puck(location, puckImage, puckSound));
            gameObjects().addGameObject(puckList.getLast());
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForEndgame();
        checkPucks();
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

    private void checkPucks() {
        Iterator<Puck> iterator = puckList.iterator();
        while (iterator.hasNext()) {
            Puck puck = iterator.next();
            if (puck.getCenter().y() > windowDimensions.y()) {
                gameObjects().addGameObject(puck);
                iterator.remove(); // Safely removes the current element
            }
        }
    }

    public void deleteStaticObject(GameObject object) {
        gameObjects().removeGameObject(object, Layer.STATIC_OBJECTS);
    }


    public static void main(String[] args) {
        GameManager manager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        manager.run();
    }
}
