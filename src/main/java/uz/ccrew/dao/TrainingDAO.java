package uz.ccrew.dao;

import uz.ccrew.entity.Training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainingDAO {
    private Map<Long, Training> trainingStorage;
    private final AtomicLong idCounter = new AtomicLong(1);
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDAO.class);

    public TrainingDAO() {
        LOGGER.info("TrainingDAO initialized");
    }

    @Autowired
    public void setTrainingStorage(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
        LOGGER.info("Training storage injected into TrainingDAO");
    }

    public Long create(Training training) {
        Long id = idCounter.getAndIncrement();
        training.setId(id);
        trainingStorage.put(id, training);
        LOGGER.info("Created Training: ID={}, Training={}", id, training);
        return id;
    }

    public Training findById(Long id) {
        Training training = trainingStorage.get(id);
        if (training != null) {
            LOGGER.info("Found Training by ID={}: {}", id, training);
        } else {
            LOGGER.warn("Training not found for ID={}", id);
        }
        return training;
    }

    public List<Training> findAll() {
        LOGGER.info("Fetching all Trainings");
        return new ArrayList<>(trainingStorage.values());
    }
}
