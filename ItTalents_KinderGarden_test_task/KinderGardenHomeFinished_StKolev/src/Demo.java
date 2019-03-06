package Test3_StKolev;

import Test3_StKolev.kids.Kid;
import Test3_StKolev.kids.OldKid;
import Test3_StKolev.kids.YoungKid;

import java.util.Random;

public class Demo {

    private static final int KIDS_IN_THE_GARDEN = 100;
    private static final int GROUPS_IN_THE_GARDEN = 4;

    public static void main(String[] args) {
        KinderGarden kinderGarden = new KinderGarden(GROUPS_IN_THE_GARDEN);
        Kid.setGarden(kinderGarden);
        kinderGarden.start();

        for (int i = 0; i < KIDS_IN_THE_GARDEN; i++) {
            Kid kid = null;
            if (new Random().nextBoolean()){
                kid = new OldKid("Kid "+(i+1));
            }else {
                kid = new YoungKid("Kid "+(i+1));
            }
            kid.start();

        }

    }
    public static int random(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
