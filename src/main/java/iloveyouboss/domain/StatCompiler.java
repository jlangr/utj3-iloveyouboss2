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
      answers.forEach(answer -> incrementHistogram(responses, answer));
      return convertHistogramIdsToText(responses);
   }

   private Map<String, Map<Boolean, AtomicInteger>> convertHistogramIdsToText(
         Map<Integer, Map<Boolean, AtomicInteger>> responses) {
      var textResponses = new HashMap<String, Map<Boolean, AtomicInteger>>();
      responses.keySet().forEach(id ->
         // START_HIGHLIGHT
         textResponses.put(controller.find(id).getText(), responses.get(id)));
      // END_HIGHLIGHT
      return textResponses;
   }

   private void incrementHistogram(
         Map<Integer, Map<Boolean, AtomicInteger>> responses, 
         BooleanAnswer answer) {
      var histogram = getHistogram(responses, answer.questionId());
      histogram.get(answer.value()).getAndIncrement();
   }

   private Map<Boolean, AtomicInteger> getHistogram(
         Map<Integer, Map<Boolean, AtomicInteger>> responses, int id) {
      if (responses.containsKey(id)) return responses.get(id);

      var histogram = Map.of(
         Boolean.FALSE, new AtomicInteger(0),
         Boolean.TRUE, new AtomicInteger(0));
      responses.put(id, histogram);
      return histogram;
   }
   // END:responsesByQuestion
}
