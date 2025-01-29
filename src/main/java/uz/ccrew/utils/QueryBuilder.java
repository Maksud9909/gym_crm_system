package uz.ccrew.utils;

import uz.ccrew.entity.Training;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;

public class QueryBuilder {

    public static Query<Training> buildAndSetTraineeTrainingsQuery(Session session,
                                                                   String username,
                                                                   LocalDate fromDate,
                                                                   LocalDate toDate,
                                                                   String trainerName,
                                                                   String trainingTypeName) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT t FROM Training t " +
                "JOIN t.trainee tr ON tr.id = t.trainee.id " +
                "JOIN tr.user u ON u.id = tr.user.id " +
                "JOIN t.trainer trainer ON trainer.id = t.trainer.id " +
                "WHERE u.username = :username"
        );

        if (fromDate != null) {
            queryBuilder.append(" AND t.trainingDate >= :fromDate");
        }
        if (toDate != null) {
            queryBuilder.append(" AND t.trainingDate <= :toDate");
        }
        if (trainerName != null) {
            queryBuilder.append(" AND trainer.user.firstName LIKE CONCAT('%', :trainerName, '%')");
        }
        if (trainingTypeName != null) {
            queryBuilder.append(" AND t.trainingType.trainingTypeName = :trainingTypeName");
        }

        Query<Training> query = session.createQuery(queryBuilder.toString(), Training.class);
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
        if (trainingTypeName != null) {
            query.setParameter("trainingTypeName", trainingTypeName);
        }

        return query;
    }

    public static Query<Training> buildAndSetTrainerTrainingsQuery(Session session,
                                                                   String username,
                                                                   LocalDate fromDate,
                                                                   LocalDate toDate,
                                                                   String traineeName) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT t FROM Training t " +
                "JOIN t.trainer tr ON tr.id = t.trainer.id " +
                "JOIN tr.user u ON u.id = tr.user.id " +
                "JOIN t.trainee te ON te.id = t.trainee.id " +
                "JOIN te.user tu ON tu.id = te.user.id " +
                "WHERE u.username = :username"
        );

        if (fromDate != null) {
            queryBuilder.append(" AND t.trainingDate >= :fromDate");
        }
        if (toDate != null) {
            queryBuilder.append(" AND t.trainingDate <= :toDate");
        }
        if (traineeName != null) {
            queryBuilder.append(" AND tu.firstName LIKE CONCAT('%', :traineeName, '%')");
        }

        Query<Training> query = session.createQuery(queryBuilder.toString(), Training.class);
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
