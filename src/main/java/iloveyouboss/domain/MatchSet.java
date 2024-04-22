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

   // TODO update tests
   private void calculateScore() {
      score = criteria.stream()
         .filter(criterion -> criterion.matches(profileAnswerFor(criterion)) && criterion.weight() != Weight.REQUIRED)
         .map(criterion -> criterion.weight().getValue())
         .reduce(0, Integer::sum);
   }

   private Answer profileAnswerFor(Criterion criterion) {
      return answers.get(criterion.answer().getQuestionText());
   }

   public boolean matches() {
//      try {
//         Thread.sleep(1000);
//      } catch (InterruptedException e) {
//      }
      return meetsAllRequiredCriterion() && anyMatches();
   }

   private boolean meetsAllRequiredCriterion() {
      return criteria.stream()
         .filter(criterion -> criterion.weight() == Weight.REQUIRED)
         .allMatch(criterion -> criterion.matches(profileAnswerFor(criterion)));
   }

   private boolean anyMatches() {
      return criteria.stream()
         .anyMatch(criterion -> criterion.matches(profileAnswerFor(criterion)));
   }

   @Override
   public int compareTo(MatchSet that) {
      return Integer.valueOf(getScore())
         .compareTo(Integer.valueOf(that.getScore()));
   }
}
