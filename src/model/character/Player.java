package model.character;

import model.item.Item;
import model.item.Shield;
import model.item.Sword;
import model.item.Weapon;
import model.world.Point;

import java.io.Serializable;
import java.util.List;

/**
 * Player class is a subclass of Character class. It represents the player in the game.
 */
public class Player extends Character implements Serializable {

    /**
     * Constructor for Player class.
     * @param name name of the player
     * @param position position of the player
     */
    public Player(String name, Point position) {
        super(name, position, 100, 5, 5);
        this.inventory.setGoldAmount(5);
    }

    /**
     * According to the items in the inventory, updates the defense and damage the player can deal.
     */
    public void updateStats() {
        float defensePercentage = this.getDefense();
        float damagePercentage = this.getDamage();

        for (Item item : this.getInventory().getItems()) {
            if (item instanceof Shield) {
                defensePercentage *= (1 + (((Shield) item).getDamageReduced() / 100.0f));
            } else if (item instanceof Sword) {
                damagePercentage *= (1 + (((Sword) item).getDamageBoost() / 100.0f));
            }
        }

        this.setDefense((float) (Math.ceil(defensePercentage * 10) / 10.0f));
        this.setDamage((float) (Math.ceil(damagePercentage * 10) / 10.0f));
    }

    public boolean isInventoryFull() {
        return this.getInventory().getItems().size() == this.getInventory().getSpace();
    }
}
