package iloveyouboss.domain;

import java.util.*;
import java.util.stream.Stream;

public class Criteria implements Iterable<Criterion> {
   private List<Criterion> criteria = new ArrayList<>();

   public void add(Criterion criterion) {
      criteria.add(criterion);
   }

   @Override
   public Iterator<Criterion> iterator() {
      return criteria.iterator();
   }

   public Stream<Criterion> stream() {
      return criteria.stream();
   }
}
