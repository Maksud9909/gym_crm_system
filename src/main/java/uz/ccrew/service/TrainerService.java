package uz.ccrew.service;

import uz.ccrew.dto.trainer.*;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.service.base.BaseService;
import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.dto.trainer.TrainerTrainingDTO;

import java.util.List;
import java.time.LocalDate;

public interface TrainerService extends BaseService<UserCredentials, TrainerCreateDTO, TrainerProfileDTO> {

    List<TrainerDTO> getUnassignedTrainers(String traineeUsername);

    TrainerProfileUsernameDTO update(TrainerUpdateDTO dto);

    List<TrainerTrainingDTO> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName);
}
