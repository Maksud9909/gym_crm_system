package uz.ccrew.service.impl;

import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.TraineeService;
import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.service.base.advancedBase.AbstractAdvancedUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TraineeServiceImpl extends AbstractAdvancedUserService<Trainee, TraineeCreateDTO, TraineeUpdateDTO> implements TraineeService {
    private static final String ENTITY_NAME = "Trainee";
    private final TraineeDAO traineeDAO;

    @Autowired
    public TraineeServiceImpl(TraineeDAO traineeDAO) {
        super(traineeDAO);
        this.traineeDAO = traineeDAO;
        log.debug("TraineeService initialized");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }
        log.info("Deleting Trainee by username={}", username);
        traineeDAO.deleteByUsername(username);
        log.info("Trainee with username={} deleted successfully", username);
    }

    @Override
    @Transactional
    public void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds) {
        if (traineeId == null || newTrainerIds == null || newTrainerIds.isEmpty()) {
            throw new IllegalArgumentException("Trainee ID and Trainer IDs must not be null or empty");
        }
        log.info("Updating trainers for Trainee ID={}", traineeId);
        traineeDAO.updateTraineeTrainers(traineeId, newTrainerIds);
        log.info("Trainers updated successfully for Trainee ID={}", traineeId);
    }
}
