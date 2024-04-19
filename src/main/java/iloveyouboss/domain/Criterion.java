package iloveyouboss.domain;

public class Criterion {
   private Weight weight;
   private Answer answer;
   private int score;

   public Criterion(Answer answer, Weight weight) {
      this.answer = answer;
      this.weight = weight;
   }
   
   public Answer getAnswer() { return answer; }
   public Weight getWeight() { return weight; }
}
