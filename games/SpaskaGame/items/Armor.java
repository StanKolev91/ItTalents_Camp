package diabloGame.items;

import diabloGame.Drop.Dropable;
import diabloGame.rolePlayingChar.humanoids.Hero;

import java.util.Random;

public class Armor extends Item implements Equiptables, Upgradable, Dropable {
    private double bonusArmor;
    private double bonusHealth;

    public double getBonusArmor() {
        return bonusArmor;
    }

    public double getBonusHealth() {
        return bonusHealth;
    }

    public Armor(String armorType) {
        super.setItemType(armorType);
        this.bonusHealth = (int)(("Chest,Helmet,Pants".toLowerCase().contains(super.getItemType().toLowerCase())) ? new Random().nextDouble() * super.getItemLvl() * 10 : 0.0);
        this.bonusArmor = (int)("Gloves,Boots,Chest,Pants,Helmet".toLowerCase().contains(super.getItemType().toLowerCase()) ? (new Random().nextDouble() + 1) * super.getItemLvl() : 0);
    }

    public void itemInfo() {
        if (super.getItemType() != null) {
            System.out.println(this.getItemType().substring(0, 1).toUpperCase() + this.getItemType().substring(1));
            System.out.println("Item level: " + super.getItemLvl());
            System.out.println("Armor: +" + (int) this.bonusArmor);
            System.out.println("Health: +" + (int) this.bonusHealth);
        }
    }

    @Override
    public void upgrade() {
        System.out.println("Your " + this.getItemType() + " was upgraded");
        this.bonusArmor += 2 * diabloGame.Game.getLevel();
        this.bonusHealth += 2 * diabloGame.Game.getLevel();
    }

    @Override
    public void addBonuses(Hero player) {
        player.setMaxHealth(player.getMaxHealth() + this.getBonusHealth());
        player.setArmor(player.getArmor() + this.getBonusArmor());
    }

    public void removeBonuses(Hero player) {
        player.setMaxHealth(player.getMaxHealth() - this.getBonusHealth());
        player.setArmor(player.getArmor() - this.getBonusArmor());
    }
}

