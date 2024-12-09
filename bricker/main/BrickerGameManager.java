package bricker.main;

import bricker.brick_strategies.ExtraLifeStrategy;
import bricker.gameobjects.BonusPaddle;
import bricker.brick_strategies.TurboStrategy;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
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
    private ImageRenderable puckImage;
    private UserInputListener userInputListener;
    private Sound puckSound;
    private final int NUM_LIVES = 3, MAX_LIVES = 4;
    private ImageRenderable ballNormalImage, ballTurboImage;
    private Sound collisionSound;
    private ImageRenderable paddleImage;
    private ImageRenderable heartImage;
    private BonusPaddle bonusPaddle;
    private boolean bonusPaddleExists;

    public BrickerGameManager(String name, Vector2 pos) {super(name, pos);}

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.bonusPaddleExists = false;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.userInputListener = inputListener;
        this.paddleImage = imageReader.readImage("assets/paddle.png", true);
        // Create background
        Renderable bgImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject bg = new GameObject(Vector2.ZERO, windowDimensions, bgImage);
        bg.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(bg, Layer.BACKGROUND);

        // Create ball
        ballNormalImage = imageReader.readImage("assets/ball.png", true);
        ballTurboImage = imageReader.readImage("assets/redball.png", true);
        collisionSound = soundReader.readSound("assets/blop.wav");
        createBall();

        //create Pucks
        createPucks(imageReader, soundReader);

        // create brick
        createBricks(imageReader, 2, 1);

        // Create paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new Paddle(Vector2.ZERO, paddleImage, inputListener,
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

        // Create lives
        createLives(imageReader);

    }

    private void createBricks(ImageReader imageReader, int numOfRows, int numOfCols) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        bricks = new Brick[numOfRows * numOfCols];
        float brickSizeX = (windowDimensions.x()-BRICKS_DISTANCE*(numOfCols+2))/numOfCols;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                bricks[i*numOfCols + j] = new Brick(new Vector2((brickSizeX+BRICKS_DISTANCE) * j + BRICKS_DISTANCE,
                        (BRICK_HEIGHT+BRICKS_DISTANCE) * i + BRICKS_DISTANCE), new Vector2(brickSizeX,
                        BRICK_HEIGHT), brickImage, new ExtraLifeStrategy(this));
                gameObjects().addGameObject(bricks[i*numOfCols + j], Layer.STATIC_OBJECTS);
            }
        }
    }

    private void createLives(ImageReader imageReader) {
        //create LifeCounternew Vector2(((ImageRenderable)
        //                     heartImage).width(), ((ImageRenderable) heartImage).height()).mult(0.2f)
        heartImage = imageReader.readImage("assets/heart.png", true);
        GameObject hearts[] = new GameObject[MAX_LIVES];
        for (int i = 0; i < MAX_LIVES; i++) {
            // TODO const factor
             hearts[i] = new GameObject(new Vector2(WALL_WIDTH + i * FallingExtraLife.DEFAULT_SIZE.x()
                     , windowDimensions.y() - WALL_WIDTH * 2),
                     FallingExtraLife.DEFAULT_SIZE, heartImage);
             if (i < NUM_LIVES) {
                 gameObjects().addGameObject(hearts[i], Layer.BACKGROUND);
             }
        }
        lifeCounter=new LifeCounter(NUM_LIVES, MAX_LIVES, hearts,
                this, new Vector2(WALL_WIDTH, WALL_WIDTH),
                new Vector2(WALL_WIDTH, WALL_WIDTH * 2));
    }

    private void createBall() {
        ball = new Ball(Vector2.ZERO, ballNormalImage, collisionSound);
        ball.setCenter(this.windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
    }

    private void createPucks(ImageReader imageReader, SoundReader soundReader) {
        this.puckList = new LinkedList<Puck>();
        this.puckImage = imageReader.readImage("assets/mockBall.png", true);
        this.puckSound = soundReader.readSound("assets/blop.wav");
    }

    public void createBonusPaddle() {
        if (!this.bonusPaddleExists) {
            this.bonusPaddle = new BonusPaddle(this.paddleImage, this.userInputListener, windowDimensions, this);
            gameObjects().addGameObject(bonusPaddle);
            bonusPaddleExists = true;
        }
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
            if (!this.lifeCounter.loss()) {
                gameObjects().removeGameObject(ball);
                createBall();
                gameObjects().addGameObject(ball);
                return;
            }
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

    public void deleteBackgroundObject(GameObject object) {
        gameObjects().removeGameObject(object, Layer.BACKGROUND);
    }

    public void addBackgroundObject(GameObject object) {
        gameObjects().addGameObject(object, Layer.BACKGROUND);
    }

    public void deleteBonusPaddle() {
        gameObjects().removeGameObject(this.bonusPaddle);
        bonusPaddleExists = false;
    }

    public void deleteObject(GameObject object) {
        gameObjects().removeGameObject(object);
    }

    public boolean isMainBall(GameObject ball) {
        return ball == this.ball;
    }

    public void enterTurboMode() {
        ball.setTurboMode(ballTurboImage);
    }

    public void addExtraLife(Vector2 center) {
        Vector2 topLeftCorner = new Vector2(center.x() - heartImage.width() / 2, center.y());
        FallingExtraLife heart = new FallingExtraLife(topLeftCorner, new Vector2(heartImage.width(), heartImage.height()),
                heartImage, this);
        gameObjects().addGameObject(heart);
    }

    public void increaseLives() {
        lifeCounter.increaseLives();
    }


    public static void main(String[] args) {
        GameManager manager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        manager.run();
    }


}
