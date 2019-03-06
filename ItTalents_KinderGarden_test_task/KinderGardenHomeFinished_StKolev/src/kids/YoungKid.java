package Test3_StKolev.kids;

import Test3_StKolev.Demo;
import Test3_StKolev.KinderGarden;
import Test3_StKolev.groups.YoungGroup;

public class YoungKid extends Kid {

    public YoungKid(String name) {
        super(name, true, YoungGroup.groupName.values()[Demo.random(0,YoungGroup.groupName.values().length-1)].toString());
    }

    @Override
    KinderGarden.Drawing paint() throws InterruptedException {
        System.out.println("\t\t\t"+getName() + " start looking for drawing to paint on.");

        KinderGarden.Drawing drawing = getGarden().getDrawing();
        if (drawing != null) {
            if (!drawing.getIsPainted()) {
                int time = Demo.random(1, 3);
                sleep(time * 1000);
                drawing.paint();
                System.out.println("\t\t\t\t"+this.getName()+" painted drawing "+ drawing.getId());
                tellTeacherAboutPainting(drawing, time, this);
            }else sleep(5000);
        }
        return drawing;
    }

    @Override
    protected void tellTeacherAboutPainting(KinderGarden.Drawing drawing, int drawTime, Kid kid) {
        getGroup().getTeacher().tell(drawing, drawTime, this, this.getGroup() );
    }
}
