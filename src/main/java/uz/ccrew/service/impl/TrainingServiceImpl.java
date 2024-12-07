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
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

    public TrainingServiceImpl() {
        LOGGER.info("TrainingServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainingDAO dao) {
        this.dao = dao;
        LOGGER.info("TrainingDAO injected into TrainingServiceImpl");
    }

    @Override
    public Long create(Training training) {
        LOGGER.info("Creating Training: {}", training);
        Long id = dao.create(training);
        LOGGER.info("Training created with ID={}", id);
        return id;
    }

    @Override
    public Training findById(Long id) {
        LOGGER.info("Finding Training by ID={}", id);
        Training training = dao.findById(id);
        if (training != null) {
            LOGGER.info("Found Training: {}", training);
        } else {
            LOGGER.warn("Training with ID={} not found", id);
        }
        return training;
    }

    @Override
    public List<Training> findAll() {
        LOGGER.info("Fetching all Trainings");
        List<Training> trainings = dao.findAll();
        LOGGER.info("Fetched {} Trainings", trainings.size());
        return trainings;
    }
}
