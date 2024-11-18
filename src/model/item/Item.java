package model.item;

import model.world.Point;

import java.io.Serializable;

/**
 * Item is a class that represents an item in the game. It has a name and a description and a spawn point variable to
 * determine where the item is located.
 */
public class Item implements Serializable {
    private final String name;
    private final String description;
    private Point spawnPoint;

    /**
     * Constructor for Item, based on the name and description.
     * @param name name of the item
     * @param description description of the item
     */
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Point spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

}
