package iloveyouboss.controller;

// START:test
import java.time.*;
import java.util.*;
import iloveyouboss.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.time.Clock.fixed;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AQuestionController {
   QuestionController controller = new QuestionController();

   @BeforeEach
   void create() {
      controller.deleteAll();
   }

   @Test
   void findsPersistedQuestionById() {
      var id = controller.addBooleanQuestion("question text");
      
      var question = controller.find(id);
      
      assertEquals("question text", question.getText());
   }
   
   @Test
   void questionAnswersDateAdded() {
      var now = new Date().toInstant();
      controller.setClock(fixed(now, ZoneId.systemDefault()));
      var id = controller.addBooleanQuestion("text");
      
      var question = controller.find(id);
      
      assertEquals(now, question.getCreateTimestamp());
   }
   
   @Test
   void answersMultiplePersistedQuestions() {
      controller.addBooleanQuestion("q1");
      controller.addBooleanQuestion("q2");
      controller.addPercentileQuestion("q3", "a1", "a2");
      
      var questions = controller.getAll();
      
      assertEquals(asList("q1", "q2", "q3"), extractText(questions));
   }

   @Test
   void findsMatchingEntries() {
      controller.addBooleanQuestion("alpha 1");
      controller.addBooleanQuestion("alpha 2");
      controller.addBooleanQuestion("beta 1");

      var questions = controller.findWithMatchingText("alpha");
      
      assertEquals(asList("alpha 1", "alpha 2"), extractText(questions));
   }

   private List<String> extractText(List<Question> questions) {
      return questions.stream().map(Question::getText).collect(toList());
   }
}
// END:test
