package uz.ccrew.service;

import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Training;
import uz.ccrew.service.base.BaseService;

import java.util.List;
import java.time.LocalDate;
import java.util.Objects;

public interface TrainingService extends BaseService<Training, Object, Object> {
    List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                       LocalDate toDate, String trainerName, String trainingTypeName,UserCredentials userCredentials);

    List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName, UserCredentials userCredentials);

    void addTraining(TrainingDTO dto);
}
