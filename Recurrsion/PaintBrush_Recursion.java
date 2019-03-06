public class PaintBrush {

    static boolean isValidMove(String[][] arr, int i, int j){
        boolean isValid = false;
        if (i < arr.length && i >= 0 ){
            if (j < arr[0].length && j >= 0 ){
                if (arr[i][j].equalsIgnoreCase("   ")){
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    static void paintBruschRec(String[][]arr, int i, int j  ){

        int[][] moves ={
                {1,0},
                {-1,0},
                {0,1},
                {0,-1}
        };
            arr[i][j] = "red";

            for (int x = 0; x < moves.length; x++) {
                if (isValidMove(arr,i + moves[x][0],j + moves[x][1])) {
                    paintBruschRec(arr, i + moves[x][0], j + moves[x][1]);
                }
            }
        }


    public static void main(String[] args) {

        String[][] screen = {
                {" b ", "   ", "   ", "   ", "   ", "   ", "   ", " b "},
                {" b ", "   ", "   ", "   ", "   ", "   ", "   ", " b "},
                {" b ", " b ", "   ", "   ", "   ", "   ", " b ", " b "},
                {" b ", "   ", " b ", "   ", " b ", " b ", "   ", " b "},
                {" b ", "   ", "   ", " b ", "   ", "   ", "   ", " b "},
                {" b ", "   ", "   ", "   ", "   ", "   ", "   ", " b "},
                {" b ", "   ", "   ", "   ", "   ", "   ", "   ", " b "},
                {" b ", "   ", "   ", "   ", "   ", "   ", "   ", " b "},
        };

        int xRed = 3;
        int xCol = 3;
        paintBruschRec(screen, xRed,xCol);

        for (int i = 0; i < screen.length; i++){

            for (int j = 0; j < screen[i].length; j++){
                System.out.print(screen[i][j] + " ");
            }
            System.out.println();
        }
    }
}
