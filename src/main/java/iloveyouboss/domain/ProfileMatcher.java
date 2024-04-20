package iloveyouboss.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

// START:impl
public class ProfileMatcher {
   private Map<String, Profile> profiles = new HashMap<>();
   private static final int DEFAULT_POOL_SIZE = 4;

   public void add(Profile profile) {
      profiles.put(profile.getId(), profile);
   }

   public void findMatchingProfiles(
      Criteria criteria, MatchListener listener) {
      var executor = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);

      var matchSets = profiles.values().stream()
         .map(profile -> profile.getMatchSet(criteria))
         .toList();
      for (MatchSet set: matchSets) {
         Runnable runnable = () -> {
            if (set.matches())
               listener.foundMatch(profiles.get(set.getProfileId()), set);
         };
         executor.execute(runnable);
      }
      executor.shutdown();
   }
}
// END:impl
