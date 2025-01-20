package uz.ccrew.service;

import uz.ccrew.dto.trainee.*;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.BaseAdvancedUserService;

import java.time.LocalDate;
import java.util.List;

public interface TraineeService {
    UserCredentials create(TraineeCreateDTO traineeCreateDTO);

    void deleteTraineeByUsername(String username);

    List<TrainerDTO> updateTraineeTrainers(String username, List<String> newTrainers);

    TraineeProfileDTO getProfile(String username);

    TraineeProfileUsernameDTO update(TraineeUpdateDTO dto);

    List<TraineeTrainingDTO> getTraineeTrainings(String username, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingTypeName);

    void activateDeactivate(String username, Boolean isActive);
}
