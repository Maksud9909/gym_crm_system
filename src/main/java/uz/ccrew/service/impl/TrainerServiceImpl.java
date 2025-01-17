package uz.ccrew.service.impl;

import lombok.RequiredArgsConstructor;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.User;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.AuthService;
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
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final UserDAO userDAO;
    private final UserUtils userUtils;
    private final TrainerDAO trainerDAO;
    private final TrainingTypeDAO trainingTypeDAO;
    private final AuthService authService;

//    @Override
    @Transactional
    public Long create(Trainer trainer) {
        if (trainer == null) {
            log.error("Cannot create a null trainer");
            throw new IllegalArgumentException("Trainer cannot be null");
        }

        if (trainer.getUser() == null) {
            log.error("Cannot create Trainer without User");
            throw new IllegalArgumentException("Associated User cannot be null");
        }

        User user = trainer.getUser();
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            log.error("User firstName is required");
            throw new IllegalArgumentException("User firstName cannot be empty");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            log.error("User lastName is required");
            throw new IllegalArgumentException("User lastName cannot be empty");
        }

        String username = userUtils.generateUniqueUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName());
        String password = userUtils.generateRandomPassword();

        User newUser = User.builder()
                .username(username)
                .password(password)
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(Boolean.TRUE)
                .build();

        userDAO.create(newUser);

        trainer.setUser(newUser);
        Optional<TrainingType> trainingType = trainingTypeDAO.findById(trainer.getTrainingType().getId());
        trainingType.ifPresent(trainer::setTrainingType);
        Long id = trainerDAO.create(trainer);
        log.info("Trainer created: {}", trainer);
        return id;
    }

    @Override
    @Transactional
    public void update(Trainer trainer, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);

        if (trainer == null || trainer.getId() == null) {
            log.error("Trainer or Trainer ID is null");
            throw new IllegalArgumentException("Trainer and Trainer ID must not be null");
        }

        Trainer existingTrainer = trainerDAO.findById(trainer.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer with id=" + trainer.getId() + " not found"));

        User existingUser = existingTrainer.getUser();
        if (existingUser == null) {
            log.error("Associated User not found for Trainer with id {}", trainer.getId());
            throw new IllegalArgumentException("User with id=" + trainer.getId() + " not found");
        }


        if (trainer.getUser() != null) {
            validateAndUpdateUser(existingUser, trainer.getUser());
        }

        if (trainer.getTrainingType() != null) {
            existingTrainer.setTrainingType(trainer.getTrainingType());
        }
        trainerDAO.update(existingTrainer);
        log.info("Updated trainer with ID={}", trainer.getId());
    }


    @Override
    public Object create(Object entity) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer findById(Long id, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Fetching trainer for id={}", id);
        return trainerDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Trainer with id=" + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer findByUsername(String username, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
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
    public List<Trainer> findAll(UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Fetching all trainers");
        return trainerDAO.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Deleting trainer={}", id);
        trainerDAO.delete(id);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Changing password for trainer={}", id);
        trainerDAO.changePassword(id, newPassword);
    }

    @Override
    @Transactional
    public void activateDeactivate(Long id, Boolean isActive, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
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
    public List<Trainer> getUnassignedTrainers(String traineeUsername, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Fetching unassigned trainers for Trainee username={}", traineeUsername);
        return trainerDAO.getUnassignedTrainers(traineeUsername);
    }

    private void validateAndUpdateUser(User existingUser, User newUserData) {
        if (newUserData.getFirstName() != null) {
            if (newUserData.getFirstName().trim().isEmpty()) {
                log.error("First name cannot be empty");
                throw new IllegalArgumentException("First name cannot be empty");
            }
            existingUser.setFirstName(newUserData.getFirstName().trim());
        }

        if (newUserData.getLastName() != null) {
            if (newUserData.getLastName().trim().isEmpty()) {
                log.error("Last name cannot be empty");
                throw new IllegalArgumentException("Last name cannot be empty");
            }
            existingUser.setLastName(newUserData.getLastName().trim());
        }

        if (newUserData.getUsername() != null) {
            if (newUserData.getUsername().trim().isEmpty()) {
                log.error("Username cannot be empty");
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (!existingUser.getUsername().equals(newUserData.getUsername().trim()) &&
                userDAO.isUsernameExists(newUserData.getUsername().trim())) {
                log.error("Username {} already exists", newUserData.getUsername());
                throw new IllegalArgumentException("Username already exists");
            }
            existingUser.setUsername(newUserData.getUsername().trim());
        }

        if (newUserData.getPassword() != null) {
            if (newUserData.getPassword().trim().isEmpty()) {
                log.error("Password cannot be empty");
                throw new IllegalArgumentException("Password cannot be empty");
            }
            existingUser.setPassword(newUserData.getPassword());
        }

        if (newUserData.getIsActive() != null) {
            existingUser.setIsActive(newUserData.getIsActive());
        }
    }
}
