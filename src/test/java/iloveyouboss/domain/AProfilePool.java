package iloveyouboss.domain;

import org.junit.jupiter.api.Test;

import static iloveyouboss.domain.Bool.FALSE;
import static iloveyouboss.domain.Bool.TRUE;
import static iloveyouboss.domain.Weight.IMPORTANT;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AProfilePool {
   ProfilePool pool = new ProfilePool();
   Profile langrsoft = new Profile("Langrsoft");
   Profile smeltInc = new Profile("Smelt Inc.");
   BooleanQuestion tuitionReimburse = new BooleanQuestion("Tuition?");

   @Test
   void scoresProfileInPool() {
      langrsoft.add(new Answer(tuitionReimburse, TRUE));
      pool.add(langrsoft);
      var criteria = soleNeed(tuitionReimburse, TRUE, IMPORTANT);

      pool.score(criteria);
      
      assertEquals(IMPORTANT.getValue(), langrsoft.score(criteria));
   }

   Criteria soleNeed(Question question, int value, Weight weight) {
      var criteria = new Criteria();
      criteria.add(new Criterion(new Answer(question, value), weight));
      return criteria;
   }

   @Test
   void answersResultsInScoredOrder() {
      smeltInc.add(new Answer(tuitionReimburse, FALSE));
      pool.add(smeltInc);
      langrsoft.add(new Answer(tuitionReimburse, TRUE));
      pool.add(langrsoft);
      var criteria = soleNeed(tuitionReimburse, TRUE, IMPORTANT);

      pool.score(criteria);
      var ranked = pool.ranked(criteria);
      
      assertArrayEquals(new Profile[] { langrsoft, smeltInc }, ranked.toArray());
   }
}
