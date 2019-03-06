package oopTasks.bowOOP;

import oopTasks.bowOOP.archer.Archer;
import oopTasks.bowOOP.archer.JuniorArcher;
import oopTasks.bowOOP.archer.SeniorArcher;
import oopTasks.bowOOP.archer.VeteranArcher;
import oopTasks.bowOOP.bow.AluminiumBow;
import oopTasks.bowOOP.bow.CarbonBow;
import oopTasks.bowOOP.bow.ISeniorBow;
import oopTasks.bowOOP.bow.WoodenBow;

import java.util.Random;

public class Demo {
    public static void main(String[] args) {
        Random rand = new Random();
        Archery club = new Archery(
                "IT Archer",
                "Bulgaria 69");
        for (int i = 0; i <40 ; i++) {
            Archer archer = null;
            int chance = random(1,100);
            if (chance<33){
                boolean isMale = rand.nextBoolean();
                String name = randomName();
                if (!isMale){
                    name = randomFemaleName();
                }
                archer = new JuniorArcher(
                        name,
                        isMale,
                        random(12,52),
                        random(0,3),
                        new WoodenBow(
                                "manufacturer",
                                random(1,2),
                                random(20,30))
                );
            }else if (chance<66){
                ISeniorBow bow;
                if (rand.nextBoolean()){
                    bow=new AluminiumBow(
                            "manufacturer",
                            random(1,2),
                            random(20,30),
                            1);
                }else bow = new CarbonBow(
                        "manufacturer",
                        random(1,2),
                        random(20,30),
                        1,
                        2);
                boolean isMale = rand.nextBoolean();
                String name = randomName();
                if (!isMale){
                    name = randomFemaleName();
                }
                archer = new SeniorArcher(
                        name,
                        isMale,
                        random(12,52),
                        random(3,9),
                        bow);
            }else {
                boolean isMale = rand.nextBoolean();
                String name = randomName();
                if (!isMale){
                    name = randomFemaleName();
                }
                archer = new VeteranArcher(
                        name,
                        isMale,
                        random(12,52),
                        random(10,30),
                        new CarbonBow(
                                "manufacturer",
                                random(1,2),
                                random(20,30),
                                1,
                                2));
            }
            club.addArcher(archer);
        }
        club.startTournament();
    }

    public static int random(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    public static String randomName() {
        String[] names = {"Georgi", "Ivan", "Dragan", "Ceco", "Ceko", "Petur", "Lorenco", "Goscho", "Tischo", "Mischo", "Plamen", "Marin"};
        String[] surNames = {"Georgiev", "Ivanov", "Petrov", "Aleksandrov", "Kolev", "Lamas", "Cecov", "Cekov", "Colov"};
        return names[random(0, names.length - 1)] + " " + surNames[random(0, surNames.length - 1)];
    }

    public static String randomFemaleName() {
        String[] names = {"Iveta", "Ceca", "Dragana", "Cura", "Pepa", "Malina", "Gergana", "Pudrileta", "Tonka", "Bonka", "Penka", "Marinka"};
        String[] surNames = {"Georgieva", "Ivanova", "Petrova", "Aleksandrova", "Koleva", "Lamasa", "Cecova", "Cekova", "Colova"};
        return names[random(0, names.length - 1)] + " " + surNames[random(0, surNames.length - 1)];
    }

}
