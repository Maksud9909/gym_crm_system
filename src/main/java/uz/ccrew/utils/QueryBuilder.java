package uz.ccrew.utils;

import uz.ccrew.entity.Training;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;

public class QueryBuilder {

    private static final StringBuilder TRAINEE_TRAININGS_QUERY = new StringBuilder(
            "SELECT t FROM Training t " +
            "JOIN t.trainee tr " +
            "JOIN tr.user u " +
            "WHERE u.username = :username"
    );

    private static final StringBuilder TRAINER_TRAININGS_QUERY = new StringBuilder(
            "SELECT t FROM Training t " +
            "JOIN t.trainer tr " +
            "JOIN tr.user u " +
            "JOIN t.trainee te " +
            "JOIN te.user tu " +
            "WHERE u.username = :username"
    );


    public static Query<Training> buildAndSetTraineeTrainingsQuery(Session session,
                                                                   String username,
                                                                   LocalDate fromDate,
                                                                   LocalDate toDate,
                                                                   String trainerName,
                                                                   Long trainingTypeId) {
        if (fromDate != null) {
            TRAINEE_TRAININGS_QUERY.append(" AND t.trainingDate >= :fromDate");
        }
        if (toDate != null) {
            TRAINEE_TRAININGS_QUERY.append(" AND t.trainingDate <= :toDate");
        }
        if (trainerName != null) {
            TRAINEE_TRAININGS_QUERY.append(" AND u.firstName LIKE CONCAT('%', :trainerName, '%')");
        }
        if (trainingTypeId != null) {
            TRAINEE_TRAININGS_QUERY.append(" AND t.trainingType.id = :trainingTypeId");
        }

        Query<Training> query = session.createQuery(TRAINEE_TRAININGS_QUERY.toString(), Training.class);
        query.setParameter("username", username);

        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        if (trainerName != null) {
            query.setParameter("trainerName", trainerName);
        }
        if (trainingTypeId != null) {
            query.setParameter("trainingTypeId", trainingTypeId);
        }

        return query;
    }

    public static Query<Training> buildAndSetTrainerTrainingsQuery(Session session, String username, LocalDate fromDate, LocalDate toDate, String traineeName) {

        if (fromDate != null) {
            TRAINER_TRAININGS_QUERY.append(" AND t.trainingDate >= :fromDate");
        }
        if (toDate != null) {
            TRAINER_TRAININGS_QUERY.append(" AND t.trainingDate <= :toDate");
        }
        if (traineeName != null) {
            TRAINER_TRAININGS_QUERY.append(" AND tu.firstName LIKE CONCAT('%', :traineeName, '%')");
        }

        Query<Training> query = session.createQuery(TRAINER_TRAININGS_QUERY.toString(), Training.class);
        query.setParameter("username", username);
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        if (traineeName != null) {
            query.setParameter("traineeName", traineeName);
        }

        return query;
    }
}
