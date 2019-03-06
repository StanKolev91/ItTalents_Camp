package port_oop;

public class Package {
    private static int packageId = 0;
    private int id;

    Package(){
        this.id = ++Package.packageId;
    }

    public int getId() {
        return id;
    }
}
