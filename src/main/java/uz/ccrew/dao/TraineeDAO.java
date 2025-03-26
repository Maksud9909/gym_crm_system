package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.base.BaseAdvancedDAO;

public interface TraineeDAO extends BaseAdvancedDAO<Trainee> {
    void delete(Trainee trainee);
}
