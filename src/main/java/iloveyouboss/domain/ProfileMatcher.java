package iloveyouboss.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

// START:impl
public class ProfileMatcher {
   private List<Profile> profiles = new ArrayList<>();

   public void addProfile(Profile profile) {
      profiles.add(profile);
   }

   ExecutorService executorService =
      Executors.newFixedThreadPool(16);
//      Executors.newSingleThreadExecutor();

   public Map<Profile, Integer> scoreProfiles(Criteria criteria)
      throws ExecutionException, InterruptedException {
      var futures = new ArrayList<Future<Map<Profile, Integer>>>();
      for (var profile : profiles) {
         var future = executorService.submit(() -> {
            if (!profile.matches(criteria)) return Map.of(profile, 0);
            return Map.of(profile, profile.score(criteria));
         });
         futures.add(future);
      }

      var finalScores = new HashMap<Profile, Integer>();
      for (var future: futures) {
         finalScores.putAll(future.get());
      }

      executorService.shutdown();
      return finalScores;
   }

   public void findMatchingProfiles(
      Criteria criteria, MatchListener listener) {
      profiles.stream()
         .parallel()
         .forEach(profile -> {
            var set = profile.createMatchSet(criteria);
            if (set.matches())
               listener.foundMatch(profile, set);
         });
   }
}
// END:impl
