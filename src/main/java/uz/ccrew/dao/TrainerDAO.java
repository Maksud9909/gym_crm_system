package uz.ccrew.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import uz.ccrew.entity.Trainer;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainerDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);

    private final Map<Long, Trainer> trainerStorage;
    private final AtomicLong idCounter = new AtomicLong(1);
    private final Set<String> existingUsernames = new HashSet<>();

    public TrainerDAO(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
        logger.info("TrainerDAO initialized");
    }

    public Long create(Trainer trainer) {
        String username = generateUniqueUsername(trainer.getFirstName(), trainer.getLastName());
        trainer.setUsername(username);
        trainer.setPassword(generateRandomPassword());

        Long id = idCounter.getAndIncrement();
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

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String uniqueUsername = baseUsername;
        int counter = 1;

        while (existingUsernames.contains(uniqueUsername)) {
            uniqueUsername = baseUsername + "." + counter;
            counter++;
        }

        existingUsernames.add(uniqueUsername);
        logger.debug("Generated unique username: {}", uniqueUsername);
        return uniqueUsername;
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            password.append(chars.charAt(randomIndex));
        }
        logger.debug("Generated random password");
        return password.toString();
    }
}
