package uz.ccrew.service;

import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.BaseAdvancedUserService;

import java.util.List;

public interface TraineeService extends BaseAdvancedUserService<Trainee, UserCredentials, TraineeCreateDTO> {
    void deleteTraineeByUsername(String username, UserCredentials userCredentials);

    void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds, UserCredentials userCredentials);
}
