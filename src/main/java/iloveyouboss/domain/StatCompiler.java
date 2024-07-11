package iloveyouboss.domain;

// START:responsesByQuestion
import iloveyouboss.persistence.QuestionRepository;
import java.util.*;
import java.util.concurrent.atomic.*;

public class StatCompiler {
   private QuestionRepository repository;

   public Map<String, Map<Boolean, AtomicLong>> responsesByQuestion(
         List<BooleanAnswer> answers) {
      var responses = new HashMap<Long, Map<Boolean, AtomicLong>>();
      answers.forEach(answer -> {
         var id = answer.questionId();
         Map<Boolean, AtomicLong> histogram;
         if (responses.containsKey(id))
            histogram = responses.get(id);
         else
            histogram = Map.of(
               Boolean.FALSE, new AtomicLong(0),
               Boolean.TRUE, new AtomicLong(0));
            responses.put(id, histogram);
         histogram.get(answer.value()).getAndIncrement();
      });

      var textResponses = new HashMap<String, Map<Boolean, AtomicLong>>();
      responses.keySet().forEach(id ->
         // START_HIGHLIGHT
         textResponses.put(repository.find(id).getText(), responses.get(id)));
      // END_HIGHLIGHT
      return textResponses;
   }
}
// END:responsesByQuestion
