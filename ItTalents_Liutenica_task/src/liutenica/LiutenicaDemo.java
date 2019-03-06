package liutenica;

import liutenica.persons.*;

import java.util.ArrayList;
import java.util.Random;

public class LiutenicaDemo {


    public static void main(String[] args) {
        Person.Baraka baraka = new Person.Baraka();
        Pisar pisar = null;
        try {
            pisar = new Pisar("Pisar",55);
        } catch (InvalidAgeException e) {
            System.out.println("sorry, cant create Pisar - "+ e.getMessage());
        }
        Person.Baraka.setPisar(pisar);
        pisar.start();
        Person.setBarachka(baraka);
        ArrayList<Girl> girls = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            try {
                girls.add(new Girl("Girl "+(i+1),random(14,19)));
            } catch (InvalidAgeException e) {
                System.out.println(e.getMessage());
            }
        }

        ArrayList<Boy> boys = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            try {
                boys.add(new Boy("Boy "+(i+1),random(15,23)));
            } catch (InvalidAgeException e) {
                System.out.println(e.getMessage());
            }
        }

        ArrayList<Grandma> grandmas = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            try {
                grandmas.add(new Grandma("Grandma "+(i+1),random(50,120)));
            } catch (InvalidAgeException e) {
                System.out.println(e.getMessage());
            }
        }

        for (Girl girl: girls){
            girl.start();
        }
        for (Boy boy: boys){
            boy.start();
        }
        for (Grandma grandma: grandmas){
            grandma.start();
        }

        try {
            for (Girl girl: girls){
                girl.join();
            }
            for (Boy boy: boys){
                boy.join();
            }
            for (Grandma grandma: grandmas){
                grandma.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int random(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max - min +1)+min;
    }
}
