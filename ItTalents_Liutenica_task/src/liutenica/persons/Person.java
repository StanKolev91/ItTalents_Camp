package liutenica.persons;

import liutenica.Liutenica;
import liutenica.LiutenicaDemo;
import liutenica.vegetables.IVegetable;
import liutenica.vegetables.Vegetable;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Person extends Thread implements IWorker {

    public static class Baraka {
        private static Pisar pisar;
        private ConcurrentHashMap<IVegetable, AtomicInteger> koshnici;
        private ConcurrentHashMap<IVegetable, AtomicInteger> tavi;
        private final ReentrantLock koshniciLock = new ReentrantLock();
        private final ReentrantLock taviLock = new ReentrantLock();
        private final ReentrantLock taviLockGet = new ReentrantLock(true);
        private static final int MAX_VEGETABLES_IN_BASKET = 40;
        private static final int MAX_VEGETABLES_IN_PAN = 30;

        public Baraka() {
            this.koshnici = new ConcurrentHashMap<>();
            this.tavi = new ConcurrentHashMap<>();
        }

        public static void setPisar(Pisar pisar) {
            if (Baraka.pisar == null) {
                Baraka.pisar = pisar;
            } else System.out.println("You have a pisar, already");
        }

        static Pisar getPisar() {
            return pisar;
        }
    }

    int age;
    static Baraka barachka;

    Person(String name, int age) throws InvalidAgeException {
        if (!isValidAge(age)) {
            throw new InvalidAgeException();
        }
        this.age = age;
        setName(name);
    }

    @Override
    public Person getInPerson() {
        return this;
    }

    protected abstract boolean isMale();

    protected abstract boolean isValidAge(int age);

    public static void setBarachka(Baraka barachka) {
        Person.barachka = barachka;
    }

    void showQuantities() {
        System.out.println("=============== BASKETS ===============");
        for (Map.Entry<IVegetable, AtomicInteger> entry : barachka.koshnici.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue().get());
        }

        System.out.println("=============== PANS ===============");
        for (Map.Entry<IVegetable, AtomicInteger> entry : barachka.tavi.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue().get());
        }
    }

    IVegetable putVegetableToPan(IVegetable vegetable) {
        synchronized (barachka.taviLock) {
            if (!barachka.tavi.containsKey(vegetable)) {
                barachka.tavi.put(vegetable, new AtomicInteger(0));
            }
        }
        try {
            while (barachka.tavi.get(vegetable).compareAndSet(30, 30)) {
                synchronized (barachka.taviLock) {
                    barachka.taviLock.wait();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted waiting to put vegetable in pan!");
        }

        barachka.tavi.get(vegetable).incrementAndGet();

        synchronized (barachka.taviLock) {
            barachka.taviLock.notifyAll();
        }return vegetable;
    }

    Liutenica makeLiutenica() {
        int readyState = 0;

        do {
            synchronized (barachka.taviLock) {
                try {
                    barachka.taviLock.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (barachka.taviLockGet.tryLock()) {
                readyState = barachka.tavi
                        .entrySet()
                        .stream()
                        .mapToInt(e -> {
                            if (e.getValue().get() > e.getKey().getVeggieInstance().getQuantityNeededForLiutenica()) {
                                return 1;
                            } else return 0;
                        })
                        .reduce(0, (i1, i2) -> i1 + i2);

                if (readyState < Vegetable.VegetableNeededForLiutenica.values().length) {
                    if (barachka.taviLockGet.isHeldByCurrentThread() && barachka.taviLockGet.isLocked()) {
                        barachka.taviLockGet.unlock();
                    }
                }
            }
        } while (readyState < 3);
        System.out.println("Tavi lock " + barachka.taviLock);
        System.out.println("Tavi lokck get " + barachka.taviLockGet);
        for (Map.Entry<IVegetable, AtomicInteger> entry : barachka.tavi.entrySet()) {
            barachka.tavi.put(entry.getKey(), new AtomicInteger(entry.getValue().get() - 5));
        }

        if (barachka.taviLockGet.isHeldByCurrentThread() && barachka.taviLockGet.isLocked()) {
            barachka.taviLockGet.unlock();
        }
        synchronized (barachka.taviLock) {
            barachka.taviLock.notifyAll();
        }

        System.out.println(Thread.currentThread().getName() + " is starting to make liutenica");
        Liutenica liutenica = null;
        try {
            sleep(10000);
            liutenica = new Liutenica();
            System.out.println(Thread.currentThread().getName() + " made " + liutenica.getKilos() + " kilos liutenica");
        } catch (InterruptedException e) {
            System.out.println("Interrupted while making liutenica");
        }
        return liutenica;
    }

    IVegetable addNewVegetableToBasket(IVegetable vegetable) {
        synchronized (barachka.koshniciLock) {
            if (!barachka.koshnici.containsKey(vegetable)) {
                barachka.koshnici.put(vegetable, new AtomicInteger(0));
            }
        }
        while (barachka.koshnici.get(vegetable).get() >= 40) {
            try {
                synchronized (barachka.koshniciLock) {
                    barachka.koshniciLock.wait();
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        barachka.koshnici.get(vegetable).incrementAndGet();

        synchronized (barachka.koshniciLock) {
            barachka.koshniciLock.notifyAll();
        }
        return vegetable;
    }

    IVegetable getRandomVegetableForTreatment() {
        IVegetable vegetable = null;
        while (vegetable == null) {
            synchronized (barachka.koshniciLock) {
                Iterator<IVegetable> iterator = barachka.koshnici.keySet().iterator();
                for (int i = 0; i < LiutenicaDemo.random(0, barachka.koshnici.size()); i++) {
                    vegetable = iterator.next();
                }
                if (vegetable != null) {
                    if (barachka.koshnici.get(vegetable).compareAndSet(1, 0)) {
                        barachka.koshnici.remove(vegetable);
                    } else barachka.koshnici.get(vegetable).decrementAndGet();
                }
            }
        }
        synchronized (barachka.koshniciLock) {
            barachka.koshniciLock.notifyAll();
        }
        return vegetable;
    }
}
