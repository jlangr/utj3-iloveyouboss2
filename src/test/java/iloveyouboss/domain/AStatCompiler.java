package iloveyouboss.domain;

import iloveyouboss.controller.QuestionController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// START:test
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AStatCompiler {
   @InjectMocks
   StatCompiler stats = new StatCompiler();

   @Mock
   QuestionController controller;
   Question q1Tuition = new BooleanQuestion("Tuition?");
   Question q2Relocation = new BooleanQuestion("Relocation?");

   @Test
   void createsHistogramOfResponsesByQuestion() {
      when(controller.find(1L)).thenReturn(q1Tuition);
      when(controller.find(2L)).thenReturn(q2Relocation);
      var answers = List.of(
         new BooleanAnswer(1, true),
         new BooleanAnswer(1, true),
         new BooleanAnswer(1, true),
         new BooleanAnswer(1, false),
         new BooleanAnswer(2, true),
         new BooleanAnswer(2, true));

      var responses = stats.responsesByQuestion(answers);

      assertEquals(3,
         responses.get("Tuition?").get(true).get());
      assertEquals(1,
         responses.get("Tuition?").get(false).get());
      assertEquals(2,
         responses.get("Relocation?").get(true).get());
      assertEquals(0,
         responses.get("Relocation?").get(false).get());
   }
}
// END:test
