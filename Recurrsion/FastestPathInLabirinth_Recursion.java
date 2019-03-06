public class Labirinth {

    static int[] isOptimalMove( char[][] arr, int i, int j) {
        int maxValue = -1000;
        int[][] moves = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        int[] minStepsArr = new int[2];
        for (int z = 0; z < moves.length; z++) {
            arr[i+moves[z][0]][j+moves[z][1]] = 'd';

            if (
                    i+moves[z][0] >= 0 &&
                            i+moves[z][0] <= arr.length - 1 &&
                            j+moves[z][1] >= 0 &&
                            j+moves[z][1] <= arr[0].length - 1 &&
                            arr[i+moves[z][0]][j+moves[z][1]] == 'O' ||
                            arr[i+moves[z][0]][j+moves[z][1]] == ' ') {
                int currSteps = -1;
                currSteps = maxOf(currSteps, outOf(arr, 1, i + moves[z][0], j + moves[z][1], -1));
                if (currSteps > maxValue) {
                    System.out.println("yes");
                    maxValue = currSteps;
                    minStepsArr[0] = moves[z][0];
                    minStepsArr[1] = moves[z][1];
                }
                arr[i+moves[z][0]][j+moves[z][1]] = ' ';
            }

        }
        System.out.println(i+minStepsArr[0]);
        System.out.println(j +minStepsArr[1]);
        System.out.println(maxValue);
        return minStepsArr;
    }

    static int maxOf(int one, int two) {
        if (one > two) {
            return one;
        } else return two;
    }

    static boolean isValidMove(char[][] arr, int i, int j) {
        boolean isValid = true;
        if (i < 0 || i > arr.length - 1 || j < 0 || j > arr[0].length - 1 || arr[i][j] != ' ') {
            isValid = false;
        }
        return isValid;
    }

    static int outOf(char[][] arr, int depth, int currI, int currJ, int max) {
        if (currI == 0 || currI == arr.length - 1 || currJ == 0 || currJ == arr[0].length - 1) {
            if (max < (1000 - depth)) {   //fastest path
                max = 1000 - depth;
            }
//            System.out.println(max);
        }
        int[][] moves = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        arr[currI][currJ] = 'O';

        for (int i = 0; i < moves.length; i++) {
            if (isValidMove(arr, currI + moves[i][0], currJ + moves[i][1])) {
                max = outOf(arr, depth + 1, currI + moves[i][0], currJ + moves[i][1], max);
            }
        }
        arr[currI][currJ] = ' ';
        return max;
    }

    static int fastestPath(char[][] arr, int startI, int startJ){
        int res = 1000-outOf(arr,0,startI,startJ,-1);
        if (res>1000) return -1;
        return res;
    }

    public static void main(String[] args) {

        char[][] deck = {
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', ' '},
                {'X', ' ', ' ', ' ', 'X', ' ', 'X', ' '},
                {'X', 'X', ' ', 'X', ' ', ' ', ' ', 'X'},
                {' ', ' ', ' ', ' ', ' ', 'X', ' ', 'X'},
                {' ', 'X', 'X', ' ', 'X', ' ', 'X', ' '},
                {' ', ' ', 'X', ' ', ' ', ' ', 'X', ' '},
                {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', 'X', 'X', 'X', 'X', ' '},
        };

        System.out.println(fastestPath(deck, 1, 1));
        for (int i = 0; i < deck.length; i++) {

            for (int j = 0; j < deck[0].length; j++) {
                System.out.print(deck[i][j] + " ");
            }
            System.out.println();
        }
//        int[] arr1 = isOptimalMove(deck,6,4);
//        System.out.println(arr1[0]);
//        System.out.println(arr1[1]);
    }
}
