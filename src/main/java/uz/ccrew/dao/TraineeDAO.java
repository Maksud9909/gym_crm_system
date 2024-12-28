package uz.ccrew.dao;

import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dao.base.advancedBase.BaseAdvancedUserCRUDDAO;

import java.util.List;

public interface TraineeDAO extends BaseAdvancedUserCRUDDAO<Trainee, TraineeCreateDTO, TraineeUpdateDTO> {
    void deleteByUsername(String username);

    void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds);
}
