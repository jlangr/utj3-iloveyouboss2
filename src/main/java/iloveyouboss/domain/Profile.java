package iloveyouboss.domain;

import java.util.HashMap;
import java.util.Map;

public class Profile {
   private final Map<String,Answer> answers = new HashMap<>();
   private int score;
   private final String name;

   public Profile(String name) {
      this.name = name;
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

   public int score() {
      return score;
   }

   @Override
   public String toString() {
     return name;
   }
}
