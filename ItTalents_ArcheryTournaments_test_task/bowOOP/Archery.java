package oopTasks.bowOOP;


import oopTasks.bowOOP.archer.Archer;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Archery {
    private String name;
    private String address;
    private static String trener;
    private TreeSet<Archer> archers;
    private TreeMap<String, TreeMap<Archer, Integer[]>> journal; //Archer > array[0] - totalScore; array[1] - misses; array[2] - tens;

    Archery(String name, String address) {
        this.name = name;
        this.address = address;
        archers = new TreeSet<>(new ComparatorArchersByName());
        journal = new TreeMap<>();
    }

    private void showContesters() {
        System.out.println("\n============= CONTESTERS =============");
        for (Archer archer : archers) {
            System.out.println(archer);
            archer.goToTournament();
        }
    }

    void addArcher(Archer archer) {
        archers.add(archer);
    }

    void startTournament() {
        showContesters();
        Comparator<Archer> comparatorYearsExp = new ComparatorArchersByYearsExp();
        Comparator<Archer> comparatorArchersName = new ComparatorArchersByName();
        Comparator<Integer> comparatorIntegerScore = new ComparatorIntegerScore();

        TreeMap<String, TreeMap<Integer, TreeSet<Archer>>> archersScore = new TreeMap<>();
        TreeMap<Integer, TreeSet<Archer>> archersMisses = new TreeMap<>(comparatorIntegerScore);
        TreeMap<Integer, TreeSet<Archer>> archersScoreRating = new TreeMap<>(comparatorIntegerScore);
        TreeMap<Archer, Integer> archersWithCarbonBows = new TreeMap<>(comparatorYearsExp);
        TreeMap<Integer, TreeSet<Archer>> femaleHitScore = new TreeMap<>(comparatorIntegerScore);

        int currentScore;
        int currentMisses;
        int currentDirectHitChance;

        for (Archer archer : archers) {
            if (!journal.containsKey(archer.getTitle())) {
                journal.put(archer.getTitle(), new TreeMap<>(new ComparatorArchersByName()));
            }
            journal.get(archer.getTitle()).put(archer, new Integer[3]);
            journal.get(archer.getTitle()).get(archer)[0] = 0;
            journal.get(archer.getTitle()).get(archer)[1] = 0;
            journal.get(archer.getTitle()).get(archer)[2] = 0;

            for (int i = 0; i < archer.getNumberOfArrows(); i++) {
                int score = archer.shoot();
                journal.get(archer.getTitle()).get(archer)[0] += score;
                if (score == 0) {
                    journal.get(archer.getTitle()).get(archer)[1] += 1;
                }
                if (score == 10) {
                    journal.get(archer.getTitle()).get(archer)[2] += 1;
                }
            }

            currentScore = journal.get(archer.getTitle()).get(archer)[0];
            currentMisses = journal.get(archer.getTitle()).get(archer)[1];

            double percentDirectHit = (double) journal.get(archer.getTitle()).get(archer)[2] / archer.getNumberOfArrows() * 100; //calculating hit percentage
            currentDirectHitChance = (int) percentDirectHit;
            journal.get(archer.getTitle()).get(archer)[2] = currentDirectHitChance;


            if (!archersScore.containsKey(archer.getTitle())) {   // <Integer,TreeSet<Archer>> score statistic
                archersScore.put(archer.getTitle(), new TreeMap<>(comparatorIntegerScore));
            }
            if (!archersScore.get(archer.getTitle()).containsKey(currentScore)) {
                archersScore.get(archer.getTitle()).put(currentScore, new TreeSet<>(comparatorArchersName));
            }
            archersScore.get(archer.getTitle()).get(currentScore).add(archer);

            if (!archersMisses.containsKey(currentMisses)) {    // <Integer,TreeSet<Archer>> miss statistic
                archersMisses.put(currentMisses, new TreeSet<>(comparatorArchersName));
            }
            archersMisses.get(currentMisses).add(archer);

            if (!archersScoreRating.containsKey(currentDirectHitChance)) {    // <Integer,TreeSet<Archer>> direct hit chance statistic
                archersScoreRating.put(currentDirectHitChance, new TreeSet<>(comparatorArchersName));
            }
            archersScoreRating.get(currentDirectHitChance).add(archer);

            if (!archer.isMale()) {    // <Integer,TreeSet<Archer>> females direct hit chance statistic
                if (!femaleHitScore.containsKey(currentDirectHitChance)) {
                    femaleHitScore.put(currentDirectHitChance, new TreeSet<>(comparatorArchersName));
                }
                femaleHitScore.get(currentDirectHitChance).add(archer);
            }
        }

        for (String key : journal.keySet()) {   // <Integer,Archer> males with Carbon bow score statistic
            for (Map.Entry<Archer, Integer[]> entry : journal.get(key).entrySet()) {
                if (entry.getKey().isMale() && entry.getKey().getBowMaterial().equalsIgnoreCase("Carbon")) {
                    archersWithCarbonBows.put(entry.getKey(), entry.getValue()[0]);
                }
            }
        }

        System.out.println("\n============= RESULTS IN CATEGORIES =============");
        for (Map.Entry<String, TreeMap<Integer, TreeSet<Archer>>> entry : archersScore.entrySet()) {
            System.out.println(entry.getKey());
            for (Map.Entry<Integer, TreeSet<Archer>> entry1 : entry.getValue().entrySet()) {
                for (Archer archer : entry1.getValue()) {
                    System.out.println("\t" + archer.getName() + ": " + entry1.getKey());
                }
            }
        }

        System.out.println();
        int totalScoreInCategory = 0;
        int contestersInCategory = 0;

        System.out.println("\n============= STATISTICS =============");
        for (String keyCategory : journal.keySet()) {
            System.out.println("Best score in " + keyCategory + " category: " + archersScore.get(keyCategory).keySet().iterator().next());

            for (Archer archer : archersScore.get(keyCategory).values().iterator().next()) {
                System.out.println("\t" + archer);
            }

            for (Map.Entry<Integer, TreeSet<Archer>> entry : archersScore.get(keyCategory).entrySet()) {
                totalScoreInCategory += entry.getKey() * entry.getValue().size();
                contestersInCategory += entry.getValue().size();
            }
            System.out.println("Average score in " + keyCategory + " category: " + totalScoreInCategory / contestersInCategory);
        }

        System.out.println("Best hit score: " + archersScoreRating.keySet().iterator().next() + " percent");
        for (Archer archer : archersScoreRating.values().iterator().next()) {
            System.out.println("\t" + archer);
        }

        System.out.println("Most misses " + archersMisses.keySet().iterator().next());
        for (Archer archer : archersMisses.values().iterator().next()) {
            System.out.println("\t" + archer);
        }

        System.out.println("\n============= FEMALE ARCHERS HIT SCORE RATING PERCENTAGE OF MAX FEMALE RATING =============");
        double maxFemaleHitScore = femaleHitScore.keySet().iterator().next();

        System.out.println("Max female hit score rating: " + maxFemaleHitScore + "%");
        for (Map.Entry<Integer, TreeSet<Archer>> entry : femaleHitScore.entrySet()) {

            for (Archer archer : entry.getValue()) {
                int rating = (int) (entry.getKey() / maxFemaleHitScore * 100);
                for (int i = 0; i < rating / 2; i++) {
                    System.out.print(".");
                }
                System.out.println("\t" + archer.getName() + ": " + String.format("%.2f", entry.getKey() / maxFemaleHitScore * 100));
            }
        }

        System.out.println("\n============= ARCHERS WITH CARBON BOW =============");
        for (Map.Entry<Archer, Integer> entry : archersWithCarbonBows.entrySet()) {
            System.out.println(entry.getKey().getName() + " - " + entry.getKey().getYearsExp() + " years of exp." + " - " + entry.getValue() + " points.");
        }
    }
}
