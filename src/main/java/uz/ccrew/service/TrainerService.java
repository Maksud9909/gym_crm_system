package uz.ccrew.service;

import uz.ccrew.dto.UserCredentials;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.base.BaseAdvancedUserService;

import java.util.List;

public interface TrainerService extends BaseAdvancedUserService<Trainer> {
    List<Trainer> getUnassignedTrainers(String traineeUsername, UserCredentials userCredentials);
}
