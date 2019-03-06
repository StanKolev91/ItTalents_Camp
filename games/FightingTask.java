import java.util.Random;
import java.util.Scanner;

public class FightingTask {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    static int levelValidation(String player) {
        Scanner sc = new Scanner(System.in);
        int level;
        do {
            System.out.println("Въведете нивото на " + player);
            level = sc.nextInt();
            if (level <= 0 || level > 100) {
                System.out.println(ANSI_RED + "<<<невалидно ниво не е в интервала 1 .. 100 >>>");
            }
        } while (level <= 0 || level > 100);
        return level;
    }

    static int critChanceValidation(String player) {
        Scanner sc = new Scanner(System.in);
        int critChance;
        do {
            System.out.println("Въведете шанса за критикал на " + player + " 0..100%");
            critChance = sc.nextInt();
            if (critChance < 0 || critChance > 100) {
                System.out.println(ANSI_RED + "<<<невалиден шанс>>>");
            }
        } while (critChance < 0 || critChance > 100);
        return critChance;
    }

    static int evasionChanceValidation(String player) {
        Scanner sc = new Scanner(System.in);
        int evasionChance;
        do {
            System.out.println("Въведете шанса за избягване на " + player + " 0..100%");
            evasionChance = sc.nextInt();
            if (evasionChance < 0 || evasionChance > 100) {
                System.out.println(ANSI_RED + "<<<невалиден шанс>>>");
            }
        } while (evasionChance < 0 || evasionChance > 100);
        return evasionChance;
    }

    static int blockChanceValidation(String player) {
        Scanner sc = new Scanner(System.in);
        int blockChance;
        do {
            System.out.println("Въведете шанса за блокиране на " + player + " 0..100%");
            blockChance = sc.nextInt();
            if (blockChance < 0 || blockChance > 100) {
                System.out.println(ANSI_RED + "<<<невалиден шанс>>>");
            }
        } while (blockChance < 0 || blockChance > 100);
        return blockChance;
    }

    static void pressEnter(String player1, String player2, float player1Health, float player2Health) {
        Scanner sc = new Scanner(System.in);
        System.out.println(ANSI_PURPLE + "\n<<<Press enter to continue...>>>\n  \\\"stats\" for players health\\");
        String enter = sc.nextLine();
        System.out.println(ANSI_PURPLE + "*********************************\n");
        if (enter.equals("stats")) {
            stats(player1, player2, player1Health, player2Health);
            System.out.println(ANSI_PURPLE + "*********************************\n");
        }
    }

    static void stats(String player1, String player2, float player1Health, float player2Health) {
        Scanner sc = new Scanner(System.in);
        System.out.println(ANSI_BLUE + player1);
        System.out.println(ANSI_BLUE + (int) player1Health + " кръв\n");
        System.out.println(ANSI_GREEN + player2);
        System.out.println(ANSI_GREEN + (int) player2Health + " кръв" + ANSI_PURPLE + "\n\n<<<Press enter to continue...>>>");
        sc.nextLine();
    }

    static boolean chanceForMove(int criticalChance) {
        Random rand = new Random();
        boolean willDoSmthg = false;
        int chance = rand.nextInt(100 + 1);
        for (int i = 1; i <= criticalChance; i++) {
            if (chance == i) willDoSmthg = true;
        }
        return willDoSmthg;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("Въведете име за 1ви играч");
        String player1Name = sc.nextLine().trim().split(" ")[0];
        int player1Level = levelValidation(player1Name);   // user inputCard value
//        int player1Level = rand.nextInt(100) + 1;    // Random value

        System.out.println("Въведете име за 2ри играч");
        String player2Name = sc.nextLine().trim().split(" ")[0];
        int player2Level = levelValidation(player2Name);   // user inputCard value
//        int player2Level = rand.nextInt(100) + 1;    // Random value

        float player1Health = /*player1Level * 0.95F + 5;*/ 150;
        float player2Health = /*player2Level * 0.95F + 5; */ 150;

        float player1Dmg = player1Level * 0.49F + 1;
        player1Dmg = (int) player1Dmg;
        float player2Dmg = player2Level * 0.49F + 1;
        player2Dmg = (int) player2Dmg;

        int critChPl1 = critChanceValidation(player1Name);   // user inputCard value
//        int critChPl1 = rand.nextInt(80) + 1;    // Random value
        int critChPl2 = critChanceValidation(player2Name);   // user inputCard value
//        int critChPl2 = rand.nextInt(80) + 1;    // Random value

        int evasionChPl1 = evasionChanceValidation(player1Name);   // user inputCard value
//        int evasionChPl1 = rand.nextInt(80) + 1;    // Random value
        int evasionChPl2 = evasionChanceValidation(player2Name);   // user inputCard value
//        int evasionChPl2 = rand.nextInt(80) + 1;    // Random value

        int blockChPl1 = blockChanceValidation(player1Name); // user inputCard value
//        int blockChPl1 = rand.nextInt(80) + 1;    // Random value
        int blockChPl2 = blockChanceValidation(player2Name); // user inputCard value
//        int blockChPl2 = rand.nextInt(80) + 1;    // Random value

        System.out.println(ANSI_BLUE + "\n" + player1Name);
        System.out.println(player1Level + " ниво");
        System.out.println((int) player1Health + " кръв");
        System.out.println((int) player1Dmg + " сила");
        System.out.println(critChPl1 + "% шанс за критикал");
        System.out.println(evasionChPl1 + "% шанс за избягване на удар");
        System.out.println(blockChPl1 + "% шанс за блокиране на удар\n");
        System.out.println(ANSI_GREEN + player2Name);
        System.out.println(player2Level + " ниво");
        System.out.println((int) player2Health + " кръв");
        System.out.println((int) player2Dmg + " сила");
        System.out.println(critChPl2 + "% шанс за критикал");
        System.out.println(evasionChPl2 + "% шанс за избягване на удар");
        System.out.println(blockChPl2 + "% шанс за блокиране на удар\n");

        //pressEnter(player1Name, player2Name, player1Health, player2Health);

        int player1Wins = 0;
        int player2Wins = 0;

        for (int i = 0; i < 100; i++) {
            int whoHitsFirst = rand.nextInt(2) + 1;  // random number 1 - 2
            System.out.println((whoHitsFirst == 1 ? ANSI_BLUE + player1Name : ANSI_GREEN + player2Name) + " удря пръв.\n");
            int evadedHits = 0;
            int criticalStrikes = 0;
            int player1Criticals = 0;
            int player2Criticals = 0;
            int player1Hits = 0;
            int player2Hits = 0;
            int player1Evaded = 0;
            int player2Evaded = 0;
            int player1Blocks = 0;
            int player2Blocks = 0;
            float loserHealth = ((whoHitsFirst == 1) ? player2Health : player1Health);

            while (loserHealth > 0) {
                String winnerName = whoHitsFirst == 1 ? ANSI_BLUE + player1Name : ANSI_GREEN + player2Name;
                String loserName = whoHitsFirst == 1 ? ANSI_GREEN + player2Name : ANSI_BLUE + player1Name;
                int block = (whoHitsFirst == 1 ? player2Blocks : player1Blocks);
                evadedHits = (whoHitsFirst == 1 ? player2Evaded : player1Evaded);
                criticalStrikes = (whoHitsFirst == 1 ? player1Criticals : player2Criticals);
                int hits = (whoHitsFirst == 1 ? player1Hits : player2Hits);
                boolean winnerCritical = whoHitsFirst == 1 ? chanceForMove(critChPl1) : chanceForMove(critChPl2);
                boolean loserEvasion = whoHitsFirst == 1 ? chanceForMove(evasionChPl2) : chanceForMove(evasionChPl1);
                boolean loserBlock = whoHitsFirst == 1 ? chanceForMove(blockChPl1) : chanceForMove(blockChPl2);
                loserHealth = ((whoHitsFirst == 1) ? player2Health : player1Health);
                float winnerHealth = ((whoHitsFirst == 1) ? player1Health : player2Health);
                float winnerDmg = whoHitsFirst == 1 ? player1Dmg : player2Dmg;

                System.out.println(winnerName + ANSI_RESET + " удря...");
                hits++;
                if (winnerCritical) {
                    System.out.println(ANSI_BLACK + "<<<<CRITICAL STRIKE>>>>\n<<<< DOUBLE DAMAGE >>>>");
                    criticalStrikes++;
                    if (loserEvasion) {
                        System.out.println(loserName + ANSI_RESET + " << evade >>\n");
                        evadedHits++;
                    } else {
                        System.out.println(loserName + ANSI_RED + " загуби " + (2 * (int) winnerDmg) + " кръв.");
                        loserHealth -= 2 * winnerDmg;
                        System.out.println(loserName + ANSI_RED + " остава с " + (loserHealth) + " кръв.\n");
                    }
                } else {
                    if (loserEvasion) {
                        System.out.println(loserName + ANSI_RESET + " << evade >>\n");
                        evadedHits++;
                    } else {
                        if (loserBlock) {
                            System.out.println(loserName + ANSI_RESET + " блокира.");
                            System.out.println(loserName + ANSI_RED + " загуби " + ((int) winnerDmg) / 2 + " кръв.");
                            loserHealth -= winnerDmg / 2;
                            System.out.println(loserName + ANSI_RED + " остава с " + (loserHealth) + " кръв.\n");
                            block++;

                        } else {
                            System.out.println(loserName + ANSI_RED + " загуби " + ((int) winnerDmg) + " кръв.");
                            loserHealth -= winnerDmg;
                            System.out.println(loserName + ANSI_RED + " остава с " + ((int) loserHealth) + " кръв.\n");
                        }
                    }
                }
                if (loserHealth <= 0) {
                    int win = ((whoHitsFirst == 1) ? ++player1Wins : ++player2Wins);
                    System.out.println("\n" + winnerName + " спечели!");
                    System.out.println(winnerName + " - направени " + hits + " удара.");
                    System.out.println(winnerName + " - направени " + criticalStrikes + " смъртоносни удара.");
                    System.out.println(winnerName + " - блокирани " + (!(whoHitsFirst == 1) ? player2Blocks : player1Blocks) + " удара.");
                    System.out.println(winnerName + " - избегнати " + (!(whoHitsFirst == 1) ? player2Evaded : player1Evaded) + " удара.\n");
                    System.out.println(loserName + " - направени " + (!(whoHitsFirst == 1) ? player1Hits : player2Hits) + " удара.");
                    System.out.println(loserName + " - направени " + (!(whoHitsFirst == 1) ? player1Criticals : player2Criticals) + " смъртоносни удара.");
                    System.out.println(loserName + " - блокирани " + ((whoHitsFirst == 1) ? player2Blocks : player1Blocks) + " удара.");
                    System.out.println(loserName + " - избегнати " + ((whoHitsFirst == 1) ? player2Evaded : player1Evaded) + " удара.\n\n\n");
                    break;
                }
                player2Health = ((whoHitsFirst == 1) ? loserHealth : winnerHealth);
                player1Health = ((whoHitsFirst == 1) ? winnerHealth : loserHealth);
                player1Evaded = (whoHitsFirst == 1 ? player1Evaded : evadedHits);
                player2Evaded = (whoHitsFirst == 1 ? evadedHits : player2Evaded);
                player1Criticals = (whoHitsFirst == 1 ? criticalStrikes : player1Criticals);
                player2Criticals = (whoHitsFirst == 1 ? player2Criticals : criticalStrikes);
                player1Hits = (whoHitsFirst == 1) ? hits : player1Hits;
                player2Hits = (whoHitsFirst == 1) ? player2Hits : hits;
                player1Blocks = (whoHitsFirst == 1) ? player1Blocks : block;
                player2Blocks = (whoHitsFirst == 1) ? block : player2Blocks;
                whoHitsFirst = (whoHitsFirst == 1) ? ++whoHitsFirst : ((whoHitsFirst > 1) ? 1 : whoHitsFirst);


                //pressEnter(player1Name, player2Name, player1Health, player2Health);
            }
            player1Health = /*player1Level * 0.95F + 5;*/ 150;
            player2Health = /*player2Level * 0.95F + 5; */ 150;
        }
        System.out.println(player1Name + " " + player1Wins + " победи");
        System.out.println(player2Name + " " + player2Wins + " победи ");
    }
}


