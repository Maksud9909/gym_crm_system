package uz.ccrew.service;

import uz.ccrew.dto.trainee.*;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.BaseAdvancedUserService;
import uz.ccrew.service.base.BaseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TraineeService extends BaseService<UserCredentials, TraineeCreateDTO, TraineeProfileDTO> {

    void deleteTraineeByUsername(String username);

    List<TrainerDTO> updateTraineeTrainers(UpdateTraineeTrainersDTO trainersDTOList);

    TraineeProfileUsernameDTO update(TraineeUpdateDTO dto);

    List<TraineeTrainingDTO> getTraineeTrainings(String username, LocalDateTime periodFrom, LocalDateTime periodTo, String trainerName, String trainingTypeName);
}
