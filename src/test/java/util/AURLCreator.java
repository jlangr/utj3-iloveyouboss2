package util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.net.MalformedURLException;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AURLCreator {
   URLCreator urlCreator = new URLCreator();

   @Test
   void returnsCombinedURLStringGivenServerAndDocument() throws MalformedURLException {
      assertEquals(
         "https://example.com/customer?id=123",
         urlCreator.create("example.com", "customer?id=123"));
   }

   @ParameterizedTest
   @NullSource
   @ValueSource(strings = { "", " \n\t\r " })
   void buildsURLGivenServerOnly(String document) throws MalformedURLException {
      assertEquals(
         "https://example.com",
         urlCreator.create("example.com", document));
   }

   @Test
   void eliminatesRedundantLeadingSlash() throws MalformedURLException {
      assertEquals(
         "https://example.com/customer?id=123",
         urlCreator.create("example.com", "/customer?id=123"));
   }
}