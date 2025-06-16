import bagel.*;
import bagel.util.*;

/**
 * Abstract Monkey class that can be extended to normal or intelligent monkeys, with their
 * own characteristics and actions as specified.
 */
public abstract class Monkey {
    protected double x, y;
    protected Image sprite;         // Image that represents the types of monkeys
    protected boolean destroyed = false;  // Track whether monkey is destroyed

    protected static final double SPEED = 0.5;  // Constant speed of 0.5 pixels per frame
    protected double velocityY = 0.4;           // Initial falling velocity
    protected boolean movingRight;              // Track the direction of monkeys
    protected int[] route;                      // Route that monkeys will walk
    protected int routeIndex = 0;
    protected double distanceWalked = 0;        // Total distance walked for one route

    protected static final double HEIGHT_TOLERANCE = 1.0;   // Tolerance value when checking platform interaction

    protected Image leftSprite;
    protected Image rightSprite;

    /**
     * Construct the Monkey object at the specified position with correct image.
     * @param x The x-coordinate of the Monkey.
     * @param y The y-coordinate of the Monkey.
     * @param image The image of the Monkey.
     */
    public Monkey(double x, double y, String image) {
        this.x = x;
        this.y = y;
        this.sprite = new Image(image);
    }

    /**
     * Draw the Monkeys when they are alive
     */
    public void draw() {
        if (!destroyed) {
            sprite.draw(x, y);
        }
    }

    /**
     * Retrieves the rectangle of the Monkeys.
     * @return The Rectangle of the Monkeys.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(x - sprite.getWidth()/2, y - sprite.getHeight()/2, sprite.getWidth(), sprite.getHeight());
    }

    /**
     * Track if the Monkeys are destroyed.
     * @return {@code true} if is destroyed, {@code false} otherwise.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Set monkeys to be destroyed.
     */
    public void destroy() {
        destroyed = true;
    }

    /**
     * Update the Monkey's position by applying gravity and checking for platform collisions.
     * Monkeys should turn back when they reach the edge of screen or platforms.
     * Monkeys should also turn back when they walk over the route specified distance.
     * When Monkeys are turned back, they should focus on the next route, ignoring
     * the remaining distance for the last route.
     * After reaching the final distance in the sequence, the pattern should loop back
     * to the first distance and continue cycling indefinitely.
     *
     * @param mario Mario object in the game.
     * @param platforms A list of platforms in the game.
     */
    public void update(Mario mario, Platform[] platforms) {
        // 1) Check if monkey has been destroyed
        if (destroyed) return;

        // 2) Apply gravity and snap monkeys onto platforms when landing
        velocityY += Physics.MONKEY_GRAVITY;
        y += velocityY;

        if (handlePlatforms(platforms)) {
            velocityY = 0;
        } else {
            draw();
            return;
        }

        // 3) Turn around if next step leads off the platform
        if (isAtPlatformEdge(platforms)) {
            flipDirection();
        }

        // 4) Make sure that monkeys stop exactly at the
        //    route boundary when it is about to overshoot the distance
        //    Also, monkeys should turn back and walk for the next route
        //    when they walked over the current route
        double move = SPEED;
        if (distanceWalked >= route[routeIndex]) {
            move = route[routeIndex] - distanceWalked;
            flipDirection();
        }

        // 5) Control movement
        x += movingRight ? move : -move;
        distanceWalked += move;


        // 5) Monitor the screen boundary and turn back if necessary
        if ((x-sprite.getWidth()/2) <= 0 || (x+sprite.getWidth()/2) >= ShadowDonkeyKong.getScreenWidth()) {
            flipDirection();
        }

        // 6) Update sprite again since there might be any change of image
        updateSprite();

        // 7) Draw monkeys
        draw();
    }


    /**
     * Helper method to handle platforms to see if the Monkeys falls to them.
     * @param platforms A list of platforms in the game.
     * @return {@code true} if the Monkeys falls to any platforms, {@code false} otherwise.
     */
    protected boolean handlePlatforms(Platform[] platforms) {
        Rectangle monkeyBox = getBoundingBox();
        for (Platform p : platforms) {
            if (monkeyBox.intersects(p.getBoundingBox())) {
                y = p.getBoundingBox().top() - sprite.getHeight() / 2;
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to see if the Monkeys reach the edge of platforms.
     * @param platforms A list that contains platforms.
     * @return {@code true} if the Monkeys reach teh edge of platforms, {@code false} otherwise.
     */
    protected boolean isAtPlatformEdge(Platform[] platforms) {
        double monkeyFeetY = y + sprite.getHeight() / 2;
        double nextX = x + (movingRight ? SPEED : -SPEED);

        for (Platform p : platforms) {
            Rectangle platBox = p.getBoundingBox();

            // 1. Check if monkey is standing at same Y level as platform
            boolean sameLevel = Math.abs(monkeyFeetY - platBox.top()) <= HEIGHT_TOLERANCE;

            // 2. Check horizontal overlap with current position
            boolean currentlyOnPlatform = x + sprite.getWidth() / 2 > platBox.left() &&
                    x - sprite.getWidth() / 2 < platBox.right();

            // 3. Will next step move monkey off the platform?
            boolean steppingOff = nextX + sprite.getWidth() / 2 > platBox.right() ||
                    nextX - sprite.getWidth() / 2 < platBox.left();

            if (sameLevel && currentlyOnPlatform && steppingOff) {
                return true;
            }
        }

        return false;

    }

    /**
     * Flip the direction of the Monkeys.
     * Set the distance walked to be 0.
     * Monkeys should move with the next route.
     */
    protected void flipDirection() {
        movingRight = !movingRight;
        distanceWalked = 0;
        routeIndex = (routeIndex + 1) % route.length;
    }

    /**
     * Update the Monkeys image.
     */
    protected void updateSprite() {
        sprite = movingRight ? rightSprite : leftSprite;
    }
}