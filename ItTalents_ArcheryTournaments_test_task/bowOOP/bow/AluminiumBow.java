package oopTasks.bowOOP.bow;

public class AluminiumBow extends Bow implements ISeniorBow {
    private int bonusSight;

    public AluminiumBow(String manufacturer, double weight, int tensileStrenght, int bonusSight) {
        super(manufacturer, weight, tensileStrenght, "aluminium");
        this.bonusSight = bonusSight;
    }

    @Override
    public int getBonuses() {
        return bonusSight;
    }
}
