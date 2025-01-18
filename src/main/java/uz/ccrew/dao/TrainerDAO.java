package uz.ccrew.dao;

import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.base.BaseUserCRUDDAO;

import java.util.List;

public interface TrainerDAO extends BaseUserCRUDDAO<Trainer> {
    List<Trainer> getUnassignedTrainers(String traineeUsername);

    List<Trainer> findByTrainerUsername(List<String> usernames);
}
