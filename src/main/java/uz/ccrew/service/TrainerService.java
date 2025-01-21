package uz.ccrew.service;

import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.dto.trainer.*;
import uz.ccrew.dto.trainer.TrainerTrainingDTO;
import uz.ccrew.dto.user.UserCredentials;

import java.time.LocalDate;
import java.util.List;

public interface TrainerService {

    UserCredentials create(TrainerCreateDTO trainerCreateDTO);

    List<TrainerDTO> getUnassignedTrainers(String traineeUsername);

    TrainerProfileDTO getProfile(String username);

    TrainerProfileUsernameDTO update(TrainerUpdateDTO dto);

    List<TrainerTrainingDTO> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName);

    void activateDeactivate(String username, Boolean isActive);
}
