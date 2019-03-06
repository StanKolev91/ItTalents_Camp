package diabloGame.items;

import diabloGame.Drop.Dropable;

public class Trasch extends Item implements Dropable {
    public Trasch(){
        this.setItemType("Trasch");
        this.setSellPrice(1);
    }

    @Override
    public void itemInfo() {
        System.out.println("Trasch\nRandom trasch, sells for some value");
    }
}
