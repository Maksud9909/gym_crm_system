package uz.ccrew.service;

import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.service.base.advancedBase.BaseAdvancedUserService;

import java.util.List;

public interface TraineeService extends BaseAdvancedUserService<Trainee, TraineeCreateDTO, TraineeUpdateDTO> {
    void deleteTraineeByUsername(String username);

    void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds);
}
