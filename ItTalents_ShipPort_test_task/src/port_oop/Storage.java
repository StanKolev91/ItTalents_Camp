package port_oop;

import java.util.concurrent.ArrayBlockingQueue;

public class Storage extends ArrayBlockingQueue<Package> {
    private static int storageId = 0;
    private int id;

    private static final int PACKAGE_POCESSING_TIME_SEC = 5;

    public Storage(int capacity) {
        super(capacity);
        this.id = ++storageId;
    }

    void processPackage() {
        try {
            Package pack = super.take();
            System.out.println("\t\t\t\t"+Thread.currentThread().getName()+ " start processing package "+pack.getId());
            Thread.sleep(PACKAGE_POCESSING_TIME_SEC*1000);

            System.out.println("\t\t\t\t\t"+Thread.currentThread().getName()+" processed successfully package "+ pack.getId());
        } catch (InterruptedException e) {
            System.out.println("Worker interrupted while processing package!");
        }
    }

    public int getId() {
        return id;
    }
}
