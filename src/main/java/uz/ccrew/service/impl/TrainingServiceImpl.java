package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.service.TrainingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private TrainingDAO dao;
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    public TrainingServiceImpl() {
        logger.info("TrainingServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainingDAO dao) {
        this.dao = dao;
        logger.info("TrainingDAO injected into TrainingServiceImpl");
    }

    @Override
    public Long create(Training training) {
        logger.info("Creating Training: {}", training);
        Long id = dao.create(training);
        logger.info("Training created with ID={}", id);
        return id;
    }

    @Override
    public Training findById(Long id) {
        logger.info("Finding Training by ID={}", id);
        Training training = dao.findById(id);
        if (training != null) {
            logger.info("Found Training: {}", training);
        } else {
            logger.warn("Training with ID={} not found", id);
        }
        return training;
    }

    @Override
    public List<Training> findAll() {
        logger.info("Fetching all Trainings");
        List<Training> trainings = dao.findAll();
        logger.info("Fetched {} Trainings", trainings.size());
        return trainings;
    }
}
