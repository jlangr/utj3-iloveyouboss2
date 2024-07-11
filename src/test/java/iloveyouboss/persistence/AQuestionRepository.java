package iloveyouboss.persistence;

// START:test

import iloveyouboss.domain.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.time.Clock.fixed;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AQuestionRepository {
   QuestionRepository repository = new QuestionRepository();

   @BeforeEach
   void setUp() {
      repository.deleteAll();
   }

   @AfterEach
   void tearDown() {
      repository.deleteAll();
   }

   @Test
   void findsPersistedQuestionById() {
      var id = repository.addBooleanQuestion("question text");
      
      var question = repository.find(id);
      
      assertEquals("question text", question.getText());
   }
   
   @Test
   void storesDateAddedForPersistedQuestion() {
      var now = new Date().toInstant();
      repository.setClock(fixed(now, ZoneId.systemDefault()));
      var id = repository.addBooleanQuestion("text");
      
      var question = repository.find(id);
      
      assertEquals(now, question.getCreateTimestamp());
   }
   
   @Test
   void answersMultiplePersistedQuestions() {
      repository.addBooleanQuestion("q1");
      repository.addBooleanQuestion("q2");
      repository.addPercentileQuestion("q3", "a1", "a2");
      
      var questions = repository.getAll();
      
      assertEquals(asList("q1", "q2", "q3"), extractText(questions));
   }

   @Test
   void findsMatchingEntries() {
      repository.addBooleanQuestion("alpha 1");
      repository.addBooleanQuestion("alpha 2");
      repository.addBooleanQuestion("beta 1");

      var questions = repository.findWithMatchingText("alpha");
      
      assertEquals(asList("alpha 1", "alpha 2"), extractText(questions));
   }

   private List<String> extractText(List<Question> questions) {
      return questions.stream().map(Question::getText).toList();
   }
}
// END:test
