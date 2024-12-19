package model.item;

import java.io.Serializable;

/**
 * StrengthPotion is a subclass of Potion and has a strength field.
 */
public class StrengthPotion extends Potion implements Serializable {
    private final float strength;

    /**
     * Constructor for StrengthPotion, based on the tier (1-2) the strength gets its value.
     * @param tier 1 or 2
     * @param name name of the potion
     * @param description description of the potion
     */
    public StrengthPotion(int tier, String name, String description){
        super(tier, name, description);
        this.strength = setupStrength(tier);
    }

    public String getSpecialAbility() {
        return "strength";
    }

    @Override
    public float getHeal() {
        return 0;
    }

    @Override
    public float getStrengthIncrease() {
        return strength;
    }

    @Override
    public float getDefenseIncrease() {
        return 0;
    }

    private int setupStrength(int tier) {
        return switch (tier) {
            case 1 -> 10;
            case 2 -> 15;
            default -> 0;
        };
    }

}
