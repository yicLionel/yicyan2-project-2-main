import java.util.Properties;

/**
 * Strategy class for initializing game objects in level1.
 * Implements the {@link GameLevelStrategy} interface to configure Mario, Donkey, barrels,
 * ladders, platforms, and hammers based on level1 specific properties.
 */
public class Level1Strategy implements GameLevelStrategy {
    /**
     * Initializes all game objects specific to Level1 by reading from the properties file.
     *
     * @param screen The {@link GamePlayScreen} where all game elements will be placed.
     */
    @Override
    public void initialize(GamePlayScreen screen) {
        Properties props = screen.getProps();

        // 1) Create Mario
        String[] marioPos = props.getProperty("mario.level1").split(",");
        screen.setMario(new Mario(Double.parseDouble(marioPos[0]), Double.parseDouble(marioPos[1])));

        // 2) Create Donkey
        String[] donkeyPos = props.getProperty("donkey.level1").split(",");
        screen.setDonkey(new Donkey(Double.parseDouble(donkeyPos[0]), Double.parseDouble(donkeyPos[1])));

        // 3) Create the Barrels array
        int barrelCount = Integer.parseInt(props.getProperty("barrel.level1.count"));
        Barrel[] barrels = new Barrel[barrelCount];
        for (int i = 1; i <= barrelCount; i++) {
            String[] barrelPos = props.getProperty("barrel.level1." + i).split(",");
            barrels[i - 1] = new Barrel(Double.parseDouble(barrelPos[0]), Double.parseDouble(barrelPos[1]));
        }
        screen.setBarrels(barrels);

        // 4) Create the Ladders array
        int ladderCount = Integer.parseInt(props.getProperty("ladder.level1.count"));
        Ladder[] ladders = new Ladder[ladderCount];
        for (int i = 1; i <= ladderCount; i++) {
            String[] ladderPos = props.getProperty("ladder.level1." + i).split(",");
            ladders[i - 1] = new Ladder(Double.parseDouble(ladderPos[0]), Double.parseDouble(ladderPos[1]));
        }
        screen.setLadders(ladders);

        // 5) Create the Platforms array
        String[] platformEntries = props.getProperty("platforms.level1").split(";");
        Platform[] platforms = new Platform[platformEntries.length];
        for (int i = 0; i < platformEntries.length; i++) {
            String[] coords = platformEntries[i].split(",");
            platforms[i] = new Platform(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
        }
        screen.setPlatforms(platforms);

        // 6) Create Hammer
        int hammerCount = Integer.parseInt((props.getProperty("hammer.level1.count")));
        Hammer[] hammers = new Hammer[hammerCount];
        for (int i = 1; i <= hammerCount; i++) {
            String[] hammerPos = props.getProperty("hammer.level1." + i).split(",");
            hammers[i - 1] = new Hammer(Double.parseDouble(hammerPos[0]), Double.parseDouble(hammerPos[1]));
        }
        screen.setHammer(hammers);

        // 7) No blaster in level 1
        screen.setBlaster(null);
    }
}