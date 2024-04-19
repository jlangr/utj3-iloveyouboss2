package iloveyouboss.domain;

import java.util.*;

public class ProfilePool {
   private List<Profile> profiles = new ArrayList<>();

   public void add(Profile profile) {
      profiles.add(profile);
   }

   public void score(Criteria criteria) {
      for (Profile profile: profiles)
         profile.matches(criteria);
   }

   public List<Profile> ranked() {
      Collections.sort(profiles, 
            (p1, p2) -> -1 * ((Integer)p1.score()).compareTo(p2.score()));
      return profiles;
   }
}
