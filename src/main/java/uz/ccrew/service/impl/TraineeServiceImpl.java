package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dto.trainee.*;
import uz.ccrew.entity.*;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.service.TraineeService;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final UserDAO userDAO;
    private final UserUtils userUtils;
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;

    @Override
    @Transactional
    public UserCredentials create(TraineeCreateDTO dto) {
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
    public List<TraineeTrainingDTO> getTraineeTrainings(String username, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingTypeName) {
        List<Training> trainings = trainingDAO.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingTypeName);
        return trainings.stream().map(training -> {
            Trainer trainer = training.getTrainer();
            TrainingType trainingType = training.getTrainingType();
            return TraineeTrainingDTO.builder()
                    .trainingName(training.getTrainingName())
                    .trainingDate(training.getTrainingDate())
                    .trainingType(trainingType.getTrainingTypeName())
                    .trainingDuration(training.getTrainingDuration())
                    .trainerName(trainer.getUser().getFirstName())
                    .build();
        }).toList();
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee by username={}", username);
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Trainee with username={} not found", username);
                    return new EntityNotFoundException("Trainee with username=" + username + " not found");
                });
        traineeDAO.delete(trainee.getId());
        log.info("Trainee with username={} deleted successfully", username);
    }

    @Override
    @Transactional
    public void activateDeactivate(String username, Boolean isActive) {
        log.info("Activating/deactivating trainee={}", username);
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            if (user.get().getIsActive().equals(isActive)) {
                log.warn("Trainee with ID={} is already in the desired state (isActive={})", username, isActive);
                return;
            }
        }
        userDAO.activateDeactivate(username, isActive);
        log.info("Trainee with ID={} is now isActive={}", username, isActive);
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

        log.info("Found a profile for username={}", username);
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
