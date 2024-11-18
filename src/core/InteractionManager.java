package core;

import model.character.Merchant;
import model.character.NPC;
import model.character.Player;
import model.character.ShopkeeperNPC;
import model.item.Item;
import model.world.Point;

import java.io.Serializable;

/**
 * Manages all interactions between the player and NPCs and Items.
 * This class follows the Singleton pattern to ensure only one interaction manager exists.
 * It coordinates combat, inventory, and dialogue interactions throughout the game.
 *
 * @see CombatManager
 * @see InventoryInteraction
 * @see InputHandler
 */
class InteractionManager implements Serializable {
    private static InteractionManager instance;
    private final Player player;
    private final CombatManager combatManager;
    private final InputHandler inputHandler;
    private final InventoryInteraction inventoryInteraction;

    /**
     * Private constructor to enforce Singleton pattern.
     *
     * @param player The game's player instance
     * @param inputHandler Handler for processing user input
     * @param inventoryInteraction Manager for inventory-related interactions
     * @param combatManager Manager for combat-related interactions
     */
    private InteractionManager(Player player, InputHandler inputHandler, InventoryInteraction inventoryInteraction,
                               CombatManager combatManager) {
        this.player = player;
        this.inputHandler = inputHandler;
        this.combatManager = combatManager;
        this.inventoryInteraction = inventoryInteraction;
    }

    /**
     * Gets the singleton instance of InteractionManager, creating it if it doesn't exist.
     *
     * @param player The game's player instance
     * @param inputHandler Handler for processing user input
     * @param inventoryInteraction Manager for inventory-related interactions
     * @param combatManager Manager for combat-related interactions
     * @return The singleton instance of InteractionManager
     */
    static InteractionManager getInstance(Player player, InputHandler inputHandler, InventoryInteraction inventoryInteraction,
                                                 CombatManager combatManager) {
        if (instance == null) {
            return new InteractionManager(player, inputHandler, inventoryInteraction, combatManager);
        }
        return instance;
    }

    /**
     * Handles user input during NPC interactions, providing options like talk, fight, run or do nothing.
     *
     * @param individual The NPC being interacted with
     * @param canPayOption Whether the pay option is available for this interaction
     * @param canRun Whether the player can run from this interaction
     * @return true if the player is alive after the interaction, false otherwise
     */
    private boolean handleUserInput(NPC individual, boolean canPayOption, boolean canRun) {
        boolean canDoNothing = false;
        while (true) {
            String options = canRun ? "talk / fight / run" : "talk / fight";
            if (individual instanceof Merchant) {
                canDoNothing = true;
                options += " / nothing";
            }
            System.out.println("What would you like to do? (" + options + ")");

            switch (inputHandler.getUserInput()) {
                case "talk":
                    showNPCDialogue(individual);
                    return handlePostDialogue(individual, canPayOption, canRun);
                case "fight":
                    return combatManager.handleCombat(individual);
                case "run":
                    if (canRun) {
                        return handleRunAttempt(individual);
                    }
                case "nothing":
                    if(canDoNothing){
                        return true;
                    }
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private boolean handleRunAttempt(NPC individual) {
        if (!individual.canRunAway()) {
            System.out.println("You cannot run from " + individual.getName() + ". Prepare to fight!");
            return combatManager.handleCombat(individual);
        }
        handleRunAway(individual);
        return true;
    }

    boolean handleNPCInteraction(NPC individual) {
        boolean canPayOption = individual.getGoldRequired() > 0;
        boolean canRun = player.getPreviousPosition() != null;
        return handleUserInput(individual, canPayOption, canRun);
    }

    /**
     * If the player wants to talk to an NPC after the dialogue, this method handles the post-dialogue interaction.
     * If the NPC is a shopkeeper, the player can buy or sell items. Otherwise, the player can pay, fight, or run.
     *
     * @param individual The NPC being interacted with
     * @param canPayOption Whether the pay option is available
     * @param canRun Whether running is possible
     * @return true if the player got out of the interaction alive, false otherwise
     */
    private boolean handlePostDialogue(NPC individual, boolean canPayOption, boolean canRun) {
        if (individual instanceof ShopkeeperNPC) {
            return handleShopKeeperInteraction((ShopkeeperNPC) individual, canRun);
        }

        if (canPayOption) {
            if (canRun) {
                System.out.println("What would you like to do? (pay / fight / run)");
            } else {
                System.out.println("What would you like to do? (pay / fight)");
            }
        } else {
            if (canRun) {
                System.out.println("What would you like to do? (fight / run)");
            } else {
                System.out.println("What would you like to do? (fight)");
            }
        }

        while (true) {
            String input = inputHandler.getUserInput();

            if (input.equals("pay") && canPayOption) {
                return inventoryInteraction.handlePayment(individual);
            }

            switch (input) {
                case "fight":
                    return combatManager.handleCombat(individual);
                case "run":
                    if(canRun){
                        return handleRunAttempt(individual);
                    }
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Handles interactions with shopkeeper NPCs, including buying and selling items.
     *
     * @param shopkeeperNPC The shopkeeper being interacted with
     * @param canRun Whether the player can run from this interaction
     * @return true if the player got out of the interaction alive, false otherwise
     */
    private boolean handleShopKeeperInteraction(ShopkeeperNPC shopkeeperNPC, boolean canRun) {
        String options = canRun ? "buy / sell / fight / run" : "buy / sell / fight";
        System.out.println("What would you like to do? (" + options + "). Type 'exit' to leave.");
        String input;
        do{
            input = inputHandler.getUserInput();
            switch (input) {
                case "buy":
                    handleBuyAction(shopkeeperNPC);
                    System.out.println("What would you like to do? (" + options + "). Type 'exit' to leave.");
                    break;
                case "sell":
                    handleSellAction(shopkeeperNPC);
                    System.out.println("What would you like to do? (" + options + "). Type 'exit' to leave.");
                    break;
                case "fight":
                    return combatManager.handleCombat(shopkeeperNPC);
                case "run":
                    if (canRun) {
                        handleRunAway(shopkeeperNPC);
                        return true;
                    }
                case "exit":
                    return true;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while(true);
    }

    private void showNPCDialogue(NPC individual) {
        System.out.println(individual.getName() + ": " + individual.getDialogue());
    }

    private void handleBuyAction(ShopkeeperNPC shopkeeperNPC) {
        shopkeeperNPC.displayShop();
        System.out.println("Current gold: " + player.getGold());
        System.out.println("What would you like to buy? (exit to cancel)");

        String itemToBuy = inputHandler.getUserInput();
        do{
            if(itemToBuy.equals("exit")){
                System.out.println("You have exited the shop.");
                break;
            } else if(shopkeeperNPC.buyItem(itemToBuy, player)){
                break;
            }
        }while(true);
    }

    private void handleSellAction(ShopkeeperNPC shopkeeperNPC) {
        System.out.println(player.getInventory());
        System.out.println("What would you like to sell? (exit to cancel)");

        while (true) {
            String itemToSell = inputHandler.getUserInput();

            if (itemToSell.equals("exit")) {
                System.out.println("You have exited the shop.");
                return;
            }

            if (shopkeeperNPC.sellItem(itemToSell, player)) {
                return;
            }
        }
    }

    private void handleRunAway(NPC enemy) {
        System.out.println("You ran away from the " + enemy.getName());
        player.setPosition(new Point(player.getPreviousPosition().getX(),
                player.getPreviousPosition().getY(), player.getPreviousPosition().getZ()));
        System.out.println("You are back at your previous location.");
    }

    void handleItemInteraction(Item item) {
        inventoryInteraction.handleItemInteraction(item);
    }
}