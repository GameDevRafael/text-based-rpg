package model.item;

import java.io.Serializable;

/**
 * Weapon is a subclass of Item and has a durability field that determines the amount of fights the item can take before
 * it breaks, tier and price (to determine how much it costs on the shop) field.
 */
public abstract class Weapon extends Item implements Serializable {
    int durability;
    int tier;
    int price;

    /**
     * Constructor for Weapon, based on the tier (1-2) the durability gets its value.
     * @param tier 1 or 2
     * @param name name of the weapon
     * @param description description of the weapon
     */
    public Weapon(int tier, String name, String description) {
        super(name, description);
        this.tier = tier;
        this.durability = setupDurability(tier);
    }

    public int getDurability() {
        return durability;
    }

    public int getTier() {
        return tier;
    }

    public void updateDurability(){
        this.durability -= 1;
    }

    private int setupDurability(int tier) {
        return switch (tier) {
            case 1 -> 2;
            case 2 -> 4;
            default -> 0;
        };
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " Durability: " + durability + ". " + "Price: " + getPrice() + " gold.";
    }

    public int getPrice() {
        return price;
    }


}
