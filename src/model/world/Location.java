package model.world;

import java.util.List;
import java.io.Serializable;

/**
 * Represents a location in the game world, each location has a description, name and a list of possible exits.
 */
public class Location implements Serializable{
    private final String description;
    private final String name;
    private final List<Point> exits;

    /**
     * Constructor for a location.
     * @param description the description of the location
     * @param name the name of the location
     * @param exits the list of possible exits from the location
     */
    public Location(String description, String name, List<Point> exits) {
        this.description = description;
        this.name = name;
        this.exits = exits;
    }

    public String toString(){
        return description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public List<Point> getExits() {
        return exits;
    }

    public boolean hasExit(Point point) {
        return exits.contains(point);
    }

}
