package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.base.BaseAdvancedDAO;

import java.util.List;

public interface TraineeDAO extends BaseAdvancedDAO<Trainee> {
    void updateTraineeTrainers(String username, List<String> newTrainerUsernames);

    void delete(Long id);
}
