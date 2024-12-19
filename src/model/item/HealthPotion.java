package model.item;

import java.io.Serializable;

/**
 * HealthPotion is a subclass of Potion and has a healing field.
 */
public class HealthPotion extends Potion implements Serializable {
    private final float healing;

    /**
     * Constructor for HealthPotion, based on the tier (1-2) the healing gets its value.
     * @param tier 1 or 2
     * @param name name of the potion
     * @param description description of the potion
     */
    public HealthPotion(int tier, String name, String description) {
        super(tier, name, description);
        this.healing = setupHealing(tier);
    }

    public String getSpecialAbility() {
        return "heal";
    }

    @Override
    public float getHeal() {
        return healing;
    }

    @Override
    public float getStrengthIncrease() {
        return 0;
    }

    @Override
    public float getDefenseIncrease() {
        return 0;
    }

    private int setupHealing(int tier) {
        return switch (tier) {
            case 1 -> 15;
            case 2 -> 25;
            default -> 0;
        };
    }



}
