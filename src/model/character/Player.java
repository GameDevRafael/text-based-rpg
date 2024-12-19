package model.character;

import model.item.Item;
import model.item.Shield;
import model.item.Sword;
import model.world.Point;

import java.io.Serializable;

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
     * According to the item picked up, improves the values of defense and damage the player can deal.
     * @param item item that was picked up
     */
    public void addStats(Item item) {
        float defensePercentage = this.getDefense();
        float damagePercentage = this.getDamage();

        if (item instanceof Shield){
            defensePercentage *= (1 + (((Shield) item).getDamageReduced() / 100.0f));
        } else if (item instanceof Sword){
            damagePercentage *= (1 + (((Sword) item).getDamageBoost() / 100.0f));
        }

        this.setDefense((float) (Math.ceil(defensePercentage * 10) / 10.0f));
        this.setDamage((float) (Math.ceil(damagePercentage * 10) / 10.0f));

    }

    /**
     * According to the item removed, decreases the values of defense and damage the player can deal.
     * @param item item that was removed
     */
    public void removeStats(Item item){
        float defensePercentage = this.getDefense();
        float damagePercentage = this.getDamage();

        if (item instanceof Shield){
                defensePercentage /= (1 + (((Shield) item).getDamageReduced() / 100.0f));
        } else if (item instanceof Sword){
                damagePercentage /= (1 + (((Sword) item).getDamageBoost() / 100.0f));
        }

        this.setDefense((float) (Math.ceil(defensePercentage * 10) / 10.0f));
        this.setDamage((float) (Math.ceil(damagePercentage * 10) / 10.0f));


    }

    public boolean isInventoryFull() {
        return this.getInventory().getItems().size() == this.getInventory().getSpace();
    }
}
