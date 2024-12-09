package uz.ccrew.service;

import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.base.AbstractBaseService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class TraineeService extends AbstractBaseService<Trainee,Long> {
    private TraineeDAO dao;

    public TraineeService() {
        log.info("TraineeServiceImpl initialized");
    }

    @Autowired
    public void setDao(TraineeDAO dao) {
        super.dao = dao;
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

    public void delete(Long id) {
        log.info("Deleting Trainee with ID={}", id);
        dao.delete(id);
        log.info("Trainee with ID={} deleted", id);
    }

    public void update(Long id, Trainee trainee) {
        log.info("Updating Trainee with ID={} to new data: {}", id, trainee);
        dao.update(id, trainee);
        log.info("Trainee with ID={} updated", id);
    }

    @Override
    protected String getEntityName() {
        return "";
    }
}
