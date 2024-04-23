package iloveyouboss.domain;

// START:impl
import java.util.*;
import java.util.concurrent.*;

public class ProfileMatcher {
   private List<Profile> profiles = new ArrayList<>();

   public void addProfile(Profile profile) {
      profiles.add(profile);
   }

   ExecutorService executorService =
      Executors.newFixedThreadPool(8);

   public Map<Profile, Integer> scoreProfiles(Criteria criteria)
      throws ExecutionException, InterruptedException {
      // START_HIGHLIGHT
      var profiles = new HashMap<Profile, Integer>();
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
}
// END:impl
