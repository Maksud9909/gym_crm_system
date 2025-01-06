package uz.ccrew.service.impl;

import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.service.AuthService;
import uz.ccrew.service.TrainerService;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
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
public class TrainerServiceImpl extends AbstractAdvancedUserService<Trainer, TrainerCreateDTO, TrainerUpdateDTO> implements TrainerService {
    private static final String ENTITY_NAME = "Trainer";
    private final AuthService authService;
    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainerServiceImpl(AuthService authService, TrainerDAO trainerDAO) {
        super(trainerDAO, authService);
        this.authService = authService;
        this.trainerDAO = trainerDAO;
        log.debug("TrainerService initialized");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        authService.checkAuth();
        log.info("Fetching unassigned trainers for Trainee username={}", traineeUsername);
        return trainerDAO.getUnassignedTrainers(traineeUsername);
    }

    @Override
    @Transactional
    public Long create(TrainerCreateDTO dto) {
        Objects.requireNonNull(dto, "Entity cannot be null");
        log.info("Creating {}: {}", getEntityName(), dto);
        Long id = trainerDAO.create(dto);
        Optional<Trainer> trainer = trainerDAO.findById(id);
        if (trainer.isPresent()) {
            Trainer traineeEntity = trainer.get();
            authService.register(traineeEntity.getUser().getUsername());
        }
        log.info("{} created with ID={}", getEntityName(), id);
        return id;
    }
}
