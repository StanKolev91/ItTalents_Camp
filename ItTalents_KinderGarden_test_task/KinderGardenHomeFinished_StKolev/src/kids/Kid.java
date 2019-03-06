package Test3_StKolev.kids;

import Test3_StKolev.Demo;
import Test3_StKolev.KinderGarden;

public abstract class Kid extends Thread {
    private static final int SING_TIME_SEC = 3;
    protected volatile static KinderGarden garden;
    private Boolean isYoung;
    private KinderGarden.Group group;
    private String groupName;

    Kid(String name, Boolean isYoung, String groupName) {
        super(name);
        this.isYoung = isYoung;
        this.groupName = groupName;
    }

    public static void setGarden(KinderGarden garden) {
        Kid.garden = garden;
    }

    public String getGroupName() {
        return groupName;
    }

    public void enterGroup(KinderGarden.Group group) {
        if (this.group == null) {
            this.group = group;
        }
    }

    public enum drawFigure {
        SQUARE, CIRCLE, STAR, TRIANGLE
    }

    private void goToGarten() {
        try {
            sleep(Demo.random(1, 3) * 1000);
            garden.enter(this);
            System.out.println(this.getName() + " arrived in garden.");
        } catch (InterruptedException e) {
            System.out.println("Kid interrupted while going to garden");
        }

        synchronized (garden) {
            garden.notifyAll();
        }
    }

    private void sing() {
        System.out.println("\t" + this.getName() + " is singing!");
        try {
            sleep(SING_TIME_SEC * 1000);
        } catch (InterruptedException e) {
            System.out.println("Kid was interrupted while singing!");
        }
    }

    abstract KinderGarden.Drawing paint() throws InterruptedException;

    private void putOnTable(KinderGarden.Drawing drawing) {
        garden.putDrawingOnTable(drawing);
    }

    KinderGarden.Group getGroup() {
        return group;
    }

    static KinderGarden getGarden() {
        return garden;
    }

    protected abstract void tellTeacherAboutPainting(KinderGarden.Drawing drawing, int drawTime, Kid kid);

    @Override
    public void run() {
        goToGarten();
        while (this.group == null && !KinderGarden.isOver()) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("Kid interrupted while waiting for teacher");
                }
            }
        }

        while (!KinderGarden.isOver()) {
            while (group.getExercise() == null && !KinderGarden.isOver()) {
                synchronized (group.groupLock) {
                    try {
                        group.groupLock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Kid interrupted while waiting for excersise to start.");
                    }
                }
            }

            if (group.getExercise() != null) {

                switch (group.getExercise()) {
                    case DRAW:
                        KinderGarden.Drawing drawing = null;
                        try {
                            drawing = paint();
                        } catch (InterruptedException e) {
                            System.out.println("Kid interrupted while painting.");
                        }
                        if (drawing != null) {
                            putOnTable(drawing);
                        }
                        break;
                    case SING:
                        sing();
                        break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.getName() +"("+ getGroupName()+")";
    }
}
