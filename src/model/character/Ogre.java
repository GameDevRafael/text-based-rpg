package model.character;

import model.world.Point;

import java.io.Serializable;

/**
 * Ogre class is a subclass of NPC class. It represents an ogre NPC in the game.
 */
public class Ogre extends NPC implements Serializable {
    /**
     * Constructor for Ogre class.
     * @param name name of the ogre
     * @param position position of the ogre
     * @param specificType specific type of the ogre
     */
    public Ogre(String name, Point position, String specificType) {
        super(name, position, 100, 15, 15, false);
        setupDialogue(specificType);
        this.inventory.setGoldAmount(0);
    }

    @Override
    public void setupDialogue(String specificType) {
        switch (specificType) {
            case "swamp" -> dialogue = "GET OUTTA MY SWAMP!!!!";
            case "cave" -> dialogue = "You dare enter my cave?";
            default -> dialogue = "You will not leave here alive!";
        }
    }

    @Override
    public int getGoldRequired() {
        return 0;
    }
}