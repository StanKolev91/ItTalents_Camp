package liutenica.persons;


import liutenica.LiutenicaDemo;
import liutenica.vegetables.Eggplant;
import liutenica.vegetables.IVegetable;
import liutenica.vegetables.Pepper;
import liutenica.vegetables.Tomato;

public class Girl extends Person implements IVegetableProcesser {

    public Girl(String name, int age) throws InvalidAgeException {
        super(name, age);
    }

    @Override
    protected boolean isMale() {
        return false;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                sleep(5000);
                for (int i = 0; i <LiutenicaDemo.random(3,6) ; i++) {
                    int chance = LiutenicaDemo.random(0, 100);
                    IVegetable vegetable = null;
                    if (chance <= 33) {
                        vegetable = new Pepper();
                    } else if (chance <= 66) {
                        vegetable = new Tomato();
                    } else vegetable = new Eggplant();
                    tellPisarToWriteDown(addNewVegetableToBasket(vegetable));
                }
            } catch (InterruptedException e) {
                System.out.println(this.getName() + " was interruped while picking vegetables!");
            }
        }
    }

    @Override
    protected boolean isValidAge(int age) {
        return age >= 14 && age <= 19;
    }

    @Override
    public void tellPisarToWriteDown(IVegetable vegetable) {
        Baraka.getPisar().writeDown(vegetable,this);
    }
}
