package Test3_StKolev.groups;

import Test3_StKolev.KinderGarden;

public class YoungGroup extends KinderGarden.Group {
    public enum groupName{
        DUCKS, PENGUINS
    }
    public YoungGroup(int capacity, YoungGroup.groupName groupName) {
        super(capacity, groupName.toString());
    }


}
