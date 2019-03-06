public class RadixSort {

    static int[] countinSort(int[] arr, int digit){

        int[] counter = new int[10];

        for (int i = 0; i < arr.length; i++){
            counter[digitOf(arr[i],digit)]++;
        }

        for (int i = 1; i < counter.length; i++){
            counter[i] += counter[i-1];
        }

        int[] resultArr = new int[arr.length];

        for (int i = arr.length-1; i >= 0; i--){
            int currentDigit = digitOf(arr[i],digit);
            int resultIndx = counter[currentDigit]-1;
            resultArr[resultIndx] = arr[i];
            counter[currentDigit]--;
        }return resultArr;
    }

    static int digitOf(int number, int digit){
        int powerOfTen = (int) Math.pow(10,(double)digit-1);
        return (number/powerOfTen)%10;
    }

    static int findMax(int[]arr){
        int max = arr[0];

        for (int i = 0; i < arr.length; i++){
            if (max < arr[i]){
                max = arr[i];
            }
        }return max;
    }

    static int findDigits(int number){
        if (number < 10){
            return 1;
        }return 1+findDigits(number/10);
    }

    static int[] radixSort (int[] array){
        int maxDigits = findDigits(findMax(array));
        int[] result = new int[array.length];

        for (int i = 0; i < array.length; i++){
            result[i] = array[i];
        }

        for (int digit = 1; digit <= maxDigits; digit++ ){
            result = countinSort(result,digit);
        }return result;
    }

    public static void main(String[] args) {

        int[] array = {1, 1, 1, 2, 2, 2, 2, 4, 4, 5, 5, 23, 123, 345, 0, 1, 2, 2, 3, 3, 4,
                5, 5, 6, 6, 6, 7, 89, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5,
                5, 5, 6, 6, 6, 7, 23, 89, 123, 345};
        int[] arraySorted = radixSort(array);
        for (int i = 0; i < arraySorted.length; i++) {
            System.out.print(arraySorted[i] + ", ");
            if (i == arraySorted.length-1) System.out.println("\b\b");
        }
    }
}
