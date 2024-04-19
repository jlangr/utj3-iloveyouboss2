package iloveyouboss.domain;

import jakarta.persistence.*;

import java.util.*;

@Entity
@DiscriminatorValue(value="percentile")
public class PercentileQuestion extends Question {
   private static final long serialVersionUID = 1L;

   @ElementCollection
   @CollectionTable(name="AnswerChoice",
                    joinColumns=@JoinColumn(name="question_id"))
   private List<String> answerChoices;
   @Id
   private Long id;

   public PercentileQuestion() {}
   public PercentileQuestion(String text, String[] answerChoices) {
      super(text);
      this.answerChoices = Arrays.asList(answerChoices);
   }

   public List<String> getAnswerChoices() {
      return answerChoices;
   }

   @Override
   public boolean match(int expected, int actual) {
      return expected <= actual;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getId() {
      return id;
   }
}
