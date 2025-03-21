package uz.ccrew.dao.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.utils.QueryBuilder;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Getter
@Repository
@RequiredArgsConstructor
public class TrainingDAOImpl implements TrainingDAO {
    private final SessionFactory sessionFactory;
    private static final String HQL_QUERY_LOG = "Generated HQL Query: {}";
    private static final String FIND_BY_TRAINER_USERNAME_AND_TRAINING_YEAR_AND_MONTH = "SELECT t FROM Training t " +
                                                                                       "JOIN t.trainer tr " +
                                                                                       "JOIN tr.user u " +
                                                                                       "WHERE u.username = :username " +
                                                                                       "AND YEAR(t.trainingDate) = :year " +
                                                                                       "AND MONTH(t.trainingDate) = :month";

    @Override
    public Long create(Training training) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(training);
        Long id = training.getId();
        log.info("Created Training:{} with ID:{}", training, id);
        return id;
    }

    @Override
    public List<Training> getTraineeTrainings(String username,
                                              LocalDate fromDate,
                                              LocalDate toDate,
                                              String trainerName,
                                              String trainingTypeName) {
        Session session = getSessionFactory().getCurrentSession();
        Query<Training> query = QueryBuilder.buildAndSetTraineeTrainingsQuery(session, username, fromDate,
                toDate, trainerName, trainingTypeName);
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

    @Override
    public Optional<Training> findById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        Training training = session.get(Training.class, id);
        return Optional.ofNullable(training);
    }

    @Override
    public void update(Training training) {
        Session session = getSessionFactory().getCurrentSession();
        session.merge(training);
    }

    @Override
    public void delete(Training training) {
        Session session = getSessionFactory().getCurrentSession();
        session.remove(training);
    }

    @Override
    public List<Training> findByTrainerUsernameAndTrainingYearAndMonth(String trainerUsername, int year, int month) {
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(FIND_BY_TRAINER_USERNAME_AND_TRAINING_YEAR_AND_MONTH, Training.class)
                .setParameter("username", trainerUsername)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }
}
