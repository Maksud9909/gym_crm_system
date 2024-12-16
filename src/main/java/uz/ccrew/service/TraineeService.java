package uz.ccrew.service;

import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.AbstractCRUDBaseService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class TraineeService extends AbstractCRUDBaseService<Trainee> {
    private static final String ENTITY_NAME = "Trainee";

    public TraineeService() {
        log.debug("TraineeServiceImpl initialized");
    }

    @Autowired
    public void setDao(TraineeDAO dao) {
        super.setDao(dao);
        log.debug("TraineeDAO injected into TraineeServiceImpl");
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
