package port_oop;

import java.util.ArrayList;
import java.util.List;

public class Crane extends Thread {
    private static int craneId = 0;
    private static final int PACKAGE_UNLOADING_TIME_SEC = 2;
    private Port port;
    private int id;
    private Port.Dock currentDock;

    Crane(Port port, String name) {
        this.id = ++Crane.craneId;
        this.port = port;
        super.setName(name);
    }

    private List<Package> unloadShip(Ship ship) {
        List<Package> packages = ship.getPackages();

        try {
            Thread.sleep(packages.size() * Crane.PACKAGE_UNLOADING_TIME_SEC * 1000);
        } catch (InterruptedException e) {
            System.out.println("Crane " + this.getName() + " was interrupted while unloading!");
        }
        return packages;
    }

    @Override
    public void run() {
        List<Port.Dock> docks = port.shipDocks();

        while (!isInterrupted()) {

            for (Port.Dock dock : docks) {
                synchronized (this) {
                    currentDock = dock;
                    if (currentDock.shipInLock.isLocked() && !currentDock.craneInLock.isLocked()) {
                        currentDock.craneInLock.lock();
                    }
                }
                if (currentDock.craneInLock.isHeldByCurrentThread()) {
                    if (dock.setCraneForUnloading(this)) {
                        Ship shipForUnload = dock.getShipInDock();
                        List<Package> packages = unloadShip(shipForUnload);
                        port.storage(packages);
                        shipForUnload.unloaded();

                        synchronized (dock.shipInLock) {
                            dock.shipInLock.notifyAll();
                            shipForUnload.interrupt();
                        }
                    }
                }
            }
            try {
                while (port.getShipsInPort().get() < 1) {
                    synchronized (port) {
                        System.out.println("\t\t"+this.getName() + " is waiting for ships.");
                        port.wait();
                        System.out.println(this.getName() + " start checking the docks.");
                    }
                }

            } catch (InterruptedException e) {
                System.out.println(this.getName() + " was interrupted.");
            }

        }
    }

    public int getCraneIdId() {
        return id;
    }
}
