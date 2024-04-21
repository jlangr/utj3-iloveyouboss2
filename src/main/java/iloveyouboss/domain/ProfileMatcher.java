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
//      var executor = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);

      profiles.values().stream()
         .parallel()
         .forEach(profile -> {
            var set = profile.createMatchSet(criteria);
            if (set.matches())
               listener.foundMatch(profile, set);
         });

//      for (var set: matchSets) {
//         Runnable runnable = () -> {
//            if (set.matches())
//               listener.foundMatch(profiles.get(set.getProfileId()), set);
//         };
//         executor.execute(runnable);
//      }
//      executor.shutdown();
   }
}
// END:impl
