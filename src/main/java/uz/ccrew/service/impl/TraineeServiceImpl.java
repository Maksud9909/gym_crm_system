package uz.ccrew.service.impl;

import lombok.RequiredArgsConstructor;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.trainee.TraineeProfile;
import uz.ccrew.dto.trainee.TraineeProfileUsername;
import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.dto.trainingType.TrainingTypeDTO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.service.AuthService;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.service.TraineeService;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final UserDAO userDAO;
    private final UserUtils userUtils;
    private final TraineeDAO traineeDAO;
    private final AuthService authService;

    @Override
    @Transactional
    public UserCredentials create(TraineeCreateDTO dto) {
        if (dto == null) {
            log.error("Cannot create null Trainee");
            throw new IllegalArgumentException("Trainee cannot be null");
        }

        String username = userUtils.generateUniqueUsername(dto.getFirstName(), dto.getLastName());
        String password = userUtils.generateRandomPassword();

        User newUser = User.builder()
                .username(username)
                .password(password)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .isActive(Boolean.TRUE)
                .build();

        Trainee trainee = Trainee.builder()
                .address(dto.getAddress())
                .dateOfBirth(dto.getDatOfBirth())
                .user(newUser)
                .build();

        traineeDAO.create(trainee);
        log.info("Trainee created: {}", dto);
        return UserCredentials.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Override
    @Transactional
    public TraineeProfileUsername update(TraineeUpdateDTO dto) {
        Trainee trainee = traineeDAO.findByUsername(dto.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("Trainee with username=" + dto.getUsername() + " not found"));

        trainee.getUser().setFirstName(dto.getFirstName());
        trainee.getUser().setLastName(dto.getLastName());
        trainee.getUser().setIsActive(dto.getIsActive());
        trainee.setAddress(dto.getAddress());
        trainee.setDateOfBirth(dto.getDatOfBirth());

        traineeDAO.update(trainee);

        List<TrainerDTO> trainerDTOS = trainee.getTraining().stream()
                .map(training -> {
                    Trainer trainer = training.getTrainer();
                    return TrainerDTO.builder()
                            .username(trainer.getUser().getUsername())
                            .firstName(trainer.getUser().getFirstName())
                            .lastName(trainer.getUser().getLastName())
                            .trainingTypeDTO(TrainingTypeDTO.builder()
                                    .trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
                                    .build())
                            .build();
                })
                .toList();

        return TraineeProfileUsername.builder()
                .username(trainee.getUser().getUsername())
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .datOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().getIsActive())
                .trainerDTOS(trainerDTOS)
                .build();
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
    public void update(Trainee trainee, UserCredentials userCredentials) {

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

    @Override
    @Transactional(readOnly = true)
    public TraineeProfile getProfile(String username) {
        Trainee trainee = traineeDAO.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Trainee with username=" + username + " not found"));
        List<TrainerDTO> trainerDTOS = trainee.getTraining().stream()
                .map(training -> {
                    Trainer trainer = training.getTrainer();
                    return TrainerDTO.builder()
                            .username(trainer.getUser().getUsername())
                            .firstName(trainer.getUser().getFirstName())
                            .lastName(trainer.getUser().getLastName())
                            .trainingTypeDTO(TrainingTypeDTO.builder()
                                    .trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
                                    .build())
                            .build();
                })
                .toList();

        return TraineeProfile.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .datOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().getIsActive())
                .trainerDTOS(trainerDTOS)
                .build();
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
