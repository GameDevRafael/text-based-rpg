package model.item;

/**
 * ItemFactory is a factory class that creates items, weapons, potions and gold.
 */
public class ItemFactory {

    public static Item createItem(String itemType, String name, String description) {
        return switch (itemType.toLowerCase()) {
            case "key" -> new Item(name, description);
            default -> throw new IllegalArgumentException("Unknown item type: " + itemType);
        };
    }

    public static Weapon createWeapon(String weaponType, int tier, String name, String description) {
        return switch (weaponType.toLowerCase()) {
            case "sword" -> new Sword(tier, name, description);
            case "shield" -> new Shield(tier, name, description);
            default -> throw new IllegalArgumentException("Unknown weapon type: " + weaponType);
        };
    }

    public static Potion createPotion(String potionType, int tier, String name, String description) {
        return switch (potionType.toLowerCase()) {
            case "strengthpotion" -> new StrengthPotion(tier, name, description);
            case "healthpotion" -> new HealthPotion(tier, name, description);
            case "defensepotion" -> new DefensePotion(tier, name, description);
            default -> throw new IllegalArgumentException("Unknown potion type: " + potionType);
        };
    }

    public static Gold createGold(int amount) {
        return new Gold(amount);
    }
}