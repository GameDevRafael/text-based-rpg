package core;

import model.character.Player;
import model.world.Location;
import model.world.Point;

import java.io.Serializable;
import java.util.*;

/**
 * NavigationManager is a singleton class that manages the navigation of the player
 */
class NavigationManager implements Serializable {
    private static NavigationManager instance;
    private final Player player;
    private final HashMap<Point, Location> gameMap;
    private final InputHandler inputHandler;

    /**
     * To enforce the singleton pattern the constructor is private
     * @param player the player
     * @param gameMap the game map
     * @param inputHandler the input handler
     */
    private NavigationManager(Player player, HashMap<Point, Location> gameMap, InputHandler inputHandler) {
        this.player = player;
        this.gameMap = gameMap;
        this.inputHandler = inputHandler;
    }

    /**
     * Returns the instance of the NavigationManager class. If the instance is null, a new instance is created.
     * Otherwise, the existing instance is returned.
     *
     * @param player the player character
     * @param gameMap the game map with locations and exits
     * @param inputHandler the input handler
     * @return the instance of the NavigationManager class
     */
    public static NavigationManager getInstance(Player player, HashMap<Point, Location> gameMap, InputHandler inputHandler) {
        if (instance == null) {
            return new NavigationManager(player, gameMap, inputHandler);
        }
        return instance;
    }

    /**
     * This method is used to confirm the direction the player wants to go is valid and updates the player's
     * position accordingly.
     * It confirms the direction is valid by checking if the player's current location has
     * the target position as an exit. If the player's current location is null, it checks if the target position
     * is within the map boundaries.
     * @param direction the direction the player wants to go
     * @return true if the direction is valid, false otherwise
     */
    boolean isValidDirection(String direction) {
        Point pos = player.getPosition();
        Point targetPos = inputHandler.calculateNewPosition(direction, pos);

        if (getLocation(pos) == null) {
            return isWithinMapBoundaries(targetPos);
        }

        Location currentLocation = getLocation(pos);
        return currentLocation.getExits().contains(targetPos);
    }

    /**
     * This method shows the user the possible directions they can take from their current location, based on the exits
     * available in the current location.
     * It gets the exits of the current location and displays the possible directions to the user including the base
     * directions (North, South, East, West) and the elevation directions (Up, Down).
     */
    void showPossibleDirections() {
        List<Point> directions = getLocation(player.getPosition()).getExits();
        List<String> directionsList = new ArrayList<>();

        if(directions.isEmpty()){
            return;
        }

        for (Point direction : directions) {
            String baseDirection = getBaseDirection(direction, player.getPosition());
            String elevation = getElevation(direction, player.getPosition());

            if (baseDirection != null) {
                directionsList.add(elevation != null ? baseDirection + " " + elevation : baseDirection);
            }
        }

        Collections.sort(directionsList);
        System.out.println("Available directions: " + directionsList);
    }

    private boolean isWithinMapBoundaries(Point point) {
        return point.getX() >= MapManager.MIN_X && point.getX() <= MapManager.MAX_X &&
                point.getY() >= MapManager.MIN_Y && point.getY() <= MapManager.MAX_Y;
    }

    private Location getLocation(Point point) {
        return gameMap.get(point);
    }

    private String getBaseDirection(Point target, Point current) {
        if (target.getX() < current.getX()) return "West";
        if (target.getX() > current.getX()) return "East";
        if (target.getY() > current.getY()) return "North";
        if (target.getY() < current.getY()) return "South";
        return null;
    }

    private String getElevation(Point target, Point current) {
        if (target.getZ() > current.getZ()) return "Up";
        if (target.getZ() < current.getZ()) return "Down";
        return null;
    }
}
