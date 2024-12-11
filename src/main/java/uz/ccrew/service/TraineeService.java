package uz.ccrew.service;

import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.AbstractBaseService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import uz.ccrew.service.base.AbstractCRUDBaseService;

@Slf4j
@Service
public class TraineeService extends AbstractCRUDBaseService<Trainee, Long> {
    private TraineeDAO dao;
    private static final String ENTITY_NAME = "Trainee";

    public TraineeService() {
        log.info("TraineeServiceImpl initialized");
    }

    @Autowired
    public void setDao(TraineeDAO dao) {
        super.setDao(dao);
        this.dao = dao;
        log.info("TraineeDAO injected into TraineeServiceImpl");
    }

    @Override
    public Long create(Trainee trainee) {
        log.info("Creating Trainee: {}", trainee);
        Long id = dao.create(trainee);
        log.info("Trainee created with ID={}", id);
        return id;
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
