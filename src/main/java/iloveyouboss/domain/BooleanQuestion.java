package iloveyouboss.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.io.Serial;
import java.util.*;

@Entity
@DiscriminatorValue(value="boolean")
public class BooleanQuestion extends Question {
   @Serial
   private static final long serialVersionUID = 1L;

   public BooleanQuestion() {}
   public BooleanQuestion(String text) {
      super(text);
   }

   @Override
   @ElementCollection
   public List<String> getAnswerChoices() {
      return List.of("No", "Yes");
   }

   @Override
   public boolean match(int expected, int actual) {
      return expected == actual;
   }
}
