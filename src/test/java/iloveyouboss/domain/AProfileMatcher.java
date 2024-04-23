package iloveyouboss.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static iloveyouboss.domain.Weight.REQUIRED;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AProfileMatcher {
   ProfileMatcher matcher = new ProfileMatcher();

   @Test
   void returnsScoreForAllProfiles()
         throws ExecutionException, InterruptedException {
      var questions = createQuestions(50);
      int halfCount = 250;
      range(0, halfCount)
         .forEach(id ->
            matcher.addProfile(createProfile(
               questions, id, i -> nonMatchingAnswer(questions.get(i)))));
      range(halfCount, 2 * halfCount)
         .forEach(id ->
            matcher.addProfile(createProfile(
               questions, id, i -> matchingAnswer(questions.get(i)))));
      var criteria = createCriteria(questions);

      var results = matcher.scoreProfiles(criteria);

      assertEquals(250,
         results.values().stream().filter(score -> score == 0).count());
      assertEquals(250,
         results.values().stream().filter(score -> score > 0).count());
   }

   private Profile createProfile(
      List<BooleanQuestion> questions, int id, Function<Integer, Answer> answerFunction) {
      var profile = new Profile(String.valueOf(id));
      range(0, questions.size()).forEach(i -> profile.add(answerFunction.apply(i)));
      return profile;
   }

   private Criteria createCriteria(List<BooleanQuestion> questions) {
      var questionCount = questions.size();
      var criteria = new Criteria();
      range(0, 5).forEach(i ->
         criteria.add(new Criterion(
            matchingAnswer(questions.get(i)), REQUIRED)));
      range(5, questionCount).forEach(i ->
         criteria.add(new Criterion(
            matchingAnswer(questions.get(i)), Weight.WOULD_PREFER)));
      return criteria;
   }

   private List<BooleanQuestion> createQuestions(int questionCount) {
      return range(0, questionCount)
         .mapToObj(i -> new BooleanQuestion("question " + i))
         .toList();
   }

   Answer matchingAnswer(Question question) {
      return new Answer(question, Bool.TRUE);
   }

   Answer nonMatchingAnswer(Question question) {
      return new Answer(question, Bool.FALSE);
   }
}

