package core;

import model.character.NPC;
import model.character.Player;
import model.item.Gold;
import model.item.Item;
import model.item.Potion;

import java.io.Serializable;
import java.util.List;

 class InventoryInteraction implements Serializable {
    private static InventoryInteraction instance;
    private final Player player;
    private final InputHandler inputHandler;
    private final List<Item> items;
    private final CombatManager combatManager;

    /**
     * Creates a new InventoryInteraction instance with the specified player, input handler, items and combat manager.
     * @param player the player character
     * @param inputHandler the input handler to read user input
     * @param items the list of items dropped around the map
     * @param combatManager the combat manager to handle combat interactions
     */
    private InventoryInteraction(Player player, InputHandler inputHandler, List<Item> items, CombatManager combatManager)
    {
        this.player = player;
        this.inputHandler = inputHandler;
        this.items = items;
        this.combatManager = combatManager;
    }

    /**
     * Returns the InventoryInteraction instance with the specified player, input handler, items and combat manager.
     * To enforce the singleton pattern, the instance is created only once, if the instance is null, 
     * creates a new InventoryInteraction instance with the specified parameters.
     * @param player the player character
     * @param inputHandler the input handler to read user input
     * @param items the list of items dropped around the map
     * @param combatManager the combat manager to handle combat interactions
     * @return the InventoryInteraction instance
     */
    static InventoryInteraction getInstance(Player player, InputHandler inputHandler, List<Item> items, CombatManager combatManager) {
        if (instance == null) {
            return new InventoryInteraction(player, inputHandler, items, combatManager);
        }
        return instance;
    }

    /**
     * For making a payment to an NPC, checks if the player has enough gold to pay the NPC in order to pass.
     * If the player has enough gold, the player pays the NPC the required gold and the NPC lets the player pass, 
     * otherwise the NPC gets angry and the player has to fight the NPC.
     * @param individual the NPC to make a payment to
     * @return true if the player got out of the situation alive, false otherwise
     */
    boolean handlePayment(NPC individual) {
        int requiredGold = individual.getGoldRequired();

        if (player.getGold() < requiredGold) {
            System.out.println(individual.getName() + ": You don't have enough gold. " +
                    "You have angered me, now prepare to fight!");
            return combatManager.handleCombat(individual);
        }

        player.removeGold(requiredGold);
        individual.addGold(requiredGold);
        System.out.println("You have paid " + individual.getName() + " " +
                requiredGold + " gold. Current gold: " + player.getGold());
        System.out.println(individual.getName() + ": You may pass.");
        return true;
    }

     /**
      * For the user to use a potion from their inventory, the user is prompted to select a potion from their inventory.
      * It confirms that the user has selected a valid potion and then uses the potion.
      */
    void handlePotions() {
        System.out.println("What potion from your inventory would you like to use?");
        System.out.println(player.getPotions());

        String input;

        do {
            input = inputHandler.getUserInput();
            if (isValidPotion(input)) {
                Potion selectedPotion = player.getPotion(input);
                if(player.usePotion(selectedPotion)){
                    return;
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }

        } while (true);
    }

     /**
      * For the user to interact with their inventory, the user is prompted to select an action from their inventory,
      * such as using a potion, removing an item, or exiting the inventory. The user is prompted to choose a number
      * corresponding to the action they would like to take.
      */
    void handleInventoryActions() {
        System.out.println(player.getInventory());

        String[] options = {"Use a potion", "Remove an item", "Exit inventory"};
        System.out.println("What would you like to do?");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        while (true) {
            String input = inputHandler.getUserInput();

            switch (input) {
                case "1":
                    if (player.getPotions().isEmpty()) {
                        System.out.println("You don't have any potions to use.");
                        continue;
                    } else {
                        handlePotions();
                        return;
                    }
                case "2":
                    handleOptionRemoveItem();
                    return;

                case "3":
                    return;

                default:
                    System.out.println("Invalid input. Please enter a number between 1 and " + options.length);
            }
        }
    }

     /**
      * When the user finds an Item on the ground, the user is prompted to pick up the item.
      * @param item the item the user has found
      */
    void handleItemInteraction(Item item) {
        displayItemFoundMessage(item);

        if(player.isInventoryFull()) {
            handleFullInventoryInteraction(item);
        } else{
            handleNormalItemPickup(item);
        }

    }

    private void handleFullInventoryInteraction(Item item) {
        System.out.println("Your inventory is full. Cannot pick up " + item.getName());

        if (inputHandler.getYesNoInput("Would you like to remove an item from your inventory to make space?")) {
            handleMakeSpace();
            player.addItem(item);
            destroyItemOnSpawnPoint(item);
            player.updateStats();
        }
    }

    private void handleMakeSpace() {
        String input;
        System.out.println("Which item would you like to remove? (Can't remove gold)");
        System.out.println(player.getInventory());

        do {
            input = inputHandler.getUserInput();

            if(!input.equals("gold")){
                if (isValidItem(input)) {
                    handleItemRemoval(input);
                    return;
                }
            }

            System.out.println("Invalid input. Please try again.");
        } while (true);
    }

     /**
      * When the user finds an item on the ground, the user is prompted to pick it up. If it's gold, it's added to the
      * player's gold. If it's a regular item, it's added to the player's inventory. Afterward, the item is removed
      * from the list of items on the ground and the player's stats are updated in case the item affects the player's
      * stats.
      * @param item
      */
    private void handleNormalItemPickup(Item item) {
        if (inputHandler.getYesNoInput("Would you like to pick it up?")) {
            if (item instanceof Gold) {
                player.addGold(((Gold) item).getAmount());
                System.out.println("You have picked up " + item.getDescription() +
                        ". Current gold: " + player.getGold());
            } else {
                player.addItem(item);
            }
            destroyItemOnSpawnPoint(item);
            player.updateStats();
        }
    }

    private void displayItemFoundMessage(Item item) {
        if(item instanceof Gold) {
            System.out.println("You have found " + item.getName() + ". " + item.getDescription());
        } else {
            System.out.println("You have found a " + item.getName() + ". " + item.getDescription());
        }
    }

    private void destroyItemOnSpawnPoint(Item item) {
        items.remove(item);
    }

    void handleOptionRemoveItem() {
        while (true) {
            System.out.println("Which item would you like to remove? (exit to cancel)");
            System.out.println(player.getInventory());
            String input = inputHandler.getUserInput();

            if (input.equals("exit")) {
                return;
            }

            if (isValidItem(input)) {
                handleItemRemoval(input);
                return;
            }

            System.out.println("Invalid input. Please try again.");
        }
    }

     boolean isValidPotion(String potionName) {
        for (Potion potion : player.getPotions()) {
            if (potion.getName().toLowerCase().equals(potionName)) {
                return true;
            }
        }
        return false;
    }

     boolean isValidItem(String input) {
        for (Item item : player.getInventory().getItems()) {
            if (item.getName().toLowerCase().equals(input)) {
                return true;
            }
        }
        return false;
    }

    private void handleItemRemoval(String itemName) {
        if (itemName.equals("gold")) {
            handleGoldRemoval();
        } else {
            handleRegularItemRemoval(itemName);
        }
        player.updateStats();
    }

    private void handleGoldRemoval() {
        if(player.getGold() == 0) {
            System.out.println("You have no gold to remove.");
            return;
        }
        Gold gold = new Gold(player.getGold());
        player.removeAllGold();
        System.out.println("You have removed all gold from your inventory.");
        player.dropItem(items, gold);
    }

    private void handleRegularItemRemoval(String itemName) {
        Item item = player.getItem(itemName);
        player.removeItem(item);
        player.dropItem(items, item);
    }


}