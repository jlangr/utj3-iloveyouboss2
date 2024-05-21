package iloveyouboss.domain;

// START:impl

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// START:impl
public class ProfileMatcher {

  private final List<Profile> profiles = new ArrayList<>();

  ExecutorService executorService = Executors.newFixedThreadPool(8);

  public void addProfile(Profile profile) {
    profiles.add(profile);
  }

  public Map<Profile, Integer> scoreProfiles(Criteria criteria) {
    // START_HIGHLIGHT
    Map<Profile, Integer> profiles = new HashMap<>();
    // END_HIGHLIGHT

    var futures = this.profiles.stream()
        .map(profile -> CompletableFuture.runAsync(
            () -> profiles.put(
                profile, profile.matches(criteria) ? profile.score(criteria) : 0),
            executorService))
        .toList();

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    executorService.shutdown();
    return profiles;
  }
}
// END:impl
