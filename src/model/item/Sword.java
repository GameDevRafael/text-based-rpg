package model.item;

import java.io.Serializable;

/**
 * Sword is a subclass of Weapon and has a damageBoost field.
 */
public class Sword extends Weapon implements Serializable {
    int damageBoost;

    /**
     * Constructor for Sword, based on the tier (1-2) the damageBoost gets its value.
     * The price is set to 10 times the tier.
     * @param tier 1 or 2
     * @param name name of the sword
     * @param description description of the sword
     */
    public Sword(int tier, String name, String description) {
        super(tier, name, description);
        this.damageBoost = setupDamageBoost(tier);
        this.price = 10 * tier;
    }

    public int getDamageBoost() {
        return damageBoost;
    }

    private int setupDamageBoost(int tier) {
        return switch (tier) {
            case 1 -> 15;
            case 2 -> 25;
            default -> 0;
        };
    }
}
