package uz.ccrew.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.TrainerService;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerDAO dao;
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    public TrainerServiceImpl() {
        logger.info("TrainerServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainerDAO dao) {
        this.dao = dao;
        logger.info("TrainerDAO injected into TrainerServiceImpl");
    }

    @Override
    public Long create(Trainer trainer) {
        logger.info("Creating Trainer: {}", trainer);
        Long id = dao.create(trainer);
        logger.info("Trainer created with ID={}", id);
        return id;
    }

    @Override
    public void update(Long id, Trainer trainer) {
        logger.info("Updating Trainer with ID={} to new data: {}", id, trainer);
        dao.update(id, trainer);
        logger.info("Trainer with ID={} updated", id);
    }

    @Override
    public Trainer findById(Long id) {
        logger.info("Finding Trainer by ID={}", id);
        Trainer trainer = dao.findById(id);
        if (trainer != null) {
            logger.info("Found Trainer: {}", trainer);
        } else {
            logger.warn("Trainer with ID={} not found", id);
        }
        return trainer;
    }

    @Override
    public List<Trainer> findAll() {
        logger.info("Fetching all Trainers");
        List<Trainer> trainers = dao.findAll();
        logger.info("Fetched {} Trainers", trainers.size());
        return trainers;
    }
}
