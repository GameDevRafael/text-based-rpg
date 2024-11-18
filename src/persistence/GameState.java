package persistence;



import core.MapManager;
import model.character.NPC;
import model.character.Player;
import model.item.Item;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the state of the game including the player, NPCs, items on the ground spread around the world, and
 * the game map.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<NPC> npcs;
    private final Player player;
    private final List<Item> items;
    private final MapManager gameMap;

    /**
     * Creates a new GameState object with the given NPCs, player, items, and game map.
     * @param npcs the list of NPCs scattered around the game world
     * @param player the player character
     * @param items the list of items scattered around the game world
     * @param gameMap the game map
     */
    public GameState(List<NPC> npcs, Player player, List<Item> items, MapManager gameMap) {
        this.npcs = npcs;
        this.player = player;
        this.items = items;
        this.gameMap = gameMap;
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Item> getItems() {
        return items;
    }
}