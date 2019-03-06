package liutenica.persons;

import liutenica.Liutenica;

public class Grandma extends Person implements ILiutenicaMaker {

    public Grandma(String name, int age) throws InvalidAgeException {
        super(name, age);
    }

    @Override
    protected boolean isMale() {
        return false;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            tellPisar(makeLiutenica());
        }
    }

    @Override
    protected boolean isValidAge(int age) {
        return age >= 50 && age <= 120;
    }


    @Override
    public void tellPisar(Liutenica liutenica) {
        Baraka.getPisar().writeDown(liutenica, this);
    }

}
