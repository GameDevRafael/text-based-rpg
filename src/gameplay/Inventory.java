package gameplay;

import model.item.Gold;
import model.item.Item;
import model.item.Potion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class that holds the player's items and gold. It has a space limit of 5 items.
 */
public class Inventory implements Serializable {
    private final int space = 5;
    private final List<Item> inventory;
    private final Gold gold;

    /**
     * Constructor for Inventory class. Initializes the inventory with a gold object with a set amount of 0 gold coins
     * and an empty list of items with a maximum space of 5 items.
     */
     public Inventory() {
        this.inventory = new ArrayList<>(space);
        this.gold = new Gold(0);
        inventory.add(this.gold);
    }

     public void addItem(Item item) {
        if (inventory.size() < space) {
            inventory.add(item);
        } else {
            System.out.println("Inventory is full. Cannot add " + item.getName());
        }
    }

     public Gold getGold(){
        return gold;
    }

     public int getSpace() {
        return space;
    }


     public List<Item> getItems() {
        return inventory;
    }

     public List<Potion> getPotions(){
        List<Potion> potions = new ArrayList<>();
        for(Item potion : inventory){
            if(potion instanceof Potion){
                potions.add((Potion) potion);
            }
        }
        return potions;
    }

     public Potion getPotion(String specificPotion){
        for (Item potion : inventory){
            if(potion instanceof Potion){
                if (potion.getName().equalsIgnoreCase(specificPotion)){
                    return (Potion) potion;
                }
            }

        }
        return null;
    }

     public void setGoldAmount(int amount) {
        gold.setAmount(amount);
    }

     public void removeItem(Item item){
        if(item != null){
            System.out.println("You have removed " + item.getName() + " from your inventory.");
            inventory.remove(item);
        }

    }

     public String toString() {
        StringBuilder sb = new StringBuilder();

        if (inventory.isEmpty()) {
            sb.append("No items in inventory");
        } else {
            for (Item item : inventory) {
                sb.append(item.getName()).append(": ");
                sb.append(item.getDescription()).append("\n");
            }
        }

        return sb.toString();
    }

}

