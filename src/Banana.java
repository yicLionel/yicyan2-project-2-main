import bagel.Image;
import bagel.util.Rectangle;

/**
 * Represents the object of Banana.
 * Banana can be shot by intelligent monkeys, and it can kill Mario.
 */
public class Banana {
    private double x, y;
    private final double WIDTH, HEIGHT;
    private static final double speed = 1.8;        // Banana has constant speed of 1.8 pixels per frame
    private static final double MAX_DISTANCE = 300; // Banana can move at most 300 pixels.
    private double distanceTraveled = 0;            // Distance moved so far for banana
    private boolean active = true;                  // Whether Banana is active
    private final boolean toRight;                  // Direction of the Banana
    private final Image BANANA_IMAGE;               // Banana image

    /**
     * Constructs a new Banana at the specified starting position.
     *
     * @param startX The initial x-coordinate of the banana.
     * @param startY The initial y-coordinate of the banana.
     * @param toRight The direction of the banana
     */
    public Banana(double startX, double startY, boolean toRight) {
        this.x = startX;
        this.y = startY;
        this.toRight = toRight;
        this.BANANA_IMAGE = new Image("res/banana.png");
        this.WIDTH = BANANA_IMAGE.getWidth();
        this.HEIGHT = BANANA_IMAGE.getHeight();
    }

    /**
     * Update the Banana's position, checks if banana is out of boundary or travelled to long.
     */
    public void update() {
        if (!active) {
            return;
        }

        double speed_x = toRight ? speed : -speed;
        x += speed_x;
        distanceTraveled += Math.abs(speed_x);

        // De-active if out of boundary or travelled to long
        if (x < 0 || x > ShadowDonkeyKong.getScreenWidth() || distanceTraveled > MAX_DISTANCE) {
            deActive();
        }
    }


    /**
     * Draw the banana on the screen if it is active.
     */
    public void draw() {
        if (active) BANANA_IMAGE.draw(x, y);
    }

    /**
     * Check if the banana is active.
     * @return {@code true} if the banana is active, {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the bounding box of the banana for collision detection.
     *
     * @return A {@link Rectangle} representing the banana's bounding box.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(x - BANANA_IMAGE.getWidth()/2, y - BANANA_IMAGE.getHeight()/2,
                BANANA_IMAGE.getWidth(), BANANA_IMAGE.getHeight());
    }

    /**
     * De-active the banana if needed.
     */
    public void deActive() {
        active = false;
    }
}