package uz.ccrew.dao.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.utils.QueryBuilder;
import uz.ccrew.dao.base.base.AbstractBaseDAO;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

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
    public List<Training> getTraineeTrainings(String username,
                                              LocalDate fromDate,
                                              LocalDate toDate,
                                              String trainerName,
                                              Long trainingTypeId) {
        Session session = getSessionFactory().getCurrentSession();
        String builtQuery = QueryBuilder.buildTraineeTrainingsQuery(fromDate, toDate, trainerName, trainingTypeId);
        Query<Training> query = session.createQuery(builtQuery, Training.class);

        query.setParameter("username", username);
        if (fromDate != null) query.setParameter("fromDate", fromDate);
        if (toDate != null) query.setParameter("toDate", toDate);
        if (trainerName != null) query.setParameter("trainerName", trainerName);
        if (trainingTypeId != null) query.setParameter("trainingTypeId", trainingTypeId);

        return query.getResultList();
    }

    @Override
    public List<Training> getTrainerTrainings(String username,
                                              LocalDate fromDate,
                                              LocalDate toDate,
                                              String traineeName) {
        Session session = getSessionFactory().getCurrentSession();
        String builtQuery = QueryBuilder.buildTrainerTrainingsQuery(fromDate, toDate, traineeName);
        Query<Training> query = session.createQuery(builtQuery, Training.class);

        query.setParameter("username", username);
        if (fromDate != null) query.setParameter("fromDate", fromDate);
        if (toDate != null) query.setParameter("toDate", toDate);
        if (traineeName != null) query.setParameter("traineeName", traineeName);

        return query.getResultList();
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
