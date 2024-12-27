package uz.ccrew.dao;

import uz.ccrew.dao.base.advancedBase.BaseAdvancedUserCRUDDAO;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.entity.Trainee;

import java.util.List;

public interface TraineeDAO extends BaseAdvancedUserCRUDDAO<Trainee, TraineeCreateDTO> {
    void deleteByUsername(String username);

    void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds);
}
