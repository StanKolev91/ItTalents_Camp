package Test3_StKolev.groups;

import Test3_StKolev.KinderGarden;

public class OlderGroup extends KinderGarden.Group {

    public OlderGroup(int capacity, OlderGroup.groupName groupName) {
        super(capacity, groupName.toString());
    }

    public enum groupName{
        FROGS, LADYBUG
    }
}
