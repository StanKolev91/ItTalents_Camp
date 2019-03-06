package oopTasks.bowOOP;

import java.util.Comparator;

public class ComparatorIntegerScore implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2)*-1;
    }
}
