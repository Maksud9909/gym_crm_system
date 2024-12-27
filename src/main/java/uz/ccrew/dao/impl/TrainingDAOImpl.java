package uz.ccrew.dao.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.dao.base.base.AbstractBaseDAO;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;

@Slf4j
@Repository
public class TrainingDAOImpl extends AbstractBaseDAO<Training> implements TrainingDAO {
    private static final String ENTITY_NAME = "Training";

    public TrainingDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Training.class);
        log.debug("TrainingDAO instantiated");
    }


    @Override
    public Long create(Training training) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(training);
        Long id = training.getId();
        log.info("Created {}: ID={}, Training={}", getEntityName(), id, training);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                              LocalDate toDate, String trainerName, TrainingType trainingType) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "SELECT tr FROM Training tr " +
                    "JOIN FETCH tr.trainee trainee " +
                    "JOIN FETCH tr.trainer trainer " +
                    "WHERE trainee.user.username = :traineeUsername " +
                    "AND (:fromDate IS NULL OR tr.trainingDate >= :fromDate) " +
                    "AND (:toDate IS NULL OR tr.trainingDate <= :toDate) " +
                    "AND (:trainerName IS NULL OR CONCAT(trainer.user.firstName, ' ', trainer.user.lastName) = :trainerName) " +
                    "AND (:trainingType IS NULL OR tr.trainingType = :trainingType)";

            return session.createQuery(hql, Training.class)
                    .setParameter("traineeUsername", traineeUsername)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("trainerName", trainerName)
                    .setParameter("trainingType", trainingType)
                    .list();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = """
                        SELECT t FROM Training t
                        JOIN FETCH t.trainer trainer
                        JOIN FETCH t.trainee trainee
                        WHERE trainer.user.username = :trainerUsername
                        AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
                        AND (:toDate IS NULL OR t.trainingDate <= :toDate)
                        AND (:traineeName IS NULL OR CONCAT(trainee.user.firstName, ' ', trainee.user.lastName) = :traineeName)
                    """;

            return session.createQuery(hql, Training.class)
                    .setParameter("trainerUsername", trainerUsername)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("traineeName", traineeName)
                    .list();
        }
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
