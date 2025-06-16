import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

/**
 * Represents a barrel in the game, affected by gravity and platform collisions.
 * The barrel can be destroyed, at which point it will no longer be drawn or interact with the environment.
 */
public class Barrel {
    private final Image BARREL_IMAGE;
    private final double X;               // constant because x does not change, only relying on falling
    private double y;
    private double velocityY = 0;         // Barrel's vertical velocity
    private boolean isDestroyed = false;  // Whether barrel is being destroyed

    /**
     * Constructs a new Barrel at the specified starting position.
     *
     * @param startX The initial x-coordinate of the barrel.
     * @param startY The initial y-coordinate of the barrel.
     */
    public Barrel(double startX, double startY) {
        this.BARREL_IMAGE = new Image("res/barrel.png"); // Load barrel sprite
        this.X = startX;
        this.y = startY;
    }

    /**
     * Updates the barrel's position, applies gravity, checks for platform collisions,
     * and renders the barrel if it is not destroyed.
     *
     * @param platforms An array of platforms for collision detection.
     */
    public void update(Platform[] platforms) {
        if (!isDestroyed) {
            // 1) Apply gravity
            velocityY += Physics.BARREL_GRAVITY;
            if (velocityY > Physics.BARREL_TERMINAL_VELOCITY) {
                velocityY = Physics.BARREL_TERMINAL_VELOCITY;
            }
            y += velocityY;

            // 2) Check for platform collisions
            for (Platform platform : platforms) {
                if (this.getBoundingBox().intersects(platform.getBoundingBox())) {
                    // Position the barrel on top of the platform
                    y = platform.getY() - (platform.getHeight() / 2) - (BARREL_IMAGE.getHeight() / 2);
                    velocityY = 0; // Stop falling
                    break;
                }
            }

            // 3) Draw the barrel
            draw();
        }
    }


    /**
     * Draws the barrel on the screen if it is not destroyed.
     */
    public void draw() {
        if (!isDestroyed) {
            BARREL_IMAGE.draw(X, y);
//            drawBoundingBox(); // Uncomment for debugging
        }
    }

    /**
     * Creates and returns the barrel's bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing the barrel's bounding box.
     *         If the barrel is destroyed, returns an off-screen bounding box.
     */
    public Rectangle getBoundingBox() {
        if (isDestroyed) {
            return new Rectangle(-1000, -1000, 0, 0); // Off-screen if destroyed
        }
        return new Rectangle(
                X - (BARREL_IMAGE.getWidth() / 2),
                y - (BARREL_IMAGE.getHeight() / 2),
                BARREL_IMAGE.getWidth(),
                BARREL_IMAGE.getHeight()
        );
    }

    /**
     * Marks the barrel as destroyed, preventing it from being drawn or updated.
     */
    public void destroy() {
        isDestroyed = true;
    }

    /**
     * Checks if the barrel has been destroyed.
     *
     * @return {@code true} if the barrel is destroyed, {@code false} otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Retrieves the barrel's image.
     *
     * @return An {@link Image} representing the barrel.
     */
    public Image getBarrelImage() {
        return this.BARREL_IMAGE;
    }

    /**
     * Gets the x-coordinate of the barrel.
     *
     * @return The current x-coordinate of the barrel.
     */
    public double getX() { return X; }

    /**
     * Gets the y-coordinate of the barrel.
     *
     * @return The current y-coordinate of the barrel.
     */
    public double getY() { return y; }
}
