package uz.ccrew.service.impl;

import lombok.RequiredArgsConstructor;
import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.service.AuthService;
import uz.ccrew.service.TrainingService;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                              String trainerName, String trainingTypeName, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Fetching trainings for Trainee username={} with filters", traineeUsername);
        return trainingDAO.getTraineeTrainings(traineeUsername, fromDate, toDate, trainerName, trainingTypeName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                              String traineeName, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Fetching trainings for Trainer username={} with filters", trainerUsername);
        return trainingDAO.getTrainerTrainings(trainerUsername, fromDate, toDate, traineeName);
    }

    @Override
    @Transactional
    public void addTraining(TrainingDTO dto) {
        Trainee trainee = traineeDAO.findByUsername(dto.getTraineeUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainee with username=" + dto.getTraineeUsername() + " not found"));

        Trainer trainer = trainerDAO.findByUsername(dto.getTrainerUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainer with username=" + dto.getTrainerUsername() + " not found"));

        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(dto.getTrainingDate())
                .trainingName(dto.getTrainingName())
                .trainingDuration(dto.getTrainingDuration())
                .trainingType(trainer.getTrainingType())
                .build();
        trainingDAO.create(training);
    }

    //    @Override
    @Transactional
    public Long create(Training training) {
        Trainee trainee = traineeDAO.findById(training.getTrainee().getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainee with ID=" + training.getTrainee().getId() + " not found"));

        Trainer trainer = trainerDAO.findById(training.getTrainer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer with ID=" + training.getTrainer().getId() + " not found"));

        training.setTrainee(trainee);
        training.setTrainer(trainer);

        trainingDAO.create(training);

        log.info("Created Training:{} with ID:{}", training, training.getId());
        return training.getId();
    }

    @Override
    public Object create(Object entity) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Training findById(Long id, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Finding Training with ID={}", id);
        Training training = trainingDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training with ID=" + id + " not found"));
        log.info("Found Training: {}", training);
        return training;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findAll(UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Fetching all Trainings");
        List<Training> trainings = trainingDAO.findAll();
        log.info("Found {} Trainings", trainings.size());
        return trainings;
    }
}
