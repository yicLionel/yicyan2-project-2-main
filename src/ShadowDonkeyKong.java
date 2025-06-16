import bagel.*;
import java.util.Properties;
import java.util.ServiceConfigurationError;

/**
 * The main class for the Shadow Donkey Kong game.
 * This class extends {@code AbstractGame} and is responsible for managing game initialization,
 * updates, rendering, and handling user input.
 *
 * It sets up the game world, initializes characters, platforms, ladders, and other game objects,
 * and runs the game loop to ensure smooth gameplay.
 */
public class ShadowDonkeyKong extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;

    private HomeScreen homeScreen;
    private GamePlayScreen gamePlayScreen;
    private GameEndScreen gameEndScreen;

    private int level1Score = 0;
    private int level2Score = 0;
    private int level1TotalScore = 0;
    private int level2TotalScore = 0;
    private int timeRemaining = 0;

    public static double screenWidth;

    public static double screenHeight;

    /**
     * Constructs a new instance of the ShadowDonkeyKong game.
     * Initializes the game window using provided properties and sets up the home screen.
     *
     * @param gameProps     A {@link Properties} object containing game configuration settings
     *                      such as window width and height.
     * @param messageProps  A {@link Properties} object containing localized messages or UI labels,
     *                      including the title for the home screen.
     */
    public ShadowDonkeyKong(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;
        this.screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        this.screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        homeScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
    }


    /**
     * Render the relevant screen based on the keyboard input given by the user and the status of the gameplay.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // Home Screen
        if (gamePlayScreen == null && gameEndScreen == null) {
            int startLevel = homeScreen.update(input);
            if (startLevel == 1 || startLevel == 2) {
                // Home screen is the starting point of the game and the initial score is always 0
                gamePlayScreen = new GamePlayScreen(GAME_PROPS, startLevel, 0);
            }
        }
        // Gameplay Screen
        else if (gamePlayScreen != null && gameEndScreen == null) {
            if (gamePlayScreen.update(input)) {
                boolean isWon = gamePlayScreen.isLevelCompleted(); // Track the game status
                int currentLevel = gamePlayScreen.getLevel();      // Track the game level
                timeRemaining = gamePlayScreen.getSecondsLeft();   // Store the time left of the game
                // Store the score for level 1 only
                if (currentLevel == 1) {
                    level1Score = gamePlayScreen.getScore();
                    // Calculate total level1 score individually
                    level1TotalScore = (int) (level1Score * Scores.POINTS_WEIGHT+ timeRemaining * Scores.TIME_WEIGHT);

                    if (isWon) {
                        // Move to Level 2, carry over Level 1 score
                        gamePlayScreen = new GamePlayScreen(GAME_PROPS, 2, level1Score);
                        // The level2 score starts from 0.
                        level2Score = 0;
                        level2TotalScore = 0;
                        return;
                    } else {
                        // Game ends after Level 1
                        gameEndScreen = new GameEndScreen(GAME_PROPS, MESSAGE_PROPS);
                        gameEndScreen.setIsWon(false);
                        gameEndScreen.setFinalScore(level1TotalScore, false);  // Only consider total score of level1
                        gamePlayScreen = null;
                    }
                } else if (currentLevel == 2) {
                    // Only consider level2 score itself, regardless level1
                    level2Score = gamePlayScreen.getScore() - level1Score;
                    // Calculate total level2 score individually
                    level2TotalScore = (int) (level2Score * Scores.POINTS_WEIGHT + timeRemaining * Scores.TIME_WEIGHT);

                    // Game ends after Level 2
                    gameEndScreen = new GameEndScreen(GAME_PROPS, MESSAGE_PROPS);
                    gameEndScreen.setIsWon(isWon);
                    // Calculate the total final score by adding level1 score and level2 score separately
                    gameEndScreen.setFinalScore(level1TotalScore + level2TotalScore, isWon);
                    gamePlayScreen = null;
                }
            }
        }
        // Game End Screen
        else if (gamePlayScreen == null && gameEndScreen != null) {
            if (gameEndScreen.update(input)) {
                gamePlayScreen = null;
                gameEndScreen = null;
                level1Score = 0;
                level2Score = 0;
                level1TotalScore = 0;
                level2TotalScore = 0;
                timeRemaining = 0;
            }


        }
        // Game Over / Victory Screen
        else if (gamePlayScreen == null ) {
            if (gameEndScreen.update(input)) {
                gamePlayScreen = null;
                gameEndScreen = null;
            }
        }
    }

    /**
     * Retrieves the width of the game screen.
     *
     * @return The width of the screen in pixels.
     */
    public static double getScreenWidth() {
        return screenWidth;
    }

    /**
     * Retrieves the height of the game screen.
     *
     * @return The height of the screen in pixels.
     */
    public static double getScreenHeight() {
        return screenHeight;
    }

    /**
     * The main entry point of the Shadow Donkey Kong game.
     *
     * This method loads the game properties and message files, initializes the game,
     * and starts the game loop.
     *
     * @param args Command-line arguments (not used in this game).
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDonkeyKong game = new ShadowDonkeyKong(gameProps, messageProps);
        game.run();
    }
}
