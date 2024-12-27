package uz.ccrew.service.impl;

import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.service.TrainerService;
import uz.ccrew.dao.impl.TrainerDAOImpl;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.service.base.advancedBase.AbstractAdvancedUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainerServiceImpl extends AbstractAdvancedUserService<Trainer, TrainerCreateDTO> implements TrainerService {
    private static final String ENTITY_NAME = "Trainer";

    private final TrainerDAO<Trainer, TrainerCreateDTO> trainerDAOImpl;

    public TrainerServiceImpl(TrainerDAOImpl trainerDAOImpl) {
        super(trainerDAOImpl);
        this.trainerDAOImpl = trainerDAOImpl;
        log.debug("TrainerService initialized");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }


    @Override
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for Trainee username={}", traineeUsername);
        return trainerDAOImpl.getUnassignedTrainers(traineeUsername);
    }
}
