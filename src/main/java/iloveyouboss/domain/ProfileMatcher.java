package iloveyouboss.domain;

import java.util.*;
import java.util.concurrent.*;

public class ProfileMatcher {
   private List<Profile> profiles = new ArrayList<>();

   public void addProfile(Profile profile) {
      profiles.add(profile);
   }

   ExecutorService executorService =
      Executors.newFixedThreadPool(8);

   // START:impl
   public Map<Profile, Integer> scoreProfiles(Criteria criteria)
      throws ExecutionException, InterruptedException {
      // START_HIGHLIGHT
      var futures = new ArrayList<Future<Map<Profile, Integer>>>();
      // END_HIGHLIGHT
      for (var profile : profiles)
         futures.add(executorService.submit(() ->
            // START_HIGHLIGHT
            Map.of(profile,
                   profile.matches(criteria) ? profile.score(criteria) : 0)));
      // END_HIGHLIGHT

      // START_HIGHLIGHT
      var finalScores = new HashMap<Profile, Integer>();
      for (var future: futures)
         finalScores.putAll(future.get());
      // END_HIGHLIGHT

      executorService.shutdown();
      return finalScores;
   }
   // END:impl
}
