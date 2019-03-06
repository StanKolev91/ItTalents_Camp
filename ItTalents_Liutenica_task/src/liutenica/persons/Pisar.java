package liutenica.persons;

import liutenica.Liutenica;
import liutenica.vegetables.IVegetable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;


public class Pisar extends Person {
    private ConcurrentSkipListMap<Person, ConcurrentSkipListMap<IVegetable, Integer>> vegetableProcessed;
    private ConcurrentSkipListMap<Person, HashSet<Liutenica>> kilosLiutenicaMade;

    public Pisar(String name, int age) throws InvalidAgeException {
        super(name, age);
        this.setDaemon(true);
        this.vegetableProcessed = new ConcurrentSkipListMap<>((p1, p2) -> {
            int result = p1.getName().compareTo(p2.getName());
            if (result == 0) {
                return p1.age - p2.age;
            }
            return result;
        });
        this.kilosLiutenicaMade = new ConcurrentSkipListMap<>((p1, p2) -> {
            int result = p1.getName().compareTo(p2.getName());
            if (result == 0) {
                return p1.age - p2.age;

            }
            return result;
        });
    }

    @Override
    protected boolean isMale() {
        return true;
    }

    void writeDown(IVegetable vegetable, IVegetableProcesser worker) {
        Person workerToBeWriteDown = worker.getInPerson();
        vegetableProcessed.putIfAbsent(workerToBeWriteDown, new ConcurrentSkipListMap<>(Comparator.comparing(IVegetable::getName)));
        vegetableProcessed.get(workerToBeWriteDown).putIfAbsent(vegetable, 0);
        vegetableProcessed.get(workerToBeWriteDown).put(vegetable, vegetableProcessed.get(workerToBeWriteDown).get(vegetable) + 1);
    }

    void writeDown(Liutenica liutenica, ILiutenicaMaker worker) {
        Person workerToBeWriteDown = worker.getInPerson();
        kilosLiutenicaMade.putIfAbsent(workerToBeWriteDown, new HashSet<>());
        synchronized (this) {
            kilosLiutenicaMade.get(workerToBeWriteDown).add(liutenica);
        }
    }

    @Override
    protected boolean isValidAge(int age) {
        return age >= 50 && age <= 120;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("sorry, " + getName() + e.getMessage());
            }
            showQuantities();
//            for (Map.Entry<Person, ConcurrentSkipListMap<IVegetable, Integer>> entry : vegetableProcessed.entrySet()) {
//                System.out.println(entry.getKey().getName());
//                for (Map.Entry<IVegetable, Integer> entry1 : entry.getValue().entrySet()) {
//                    System.out.println("\t" + entry1.getKey() + " - " + entry1.getValue());
//                    if (entry.getKey().isMale()) {
//                        System.out.println("Time spent processing " + entry1.getKey() + " - " + (entry1.getKey().getTreatmentTimeSec() * entry1.getValue()));
//                    }
//                }
//            }

            System.out.println("\nGirl with most picked vegetables: ");
            vegetableProcessed
                    .entrySet()
                    .stream()
                    .filter(e -> !e.getKey().isMale())
                    .max((e1, e2) ->
                            e1.getValue()
                                    .entrySet()
                                    .stream()
                                    .max(Comparator.comparing(Map.Entry::getValue))
                                    .get()
                                    .getValue()
                                    -
                                    e2.getValue()
                                            .entrySet()
                                            .stream()
                                            .max(Comparator.comparing(Map.Entry::getValue))
                                            .get()
                                            .getValue())
                    .ifPresent(e -> System.out.println(
                            e.getKey().getName()
                                    + " - "
                                    + e.getValue()
                                    .entrySet()
                                    .stream()
                                    .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                                    .get()));
            System.out.println("\nBoy that spent least time processing vegetables: ");
            vegetableProcessed
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey().isMale())
                    .min((m1, m2) ->
                            m1.getValue()
                                    .entrySet()
                                    .stream()
                                    .mapToInt((e1) ->
                                            e1.getKey().getTreatmentTimeSec() * e1.getValue())
                                    .reduce(0, (i1, i2) -> i1 + i2)
                                    -
                                    m2.getValue()
                                            .entrySet()
                                            .stream()
                                            .mapToInt((e1) ->
                                                    e1.getKey().getTreatmentTimeSec() * e1.getValue())
                                            .reduce(0, (i1, i2) -> i1 + i2)
                    )
                    .ifPresent(p -> System.out.println(p.getKey().getName() + " - " + p.getValue()));
            System.out.println("Most picked vegetable: ");

        }
    }
}
