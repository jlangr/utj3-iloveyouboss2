package iloveyouboss.domain;

import java.time.*;

public interface Persistable {
   Long getId();
   void setId(Long id);
   Instant getCreateTimestamp();
   void setCreateTimestamp(Instant instant);
}
