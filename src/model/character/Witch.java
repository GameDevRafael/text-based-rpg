package model.character;

import model.world.Point;

/**
 * Witch class is a subclass of ShopkeeperNPC class. It represents a witch NPC in the game.
 */
public class Witch extends ShopkeeperNPC {
    /**
     * Constructor for Witch class.
     * @param name name of the witch
     * @param position position of the witch
     * @param specificType specific type of the witch
     */
    public Witch(String name, Point position, String specificType) {
        super(name, position, 100, 10, 5, true, 15);
        setupDialogue(specificType);
    }

    @Override
    public void setupDialogue(String specificType) {
        switch (specificType) {
            case "house" -> dialogue = "Oh, you must be lost. But if you insist on staying, my potions might have " +
                    "a word with you.";
            case "forest" -> dialogue = "Wandering in my forest, are we? Just know, my curses are quite persuasive.";
            default -> dialogue = "Feel free to browse, but remember, my magical brews don't take kindly to thieves.";
        }
    }

    @Override
    public int getGoldRequired() {
        return 0;
    }
}