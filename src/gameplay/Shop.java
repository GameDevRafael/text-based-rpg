package gameplay;

import model.character.Merchant;
import model.character.NPC;
import model.character.Player;
import model.character.Witch;
import model.item.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;

/**
 * Shop class that represents the shop in the shopkeeper NPCs, where the player can buy and sell items.
 * Each shop has a list of weapons and a gold object.
 */
public class Shop implements Serializable {
    private final List<Weapon> inventory = new ArrayList<>();
    private final Gold gold;

    /**
     * Constructor for the Shop class, which initializes the shop with a list of weapons and a gold object set with the
     * given amount of gold set up by the NPC.
     * @param owner NPC that owns the shop
     * @param gold Gold object with the amount of gold set up by the NPC
     */
    public Shop(NPC owner, Gold gold) {
        this.gold = gold;
        setupShop(owner);
    }

    /**
     * Sets up the shop with a list of weapons that the NPC can sell and then sets up the shop with a max
     * of 3 Items to sell and may have an empty slot given at random. The items are shuffled before being added to the
     * shop.
     * @param owner NPC that owns the shop
     */
    private void setupShop(NPC owner) {
        List<Weapon> merchantShop = new ArrayList<>(Arrays.asList(
                ItemFactory.createWeapon("Shield", 1, "Wooden Shield",
                        "A wooden shield"),
                ItemFactory.createWeapon("Shield", 2, "Iron Shield",
                        "An iron shield"),
                ItemFactory.createWeapon("Sword", 1, "Wooden Sword",
                        "A wooden sword"),
                ItemFactory.createWeapon("Sword", 2, "Iron Sword",
                        "An iron sword"),
                ItemFactory.createPotion("HealthPotion", 1, "Health Potion",
                        "A health potion"),
                ItemFactory.createPotion("DefensePotion", 1, "Defense Potion",
                        "A defense potion"),
                ItemFactory.createPotion("StrengthPotion", 1, "Strength Potion",
                        "A strength potion")
        ));

        List<Potion> witchShop = new ArrayList<>(Arrays.asList(
                ItemFactory.createPotion("HealthPotion", 1, "Health Potion",
                        "A health potion"),
                ItemFactory.createPotion("DefensePotion", 1, "Defense Potion",
                        "A defense potion"),
                ItemFactory.createPotion("StrengthPotion", 1, "Strength Potion",
                        "A strength potion"),
                ItemFactory.createPotion("HealthPotion", 2, "Greater Health Potion",
                        "A greater health potion"),
                ItemFactory.createPotion("DefensePotion", 2, "Greater Defense Potion",
                        "A greater defense potion"),
                ItemFactory.createPotion("StrengthPotion", 2, "Greater Strength Potion",
                        "A greater strength potion")
        ));

        List<Weapon> shopItems = new ArrayList<>();

        if (owner instanceof Merchant) {
            shopItems.addAll(merchantShop);
        } else if (owner instanceof Witch) {
            shopItems.addAll(witchShop);
        }

        Random random = new Random();
        int hasEmptySlot = random.nextInt(2);
        shuffle(shopItems);

        int maxItems = 3;
        for (int i = 0; i <= maxItems; i++) {
            if (hasEmptySlot == 1 && i == maxItems) {
                this.inventory.add(null);
                break;
            }

            if (shopItems.get(i) != null && i != maxItems) {
                this.inventory.add(shopItems.get(i));
            }
        }
    }

    public void displayShop() {
        System.out.println("The shop has " + gold.getDescription() + ". Items available: ");
        for (Weapon weapon : inventory) {
            System.out.println(weapon.getName() + " - " + weapon.getPrice() + " gold");
        }
    }

    /**
     * Buys an item from the shop and adds it to the player's inventory if the player has enough gold to buy the item
     * and enough space to hold it.
     * @param player Player that is buying the item
     * @param weapon Weapon that the player is buying
     */
    public void buyItem(Player player, Weapon weapon) {
        if (!inventory.contains(weapon)) {
            System.out.println("This item is not available in the shop.");
            return;
        }

        if (player.getGold() >= weapon.getPrice()) {
            player.removeGold(weapon.getPrice());
            player.addItem(weapon);
            inventory.remove(weapon);
            gold.addGold(weapon.getPrice());
            System.out.println("You bought " + weapon.getName() + " for " + weapon.getPrice() + " gold.");
        } else {
            System.out.println("You do not have enough gold to buy " + weapon.getName());
        }
    }

    /**
     * Sells an item to the shop and adds the gold to the player's inventory if the player has the item to sell and the
     * shop has enough gold to buy the item.
     * @param player Player that is selling the item
     * @param weapon Weapon that the player is selling
     */
    public void sellItem(Player player, Weapon weapon) {
        if (player.getItem(weapon.getName()) == null) {
            System.out.println("You don't have this item to sell.");
            return;
        }

        if (gold.getAmount() >= weapon.getPrice()) {
            player.removeItem(weapon);
            player.addGold(weapon.getPrice());
            inventory.add(weapon);
            gold.removeGold(weapon.getPrice());
            System.out.println("You sold " + weapon.getName() + " for " + weapon.getPrice() + " gold.");
        } else {
            System.out.println("The shop does not have enough gold to buy " + weapon.getName());
        }
    }

    public Weapon getItemByName(String itemToSell) {
        for (Weapon item : inventory) {
            if (item.getName().equalsIgnoreCase(itemToSell)) {
                return item;
            }
        }
        return null;
    }
}
