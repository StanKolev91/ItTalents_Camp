public class SelSort {


    static void selSort(int[] masiv) {

        for (int j = 0; j < masiv.length / 2; j++) {
            int minIndx = j;
            int maxIndx = masiv.length - 1 - j;

            for (int i = j + 1, z = masiv.length - 2 - j; i < masiv.length - j && z >= j; i++, z--) {
                if (masiv[minIndx] > masiv[i]) {
                    minIndx = i;
                }
                if (masiv[maxIndx] < masiv[z]) {
                    maxIndx = z;
                }
            }
            if (masiv[minIndx] < masiv[j]) {
                int temp = masiv[j];
                masiv[j] = masiv[minIndx];
                masiv[minIndx] = temp;
                maxIndx = maxIndx == j ? minIndx : maxIndx;
            }
            if (masiv[maxIndx] > masiv[masiv.length - 1 - j]) {
                int tempMax = masiv[masiv.length - 1 - j];
                masiv[masiv.length - 1 - j] = masiv[maxIndx];
                masiv[maxIndx] = tempMax;
            }
        }
    }

    public static void main(String[] args) {

        int[] array = {1, 1, 1, 2, 2, 2, 2, 4, 4, 5, 5, 23, 123, 345,
                0, 1, 2, 2, 3, 3, 4, 5, 5, 6, 6, 6, 7, 89,0, 1, 1, 1,
                1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 5, 6, 6,
                6, 7, 23, 89, 123, 345};
        selSort(array);

        for (int i = 0; i < array.length; i++) {

            System.out.print(array[i] + ", ");
        }
    }
}
