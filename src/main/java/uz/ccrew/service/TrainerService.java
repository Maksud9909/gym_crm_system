package uz.ccrew.service;

import uz.ccrew.entity.Trainer;
import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.service.base.advancedBase.BaseAdvancedUserService;

import java.util.List;

public interface TrainerService extends BaseAdvancedUserService<Trainer, TrainerCreateDTO, TrainerUpdateDTO> {
    List<Trainer> getUnassignedTrainers(String traineeUsername);
}
