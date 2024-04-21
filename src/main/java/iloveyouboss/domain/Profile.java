package iloveyouboss.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Profile {
   private final String id;
   private final Map<String,Answer> answers = new HashMap<>();
   private int score;
   public Profile(String id) {
      this.id = id;
   }

   public String getId() {
      return id;
   }

   public void add(Answer answer) {
      answers.put(answer.getQuestionText(), answer);
   }

   // badly coded to be subject of refactoring exercise
   public boolean matches(Criteria criteria) {
      score = 0;
      
      var kill = false;
      var anyMatches = false;
      for (var criterion: criteria) {
         var answer = answers.get(
               criterion.answer().getQuestionText());
         var match =
               criterion.weight() == Weight.DONT_CARE ||
               answer.match(criterion.answer());
         if (!match && criterion.weight() == Weight.MUST_MATCH) {
            kill = true;
         }
         if (match) {
            score += criterion.weight().getValue();
         }
         anyMatches |= match;
      }
      if (kill)
         return false;
      return anyMatches;
   }

   public MatchSet createMatchSet(Criteria criteria) {
      return new MatchSet(id, answers, criteria);
   }

   public int score() {
      return score;
   }

   @Override
   public String toString() {
     return id;
   }

   public List<Answer> find(Predicate<Answer> pred) {
      return answers.values().stream()
         .filter(pred)
         .toList();
   }
}
