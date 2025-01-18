package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.base.BaseUserCRUDDAO;

import java.util.List;

public interface TraineeDAO extends BaseUserCRUDDAO<Trainee> {

    void updateTraineeTrainers(String username, List<String> newTrainer);
}
