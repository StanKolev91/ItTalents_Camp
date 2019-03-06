package diabloGame.rolePlayingChar.humanoids;
import diabloGame.Drop.IDropsGoldWhenKilled;
import diabloGame.rolePlayingChar.IGivesExperienceWhenKilled;
import diabloGame.rolePlayingChar.RolePlayingCharacter;

public class Mage extends RolePlayingCharacter implements IGivesExperienceWhenKilled, IDropsGoldWhenKilled {
    public Mage(String name) {
        super("Mage", name);
    }

    @Override
    public void addToCurrentHealth(double health) {
        super.addToCurrentHealth(health);
        if (super.getHealth() == 0){

        }
    }
}


