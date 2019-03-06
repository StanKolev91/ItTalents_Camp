package liutenica.persons;

import liutenica.vegetables.IVegetable;

public interface IVegetableProcesser extends IWorker {

    void tellPisarToWriteDown(IVegetable vegetable);
}
