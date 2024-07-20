package util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnArraySorter {
   ArraySorter arraySorter = new ArraySorter();

   @Test
   public void returnsSameOrderWhenAlreadySorted() {
      int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
      arraySorter.inPlaceInsertionSort(array);
      assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, array);
   }

   @Test
   public void returnsUnchangedWhenAllSameElements() {
      int[] array = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};

      arraySorter.inPlaceInsertionSort(array);

      assertArrayEquals(
         new int[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, array);
   }


   @Test
   public void returnsInOrderWhenUnsorted() {
      int[] array = {5, 2, 9, 1, 5, 6, 7, 3, 4, 8, 0, 11};

      arraySorter.inPlaceInsertionSort(array);

      assertArrayEquals(
         new int[]{0, 1, 2, 3, 4, 5, 5, 6, 7, 8, 9, 11}, array);
   }

   @Disabled
   @Test
   public void returnsInOrderWhenAlreadySortedExceptLast() {
      int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0};

      arraySorter.inPlaceInsertionSort(array);

      assertArrayEquals(new int[]{0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, array);
   }
}