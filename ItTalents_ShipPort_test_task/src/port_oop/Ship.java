package port_oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ship extends Thread {
    private String name;
    private Port port;
    private Port.Dock dock;
    private ArrayList<Package> packages;

    public Ship(String name, Port port) {
        setName(name);
        this.port = port;
        this.packages = new ArrayList<>();
        for (int i = 0; i < DemoPort.random(1, 4); i++) {
            this.packages.add(new Package());
        }
        System.out.println(getName() + " - new ship with " + packages.size() + " packages");
    }

    private void enterPort() {
        try {
            synchronized (this) {
                this.dock = this.port.getDock();
                this.dock.shipInLock.lock();
            }
            if (this.dock.shipInLock.isHeldByCurrentThread()) {
                this.dock.enter(this);

                synchronized (this.dock.shipInLock) {
                    while (!isInterrupted() && this.packages.size()>0) {
                        this.dock.shipInLock.wait();
                    }
                }
            }

        } catch (InterruptedException e) {
            System.out.println(getName()+" was interrupted while being unloaded!");
        } finally {
            if (this.dock.shipInLock.isHeldByCurrentThread()) {
                this.dock.shipInLock.unlock();
            }
        }
    }

    List<Package> getPackages() {
        return Collections.unmodifiableList(packages);
    }

    void unloaded() {
        this.packages.clear();
        this.dock.clear();
    }

    @Override
    public void run() {
        enterPort();
    }
}
