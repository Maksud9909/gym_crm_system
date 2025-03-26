package uz.ccrew.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dto.trainee.*;
import uz.ccrew.dto.training.TrainerWorkloadDTO;
import uz.ccrew.dto.training.TrainingTrainerUpdateDTO;
import uz.ccrew.entity.*;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.enums.ActionType;
import uz.ccrew.exp.exp.TrainingNotAssociatedException;
import uz.ccrew.service.TrainerWorkloadClient;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.service.TraineeService;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.exp.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final UserUtils userUtils;
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;
    private final PasswordEncoder passwordEncoder;
    private final TrainerWorkloadClient trainerWorkloadClient;

    @Override
    @Transactional
    public UserCredentials create(TraineeCreateDTO dto) {
        String username = userUtils.generateUniqueUsername(dto.getFirstName(), dto.getLastName());
        String password = userUtils.generateRandomPassword();

        User newUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
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
                .trainers(trainerDTOS)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeTrainingDTO> getTraineeTrainings(String username, LocalDateTime periodFrom, LocalDateTime periodTo, String trainerName, String trainingTypeName) {
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
    @CircuitBreaker(name = "traineeCB", fallbackMethod = "fallbackDeleteTrainingData")
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee by username={}", username);
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Trainee with username={} not found", username);
                    return new EntityNotFoundException("Trainee with username=" + username + " not found");
                });

        List<Training> traineeTrainings = new ArrayList<>(trainee.getTraining());

        if (!traineeTrainings.isEmpty()) {
            for (Training training : traineeTrainings) {
                TrainerWorkloadDTO trainerWorkloadDTO = TrainerWorkloadDTO.builder()
                        .trainerUsername(training.getTrainer().getUser().getUsername())
                        .trainerFirstName(training.getTrainer().getUser().getFirstName())
                        .trainerLastName(training.getTrainer().getUser().getLastName())
                        .isActive(training.getTrainer().getUser().getIsActive())
                        .trainingDuration(training.getTrainingDuration())
                        .trainingDate(training.getTrainingDate())
                        .actionType(ActionType.DELETE)
                        .build();
                trainingDAO.delete(training);
                trainerWorkloadClient.sendTrainingData(trainerWorkloadDTO);
            }
        }
        traineeDAO.delete(trainee);
        log.info("Trainee with username={} deleted successfully", username);
    }

    @Override
    @Transactional
    public List<TrainerDTO> updateTraineeTrainers(UpdateTraineeTrainersDTO trainersDTOList) {
        List<TrainerDTO> updatedTrainers = new ArrayList<>();


        Trainee trainee = traineeDAO.findByUsername(trainersDTOList.getTraineeUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainee with username=" + trainersDTOList.getTraineeUsername() + " not found"));

        for (TrainingTrainerUpdateDTO trainingUpdate : trainersDTOList.getTrainingTrainers()) {
            Training training = trainingDAO.findById(trainingUpdate.getTrainingId())
                    .orElseThrow(() -> new EntityNotFoundException("Training with ID=" + trainingUpdate.getTrainingId() + " not found"));

            if (!trainee.getTraining().contains(training)) {
                throw new TrainingNotAssociatedException("Training with ID=" + trainingUpdate.getTrainingId() + " is not associated with Trainee=" + trainersDTOList.getTraineeUsername());
            }

            Trainer trainer = trainerDAO.findByUsername(trainingUpdate.getTrainerUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Trainer with username=" + trainingUpdate.getTrainerUsername() + " not found"));

            training.setTrainer(trainer);
            training.setTrainingType(trainer.getTrainingType());

            if (trainingUpdate.getTrainingName() != null) {
                training.setTrainingName(trainingUpdate.getTrainingName());
            }

            trainingDAO.update(training);

            updatedTrainers.add(TrainerDTO.builder()
                    .username(trainer.getUser().getUsername())
                    .firstName(trainer.getUser().getFirstName())
                    .lastName(trainer.getUser().getLastName())
                    .trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
                    .build());
        }
        return updatedTrainers;
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
                .trainers(trainerDTOS)
                .build();
    }


    private void fallbackDeleteTrainingData(String username, Throwable ex) {
        log.error("Fallback triggered: trainer-workload-service is unavailable for deleting Training data", ex);
    }
}
