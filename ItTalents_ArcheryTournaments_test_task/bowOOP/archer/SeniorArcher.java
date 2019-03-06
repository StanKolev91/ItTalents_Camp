package oopTasks.bowOOP.archer;

import oopTasks.bowOOP.Demo;
import oopTasks.bowOOP.bow.ISeniorBow;

public class SeniorArcher extends Archer {
    private ISeniorBow bow;

    public SeniorArcher(String name, boolean isMale, int age, int yearsExp, ISeniorBow bow) {
        super(name, isMale, age, yearsExp, "Senior");
        this.bow = bow;
    }

    @Override
    protected boolean isValidYearsExp(int yearsExp) {
        if (yearsExp<10&&yearsExp>=3){
            return true;
        }
        return false;
    }

    @Override
    public int getMissChance() {
        return 5;
    }

    @Override
    public int shoot() {
        if (Demo.random(1, 100) > getMissChance()) {
            int score = Demo.random(6, 10) + bow.getBonuses();
            if (score > 10) {
                return 10;
            }
            return score;
        } else return 0;
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
