package uz.ccrew.service;

import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.trainee.TraineeProfileDTO;
import uz.ccrew.dto.trainee.TraineeProfileUsernameDTO;
import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.BaseAdvancedUserService;

import java.util.List;

public interface TraineeService extends BaseAdvancedUserService<Trainee, UserCredentials, TraineeCreateDTO> {
    void deleteTraineeByUsername(String username);

    List<TrainerDTO> updateTraineeTrainers(String username, List<String> newTrainers);

    TraineeProfileDTO getProfile(String username);

    TraineeProfileUsernameDTO update(TraineeUpdateDTO dto);
}
