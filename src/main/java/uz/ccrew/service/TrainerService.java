package uz.ccrew.service;

import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.base.BaseAdvancedUserService;

import java.util.List;
import java.util.Objects;

public interface TrainerService extends BaseAdvancedUserService<Trainer, UserCredentials, TrainerCreateDTO> {
    List<Trainer> getUnassignedTrainers(String traineeUsername, UserCredentials userCredentials);
}
