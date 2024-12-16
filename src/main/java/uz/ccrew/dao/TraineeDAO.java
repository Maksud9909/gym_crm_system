package uz.ccrew.dao;

import org.hibernate.Transaction;
import uz.ccrew.dao.base.impl.AbstractUserBaseDAO;
import uz.ccrew.entity.Trainee;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class TraineeDAO extends AbstractUserBaseDAO<Trainee> {
    private static final String ENTITY_NAME = "Trainee";

    public TraineeDAO(SessionFactory sessionFactory) {
        super(sessionFactory, Trainee.class);
        log.debug("TraineeDAO instantiated");
    }

    @Override
    public Long create(Trainee trainee) {
        Session session = getSessionFactory().getCurrentSession();

        String username = generateUniqueUsername(
                trainee.getFirstName(),
                trainee.getLastName(),
                session.getSessionFactory()
        );
        trainee.setUsername(username);
        trainee.setPassword(generateRandomPassword());

        session.persist(trainee);
        Long id = trainee.getId();
        log.info("Created {}: ID={}, Trainee={}", getEntityName(), id, trainee);

        return id;
    }

    public void deleteByUsername(String username) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "FROM Trainee t WHERE t.username = :username";
            Trainee trainee = session.createQuery(hql, Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();

            if (trainee != null) {
                session.remove(trainee);
                log.info("Deleted Trainee profile with username={}", username);
            } else {
                log.warn("Trainee with username={} not found", username);
            }
        }
    }

    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                              LocalDate toDate, String trainerName, TrainingType trainingType) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "SELECT t FROM Training t " +
                    "JOIN t.trainee trainee " +
                    "JOIN t.trainer trainer " +
                    "WHERE trainee.username = :traineeUsername " +
                    "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
                    "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
                    "AND (:trainerName IS NULL OR trainer.firstName || ' ' || trainer.lastName = :trainerName) " +
                    "AND (:trainingType IS NULL OR t.trainingType = :trainingType)";

            return session.createQuery(hql, Training.class)
                    .setParameter("traineeUsername", traineeUsername)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("trainerName", trainerName)
                    .setParameter("trainingType", trainingType)
                    .list();
        }
    }

    public void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            Trainee trainee = session.get(Trainee.class, traineeId);
            if (trainee != null) {
                List<Trainer> newTrainers = session.createQuery(
                                "FROM Trainer t WHERE t.id IN :ids", Trainer.class)
                        .setParameter("ids", newTrainerIds)
                        .list();

                trainee.getTrainers().clear();
                trainee.getTrainers().addAll(newTrainers);

                session.merge(trainee);
                log.info("Updated trainers list for Trainee ID={}", traineeId);
            } else {
                log.warn("Trainee with ID={} not found", traineeId);
            }
        } catch (Exception e) {
            log.warn("Failed to update trainers list for Trainee ID={}", traineeId, e);
            throw e;
        }
    }


    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
