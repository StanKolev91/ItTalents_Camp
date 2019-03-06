package liutenica.vegetables;

import java.util.Objects;

public abstract class Vegetable implements IVegetable {

    protected VegetableNeededForLiutenica vegetableNeeded;

    public enum VegetableNeededForLiutenica {

        TOMATO(5),PEPPER(5),EGGPLANT(5);

        int quantityNeededForLiutenica;

        VegetableNeededForLiutenica(int quantityNeededForLiutenica){
            this.quantityNeededForLiutenica = quantityNeededForLiutenica;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vegetable vegetable = (Vegetable) o;
        return vegetableNeeded == vegetable.vegetableNeeded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vegetableNeeded);
    }

    Vegetable(VegetableNeededForLiutenica vegetable){
        this.vegetableNeeded = vegetable;
    }

    public int getQuantityNeededForLiutenica() {
        return this.vegetableNeeded.quantityNeededForLiutenica;
    }

    @Override
    public String toString() {
        return this.vegetableNeeded.toString();
    }
}
