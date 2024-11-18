package model.item;

import java.io.Serializable;
import model.character.Character;

/**
 * The Potion class is an abstract class that represents a potion in the game and extends the Weapon class.
 * Each potion has a tier, name, description, and price.
 * The price is calculated based on the tier (1-2)
 * The class provides methods to revert the effects of the potion on a character,
 * and abstract methods to get the healing amount, strength increase, defense increase,
 * and special ability of the potion.
 * Subclasses of Potion must implement the abstract methods to define the specific effects of the potion.
 */
public abstract class Potion extends Weapon implements Serializable {

    /**
     * Constructor for Potion, based on the tier, name, and description.
     * The price is set to 30 times the tier divided by 2.
     * @param tier 1 or 2
     * @param name name of the potion
     * @param description description of the potion
     */
    public Potion(int tier, String name, String description) {
        super(tier, name, description);
        this.price = 30 * tier/2;
    }

    /**
     * Reverts the effects of the potion on a character depending on the special ability of the potion.
     * @param player the character to revert the effects on
     */
    public void revertEffects(Character player) {
        switch (getSpecialAbility()) {
            case "heal":
                break;
            case "strength":
                player.setDamage(player.getDamage() - getStrengthIncrease());
                System.out.println("The effects of " + getName() + " have worn off. Damage reverted.");
                break;
            case "defense":
                player.setDefense(player.getDefense() - getDefenseIncrease());
                System.out.println("The effects of " + getName() + " have worn off. Defense reverted.");
                break;
            default:
                System.out.println("This potion has an unknown effect.");
        }
    }

    public abstract float getHeal();
    public abstract float getStrengthIncrease();
    public abstract float getDefenseIncrease();
    public abstract String getSpecialAbility();

    public String toString(){
        return getName() + ": Tier: " + getTier();
    }
}
