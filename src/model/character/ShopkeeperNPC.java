package model.character;

import gameplay.Shop;
import model.item.Weapon;
import model.world.Point;

import java.io.Serializable;

/**
 * ShopkeeperNPC class is an abstract class that represents a shopkeeper NPC in the game.
 * It has a shop field which represents the shop of the shopkeeper.
 */
public abstract class ShopkeeperNPC extends NPC implements Serializable {
    final Shop shop;

    /**
     * Constructor for ShopkeeperNPC class. It initializes the shop of the shopkeeper with a random gold amount.
     * @param name name of the shopkeeper
     * @param position position of the shopkeeper
     * @param maxHealth maximum health of the shopkeeper
     * @param damage damage the shopkeeper can deal
     * @param defense defense of the shopkeeper
     * @param canRunAway whether the player can run away from the shopkeeper
     * @param initialGold initial gold amount of the shopkeeper
     */
    public ShopkeeperNPC(String name, Point position, float maxHealth, float damage, float defense,
                         boolean canRunAway, int initialGold) {
        super(name, position, maxHealth, damage, defense, canRunAway);
        this.inventory.setGoldAmount(initialGold);
        this.shop = new Shop(this, getRandomGoldAmount());
    }

    public void displayShop() {
        shop.displayShop();
    }

    public boolean sellItem(String itemToSell, Player player) {
        if (itemToSell == null) {
            System.out.println("Please enter a valid item to sell.");
            return false;
        }

        if (itemToSell.equalsIgnoreCase("gold")) {
            System.out.println("You cannot sell gold.");
            return false;
        }

        Weapon weaponToSell = (Weapon) player.getItem(itemToSell);
        if (weaponToSell == null) {
            System.out.println("You don't have this item in your inventory.");
            return false;
        }

        shop.sellItem(player, weaponToSell);
        return true;
    }

    public boolean buyItem(String itemToBuy, Player player) {
        if (itemToBuy == null) {
            System.out.println("Please enter a valid item to buy.");
            return false;
        }

        Weapon weapon = shop.getItemByName(itemToBuy);
        if (weapon == null) {
            System.out.println("Item not found in shop.");
            return false;
        }

        shop.buyItem(player, weapon);
        return true;
    }
}
