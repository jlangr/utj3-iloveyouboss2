package iloveyouboss.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static iloveyouboss.domain.Weight.REQUIRED;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AProfileMatcher {
   ProfileMatcher matcher = new ProfileMatcher();
   Random random = new Random();

   @Test
   void returnsScoreForAllProfiles() throws ExecutionException, InterruptedException {
      var questions = createQuestions(50);
      var profileCount = 500;
      var profiles = createProfiles(profileCount, questions);
      profiles.forEach(profile -> matcher.addProfile(profile));
      var criteria = createCriteria(questions);

      var results = matcher.badScoreProfiles(criteria);

      assertEquals(profileCount, results.size());
      assertTrue(results.values().stream().anyMatch(s -> s.intValue() > 0));
      assertTrue(results.values().stream().anyMatch(s -> s.intValue() == 0));
   }

   private Stream<Profile> createProfiles(
      int profileCount, List<BooleanQuestion> questions) {
      var questionCount = questions.size();
      var profiles = range(0, profileCount)
         .mapToObj(profileId -> {
            var profile = new Profile(String.valueOf(profileId));
            range(0, questionCount).forEach(i -> {
               var question = questions.get(i);
               profile.add(randomAnswer(question));
            });
            return profile;
         });
      return profiles;
   }

   private Criteria createCriteria(List<BooleanQuestion> questions) {
      var questionCount = questions.size();
      var criteria = new Criteria();
      criteria.add(new Criterion(matchingAnswer(questions.get(0)), REQUIRED));
      range(1, questionCount).forEach(i ->
         criteria.add(new Criterion(
            matchingAnswer(questions.get(i)), randomWeightNotRequired())));
      return criteria;
   }

   private List<BooleanQuestion> createQuestions(int questionCount) {
      return range(0, questionCount)
         .mapToObj(i -> new BooleanQuestion("question " + i))
         .toList();
   }

   private Answer randomAnswer(BooleanQuestion question) {
      return random.nextBoolean()
         ? matchingAnswer(question)
         : nonMatchingAnswer(question);
   }

   public Weight randomWeightNotRequired() {
      return Weight.values()[random.nextInt(Weight.values().length - 1) + 1];
   }

   Answer matchingAnswer(Question question) {
      return new Answer(question, Bool.TRUE);
   }

   Answer nonMatchingAnswer(Question question) {
      return new Answer(question, Bool.FALSE);
   }
}

