package uz.ccrew.service.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.service.AuthService;
import uz.ccrew.dto.UserCredentials;
import uz.ccrew.service.TraineeService;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {
    private final UserDAO userDAO;
    private final UserUtils userUtils;
    private final TraineeDAO traineeDAO;
    private final AuthService authService;

    @Autowired
    public TraineeServiceImpl(TraineeDAO traineeDAO, UserUtils userUtils, UserDAO userDAO, AuthService authService) {
        this.userDAO = userDAO;
        this.userUtils = userUtils;
        this.traineeDAO = traineeDAO;
        this.authService = authService;
        log.debug("TraineeService initialized");
    }

    @Override
    @Transactional
    public Long create(Trainee trainee) {
        if (trainee == null) {
            log.error("Cannot create null Trainee");
            throw new IllegalArgumentException("Trainee cannot be null");
        }
        if (trainee.getUser() == null) {
            log.error("Cannot create Trainee without User");
            throw new IllegalArgumentException("Associated User cannot be null");
        }

        User user = trainee.getUser();
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            log.error("User firstName is required");
            throw new IllegalArgumentException("User firstName cannot be empty");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            log.error("User lastName is required");
            throw new IllegalArgumentException("User lastName cannot be empty");
        }

        String username = userUtils.generateUniqueUsername(user.getFirstName(), user.getLastName());
        String password = userUtils.generateRandomPassword();

        User newUser = User.builder()
                .username(username)
                .password(password)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isActive(Boolean.TRUE)
                .build();

        userDAO.create(newUser);

        trainee.setUser(newUser);

        Long id = traineeDAO.create(trainee);
        log.info("Trainee created: {}", trainee);
        return id;
    }

    @Override
    @Transactional
    public void update(Trainee trainee, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);

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
            User newUserData = trainee.getUser();

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

        if (trainee.getDateOfBirth() != null) {
            if (trainee.getDateOfBirth().isAfter(LocalDate.now())) {
                log.error("Date of birth cannot be in the future");
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }
            existingTrainee.setDateOfBirth(trainee.getDateOfBirth());
        }

        if (trainee.getAddress() != null) {
            existingTrainee.setAddress(trainee.getAddress().trim());
        }

        userDAO.update(existingUser);
        traineeDAO.update(existingTrainee);

        log.info("Updated trainee with ID={} and associated user with ID={}",
                trainee.getId(), existingUser.getId());
    }


    @Override
    @Transactional(readOnly = true)
    public Trainee findById(Long id, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Fetching trainee for id={}", id);
        return traineeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Trainee with id=" + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee findByUsername(String username, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
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
    public List<Trainee> findAll(UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Fetching all trainees");
        return traineeDAO.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Deleting trainee={}", id);
        traineeDAO.delete(id);
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
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
    public void changePassword(Long id, String newPassword, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Changing password for trainee={}", id);
        traineeDAO.changePassword(id, newPassword);
    }

    @Override
    @Transactional
    public void activateDeactivate(Long id, Boolean isActive, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
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
    public void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
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
