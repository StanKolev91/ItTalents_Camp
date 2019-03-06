package diabloGame.rolePlayingChar.humanoids;

import diabloGame.Drop.IDropsGoldWhenKilled;
import diabloGame.Game;
import diabloGame.rolePlayingChar.IGivesExperienceWhenKilled;
import diabloGame.rolePlayingChar.RolePlayingCharacter;

public class Warrior extends RolePlayingCharacter implements IGivesExperienceWhenKilled, IDropsGoldWhenKilled {
    public Warrior(String name) {

        super("Warrior", name);
        super.setArmor(super.getArmor()+Game.getLevel()*2);
        super.setCritChance(super.getCritChance()+Game.getLevel());
        super.setEvasionChance(super.getEvasionChance()+ Game.getLevel());

    }
}
