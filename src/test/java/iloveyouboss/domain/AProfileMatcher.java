package iloveyouboss.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //<callout id="verify.createMock"/>
// END_HIGHLIGHT
class AProfileMatcher {
   // START_HIGHLIGHT
   @Mock
   private MatchListener listener;
   @InjectMocks
   ProfileMatcher matcher;
   // END_HIGHLIGHT
   // ...
   // END:test

   BooleanQuestion question = new BooleanQuestion("");
   Criteria criteria = new Criteria();
   Profile matching;
   Profile nonMatching;

   @BeforeEach
   void create() {
//      criteria.add(new Criterion(matchingAnswer(), Weight.MUST_MATCH));
//      matching = createMatchingProfile("matching");
//      nonMatching = createNonMatchingProfile("nonMatching");
   }

   Profile createMatchingProfile(String name) {
      var profile = new Profile(name);
      profile.add(matchingAnswer());
      return profile;
   }

   Profile createNonMatchingProfile(String name) {
      var profile = new Profile(name);
      profile.add(nonMatchingAnswer());
      return profile;
   }

   Answer matchingAnswer() {
      return new Answer(question, Bool.TRUE);
   }

   Answer nonMatchingAnswer() {
      return new Answer(question, Bool.FALSE);
   }

   @Test
   void x() {
      criteria.add(new Criterion(matchingAnswer(), Weight.REQUIRED));
      matching = createMatchingProfile("matching");
      nonMatching = createNonMatchingProfile("nonMatching");

      matcher.findMatchingProfiles(criteria, listener);

      verify(listener, times(1)).foundMatch(any(Profile.class), any(MatchSet.class));
   }

//   @Test
//   void collectsMatchSets() {
//      matcher.add(matching);
//      matcher.add(nonMatching);
//
//      var sets = matcher.collectMatchSets(criteria);
//
//      assertEquals(
//         new HashSet<>(asList(matching.getId(), nonMatching.getId())),
//         sets.stream().map(set -> set.getProfileId()).collect(toSet()));
//   }

   // START:test
//   @Test
//   void notifiesListenerOnMatch() {
//      matcher.add(matching);  //<callout id="verify.addMatchingProfile"/>
//      var set = matching.getMatchSet(criteria); //<callout id="verify.getMatchSet"/>
//
//      matcher.notifyOnMatch(listener, set); //<callout id="verify.nofifyOnMatch"/>
//
//      verify(listener).foundMatch(matching, set); //<callout id="verify.verify"/>
//   }
//
//   @Test
//   void doesNotNotifyListenerWhenNoMatch() {
//      matcher.add(nonMatching);
//      var set = nonMatching.getMatchSet(criteria);
//
//      matcher.notifyOnMatch(listener, set);
//
//      verify(listener, never()).foundMatch(nonMatching, set);
//   }
}
