package uz.ccrew.dao;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import uz.ccrew.dao.base.base.AbstractBaseDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class TrainingDAO extends AbstractBaseDAO<Training> {
    private static final String ENTITY_NAME = "Training";

    public TrainingDAO(SessionFactory sessionFactory) {
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
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
