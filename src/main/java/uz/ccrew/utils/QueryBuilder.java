package uz.ccrew.utils;

import java.time.LocalDate;

public class QueryBuilder {
    public static String buildTraineeTrainingsQuery(LocalDate fromDate, LocalDate toDate, String trainerName, Long trainingTypeId) {
        StringBuilder query = new StringBuilder("SELECT t FROM Training t JOIN t.trainer tr JOIN tr.user u" +
                                                " WHERE u.username = :username");

        if (fromDate != null) {
            query.append(" AND t.trainingDate >= :fromDate");
        }
        if (toDate != null) {
            query.append(" AND t.trainingDate <= :toDate");
        }
        if (trainerName != null) {
            query.append(" AND (u.firstName LIKE CONCAT('%', :trainerName, '%')");
        }
        if (trainingTypeId != null) {
            query.append(" AND t.trainingType.id = :trainingTypeId");
        }
        return query.toString();
    }

    public static String buildTrainerTrainingsQuery(LocalDate fromDate, LocalDate toDate, String traineeName) {
        StringBuilder query = new StringBuilder("SELECT t FROM Training t JOIN t.trainer tr JOIN tr.user u JOIN t.trainee te JOIN te.user tu" +
                                                " WHERE u.username = :username");

        if (fromDate != null) {
            query.append(" AND t.trainingDate >= :fromDate");
        }
        if (toDate != null) {
            query.append(" AND t.trainingDate <= :toDate");
        }
        if (traineeName != null) {
            query.append(" AND (tu.firstName LIKE CONCAT('%', :traineeName, '%')");
        }

        return query.toString();
    }
}
