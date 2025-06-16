import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Rectangle;

/**
 * Represents a Blaster collectible in the game.
 * The blaster can be collected by the player, at which point it disappears from the screen.
 */
public class Blaster {
    private final Image BLASTER_IMAGE;      // Blaster image
    private final double WIDTH, HEIGHT;
    private final double X, Y;
    private boolean isCollected = false;   // Check whether the blaster is collected yet

    /**
     * Constructs a Blaster at the specified position.
     *
     * @param startX The initial x-coordinate of the blaster.
     * @param startY The initial y-coordinate of the blaster.
     */
    public Blaster(double startX, double startY) {
        this.BLASTER_IMAGE = new Image("res/blaster.png");
        this.X = startX;
        this.Y = startY;
        this.WIDTH = BLASTER_IMAGE.getWidth();
        this.HEIGHT = BLASTER_IMAGE.getHeight();
    }

    /**
     * Returns the bounding box of the blaster for collision detection.
     * If the blaster has been collected, it returns an off-screen bounding box.
     *
     * @return A {@link Rectangle} representing the blaster's bounding box.
     */
    public Rectangle getBoundingBox() {
        if (isCollected) {
            return new Rectangle(-1000, -1000, 0, 0); // Move off-screen if collected
        }
        return new Rectangle(
                X - (WIDTH / 2),  // Center-based positioning
                Y - (HEIGHT / 2),
                WIDTH,
                HEIGHT
        );
    }

    /**
     * Draws the blaster on the screen if it has not been collected.
     */
    public void draw() {
        if (!isCollected) {
            BLASTER_IMAGE.draw(X, Y); // Bagel centers images automatically
//            drawBoundingBox(); // Uncomment for debugging
        }
    }

    /**
     * Marks the blaster as collected, removing it from the screen.
     */
    public void collect() {
        isCollected = true;
    }

    /**
     * Checks if the blaster has been collected.
     *
     * @return {@code true} if the blaster is collected, {@code false} otherwise.
     */
    public boolean isCollected() {
        return isCollected;
    }

}
