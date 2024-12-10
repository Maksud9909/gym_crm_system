package uz.ccrew.service;

import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.base.AbstractBaseService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class TrainerService extends AbstractBaseService<Trainer, Long> {
    private TrainerDAO dao;
    private static final String ENTITY_NAME = "Trainer";

    public TrainerService() {
        log.info("TrainerServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainerDAO dao) {
        super.setDao(dao);
        this.dao = dao;
        log.info("TrainerDAO injected into TrainerServiceImpl");
    }

    @Override
    public Long create(Trainer trainer) {
        log.info("Creating Trainer: {}", trainer);
        Long id = dao.create(trainer);
        log.info("Trainer created with ID={}", id);
        return id;
    }

    public void update(Long id, Trainer trainer) {
        log.info("Updating Trainer with ID={} to new data: {}", id, trainer);
        dao.update(id, trainer);
        log.info("Trainer with ID={} updated", id);
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
