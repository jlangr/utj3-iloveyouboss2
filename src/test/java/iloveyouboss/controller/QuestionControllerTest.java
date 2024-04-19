package iloveyouboss.controller;

import java.time.*;
import java.util.*;
import java.util.stream.*;
import iloveyouboss.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionControllerTest {
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
      controller.setClock(Clock.fixed(now, ZoneId.of("America/Denver")));
      var id = controller.addBooleanQuestion("text");
      
      var question = controller.find(id);
      
      assertEquals(now, question.getCreateTimestamp());
   }
   
   @Test
   void answersMultiplePersistedQuestions() {
      controller.addBooleanQuestion("q1");
      controller.addBooleanQuestion("q2");
      controller.addPercentileQuestion("q3", new String[] { "a1", "a2"});
      
      var questions = controller.getAll();
      
      assertEquals(
         Arrays.asList("q1", "q2", "q3"),
         questions.stream().map(Question::getText).collect(Collectors.toList()));
   }
   
   @Test
   void findsMatchingEntries() {
      controller.addBooleanQuestion("alpha 1");
      controller.addBooleanQuestion("alpha 2");
      controller.addBooleanQuestion("beta 1");

      var questions = controller.findWithMatchingText("alpha");
      
      assertEquals(
         Arrays.asList("alpha 1", "alpha 2"),
         questions.stream().map(Question::getText).collect(Collectors.toList()));
   }
}
