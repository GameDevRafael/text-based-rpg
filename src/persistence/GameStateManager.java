package persistence;

import java.io.*;
import java.io.Serializable;

/**
 * GameStateManager class is responsible for saving and loading the game (state).
 */
public class GameStateManager implements Serializable {
    public static void saveGame(GameState gameState, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(gameState);
        }
    }

    public static GameState loadGame(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameState) ois.readObject();
        }
    }
}
