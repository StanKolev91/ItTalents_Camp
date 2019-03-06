package diabloGame.items;

import diabloGame.Game;

import java.util.Random;

public abstract class Item {
    private int itemLvl;
    private double sellPrice;
    private String itemType;
    Random rand = new Random();

    void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return itemType;
    }

    public int getItemLvl() {
        return this.itemLvl;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Item() {
        this.itemLvl = rand.nextInt(Game.getLevel() + 5) + Game.getLevel() - ((Game.getLevel() > 4) ? 4 : 0);
        this.sellPrice = this.getItemLvl() - rand.nextDouble();
    }

    public abstract void itemInfo();
}
