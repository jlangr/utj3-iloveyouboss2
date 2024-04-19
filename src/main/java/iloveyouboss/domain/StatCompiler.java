package iloveyouboss.domain;

import java.util.*;
import java.util.concurrent.atomic.*;

public class StatCompiler {
   static Question q1 = new BooleanQuestion("Tuition?");
   static Question q2 = new BooleanQuestion("Relocation?");

   static class QuestionController {
      Question find(long id) {
         return id == 1 ? q1 : q2;
      }
   }

   private final QuestionController controller = new QuestionController();

   // START:responsesByQuestion
   public Map<String, Map<Boolean, AtomicInteger>> responsesByQuestion(
         List<BooleanAnswer> answers) {
      var responses = new HashMap<Integer, Map<Boolean, AtomicInteger>>();
      answers.forEach(answer -> {
         int id = answer.questionId();
         Map<Boolean, AtomicInteger> histogram;
         if (((Map<Integer, Map<Boolean, AtomicInteger>>) responses).containsKey(id))
            histogram = ((Map<Integer, Map<Boolean, AtomicInteger>>) responses).get(id);
         else
            histogram = Map.of(
               Boolean.FALSE, new AtomicInteger(0),
               Boolean.TRUE, new AtomicInteger(0));
            responses.put(id, histogram);
         histogram.get(answer.value()).getAndIncrement();
      });
      var textResponses = new HashMap<String, Map<Boolean, AtomicInteger>>();
      responses.keySet().forEach(id ->
         // START_HIGHLIGHT
         textResponses.put(controller.find(id).getText(), responses.get(id)));
      // END_HIGHLIGHT
      return textResponses;
   }

   // END:responsesByQuestion
}
