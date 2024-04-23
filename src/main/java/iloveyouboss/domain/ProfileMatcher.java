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

   public Map<Profile, Integer> betterscoreProfiles(Criteria criteria)
      throws ExecutionException, InterruptedException {
      var futures = new ArrayList<Future<Map<Profile, Integer>>>();
      for (var profile : profiles) {
         var future = executorService.submit(() ->
            Map.of(profile,
                   profile.matches(criteria) ? profile.score(criteria) : 0));
         futures.add(future);
      }

      var finalScores = new HashMap<Profile, Integer>();
      for (var future: futures)
         finalScores.putAll(future.get());

      executorService.shutdown();
      return finalScores;
   }

   // START:impl
   public Map<Profile, Integer> scoreProfiles(Criteria criteria)
      throws ExecutionException, InterruptedException {
      // START_HIGHLIGHT
      var profiles = Collections.synchronizedMap(new HashMap<Profile, Integer>());
      // END_HIGHLIGHT

      var futures = new ArrayList<Future<Void>>();
      for (var profile: this.profiles) {
         futures.add(executorService.submit(() -> {
            profiles.put(profile,
               profile.matches(criteria) ? profile.score(criteria) : 0);
            return null;
         }));
      }

      for (var future: futures)
         future.get();

      executorService.shutdown();
      return profiles;
   }
   // START:impl
}
// END:impl
