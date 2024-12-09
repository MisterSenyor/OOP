package bricker.main;

import bricker.brick_strategies.StrategyFactory;
import bricker.gameobjects.BonusPaddle;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * manage and runs the bricker game. integrate all the classes together and activates them
 * through the main.
 * @author Tomer Zilberman and Ido Minha
 */
public class BrickerGameManager extends GameManager {
    // constants of the game objects
    private static final int BRICKS_ROW_DEFAULT = 7;
    private static final int BRICKS_COLUMN_DEFAULT = 8;
    private static final int BRICKS_DISTANCE = 1;
    private static final float BRICK_HEIGHT = 15;
    static final float WALL_WIDTH = 20;
    private static final int NUM_OF_ARGS = 2;
    private static final int ROWS_INDEX = 0;
    private static final int COLUMN_INDEX = 1;
    private static final float DEFAULT_SCREEN_SIZE_X = 700;
    private static final float DEFAULT_SCREEN_SIZE_Y = 500;
    private final int NUM_LIVES = 3, MAX_LIVES = 4;

    //constant that the constructor init
    private final int bricksRows, bricksColumn;
    //game objects
    private Ball ball;
    private BonusPaddle bonusPaddle;
    private LinkedList<Puck> puckList;
    private LifeCounter lifeCounter;

    //games utils
    private int bricksCounter;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private UserInputListener userInputListener;
    private Sound ballSound, puckSound;
    private ImageRenderable ballNormalImage, ballTurboImage;
    private ImageRenderable paddleImage, heartImage, puckImage;
    private boolean bonusPaddleExists;

    /**
     * Creates new brickerGameManager object
     * @param name the name of the window
     * @param pos the size of the window
     * @param bricksRows the number of brick rows
     * @param bricksColumn the number of bricks in each rows
     */
    public BrickerGameManager(String name, Vector2 pos, int bricksRows, int bricksColumn) {
        super(name, pos);
        this.bricksRows = bricksRows;
        this.bricksColumn = bricksColumn;
    }

    /**
     * initialize all the game components based on the constructor and the default values
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
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
        ballSound = soundReader.readSound("assets/blop.wav");
        createBall();

        //create Pucks
        createPucks(imageReader, soundReader);

        // create brick
        createBricks(imageReader, bricksRows, bricksColumn);

        // Create paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new Paddle(Vector2.ZERO, paddleImage, inputListener,
                windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);

        // Create walls
        GameObject wall = new GameObject(new Vector2(-WALL_WIDTH, 0), new Vector2(WALL_WIDTH,
                windowDimensions.y()), null);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        wall = new GameObject(new Vector2(0, -WALL_WIDTH), new Vector2(windowDimensions.x(), WALL_WIDTH),
                null);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        wall = new GameObject(new Vector2(windowDimensions.x(), 0),
                new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);

        // Create lives
        createLives(imageReader);

    }

    /**
     * creates and positions all the bricks.
     * @param imageReader to read the image of the bricks
     * @param numOfRows the number of bricks rows
     * @param numOfCols the number of bricks in each row
     */
    private void createBricks(ImageReader imageReader, int numOfRows, int numOfCols) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        Brick[] bricks = new Brick[numOfRows * numOfCols];
        StrategyFactory strategyFactory = new StrategyFactory(this);
        float brickSizeX = (windowDimensions.x()-BRICKS_DISTANCE*(numOfCols+2))/numOfCols;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                bricks[i*numOfCols + j] = new Brick(new Vector2((brickSizeX+BRICKS_DISTANCE) *
                        j + BRICKS_DISTANCE, (BRICK_HEIGHT+BRICKS_DISTANCE) * i + BRICKS_DISTANCE),
                        new Vector2(brickSizeX, BRICK_HEIGHT), brickImage,
                        strategyFactory.generateCollisionStrategy());
                gameObjects().addGameObject(bricks[i*numOfCols + j], Layer.STATIC_OBJECTS);
            }
        }
        bricksCounter = numOfCols * numOfRows;
    }

    /**
     * initialize the life counter
     * @param imageReader to read the assets of the hearts
     */
    private void createLives(ImageReader imageReader) {
        heartImage = imageReader.readImage("assets/heart.png", true);
        GameObject[] hearts = new GameObject[MAX_LIVES];
        for (int i = 0; i < MAX_LIVES; i++) {
             hearts[i] = new GameObject(new Vector2(WALL_WIDTH + i * FallingExtraLife.DEFAULT_SIZE.x()
                     , windowDimensions.y() - WALL_WIDTH * 2),
                     FallingExtraLife.DEFAULT_SIZE, heartImage);
             if (i < NUM_LIVES) {
                 gameObjects().addGameObject(hearts[i], Layer.UI);
             }
        }
        lifeCounter=new LifeCounter(NUM_LIVES, MAX_LIVES, hearts,
                this, new Vector2(WALL_WIDTH, WALL_WIDTH),
                new Vector2(WALL_WIDTH, WALL_WIDTH * 2));
    }

    /**
     * creates and positions the ball
     */
    private void createBall() {
        ball = new Ball(Vector2.ZERO, ballNormalImage, ballSound);
        ball.setCenter(this.windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
    }

    /**
     * create the pucks' (smaller balls) list
     * @param imageReader reader to read the pucks image
     * @param soundReader reader to read the pucks collision sound
     */
    private void createPucks(ImageReader imageReader, SoundReader soundReader) {
        this.puckList = new LinkedList<>();
        this.puckImage = imageReader.readImage("assets/mockBall.png", true);
        this.puckSound = soundReader.readSound("assets/blop.wav");
    }

    /**
     * creates and positions the bonus paddle, does not create if one already exists
     */
    public void createBonusPaddle() {
        if (!this.bonusPaddleExists) {
            this.bonusPaddle = new BonusPaddle(this.paddleImage, this.userInputListener,
                    windowDimensions, this);
            gameObjects().addGameObject(bonusPaddle);
            bonusPaddleExists = true;
        }
    }

    /**
     * adds pucks to the list and the game
     * @param numOfPucks the number of pucks to add
     * @param location the location in which to add the packs in
     */
    public void addPucks(int numOfPucks, Vector2 location) {
        for (int i = 0; i < numOfPucks; i++) {
            puckList.add(new Puck(location, puckImage, puckSound));
            gameObjects().addGameObject(puckList.getLast());
        }
    }

    /**
     * updates the game loop. checks game finish, updates thing and such
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForEndgame();
        checkPucks();
    }

    /**
     * checks if the game is finished (both loss and win) and handles it with UI.
     * can reset the board or exit the program if the correct input is given.
     */
    private void checkForEndgame() {
        double ballHeight = this.ball.getCenter().y();
        //check win

        if (userInputListener.isKeyPressed(KeyEvent.VK_W) || bricksCounter <= 0) {
            String prompt = "You won! Play again?";
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
        //check loss
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

    /**
     * checks if the pucks can be deleted (i.e. if they are below the zero line) and deletes them
     * if so.
     */
    private void checkPucks() {
        Iterator<Puck> iterator = puckList.iterator();
        while (iterator.hasNext()) {
            Puck puck = iterator.next();
            if (puck.getCenter().y() > windowDimensions.y()) {
                gameObjects().addGameObject(puck);
                iterator.remove();
            }
        }
    }

    /**
     * adds an object to the UI layer.
     * @param object the object to add
     */
    public void addUIObject(GameObject object) {
        gameObjects().addGameObject(object, Layer.UI);
    }

    /**
     * remove object from the UI layer
     * @param object the object to remove
     */
    public void deleteUIObject(GameObject object) {
        gameObjects().removeGameObject(object, Layer.UI);
    }

    /**
     * deletes the bonus peddle (and updates the appropriates flags)
     */
    public void deleteBonusPaddle() {
        gameObjects().removeGameObject(this.bonusPaddle);
        bonusPaddleExists = false;
    }

    /**
     * delete an object from the DEFAULT layer
     * @param object the object to delete
     */
    public void deleteObject(GameObject object) {
        gameObjects().removeGameObject(object);
    }

    /**
     * checks if a given ball is the main ball of the game and not a puck or something else
     * @param ball the object to check
     * @return true if it is the main ball of the game
     */
    public boolean isMainBall(GameObject ball) {
        return ball == this.ball;
    }

    /**
     * switches the ball to turbo mode
     */
    public void enterTurboMode() {
        ball.setTurboMode(ballTurboImage);
    }

    /**
     * adds an extra life object to the game (the heart that falls)
     * @param center the place to spawn it in
     */
    public void addExtraLife(Vector2 center) {
        Vector2 topLeftCorner = new Vector2(center.x() - (float) heartImage.width() / 2, center.y());
        FallingExtraLife heart = new FallingExtraLife(topLeftCorner, FallingExtraLife.DEFAULT_SIZE,
                heartImage, this);
        gameObjects().addGameObject(heart);
    }

    /**
     * increases the number of life the player has
     */
    public void increaseLives() {
        lifeCounter.increaseLives();
    }

    /**
     * deletes a brick from the game. (assumes the object is brick)
     * @param obj the brick to remove
     */
    public void deleteBrick(GameObject obj) {
        boolean ret = gameObjects().removeGameObject(obj, Layer.STATIC_OBJECTS);
        //checks to not delete the same brick twice
        if (ret) {
            bricksCounter--;
            ((Brick) obj).deleteBrick();
        }
    }


    /**
     * the main function of the program, runs the game.
     * @param args the parameters of the game, none is the default values and otherwise it's
     *             the number of brick rows and then the number of bricks in each row.
     */
    public static void main(String[] args) {
        if (args.length == NUM_OF_ARGS) {
            int numOfRows = Integer.parseInt(args[ROWS_INDEX]);
            int numOfCols = Integer.parseInt(args[COLUMN_INDEX]);
            GameManager manager = new BrickerGameManager("Bricker",
                    new Vector2(DEFAULT_SCREEN_SIZE_X, DEFAULT_SCREEN_SIZE_Y), numOfRows, numOfCols);
            manager.run();
        } else if (args.length == 0) {
            GameManager manager = new BrickerGameManager("Bricker", new Vector2
                    (DEFAULT_SCREEN_SIZE_X, DEFAULT_SCREEN_SIZE_Y),
                    BRICKS_ROW_DEFAULT, BRICKS_COLUMN_DEFAULT);
            manager.run();
        }
    }
}
