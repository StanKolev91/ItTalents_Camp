import java.util.Random;
import java.util.Scanner;


public class SeaFight {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    static void findBestPosition(int[][] table) {                        // checks every possible position and compares, writes current AI move on the board
        int bestAiPositionValue = -1000;
        int bestAiPositionRow = -1;
        int bestAiPositionColum = -1;

        for (int i = 0; i < table.length; i++) {

            for (int j = 0; j < table[i].length; j++) {

                if (table[i][j] == 2) {
                    table[i][j] = 1;
                    int value = minimax(table, 0, false); // recurse with next move = Player move, returns lowest possible value, for every position {highest chance for player win}
                    if (bestAiPositionValue < value) {                    // gets the highest value of all { lowest chance for player win }
                        bestAiPositionValue = value;
                        bestAiPositionRow = i;
                        bestAiPositionColum = j;
                    }
                    table[i][j] = 2;
                }
            }
        }
        table[bestAiPositionRow][bestAiPositionColum] = 1;
    }    // checks every possible position and compares, writes the position with lowest player win chance as a current AI move on the board

    static void inputMove(int[][] table) {
        Scanner sc = new Scanner(System.in);
        int number;
        boolean isValidNumber = true;
        do {
            System.out.println("Enter position number 1..9");
            number = sc.nextInt();
            switch (number) {
                case 9: {
                    if (table[0][0] == 2) {
                        isValidNumber = true;
                        table[0][0] = 0;
                    } else
                        isValidNumber = false;
                    break;
                }
                case 8: {
                    if (table[0][1] == 2) {
                        isValidNumber = true;
                        table[0][1] = 0;
                    } else
                        isValidNumber = false;
                    break;
                }
                case 7: {
                    if (table[0][2] == 2) {
                        isValidNumber = true;
                        table[0][2] = 0;
                    } else isValidNumber = false;
                    break;
                }
                case 6: {
                    if (table[1][0] == 2) {
                        isValidNumber = true;
                        table[1][0] = 0;
                    } else isValidNumber = false;
                    break;
                }
                case 5: {
                    if (table[1][1] == 2) {
                        isValidNumber = true;
                        table[1][1] = 0;
                    } else isValidNumber = false;
                    break;
                }
                case 4: {
                    if (table[1][2] == 2) {
                        isValidNumber = true;
                        table[1][2] = 0;
                    } else isValidNumber = false;
                    break;
                }
                case 3: {
                    if (table[2][0] == 2) {
                        isValidNumber = true;
                        table[2][0] = 0;
                    } else isValidNumber = false;
                    break;
                }
                case 2: {
                    if (table[2][1] == 2) {
                        isValidNumber = true;
                        table[2][1] = 0;
                    } else isValidNumber = false;
                    break;
                }
                case 1: {
                    if (table[2][2] == 2) {
                        isValidNumber = true;
                        table[2][2] = 0;
                    } else isValidNumber = false;
                    break;
                }
                default: {
                    System.out.println("<<<INVALID NUMBER>>>");
                    isValidNumber = false;
                    break;
                }
            }
        } while (!isValidNumber);
    }  //gets number from 1 .. 9, validates it and checks if the place is free on the board, if free = true assigns it as a player's move

    static int minimax(int[][] table, int depth, boolean isAiMove) {

        int tableEvaluation = checkForWinAndReturnScore(table);  // checking for 3 consecutive returns 10,-10,0 {AI wins, Player wins, tie}
        if (tableEvaluation == 10) {
            return tableEvaluation - depth;
        }
        if (tableEvaluation == -10) {
            return tableEvaluation + depth;
        }
        if (tableIsFull(table)) {
            return 0;
        }
        if (isAiMove) {  //AI move, checks move and returns value - depth, in case of 2 situation with the same result, gets the closest in depth, gets the maximal value of all for AI move
            int bestMove = -1000;

            for (int i = 0; i < table.length; i++) {

                for (int j = 0; j < table[i].length; j++) {
                    if (table[i][j] == 2) {
                        table[i][j] = 1;
                        bestMove = max(bestMove, minimax(table, depth + 1, !isAiMove));
                        table[i][j] = 2;
                    }
                }
            }
            return bestMove;
        } else {        //Player move, checks move and returns value + depth, in case of 2 situation with the same result, gets the closest in depth, gets the minimal value of all for Player move
            int bestMove = 1000;

            for (int i = 0; i < table.length; i++) {

                for (int j = 0; j < table[i].length; j++) {
                    if (table[i][j] == 2) {
                        table[i][j] = 0;
                        bestMove = min(bestMove, minimax(table, depth + 1, !isAiMove));
                        table[i][j] = 2;
                    }
                }
            }
            return bestMove;
        }
    }  // Minimax Algorithm

    static int max(int a, int b) {
        if (a > b) {
            b = a;
        }
        return b;
    }  // returns maximum of 2 number

    static int min(int a, int b) {
        if (a < b) {
            b = a;
        }
        return b;
    }  // returns minimum of 2 number

    static boolean tableIsFull(int[][] table) {
        boolean isFull = true;

        for (int i = 0; i < table.length; i++) {

            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] == 2) isFull = false;
            }
        }
        return isFull;
    }  // returns true if no more moves5


    static int checkForWinAndReturnScore(int[][] table) {
        int aiBroiHor = 0;
        int playerBroiHor = 0;
        int aiBroiVer = 0;
        int playerBroiVer = 0;
        int aiMainDiag = 0;
        int playerMainDiag = 0;
        int aiSecDiag = 0;
        int playerSecDiag = 0;

        for (int red = 0; red < table.length; red++) {

            for (int colum = 0; colum < table[red].length; colum++) {

                if (table[red][colum] == 1) aiBroiHor++;
                if (table[red][colum] == 0) playerBroiHor++;
                if (table[colum][red] == 1) aiBroiVer++;
                if (table[colum][red] == 0) playerBroiVer++;
            }
            if (aiBroiHor == 3 || aiBroiVer == 3) {
                return 10;
            }
            if (playerBroiHor == 3 || playerBroiVer == 3) {
                return -10;
            }
            aiBroiHor = 0;
            playerBroiHor = 0;
            aiBroiVer = 0;
            playerBroiVer = 0;
        }

        for (int diag = 0; diag < table.length; diag++) {
            if (table[diag][diag] == 1) aiMainDiag++;
            if (table[diag][diag] == 0) playerMainDiag++;
            if (table[(table.length - 1) - diag][diag] == 1) aiSecDiag++;
            if (table[(table.length - 1) - diag][diag] == 0) playerSecDiag++;
        }
        if (aiMainDiag == 3 || aiSecDiag == 3) {
            return 10;
        }

        if (playerMainDiag == 3 || playerSecDiag == 3) {
            return -10;
        }
        return 0;
    }  // Evaluation


    public static void main(String[] args) {
        Random rand = new Random();                    // who's first
        int[][] table = new int[3][3];
        for (int a = 0; a < table.length; a++) {

            for (int b = 0; b < table[a].length; b++) {

                table[a][b] = 2;
            }
        }
        int whoIsNext = rand.nextInt(1 + 1);
        System.out.println(whoIsNext == 0 ? ANSI_BLACK + "Player's first" : ANSI_BLACK + "Computer's first");
        boolean aiMove = (whoIsNext == 0) ? false : true;
        int aiMoveI = 0;
        int aiMoveJ = 0;
        boolean firstMove = true;
        int broqch;
        do {
            if (aiMove) {
                System.out.println(ANSI_BLACK + "Computer's turn");
                if (firstMove) {                       // 1st AI move - Random
                    do {
                        aiMoveI = rand.nextInt(2 + 1);
                        aiMoveJ = rand.nextInt(2 + 1);
                    } while (table[aiMoveI][aiMoveJ] != 2);
                    table[aiMoveI][aiMoveJ] = 1;
                    firstMove = false;
                } else {
                    findBestPosition(table);           // else AI move best position for no Player win
                }
            } else {

                for (int i = 0; i < 3; i++) {
                    System.out.print(ANSI_BLACK + " - ");
                }
                System.out.println();
                broqch = 7;

                for (int a = 0; a < table.length; a++) {
                    System.out.print(ANSI_BLACK + "| ");

                    for (int b = table[a].length-1; b >= 0; b--) {

                        if (table[a][b] == 2) {
                            System.out.print(ANSI_BLACK + broqch + " ");
                        }
                        else
                            System.out.print(table[a][b] == 0 ? ANSI_RED + "X " + ANSI_RESET : ANSI_CYAN + "O " + ANSI_RESET);
                        broqch++;

                    }
                    broqch-=6;
                    System.out.println(ANSI_BLACK + "|");
                }

                for (int i = 0; i < 3; i++) {
                    System.out.print(ANSI_BLACK + " - ");
                }
                System.out.println();
                System.out.println(ANSI_BLACK + "Player's turn");
                inputMove(table);
                firstMove = false;
            }
            aiMove = !aiMove;
            if (checkForWinAndReturnScore(table) == 10 || checkForWinAndReturnScore(table) == -10) {
                if (checkForWinAndReturnScore(table) == 10) System.out.println(ANSI_BLACK + "Computer wins");
                if (checkForWinAndReturnScore(table) == -10) System.out.println(ANSI_BLACK + "Player wins");
                break;
            }
        } while (!tableIsFull(table));

        for (int i = 0; i < 3; i++) {
            System.out.print(ANSI_BLACK + " - ");
        }
        System.out.println();

        for (int a = 0; a < table.length; a++) {
            System.out.print(ANSI_BLACK + "| ");

            for (int b = 0; b < table[a].length; b++) {
                if (table[a][b] == 2) System.out.print("_ ");
                else
                    System.out.print(table[a][b] == 0 ? ANSI_RED + "X " + ANSI_RESET : ANSI_CYAN + "O " + ANSI_RESET);
            }
            System.out.println(ANSI_BLACK + "|");
        }

        for (int i = 0; i < 3; i++) {
            System.out.print(ANSI_BLACK + " - ");
        }
    }
}
