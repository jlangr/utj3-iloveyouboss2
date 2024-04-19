package iloveyouboss.domain;

import org.junit.jupiter.api.Test;

import static iloveyouboss.domain.Bool.FALSE;
import static iloveyouboss.domain.Bool.TRUE;
import static iloveyouboss.domain.Weight.*;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
   Profile profile = new Profile("Bull Hockey, Inc.");
   Criteria criteria = new Criteria();

   Question paysBonuses = new BooleanQuestion("Bonuses?");
   Answer paysBonusesYes = new Answer(paysBonuses, TRUE);
   Answer paysBonusesNo = new Answer(paysBonuses, FALSE);

   Question isThereRelo = new BooleanQuestion("Relocation?");
   Answer isThereReloYes = new Answer(isThereRelo, TRUE);
   Answer isThereReloNo = new Answer(isThereRelo, FALSE);

   Question questionDaycare = new BooleanQuestion("Daycare?");
   Answer answerDaycareNo = new Answer(questionDaycare, FALSE);
   Answer answerDaycareYes = new Answer(questionDaycare, TRUE);

   @Test
   void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
      profile.add(paysBonusesNo);
      criteria.add(new Criterion(paysBonusesYes, MustMatch));

      var matches = profile.matches(criteria);

      assertFalse(matches);
   }

   @Test
   void matchAnswersTrueForAnyDontCareCriteria() {
      profile.add(paysBonusesNo);
      criteria.add(new Criterion(paysBonusesYes, DontCare));

      var matches = profile.matches(criteria);

      assertTrue(matches);
   }

   @Test
   void matchAnswersTrueWhenAnyOfMultipleCriteriaMatch() {
      profile.add(isThereReloYes);
      profile.add(paysBonusesNo);
      criteria.add(new Criterion(isThereReloYes, Important));
      criteria.add(new Criterion(paysBonusesYes, Important));

      var matches = profile.matches(criteria);

      assertTrue(matches);
   }

   @Test
   void matchAnswersFalseWhenNoneOfMultipleCriteriaMatch() {
      profile.add(isThereReloNo);
      profile.add(paysBonusesNo);
      criteria.add(new Criterion(isThereReloYes, Important));
      criteria.add(new Criterion(paysBonusesYes, Important));

      var matches = profile.matches(criteria);

      assertFalse(matches);
   }

   @Test
   void scoreIsZeroWhenThereAreNoMatches() {
      profile.add(isThereReloNo);
      criteria.add(new Criterion(isThereReloYes, Important));

      profile.matches(criteria);

      assertEquals(0, profile.score());
   }

   @Test
   void scoreIsCriterionValueForSingleMatch() {
      profile.add(isThereReloYes);
      criteria.add(new Criterion(isThereReloYes, Important));

      profile.matches(criteria);

      assertEquals(Important.getValue(), profile.score());
   }

   @Test
   void scoreAccumulatesCriterionValuesForMatches() {
      profile.add(isThereReloYes);
      profile.add(paysBonusesYes);
      profile.add(answerDaycareNo);
      criteria.add(new Criterion(isThereReloYes, Important));
      criteria.add(new Criterion(paysBonusesYes, WouldPrefer));
      criteria.add(new Criterion(answerDaycareYes, VeryImportant));

      profile.matches(criteria);

      assertEquals(
         Important.getValue() + WouldPrefer.getValue(),
         profile.score());
   }
}
