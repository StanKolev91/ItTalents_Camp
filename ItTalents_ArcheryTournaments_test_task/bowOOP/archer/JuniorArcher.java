package oopTasks.bowOOP.archer;

import oopTasks.bowOOP.Demo;
import oopTasks.bowOOP.bow.WoodenBow;

public class JuniorArcher extends Archer {
    private WoodenBow bow;

    public JuniorArcher(String name, boolean isMale, int age, int yearsExp,WoodenBow bow) {
        super(name, isMale, age, yearsExp, "Junior");
        this.bow = bow;
    }

    @Override
    protected boolean isValidYearsExp(int yearsExp) {
        return true;
    }

    @Override
    public int getMissChance() {
        return 10;
    }

    @Override
    public int shoot() {
        if (Demo.random(1,100)>getMissChance()) {
            int score = Demo.random(6, 10) + bow.getBonuses();
            if (score > 10) {
                return 10;
            }
            return score;
        }else return 0;
    }

    @Override
    public int getNumberOfArrows() {
        return 30;
    }

    @Override
    public String getBowMaterial() {
        return bow.getMaterial();
    }
}
