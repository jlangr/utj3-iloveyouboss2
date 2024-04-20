package iloveyouboss.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static iloveyouboss.domain.Bool.FALSE;
import static iloveyouboss.domain.Bool.TRUE;
import static iloveyouboss.domain.Weight.*;
import static org.junit.jupiter.api.Assertions.*;

public class AMatchSet {
   private Question areThereBonuses = new BooleanQuestion("Bonuses?");
   private Answer bonusesYes = new Answer(areThereBonuses, TRUE);
   private Answer bonusesNo = new Answer(areThereBonuses, FALSE);

   private Question isThereRelo = new BooleanQuestion("Relocation?");
   private Answer reloYes = new Answer(isThereRelo, TRUE);
   private Answer reloNo = new Answer(isThereRelo, FALSE);

   private Question isThereDaycare = new BooleanQuestion("Daycare?");
   private Answer daycareNo = new Answer(isThereDaycare, TRUE);
   private Answer daycareYes = new Answer(isThereDaycare, FALSE);

   private final Criteria criteria = new Criteria();
   private final Map<String, Answer> answers = new HashMap<>();

   private void add(Answer answer) {
      answers.put(answer.getQuestionText(), answer);
   }

   private MatchSet createMatchSet() {
      return new MatchSet("", answers, criteria);
   }

   @Test
   public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
      add(bonusesNo);
      criteria.add(new Criterion(bonusesYes, MUST_MATCH));

      var matches = createMatchSet().matches();

      assertFalse(matches);
   }

   @Test
   public void matchAnswersTrueForAnyDontCareCriteria() {
      add(bonusesNo);
      criteria.add(new Criterion(bonusesYes, DONT_CARE));

      var matches = createMatchSet().matches();

      assertTrue(matches);
   }

   @Test
   public void matchAnswersTrueWhenAnyOfMultipleCriteriaMatch() {
      add(reloYes);
      add(bonusesNo);
      criteria.add(new Criterion(reloYes, IMPORTANT));
      criteria.add(new Criterion(bonusesYes, IMPORTANT));

      var matches = createMatchSet().matches();

      assertTrue(matches);
   }

   @Test
   public void matchAnswersFalseWhenNoneOfMultipleCriteriaMatch() {
      add(reloNo);
      add(bonusesNo);
      criteria.add(new Criterion(reloYes, IMPORTANT));
      criteria.add(new Criterion(bonusesYes, IMPORTANT));

      var matches = createMatchSet().matches();

      assertFalse(matches);
   }

   @Test
   public void scoreIsZeroWhenThereAreNoMatches() {
      add(reloNo);
      criteria.add(new Criterion(reloYes, IMPORTANT));

      var score = createMatchSet().getScore();

      assertEquals(0, score);
   }

   @Test
   public void scoreIsCriterionValueForSingleMatch() {
      add(reloYes);
      criteria.add(new Criterion(reloYes, IMPORTANT));

      var score = createMatchSet().getScore();

      assertEquals(IMPORTANT.getValue(), score);
   }

   @Test
   public void scoreAccumulatesCriterionValuesForMatches() {
      add(reloYes);
      add(bonusesYes);
      add(daycareNo);
      criteria.add(new Criterion(reloYes, IMPORTANT));
      criteria.add(new Criterion(bonusesYes, WOULD_PREFER));
      criteria.add(new Criterion(daycareYes, VERY_IMPORTANT));

      var score = createMatchSet().getScore();

      assertEquals(
         IMPORTANT.getValue() + WOULD_PREFER.getValue(),
         score);
   }

   @Test
   public void sortsByScore() {
      add(new Answer(areThereBonuses, FALSE));
      criteria.add(
         new Criterion(new Answer(areThereBonuses, TRUE), IMPORTANT));

      var smallerSet = new MatchSet("1", null, null) {
         @Override
         public int getScore() { return 1; }
      };
      var largerSet = new MatchSet("2", null, null) {
         @Override
         public int getScore() { return 2; }
      };

      assertTrue(smallerSet.compareTo(largerSet) < 0);
   }
}
