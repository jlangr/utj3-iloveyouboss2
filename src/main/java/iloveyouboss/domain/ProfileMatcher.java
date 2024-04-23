package iloveyouboss.domain;

import java.util.*;
import java.util.concurrent.*;

// START:impl
public class ProfileMatcher {
   private List<Profile> profiles = new ArrayList<>();

   public void addProfile(Profile profile) {
      profiles.add(profile);
   }

   ExecutorService executorService =
      Executors.newFixedThreadPool(16);

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
      for (var future: futures)
         finalScores.putAll(future.get());

      executorService.shutdown();
      return finalScores;
   }

   public Map<Profile, Integer> badScoreProfiles(Criteria criteria)
      throws ExecutionException, InterruptedException {
//      var profiles = Collections.synchronizedMap(new HashMap<Profile, Integer>());
      var profiles = new HashMap<Profile, Integer>();

      var futures = new ArrayList<Future<Void>>();
      for (var profile : this.profiles) {
         futures.add(executorService.submit(() -> {
            if (!profile.matches(criteria)) profiles.put(profile, 0);
            profiles.put(profile, profile.score(criteria));
            return null;
         }));
      }

      for (var future: futures)
         future.get();

      executorService.shutdown();
      return profiles;
   }

   public void findMatchingProfiles(
      Criteria criteria, MatchListener listener) {
      profiles.stream()
         .parallel()
         .forEach(profile -> {
            var set = profile.createMatchSet(criteria);
            if (set.matches()) {
//               listener.foundMatch(profile, set);
            }
         });
   }
}
// END:impl
