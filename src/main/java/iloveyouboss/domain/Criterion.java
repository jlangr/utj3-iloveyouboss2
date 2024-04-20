package iloveyouboss.domain;

public record Criterion(Answer answer, Weight weight) {
   public boolean matches(Answer answer) {
      return weight() == Weight.DONT_CARE ||
         answer.match(answer());
   }
}
