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

   public boolean matches(Criteria criteria) {
      var matchSet = createMatchSet(criteria);
      return matchSet.matches();
   }

   public int score(Criteria criteria) {
      var matchSet = createMatchSet(criteria);
      return matchSet.getScore();
   }

   public MatchSet createMatchSet(Criteria criteria) {
      return new MatchSet(id, answers, criteria);
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
