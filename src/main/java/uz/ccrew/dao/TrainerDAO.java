package uz.ccrew.dao;

import uz.ccrew.dao.base.advancedBase.BaseAdvancedUserCRUDDAO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.entity.Trainer;

import java.util.List;

public interface TrainerDAO extends BaseAdvancedUserCRUDDAO<Trainer, TrainerCreateDTO, TrainerUpdateDTO> {
    List<Trainer> getUnassignedTrainers(String traineeUsername);
}
