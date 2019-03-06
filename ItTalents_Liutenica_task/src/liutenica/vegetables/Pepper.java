package liutenica.vegetables;

public class Pepper extends Vegetable {

    public Pepper() {
        super(VegetableNeededForLiutenica.PEPPER);
    }

    @Override
    public VegetableNeededForLiutenica getName() {
        return super.vegetableNeeded;
    }

    @Override
    public int getTreatmentTimeSec() {
        return 6;
    }

    @Override
    public Pepper getVeggieInstance() {
        return this;
    }
}
