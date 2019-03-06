public class UmnojenieSusSubirane {

    static int umnojenie2Nbrs(int firstNum, int secondNum){
        if (secondNum == 1){
            return firstNum;
        } return firstNum + umnojenie2Nbrs(firstNum,secondNum-1);
    }


    static int umnChrezSubirane(int num, int stepen) {
        if (stepen <= 0) {
            return 1;
        }return umnojenie2Nbrs(umnChrezSubirane(num, stepen - 1), num);
    }


    public static void main(String[] args) {

        int number = 3;
        int power = 4;
        System.out.println(umnChrezSubirane(number, power));
    }
}
