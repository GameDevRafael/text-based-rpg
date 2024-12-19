package model.item;

import java.io.Serializable;

/**
 * DefensePotion is a subclass of Potion and has a defense field.
 */
public class DefensePotion extends Potion implements Serializable {
    private final float defense;

    /**
     * Constructor for DefensePotion, based on the tier (1-2) the defense gets its value.
     * @param tier 1 or 2
     * @param name name of the potion
     * @param description description of the potion
     */
    public DefensePotion(int tier, String name, String description){
        super(tier, name, description);
        this.defense = setupDefense(tier);
    }

    public String getSpecialAbility() {
        return "defense";
    }

    @Override
    public float getHeal() {
        return 0;
    }

    @Override
    public float getStrengthIncrease() {
        return 0;
    }

    @Override
    public float getDefenseIncrease() {
        return defense;
    }

    private int setupDefense(int defense) {
        return switch (defense) {
            case 1 -> 15;
            case 2 -> 20;
            default -> 0;
        };
    }

}
