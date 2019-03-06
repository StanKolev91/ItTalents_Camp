package Test3_StKolev;

import Test3_StKolev.groups.OlderGroup;
import Test3_StKolev.groups.YoungGroup;
import Test3_StKolev.kids.Kid;
import Test3_StKolev.mysql.DBManager;
import Test3_StKolev.mysql.MYSQLConnection;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class KinderGarden {
    private static final int GROUP_CAPACITY = 100;
    private static final int TABLE_CAPACITY = 1000;
    private static final int GARDEN_DAY_SEC = 30;
    private volatile static boolean over;
    private Queue<Kid> waitingKids;
    private Group[] groups;
    private ArrayBlockingQueue<Drawing> table;
    private Teacher[] teachers;
    private AtomicInteger kidsWaiting;

    public KinderGarden(int groups) {
        KinderGarden.over = false;
        this.groups = new Group[groups];
        this.teachers = new Teacher[groups];
        this.table = new ArrayBlockingQueue<>(TABLE_CAPACITY);
        this.waitingKids = new LinkedList<>();
        kidsWaiting = new AtomicInteger(0);
        Teacher.setGarden(this);
        Kid.setGarden(this);
        for (int i = 0; i < groups; i++) {
            if ((i < 2) && i % 2 == 0) {
                this.groups[i] = new OlderGroup(GROUP_CAPACITY, OlderGroup.groupName.FROGS);
            } else if (i < groups / 2) {
                this.groups[i] = new OlderGroup(GROUP_CAPACITY, OlderGroup.groupName.LADYBUG);
            }
            if (i > 1 && i % 2 == 0) {

                this.groups[i] = new YoungGroup(GROUP_CAPACITY, YoungGroup.groupName.DUCKS);
            } else if (i >= groups / 2) {

                this.groups[i] = new YoungGroup(GROUP_CAPACITY, YoungGroup.groupName.PENGUINS);
            }
            teachers[i] = new Teacher("Teacher " + (i + 1), this.groups[i]);
            this.groups[i].setTeacher(teachers[i]);
        }
    }

    public static boolean isOver() {
        return KinderGarden.over;
    }

    void start() {
        Thread clock = new Thread() {
            @Override
            public void run() {
                try {
                    DBManager.getInstance().start();
                    Thread.sleep(GARDEN_DAY_SEC * 1000);
                    KinderGarden.setDayOver();

                    synchronized (KinderGarden.this) {
                        KinderGarden.this.notifyAll();
                    }
                }
                catch (InterruptedException e) {
                    System.out.println("Clock interrupted while clocking.");
                }
                finally {
                    System.out.println("=====================");

                    for (Group group : groups) {
                        System.out.println(group + " / " + group.size());
                        System.out.println("Waiting kids: " + getKidsWaiting());
                        System.out.println("Waiting kids collection: " + KinderGarden.this.waitingKids.size());
                    }
                    System.out.println("=====================");

                    try {
                        sleep(10000);
                        DBManager.getInstance().print();
                        sleep(1000);
                        MYSQLConnection.getInstance().getConnection().close();
                    } catch (SQLException e) {
                        System.out.println("Error while trying to close connecton to DB: " + e.getMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (Group group : groups) {


                        for (Kid kid : group) {
                            System.out.println(kid.getName() + " is alive " + kid.isAlive());
                        }
                    }

                    for (Teacher teacher : teachers) {
                        System.out.println(teacher.getName() + " is alive " + teacher.isAlive());
                    }
                }
            }
        };
        clock.start();

        for (Teacher teacher : teachers) {
            teacher.start();
        }
    }

    private static void setDayOver() {
        KinderGarden.over = true;
    }

    Kid getKidFromWaitingList(Teacher teacher) {
        Kid kid = null;
        synchronized (this) {
            kid = waitingKids.poll();
        }
        return kid;
    }

    void putKidInGroup(Teacher teacher, Kid kid) {
        for (Group group : groups) {
            if (group.getName().equalsIgnoreCase(kid.getGroupName())) {
                try {
                    synchronized (this) {
                        group.put(kid);
                    }
                    kidsWaiting.decrementAndGet();
                    kid.enterGroup(group);
                    synchronized (kid) {
                        kid.notifyAll();
                    }
                    System.out.println(teacher.getName() + " put " + kid.getName() + " in " + group.getName());
                } catch (InterruptedException e) {
                    System.out.println("Teacher interrupted while waiting to put kid in a group");
                }
            }
        }
    }

    int getKidsWaiting() {
        return kidsWaiting.get();
    }

    public void putDrawingOnTable(Drawing drawing) {
        try {
            table.put(drawing);
            System.out.println("\t\t\t\t\t" + Thread.currentThread() + " put drawing on the table");
        } catch (InterruptedException e) {
            System.out.println("Kid interrupted while putting his drawing on table.");
        }
    }

    public Drawing getDrawing() {

        Drawing drawing = null;
        try {
            drawing = table.take();
            System.out.println("\t\t\t\t" + Thread.currentThread().getName() + " get new painting.");
        } catch (InterruptedException e) {
            System.out.println("Young kid interrupted while waiting for drawing");
        }
        return drawing;
    }

    public void enter(Kid kid) {
        synchronized (this) {
            if (this.waitingKids.offer(kid)) {
                this.kidsWaiting.incrementAndGet();
            }
        }
    }

    Collection<Kid> getKidsNotInGroups() {
        return Collections.unmodifiableCollection(this.waitingKids);
    }

    public static class Drawing {
        private int id;
        private Kid.drawFigure drawing;
        private Boolean isPainted;

        public Drawing() {
            this.drawing = Kid.drawFigure.values()[Demo.random(0, Kid.drawFigure.values().length - 1)];
            this.isPainted = false;
        }

        public int getId() {
            return id;
        }

        public void paint() {
            isPainted = true;
        }

        public Boolean getIsPainted() {
            return isPainted;
        }

        @Override
        public String toString() {
            return "Drawing{" +
                    "id=" + id +
                    ", drawing=" + drawing +
                    ", isPainted=" + isPainted +
                    '}';
        }

        public void setId(int id) {
            this.id = id;

        }

        public String getFigurine() {
            return this.drawing.toString();
        }
    }

    public static class Record {
        private String drawBy = null;
        private int drawTime;
        private int paintTime;
        private String paintedBy = null;
        private String painterGroup = null;
        private String drawerGroup;
        private LocalDate drawDate;
        private String figurine;


        Record(LocalDate drawDate) {
            this.drawDate = drawDate;
        }

        Record withDrawer(Kid drawBy) {
            if (this.drawBy == null) {
                this.drawBy = drawBy.getName();
            }
            return this;
        }

        Record withDawTime(int drawTime) {
            if (this.drawTime == 0) {
                this.drawTime = drawTime;
            }
            return this;
        }

        Record withPainter(Kid paintedBy) {
            if (this.paintedBy == null) {
                this.paintedBy = paintedBy.getName();
            }
            return this;
        }

        Record withPaintTime(int paintTime) {
            this.paintTime = paintTime;
            return this;
        }

        Record withPainterGroup(Group painterGroup) {
            if (this.painterGroup == null) {
                this.painterGroup = painterGroup.getName();
            }
            return this;
        }

        Record withDrawerGroup(Group drawerGroup) {
            this.drawerGroup = drawerGroup.getName();
            return this;
        }

        public String getDrawBy() {
            return drawBy;
        }

        public int getDrawTime() {
            return drawTime;
        }

        public String getPaintedBy() {
            return paintedBy;
        }

        public String getPainterGroup() {
            return painterGroup;
        }

        public String getDrawerGroup() {
            return drawerGroup;
        }

        public LocalDate getDrawDate() {
            return drawDate;
        }

        @Override
        public String toString() {
            return "Record{" +
                    "drawBy='" + drawBy + '\'' +
                    ", drawTime=" + drawTime +
                    ", paintedBy=" + paintedBy +
                    ", painterGroup=" + painterGroup +
                    ", drawerGroup=" + drawerGroup +
                    ", drawDate=" + drawDate +
                    '}';
        }

        public int getPaintTime() {
            return this.paintTime;
        }

        public Record withFigurine(String figurine) {
            if (this.figurine == null) {
                this.figurine = figurine;
            }
            return this;
        }

        public String getFigurine() {
            return this.figurine;
        }
    }

    public abstract static class Group extends ArrayBlockingQueue<Kid> {
        private String name;
        private AtomicBoolean started;
        private Teacher teacher;
        private volatile exercise exercise;
        public final ReentrantLock groupLock = new ReentrantLock();

        public Group(int capacity, String name) {
            super(capacity);
            this.name = name;
            started = new AtomicBoolean(false);
        }

        @Override
        public String toString() {
            return "Group{" +
                    "name: '" + name + '\'' +
                    ", started: " + started +
                    ", teacher: " + teacher +
                    ", exercise: " + exercise +
                    "}\n";
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        void setExercise(Teacher teacher, Group.exercise exercise) {
            if (this.exercise == null) {
                this.exercise = exercise;
            }
        }

        public enum exercise {
            DRAW, SING
        }

        public Group.exercise getExercise() {
            return exercise;
        }

        public String getName() {
            return name;
        }

        public Teacher getTeacher() {
            return teacher;
        }
    }
}
