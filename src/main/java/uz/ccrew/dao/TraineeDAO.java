package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TraineeDAO {
    private Map<Long, Trainee> traineeStorage;
    private final AtomicLong idCounter = new AtomicLong(1);
    private Set<String> existingUsernames = new HashSet<>();
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);

    public TraineeDAO() {
        logger.info("TraineeDAO initialized");
    }

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
        logger.info("Trainee storage injected into TraineeDAO");
    }

    @Autowired
    public void setExistingUsernames(Set<String> existingUsernames) {
        this.existingUsernames = existingUsernames;
    }


    public Long create(Trainee trainee) {
        String username = generateUniqueUsername(trainee.getFirstName(), trainee.getLastName(), existingUsernames);
        trainee.setUsername(username);
        trainee.setPassword(generateRandomPassword());

        Long id = idCounter.getAndIncrement();
        traineeStorage.put(id, trainee);
        logger.info("Created Trainee: ID={}, Trainee={}", id, trainee);
        return id;
    }

    public Trainee findById(Long id) {
        Trainee trainee = traineeStorage.get(id);
        if (trainee != null) {
            logger.info("Found Trainee by ID={}: {}", id, trainee);
        } else {
            logger.warn("Trainee not found for ID={}", id);
        }
        return trainee;
    }

    public void update(Long id, Trainee trainee) {
        traineeStorage.put(id, trainee);
        logger.info("Updated Trainee: ID={}, Trainee={}", id, trainee);
    }

    public void delete(Long id) {
        Trainee trainee = traineeStorage.remove(id);
        if (trainee != null) {
            logger.info("Deleted Trainee: ID={}, Trainee={}", id, trainee);
        } else {
            logger.warn("Failed to delete Trainee: ID={} not found", id);
        }
    }

    public List<Trainee> findAll() {
        logger.info("Fetching all Trainees");
        return new ArrayList<>(traineeStorage.values());
    }
}
