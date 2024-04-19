package iloveyouboss.domain;

import java.util.*;

public class ProfilePool {
   private final List<Profile> profiles = new ArrayList<>();

   public void add(Profile profile) {
      profiles.add(profile);
   }

   public void score(Criteria criteria) {
      profiles.forEach(profile -> profile.matches(criteria));
   }

   public List<Profile> ranked() {
      profiles.sort((p1, p2) -> -1 * Integer.compare(p1.score(), p2.score()));
      return profiles;
   }
}
