package core;

import model.world.Location;
import model.world.Point;

import java.io.Serializable;
import java.util.*;

/**
 * Manages the game map, including the locations and their exits.
 */
public class MapManager implements Serializable {
    private static MapManager instance;
    private final HashMap<Point, Location> gameMap;
    private final Random random;
    public static final int MIN_X = 0;
    public static final int MAX_X = 4;
    public static final int MIN_Y = 0;
    public static final int MAX_Y = 4;

    /**
     * To enforce the singleton pattern, the constructor is private and initializes the game map and a random object.
     */
    private MapManager() {
        this.gameMap = new HashMap<>();
        this.random = new Random();
        initializeMap();
    }

    /**
     * Returns the singleton instance of the MapManager.
     * @return the MapManager instance
     */
    static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    private static final String[] FILLER_DESCRIPTIONS = {
            // Northeast quadrant (closer to the winning location)
            "The path ahead seems to lead towards something important.",
            "You sense the path is guiding you in the right direction.",
            "The trees thin out, revealing a glimpse of what lies ahead.",
            "The air currents shift, as if urging you onward.",
            // Northwest quadrant
            "The forest grows denser, obscuring your view of the way forward.",
            "The path splits, leaving you to choose which way to go.",
            "The familiar surroundings offer little indication of where to head next.",
            "You hear the sounds of the forest, but the path ahead is unclear.",
            // Southwest quadrant
            "The ground becomes more uneven, making the way ahead less certain.",
            "The shadows deepen, casting an air of mystery over the area.",
            "You catch a whiff of something unfamiliar, but it doesn't seem to lead anywhere.",
            "The sounds of the forest fade, leaving you in relative silence.",
            // Southeast quadrant (farthest from the winning location)
            "The path ahead appears overgrown and difficult to traverse.",
            "The trees grow thicker, blocking out much of the sunlight.",
            "Fallen branches litter the ground, hampering your progress.",
            "The air feels stagnant, as if the forest is trapping you in place."
    };




    private void initializeMap() {
        createLocationsAndExits();
        fillEmptySpaces();
    }

    /**
     * Creates the main locations and their exits.
     */
    private void createLocationsAndExits() {
        // Define exits for each location
        List<Point> exitsStart = List.of(new Point(2, 1, 0), new Point(1, 0, 0));
        List<Point> exitsForest1 = List.of(new Point(1, 2, 0), new Point(2, 1, 0),
                new Point(0, 1, 0));
        List<Point> exitsForest2 = List.of(new Point(1,3,0), new Point(0,2,0),
                new Point(0, 4, 0));
        List<Point> exitsForest3 = List.of(new Point(0, 3, 0), new Point(1, 2, 0),
                new Point(2, 3, 0));
        List<Point> exitsForest4 = List.of(new Point(0, 4, 0), new Point(1, 3, 0),
                new Point(2, 4, 0));
        List<Point> exitsSwamp = List.of(new Point(0, 3, 0), new Point(0, 1, 0),
                new Point(1, 2, 0));
        List<Point> exitsCave = List.of(new Point(4, 0, -1), new Point(4, 2, 0));
        List<Point> exitsBridge = List.of(new Point(1, 1, 0), new Point(2, 2, 0));
        List<Point> exitsVillage1 = List.of(new Point(4, 2, 0), new Point(3, 3, 0));
        List<Point> exitsVillage2 = List.of(new Point(4, 3, 0), new Point(3, 4, 0),
                new Point(2, 3, 0));
        List<Point> exitsAbyss = null;
        List<Point> exitsWitchHouse = List.of(new Point(1, 4, 0), new Point(1, 4, -1));
        List<Point> exitsWitchCave = List.of(new Point(0, 4, 0), new Point(1, 4, 0));
        List<Point> exitsRestPoint = List.of(new Point(0, 1, 0), new Point(1, 0, 0));
        List<Point> exitsPrincess = List.of(new Point(4, 3, 0), new Point(3, 4, 0));

        // Define locations
        Location start = new Location("You stand at the beginning of your journey, filled with a sense of " +
                "adventure and curiosity.", "Start", exitsStart);
        Location forest1 = new Location("You find yourself in a dense forest, the trees casting long " +
                "shadows on the path ahead.", "Dark Forest", exitsForest1);
        Location forest2 = new Location("The forest grows thicker, but you can see a faint glimmer of " +
                "light in the distance.", "Deeper Forest", exitsForest2);
        Location forest3 = new Location("The forest seems to be opening up, and you feel a growing sense " +
                "of purpose.", "Clearing Forest", exitsForest3);
        Location forest4 = new Location("The trees part, revealing a breathtaking view of the landscape" +
                " beyond. You sense your goal is within reach.", "Enlightened Forest", exitsForest4);
        Location swamp = new Location("The path leads you into a dark, murky swamp. The way forward is " +
                "unclear. You sense that the key to save the princess is here, guarded by a fearsome ogre.",
                "Foreboding Swamp", exitsSwamp);
        Location cave = new Location("You approach the entrance of a cave, its dark maw beckoning you to " +
                "explore.", "Cave Entrance", exitsCave);
        Location bridge = new Location("You cross an old, weathered bridge, feeling a sense of progress" +
                " in your journey.", "Crossing the Bridge", exitsBridge);
        Location village1 = new Location("You enter a small, bustling village, the residents going about" +
                " their daily lives.", "Village Center", exitsVillage1);
        Location village2 = new Location("The village expands, with more buildings and activity in the " +
                "distance.", "Village Outskirts", exitsVillage2);
        Location abyss = new Location("You stand at the edge of a vast, unforgiving abyss, the depths" +
                " below filled with an unsettling energy.", "Abyss Edge", exitsAbyss);
        Location witchHouse = new Location("You approach a mysterious-looking house, its appearance" +
                " suggesting the presence of a powerful witch.", "Witch's House", exitsWitchHouse);
        Location witchCave = new Location("You delve deeper, entering the hidden cave where the witch" +
                "resides.", "Witch's Cave", exitsWitchCave);
        Location restPoint = new Location("You come across a peaceful clearing, a place where you can" +
                " rest and regain your strength.", "Resting Point", exitsRestPoint);
        Location winPoint = new Location("Congratulations! You have reached the end of your journey." +
                " Insert the key to free the Princess!", "Winning Point", exitsPrincess);

        // Add all main locations to the map
        addLocation(new Point(2, 0, 0), start);
        addLocation(new Point(1, 1, 0), forest1);
        addLocation(new Point(1, 2, 0), forest2);
        addLocation(new Point(0, 3, 0), forest3);
        addLocation(new Point(0, 4, 0), forest4);
        addLocation(new Point(0, 2, 0), swamp);
        addLocation(new Point(4, 1, 0), cave);
        addLocation(new Point(2, 1, 0), bridge);
        addLocation(new Point(3, 2, 0), village1);
        addLocation(new Point(3, 3, 0), village2);
        addLocation(new Point(4, 0, -1), abyss);
        addLocation(new Point(0, 4, 0), witchHouse);
        addLocation(new Point(1, 4, -1), witchCave);
        addLocation(new Point(0, 0, 0), restPoint);
        addLocation(new Point(4, 4, 0), winPoint);
    }

    private void fillEmptySpaces() {
        for (int x = MIN_X; x <= MAX_X; x++) {
            for (int y = MIN_Y; y <= MAX_Y; y++) {
                Point point = new Point(x, y, 0);
                if (!gameMap.containsKey(point)) {
                    addFillerLocation(point);
                }
            }
        }
    }

    /**
     * For each filler location, the description is chosen based on the location's quadrant in the map. As the map is
     * essentially a square, the map is divided into four quadrants. With the winning point being on the top right
     * of the corner, the filler locations closer to the winning point have more positive descriptions, while the
     * filler locations farther away have more negative descriptions.
     * Afterward, the locations are added to the game map.
     * @param point the point at which to add the filler location
     */
    private void addFillerLocation(Point point) {
        List<Point> exits = generateExits(point);
        String description;
        if (point.getX() >= 2 && point.getY() >= 2) { // Northeast quadrant (closer to winning location)
            description = FILLER_DESCRIPTIONS[random.nextInt(4)];
        } else if (point.getX() < 2 && point.getY() >= 2) { // Northwest quadrant
            description = FILLER_DESCRIPTIONS[4 + random.nextInt(4)];
        } else if (point.getX() < 2 && point.getY() < 2) { // Southwest quadrant
            description = FILLER_DESCRIPTIONS[8 + random.nextInt(4)];
        } else { // Southeast quadrant (farthest from winning location)
            description = FILLER_DESCRIPTIONS[12 + random.nextInt(4)];
        }
        Location fillerLocation = new Location(description, "Path", exits);
        gameMap.put(point, fillerLocation);
    }

    /**
     * Generates the exits for a given point. The exits are the points that are adjacent to the given point and are
     * within the bounds of the map.
     * @param point the point for which to generate exits
     * @return a list of valid exits
     */
    private List<Point> generateExits(Point point) {
        List<Point> exits = new ArrayList<>();
        Point[] possibleExits = {
                new Point(point.getX() + 1, point.getY(), point.getZ()),
                new Point(point.getX() - 1, point.getY(), point.getZ()),
                new Point(point.getX(), point.getY() + 1, point.getZ()),
                new Point(point.getX(), point.getY() - 1, point.getZ())
        };

        for (Point exit : possibleExits) {
            if (isValidPoint(exit)) {
                exits.add(exit);
            }
        }
        return exits;
    }

    private boolean isValidPoint(Point point) {
        return point.getX() >= MIN_X && point.getX() <= MAX_X &&
                point.getY() >= MIN_Y && point.getY() <= MAX_Y;
    }

    /**
     * Returns a random spawn point for each ground Item. The spawn point is chosen randomly from the map, excluding the
     * starting point.
     * @return a random spawn point
     */
    Point getRandomSpawnPoint() {
        Point spawnPoint;
        do {
            spawnPoint = new Point(
                    random.nextInt(MAX_X + 1),
                    random.nextInt(MAX_Y + 1),
                    random.nextInt(2) - 1
            );
        } while (!gameMap.containsKey(spawnPoint) && !spawnPoint.equals(new Point(2, 0, 0)));
        return spawnPoint;
    }

    void addLocation(Point point, Location location) {
        gameMap.put(point, location);
    }

    HashMap<Point, Location> getGameMap() {
        return new HashMap<>(gameMap);
    }
}
