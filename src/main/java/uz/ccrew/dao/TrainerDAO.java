package uz.ccrew.dao;

import uz.ccrew.dao.base.BaseAdvancedDAO;
import uz.ccrew.entity.Trainer;

import java.util.List;

public interface TrainerDAO extends BaseAdvancedDAO<Trainer> {
    List<Trainer> getUnassignedTrainers(String traineeUsername);

    List<Trainer> findByTrainerUsername(List<String> usernames);
}
