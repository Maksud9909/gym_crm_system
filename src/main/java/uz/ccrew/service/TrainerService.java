package uz.ccrew.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.service.base.advancedBase.AbstractAdvancedUserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class TrainerService extends AbstractAdvancedUserService<Trainer, TrainerCreateDTO> {
    private static final String ENTITY_NAME = "Trainer";

    private final TrainerDAO trainerDAO;

    public TrainerService(TrainerDAO trainerDAO) {
        super(trainerDAO);
        this.trainerDAO = trainerDAO;
        log.debug("TrainerService initialized");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }

    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        log.info("Fetching trainings for Trainer username={} with filters", trainerUsername);
        return trainerDAO.getTrainerTrainings(trainerUsername, fromDate, toDate, traineeName);
    }

    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for Trainee username={}", traineeUsername);
        return trainerDAO.getUnassignedTrainers(traineeUsername);
    }
}
