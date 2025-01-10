package uz.ccrew.dao;

import uz.ccrew.entity.Training;
import uz.ccrew.dao.base.BaseDAO;

import java.util.List;
import java.time.LocalDate;

public interface TrainingDAO extends BaseDAO<Training> {
    List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                       LocalDate toDate, String trainerName, Long trainingTypeId);

    List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName);
}
