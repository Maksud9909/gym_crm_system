package uz.ccrew.service.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.service.TrainerService;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {
    private final UserDAO userDAO;
    private final UserUtils userUtils;
    private final TrainerDAO trainerDAO;
    private final TrainingTypeDAO trainingTypeDAO;

    @Autowired
    public TrainerServiceImpl(UserDAO userDAO, TrainerDAO trainerDAO, UserUtils userUtils, TrainingTypeDAO trainingTypeDAO) {
        this.userDAO = userDAO;
        this.trainerDAO = trainerDAO;
        this.userUtils = userUtils;
        this.trainingTypeDAO = trainingTypeDAO;
        log.debug("TrainerService initialized");
    }

    @Override
    @Transactional
    public Long create(Trainer trainer) {
        if (trainer == null) {
            log.warn("Cannot create a null trainer");
            throw new IllegalArgumentException("Trainer cannot be null");
        }
        log.info("Creating trainer: {}", trainer);
        String username = userUtils.generateUniqueUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName());
        String password = userUtils.generateRandomPassword();
        User user = User.builder()
                .username(username)
                .password(password)
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(Boolean.TRUE)
                .build();
        userDAO.create(user);
        trainer.setUser(user);
        Optional<TrainingType> trainingType = trainingTypeDAO.findById(trainer.getTrainingType().getId());
        trainingType.ifPresent(trainer::setTrainingType);
        trainerDAO.create(trainer);
        log.info("Trainer created: {}", trainer);
        return trainer.getId();
    }

    @Override
    @Transactional
    public void update(Trainer trainer) {
        if (trainer == null || trainer.getId() == null) {
            log.error("Trainer or Trainer ID is null");
            throw new IllegalArgumentException("Trainer and Trainer ID must not be null");
        }

        Trainer existingTrainer = trainerDAO.findById(trainer.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer with id=" + trainer.getId() + " not found"));

        User existingUser = existingTrainer.getUser();
        if (trainer.getUser() != null) {
            if (trainer.getUser().getFirstName() != null) {
                existingUser.setFirstName(trainer.getUser().getFirstName());
            }
            if (trainer.getUser().getLastName() != null) {
                existingUser.setLastName(trainer.getUser().getLastName());
            }
            if (trainer.getUser().getUsername() != null) {
                existingUser.setUsername(trainer.getUser().getUsername());
            }
            if (trainer.getUser().getPassword() != null) {
                existingUser.setPassword(trainer.getUser().getPassword());
            }
        }

        if (trainer.getTrainingType() != null) {
            TrainingType trainingType = trainingTypeDAO.findById(trainer.getTrainingType().getId())
                    .orElseThrow(() -> new EntityNotFoundException("TrainingType with id=" + trainer.getTrainingType().getId() + " not found"));
            existingTrainer.setTrainingType(trainingType);
        }

        trainerDAO.update(existingTrainer);
        log.info("Updated trainer with ID={}", trainer.getId());
    }


    @Override
    @Transactional(readOnly = true)
    public Trainer findById(Long id) {
        log.info("Fetching trainer for id={}", id);
        return trainerDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Trainer with id=" + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer findByUsername(String username) {
        log.info("Fetching trainer for username={}", username);
        Trainer trainer = trainerDAO.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainer with username={} not found", username);
                    return new EntityNotFoundException(username);
                });
        log.info("Found trainer: {}", trainer);
        return trainer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAll() {
        log.info("Fetching all trainers");
        return trainerDAO.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting trainer={}", id);
        trainerDAO.delete(id);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        log.info("Changing password for trainer={}", id);
        trainerDAO.changePassword(id, newPassword);
    }

    @Override
    @Transactional
    public void activateDeactivate(Long id, Boolean isActive) {
        log.info("Activating/deactivating trainer={}", id);
        User user = userDAO.findById(id);
        if (user.getIsActive().equals(isActive)) {
            log.warn("Trainer with ID={} is already in the desired state (isActive={})", id, isActive);
            return;
        }
        trainerDAO.activateDeactivate(id, isActive);
        log.info("Trainer with ID={} is now isActive={}", id, isActive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for Trainee username={}", traineeUsername);
        return trainerDAO.getUnassignedTrainers(traineeUsername);
    }
}
