package uz.ccrew.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import uz.ccrew.entity.Training;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainingDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);

    private final Map<Long, Training> trainingStorage;
    private final AtomicLong idCounter = new AtomicLong(1);

    public TrainingDAO(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
        logger.info("TrainingDAO initialized");
    }

    public Long create(Training training) {
        Long id = idCounter.getAndIncrement();
        trainingStorage.put(id, training);
        logger.info("Created Training: ID={}, Training={}", id, training);
        return id;
    }

    public Training findById(Long id) {
        Training training = trainingStorage.get(id);
        if (training != null) {
            logger.info("Found Training by ID={}: {}", id, training);
        } else {
            logger.warn("Training not found for ID={}", id);
        }
        return training;
    }

    public List<Training> findAll() {
        logger.info("Fetching all Trainings");
        return new ArrayList<>(trainingStorage.values());
    }
}
