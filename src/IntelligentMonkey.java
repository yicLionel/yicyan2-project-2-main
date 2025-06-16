import bagel.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the intelligent monkeys class in the game.
 * Intelligent monkeys can move along the platform according to their route.
 * They can also shoot bananas to kill Mario.
 */

public class IntelligentMonkey extends Monkey implements Shootable{
    private int shootCoolDown = 0;
    private static final int SHOOT_INTERVAL = 300;
    private ArrayList<Banana> bananas = new ArrayList<>();


    /**
     * Construct Intelligent Monkey at the specified position.
     *
     * @param x The x-coordinate of Intelligent Monkey.
     * @param y The y-coordinate of Intelligent Monkey.
     * @param direction The direction of Intelligent Monkey.
     * @param route The route of Intelligent Monkey.
     */
    public IntelligentMonkey(double x, double y, String direction, int[] route) {
        super(x, y, direction.equals("right") ? "res/intelli_monkey_right.png" : "res/intelli_monkey_left.png");
        this.leftSprite = new Image("res/intelli_monkey_left.png");
        this.rightSprite = new Image("res/intelli_monkey_right.png");
        this.movingRight = direction.equals("right");
        this.route = route;
    }

    /**
     * Update Intelligent Monkey's position by applying gravity and check for platform collisions.
     * Intelligent Monkeys can shoot banana every 5 seconds when they are alive.
     *
     * @param mario  Mario object that player controls.
     * @param platforms An array of platforms Donkey can land on.
     */
    @Override
    public void update(Mario mario, Platform[] platforms) {
        super.update(mario, platforms);

        shootCoolDown++;
        if (shootCoolDown >= SHOOT_INTERVAL) {
            shootCoolDown = 0;
            shoot();
        }
    }


    /**
     * Call this class to shoot banana.
     */
    @Override
    public void shoot() {
        double bananaX = x;
        double bananaY = y;
        Banana banana = new Banana(bananaX, bananaY, movingRight);
        bananas.add(banana);
    }


    /**
     * Update and draw Banana.
     * Bananas are stored in an iterator, being moved if they are not active any more.
     * @param mario Mario object in the game.
     * @return {@code true} if Mario touches banana, {@code false} otherwise
     */
    public boolean updateBananasAndCheckMario(Mario mario) {
        Iterator<Banana> iter = bananas.iterator();
        while (iter.hasNext()) {
            Banana b = iter.next();
            b.update();
            b.draw();

            if (!b.isActive()) {
                iter.remove();
                continue;
            }

            if (mario.getBoundingBox().intersects(b.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Destroy the certain monkey when calling this method.
     * Also destroy all the banana that shot by that monkey.
     */
    @Override
    public void destroy() {
        super.destroy(); // Destroyed the current monkey
        for (Banana b : bananas) {
            b.deActive(); // De-active all the banana which this monkey shot
        }
    }
}