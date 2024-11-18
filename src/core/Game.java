package core;

import model.character.NPC;
import model.character.Player;
import model.item.Item;
import model.world.Location;
import model.world.Point;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 * The Game class is responsible for managing the game logic and the interactions between the player and the NPCs.
 * It handles the game loop, where the player can explore the map, interact with NPCs and manage their inventory.
 * It also checks for win conditions and updates the game state accordingly.
 */
class Game implements Serializable {
    private final List<NPC> individuals;
    private final Player player;
    private boolean quit = false;
    private final HashMap<Point, Location> gameMap;
    private final List<Item> items;
    private final InputHandler inputHandler;
    private final InteractionManager interactionManager;
    private final NavigationManager navigationManager;
    private final InventoryInteraction inventoryInteraction;


    /**
     * Creates a new Game instance with the specified list of NPCs, player character, game map, items and scanner.
     * Initializes the input handler, combat manager, inventory interaction, interaction manager
     * and navigation manager.
     *
     * @param individuals the list of NPCs in the game
     * @param player the player character
     * @param gameMap the game map with locations and exits
     * @param items the list of items dropped around the map
     * @param scanner the scanner object to read user input
     */
    Game(List<NPC> individuals, Player player, HashMap<Point, Location> gameMap, List<Item> items,
                Scanner scanner) {
        this.individuals = individuals;
        this.gameMap = gameMap;
        this.player = player;
        this.items = items;
        this.inputHandler = InputHandler.getInstance(scanner);
        CombatManager combatManager = CombatManager.getInstance(player, individuals, items);
        this.inventoryInteraction = InventoryInteraction.getInstance(player, inputHandler, items, combatManager);
        this.interactionManager = InteractionManager.getInstance(player, inputHandler, inventoryInteraction,
                combatManager);
        this.navigationManager = NavigationManager.getInstance(player, gameMap, inputHandler);
    }

    /**
     * Starts the game loop, where the player can play the game.
     * The game loop continues until the player's health reaches 0 or the player decides to quit the game.
     *
     * @return true if the player is still alive and has not quit the game, false otherwise
     */
    boolean playTurn() {
        if(!compareLocations()){
            return false;
        }

        if (player.getHealth() > 0 && !quit) {
            if (checkForInteractions()) {
                return false;
            }
            return handleInput();
        }
        return false;
    }

    private boolean handleInput() {
        System.out.println("What would you like to do? (explore / inventory / quit)");

        while (true) {
            String input = inputHandler.getUserInput();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                quit = true;
                break;
            } else if (input.equalsIgnoreCase("inventory")) {
                inventoryInteraction.handleInventoryActions();
                break;
            } else if (input.equalsIgnoreCase("explore")) {
                return handleExplore();
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
        return true;
    }


    /**
     * Handles the player's exploration of the game map. The player can only move in the directions that are available
     * from their current location. The player can move to a new location by entering the direction they want to go and
     * the game checks if the direction is valid before moving the player.
     * @return true if the player is still alive (some locations may have no exits), false otherwise
     */
    private boolean handleExplore() {
        String input;
        do {
            System.out.println("You may only go in the following directions: ");
            navigationManager.showPossibleDirections();

            input = inputHandler.getUserInput();
            if (navigationManager.isValidDirection(input)) {
                return handleDirection(input);
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        } while (true);
    }


    /**
     * Compares the player's current location with the game map and displays the location description.
     * It also checks if the player is at the winning location and has the key to free the princess (win the game)
     * If there are NPCs present at the location, it displays the NPCs' names.
     * @return true if the game continues because the player doesn't have the key or hasn't reached the princess yet,
     * false otherwise.
     */
    boolean compareLocations() {
        Point playerPos = player.getPosition();
        Location currentLocation = gameMap.get(playerPos);

        if (currentLocation != null) {
            List<NPC> npcsPresent = checkForIndividuals(playerPos);
            if (!npcsPresent.isEmpty()) {
                String npcNames = npcsPresent.stream()
                        .map(NPC::getName)
                        .collect(Collectors.joining(", "));
                System.out.println(currentLocation + " You're accompanied by: " + npcNames);
            } else {
                System.out.println(currentLocation);
            }
        }

        if(playerPos.equals(new Point(4, 4, 0))){
            if(player.getItem("Key") != null){
                System.out.println("You have freed the princess! You win!");
                return false;
            } else if(player.getItem("Key") == null){
                System.out.println("You need a key to free the princess!");
                return true;
            }

        }

        return true;
    }

    /**
     * Checks for interactions at the player's current location whether it's with NPCs or items.
     * If the player's health reaches 0 due to any of the NPC interactions, the game is over.
     * @return true if the player's health is less or equal to 0, false otherwise
     */
    boolean checkForInteractions() {
        List<NPC> npcs = checkForIndividuals(player.getPosition());
        for (NPC npc : npcs) {
            if (npc != null && !interactionManager.handleNPCInteraction(npc)) {
                return true;
            }
        }

        List<Item> locationItems = checkForItems(player.getPosition());
        for (Item item : locationItems) {
            if (item != null) {
                interactionManager.handleItemInteraction(item);
            }
        }


        return player.getHealth() <= 0;
    }

    /**
     * Handles the player's movement in the specified direction. The player's new position is calculated based on the
     * direction they want to move in. If the new position has no exits, the player's health is set to 0 and
     * the game is over.
     * @param direction the direction the player wants to move in
     * @return true if the player is still alive and the game continues, false otherwise
     */
    private boolean handleDirection(String direction) {
        Point newPosition = inputHandler.calculateNewPosition(direction, player.getPosition());
        player.setPosition(newPosition);

        Location currentLocation = gameMap.get(newPosition);
        if (currentLocation != null && (currentLocation.getExits() == null || currentLocation.getExits().isEmpty())) {
            player.setHealth(0);
            System.out.println(currentLocation);
            System.out.println("There are no exits. You have died!");
            return false;
        }
        return true;
    }

    List<NPC> checkForIndividuals(Point point) {
        return individuals.stream()
                .filter(individual -> individual.getPosition().equals(point))
                .collect(Collectors.toList());
    }

    List<Item> checkForItems(Point point) {
        return items.stream()
                .filter(item -> item.getSpawnPoint().equals(point))
                .collect(Collectors.toList());
    }

}
