package diabloGame.items;

import diabloGame.Drop.Dropable;
import diabloGame.rolePlayingChar.humanoids.Hero;

public interface Equiptables extends Dropable {
    void addBonuses(Hero player);
    void removeBonuses(Hero player);
    String  getItemType();
}

