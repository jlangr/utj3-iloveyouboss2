package iloveyouboss.persistence;

// START:impl
import iloveyouboss.domain.BooleanQuestion;
import iloveyouboss.domain.PercentileQuestion;
import iloveyouboss.domain.Persistable;
import iloveyouboss.domain.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.Clock;
import java.util.List;
import java.util.function.Consumer;

public class QuestionRepository {
   private Clock clock = Clock.systemUTC();

   private static EntityManagerFactory getEntityManagerFactory() {
      return Persistence.createEntityManagerFactory("h2-ds");
   }

   public Question find(Long id) {
      try (var em = em()) {
         return em.find(Question.class, id);
      }
   }

   public List<Question> getAll() {
      try (var em = em()) {
         return em.createQuery("select q from Question q",
            Question.class).getResultList();
      }
   }

   public List<Question> findWithMatchingText(String text) {
      try (var em = em()) {
         var queryString =
            "select q from Question q where q.text like :searchText";
         var query = em.createQuery(queryString, Question.class);
         query.setParameter("searchText", "%" + text + "%");
         return query.getResultList();
      }
   }

   public long addPercentileQuestion(String text, String... answerChoices) {
      return persist(new PercentileQuestion(text, answerChoices));
   }

   public long addBooleanQuestion(String text) {
      return persist(new BooleanQuestion(text));
   }

   void setClock(Clock clock) {
      this.clock = clock;
   }

   void deleteAll() {
      executeInTransaction(em ->
         em.createNativeQuery("delete from Question").executeUpdate());
   }

   private EntityManager em() {
      return getEntityManagerFactory().createEntityManager();
   }

   private void executeInTransaction(Consumer<EntityManager> func) {
      try (var em = em()) {
         var transaction = em.getTransaction();
         try {
            transaction.begin();
            func.accept(em);
            transaction.commit();
         } catch (Exception t) {
            if (transaction.isActive()) {
               transaction.rollback();
            }
         }
      }
   }

   private long persist(Persistable object) {
      object.setCreateTimestamp(clock.instant());
      executeInTransaction(em -> em.persist(object));
      return object.getId();
   }
}
// END:impl

