public class Chess {
    public static void main(String[] args) {

        char[][] deck = {
                {' ', ' ', 'X', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'X', ' ', 'X', ' '},
                {' ', 'X', ' ', 'X', ' ', ' ', ' ', ' '},
                {' ', 'X', ' ', ' ', ' ', 'X', ' ', ' '},
                {' ', 'X', 'X', ' ', 'X', ' ', 'X', ' '},
                {' ', ' ', 'X', ' ', ' ', ' ', 'X', ' '},
                {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        };

        int horseI = 7;
        int horseJ = 4;
        int kingI = 4;
        int kingJ = 7;

        boolean[] horseCanReachKing = {false};

        moves(horseI, horseJ, kingI, kingJ, deck, horseCanReachKing);

        for (int i = 0; i < deck.length; i++) {

            for (int j = 0; j < deck[i].length; j++) {
                System.out.print(deck[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(horseCanReachKing[0]);
    }


    static boolean isNotValidMove(int i, int j, char[][] map) {
        if (i >= map.length || i < 0 || j >= map[0].length || j < 0 || map[i][j] != ' ') {
            return true;
        } else return false;
    }


    static void moves(int iHorse, int jHorse, int iKing, int jKing, char[][] table, boolean[] horseCanDoItFlag) {

        int[][] horsePossibleMoves = {
                { 2,  1},
                { 2, -1},
                {-2,  1},
                {-2, -1},
                { 1,  2},
                { 1, -2},
                {-1,  2},
                {-1, -2}
        };

        table[iHorse][jHorse] = '#';

        for (int a = 0; a < horsePossibleMoves.length; a++) {

            if (isNotValidMove(iHorse + horsePossibleMoves[a][0], jHorse + horsePossibleMoves[a][1], table)) {
                continue;
            }
            if (iHorse + horsePossibleMoves[a][0] == iKing && jHorse + horsePossibleMoves[a][1] == jKing) {
                table[iHorse + horsePossibleMoves[a][0]][jHorse + horsePossibleMoves[a][1]] = 'H';
                horseCanDoItFlag[0] = true;
                return;
            }
        }

        for (int a = 0; a < horsePossibleMoves.length; a++) {

            if (isNotValidMove(iHorse + horsePossibleMoves[a][0], jHorse + horsePossibleMoves[a][1], table)) {
                continue;
            }
            moves(iHorse + horsePossibleMoves[a][0], jHorse + horsePossibleMoves[a][1], iKing, jKing, table, horseCanDoItFlag);
        }
    }
}
