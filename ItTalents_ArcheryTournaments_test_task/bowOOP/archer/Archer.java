package oopTasks.bowOOP.archer;

import oopTasks.bowOOP.Archery;

public abstract class Archer {
    private String name;
    private boolean isMale;
    private int age;
    private int yearsExp;
    private int contestsIn;
    private String title;
    private static Archery archery;

    public Archer(String name, boolean isMale, int age, int yearsExp, String title) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.contestsIn = 0;
        this.title = title;
        if (isValidYearsExp(yearsExp)) {
            this.yearsExp = yearsExp;
        }else System.out.println("Invalid years of experience for "+this.title);
    }

    protected abstract boolean isValidYearsExp(int yearsExp);

    public abstract int getMissChance();

    public abstract int shoot();

    public abstract int getNumberOfArrows();

    public void goToTournament(){
        contestsIn++;
    }

    @Override
    public String toString() {
        return this.name + " / " + this.age+" years old / "+this.title;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getTitle() {
        return title;
    }

    public int getYearsExp() {
        return yearsExp;
    }

    public boolean isMale() {
        return isMale;
    }
    public abstract String getBowMaterial();
}
