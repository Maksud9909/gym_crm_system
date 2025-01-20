package uz.ccrew.service;

import uz.ccrew.dto.TrainerUpdateDTO;
import uz.ccrew.dto.trainer.*;
import uz.ccrew.dto.trainer.TrainerTrainingDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.base.BaseAdvancedUserService;

import java.time.LocalDate;
import java.util.List;

public interface TrainerService extends BaseAdvancedUserService<Trainer, UserCredentials, TrainerCreateDTO> {
    List<TrainerDTO> getUnassignedTrainers(String traineeUsername);

    TrainerProfileDTO getProfile(String username);

    TrainerProfileUsernameDTO update(TrainerUpdateDTO dto);

    List<TrainerTrainingDTO> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName);
}
