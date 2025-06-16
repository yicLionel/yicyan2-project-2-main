import java.util.Properties;


/**
 * Strategy class for initializing game objects in level2.
 * Implements the {@link GameLevelStrategy} interface to configure Mario, Donkey, barrels,
 * ladders, platforms, and hammers based on level2 specific properties.
 */
public class Level2Strategy implements GameLevelStrategy {
    /**
     * Initializes all game objects specific to Level2 by reading from the properties file.
     *
     * @param screen The {@link GamePlayScreen} where all game elements will be placed.
     */
    @Override
    public void initialize(GamePlayScreen screen) {
        Properties props = screen.getProps();

        // 1) Create Mario
        String[] marioPos = props.getProperty("mario.level2").split(",");
        screen.setMario(new Mario(Double.parseDouble(marioPos[0]), Double.parseDouble(marioPos[1])));
        screen.setDonkey(new Donkey(Double.parseDouble(marioPos[0]), Double.parseDouble(marioPos[1])));


        // 2) Create Donkey
        String[] donkeyPos = props.getProperty("donkey.level2").split(",");
        screen.setDonkey(new Donkey(Double.parseDouble(donkeyPos[0]), Double.parseDouble(donkeyPos[1])));

        // 3) Create the Barrels array
        int barrelCount = Integer.parseInt(props.getProperty("barrel.level2.count"));
        Barrel[] barrels = new Barrel[barrelCount];
        for (int i = 1; i <= barrelCount; i++) {
            String[] barrelPos = props.getProperty("barrel.level2." + i).split(",");
            barrels[i - 1] = new Barrel(Double.parseDouble(barrelPos[0]), Double.parseDouble(barrelPos[1]));
        }
        screen.setBarrels(barrels);

        // 4) Create the Ladders array
        int ladderCount = Integer.parseInt(props.getProperty("ladder.level2.count"));
        Ladder[] ladders = new Ladder[ladderCount];
        for (int i = 1; i <= ladderCount; i++) {
            String[] ladderPos = props.getProperty("ladder.level2." + i).split(",");
            ladders[i - 1] = new Ladder(Double.parseDouble(ladderPos[0]), Double.parseDouble(ladderPos[1]));
        }
        screen.setLadders(ladders);

        // 5) Create the Platforms array
        String[] platformEntries = props.getProperty("platforms.level2").split(";");
        Platform[] platforms = new Platform[platformEntries.length];
        for (int i = 0; i < platformEntries.length; i++) {
            String[] coords = platformEntries[i].split(",");
            platforms[i] = new Platform(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
        }
        screen.setPlatforms(platforms);

        // 6) Create Hammer
        int hammerCount = Integer.parseInt((props.getProperty("hammer.level2.count")));
        Hammer[] hammers = new Hammer[hammerCount];
        for (int i = 1; i <= hammerCount; i++) {
            String[] hammerPos = props.getProperty("hammer.level2." + i).split(",");
            hammers[i - 1] = new Hammer(Double.parseDouble(hammerPos[0]), Double.parseDouble(hammerPos[1]));
        }
        screen.setHammer(hammers);


        // 7) Create Blaster
        int blasterCount = Integer.parseInt(props.getProperty("blaster.level2.count"));
        Blaster[] blasters = new Blaster[blasterCount];
        for (int i = 1; i <= blasterCount; i++) {
            String[] blasterPos = props.getProperty("blaster.level2." + i).split(",");
            blasters[i - 1] = new Blaster(Double.parseDouble(blasterPos[0]), Double.parseDouble(blasterPos[1]));
        }
        screen.setBlaster(blasters);

        // 8) Create Monkeys (Normal + Intelligent)
        int normalCount = Integer.parseInt(props.getProperty("normalMonkey.level2.count"));
        NormalMonkey[] normalMonkeys = new NormalMonkey[normalCount];
        for (int i = 1; i <= normalCount; i++) {
            String[] parts = props.getProperty("normalMonkey.level2." + i).split(";");
            String[] pos = parts[0].split(",");
            int x = Integer.parseInt(pos[0]);
            int y = Integer.parseInt(pos[1]);
            String direction = parts[1];
            String[] routeStr = parts[2].split(",");
            int[] route = new int[routeStr.length];
            for (int j = 0; j < routeStr.length; j++) {
                route[j] = Integer.parseInt(routeStr[j]);
            }
            normalMonkeys[i - 1] = new NormalMonkey(x, y, direction, route);
        }

        int intelCount = Integer.parseInt(props.getProperty("intelligentMonkey.level2.count"));
        IntelligentMonkey[] intelligentMonkeys = new IntelligentMonkey[intelCount];
        for (int i = 1; i <= intelCount; i++) {
            String[] parts = props.getProperty("intelligentMonkey.level2." + i).split(";");
            String[] pos = parts[0].split(",");
            int x = Integer.parseInt(pos[0]);
            int y = Integer.parseInt(pos[1]);
            String direction = parts[1];
            String[] routeStr = parts[2].split(",");
            int[] route = new int[routeStr.length];
            for (int j = 0; j < routeStr.length; j++) {
                route[j] = Integer.parseInt(routeStr[j]);
            }
            intelligentMonkeys[i - 1] = new IntelligentMonkey(x, y, direction, route);
        }

        // 9) Combine both arrays into one Monkey[]
        Monkey[] allMonkeys = new Monkey[normalCount + intelCount];
        System.arraycopy(normalMonkeys, 0, allMonkeys, 0, normalCount);
        System.arraycopy(intelligentMonkeys, 0, allMonkeys, normalCount, intelCount);

        // 10) Set monkeys to screen
        screen.setMonkeys(allMonkeys);
    }
}