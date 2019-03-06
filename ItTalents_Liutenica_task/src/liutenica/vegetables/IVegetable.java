package liutenica.vegetables;

public interface IVegetable {

    Vegetable.VegetableNeededForLiutenica getName();
    int getTreatmentTimeSec();
    boolean equals(Object o);
    int hashCode();
    Vegetable getVeggieInstance();
}
