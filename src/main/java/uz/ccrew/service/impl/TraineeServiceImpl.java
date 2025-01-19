package uz.ccrew.service.impl;

import uz.ccrew.entity.User;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.service.AuthService;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.service.TraineeService;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.dto.trainee.TraineeProfileDTO;
import uz.ccrew.dto.trainee.TraineeProfileUsernameDTO;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
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
    private final TrainerDAO trainerDAO;

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
    public TraineeProfileUsernameDTO update(TraineeUpdateDTO dto) {
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
                            .trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
                            .build();
                })
                .toList();

        return TraineeProfileUsernameDTO.builder()
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
    public void changePassword(Long id, String newPassword, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
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
    public List<TrainerDTO> updateTraineeTrainers(String username, List<String> newTrainers) {
        log.info("Updating trainers for Trainee ID={}", username);

        if (username == null) {
            log.warn("Trainee ID is null");
            throw new IllegalArgumentException("Trainee ID must not be null");
        }

        if (newTrainers == null || newTrainers.isEmpty()) {
            log.warn("No trainers provided for update");
            throw new IllegalArgumentException("Trainer IDs must not be null or empty");
        }

        traineeDAO.updateTraineeTrainers(username, newTrainers);
        log.info("Trainers updated successfully for Trainee ID={}", username);
        List<Trainer> trainers = trainerDAO.findByTrainerUsername(newTrainers);
        return trainers.stream()
                .map(trainer -> TrainerDTO.builder()
                        .username(trainer.getUser().getUsername())
                        .firstName(trainer.getUser().getFirstName())
                        .lastName(trainer.getUser().getLastName())
                        .trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TraineeProfileDTO getProfile(String username) {
        Trainee trainee = traineeDAO.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Trainee with username=" + username + " not found"));
        List<TrainerDTO> trainerDTOS = trainee.getTraining().stream()
                .map(training -> {
                    Trainer trainer = training.getTrainer();
                    return TrainerDTO.builder()
                            .username(trainer.getUser().getUsername())
                            .firstName(trainer.getUser().getFirstName())
                            .lastName(trainer.getUser().getLastName())
                            .trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
                            .build();
                })
                .toList();

        return TraineeProfileDTO.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .datOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().getIsActive())
                .trainerDTOS(trainerDTOS)
                .build();
    }
}
