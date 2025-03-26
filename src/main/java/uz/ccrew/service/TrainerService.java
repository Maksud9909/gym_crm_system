package uz.ccrew.service;

import uz.ccrew.dto.trainer.*;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.service.base.BaseService;
import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.dto.trainer.TrainerTrainingDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainerService extends BaseService<UserCredentials, TrainerCreateDTO, TrainerProfileDTO> {

    List<TrainerDTO> getUnassignedTrainers(String traineeUsername);

    TrainerProfileUsernameDTO update(TrainerUpdateDTO dto);

    List<TrainerTrainingDTO> getTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate, String traineeName);
}
