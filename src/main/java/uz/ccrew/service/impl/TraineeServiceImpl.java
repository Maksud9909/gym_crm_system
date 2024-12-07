package uz.ccrew.service.impl;

import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.TraineeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {
    private TraineeDAO dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);

    public TraineeServiceImpl() {
        LOGGER.info("TraineeServiceImpl initialized");
    }

    @Autowired
    public void setDao(TraineeDAO dao) {
        this.dao = dao;
        LOGGER.info("TraineeDAO injected into TraineeServiceImpl");
    }

    @Override
    public Long create(Trainee trainee) {
        LOGGER.info("Creating Trainee: {}", trainee);
        Long id = dao.create(trainee);
        LOGGER.info("Trainee created with ID={}", id);
        return id;
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting Trainee with ID={}", id);
        dao.delete(id);
        LOGGER.info("Trainee with ID={} deleted", id);
    }

    @Override
    public void update(Long id, Trainee trainee) {
        LOGGER.info("Updating Trainee with ID={} to new data: {}", id, trainee);
        dao.update(id, trainee);
        LOGGER.info("Trainee with ID={} updated", id);
    }

    @Override
    public Trainee findById(Long id) {
        LOGGER.info("Finding Trainee by ID={}", id);
        Trainee trainee = dao.findById(id);
        if (trainee != null) {
            LOGGER.info("Found Trainee: {}", trainee);
        } else {
            LOGGER.warn("Trainee with ID={} not found", id);
        }
        return trainee;
    }

    @Override
    public List<Trainee> findAll() {
        LOGGER.info("Fetching all Trainees");
        List<Trainee> trainees = dao.findAll();
        LOGGER.info("Fetched {} Trainees", trainees.size());
        return trainees;
    }
}
