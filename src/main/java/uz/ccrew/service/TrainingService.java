package uz.ccrew.service;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.service.base.AbstractBaseService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class TrainingService extends AbstractBaseService<Training, Long> {
    private static final String ENTITY_NAME = "Training";

    public TrainingService() {
        log.info("TrainingServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainingDAO dao) {
        super.setDao(dao);
        log.info("TrainingDAO injected into TrainingServiceImpl");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
