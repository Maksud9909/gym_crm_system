package uz.ccrew.service;

import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.service.base.base.BaseService;

import java.util.List;
import java.time.LocalDate;

public interface TrainingService extends BaseService<Training> {
    List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                       LocalDate toDate, String trainerName, TrainingType trainingType);

    List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName);
}
