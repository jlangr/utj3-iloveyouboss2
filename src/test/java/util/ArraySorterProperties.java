package util;

// START:test
import static java.util.Arrays.fill;
import static java.util.Arrays.sort;
import static org.junit.jupiter.api.Assertions.*;
import net.jqwik.api.*;

public class ArraySorterProperties {
   ArraySorter arraySorter = new ArraySorter();

   @Property
   void returnsSameArrayWhenAlreadySorted(@ForAll int[] array) {
      sort(array);
      var expected = array.clone();

      arraySorter.inPlaceInsertionSort(array);

      assertArrayEquals(expected, array);
   }

   @Property
   void returnsSameArrayWhenAllSameElements(@ForAll int element) {
      var array = new int[12];
      fill(array, element);
      int[] expected = array.clone();

      arraySorter.inPlaceInsertionSort(array);

      assertArrayEquals(expected, array);
   }

   @Property
   void sortsAscendingWhenRandomUnsortedArray(@ForAll int[] array) {
      var expected = array.clone();
      sort(expected);

      arraySorter.inPlaceInsertionSort(array);

      assertArrayEquals(expected, array);
   }
}
// END:test
