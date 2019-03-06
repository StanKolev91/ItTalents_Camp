package port_oop;

import mysql.DBManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
    private static int dockId = 0;
    private Dock[] docks;
    private Crane[] cranes;
    private Worker[] workers;
    private Storage[] storages;
    private ConcurrentSkipListSet<Record> records;
    private AtomicInteger shipsWaitingInPort;
    private AtomicInteger shipsInPort;

    Port(int docks, int cranes, int workers, int storages) {
        this.shipsInPort = new AtomicInteger(0);
        this.shipsWaitingInPort = new AtomicInteger(0);
        records = new ConcurrentSkipListSet<>();
        this.docks = new Dock[docks];
        this.cranes = new Crane[cranes];
        this.storages = new Storage[storages];
        this.workers = new Worker[workers];

        for (int i = 0; i < docks; i++) {
            this.docks[i] = new Dock();
        }

        for (int i = 0; i < cranes; i++) {
            this.cranes[i] = new Crane(this, "Crane " + (i + 1));
        }

        for (int i = 0; i < storages; i++) {
            this.storages[i] = new Storage(100);
        }

        for (int i = 0; i < workers; i++) {
            this.workers[i] = new Worker("Worker " + (i + 1), this.storages);
        }

        DBManager.getInstance().setDaemon(true);
        DBManager.getInstance().start();
    }

    void storage(List<Package> packages) {
        int storageNumber = DemoPort.random(0, storages.length - 1);
        for (Package pack : packages) {
            try {
                this.storages[storageNumber].put(pack);
                System.out.println("\t\t\t" + Thread.currentThread().getName() + " put package " + pack.getId() + " in storage " + storages[storageNumber].getId());
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting on storage");
            }
        }

    }

    List<Dock> shipDocks() {
        return Collections.unmodifiableList(Arrays.asList(docks));
    }

    void start() {
        for (Crane crane : cranes) {
            crane.start();
            System.out.println(crane.getName() + " started!");
        }
        for (Worker worker : workers) {
            worker.start();
            System.out.println(worker.getName() + " started!");
        }
    }

    Dock getDock() {
        int dockNumber = DemoPort.random(0, docks.length - 1);
        return docks[dockNumber];
    }

    AtomicInteger getShipsInPort() {
        return shipsInPort;
    }

    public class Dock {
        private AtomicBoolean isBusy;
        private Port port = Port.this;
        private Ship shipInDock;
        private Crane workingCrane;
        private int id;
        private BlockingQueue<Ship> waitingShips;
        private Record currentRecord;
        final ReentrantLock shipInLock = new ReentrantLock();
        final ReentrantLock craneInLock = new ReentrantLock();

         Dock() {
            this.isBusy = new AtomicBoolean(false);
            this.id = ++Port.dockId;
            this.waitingShips = new LinkedBlockingQueue<>();

            Thread coordinator = new Thread() {
                @Override
                public void run() {

                    while (!isInterrupted()) {

                        if (Dock.this.shipInDock == null && !Dock.this.waitingShips.isEmpty()) {

                            try {
                                setShipInDock(Dock.this.waitingShips.take());
                                Port.this.shipsWaitingInPort.decrementAndGet();
                                synchronized (port) {
                                    port.notifyAll();
                                }
                                synchronized (Dock.this.craneInLock) {
                                    Dock.this.craneInLock.notifyAll();
                                    while (Port.this.shipsWaitingInPort.get() < 1) {
                                        System.out.println("\t\t\tCoordinator is waiting for ship");
                                        Dock.this.craneInLock.wait();
                                    }
                                }
                                System.out.println("\t\tCoordinator put new ship in dock " + Dock.this.id);
                            } catch (InterruptedException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
            };
            coordinator.setDaemon(true);
            coordinator.start();
        }

        void setShipInDock(Ship shipInDock) {
            this.shipInDock = shipInDock;
        }

        void createRecord() {
            this.currentRecord = new Record(LocalDateTime.now());
            this.currentRecord.withShip(shipInDock);
            this.currentRecord.withPackages(shipInDock.getPackages());
            this.currentRecord.withDock(this);
            this.currentRecord.withCrane(this.workingCrane);
        }

        void enter(Ship ship) {
            try {
                waitingShips.put(ship);
                port.shipsWaitingInPort.incrementAndGet();
                port.shipsInPort.incrementAndGet();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            synchronized (this.craneInLock) {
                this.craneInLock.notifyAll();
            }
            synchronized (port) {
                port.notifyAll();
            }

        }

        Boolean setCraneForUnloading(Crane crane) {
            if (shipInDock != null && shipInDock.isAlive()) {
                this.workingCrane = crane;
                createRecord();
                System.out.println("\t" + Thread.currentThread().getName() + " start unloading " +
                        shipInDock.getName() + " with " +
                        shipInDock.getPackages().size() + " packages");
                return true;
            } else return false;

        }

        void clear() {
            addRecordToLog(currentRecord);
            System.out.println("\t\t\t"+workingCrane.getName()+ " and "+ shipInDock.getName() +" left Dock "+ this.id );
            this.workingCrane = null;
            this.shipInDock = null;
            port.shipsInPort.decrementAndGet();

        }

        Ship getShipInDock() {
            return this.shipInDock;
        }

        void showRecords() {
            System.out.println("\n===========================");
            System.out.println("Records in log:\n");
            for (Record record1 : records) {
                System.out.println("\t" + record1);
            }
            System.out.println("===========================\n");

        }

        void addRecordToLog(Record record) {
            Port.this.records.add(record);
            DBManager.insert(record);
            showRecords();
        }

        public int getId() {
            return this.id;
        }
    }

    public class Record implements Comparable<Record> {
        private LocalDateTime dateTime;
        private Ship shipUnloaded;
        private Crane unloadingCrane;
        private List<Package> packages;
        private Dock dock;

        private Record(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            packages = new ArrayList<>();
        }

        void withShip(Ship ship) {
            if (this.shipUnloaded == null) {
                this.shipUnloaded = ship;
            }
        }

        void withCrane(Crane crane) {
            if (unloadingCrane == null) {
                this.unloadingCrane = crane;
            }
        }

        void withPackages(List<Package> packages) {
            this.packages.addAll(packages);

        }

        void withDock(Dock dock) {
            if (this.dock == null) {
                this.dock = dock;
            }
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss / MM:dd:YYYY");
            return "Record{" +
                    "dateTime: " + dateTime.format(formatter) +
                    ", shipUnloaded: " + this.shipUnloaded.getName() +
                    ", unloadingCrane: " + unloadingCrane.getName() +
                    ", packages: " + packages.size() +
                    ", dock " + dock.id +
                    '}';
        }

        @Override
        public int compareTo(Record o) {
            return this.dateTime.compareTo(o.dateTime);
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public Ship getShipUnloaded() {
            return shipUnloaded;
        }

        public Crane getUnloadingCrane() {
            return unloadingCrane;
        }

        public List<Package> getPackages() {
            return packages;
        }

        public Dock getDock() {
            return dock;
        }
    }
}
