package oopTasks.bowOOP.bow;

public class CarbonBow extends Bow implements ISeniorBow {
    private int stability;
    private int bonusSight;

    public CarbonBow(String manufacturer, double weight, int tensileStrenght, int stability, int bonusSight) {
        super(manufacturer, weight, tensileStrenght, "Carbon");
        this.bonusSight = bonusSight;
        this.stability = stability;
    }

    @Override
    public int getBonuses() {
        return stability+bonusSight;
    }
}
