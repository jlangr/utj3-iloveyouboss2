package iloveyouboss.domain;

import org.junit.jupiter.api.Test;

import static iloveyouboss.domain.Bool.FALSE;
import static iloveyouboss.domain.Bool.TRUE;
import static iloveyouboss.domain.Weight.IMPORTANT;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfilePoolTest {
   ProfilePool pool = new ProfilePool();
   Profile langrsoft = new Profile("Langrsoft");
   Profile smeltInc = new Profile("Smelt Inc.");
   BooleanQuestion tuitionReimburse = new BooleanQuestion("Tuition?");

   @Test
   void scoresProfileInPool() {
      langrsoft.add(new Answer(tuitionReimburse, TRUE));
      pool.add(langrsoft);

      pool.score(soleNeed(tuitionReimburse, TRUE, IMPORTANT));
      
      assertEquals(IMPORTANT.getValue(), langrsoft.score());
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

      pool.score(soleNeed(tuitionReimburse, TRUE, IMPORTANT));
      var ranked = pool.ranked();
      
      assertArrayEquals(new Profile[] { langrsoft, smeltInc }, ranked.toArray());
   }
}
