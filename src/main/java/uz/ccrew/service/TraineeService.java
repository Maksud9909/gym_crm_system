package uz.ccrew.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.advancedBase.AbstractAdvancedUserService;

import java.util.List;

@Slf4j
@Service
public class TraineeService extends AbstractAdvancedUserService<Trainee, TraineeCreateDTO> {
    private static final String ENTITY_NAME = "Trainee";
    private final TraineeDAO traineeDAO;

    public TraineeService(TraineeDAO traineeDAO) {
        super(traineeDAO);
        this.traineeDAO = traineeDAO;
        log.debug("TraineeService initialized");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }

    public void deleteTraineeByUsername(String username) {
        log.info("Deleting Trainee by username={}", username);
        traineeDAO.deleteByUsername(username);
        log.info("Trainee with username={} deleted successfully", username);
    }

    public void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds) {
        log.info("Updating trainers for Trainee ID={}", traineeId);
        traineeDAO.updateTraineeTrainers(traineeId, newTrainerIds);
        log.info("Trainers updated successfully for Trainee ID={}", traineeId);
    }
}

