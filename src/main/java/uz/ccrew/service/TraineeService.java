package uz.ccrew.service;

import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.BaseAdvancedUserService;

import java.util.List;

public interface TraineeService extends BaseAdvancedUserService<Trainee> {
    void deleteTraineeByUsername(String username);

    void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds);
}
