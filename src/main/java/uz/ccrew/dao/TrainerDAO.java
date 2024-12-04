package uz.ccrew.dao;

import uz.ccrew.entity.Trainer;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainerDAO {
    private Map<Long, Trainer> trainerStorage;
    private Set<String> existingUsernames = new HashSet<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);

    public TrainerDAO() {
        logger.info("TrainerDAO initialized");
    }

    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
        logger.info("Trainer storage injected into TrainerDAO");
    }

    @Autowired
    public void setExistingUsernames(Set<String> existingUsernames) {
        this.existingUsernames = existingUsernames;
    }

    public Long create(Trainer trainer) {
        String username = generateUniqueUsername(trainer.getFirstName(), trainer.getLastName(), existingUsernames);
        trainer.setUsername(username);
        trainer.setPassword(generateRandomPassword());

        Long id = idCounter.getAndIncrement();
        trainer.setId(id);
        trainerStorage.put(id, trainer);
        logger.info("Created Trainer: ID={}, Trainer={}", id, trainer);
        return id;
    }

    public Trainer findById(Long id) {
        Trainer trainer = trainerStorage.get(id);
        if (trainer != null) {
            logger.info("Found Trainer by ID={}: {}", id, trainer);
        } else {
            logger.warn("Trainer not found for ID={}", id);
        }
        return trainer;
    }

    public void update(Long id, Trainer trainer) {
        trainerStorage.put(id, trainer);
        logger.info("Updated Trainer: ID={}, Trainer={}", id, trainer);
    }

    public void delete(Long id) {
        Trainer trainer = trainerStorage.remove(id);
        if (trainer != null) {
            existingUsernames.remove(trainer.getUsername());
            logger.info("Deleted Trainer: ID={}, Trainer={}", id, trainer);
        } else {
            logger.warn("Failed to delete Trainer: ID={} not found", id);
        }
    }

    public List<Trainer> findAll() {
        logger.info("Fetching all Trainers");
        return new ArrayList<>(trainerStorage.values());
    }
}
