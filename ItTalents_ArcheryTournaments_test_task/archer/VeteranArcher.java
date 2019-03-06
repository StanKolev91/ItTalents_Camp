package oopTasks.bowOOP.archer;

import oopTasks.bowOOP.Demo;
import oopTasks.bowOOP.bow.CarbonBow;

public class VeteranArcher extends SeniorArcher {
    private CarbonBow bow;

    public VeteranArcher(String name, boolean isMale, int age, int yearsExp, CarbonBow bow) {
        super(name, isMale, age, yearsExp, bow);
        super.setArcherRank("Veteran");
        this.bow = bow;
    }

    @Override
    public Archer getArcherWithCarbonBow() {
        return this;
    }

    @Override
    protected boolean isValidYearsExp(int yearsExp) {
        if (yearsExp >= 10 && yearsExp < 60) {
            return true;
        }
        return false;
    }

    @Override
    public int getMissChance() {
        return 0;
    }

    @Override
    public int getNumberOfArrows() {
        return 60;
    }

    @Override
    public String getBowMaterial() {
        return bow.getMaterial();
    }
}
