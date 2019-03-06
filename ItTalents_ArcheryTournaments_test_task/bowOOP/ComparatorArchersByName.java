package oopTasks.bowOOP;

import oopTasks.bowOOP.archer.Archer;

import java.util.Comparator;

public class ComparatorArchersByName implements Comparator<Archer> {
    @Override
    public int compare(Archer o1, Archer o2) {
        if (o1.getName().equalsIgnoreCase(o2.getName())){
            return o1.getAge()-o2.getAge();
        }
        return o1.getName().compareTo(o2.getName());
    }
}
