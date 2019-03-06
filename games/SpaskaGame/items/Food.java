package diabloGame.items;

import diabloGame.rolePlayingChar.humanoids.Hero;

abstract class Food extends Item implements Consumables {

    Food(){
        super.setItemType("Food");
    }
    @Override
    public void consume(Hero player) {
        player.heal();
    }

    public void itemInfo(){
        System.out.println("Eating this will restore all of your health. \nUsable out of combat.");
    }
}
