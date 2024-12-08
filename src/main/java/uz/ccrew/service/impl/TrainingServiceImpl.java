package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.service.TrainingService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {
    private TrainingDAO dao;

    public TrainingServiceImpl() {
        log.info("TrainingServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainingDAO dao) {
        this.dao = dao;
        log.info("TrainingDAO injected into TrainingServiceImpl");
    }

    @Override
    public Long create(Training training) {
        log.info("Creating Training: {}", training);
        Long id = dao.create(training);
        log.info("Training created with ID={}", id);
        return id;
    }

    @Override
    public Training findById(Long id) {
        log.info("Finding Training by ID={}", id);
        Training training = dao.findById(id);
        if (training != null) {
            log.info("Found Training: {}", training);
        } else {
            log.warn("Training with ID={} not found", id);
        }
        return training;
    }

    @Override
    public List<Training> findAll() {
        log.info("Fetching all Trainings");
        List<Training> trainings = dao.findAll();
        log.info("Fetched {} Trainings", trainings.size());
        return trainings;
    }
}
