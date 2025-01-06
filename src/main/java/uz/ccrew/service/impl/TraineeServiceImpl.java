package uz.ccrew.service.impl;

import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.AuthService;
import uz.ccrew.service.TraineeService;
import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.service.base.advancedBase.AbstractAdvancedUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class TraineeServiceImpl extends AbstractAdvancedUserService<Trainee, TraineeCreateDTO, TraineeUpdateDTO> implements TraineeService {
    private final TraineeDAO traineeDAO;
    private final AuthService authService;
    private static final String ENTITY_NAME = "Trainee";

    @Autowired
    public TraineeServiceImpl(TraineeDAO traineeDAO, AuthService authService) {
        super(traineeDAO, authService);
        this.traineeDAO = traineeDAO;
        this.authService = authService;
        log.debug("TraineeService initialized");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    @Transactional
    public Long create(TraineeCreateDTO dto) {
        Objects.requireNonNull(dto, "Entity cannot be null");
        log.info("Creating {}: {}", getEntityName(), dto);
        Long id = traineeDAO.create(dto);
        Optional<Trainee> trainee = traineeDAO.findById(id);
        if (trainee.isPresent()) {
            Trainee traineeEntity = trainee.get();
            authService.register(traineeEntity.getUser().getUsername());
        }
        log.info("{} created with ID={}", getEntityName(), id);
        return id;
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username) {
        authService.checkAuth();
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
        authService.checkAuth();
        if (traineeId == null || newTrainerIds == null || newTrainerIds.isEmpty()) {
            throw new IllegalArgumentException("Trainee ID and Trainer IDs must not be null or empty");
        }
        log.info("Updating trainers for Trainee ID={}", traineeId);
        traineeDAO.updateTraineeTrainers(traineeId, newTrainerIds);
        log.info("Trainers updated successfully for Trainee ID={}", traineeId);
    }
}
