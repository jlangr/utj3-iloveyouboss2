package iloveyouboss.domain;

public record Answer(Question question, int i) {
   public Answer(Question question, String matchingValue) {
      this(question, question.indexOf(matchingValue));
   }
   
   public String getQuestionText() {
      return question.getText();
   }

   @Override
   public String toString() {
      return String.format("%s %s", question.getText(), question.getAnswerChoice(i));
   }

   public boolean match(Answer otherAnswer) {
      return question.match(i, otherAnswer.i);
   }
}
