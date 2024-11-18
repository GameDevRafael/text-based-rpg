package model.item;

import java.io.Serializable;

/**
 * Gold is a subclass of Item and has a goldAmount field for determining the number of gold coins.
 */
public class Gold extends Item implements Serializable {
    private int goldAmount;

    /**
     * Constructor for Gold, based on the amount of gold.
     * @param amount amount of gold
     */
    public Gold(int amount) {
        super("Gold", amount + " shiny gold coins");
        this.goldAmount = amount;
    }

    @Override
    public String getDescription() {
        return goldAmount + " shiny gold coins";
    }

    public int getAmount() {
        return goldAmount;
    }

    public void setAmount(int amount) {
        this.goldAmount = amount;
    }

    public void addGold(int amount) {
        goldAmount += amount;
    }

    public void removeGold(int amount) {
        goldAmount -= amount;
    }

    public void removeAllGold() {
        goldAmount = 0;
    }
}