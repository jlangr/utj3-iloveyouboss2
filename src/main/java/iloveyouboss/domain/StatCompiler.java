package iloveyouboss.domain;

import java.util.*;
import java.util.concurrent.atomic.*;

public class StatCompiler {
   static Question q1 = new BooleanQuestion("Tuition?");
   static Question q2 = new BooleanQuestion("Relo?");

   class QuestionController {
      Question find(long id) {
         return id == 1 ? q1 : q2;
      }
   }

   private QuestionController controller = new QuestionController();

   public Map<String, Map<Boolean, AtomicInteger>> responsesByQuestion(
         List<BooleanAnswer> answers) {
      Map<Integer, Map<Boolean, AtomicInteger>> responses = new HashMap<>();
      answers.stream().forEach(answer -> incrementHistogram(responses, answer));
      return convertHistogramIdsToText(responses);
   }

   private Map<String, Map<Boolean, AtomicInteger>> convertHistogramIdsToText(
         Map<Integer, Map<Boolean, AtomicInteger>> responses) {
      Map<String, Map<Boolean, AtomicInteger>> textResponses = new HashMap<>();
      responses.keySet().stream().forEach(id -> 
         textResponses.put(controller.find(id).getText(), responses.get(id)));
      return textResponses;
   }

   private void incrementHistogram(
         Map<Integer, Map<Boolean, AtomicInteger>> responses, 
         BooleanAnswer answer) {
      Map<Boolean, AtomicInteger> histogram = 
            getHistogram(responses, answer.questionId());
      histogram.get(Boolean.valueOf(answer.value())).getAndIncrement();
   }

   private Map<Boolean, AtomicInteger> getHistogram(
         Map<Integer, Map<Boolean, AtomicInteger>> responses, int id) {
      if (responses.containsKey(id)) return responses.get(id);

      var histogram = createNewHistogram();
      responses.put(id, histogram);
      return histogram;
   }

   private Map<Boolean, AtomicInteger> createNewHistogram() {
      return Map.of(
         Boolean.FALSE, new AtomicInteger(0),
         Boolean.TRUE, new AtomicInteger(0));
   }
}
