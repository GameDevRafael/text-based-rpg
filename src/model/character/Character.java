package model.character;

import gameplay.Inventory;
import model.item.Gold;
import model.item.Item;
import model.item.Potion;
import model.item.Weapon;
import model.world.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a character in the game, each character has a name, position, health, damage, defense and inventory.
 */
public class Character implements Serializable {
    private final float maxHealth;
    private float currentHealth;
    private float damage;
    private float defense;
    final Inventory inventory;
    private final String name;
    private Point position;
    private Point previousPosition;

    /**
     * Creates a new character with the given name, position, max health, damage and defense.
     * @param name the name of the character
     * @param position the position of the character
     * @param maxHealth the maximum health of the character
     * @param damage the damage the character can deal
     * @param defense the defense of the character
     */
    public Character(String name, Point position, float maxHealth, float damage, float defense) {
        this.name = name;
        this.position = position;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage = damage;
        this.defense = defense;
        this.inventory = new Inventory();
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.previousPosition = this.position;
        this.position = position;
    }

    public List<Potion> getPotions(){
        return inventory.getPotions();
    }

    public Potion getPotion(String specificPotion){
        return inventory.getPotion(specificPotion);
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Point getPreviousPosition() {
        return previousPosition;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDefense() {
        return defense;
    }

    public void setDefense(float defense) {
        this.defense = defense;
    }

    public float getHealth() {
        return currentHealth;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void setHealth(float health) {
        this.currentHealth = health;
    }

    public void addGold(int amount) {
        inventory.getGold().addGold(amount);
    }

    public void removeGold(int amount) {
        inventory.getGold().removeGold(amount);
    }

    public void removeAllGold() {
        inventory.getGold().removeAllGold();
    }

    public int getGold() {
        return inventory.getGold().getAmount();
    }


    public List<Weapon> getWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        for (Item item : inventory.getItems()) {
            if (item instanceof Weapon) {
                weapons.add((Weapon) item);
            }
        }
        return weapons;
    }

    /**
     * Adds an item to the inventory. If the item is a weapon, it will check if the character already has a weapon of
     * the same type, so it can check if it's better, worse or the same to decide what to do.
     * @param item the item to add to the inventory
     */
    public void addItem(Item item) {
        if (item instanceof Weapon) {
            addOrUpgradeWeapon((Weapon) item);
        } else {
            inventory.addItem(item);
            System.out.println("You have picked up " + item.getName());
        }
    }

    public void removeItem(Item item) {
        inventory.removeItem(item);
    }

    /**
     * Adds a weapon to the inventory. If the character already has a weapon of the same type, it will check if the new
     * weapon is better, worse or the same to decide what to do.
     * If better, it will remove the old weapon and add the new one.
     * If worse, it will not add the new weapon.
     * If the same, it will not add the new weapon.
     * @param newWeapon the weapon to add to the inventory
     */
    private void addOrUpgradeWeapon(Weapon newWeapon) {
        Weapon existingWeapon = getWeaponOfSameType(newWeapon);
        if (existingWeapon != null) {
            if (existingWeapon.getTier() >= newWeapon.getTier()) {
                System.out.println("You already have a better " + existingWeapon.getName() +
                        " (Tier " + existingWeapon.getTier() + ") in your inventory.");
            } else {
                removeItem(existingWeapon);
                inventory.addItem(newWeapon);
                System.out.println("You have upgraded your " + existingWeapon.getName() +
                        " from Tier " + existingWeapon.getTier() + " to " +
                        newWeapon.getName() + " Tier " + newWeapon.getTier());
            }
        } else {
            inventory.addItem(newWeapon);
            System.out.println("You have picked up " + newWeapon.getName() +
                    " (Tier " + newWeapon.getTier() + ")");
        }
    }

    private Weapon getWeaponOfSameType(Weapon newWeapon) {
        for (Weapon existingWeapon : getWeapons()) {
            if (existingWeapon.getClass().getName().equals(newWeapon.getClass().getName())) {
                return existingWeapon;
            }
        }
        return null;
    }

    /**
     * Uses a potion from the inventory. If the potion is null, it will print a message saying the character doesn't
     * have that potion in the inventory.
     * @param potion the potion to use
     * @return true if the potion was used, false otherwise
     */
    public boolean usePotion(Potion potion) {
        if (potion != null) {
            switch (potion.getSpecialAbility()) {
                case "heal":
                    float healAmount = potion.getHeal();
                    currentHealth = Math.min(maxHealth, currentHealth + healAmount);
                    System.out.println("You used a " + potion.getName() + " and healed for " + healAmount + " health.");
                    break;
                case "strength":
                    float strengthIncrease = potion.getStrengthIncrease();
                    damage += strengthIncrease;
                    System.out.println("You used a " + potion.getName() + " and increased your damage by " +
                            strengthIncrease + ".");
                    break;
                case "defense":
                    float defenseIncrease = potion.getDefenseIncrease();
                    defense += defenseIncrease;
                    System.out.println("You used a " + potion.getName() + " and increased your defense by " +
                            defenseIncrease + ".");
                    break;
                default:
                    System.out.println("This potion has an unknown effect.");
            }
            inventory.removeItem(potion);
            return true;
        } else {
            System.out.println("You don't have that potion in your inventory.");
        }
        return false;
    }

    public Item getItem(String input) {
        for(Item item : inventory.getItems()){
            if(item.getName().equalsIgnoreCase(input)){
                return item;
            }
        }
        return null;
    }

    public void dropItem( List<Item> items, Item item) {
        items.add(item);
        item.setSpawnPoint(this.getPosition());
        System.out.println("The " + (item instanceof Gold ? "gold" : "item") + " has been placed back on the ground");
    }

    public void dropEnemyLoot(List<Item> items, Item item){
        if(item instanceof Gold){
            if(((Gold) item).getAmount() <= 0){
                return;
            }
        }
        items.add(item);
        item.setSpawnPoint(this.getPosition());
        System.out.println("The enemy dropped " + item.getName() + " on the ground.");
    }


}