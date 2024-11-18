package model.character;


import model.world.Point;

/**
 * NPCFactory class is a factory class that creates different types of NPCs in the game.
 */
public class NPCFactory {

    public static Player createPlayer(String name, Point position) {
        return new Player(name, position);
    }

    public static NPC createGoblin(String name, Point position, String specificType, int goldRequired) {
        return new Goblin(name, position, specificType, goldRequired);
    }

    public static NPC createOgre(String name, Point position, String specificType) {
        return new Ogre(name, position, specificType);
    }

    public static NPC createWitch(String name, Point position, String specificType) {
        return new Witch(name, position, specificType);
    }

    public static NPC createMerchant(String name, Point position, String specificType) {
        return new Merchant(name, position, specificType);
    }
}
