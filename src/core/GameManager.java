package core;

import model.character.*;
import model.item.*;
import model.world.Point;
import persistence.GameState;
import persistence.GameStateManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/**
 * The GameManager class is responsible for managing the game state and the game loop. It handles the setup of the game,
 * including creating the player, NPCs, and items, and starting the game loop. It also provides the option to save and
 * load the game state.
 */
class GameManager implements Serializable {
    private Game game;
    private final Scanner scanner;
    private final MapManager mapManager;
    private List<Item> items;
    private List<NPC> individuals;
    private Player player;
    private static final String SAVE_FILE = "save_game.dat";

    /**
     * Creates a new GameManager instance with a scanner and a map manager. It then shows the start menu to the player.
     */
    GameManager() {
        this.scanner = new Scanner(System.in);
        this.mapManager = MapManager.getInstance();
        showStartMenu();
    }

    /**
     * Shows the start menu to the player, where they can choose to start a new game, load a saved game, or exit the
     * game. If the player chooses to start a new game, the game setup is called. If the player chooses to load a saved
     * game, the game is loaded from the save file. If the player chooses to exit the game, the program is terminated.
     */
    private void showStartMenu() {
        while (true) {
            System.out.println("\n=== Welcome to my RPG text based game ===");
            System.out.println("1. New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Exit");
            System.out.print("Please choose an option (1,2,3): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    setupGame();
                    startGameLoop();
                    return;
                case "2":
                    if (loadGame(SAVE_FILE)) {
                        startGameLoop();
                    } else {
                        System.out.println("Starting new game instead...");
                        setupGame();
                        startGameLoop();
                    }
                    return;
                case "3":
                    System.out.println("Thanks for playing!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void setupGame() {
        individuals = createNPCs();
        items = createItems();
        player = NPCFactory.createPlayer("Hero", new Point(2, 0, 0));

        for (Item item : items) {
            if(item.getSpawnPoint() == null){
                Point spawnPoint = mapManager.getRandomSpawnPoint();
                item.setSpawnPoint(spawnPoint);
            }
        }

        game = new Game(individuals, player, mapManager.getGameMap(), items, scanner);
    }

    private List<NPC> createNPCs() {
        List<NPC> individuals = new ArrayList<>();

        individuals.add(NPCFactory.createGoblin("Bridge Goblin", new Point(2, 1, 0),
                "bridge", 5));
        individuals.add(NPCFactory.createGoblin("Village Goblin", new Point(3, 2, 0),
                "village", 7));
        individuals.add(NPCFactory.createWitch("House Witch", new Point(0, 4, 0), "house"));
        individuals.add(NPCFactory.createOgre("Swamp Ogre", new Point(0, 2, 0), "swamp"));
        individuals.add(NPCFactory.createMerchant("Village Merchant", new Point(3, 3, 0),
                "village"));

        return individuals;
    }


    private List<Item> createItems() {
        List<Item> items = new ArrayList<>();

        items.add(ItemFactory.createWeapon("Shield", 1, "Wooden Shield",
                "A wooden shield"));
        items.add(ItemFactory.createWeapon("Shield", 2, "Iron Shield",
                "An iron shield"));
        items.add(ItemFactory.createWeapon("Sword", 1, "Wooden Sword",
                "A wooden sword"));
        items.add(ItemFactory.createWeapon("Sword", 2, "Iron Sword",
                "An iron sword"));
        items.add(ItemFactory.createPotion("HealthPotion", 1, "Health Potion",
                "A health potion"));
        items.add(ItemFactory.createPotion("StrengthPotion", 1, "Strength Potion",
                "A strength potion"));
        items.add(ItemFactory.createPotion("DefensePotion", 1, "Defense Potion",
                "A defense potion"));
        items.add(ItemFactory.createGold(5));
        items.add(ItemFactory.createGold(7));

        Point healthPotionSpawnPoint = new Point(1, 4, -1);
        Point strengthPotionSpawnPoint = new Point(1, 4, -1);
        Point defensePotionSpawnPoint = new Point(1, 4, -1);
        Point keySpawnPoint = new Point(0, 2, 0);


        Potion healthPotion = ItemFactory.createPotion("HealthPotion", 2,
                "Greater Health Potion", "A greater health potion");
        healthPotion.setSpawnPoint(healthPotionSpawnPoint);
        items.add(healthPotion);

        Potion strengthPotion = ItemFactory.createPotion("StrengthPotion", 2,
                "Greater Strength Potion", "A greater strength potion");
        strengthPotion.setSpawnPoint(strengthPotionSpawnPoint);
        items.add(strengthPotion);

        Potion defensePotion = ItemFactory.createPotion("DefensePotion", 2,
                "Greater Defense Potion", "A greater defense potion");
        defensePotion.setSpawnPoint(defensePotionSpawnPoint);
        items.add(defensePotion);

        Item key = ItemFactory.createItem("Key", "Key", "The key to unlock the princess!");
        key.setSpawnPoint(keySpawnPoint);
        items.add(key);

        return items;
    }


    /**
     * Starts the game loop, where the player can play the game. The game loop continues until the player's health
     * reaches 0 or the player decides to quit the game. Each 5 turns the game is auto-saved.
     */
    private void startGameLoop() {
        boolean isPlaying = true;
        int turnCounter = 0;

        System.out.println("Instructions: Explore the map, defeat enemies, and collect items to find the princess.");

        while (isPlaying) {
            isPlaying = game.playTurn();
            turnCounter++;

            if (turnCounter % 5 == 0) {
                autoSaveGame();
            }

        }
    }


    void saveGame(String filePath) {
        GameState gameState = new GameState(individuals, player, items, mapManager);
        try {
            GameStateManager.saveGame(gameState, filePath);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    private void autoSaveGame() {
        saveGame(SAVE_FILE);
    }

    /**
     * Loads the game from the specified file path. If the file does not exist, a message is displayed to the user.
     * The GameState class provides an object representation of the game state containing the player, NPCs, and items.
     * @param filePath the file path to load the game from
     * @return true if the game was loaded successfully, false otherwise
     */
    boolean loadGame(String filePath) {
        try {
            File saveFile = new File(filePath);
            if (!saveFile.exists()) {
                System.out.println("No saved game found.");
                return false;
            }

            GameState gameState = GameStateManager.loadGame(filePath);
            individuals = gameState.getNpcs();
            player = gameState.getPlayer();
            items = gameState.getItems();
            game = new Game(individuals, player, mapManager.getGameMap(), items, scanner);
            System.out.println("Game loaded successfully.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        new GameManager();
    }
}
