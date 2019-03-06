package port_oop;

public class Worker extends Thread {
    private Storage[] storages;

    Worker(String name, Storage[] storages) {
        this.storages = storages;
        super.setName(name);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            for (Storage storage : storages)
                storage.processPackage();
        }
    }
}
