import bagel.*;
import bagel.Input;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Iterator;

/**
 * Represents the main gameplay screen where the player controls Mario.
 * This class manages game objects, updates their states, and handles game logic.
 */
public class GamePlayScreen {
    private final Properties GAME_PROPS;

    // Game objects
    private Mario mario;
    private Barrel[] barrels;   // Array of barrels in the game
    private Ladder[] ladders;   // Array of ladders in the game
    private Hammer[] hammers;      // The hammer object that Mario can collect
    private Blaster[] blasters;    // The blaster object that Mario can collect
    private Donkey donkey;      // Donkey Kong, the objective of the game
    private Image background;   // Background image for the game
    private Platform[] platforms; // Array of platforms in the game
    private Monkey[] monkeys;   // Array of different Monkeys in the game

    // Frame tracking
    private int currFrame = 0;  // Tracks the number of frames elapsed

    // Game parameters
    private final int MAX_FRAMES;  // Maximum number of frames before game ends

    // Display text variables
    private final Font STATUS_FONT;
    private final int SCORE_X;
    private final int SCORE_Y;
    private final int HEALTH_X;
    private final int HEALTH_Y;
    private final int BULLET_X;
    private final int BULLET_Y;
    private static final String HEALTH_MESSAGE = "DONKEY HEALTH ";
    private static final String BULLET_MESSAGE = "BULLET ";
    private static final String SCORE_MESSAGE = "SCORE ";
    private static final String TIME_MESSAGE = "Time Left ";

    private static final int BARREL_SCORE = 100;   // Earn 100 points when destroying a barrel
    private static final int BARREL_CROSS_SCORE = 30;  // Earn 30 points when jumping over a barrel
    private static final int KILL_MONKEY_SCORE = 100;  // Earn 100 points when killing a monkey
    private static final int TIME_DISPLAY_DIFF_Y = 30;


    private int score = 0;  // Player's score for jumping over barrels only
    private int donkeyHealth = 5;  // DonkeyKong health
    private int bulletCount = 0;   // Bullet count
    private boolean isGameOver = false; // Game over flag

    private final GameLevelStrategy levelStrategy;  // Pick which level to be shown
    private final int level;   // Game level

    /**
     * Returns the player's current score.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Calculates the remaining time left in seconds.
     *
     * @return The number of seconds remaining before the game ends.
     */
    public int getSecondsLeft() {
        return (MAX_FRAMES - currFrame) / 60;
    }

    /**
     * Constructs the gameplay screen, loading resources and initializing game objects.
     *
     * @param gameProps  Properties file containing game settings.
     * @param startLevel Game level to be loaded.
     * @param startScore Starting score of the current level
     */
    public GamePlayScreen(Properties gameProps, int startLevel, int startScore) {
        this.GAME_PROPS = gameProps;

        // Load game parameters
        this.MAX_FRAMES = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));
        this.STATUS_FONT = new Font(
                gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gamePlay.score.fontSize"))
        );
        this.SCORE_X = Integer.parseInt(gameProps.getProperty("gamePlay.score.x"));
        this.SCORE_Y = Integer.parseInt(gameProps.getProperty("gamePlay.score.y"));
        String[] healthPos = GAME_PROPS.getProperty("gamePlay.donkeyhealth.coords").split(",");
        this.HEALTH_X = Integer.parseInt(healthPos[0]);
        this.HEALTH_Y = Integer.parseInt(healthPos[1]);
        this.BULLET_X = Integer.parseInt(healthPos[0]);
        this.BULLET_Y = Integer.parseInt(healthPos[1]) +30 ;
        this.background = new Image("res/background.png");

        this.levelStrategy = (startLevel == 2) ? new Level2Strategy() : new Level1Strategy();
        this.levelStrategy.initialize(this);
        this.level = startLevel;
        this.score = startScore;
    }

    /**
     * Set Mario object in the game.
     */
    public void setMario(Mario mario) {
        this.mario = mario;
    }

    /**
     * Set Donkey object in the game.
     */
    public void setDonkey(Donkey donkey) {
        this.donkey = donkey;
    }

    /**
     * Set barrels object in the game.
     */
    public void setBarrels(Barrel[] barrels) {
        this.barrels = barrels;
    }

    /**
     * Set ladders object in the game.
     */
    public void setLadders(Ladder[] ladders) {
        this.ladders = ladders;
    }

    /**
     * Set platforms object in the game.
     */
    public void setPlatforms(Platform[] platforms) {
        this.platforms = platforms;
    }

    /**
     * Set Hammer object in the game.
     */
    public void setHammer(Hammer[] hammers) {
        this.hammers = hammers;
    }

    /**
     * Set Blaster object in the game.
     */
    public void setBlaster(Blaster[] blasters) {
        this.blasters = blasters;
    }

    /**
     * Set Monkey object in the game.
     */
    public void setMonkeys(Monkey[] monkeys) {
        this.monkeys = monkeys;
    }


    /**
     * Updates game state each frame.
     *
     * @param input The current player input.
     * @return {@code true} if the game ends, {@code false} otherwise.
     */
    public boolean update(Input input) {
        currFrame++;
        // Draw background
        background.drawFromTopLeft(0, 0);

        // 1) Draw and update platforms
        for (Platform platform : platforms) {
            if (platform != null) {
                platform.draw();
            }
        }

        // 2) Update ladders
        for (Ladder ladder : ladders) {
            if (ladder != null) {
                ladder.update(platforms);
            }
        }

        // 3) Update barrels
        for (Barrel barrel : barrels) {
            if (barrel == null) continue;
            if (mario.jumpOver(barrel)) {
                score += BARREL_CROSS_SCORE;  // Add 30 score when jumped over barrels
            }
            if (!barrel.isDestroyed() && mario.isTouchingBarrel(barrel)) {
                if (!mario.holdHammer()) {
                    isGameOver = true;
                } else {
                    barrel.destroy();
                    score += BARREL_SCORE;   // Add 100 score when destroyed barrels
                }
            }
            barrel.update(platforms);
        }

        // 4) Check game time and donkey status
        if (checkingGameTime()) {
            isGameOver = true;
        }
        donkey.update(platforms);

        // 5) Draw hammer, blaster and donkey
        if (hammers != null) {
            for (Hammer hammer : hammers) {
                if (hammer != null) {
                    hammer.draw();
                }
            }
        }
        if (blasters != null) {
            for (Blaster blaster : blasters) {
                if (blaster != null) {
                    blaster.draw();
                }
            }
        }
        donkey.draw();

        // 6) Update monkeys
        if (monkeys != null) {
            for (Monkey monkey : monkeys) {
                monkey.update(mario, platforms);

                // Handle Mario-monkey collision (game over if not holding hammer/blaster, otherwise kill the monkey)
                if (!monkey.isDestroyed() && mario.isTouchingMonkey(monkey)) {
                    if (!mario.holdHammer()) {
                        isGameOver = true;
                    } else{
                        monkey.destroy();
                        score += KILL_MONKEY_SCORE;  // Add 100 score when killed monkeys
                    }
                }
            }
        }
        // 6) Update Mario
        mario.update(input, ladders, platforms, hammers, blasters);


        // 7) Check if Mario reaches Donkey
        if (mario.hasReached(donkey) && !mario.holdHammer()) {
            isGameOver = true;
        }

        // 8) Update and check banana collisions
        if (monkeys != null) {
            for (Monkey monkey : monkeys) {
                // Let intelligent monkey handle its own bananas
                if (monkey instanceof IntelligentMonkey && !monkey.destroyed) {
                    if (((IntelligentMonkey) monkey).updateBananasAndCheckMario(mario)) {
                        isGameOver = true;
                    }

                }
            }
        }

        // 9) Check if Mario shoots
        if (input.wasPressed(Keys.S) && bulletCount > 0 && mario.holdBlaster()) {
            mario.shoot();
            bulletCount--;
        }

        // 10) Update Bullets
        Iterator<Bullet> bulletIterator = mario.getBullets().iterator();
        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.update(platforms);
            b.draw();

            if (!b.isActive()) {
                bulletIterator.remove();
                continue;
            }

            // Check intersection with monkeys and destroy them
            if (monkeys != null) {
                for (Monkey monkey : monkeys) {
                    if (!monkey.isDestroyed() && b.getBoundingBox().intersects(monkey.getBoundingBox())) {
                        monkey.destroy();
                        bulletIterator.remove();
                        score += KILL_MONKEY_SCORE;  // Add 100 score when killed monkeys.
                        break;
                    }
                }
                // If bullet was removed, skip donkey check
                if (!b.isActive()) {
                    continue;
                }
            }

            // Check intersection with donkey and reduce its health
            if (b.getBoundingBox().intersects(donkey.getBoundingBox())) {
                bulletIterator.remove();
                donkeyHealth--;
                // If donkey is out of health, game over
                if (donkeyHealth <= 0) {
                    isGameOver = true;
                }
            }
        }


        // 11) Display bullet count, health, score and time left
        if (mario.didCollectBlaster()) {
            bulletCount += 5;
        }
        if (mario.holdHammer()) {
            bulletCount = 0;
        }
        displayInfo();

        // 12) Return game state
        return isGameOver || isLevelCompleted();
    }

    /**
     * Displays the player's score & time left on the screen.
     */
    public void displayInfo() {
        STATUS_FONT.drawString(SCORE_MESSAGE + score, SCORE_X, SCORE_Y);
        STATUS_FONT.drawString(HEALTH_MESSAGE + donkeyHealth, HEALTH_X, HEALTH_Y);
        STATUS_FONT.drawString(BULLET_MESSAGE + bulletCount, BULLET_X, BULLET_Y);
        // Time left in seconds
        int secondsLeft = (MAX_FRAMES - currFrame) / 60;
        int TIME_X = SCORE_X;
        int TIME_Y = SCORE_Y + TIME_DISPLAY_DIFF_Y;
        STATUS_FONT.drawString(TIME_MESSAGE + secondsLeft, TIME_X, TIME_Y);
    }

    /**
     * Checks whether the level is completed by determining if Mario has reached Donkey Kong
     * while holding a hammer. This serves as the game's winning condition.
     *
     * @return {@code true} if Mario reaches Donkey Kong while holding a hammer,
     *         indicating the level is completed; {@code false} otherwise.
     */
    public boolean isLevelCompleted() {
        // Win the game if mario use hammer or blaster to kill the donkey
        return (mario.hasReached(donkey) && mario.holdHammer()) || donkeyHealth <= 0;
    }

    /**
     * Checks if the game has reached its time limit by comparing the current frame count
     * against the maximum allowed frames. If the limit is reached, the game may trigger
     * a timeout condition.
     *
     * @return {@code true} if the current frame count has reached or exceeded
     *         the maximum allowed frames, indicating the time limit has been reached;
     *         {@code false} otherwise.
     */
    public boolean checkingGameTime() {
        return currFrame >= MAX_FRAMES;
    }

    /**
     * Return the Game Properties that being used.
     */
    public Properties getProps() { return this.GAME_PROPS; }

    /**
     * Return which level it currently is
     */
    public int getLevel() { return this.level; }
}
