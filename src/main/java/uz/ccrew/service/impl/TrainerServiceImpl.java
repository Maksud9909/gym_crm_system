package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.TrainerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerDAO dao;

    public TrainerServiceImpl() {
        log.info("TrainerServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainerDAO dao) {
        this.dao = dao;
        log.info("TrainerDAO injected into TrainerServiceImpl");
    }

    @Override
    public Long create(Trainer trainer) {
        log.info("Creating Trainer: {}", trainer);
        Long id = dao.create(trainer);
        log.info("Trainer created with ID={}", id);
        return id;
    }

    @Override
    public void update(Long id, Trainer trainer) {
        log.info("Updating Trainer with ID={} to new data: {}", id, trainer);
        dao.update(id, trainer);
        log.info("Trainer with ID={} updated", id);
    }

    @Override
    public Trainer findById(Long id) {
        log.info("Finding Trainer by ID={}", id);
        Trainer trainer = dao.findById(id);
        if (trainer != null) {
            log.info("Found Trainer: {}", trainer);
        } else {
            log.warn("Trainer with ID={} not found", id);
        }
        return trainer;
    }

    @Override
    public List<Trainer> findAll() {
        log.info("Fetching all Trainers");
        List<Trainer> trainers = dao.findAll();
        log.info("Fetched {} Trainers", trainers.size());
        return trainers;
    }
}
