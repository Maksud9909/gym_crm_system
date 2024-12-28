package uz.ccrew.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.service.AuthService;
import uz.ccrew.service.TrainerService;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.service.base.advancedBase.AbstractAdvancedUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainerServiceImpl extends AbstractAdvancedUserService<Trainer, TrainerCreateDTO, TrainerUpdateDTO> implements TrainerService {
    private static final String ENTITY_NAME = "Trainer";

    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainerServiceImpl(TrainerDAO trainerDAO, AuthService authService) {
        super(trainerDAO,authService);
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
        checkAuthentication();
        log.info("Fetching unassigned trainers for Trainee username={}", traineeUsername);
        return trainerDAO.getUnassignedTrainers(traineeUsername);
    }
}
