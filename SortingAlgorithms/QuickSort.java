public class QuickSort {


    static void swapArrValues(int[] arr, int firstIndx, int secondIndx) {
        int temp = arr[firstIndx];
        arr[firstIndx] = arr[secondIndx];
        arr[secondIndx] = temp;
    }

    static int findPivot(int[] arr, int start, int end) {

        if (start == end){
            return start;
        }
        int sum = arr[start];
        for (int i = start + 1; i <= end; i++) {
            sum += arr[i];
        }
        int avg = sum / (end + 1);
        int diff = avg - arr[start];
        diff = diff * diff;
        int pivot = start;

        for (int i = start + 1; i <= end; i++) {
            int currDiff = avg - arr[i];
            currDiff = currDiff * currDiff;
            if (diff >= currDiff){
                diff = currDiff;
                pivot = i;
            }
        }return pivot;
    }

    static int partitize(int[] arr, int start, int end){
        if (start == end){
//            System.out.println(start); // currValue
//            System.out.println("start " + start + "\nend "+end);
            return start;
        }        int pivot = findPivot(arr, start, end);
        int currValue = start;
        if (pivot != end){
            swapArrValues(arr, pivot, end );
            pivot = end;
        }

        for (int i = start; i < pivot; i++){
            if (arr[i] <= arr[pivot]){
                swapArrValues(arr,i,currValue);
                currValue++;
            }
        }
        swapArrValues(arr, pivot, currValue);
//        System.out.println(currValue);
//        System.out.println("start " + start + "\nend "+end);
        return currValue;
    }

    static void quickSort(int[] arr, int start, int end){
        if (start >= end){
            return;
        }

        int pivotIndx = partitize(arr, start, end);
        quickSort(arr, start, pivotIndx-1);
        quickSort(arr, pivotIndx+1,end);
    }


    public static void main(String[] args) {

        int[] array = {1, 1, 1, 2, 2, 2, 2, 9, 7, 5, 6,9,423,2,4,2,1,45,1,24,2};

        quickSort(array,0,array.length-1);
//        partitize(array,0, array.length -1);

        for (int i = 0; i < array.length; i++){
            System.out.print(array[i] + " ");
        }
    }
}
