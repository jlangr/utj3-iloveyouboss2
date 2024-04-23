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
      var futures = new ArrayList<Future<Map<Profile, Integer>>>();
      for (var profile : profiles)
         futures.add(executorService.submit(() ->
            Map.of(profile,
                   profile.matches(criteria) ? profile.score(criteria) : 0)));

      var finalScores = new HashMap<Profile, Integer>();
      for (var future: futures)
         finalScores.putAll(future.get());

      executorService.shutdown();
      return finalScores;
   }
   // END:impl
}
