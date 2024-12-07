package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.TrainerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerDAO dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);

    public TrainerServiceImpl() {
        LOGGER.info("TrainerServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainerDAO dao) {
        this.dao = dao;
        LOGGER.info("TrainerDAO injected into TrainerServiceImpl");
    }

    @Override
    public Long create(Trainer trainer) {
        LOGGER.info("Creating Trainer: {}", trainer);
        Long id = dao.create(trainer);
        LOGGER.info("Trainer created with ID={}", id);
        return id;
    }

    @Override
    public void update(Long id, Trainer trainer) {
        LOGGER.info("Updating Trainer with ID={} to new data: {}", id, trainer);
        dao.update(id, trainer);
        LOGGER.info("Trainer with ID={} updated", id);
    }

    @Override
    public Trainer findById(Long id) {
        LOGGER.info("Finding Trainer by ID={}", id);
        Trainer trainer = dao.findById(id);
        if (trainer != null) {
            LOGGER.info("Found Trainer: {}", trainer);
        } else {
            LOGGER.warn("Trainer with ID={} not found", id);
        }
        return trainer;
    }

    @Override
    public List<Trainer> findAll() {
        LOGGER.info("Fetching all Trainers");
        List<Trainer> trainers = dao.findAll();
        LOGGER.info("Fetched {} Trainers", trainers.size());
        return trainers;
    }
}
