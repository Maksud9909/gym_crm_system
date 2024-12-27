package uz.ccrew.dao;

import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.dao.base.base.BaseDAO;

import java.util.List;
import java.time.LocalDate;

public interface TrainingDAO extends BaseDAO<Training> {
    List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                       LocalDate toDate, String trainerName, TrainingType trainingType);

    List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName);
}
