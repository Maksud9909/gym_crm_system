package uz.ccrew.dao.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.utils.QueryBuilder;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Slf4j
@Getter
@Repository
public class TrainingDAOImpl implements TrainingDAO {
    private final SessionFactory sessionFactory;
    private static final String HQL_QUERY_LOG = "Generated HQL Query: {}";
    private static final String FIND_ALL = """
            SELECT t FROM Training t JOIN FETCH t.trainee
            JOIN FETCH t.trainer
            JOIN FETCH t.trainingType
            """;
    private static final String FIND_BY_ID = """
            SELECT t FROM Training t
            JOIN FETCH t.trainee
            JOIN FETCH t.trainer
            JOIN FETCH t.trainingType
            WHERE t.id = :id
            """;

    @Autowired
    public TrainingDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        log.debug("TrainingDAO instantiated");
    }

    @Override
    public Long create(Training training) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(training);
        Long id = training.getId();
        log.info("Created Training:{} with ID:{}", training, id);
        return id;
    }

    @Override
    public Optional<Training> findById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(FIND_BY_ID, Training.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }

    @Override
    public List<Training> findAll() {
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(FIND_ALL, Training.class)
                .getResultList();
    }

    @Override
    public List<Training> getTraineeTrainings(String username,
                                              LocalDate fromDate,
                                              LocalDate toDate,
                                              String trainerName,
                                              Long trainingTypeId) {
        Session session = getSessionFactory().getCurrentSession();
        Query<Training> query = QueryBuilder.buildAndSetTraineeTrainingsQuery(session, username, fromDate,
                toDate, trainerName, trainingTypeId);
        log.debug(HQL_QUERY_LOG, query);
        return query.getResultList();
    }


    @Override
    public List<Training> getTrainerTrainings(String username,
                                              LocalDate fromDate,
                                              LocalDate toDate,
                                              String traineeName) {
        Session session = getSessionFactory().getCurrentSession();
        Query<Training> query = QueryBuilder.buildAndSetTrainerTrainingsQuery(session, username, fromDate,
                toDate, traineeName);
        log.debug(HQL_QUERY_LOG, query);
        return query.getResultList();
    }
}
