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
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    public TraineeServiceImpl() {
        logger.info("TraineeServiceImpl initialized");
    }

    @Autowired
    public void setDao(TraineeDAO dao) {
        this.dao = dao;
        logger.info("TraineeDAO injected into TraineeServiceImpl");
    }

    @Override
    public Long create(Trainee trainee) {
        logger.info("Creating Trainee: {}", trainee);
        Long id = dao.create(trainee);
        logger.info("Trainee created with ID={}", id);
        return id;
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting Trainee with ID={}", id);
        dao.delete(id);
        logger.info("Trainee with ID={} deleted", id);
    }

    @Override
    public void update(Long id, Trainee trainee) {
        logger.info("Updating Trainee with ID={} to new data: {}", id, trainee);
        dao.update(id, trainee);
        logger.info("Trainee with ID={} updated", id);
    }

    @Override
    public Trainee findById(Long id) {
        logger.info("Finding Trainee by ID={}", id);
        Trainee trainee = dao.findById(id);
        if (trainee != null) {
            logger.info("Found Trainee: {}", trainee);
        } else {
            logger.warn("Trainee with ID={} not found", id);
        }
        return trainee;
    }

    @Override
    public List<Trainee> findAll() {
        logger.info("Fetching all Trainees");
        List<Trainee> trainees = dao.findAll();
        logger.info("Fetched {} Trainees", trainees.size());
        return trainees;
    }
}
