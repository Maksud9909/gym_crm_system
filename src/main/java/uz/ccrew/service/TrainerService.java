package uz.ccrew.service;

import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.Trainer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import uz.ccrew.service.base.AbstractCRUDBaseService;

@Slf4j
@Service
public class TrainerService extends AbstractCRUDBaseService<Trainer> {
    private static final String ENTITY_NAME = "Trainer";

    public TrainerService() {
        log.debug("TrainerServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainerDAO dao) {
        super.setDao(dao);
        log.debug("TrainerDAO injected into TrainerServiceImpl");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
