import java.util.Random;
import java.util.Scanner;

public class ChovecheSKamache {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    static void obstacles(String[][] world, int obstaclesPercent) {
        int randomObstaclesNumber = obstaclesPercent * (world.length * world.length) / 100;
        Random rand = new Random();

        for (int counter = 0; counter < randomObstaclesNumber; ) {
            int i = rand.nextInt(world.length);
            int j = rand.nextInt(world.length);
            if (world[i][j].equals(" ")) {
                if (i == world.length - 1 && j == world.length - 1 || i == world.length - 2 && j == world.length - 1 || i == world.length - 1 && j == world.length - 2)
                    continue;
                if (i == 0 && j == 0) continue;
                world[i][j] = "#";
                counter++;
            }
        }
    }

    static int difficulty() {
        Scanner sc = new Scanner(System.in);
        int difficulty;

        do {
            System.out.println("Enter Difficulty level 1 .. 3 ");
            difficulty = sc.nextByte();
            if (difficulty < 1 || difficulty > 3) {
                System.out.println("Invalid Level");
            }

        } while (difficulty < 1 || difficulty > 3);
        return difficulty;
    }

    static String[][] worldCreation(int difficulty) {
        int obstaclesPercentage = 0;
        int worldSideLength = 0;
        switch (difficulty) {
            case 1: {
                worldSideLength = 20;
                obstaclesPercentage = 10;
                break;
            }
            case 2: {
                worldSideLength = 40;
                obstaclesPercentage = 20;
                break;
            }
            case 3: {
                worldSideLength = 50;
                obstaclesPercentage = 40;
                break;
            }
        }
        String[][] world = new String[worldSideLength][worldSideLength];

        for (int i = 0; i < world.length; i++) {

            for (int j = 0; j < world[i].length; j++) {
                world[i][j] = " ";
            }
        }
        obstacles(world, obstaclesPercentage);
        return world;
    }

    static int[] move(String[][] world, int[] humanRockPosition) {
        int humanI = humanRockPosition[0];
        int humanJ = humanRockPosition[1];
        int rockI = humanRockPosition[2];
        int rockJ = humanRockPosition[3];
        Scanner sc = new Scanner(System.in);
        int[][] moves = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        String nextMove = null;
        do {
            System.out.println("enter move [W - up, S - down, A - left, D - right]");
            nextMove = sc.next();
            if (!(nextMove.equalsIgnoreCase("w") || nextMove.equalsIgnoreCase("s") || nextMove.equalsIgnoreCase("a") || nextMove.equalsIgnoreCase("d"))) {
                System.out.println("invalid move character");
            }
        }
        while (!(nextMove.equalsIgnoreCase("w") || nextMove.equalsIgnoreCase("s") || nextMove.equalsIgnoreCase("a") || nextMove.equalsIgnoreCase("d")));
        int newHumanI = humanI;
        int newHumanJ = humanJ;
        int newRockI = rockI;
        int newRockJ = rockJ;
        switch (nextMove.toLowerCase()) {
            case "w": {
                if (isValidMoveHuman(world, humanI + moves[1][0], humanJ + moves[1][1])) {
                    newHumanI = humanI + moves[1][0];
                    newHumanJ = humanJ + moves[1][1];
                    swapArrValues(world, humanI, humanJ, newHumanI, newHumanJ);
                } else if (humanI + moves[1][0] == rockI && humanJ + moves[1][1] == rockJ) {
                    if (isValidMoveHuman(world, (rockI + moves[1][0]), (rockJ + moves[1][1]))) {
                        newRockI = rockI + moves[1][0];
                        newRockJ = rockJ + moves[1][1];
                        swapArrValues(world, rockI, rockJ, newRockI, newRockJ);
                        newHumanI = humanI + moves[1][0];
                        newHumanJ = humanJ + moves[1][1];
                        swapArrValues(world, humanI, humanJ, newHumanI, newHumanJ);
                    } else System.out.println("invalid move");
                } else System.out.println("invalid move");
                break;
            }
            case "s": {
                if (isValidMoveHuman(world, humanI + moves[0][0], humanJ + moves[0][1])) {
                    newHumanI = humanI + moves[0][0];
                    newHumanJ = humanJ + moves[0][1];
                    swapArrValues(world, humanI, humanJ, newHumanI, newHumanJ);
                } else if (humanI + moves[0][0] == rockI && humanJ + moves[0][1] == rockJ) {
                    if (isValidMoveHuman(world, (rockI + moves[0][0]), (rockJ + moves[0][1]))) {
                        newRockI = rockI + moves[0][0];
                        newRockJ = rockJ + moves[0][1];
                        swapArrValues(world, rockI, rockJ, newRockI, newRockJ);
                        newHumanI = humanI + moves[0][0];
                        newHumanJ = humanJ + moves[0][1];
                        swapArrValues(world, humanI, humanJ, newHumanI, newHumanJ);
                    } else System.out.println("invalid move");
                } else System.out.println("invalid move");
                break;
            }
            case "a": {
                if (isValidMoveHuman(world, humanI + moves[3][0], humanJ + moves[3][1])) {
                    newHumanI = humanI + moves[3][0];
                    newHumanJ = humanJ + moves[3][1];
                    swapArrValues(world, humanI, humanJ, newHumanI, newHumanJ);
                } else if (humanI + moves[3][0] == rockI && humanJ + moves[3][1] == rockJ) {
                    if (isValidMoveHuman(world, (rockI + moves[3][0]), (rockJ + moves[3][1]))) {
                        newRockI = rockI + moves[3][0];
                        newRockJ = rockJ + moves[3][1];
                        swapArrValues(world, rockI, rockJ, newRockI, newRockJ);
                        newHumanI = humanI + moves[3][0];
                        newHumanJ = humanJ + moves[3][1];
                        swapArrValues(world, humanI, humanJ, newHumanI, newHumanJ);
                    } else System.out.println("invalid move");
                } else System.out.println("invalid move");
                break;
            }
            case "d": {
                if (isValidMoveHuman(world, humanI + moves[2][0], humanJ + moves[2][1])) {
                    newHumanI = humanI + moves[2][0];
                    newHumanJ = humanJ + moves[2][1];
                    swapArrValues(world, humanI, humanJ, newHumanI, newHumanJ);
                } else if (humanI + moves[2][0] == rockI && humanJ + moves[2][1] == rockJ) {
                    if (isValidMoveHuman(world, (rockI + moves[2][0]), (rockJ + moves[2][1]))) {
                        newRockI = rockI + moves[2][0];
                        newRockJ = rockJ + moves[2][1];
                        swapArrValues(world, rockI, rockJ, newRockI, newRockJ);
                        newHumanI = humanI + moves[2][0];
                        newHumanJ = humanJ + moves[2][1];
                        swapArrValues(world, humanI, humanJ, newHumanI, newHumanJ);
                    } else System.out.println("invalid move");
                } else System.out.println("invalid move");
                break;
            }
        }
        humanRockPosition[0] = newHumanI;
        humanRockPosition[1] = newHumanJ;
        humanRockPosition[2] = newRockI;
        humanRockPosition[3] = newRockJ;
        return humanRockPosition;
    }

    static void swapArrValues(String[][] arr, int firstIndx1, int secondIndx1, int firstIndx2, int secondIndx2) {
        String temp = arr[firstIndx1][secondIndx1];
        arr[firstIndx1][secondIndx1] = arr[firstIndx2][secondIndx2];
        arr[firstIndx2][secondIndx2] = temp;
    }

    static boolean isValidMoveHuman(String[][] arr, int i, int j) {
        boolean isValid = false;
        if (i < arr.length && i >= 0) {
            if (j < arr[0].length && j >= 0) {
                if (arr[i][j].equals(" ") || arr[i][j].equals("Y") || arr[i][j].equals(ANSI_RED + "♻" + ANSI_RESET)) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }


    static boolean isValidMoveArr(String[][] arr, int i, int j) {
        boolean isValid = false;
        if (i < arr.length && i >= 0) {
            if (j < arr[0].length && j >= 0) {
                isValid = true;
            }
        }
        return isValid;
    }

    static boolean isValidMoveRock(String[][] arr, int i, int j) {
        boolean isValid = false;
        if (i < arr.length && i >= 0) {
            if (j < arr[0].length && j >= 0) {
                if ((arr[i][j].equals(" ") || arr[i][j].equals("Y") || arr[i][j].equals("O") || arr[i][j].equals(ANSI_RED + "♻" + ANSI_RESET))) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    static void humanPossMove(String[][] worldArr, int presentI, int presentJ) {
        worldArr[presentI][presentJ] = ".";

       /* for (int i = 0; i < worldArr.length; i++) {

            for (int j = 0; j < worldArr[0].length; j++) {
                System.out.print(worldArr[i][j]);
            }
            System.out.println();
        }*/
        int[][] moves = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        for (int i = 0; i < moves.length; i++) {
            if (isValidMoveHuman(worldArr, presentI + moves[i][0], presentJ + moves[i][1])) {
                humanPossMove(worldArr, presentI + moves[i][0], presentJ + moves[i][1]);
            }
        }
    }

    static boolean notOverYet(String[][] humanWorld, String[][] rockWorld, int rockI, int rockJ, int exitI, int exitJ, boolean rockCanFinish) {
        rockWorld[rockI][rockJ] = "*";

/*        for (int i = 0; i < rockWorld.length; i++) {

            for (int j = 0; j < rockWorld[0].length; j++) {
                System.out.print(rockWorld[i][j]);
            }
            System.out.println();
        }*/
        if (rockI == exitI && rockJ == exitJ) {
            rockCanFinish = true;
            return rockCanFinish;
        }
        int[][] moves = {
                {1, 0, -1, 0},
                {-1, 0, 1, 0},
                {0, 1, 0, -1},
                {0, -1, 0, 1}
        };

        for (int i = 0; i < moves.length; i++) {
            if (isValidMoveRock(rockWorld, rockI + moves[i][0], rockJ + moves[i][1]) && isValidMoveArr(humanWorld, rockI + moves[i][2], rockJ + moves[i][3])) {
                if (humanWorld[rockI + moves[i][2]][rockJ + moves[i][3]].equals(".") || humanWorld[rockI + moves[i][2]][rockJ + moves[i][3]].equals("O")) {
                    rockCanFinish = notOverYet(humanWorld, rockWorld, rockI + moves[i][0], rockJ + moves[i][1], exitI, exitJ, rockCanFinish);
                }
            }
        }
        return rockCanFinish;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        boolean noGameOver = false;
        boolean playing = true;
        int[] humanRockPosition = new int[4];
        humanRockPosition[0] = 0;                                     //humanI
        humanRockPosition[1] = 0;                                     //humanJ
        String[][] world;/* = {
                {" ", "b", " ", "b", " ", " ", "b", " "},
                {" ", " ", " ", "b", " ", " ", " ", " "},
                {" ", " ", " ", "b", " ", " ", "b", " "},
                {"b", " ", " ", " ", "b", " ", "b", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "b", " ", " ", "b"},
                {" ", " ", " ", "b", " ", " ", " ", " "},
                {"b", " ", "b", "b", " ", " ", " ", " "},
        };*/
        String[][] rockWorld;
        String[][] humanWorld;
        int exitI;
        int exitJ;
        int difficulty = difficulty();

        do {
            world = worldCreation(difficulty);
            humanWorld = new String[world.length][world[0].length];
            rockWorld = new String[world.length][world[0].length];
            exitI = world.length - 1;
            exitJ = world.length - 1;

            do {
                humanRockPosition[2] = rand.nextInt(world.length);           //rockI
                humanRockPosition[3] = rand.nextInt(world.length);           //rockJ
            }while (humanRockPosition[2] == exitI && humanRockPosition[3] == exitJ);
            world[humanRockPosition[0]][humanRockPosition[1]] = "Y";
            world[humanRockPosition[2]][humanRockPosition[3]] = "O";

            for (int i = 0; i < rockWorld.length; i++) {

                for (int j = 0; j < rockWorld[i].length; j++) {
                    rockWorld[i][j] = world[i][j];
                    humanWorld[i][j] = world[i][j];
                }
                System.out.println();
            }

            world[exitI][exitJ] = ANSI_RED + "♻" + ANSI_RESET;

            humanPossMove(humanWorld, humanRockPosition[0], humanRockPosition[1]);

            noGameOver = notOverYet(humanWorld, rockWorld, humanRockPosition[2], humanRockPosition[3], exitI, exitJ, noGameOver);
        } while (!noGameOver);

        int afterGameOverTries = 0;

        do {
            noGameOver = false;
            System.out.println();

            for (int i = 0; i < world[0].length; i++) {
                System.out.print(ANSI_CYAN + " -" + ANSI_RESET);
            }
            System.out.println();

            for (int k = 0; k < world.length; k++) {
                System.out.print(ANSI_CYAN + "|" + ANSI_RESET);

                for (int o = 0; o < world[k].length; o++) {
                    System.out.print(ANSI_BLACK + (world[k][o]) + ANSI_RESET);
                    if (o < world[0].length - 1) System.out.print(" ");
                }
                System.out.println(ANSI_CYAN + "|" + ANSI_RESET);
            }

            for (int i = 0; i < world[0].length; i++) {
                System.out.print(ANSI_CYAN + " -" + ANSI_RESET);
            }
            System.out.println();

            move(world, humanRockPosition);  // control the movement

            for (int i = 0; i < rockWorld.length; i++) {

                for (int j = 0; j < rockWorld[i].length; j++) {
                    rockWorld[i][j] = world[i][j];
                    humanWorld[i][j] = world[i][j];
                }
            }
            int rockI = humanRockPosition[2];
            int rockJ = humanRockPosition[3];

            humanPossMove(humanWorld, humanRockPosition[0], humanRockPosition[1]);  // checks where the human can go from current human position
            humanPossMove(humanWorld, exitI, exitJ);                                // checks where the human can go from exit position in case, where the exit is at the end of a tunnel in which the human cant go yet,
                                                                                    // because of the rock, but at the end of the rock has valid moves to finish, there is a bug however in cases where the human pushes the rock
                                                                                    // in a place where he has no other route to go in, so no valid moves for the rock to finish, but inside there is enough space to turn around

            noGameOver = notOverYet(humanWorld, rockWorld, rockI, rockJ, exitI, exitJ, noGameOver);
//            System.out.println(noGameOver + " = noGameOver");
            afterGameOverTries = (afterGameOverTries == 5) ? 0 : afterGameOverTries;
            if (rockI == exitI && rockJ == exitJ) {
                noGameOver = false;     // in case exitI && exitJ are changed to valid moves, not triggering GameOver
                break;
            } else if (!noGameOver && afterGameOverTries == 0) {   // in case, there is a buggy situation
                System.out.println("GAME OVER, do you want to continue...[y / n]");
                String continueGame = sc.nextLine().trim().toLowerCase();
                if (continueGame.contains("n")) {
                    playing = false;
                }
            }
            afterGameOverTries = (!noGameOver) ? ++afterGameOverTries : afterGameOverTries;
        } while (playing);

        for (int i = 0; i < world[0].length; i++) {
            System.out.print(ANSI_CYAN + " -" + ANSI_RESET);
        }
        System.out.println();

        for (int i = 0; i < world.length; i++) {
            System.out.print(ANSI_CYAN + "|" + ANSI_RESET);

            for (int j = 0; j < world[i].length; j++) {
                System.out.print(rockWorld[i][j] + " ");
            }
            System.out.println(ANSI_CYAN + "|" + ANSI_RESET);
        }

        for (int i = 0; i < world[0].length; i++) {
            System.out.print(ANSI_CYAN + " -" + ANSI_RESET);
        }
        System.out.println();
        if (humanRockPosition[2] == world.length - 1 && humanRockPosition[3] == world[0].length - 1) {
            System.out.println(ANSI_CYAN + " YOU WON ");
        } else System.out.println(ANSI_RED + "GAME OVER");
    }
}


