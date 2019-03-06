package diabloGame.items;

import diabloGame.Drop.Dropable;
import diabloGame.Game;
import diabloGame.rolePlayingChar.humanoids.Hero;

public abstract class Weapon extends Item implements Equiptables, Upgradable, Dropable {
    private int bonusDmg;
    private double bonusAttackSpeed;
    private boolean usesTwoHands;

    public boolean getUsesTwoHands() {
        return usesTwoHands;
    }

    private int getBonusDmg() {
        return bonusDmg;
    }

    private double getBonusAttackSpeed() {
        return bonusAttackSpeed;
    }

    Weapon(String weaponType) {
        super.setItemType(weaponType);
        this.bonusDmg = (int)(super.getItemType().equalsIgnoreCase("Weapon") ? ((rand.nextDouble() + rand.nextInt(2) + 1) * super.getItemLvl()) : 0);
        this.bonusAttackSpeed = (int)(super.getItemType().equalsIgnoreCase("EpicWeapon") ? ((rand.nextDouble() + rand.nextInt(2) + 1) * super.getItemLvl()) : 0);
    }

    public void itemInfo() {
        if (super.getItemType() != null) {
            System.out.println(this.getItemType().substring(0, 1).toUpperCase() + this.getItemType().substring(1));
            System.out.println("Item level: " + super.getItemLvl());
            System.out.println("Damage: +" + this.getBonusDmg());
            System.out.println("Attack speed: +" + (int) this.getBonusAttackSpeed());
        } else System.out.println("<<no Item>>");
    }

    public void upgrade() {
        System.out.println("Your weapon was upgraded");
        this.bonusAttackSpeed += 2 * Game.getLevel();
        this.bonusDmg += 2 * Game.getLevel();
    }

    @Override
    public void addBonuses(Hero player) {
        player.addDmg(this.getBonusDmg());
        player.setAttackSpeed(player.getAttackSpeed() + this.getBonusAttackSpeed());
    }

    @Override
    public void removeBonuses(Hero player) {
        player.addDmg(- this.getBonusDmg());
        player.setAttackSpeed(player.getArmor() - this.getBonusAttackSpeed());
    }

}
