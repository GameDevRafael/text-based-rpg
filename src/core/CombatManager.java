package core;

import model.character.NPC;
import model.character.Player;
import model.character.Character;
import model.item.Item;
import model.item.Potion;
import model.item.Weapon;

import java.io.Serializable;
import java.util.List;

/**
 * The CombatManager class is responsible for managing the combat between the player and the enemy NPCs. It handles the
 * combat loop, where the player and the enemy take turns attacking each other until one of them is defeated. It also
 * updates the durability of the player's items after the combat and drops their loot when defeated.
 */
class CombatManager implements Serializable {
    private static CombatManager instance;
    private final Player player;
    private final List<NPC> individuals;
    private final List<Item> items;

    private CombatManager(Player player, List<NPC> individuals, List<Item> items) {
        this.player = player;
        this.individuals = individuals;
        this.items = items;
    }

    /**
     * Returns the instance of the CombatManager class. If the instance is null, a new instance is created.
     * Otherwise, the existing instance is returned.
     *
     * @param player the player character
     * @param individuals the list of NPCs
     * @param items the list of items dropped around the map
     * @return the instance of the CombatManager class
     */
     static CombatManager getInstance(Player player, List<NPC> individuals, List<Item> items) {
        if (instance == null) {
            return new CombatManager(player, individuals, items);
        }
        return instance;
    }


    /**
     * Handles the combat between the player and the specified enemy NPC. This method manages the combat loop,
     * where the player and the enemy take turns attacking each other until one of them is defeated. It also updates
     * the durability of the player's items after the combat.
     *
     * @param enemy the NPC that the player is fighting against
     * @return true if the player is still alive after the combat, false if the player is defeated
     */
     boolean handleCombat(NPC enemy) {
        System.out.println("You are fighting " + enemy.getName());
        displayCombatStats(enemy);


        while (player.isAlive() && enemy.isAlive()) {
            takeDamage(enemy, player.getDamage());

            if (!player.isAlive()) {
                handlePlayerDefeat(enemy);
                return false;
            }

            takeDamage(player, enemy.getDamage());

            if (!enemy.isAlive()) {
                handleEnemyDefeat(enemy);
                dropNPCItems(enemy);
                return true;
            }
        }
        updateItemDurability();
        handlePlayerDefeat(enemy);
        return false;
    }

    private void dropNPCItems(NPC enemy) {
        for(Item item : enemy.getInventory().getItems()){
            enemy.dropEnemyLoot(items, item);
        }
    }

    private void updateItemDurability() {
        List<Weapon> weapons = player.getWeapons();
        weapons.forEach(weapon -> {
            weapon.updateDurability();
            if (weapon.getDurability() == 0) {
                player.getInventory().removeItem(weapon);
                System.out.println(weapon.getName() + " has broken and has been removed from your inventory.");
            }
        });

        List<Potion> potions = player.getPotions();
        potions.forEach(potion -> {
            potion.updateDurability();
            if (potion.getDurability() == 0) {
                potion.revertEffects(player);
                player.getInventory().removeItem(potion);
                System.out.println(potion.getName() + " has worn off and has been removed from your inventory.");
            }
        });
    }


    /**
     * Inflicts damage on the specified character. The damage is calculated based on the character's defense and on the
     * amount of damage that the character is supposed to take. The character's health is then updated based on the
     * amount of damage taken. If the character's health drops to 0 or below, the character is considered dead.
     *
     * @param character the character that is taking damage
     * @param amount the amount of damage that the character is supposed to take
     */
     void takeDamage(Character character, float amount) {
        float playerDefensePercentage = character.getDefense() / 100.0f;
        float playerReducedDamage = amount * (1 - playerDefensePercentage);

        float playerCurrentHealth = character.getHealth();
        float playerNewHealth = Math.max(0, playerCurrentHealth - playerReducedDamage);
        character.setHealth(playerNewHealth);

        System.out.println(character.getName() + " has taken " + playerReducedDamage + " damage." +
                " Remaining health: " + character.getHealth());
    }


    private void displayCombatStats(NPC enemy) {
        System.out.println(enemy.getName() + " has " + enemy.getHealth() + " health, " +
                enemy.getDamage() + " damage, and " + enemy.getDefense() + " defense.");
        System.out.println("You have " + player.getHealth() + " health, " + player.getDamage() +
                " damage, and " + player.getDefense() + " defense.");
    }

    private void handlePlayerDefeat(NPC enemy) {
        System.out.println("\nYou have been defeated by " + enemy.getName());
    }

    private void handleEnemyDefeat(NPC enemy) {
        System.out.println("\nYou have defeated " + enemy.getName() + ". You may pass.");
        destroyNPCOnSpawnPoint(enemy);
    }

    private void destroyNPCOnSpawnPoint(NPC npc) {
        individuals.remove(npc);
    }


}