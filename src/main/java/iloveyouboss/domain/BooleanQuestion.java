package iloveyouboss.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.*;

import static java.util.Arrays.asList;

@Entity
@DiscriminatorValue(value="boolean")
public class BooleanQuestion extends Question {
   private static final long serialVersionUID = 1L;
   @Id
   private Long id;

   public BooleanQuestion() {}
   public BooleanQuestion(String text) {
      super(text);
   }

   @Override
   public List<String> getAnswerChoices() {
      return asList("No", "Yes");
   }

   @Override
   public boolean match(int expected, int actual) {
      return expected == actual;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getId() {
      return id;
   }
}
