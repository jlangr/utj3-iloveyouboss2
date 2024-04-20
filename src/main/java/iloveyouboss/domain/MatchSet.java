package iloveyouboss.domain;

import java.util.Map;

public class MatchSet implements Comparable<MatchSet> {
   private Map<String, Answer> answers;
   private Criteria criteria;
   private int score = Integer.MIN_VALUE;
   private String profileId;

   public MatchSet(String profileId, Map<String, Answer> answers, Criteria criteria) {
      this.profileId = profileId;
      this.answers = answers;
      this.criteria = criteria;
   }

   public String getProfileId() {
      return profileId;
   }

   public int getScore() {
      if (score == Integer.MIN_VALUE) calculateScore();
      return score;
   }

   private void calculateScore() {
      score = 0;
      for (Criterion criterion : criteria)
         if (criterion.matches(answerMatching(criterion)))
            score += criterion.weight().getValue();
   }

   private Answer answerMatching(Criterion criterion) {
      return answers.get(criterion.answer().getQuestionText());
   }

   public boolean matches() {
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
      if (doesNotMeetAnyMustMatchCriterion())
         return false;
      return anyMatches();
   }

   private boolean doesNotMeetAnyMustMatchCriterion() {
      for (Criterion criterion : criteria) {
         boolean match = criterion.matches(answerMatching(criterion));
         if (!match && criterion.weight() == Weight.MUST_MATCH)
            return true;
      }
      return false;
   }

   private boolean anyMatches() {
      var anyMatches = false;
      for (var criterion : criteria)
         anyMatches |= criterion.matches(answerMatching(criterion));
      return anyMatches;
   }

   @Override
   public int compareTo(MatchSet that) {
      return Integer.valueOf(getScore())
         .compareTo(Integer.valueOf(that.getScore()));
   }
}
