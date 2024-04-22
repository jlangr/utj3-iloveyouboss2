package iloveyouboss.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AProfileMatcher {
   private MatchListener listener;
   ProfileMatcher matcher = new ProfileMatcher();

   BooleanQuestion question = new BooleanQuestion("");
   Criteria criteria = new Criteria();

   Profile createMatchingProfile(String id) {
      var profile = new Profile(id);
      profile.add(matchingAnswer());
      return profile;
   }

   Profile createNonMatchingProfile(String id) {
      var profile = new Profile(id);
      profile.add(nonMatchingAnswer());
      return profile;
   }

   Answer matchingAnswer() {
      return new Answer(question, Bool.TRUE);
   }

   Answer nonMatchingAnswer() {
      return new Answer(question, Bool.FALSE);
   }

   int numberOfWeights = Weight.values().length;
   Random random = new Random();

   public Weight randomWeightNotRequired() {
      return Weight.values()[random.nextInt(numberOfWeights - 1) + 1];
   }

   Answer matchingAnswer(Question question) {
      return new Answer(question, Bool.TRUE);
   }
   Answer nonMatchingAnswer(Question question) {
      return new Answer(question, Bool.FALSE);
   }

   @Test
   void profilePerformance() throws ExecutionException, InterruptedException {
      int questionCount = 50;
      var questions = createQuestions(questionCount);
      IntStream.range(0, questionCount - 3).forEach(i -> {
         var question = questions.get(i);
         criteria.add(new Criterion(matchingAnswer(question), randomWeightNotRequired()));
      });
      question = questions.get(47);
      criteria.add(new Criterion(matchingAnswer(question), Weight.REQUIRED));
      question = questions.get(48);
      criteria.add(new Criterion(matchingAnswer(question), Weight.REQUIRED));
      question = questions.get(49);
      criteria.add(new Criterion(matchingAnswer(question), Weight.REQUIRED));

      int profileCount = 500;
      IntStream.range(0, profileCount)
        .forEach(profileId -> {
           var profile = new Profile(String.valueOf(profileId));
           IntStream.range(0, questionCount).forEach(i -> {
              var question = questions.get(i);
              profile.add(randomAnswer(question));
           });
           matcher.addProfile(profile);
        });

      var start = System.currentTimeMillis();
      var results = matcher.scoreProfiles(criteria);
      var end = System.currentTimeMillis();

      System.out.println("RESULT: " + (end - start));
//      assertEquals(50, profiles.size());
//      assertTrue(profiles.stream().allMatch(p -> p.equals("matching")));
   }

   private Answer randomAnswer(BooleanQuestion question) {
      var answer = random.nextBoolean()
         ? matchingAnswer(question)
         : nonMatchingAnswer(question);
      return answer;
   }

   private static List<BooleanQuestion> createQuestions(int questionCount) {
      return IntStream.range(0, questionCount)
         .mapToObj(i -> new BooleanQuestion("question " + i))
         .toList();
   }

   @Test
   void scoringProfiles() {
      var profiles = Collections.synchronizedList(new ArrayList<String>());
      criteria.add(new Criterion(matchingAnswer(), Weight.REQUIRED));
      criteria.add(new Criterion(matchingAnswer(), Weight.IMPORTANT));
      criteria.add(new Criterion(matchingAnswer(), Weight.VERY_IMPORTANT));
      criteria.add(new Criterion(matchingAnswer(), Weight.WOULD_PREFER));
      criteria.add(new Criterion(matchingAnswer(), Weight.DONT_CARE));

      for (var i = 0; i < 100; i++)
         if (i % 2 == 0)
            matcher.addProfile(createMatchingProfile("matching"));
         else
            matcher.addProfile(createNonMatchingProfile("nonmatching"));
      listener = (profile, matchSet) ->
         profiles.add(profile.getId());

      matcher.findMatchingProfiles(criteria, listener);

      assertEquals(50, profiles.size());
      assertTrue(profiles.stream().allMatch(p -> p.equals("matching")));
   }
}
