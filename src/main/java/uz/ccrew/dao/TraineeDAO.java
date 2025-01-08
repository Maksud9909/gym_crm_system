package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.base.BaseUserCRUDDAO;

import java.util.List;

public interface TraineeDAO extends BaseUserCRUDDAO<Trainee> {

    void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds);
}
