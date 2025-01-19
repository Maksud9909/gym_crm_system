package uz.ccrew.service.impl;

import lombok.RequiredArgsConstructor;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.TrainerUpdateDTO;
import uz.ccrew.dto.trainee.TraineeShortDTO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.dto.trainer.TrainerProfileDTO;
import uz.ccrew.dto.trainer.TrainerProfileUsernameDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainee;
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
                .password(password)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .isActive(Boolean.TRUE)
                .build();
        Optional<TrainingType> trainingType = trainingTypeDAO.findByName(dto.getTrainingTypeName());

        Trainer trainer = Trainer.builder()
                .trainingType(trainingType.get())
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


        if (trainer.getTrainingType() != null) {
            existingTrainer.setTrainingType(trainer.getTrainingType());
        }
        trainerDAO.update(existingTrainer);
        log.info("Updated trainer with ID={}", trainer.getId());
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
                .traineeShortDTOS(traineeShortDTOS)
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
                .traineeShortDTOS(traineeShortDTOS)
                .build();
    }
}
