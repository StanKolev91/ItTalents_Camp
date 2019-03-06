package diabloGame.rolePlayingChar.humanoids;

import diabloGame.Game;
import diabloGame.items.*;
import diabloGame.rolePlayingChar.Beast;
import diabloGame.rolePlayingChar.RolePlayingCharacter;

import java.util.Scanner;

public class Hero extends RolePlayingCharacter {
    Scanner sc = new Scanner(System.in);
    private boolean hasPet;
    private int isHit;
    private boolean isInFight;
    private Beast pet;
    private SpellScroll[] spellBook;
    private int spellBookFreeSpaces;
    private Item[] inventory;
    private RolePlayingCharacter target;
    private double money;
    private double mana;
    private double maxMana;
    private double experience;
    private Armor[] gear;
    private Weapon primaryWeapon;
    private Weapon secondaryWeapon;
    private Armor helmet;
    private Armor chest;
    private Armor boots;
    private Armor gloves;
    private Armor pants;
    private int inventoryFreeSlots;

    public Hero(String name) {
        super("Player", name);
        this.experience = 0;
        this.maxMana = super.getMaxHealth() / 3;
        fillMana();
        spellBook = new SpellScroll[2];
        spellBookFreeSpaces = spellBook.length;
        inventory = new Item[8];
        inventoryFreeSlots = inventory.length;
        System.out.println(this.getLevel());
    }

    public double getExperience() {
        return experience;
    }

    public void addScExperience(double experience) {
        this.experience += experience;
        if (this.experience >= Game.getExpTillNextLevel()) {
            levelUp();
        }
    }

    public SpellScroll getSpell(int spellNmbr) {
        return spellBook[spellNmbr];
    }

    private void lastChance() {
        System.out.println("You are about to die, do you wanna check your invntory for a health Potion?(Y/N)");
        String answer = sc.nextLine().trim().toLowerCase();
        if (answer.contains("yes") || answer.contains("y")) {
            int itemNmbr = this.chooseItemFromInventory();
            Item chosenItem = this.getItemFromInventory(itemNmbr);
            if (chosenItem != null) {
                if (chosenItem.getItemType().toLowerCase().contains("health") &&
                        chosenItem.getItemType().toLowerCase().contains("potion")) {
                    ((Potion) chosenItem).consume(this);
                    this.removeItemFromInventory(itemNmbr);
                } else System.out.println("This is not a Health potion");
            }
        }
    }

    @Override
    public void addToCurrentHealth(double health) {
        if ((int) (super.getHealth() + health) <= 0) {
            lastChance();
            if ((int) (super.getHealth() + health) <= 0) {
                System.out.println(this.getName() + " died.");
                diabloGame.Game.setIsGameOver(true);
            }
        } else super.addToCurrentHealth(health);
    }

    public void addSpell(SpellScroll spell) {
        if (spellBookFreeSpaces > 0) {
            this.spellBook[spellBook.length - this.spellBookFreeSpaces--] = spell;
        } else {
            System.out.println("No free places in your SpellBook\nDo you want to overwrite existing spell - YES/NO");
            String answer = sc.nextLine().trim().toLowerCase();
            if (answer.contains("yes") || answer.contains("y")) {
                System.out.println("Choose spell to be overwritten or write esc to return");
                answer = sc.nextLine().trim().toLowerCase();
                if (!answer.contains("esc")) {
                    for (int i = 0; i < spellBook.length; i++) {
                        System.out.println((i + 1) + ". position: ");
                        spellBook[i].itemInfo();
                    }
                    int chosenSpellNmbr;
                    do {
                        chosenSpellNmbr = sc.nextInt();
                        chosenSpellNmbr -= 1;
                    } while (chosenSpellNmbr >= 0 && chosenSpellNmbr < spellBook.length);
                    spellBook[chosenSpellNmbr] = spell;
                }
            }
        }
    }

    public void setArmor(double armor) {
        super.setArmor(armor);
    }

    public Beast getPet() {
        return pet;
    }

    public void setAttackSpeed(double attackSpeed) {
        super.setAttackSpeed(attackSpeed);
    }

    public void setHasPet(boolean hasPet) {
        this.hasPet = hasPet;
    }

    public void setIsHit(int isHit) {
        this.isHit = isHit;
    }

    public void setInFight(boolean inFight) {
        isInFight = inFight;
    }

    public void setPet(Beast pet) {
        this.pet = pet;
    }

    public double showGold() {
        return money;
    }

    public void addMoney(double money) {
        this.money += money;
    }

    private void levelUp() {
        super.setLevel(super.getLevel() + 1);
        diabloGame.Game.setLevel(super.getLevel());
        Game.setExpTillNextLevel(Game.getLevel() * 150);
        if (super.getLevel() > 1) {
            System.out.println("Congratulations you've reached Level " + super.getLevel() + "!");
            updateStats();
        }
    }

    public String getName() {
        return super.getName();
    }

    public double getAttackSpeed() {
        return super.getAttackSpeed();
    }

    public double getHealth() {
        return super.getHealth();
    }

    public void updateStats() {
        super.setMaxHealth(super.getMaxHealth() + 50);
        super.addDmg(+4);
        super.setAttackSpeed(super.getAttackSpeed() + 2);
        super.setArmor(super.getArmor() + 7);
        super.setCritChance(super.getCritChance() + 2);
        super.setEvasionChance(super.getEvasionChance() + 1);
        heal();
    }

    private Equiptables swapEquiptables(Equiptables equipted, int inventoryNumberToBeEquipt) {
        if (equipted != null) {
            equipted.removeBonuses(this);
            Equiptables temp = equipted;
            equipted = (Equiptables) inventory[inventoryNumberToBeEquipt];
            inventory[inventoryNumberToBeEquipt] = (Item) temp;
        } else {
            equipted = (Equiptables) inventory[inventoryNumberToBeEquipt];
            removeItemFromInventory(inventoryNumberToBeEquipt);
        }
        equipted.addBonuses(this);
        return equipted;
    }

    public void equiptItem(int itemNumber) {
        if (inventory[itemNumber] != null) {
            if (inventory[itemNumber] instanceof Equiptables) {
                Equiptables toBeEquipt = (Equiptables) inventory[itemNumber];
                System.out.println("<<" + toBeEquipt.getItemType().substring(0, 1).toUpperCase() +
                        toBeEquipt.getItemType().substring(1) + " equipt>>");
                if (toBeEquipt.getItemType().equalsIgnoreCase("Weapon")) {
                    String answer = null;
                    if (((Weapon) toBeEquipt).getUsesTwoHands()) {
                        if (this.secondaryWeapon != null) {
                            System.out.println("You are trying to equipt a two - hand weapon, \n" +
                                    "equipting it will remove your both weapons and will put them in inventory. Do you want to continue: YES/NO");
                            answer = sc.nextLine().trim().toLowerCase();
                        }
                        if (answer.contains("yes")) {
                            this.primaryWeapon = (Weapon) swapEquiptables(this.primaryWeapon, itemNumber);
                            addToInventory(this.secondaryWeapon);
                            this.secondaryWeapon = (Weapon) inventory[itemNumber];
                        }
                    } else {
                        if (this.primaryWeapon == null || this.secondaryWeapon == null) {
                            if (this.primaryWeapon == null) {
                                this.primaryWeapon = (Weapon) swapEquiptables(this.primaryWeapon, itemNumber);
                            } else this.secondaryWeapon = (Weapon) swapEquiptables(this.secondaryWeapon, itemNumber);
                        } else {
                            System.out.println("You have no free hands to equipt this weapon, " +
                                    "\ndo you want to change with a already equipt weapon? YES/NO");
                            answer = sc.nextLine().trim().toLowerCase();
                            if (answer.contains("yes") || answer.contains("y")) {
                                System.out.println("Choose weapon to go to the inventory");
                                System.out.println("1:");
                                this.primaryWeapon.itemInfo();
                                System.out.println("2:");
                                this.secondaryWeapon.itemInfo();
                                do {
                                    answer = sc.nextLine().trim();
                                } while (!answer.matches("[1-2]"));
                                switch (answer) {
                                    case "1": {
                                        this.primaryWeapon = (Weapon) swapEquiptables(this.primaryWeapon, itemNumber);
                                        break;
                                    }
                                    case "2": {
                                        this.secondaryWeapon = (Weapon) swapEquiptables(this.secondaryWeapon, itemNumber);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    switch (inventory[itemNumber].getItemType().toLowerCase()) {
                        case "chest": {
                            this.chest = (Armor) swapEquiptables(this.chest, itemNumber);
                            break;
                        }
                        case "helmet": {
                            this.helmet = (Armor) swapEquiptables(this.helmet, itemNumber);
                            break;
                        }
                        case "pants": {
                            this.pants = (Armor) swapEquiptables(this.pants, itemNumber);
                            break;
                        }
                        case "gloves": {
                            this.gloves = (Armor) swapEquiptables(this.gloves, itemNumber);
                            break;
                        }
                        case "boots": {
                            this.boots = (Armor) swapEquiptables(this.boots, itemNumber);
                            break;
                        }
                    }
                }
            } else System.out.println("You cannot equipt this Item");
        }
    }

    public void removeSpell() {
        //TODO
        int spellNumber;
        do {
            spellNumber = sc.nextInt();
        } while (spellNumber < 1 || spellNumber > 2);
        if (this.spellBook[spellNumber] != null) {
            this.spellBook[spellNumber] = null;
            System.out.println("SpellScroll number " + spellNumber + " is removed");
        } else System.out.println("<<there is no spell to be removed>>");
    }

    public void useSpell(int spellNumber) {
        //TODO
        if (spellBook[spellNumber] != null) {
            this.spellBook[spellNumber].castSpell(this.target);
        } else System.out.println("<<no spell>>");
    }

    public Item getItemFromInventory(int itemNumber) {
        if (itemNumber >= 0 && itemNumber < this.inventory.length) {
            if (inventory[itemNumber] != null) {
                return inventory[itemNumber];
            } else {
                System.out.println("<<this inventory position is empty>>");
            }
        } else System.out.println("<<invalid input>>");
        return null;
    }

    public void addToInventory(Item drop) {
        String input;
        if (inventoryFreeSlots == 0) {
            System.out.println("No free slot in the inventory. Do you want to remove an Item? YES/NO");
            input = sc.nextLine().trim().toLowerCase();
            if (input.contains("yes") || input.contains("y")) {
                int itemNumber = chooseItemFromInventory();
                removeItemFromInventory(itemNumber);
                inventory[itemNumber] = drop;
                inventoryFreeSlots--;
            }
        } else {
            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i] == null) {
                    inventory[i] = drop;
                    inventoryFreeSlots--;
                    break;
                }
            }
        }
    }

    public void showSpells() {
        for (int i = 0; i < spellBook.length; i++) {
            if (spellBook[i] != null) {
                System.out.println("\nSpellBook position " + (i + 1) + ":");
                spellBook[i].itemInfo();
            } else System.out.println("\nSpellBook position " + (i + 1) + " is empty.");
        }
    }

    public void removeItemFromInventory(int itemNumber) {
        if (inventory[itemNumber] != null) {
            inventory[itemNumber] = null;
            inventoryFreeSlots++;
        } else System.out.println("<<this inventory position is empty>>");
    }

    public int chooseItemFromInventory() {
        System.out.println(inventoryFreeSlots);
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                System.out.println("\nInventory position: " + (i + 1));
                inventory[i].itemInfo();
                System.out.println("Sell price: " + String.format("%.2f", inventory[i].getSellPrice()) + " gold");
            } else System.out.println("\nInventory position " + (i + 1) + " is empty.");
        }
        System.out.println("\nChoose item number 1 - 8 or 9 to return\n");
        String itemNumber;
        do {
            itemNumber = sc.nextLine().trim();
        } while (!itemNumber.matches("[1-9]"));
        int num = Integer.parseInt(itemNumber) - 1;
        if (num == 8) {
            return -1;
        } else return num;
    }


    public void tame(Beast pet) {
        //TODO
        if (isInFight) {
            System.out.println("Taming beast ...");
            while (isHit < 10) {
                super.setAttackSpeed(1000);
            }
            if (super.getHealth() > 0) {
                this.pet = pet;
                System.out.println("Congratulations you just tamed a new pet");
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter pets name");
                this.pet.setName(sc.nextLine());
            }
        }
    }

    private void fillMana() {
        this.mana = maxMana;
    }

    public void showGear() {
        System.out.println("Head: ");
        if (this.helmet != null) {
            this.helmet.itemInfo();
        } else System.out.println("<<no equipt gear at this position>>");
        System.out.println("\nChest: ");
        if (this.chest != null) {

            this.chest.itemInfo();
        } else System.out.println("<<no equipt gear at this position>>");
        System.out.println("\nPants: ");
        if (this.pants != null) {

            this.pants.itemInfo();
        } else System.out.println("<<no equipt gear at this position>>");
        System.out.println("\nGloves: ");
        if (this.gloves != null) {

            this.gloves.itemInfo();
        } else System.out.println("<<no equipt gear at this position>>");
        System.out.println("\nBoots: ");
        if (this.boots != null) {

            this.boots.itemInfo();
        } else System.out.println("<<no equipt gear at this position>>");
        System.out.println("\nPrimary Weapon: ");
        if (this.primaryWeapon != null) {

            this.primaryWeapon.itemInfo();
        } else System.out.println("<<no equipt gear at this position>>");
        System.out.println("\nSecondary Weapon: ");
        if (this.secondaryWeapon != null) {
            this.secondaryWeapon.itemInfo();
        } else System.out.println("<<no equipt gear at this position>>");
    }

    public void stats() {
        super.stats();
        System.out.println("Gold: " + String.format("%.2f", this.showGold()));
        System.out.println("Experience untill next level: " + (int) (Game.getExpTillNextLevel() -
                this.getExperience()) + "\n");
    }
}
