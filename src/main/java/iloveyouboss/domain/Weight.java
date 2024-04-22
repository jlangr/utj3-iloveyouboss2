package iloveyouboss.domain;

public enum Weight {
   REQUIRED(0),
   VERY_IMPORTANT(5000),
   IMPORTANT(1000),
   WOULD_PREFER(100),
   DONT_CARE(1);
   
   private final int value;

   Weight(int value) { this.value = value; }
   public int getValue() { return value; }
}
