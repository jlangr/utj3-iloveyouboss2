package util;

// START:impl
public class ArraySorter {
   public void inPlaceInsertionSort(int[] arr) {
      for (var i = 1; i < arr.length - 1; i++) {
         var key = arr[i];
         var j = i - 1;

         while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j = j - 1;
         }
         arr[j + 1] = key;
      }
   }
}
// END:impl

// Defect: The for loop should check up to arr.length, not just arr.length - 1:
//      for (var i = 1; i < arr.length; i++) {
