package Test3_StKolev.kids;

import Test3_StKolev.Demo;
import Test3_StKolev.KinderGarden;
import Test3_StKolev.groups.OlderGroup;

public class OldKid extends Kid {

    public OldKid(String name) {
        super(name, false, OlderGroup.groupName.values()[Demo.random(0, OlderGroup.groupName.values().length - 1)].toString());
    }

    @Override
    KinderGarden.Drawing paint() throws InterruptedException {
        System.out.println("\t\t" + getName() + " starts drawing.");
        int time = Demo.random(1, 3);
        sleep(time * 1000);
        KinderGarden.Drawing drawing = new KinderGarden.Drawing();
        tellTeacherAboutPainting(drawing, time, this);
        return drawing;
    }

    @Override
    protected void tellTeacherAboutPainting(KinderGarden.Drawing drawing, int drawTime, Kid kid) {
        getGroup().getTeacher().tell(drawing, drawTime, this, this.getGroup());
    }
}
