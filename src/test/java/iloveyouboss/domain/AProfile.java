package iloveyouboss.domain;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static iloveyouboss.domain.Bool.FALSE;
import static iloveyouboss.domain.Bool.TRUE;
import static iloveyouboss.domain.Weight.*;
import static org.junit.jupiter.api.Assertions.*;

class AProfile {
   Profile profile = new Profile("X");
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
      criteria.add(new Criterion(paysBonusesYes, MUST_MATCH));

      var matches = profile.matches(criteria);

      assertFalse(matches);
   }

   @Test
   void matchAnswersTrueForAnyDontCareCriteria() {
      profile.add(paysBonusesNo);
      criteria.add(new Criterion(paysBonusesYes, DONT_CARE));

      var matches = profile.matches(criteria);

      assertTrue(matches);
   }

   @Test
   void matchAnswersTrueWhenAnyOfMultipleCriteriaMatch() {
      profile.add(isThereReloYes);
      profile.add(paysBonusesNo);
      criteria.add(new Criterion(isThereReloYes, IMPORTANT));
      criteria.add(new Criterion(paysBonusesYes, IMPORTANT));

      var matches = profile.matches(criteria);

      assertTrue(matches);
   }

   @Test
   void matchAnswersFalseWhenNoneOfMultipleCriteriaMatch() {
      profile.add(isThereReloNo);
      profile.add(paysBonusesNo);
      criteria.add(new Criterion(isThereReloYes, IMPORTANT));
      criteria.add(new Criterion(paysBonusesYes, IMPORTANT));

      var matches = profile.matches(criteria);

      assertFalse(matches);
   }

   @Test
   void scoreIsZeroWhenThereAreNoMatches() {
      profile.add(isThereReloNo);
      criteria.add(new Criterion(isThereReloYes, IMPORTANT));

      profile.matches(criteria);

      assertEquals(0, profile.score());
   }

   @Test
   void scoreIsCriterionValueForSingleMatch() {
      profile.add(isThereReloYes);
      criteria.add(new Criterion(isThereReloYes, IMPORTANT));

      profile.matches(criteria);

      assertEquals(IMPORTANT.getValue(), profile.score());
   }

   @Test
   void scoreAccumulatesCriterionValuesForMatches() {
      profile.add(isThereReloYes);
      profile.add(paysBonusesYes);
      profile.add(answerDaycareNo);
      criteria.add(new Criterion(isThereReloYes, IMPORTANT));
      criteria.add(new Criterion(paysBonusesYes, WOULD_PREFER));
      criteria.add(new Criterion(answerDaycareYes, VERY_IMPORTANT));

      profile.matches(criteria);

      assertEquals(
         IMPORTANT.getValue() + WOULD_PREFER.getValue(),
         profile.score());
   }


   List<String> text(Collection<Answer> answers) {
      return answers.stream()
         .map(a -> a.question().getText())
         .toList();
   }

   @Test
   void findsAnswersBasedOnPredicate() {
      var choices = new String[] {"a", "b"};
      profile.add(new Answer(new BooleanQuestion("1"), FALSE));
      profile.add(new Answer(new PercentileQuestion("2", choices), 0));
      profile.add(new Answer(new PercentileQuestion("3", choices), 0));

      var answers =
         profile.find(a -> a.question().getClass() == PercentileQuestion.class);

      assertEquals(List.of("2", "3"), text(answers));
   }
}
