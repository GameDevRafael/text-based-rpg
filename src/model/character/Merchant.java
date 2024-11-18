package model.character;

import model.world.Point;

/**
 * Merchant class is a subclass of ShopkeeperNPC class. It represents a merchant NPC in the game.
 */
public class Merchant extends ShopkeeperNPC {

    /**
     * Constructor for Merchant class.
     * @param name name of the merchant
     * @param position position of the merchant
     * @param specificType specific type of the merchant
     */
    public Merchant(String name, Point position, String specificType) {
        super(name, position, 100, 2, 2, true, 15);
        setupDialogue(specificType);
    }

    @Override
    public void setupDialogue(String specificType) {
        switch (specificType) {
            case "village" -> dialogue = "Welcome to my shop!";
            case "forest" -> dialogue = "I have the finest wares in the land!";
            case "swamp" -> dialogue = "Care for some special swamp concoctions?";
            default -> dialogue = "My magical brews await!";
        }
    }

    @Override
    public int getGoldRequired() {
        return 0;
    }
}