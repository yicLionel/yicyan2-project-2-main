import bagel.*;
import bagel.Image;
import bagel.util.Rectangle;

/**
 * Represents Bullets in the game.
 * The Bullets can be shot by Mario when he holds a Blaster.
 * The Bullets can kill monkeys and donkey.
 */
public class Bullet {
    private double x, y;
    private final double WIDTH, HEIGHT;
    private static final double speed = 3.8;        // Constant speed of 3.8 pixels per frame
    private static final double MAX_DISTANCE = 300; // Bullet can move at most 300 pixels
    private double distanceTraveled = 0;            // Distance moved so far for a bullet
    private boolean active = true;                  // Whether the bullet is active
    private final boolean toRight;                  // Direction of the bullet
    private final Image BULLET_IMAGE;               // Bullet image


    /**
     * Constructs a Bullet at the specified position.
     *
     * @param startX The initial x-coordinate of the bullet.
     * @param startY The initial y-coordinate of the bullet.
     * @param toRight The direction of the bullet.
     */
    public Bullet(double startX, double startY, boolean toRight) {
        this.x = startX;
        this.y = startY;
        this.toRight = toRight;
        if (toRight == true) {
            this.BULLET_IMAGE = new Image("res/bullet_right.png");
        } else {
            this.BULLET_IMAGE = new Image("res/bullet_left.png");
        }
        this.WIDTH = BULLET_IMAGE.getWidth();
        this.HEIGHT = BULLET_IMAGE.getHeight();

    }

    /**
     * Update bullet's position, checks if out of boundary or travelled too long.
     * @param platforms
     */
    public void update(Platform[] platforms) {
        if (!active) {
            return;
        }

        double speed_x = toRight ? speed : -speed;
        x += speed_x;
        distanceTraveled += Math.abs(speed_x);

        // De-active if out of boundary or travelled too long
        if (x < 0 || x > ShadowDonkeyKong.getScreenWidth() || distanceTraveled > MAX_DISTANCE) {
            deActive();
        }

        // De-active if touches any platform
        handlePlatforms(platforms);


    }

    /**
     * Draw the bullet on the screen if it is active.
     */
    public void draw() {
        if (active) BULLET_IMAGE.draw(x, y);
    }

    /**
     * Check whether the current bullet is active
     * @return {@code true} if the current bullet is active.
     *         {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the bounding box of the bullet for collision detection.
     *
     * @return A {@link Rectangle} representing the bullet's bounding box.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(x - BULLET_IMAGE.getWidth()/2, y - BULLET_IMAGE.getHeight()/2,
                               BULLET_IMAGE.getWidth(), BULLET_IMAGE.getHeight());
    }

    /**
     * De-active the bullet
     */
    public void deActive() {
        active = false;
    }

    /**
     * De-active the bullet when touching platforms
     */
    private void handlePlatforms(Platform[] platforms) {
        Rectangle bulletBox = getBoundingBox();
        for (Platform p : platforms) {
            if (bulletBox.intersects(p.getBoundingBox())) {
                deActive();
            }
        }
    }
}