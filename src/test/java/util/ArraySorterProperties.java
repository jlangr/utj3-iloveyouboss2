package util;

// START:test
import static java.util.Arrays.fill;
import static java.util.Arrays.sort;
import net.jqwik.api.*;
import java.util.Arrays;

public class ArraySorterProperties {
   ArraySorter arraySorter = new ArraySorter();

   @Property
   boolean returnsSameArrayWhenAlreadySorted(@ForAll int[] array) {
      sort(array);
      var expected = array.clone();

      arraySorter.inPlaceInsertionSort(array);

      return Arrays.equals(expected, array);
   }

   @Property
   boolean returnsSameArrayWhenAllSameElements(@ForAll int element) {
      var array = new int[12];
      fill(array, element);
      var expected = array.clone();

      arraySorter.inPlaceInsertionSort(array);

      return Arrays.equals(expected, array);
   }

   @Property
   boolean sortsAscendingWhenRandomUnsortedArray(@ForAll int[] array) {
      var expected = array.clone();
      sort(expected);

      arraySorter.inPlaceInsertionSort(array);

      return Arrays.equals(expected, array);
   }
}
// END:test
