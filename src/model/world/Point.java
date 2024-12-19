package model.world;

import java.util.Objects;
import java.io.Serializable;

/**
 * Represents a point in 3D space that has integer coordinates. Each object will represent a point in the world which
 * will determine the positions of the locations in the world.
 */
public class Point implements Serializable{
    private final int x;
    private final int y;
    private final int z;

    /**
     * Constructor for the Point class.
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param z The z coordinate of the point.
     */
    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String toString(){
        return "x: " + x + ", y: " + y + ", z: " + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y && z == point.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }


}