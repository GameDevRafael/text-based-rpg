package model.character;

import model.world.Point;

import java.io.Serializable;

/**
 * Goblin class is a subclass of NPC class. It represents a goblin NPC in the game.
 * It has a goldRequired field which represents the amount of gold required to pass the goblin.
 */
public class Goblin extends NPC implements Serializable {
    private final int goldRequired;

    /**
     * Constructor for Goblin class.
     * @param name name of the goblin
     * @param position position of the goblin
     * @param specificType specific type of the goblin
     * @param goldRequired gold required to pass the goblin
     */
    public Goblin(String name, Point position, String specificType, int goldRequired) {
        super(name, position,  50, 7, 4, true);
        this.inventory.setGoldAmount(5);
        this.goldRequired = goldRequired;
        setupDialogue(specificType);
    }

    @Override
    public void setupDialogue(String specificType) {
        switch (specificType) {
            case "bridge" -> dialogue = "I'm guarding this bridge. You can't pass... Unless you pay me " +
                    goldRequired + " gold.";
            case "village" -> dialogue = "I'm guarding this village. You can't pass without paying " +
                    goldRequired + " gold.";
            default -> dialogue = "You shall not pass without paying " + goldRequired + " gold!";
        }
    }

    @Override
    public int getGoldRequired() {
        return goldRequired;
    }


}