package oopTasks.bowOOP;

import oopTasks.bowOOP.archer.Archer;

import java.text.Format;
import java.util.*;
import java.util.stream.Collectors;

public class Archery {

    private class Score {
        private int score;
        private int misses;
        private int directHits;
        private int totalArrows;

        private Score() {
            this.score = 0;
            this.misses = 0;
            this.directHits = 0;
            this.totalArrows = 0;
        }

        private void addDirectHit() {
            this.directHits++;
        }


        private void addMiss() {
            this.misses++;
        }

        private void addScore(int score) {
            this.score += score;
        }

        private double getHitChance() {
            return (((double) directHits) / ((double) totalArrows)) * 100;
        }

        private int getMisses() {
            return misses;
        }

        private int getTotalArrows() {
            return totalArrows;
        }

        public int getScore() {
            return score;
        }

        private void setTotalArrows(int arrows) {
            this.totalArrows = arrows;
        }

        @Override
        public String toString() {
            return " " + score+" / "+directHits+ " / "+misses;
        }
    }

    private String name;
    private String address;
    private static String trener;
    private TreeSet<Archer> archers;
    private TreeMap<String, TreeMap<Archer, Score>> journal; //Archer > array[0] - totalScore; array[1] - misses; array[2] - tens;

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

        for (Archer archer : archers) {
            if (!journal.containsKey(archer.getTitle())) {
                journal.put(archer.getTitle(), new TreeMap<>(new ComparatorArchersByName()));
            }
            journal.get(archer.getTitle()).put(archer, new Score());
            journal.get(archer.getTitle()).get(archer).setTotalArrows(archer.getNumberOfArrows());

            for (int i = 0; i < archer.getNumberOfArrows(); i++) {
                int score = archer.shoot();
                journal.get(archer.getTitle()).get(archer).addScore(score);
                if (score == 0) {
                    journal.get(archer.getTitle()).get(archer).addMiss();
                }
                if (score == 10) {
                    journal.get(archer.getTitle()).get(archer).addDirectHit();
                }
            }
        }

        showResult();
        showStatistics();
    }

    private void showResult() {
        System.out.println("\n============= RESULTS IN CATEGORIES =============");
//        List<Stream<Map.Entry<Archer, Score>>> arrayList1 = journal.entrySet().stream().map(Map.Entry::getKey).map(journal::get).map(TreeMap::entrySet).map(entries -> entries.stream()).collect(Collectors.toList());
//        System.out.println(arrayList1);
        for (Map.Entry<String, TreeMap<Archer, Score>> entry : journal.entrySet()) {
            System.out.println(entry.getKey());
            entry.
                    getValue()
                    .entrySet()
                    .stream()
                    .sorted((e1, e2) -> (e1.getValue().getScore() - e2.getValue().getScore()) * -1)
                    .map(entry1 -> "\t" + entry1.getKey().getName() + ": " + entry1.getValue())
                    .forEach(System.out::println);
        }
        System.out.println();
    }

    private void showStatistics() {

        System.out.println("\n============= STATISTICS =============");
//        JuniorArcher
        TreeMap<Archer, Score> allContestersResult = new TreeMap<>((Archer a1, Archer a2) -> 1);
        List<Optional<Map.Entry<Archer, Score>>> arrayList = journal
                .entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .map(TreeMap::entrySet)
                .map(entries -> entries.stream()
                        .max((e1, e2) -> e1.getValue().getScore() - e2.getValue().getScore()))
                .collect(Collectors.toList());

        arrayList
                .stream()
                .map(archerScoreEntry ->
                        ("Best score in "
                                + archerScoreEntry.get().getKey().getTitle()
                                + " category: "
                                + archerScoreEntry.get().getKey().getName()
                                + archerScoreEntry.get().getValue()))
                .forEach(System.out::println);
        System.out.println("=============");

        for (String keyCategory : journal.keySet()) {  //CATEGORY
            double sum = journal
                    .get(keyCategory)
                    .values()
                    .parallelStream()
                    .mapToInt(Score::getScore)
                    .average()
                    .getAsDouble();


            System.out.println("Average score in "
                    + keyCategory
                    + " category: " + String.format("%.2f",sum));

            allContestersResult.putAll(journal.get(keyCategory));
        }
        System.out.println("=============");
        System.out.print("Best hit score: ");
        journal
                .entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().entrySet().stream())
                .sorted((entry1, entry2) -> (entry1.getValue().getScore() - entry2.getValue().getScore()) * -1)
                .filter(e -> e
                        .getValue()
                        .getScore()
                        ==
                        journal
                                .entrySet()
                                .stream()
                                .flatMap(entry -> entry.getValue()
                                        .entrySet()
                                        .stream())
                                .min((e1, e2) -> (e1.getValue().getScore() - e2.getValue().getScore()) * -1).get().getValue().getScore())
                .forEach(s -> System.out.print(s.getKey().getName() + ": " + String.format("%.2f",s.getValue().getHitChance())+" "));
        System.out.println();

        System.out.print("Most misses ");
        allContestersResult.entrySet()
                .stream()
                .max((entry1, entry2) -> entry1.getValue().getMisses() - entry2.getValue().getMisses())
                .map(entry -> entry.getKey().getName() + ": " + entry.getValue().getMisses()).ifPresent(System.out::println);

        System.out.println("\n============= FEMALE ARCHERS HIT SCORE RATING PERCENTAGE OF MAX FEMALE RATING =============");

        TreeMap<Double, Archer> archers2 = new TreeMap<>((s1, s2) -> {
            int result = Double.compare(s1, s2)*-1;
            if (result == 0) {
                return 1;
            }
            return result;
        });

        for (String key : journal.keySet()) {

            journal
                    .get(key)
                    .entrySet()
                    .parallelStream()
                    .filter(e -> e.getKey().getTitle().equalsIgnoreCase(key))
                    .filter(e -> !e.getKey().isMale())
                    .sorted((e1, e2) -> (Double.compare(e1.getValue().getHitChance(), e2.getValue().getHitChance())) * -1)
                    .forEach(s -> archers2.put((s.getValue().getHitChance()
                            /
                            journal
                                    .get(key)
                                    .entrySet()
                                    .parallelStream()
                                    .filter(e -> e.getKey().getTitle().equalsIgnoreCase(key))
                                    .min((e1, e2) -> (Double.compare(e1.getValue().getHitChance(), e2.getValue().getHitChance())) * -1)
                                    .get()
                                    .getValue()
                                    .getHitChance()) * 100, s.getKey()));
        }

        for (Map.Entry<Double, Archer> entry : archers2.entrySet()) {

            for (int i = 0; i < entry.getKey() / 2; i++) {
                System.out.print(".");
            }
            System.out.println("\t" + entry.getValue().getName() + ": " + String.format("%.2f", entry.getKey()));
        }

        System.out.println("\n============= ARCHERS WITH CARBON BOW =============");
        TreeMap<Archer, Score> stringStream;
        allContestersResult
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getBowMaterial().equalsIgnoreCase("Carbon"))
                .sorted((e1, e2) -> (e1.getValue().getScore() - e2.getValue().getScore()) * -1)
                .map(entry -> entry.getKey().getName() + ": " + entry.getValue().getScore() + " - " + entry.getKey().getTitle())
                .forEach(System.out::println);
        /*for (Map.Entry<Archer, Integer> entry : archersWithCarbonBows.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getKey().getYearsExp() + " years of exp." + " - " + entry.getValue() + " points.");
        }*/
    }
}
