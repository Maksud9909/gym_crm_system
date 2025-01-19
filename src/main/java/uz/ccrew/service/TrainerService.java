package uz.ccrew.service;

import uz.ccrew.dto.TrainerUpdateDTO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.dto.trainer.TrainerProfileDTO;
import uz.ccrew.dto.trainer.TrainerProfileUsernameDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.base.BaseAdvancedUserService;

import java.util.List;
import java.util.Objects;

public interface TrainerService extends BaseAdvancedUserService<Trainer, UserCredentials, TrainerCreateDTO> {
    List<TrainerDTO> getUnassignedTrainers(String traineeUsername);

    TrainerProfileDTO getProfile(String username);

    TrainerProfileUsernameDTO update(TrainerUpdateDTO dto);
}
