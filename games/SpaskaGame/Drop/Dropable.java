package diabloGame.Drop;

import diabloGame.items.*;
import diabloGame.rolePlayingChar.RolePlayingCharacter;
import java.util.Random;

public interface Dropable {
    int getItemLvl();
    void itemInfo();
    String getItemType();

    default Dropable generateDrop(RolePlayingCharacter rpg){
        Random rand = new Random();
        Dropable returnItem;
        if (!rpg.getType().equalsIgnoreCase("mage")) {
            int chance = rand.nextInt(100) + 1;
            if (chance <= 1) {
                returnItem = new AxeOneHand("weapon");
            } else {
                if (chance <= 10) {
                    returnItem = new SwordOneHand("weapon");
                } else {
                    if (chance <= 20) {
                        returnItem = new Armor("chest");
                    } else {
                        if (chance <= 30) {
                            returnItem = new Armor("pants");
                        } else {
                            if (chance <= 40) {
                                returnItem = new Armor("helmet");
                            } else {
                                if (chance <= 50) {
                                    returnItem = new Armor("boots");
                                } else {
                                    if (chance <= 60) {
                                        returnItem = new Armor("gloves");
                                    } else if (rpg.getType().equalsIgnoreCase("beast")) {
                                        returnItem = new Potion();
                                    } else {
                                        returnItem = new Trasch();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            returnItem = new SpellScroll();
        }
        System.out.println(rpg.getName() + " drops new item:\n");
        returnItem.itemInfo();
        return returnItem;
    }
}
