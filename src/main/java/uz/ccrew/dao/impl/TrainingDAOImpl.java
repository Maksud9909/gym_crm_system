package uz.ccrew.dao.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.dao.base.base.AbstractBaseDAO;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Slf4j
@Repository
public class TrainingDAOImpl extends AbstractBaseDAO<Training> implements TrainingDAO {
    private static final String ENTITY_NAME = "Training";

    private static final String GET_TRAINEE_TRAININGS = """
            SELECT t FROM Training t
            JOIN t.trainee tr
            JOIN tr.user u
            JOIN t.trainer tn
            JOIN tn.user tu
            WHERE u.username = :username
            AND (cast(:fromDate as date) IS NULL OR t.trainingDate >= :fromDate)
            AND (cast(:toDate as date) IS NULL OR t.trainingDate <= :toDate)
            AND (:trainerName IS NULL 
                OR LOWER(tu.firstName) LIKE LOWER(CONCAT('%', :trainerName, '%'))
                OR LOWER(tu.lastName) LIKE LOWER(CONCAT('%', :trainerName, '%')))
            AND (cast(:trainingTypeId as long) IS NULL OR t.trainingType.id = :trainingTypeId)
            ORDER BY t.trainingDate ASC
            """;

    private static final String GET_TRAINER_TRAININGS = """
            SELECT t FROM Training t
            JOIN t.trainer tr
            JOIN tr.user u
            JOIN t.trainee te
            JOIN te.user tu
            WHERE u.username = :username
            AND (cast(:fromDate as date) IS NULL OR t.trainingDate >= :fromDate)
            AND (cast(:toDate as date) IS NULL OR t.trainingDate <= :toDate)
            AND (:traineeName IS NULL 
                OR LOWER(tu.firstName) LIKE LOWER(CONCAT('%', :traineeName, '%'))
                OR LOWER(tu.lastName) LIKE LOWER(CONCAT('%', :traineeName, '%')))
            ORDER BY t.trainingDate ASC
            """;

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
    public List<Training> getTraineeTrainings(String username,
                                              LocalDate fromDate,
                                              LocalDate toDate,
                                              String trainerName,
                                              Long trainingTypeId) {
        Session session = getSessionFactory().getCurrentSession();
        Query<Training> query = session.createQuery(GET_TRAINEE_TRAININGS, Training.class);

        query.setParameter("username", username);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("trainerName", trainerName);
        query.setParameter("trainingTypeId", trainingTypeId);

        List<Training> trainings = query.getResultList();
        log.debug("Found {} trainee trainings for username: {}", trainings.size(), username);

        return trainings;
    }

    @Override
    public List<Training> getTrainerTrainings(String username,
                                              LocalDate fromDate,
                                              LocalDate toDate,
                                              String traineeName) {
        Session session = getSessionFactory().getCurrentSession();
        Query<Training> query = session.createQuery(GET_TRAINER_TRAININGS, Training.class);

        query.setParameter("username", username);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("traineeName", traineeName);

        List<Training> trainings = query.getResultList();
        log.debug("Found {} trainer trainings for username: {}", trainings.size(), username);

        return trainings;
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}