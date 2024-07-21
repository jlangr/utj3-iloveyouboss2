package util;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import java.net.MalformedURLException;

public class AURLCreatorFuzzer {
   @FuzzTest
   public void fuzzTestIsValidURL(FuzzedDataProvider data) throws MalformedURLException {
      var server = data.consumeString(32);
      var document = data.consumeRemainingAsString();
      new URLCreator().create(server, document);
   }
}
