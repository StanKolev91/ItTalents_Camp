package liutenica.vegetables;

public class Eggplant extends Vegetable {
    public Eggplant() {
        super(VegetableNeededForLiutenica.EGGPLANT);
    }

    @Override
    public VegetableNeededForLiutenica getName() {
        return super.vegetableNeeded;
    }

    @Override
    public int getTreatmentTimeSec() {
        return 9;
    }

    @Override
    public Eggplant getVeggieInstance() {
        return this;
    }
}
