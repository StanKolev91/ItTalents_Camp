package diabloGame.items;

import diabloGame.Drop.Dropable;
import diabloGame.rolePlayingChar.humanoids.Hero;

public  class Potion extends Item implements Consumables, Dropable {
    private int restoredHealth;
    public Potion(){
        setItemType("Health potion");
        this.restoredHealth = this.getItemLvl()*25;
    }

    @Override
    public void itemInfo() {
        System.out.println(this.getItemType().substring(0, 1).toUpperCase() + this.getItemType().substring(1));
        System.out.println("Consuming this will restore " + restoredHealth + " health.Can be Used in combat");
    }

    @Override
    public void consume(Hero player) {
        System.out.println("You just consumed a potion, you restored "+this.restoredHealth+" health.");
        player.addToCurrentHealth(player.getHealth()+this.restoredHealth);
    }
}
