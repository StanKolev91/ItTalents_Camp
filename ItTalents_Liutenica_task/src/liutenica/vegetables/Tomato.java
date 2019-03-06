package liutenica.vegetables;

public class Tomato extends Vegetable {

    public Tomato( ) {
        super(VegetableNeededForLiutenica.TOMATO);
    }

    @Override
    public VegetableNeededForLiutenica getName() {
        return super.vegetableNeeded;
    }

    @Override
    public int getTreatmentTimeSec() {
        return 3;
    }

    @Override
    public Tomato getVeggieInstance() {
        return this;
    }
}
