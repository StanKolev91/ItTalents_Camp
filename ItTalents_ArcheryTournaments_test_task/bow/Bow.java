package oopTasks.bowOOP.bow;

public abstract class Bow implements IBow {
    private String manufacturer;
    private double weight;
    private int tensileStrenght;
    private String material;

    public Bow(String manufacturer, double weight, int tensileStrenght, String material) {
        this.manufacturer = manufacturer;
        this.weight = weight;
        this.tensileStrenght = tensileStrenght;
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public abstract int getBonuses();
}
