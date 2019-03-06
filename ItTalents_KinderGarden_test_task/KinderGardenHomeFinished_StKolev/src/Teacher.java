package Test3_StKolev;

import Test3_StKolev.kids.Kid;
import Test3_StKolev.kids.OldKid;
import Test3_StKolev.kids.YoungKid;
import Test3_StKolev.mysql.DBManager;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

public class Teacher extends Thread {
    private static KinderGarden garden;
    private KinderGarden.Group group;
    private static volatile ConcurrentSkipListMap<Integer, KinderGarden.Record> records;
    private final ReentrantLock recordLock = new ReentrantLock();

    Teacher(String name, KinderGarden.Group group) {
        setName(name);
        this.group = group;
        records = new ConcurrentSkipListMap<>();
    }

    static void setGarden(KinderGarden garden) {
        Teacher.garden = garden;
    }

    private void walkKidToGroup() {
        Kid kid = null;
        kid = garden.getKidFromWaitingList(this);
        if (kid != null) {
            garden.putKidInGroup(this, kid);
            synchronized (kid) {
                kid.notifyAll();
            }
        }
    }

    private void startGame() {
        if (new Random().nextBoolean()) {
            group.setExercise(this, KinderGarden.Group.exercise.DRAW);
            System.out.println("\t\t\t\t\t" + group.getName() + " starts drawing.");
        } else {
            group.setExercise(this, KinderGarden.Group.exercise.SING);
            System.out.println("\t\t\t\t\t" + group.getName() + " starts singing.");
        }
    }

    @Override
    public void run() {
        while (!KinderGarden.isOver()) {

            if (!group.groupLock.isLocked()) {
                group.groupLock.lock();
            }

            if (group.groupLock.isHeldByCurrentThread()) {
                if (!garden.getKidsNotInGroups().isEmpty()) {
                    walkKidToGroup();
                }
                group.groupLock.unlock();
            }
            try {
                if (group.getExercise() == null && group.size() >= 10) {
                    startGame();
                    synchronized (group.groupLock) {
                        group.groupLock.notifyAll();
                    }
                }

                synchronized (garden) {
                    if (garden.getKidsNotInGroups().isEmpty()) {
                        garden.wait();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Teacher interrupted during exercise!");
            }
        }
        synchronized (group.groupLock){
            group.groupLock.notifyAll();
        }
        System.out.println("Day is over for teacher " + getName());
    }

    public void tell(KinderGarden.Drawing drawing, int drawTime, OldKid oldKid, KinderGarden.Group group) {
        synchronized (recordLock) {
            int id = -1;
            if (!records.containsKey(drawing.getId())) {
                KinderGarden.Record record = new KinderGarden.Record(LocalDate.now()).withDrawer(oldKid).withDawTime(drawTime).withDrawerGroup(group).withFigurine(drawing.getFigurine());
                id = DBManager.insert(record);
                drawing.setId(id);
                records.put((id), record);
            }
        }
    }

    public void tell(KinderGarden.Drawing drawing, int drawTime, YoungKid youngKid, KinderGarden.Group group) {
        DBManager.update(drawing.getId(), records.get(drawing.getId()).withPainter(youngKid).withPainterGroup(group).withPaintTime(drawTime));
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name: " + getName() +
                ", group: " + group.getName() +
                '}';
    }
}
