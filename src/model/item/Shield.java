package model.item;

import java.io.Serializable;

/**
 * Shield is a subclass of Weapon and has a damageReduced field.
 */
public class Shield extends Weapon implements Serializable {
    int damageReduced;

    /**
     * Constructor for Shield, based on the tier (1-2) the damageReduced gets its value.
     * The price is set to 15 times the tier.
     * @param tier 1 or 2
     * @param name name of the shield
     * @param description description of the shield
     */
    public Shield(int tier, String name, String description) {
        super(tier, name, description);
        this.damageReduced = setupDamageReduced(tier);
        this.price = 15 * tier;


    }

    private int setupDamageReduced(int tier) {
        return switch (tier) {
            case 1 -> 15;
            case 2 -> 25;
            default -> 0;
        };
    }

    public int getDamageReduced() {
        return damageReduced;
    }
}
