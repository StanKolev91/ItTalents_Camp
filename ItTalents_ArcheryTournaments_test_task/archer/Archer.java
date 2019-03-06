package oopTasks.bowOOP.archer;

import oopTasks.bowOOP.Archery;
import oopTasks.bowOOP.bow.IBow;

import java.util.Objects;

public abstract class Archer {
    private String name;
    private boolean isMale;
    private int age;
    private int yearsExp;
    private int contestsIn;
    protected String title;
    private static Archery archery;
    protected IBow bow;

    public Archer(String name, boolean isMale, int age, int yearsExp, String title,IBow bow) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.contestsIn = 0;
        this.title = title;
        if (isValidYearsExp(yearsExp)) {
            this.yearsExp = yearsExp;
        }else System.out.println("Invalid years of experience for "+this.title);
        this.bow = bow;
    }

    public abstract Archer getArcherWithCarbonBow();

    protected abstract boolean isValidYearsExp(int yearsExp);

    public abstract int getMissChance();

    public abstract int shoot();

    public abstract int getNumberOfArrows();

    public void goToTournament(){
        contestsIn++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Archer archer = (Archer) o;
        return isMale == archer.isMale &&
                age == archer.age &&
                yearsExp == archer.yearsExp &&
                contestsIn == archer.contestsIn &&
                Objects.equals(name, archer.name) &&
                Objects.equals(title, archer.title) &&
                Objects.equals(bow, archer.bow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isMale, age, yearsExp, contestsIn, title, bow);
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
    protected abstract void setArcherRank(String rank);

}
