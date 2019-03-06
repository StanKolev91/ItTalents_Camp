package diabloGame.rolePlayingChar;

import diabloGame.Drop.Dropable;
import diabloGame.Game;

import java.util.Random;
import java.util.Scanner;

public abstract class RolePlayingCharacter {
    private String name;
    private String type;
    private double health;
    private double maxHealth;
    private int dmg;
    private double attackSpeed;
    private double armor;
    private int critChance;
    private int evasionChance;
    private int level;
    private Dropable drop;
    private boolean hasShield;
    private int blockChance;
    private Random rand = new Random();
    private Scanner sc = new Scanner(System.in);

    public RolePlayingCharacter() {
        this.level = Game.getLevel();
        this.updateStats();
    }

    public RolePlayingCharacter(String type, String name) {
        this();
        this.type = type;
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setDrop(Dropable drop) {
        this.drop = drop;
    }

    protected int getCritChance() {
        return critChance;
    }

    protected void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    protected int getEvasionChance() {
        return evasionChance;
    }

    protected void setEvasionChance(int evasionChance) {
        this.evasionChance = evasionChance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public void addToCurrentHealth(double health) {
        if ((int) (this.health + health) <= 0) {
            this.health = 0;
                System.out.println(this.getName() + " died.");
        } else if ((this.health + health) > this.maxHealth) {
            this.heal();
        } else {
            this.health = this.health + health;
                System.out.println(this.getName() + " is left with " + (int) this.health + " life");
        }
    }

    public void addDmg(int dmg) {
        this.dmg += dmg;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public String getType() {
        return type;
    }

    public double getHealth() {
        return health;
    }

    public int getDmg() {
        return dmg;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }


    public void setBlockChance() {
        hasShield = true;
    }

    private int getBlockChance() {
        if (hasShield) {
            return 10;
        } else
            return 0;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void heal() {
        this.health = this.maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void updateStats() {
        this.setMaxHealth(this.getLevel() * 50 + this.getLevel() * 5);
        this.addDmg(2 + this.getLevel() * (this.getLevel() / 2));
        this.setAttackSpeed(this.getLevel() * 2);
        this.setArmor(this.getLevel() * 10);
        this.setCritChance((this.getLevel() > 20) ? 26 : (this.getLevel() + 6));
        this.setEvasionChance((this.getLevel() > 18) ? 24 : (this.getLevel() + 6));
        this.addToCurrentHealth(this.getMaxHealth());
    }

    public Dropable getDrop() {
            return drop;
    }

    public void attack(RolePlayingCharacter c) {
        int damage = (int) ((((100 + c.level * 30) - c.armor) / (100 + c.level * 30)) * this.getDmg());
        if (rand.nextInt(100) + 1 <= this.critChance) {
            damage *= 2;
            System.out.println(this.getName() + " hit " + c.getName() + " with CITICAL STRIKE for " + damage + " damage.");

        } else {
            System.out.println(this.getName() + " attacks " + c.getName() + " for " + damage + " damage.");
        }
        if (rand.nextInt(100) + 1 < c.getEvasionChance()) {
            System.out.println(c.getName() + " evades!" + "Zero damage is done");
        } else {
            if (rand.nextInt(100) + 1 <= c.getBlockChance()) {
                System.out.println(c.getName() + " blocks half of " + this.getName() + " damage");
                damage /= 2;
            }
            c.addToCurrentHealth(-damage);

        }


    }

    public void stats() {
        System.out.println("\n" + this.getName());
        System.out.println("Health: " + String.format("%.2f", this.getHealth()) + " / " +
                String.format("%.2f", this.getMaxHealth()));
        System.out.println("Level: " + this.getLevel());
        System.out.println("Damage: " + this.getDmg());
        System.out.println("Armor: " + String.format("%.2f", this.getArmor())+" / "+
                String.format("%.2f",1-(((100 + this.level * 30) - this.armor) / (100 + this.level * 30)))+"% damage reduction");
        System.out.println("Attack speed: " + String.format("%.2f", this.getAttackSpeed()));
        System.out.println("Critical chance: " + this.getCritChance());
        System.out.println(("Evasion chance: " + this.getEvasionChance()));
        System.out.println("Block chance: " + this.getBlockChance());
    }

}
