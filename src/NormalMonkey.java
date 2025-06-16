import bagel.Image;

/**
 * Represents Normal Monkey in the game.
 */
public class NormalMonkey extends Monkey {
    /**
     * Construct Normal Monkey object.
     * @param x  The x-coordinate of Normal Monkeys.
     * @param y  The y-coordinate of Normal Monkeys.
     * @param direction  The direction of Normal Monkeys.
     * @param route  The route of Normal Monkeys.
     */
    public NormalMonkey(double x, double y, String direction, int[] route) {
        super(x, y, direction.equals("right") ? "res/normal_monkey_right.png" : "res/normal_monkey_left.png");
        this.leftSprite = new Image("res/normal_monkey_left.png");
        this.rightSprite = new Image("res/normal_monkey_right.png");
        this.movingRight = direction.equals("right");
        this.route = route;
    }

    /**
     * Update Normal Monkey's position by calling the update method from super class.
     *
     * @param mario Mario object in the game.
     * @param platforms A list of platforms in the game.
     */
    @Override
    public void update(Mario mario, Platform[] platforms) {
        super.update(mario, platforms);
    }
}