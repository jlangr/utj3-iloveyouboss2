package iloveyouboss.domain;

import java.time.*;

public interface Persistable {
   Long getId();
   Instant getCreateTimestamp();
   void setCreateTimestamp(Instant instant);
}
