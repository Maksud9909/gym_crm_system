package uz.ccrew.dao;

import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.dao.base.AbstractUserBaseDAO;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
public class TrainerDAO extends AbstractUserBaseDAO<Trainer> {
    private static final String ENTITY_NAME = "Trainer";

    public TrainerDAO(SessionFactory sessionFactory, Class<Trainer> entityClass) {
        super(sessionFactory, entityClass);
        log.debug("TrainerDAO instantiated");
    }

    @Override
    public Long create(Trainer trainer) {
        Session session = getSessionFactory().getCurrentSession();

        String username = generateUniqueUsername(
                trainer.getFirstName(),
                trainer.getLastName(),
                session.getSessionFactory()
        );
        trainer.setUsername(username);
        trainer.setPassword(generateRandomPassword());

        session.persist(trainer);
        Long id = trainer.getId();
        log.info("Created {}: ID={}, Trainee={}", getEntityName(), id, trainer);

        return id;
    }

    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "SELECT t FROM Training t " +
                    "JOIN t.trainer trainer " +
                    "JOIN t.trainee trainee " +
                    "WHERE trainer.username = :trainerUsername " +
                    "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
                    "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
                    "AND (:traineeName IS NULL OR trainee.firstName || ' ' || trainee.lastName = :traineeName)";

            return session.createQuery(hql, Training.class)
                    .setParameter("trainerUsername", trainerUsername)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("traineeName", traineeName)
                    .list();
        }
    }

    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "SELECT tr FROM Trainer tr " +
                    "WHERE tr.id NOT IN (" +
                    "   SELECT t.id FROM Trainee trainee " +
                    "   JOIN trainee.trainers t " +
                    "   WHERE trainee.username = :traineeUsername" +
                    ")";

            return session.createQuery(hql, Trainer.class)
                    .setParameter("traineeUsername", traineeUsername)
                    .list();
        }
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
