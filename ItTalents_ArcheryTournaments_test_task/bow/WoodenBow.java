package oopTasks.bowOOP.bow;

public class WoodenBow extends Bow {

    public WoodenBow(String manufacturer, double weight, int tensileStrenght) {
        super(manufacturer, weight, tensileStrenght, "wood");
    }

    @Override
    public int getBonuses() {
        return 0;
    }
}
