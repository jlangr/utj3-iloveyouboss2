package iloveyouboss.domain;

import jakarta.persistence.*;
import java.io.*;
import java.time.*;
import java.util.*;

import static java.util.stream.IntStream.range;

@Entity
@Table(name="Question")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
public abstract class Question implements Serializable, Persistable {
   @Serial
   private static final long serialVersionUID = 1L;
   
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   @Column(updatable=false, nullable=false)
   private Long id;

   @Column
   private String text;

   @Column
   private Instant instant;

   protected Question() {}
   protected Question(String text) {
      this.text = text;
   }

   public abstract List<String> getAnswerChoices();

   public abstract boolean match(int expected, int actual);

   public Long getId() { return id; }
   public void setId(Long id) { this.id = id; }

   public String getText() { return text; }

   @Override
   public String toString() {
      var s = new StringBuilder("Question #" + getId() + ": " + getText());
      getAnswerChoices().forEach(choice -> s.append("\t").append(choice));
      return s.toString();
   }

   public String getAnswerChoice(int i) {
      return getAnswerChoices().get(i);
   }

   public int indexOf(String matchingAnswerChoice) {
      return range(0, getAnswerChoices().size())
         .filter(i -> getAnswerChoice(i).equals(matchingAnswerChoice))
         .findFirst()
         .orElse(-1);
   }

   @Override
   public Instant getCreateTimestamp() {
      return instant;
   }

   @Override
   public void setCreateTimestamp(Instant instant) {
      this.instant = instant;
   }
}
