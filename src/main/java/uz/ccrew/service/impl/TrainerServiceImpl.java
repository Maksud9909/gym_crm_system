package uz.ccrew.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.dto.trainee.TraineeShortDTO;
import uz.ccrew.dto.trainer.*;
import uz.ccrew.dto.trainer.TrainerTrainingDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.*;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.service.TrainerService;
import uz.ccrew.exp.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final UserDAO userDAO;
    private final UserUtils userUtils;
    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;
    private final TrainingTypeDAO trainingTypeDAO;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserCredentials create(TrainerCreateDTO dto) {
        if (dto == null) {
            log.error("Cannot create a null trainer");
            throw new IllegalArgumentException("Trainer cannot be null");
        }

        String username = userUtils.generateUniqueUsername(dto.getFirstName(), dto.getLastName());
        String password = userUtils.generateRandomPassword();

        User newUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .isActive(Boolean.TRUE)
                .build();
        TrainingType trainingType = trainingTypeDAO.findByName(dto.getTrainingTypeName()).orElseThrow(
                () -> new EntityNotFoundException("Training type not found with name: " + dto.getTrainingTypeName()));

        Trainer trainer = Trainer.builder()
                .trainingType(trainingType)
                .user(newUser)
                .build();

        trainerDAO.create(trainer);
        log.info("Trainer created: {}", dto);
        return UserCredentials.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerDTO> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for Trainee username={}", traineeUsername);
        List<Trainer> trainers = trainerDAO.getUnassignedTrainers(traineeUsername);

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
    public TrainerProfileDTO getProfile(String username) {
        Trainer trainer = trainerDAO.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Trainer with username=" + username + " not found"));
        List<TraineeShortDTO> traineeShortDTOS = trainer.getTraining().stream()
                .map(training -> {
                    Trainee trainee = training.getTrainee();
                    return TraineeShortDTO.builder()
                            .username(trainee.getUser().getUsername())
                            .firstName(trainee.getUser().getFirstName())
                            .lastName(trainee.getUser().getLastName())
                            .build();
                })
                .toList();
        return TrainerProfileDTO.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
                .trainees(traineeShortDTOS)
                .isActive(trainer.getUser().getIsActive())
                .build();
    }

    @Override
    @Transactional
    public TrainerProfileUsernameDTO update(TrainerUpdateDTO dto) {
        Trainer trainer = trainerDAO.findByUsername(dto.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("Trainer with username=" + dto.getUsername() + " not found")
        );
        trainer.getUser().setFirstName(dto.getFirstName());
        trainer.getUser().setLastName(dto.getLastName());
        trainer.getUser().setIsActive(dto.getIsActive());

        trainerDAO.update(trainer);

        List<TraineeShortDTO> traineeShortDTOS = trainer.getTraining().stream()
                .map(training -> {
                    Trainee trainee = training.getTrainee();
                    return TraineeShortDTO.builder()
                            .username(trainee.getUser().getUsername())
                            .firstName(trainee.getUser().getFirstName())
                            .lastName(trainee.getUser().getLastName())
                            .build();
                })
                .toList();

        return TrainerProfileUsernameDTO.builder()
                .username(dto.getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
                .isActive(trainer.getUser().getIsActive())
                .trainees(traineeShortDTOS)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerTrainingDTO> getTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        List<Training> trainings = trainingDAO.getTrainerTrainings(username, fromDate, toDate, traineeName);
        return trainings.stream().map(training -> {
            Trainee trainee = training.getTrainee();
            return TrainerTrainingDTO.builder()
                    .trainingName(training.getTrainingName())
                    .trainingDate(training.getTrainingDate())
                    .trainingType(training.getTrainingType().getTrainingTypeName())
                    .trainingDuration(training.getTrainingDuration())
                    .traineeName(trainee.getUser().getFirstName())
                    .build();
        }).toList();
    }
}
