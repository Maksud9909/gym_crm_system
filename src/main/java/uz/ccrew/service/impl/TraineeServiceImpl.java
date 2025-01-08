package uz.ccrew.service.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.service.TraineeService;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {
    private final UserDAO userDAO;
    private final UserUtils userUtils;
    private final TraineeDAO traineeDAO;

    @Autowired
    public TraineeServiceImpl(TraineeDAO traineeDAO, UserUtils userUtils, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.userUtils = userUtils;
        this.traineeDAO = traineeDAO;
        log.debug("TraineeService initialized");
    }

    @Override
    @Transactional
    public Long create(Trainee trainee) {
        if (trainee == null || trainee.getUser() == null) {
            log.warn("Cannot create Trainee without User");
            throw new IllegalArgumentException("Trainee and associated User cannot be null");
        }
        String username = userUtils.generateUniqueUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName());
        String password = userUtils.generateRandomPassword();
        User user = User.builder()
                .username(username)
                .password(password)
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .isActive(Boolean.TRUE)
                .build();
        userDAO.create(user);
        trainee.setUser(user);

        Long id = traineeDAO.create(trainee);
        log.info("Created Trainee with ID={}", id);
        return id;
    }

    @Override
    @Transactional
    public void update(Trainee trainee) {
        if (trainee == null || trainee.getId() == null) {
            log.error("Trainee or Trainee ID is null");
            throw new IllegalArgumentException("Trainee and Trainee ID must not be null");
        }

        Trainee existingTrainee = traineeDAO.findById(trainee.getId())
                .orElseThrow(() -> {
                    log.warn("Trainee with id={} not found", trainee.getId());
                    return new EntityNotFoundException("Trainee with id=" + trainee.getId() + " not found");
                });

        User existingUser = existingTrainee.getUser();
        if (existingUser == null) {
            log.error("Associated User not found for Trainee with id {}", trainee.getId());
            throw new EntityNotFoundException("Associated User not found");
        }

        if (trainee.getUser() != null) {
            if (trainee.getUser().getFirstName() != null) {
                existingUser.setFirstName(trainee.getUser().getFirstName());
            }
            if (trainee.getUser().getLastName() != null) {
                existingUser.setLastName(trainee.getUser().getLastName());
            }
            if (trainee.getUser().getUsername() != null) {
                existingUser.setUsername(trainee.getUser().getUsername());
            }
            if (trainee.getUser().getPassword() != null) {
                existingUser.setPassword(trainee.getUser().getPassword());
            }
            if (trainee.getUser().getIsActive() != null) {
                existingUser.setIsActive(trainee.getUser().getIsActive());
            }
        }

        if (trainee.getDateOfBirth() != null) {
            existingTrainee.setDateOfBirth(trainee.getDateOfBirth());
        }
        if (trainee.getAddress() != null) {
            existingTrainee.setAddress(trainee.getAddress());
        }

        userDAO.update(existingUser);
        traineeDAO.update(existingTrainee);

        log.info("Updated trainee with ID={}", trainee.getId());
    }


    @Override
    @Transactional(readOnly = true)
    public Trainee findById(Long id) {
        log.info("Fetching trainee for id={}", id);
        return traineeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Trainee with id=" + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee findByUsername(String username) {
        log.info("Fetching trainee for username={}", username);
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainee with username={} not found", username);
                    return new EntityNotFoundException(username);
                });
        log.info("Found trainee: {}", trainee);
        return trainee;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> findAll() {
        log.info("Fetching all trainees");
        return traineeDAO.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting trainee={}", id);
        traineeDAO.delete(id);
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee by username={}", username);
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainee with username={} not found", username);
                    return new EntityNotFoundException("Trainee with username=" + username + " not found");
                });
        traineeDAO.delete(trainee.getId());
        log.info("Trainee with username={} deleted successfully", username);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        log.info("Changing password for trainee={}", id);
        traineeDAO.changePassword(id, newPassword);
    }

    @Override
    @Transactional
    public void activateDeactivate(Long id, Boolean isActive) {
        log.info("Activating/deactivating trainee={}", id);
        User user = userDAO.findById(id);
        if (user.getIsActive().equals(isActive)) {
            log.warn("Trainee with ID={} is already in the desired state (isActive={})", id, isActive);
            return;
        }
        traineeDAO.activateDeactivate(id, isActive);
        log.info("Trainee with ID={} is now isActive={}", id, isActive);
    }

    @Override
    @Transactional
    public void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds) {
        log.info("Updating trainers for Trainee ID={}", traineeId);

        if (traineeId == null) {
            log.warn("Trainee ID is null");
            throw new IllegalArgumentException("Trainee ID must not be null");
        }

        if (newTrainerIds == null || newTrainerIds.isEmpty()) {
            log.warn("No trainers provided for update");
            throw new IllegalArgumentException("Trainer IDs must not be null or empty");
        }

        traineeDAO.updateTraineeTrainers(traineeId, newTrainerIds);
        log.info("Trainers updated successfully for Trainee ID={}", traineeId);
    }
}
