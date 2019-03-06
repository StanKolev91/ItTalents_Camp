package liutenica.persons;

import liutenica.vegetables.IVegetable;

public class Boy extends Person implements IVegetableProcesser {

    public Boy(String name, int age) throws InvalidAgeException {
        super(name, age);
    }

    @Override
    protected boolean isMale() {
        return true;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            IVegetable vegetable = getRandomVegetableForTreatment();
            try {
                sleep(vegetable.getTreatmentTimeSec() * 1000);
            } catch (InterruptedException e) {
                System.out.println(this.getName() + " was interruped while picking vegetables!");
            }
            if (vegetable != null) {

                tellPisarToWriteDown(putVegetableToPan(vegetable));
            }
        }
    }

    @Override
    protected boolean isValidAge(int age) {
        return age >= 15 && age <= 23;
    }

    @Override
    public void tellPisarToWriteDown(IVegetable vegetable) {
        Baraka.getPisar().writeDown(vegetable,this);
    }
}
