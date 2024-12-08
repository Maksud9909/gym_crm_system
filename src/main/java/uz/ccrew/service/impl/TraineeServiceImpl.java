package uz.ccrew.service.impl;

import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.TraineeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {
    private TraineeDAO dao;

    public TraineeServiceImpl() {
        log.info("TraineeServiceImpl initialized");
    }

    @Autowired
    public void setDao(TraineeDAO dao) {
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
    public void delete(Long id) {
        log.info("Deleting Trainee with ID={}", id);
        dao.delete(id);
        log.info("Trainee with ID={} deleted", id);
    }

    @Override
    public void update(Long id, Trainee trainee) {
        log.info("Updating Trainee with ID={} to new data: {}", id, trainee);
        dao.update(id, trainee);
        log.info("Trainee with ID={} updated", id);
    }

    @Override
    public Trainee findById(Long id) {
        log.info("Finding Trainee by ID={}", id);
        Trainee trainee = dao.findById(id);
        if (trainee != null) {
            log.info("Found Trainee: {}", trainee);
        } else {
            log.warn("Trainee with ID={} not found", id);
        }
        return trainee;
    }

    @Override
    public List<Trainee> findAll() {
        log.info("Fetching all Trainees");
        List<Trainee> trainees = dao.findAll();
        log.info("Fetched {} Trainees", trainees.size());
        return trainees;
    }
}
