public class MergeSort {

    static int[] mergeSort(int[] array) {
        if (array.length < 2) {
            return array;
        }
        int[] arr1 = new int[array.length / 2];
        int[] arr2 = new int[array.length - arr1.length];
        for (int i = 0; i < array.length; i++) {
            if (i < arr1.length) {
                arr1[i] = array[i];
            } else {
                arr2[i - arr1.length] = array[i];
            }
        }
        arr1 = mergeSort(arr1);
        arr2 = mergeSort(arr2);
        int[] arrResult = new int[arr1.length + arr2.length];
        int arrResultIndx = 0;
        int arr1Indx = 0;
        int arr2Indx = 0;

        while (arr1Indx < arr1.length && arr2Indx < arr2.length) {
            if (arr1[arr1Indx] <= arr2[arr2Indx]) {   //  "=" making the algorithm stable
                arrResult[arrResultIndx] = arr1[arr1Indx];
                arr1Indx++;
            } else {
                arrResult[arrResultIndx] = arr2[arr2Indx];
                arr2Indx++;
            }
            arrResultIndx++;
        }

        while (arr1Indx < arr1.length) {
            arrResult[arrResultIndx] = arr1[arr1Indx];
            arr1Indx++;
            arrResultIndx++;
        }

        while (arr2Indx < arr2.length) {
            arrResult[arrResultIndx] = arr2[arr2Indx];
            arr2Indx++;
            arrResultIndx++;
        }
        return arrResult;
    }

    public static void main(String[] args) {

        int[] array = {1, 1, 1, 2, 2, 2, 2, 4, 4, 5, 5, 23, 123, 345, 0, 1, 2,
                2, 3, 3, 4, 5, 5, 6, 6, 6, 7, 89, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2,
                2, 3, 3, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 23, 89, 123, 345};
        int[] arraySorted = mergeSort(array);
        for (int i = 0; i < arraySorted.length; i++) {
            System.out.print(arraySorted[i] + ", ");
            if (i == arraySorted.length-1) System.out.println("\b\b");
        }
    }
}
