package model.character;

import model.item.Gold;
import model.world.Point;

import java.io.Serializable;
import java.util.Random;

/**
 * NPC class is an abstract class that represents a non-playable character in the game.
 * It has a dialogue field which represents the dialogue of the NPC and a canRunAway field which
 * represents whether the player can run away from the NPC.
 */
public abstract class NPC extends Character implements Serializable {
    String dialogue;
    boolean canRunAway;

    /**
     * Constructor for NPC class.
     * @param name name of the NPC
     * @param position position of the NPC
     * @param maxHealth maximum health of the NPC
     * @param damage damage the NPC can deal
     * @param defense defense of the NPC
     * @param canRunAway whether the player can run away from the NPC
     */
    public NPC(String name, Point position, float maxHealth, float damage, float defense,
               boolean canRunAway) {
        super(name, position, maxHealth, damage, defense);
        this.canRunAway = canRunAway;
    }

    Gold getRandomGoldAmount() {
        Random random = new Random();
        int goldAmount = random.nextInt(25);
        return new Gold(goldAmount);
    }

    public boolean canRunAway() {
        return canRunAway;
    }

    public abstract void setupDialogue(String specificType);

    public abstract int getGoldRequired();

    public String getDialogue() {
        return dialogue;
    }

}