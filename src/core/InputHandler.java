package core;

import model.world.Point;

import java.io.Serializable;
import java.util.*;

/**
 * This class is responsible for handling user input.
 * It is a singleton class, meaning that only one instance of this class can be created.
 */
class InputHandler implements Serializable {
    private static InputHandler instance;
    private final Scanner scanner;

    private InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    static InputHandler getInstance(Scanner scanner) {
        if (instance == null) {
            return new InputHandler(scanner);
        }
        return instance;
    }

    String getUserInput() {
        return scanner.nextLine().toLowerCase();
    }

    boolean getYesNoInput(String question) {
        while (true) {
            System.out.println(question + " (yes / no)");
            String input = getUserInput();

            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;

            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * This method is used to get a valid integer input from the user. It gets the current position of the user, and
     * it shows the possible directions the user can take. If the direction the user chose only has the base movements,
     * the method will increment, or decrement, the x and y coordinates accordingly. If the user's direction has
     * additional movements, such as up or down, the method will increment, or decrement, the z coordinate accordingly.
     * @param direction the direction the user wants to go
     * @param currentPos the current position of the user
     * @return the new position of the user
     */
    Point calculateNewPosition(String direction, Point currentPos) {
        int newX = currentPos.getX();
        int newY = currentPos.getY();
        int newZ = currentPos.getZ();

        String[] commands = direction.toLowerCase().split(" ");

        switch (commands[0]) {
            case "west" -> newX--;
            case "east" -> newX++;
            case "north" -> newY++;
            case "south" -> newY--;
        }

        if (commands.length > 1) {
            switch (commands[1]) {
                case "up" -> newZ++;
                case "down" -> newZ--;
            }
        }

        return new Point(newX, newY, newZ);
    }
}