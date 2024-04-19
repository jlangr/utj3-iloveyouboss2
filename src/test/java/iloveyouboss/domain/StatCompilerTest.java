package iloveyouboss.domain;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatCompilerTest {
   // TODO MOCK
   // START:test
   @Test
   void test() {
      var stats = new StatCompiler();
      var answers = new ArrayList<BooleanAnswer>();
      answers.add(new BooleanAnswer(1, true));
      answers.add(new BooleanAnswer(1, true));
      answers.add(new BooleanAnswer(1, true));
      answers.add(new BooleanAnswer(1, false));
      answers.add(new BooleanAnswer(2, true));
      answers.add(new BooleanAnswer(2, true));
      
      var responses = stats.responsesByQuestion(answers);

      assertEquals(3,
         responses.get("Tuition?").get(Boolean.TRUE).get());
      assertEquals(1,
         responses.get("Tuition?").get(Boolean.FALSE).get());
      assertEquals(2,
         responses.get("Relocation?").get(Boolean.TRUE).get());
      assertEquals(0,
         responses.get("Relocation?").get(Boolean.FALSE).get());
   }
   // END:test
}
