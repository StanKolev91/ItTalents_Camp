package diabloGame.items;

import diabloGame.Drop.Dropable;
import diabloGame.Game;
import diabloGame.rolePlayingChar.humanoids.Hero;
import diabloGame.rolePlayingChar.RolePlayingCharacter;

import java.util.Random;

public class SpellScroll extends Item implements Dropable, IScrolls {
    Random rand = new Random();
    private String stats;
    private int debuffPercent;
    private String spellName;
    private boolean learned;

    public void learn(Hero player){
        System.out.println("You've learned a new spell "+this.spellName);
        player.addSpell(this);
        this.learned = true;
    }


    public SpellScroll() {
        super.setItemType("SpellScroll");
        setSellPrice(Game.getLevel());
        this.spellName = "Slow Agony level "+this.getItemLvl();
        this.debuffPercent = this.getItemLvl()*3;
        int chance = rand.nextInt(100) + 1;
        if (chance <= 18) stats = "health";
        else if (chance <= 36) stats = "attack speed";
        else if (chance <= 54) stats = "armor";
        else if (chance <= 72) stats = "damage";
        else if (chance <= 90) stats = "evasion chance";
        else stats = "critical strike chance";
    }

    public void castSpell(RolePlayingCharacter c) {
        c.addToCurrentHealth((stats.equalsIgnoreCase("health")) ? c.getHealth() * (this.debuffPercent/100.0) : c.getHealth());
        c.setArmor(stats.equalsIgnoreCase("armor") ? c.getArmor() * (this.debuffPercent/100.0) : c.getArmor());
        c.addDmg(stats.equalsIgnoreCase("damage") ? c.getDmg() * (this.debuffPercent/100) : c.getDmg());
        c.setAttackSpeed(stats.equalsIgnoreCase("attack speed") ? c.getAttackSpeed() * (this.debuffPercent/100.0) : c.getAttackSpeed());
    }
    public void itemInfo(){
        if (!learned){
            System.out.println(this.getItemType().substring(0, 1).toUpperCase() + this.getItemType().substring(1));
        }
        System.out.println("This spell will decrease your oponents " + this.stats + " with "+debuffPercent+"%.");
    }
}
