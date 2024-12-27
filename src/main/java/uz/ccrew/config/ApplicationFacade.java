package uz.ccrew.config;

import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.service.TraineeService;
import uz.ccrew.service.TrainerService;
import uz.ccrew.service.TrainingService;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Component
public class ApplicationFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public ApplicationFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        log.info("ApplicationFacade initialized with services");
    }

    public Long createTrainee(TraineeCreateDTO trainee) {
        return traineeService.create(trainee);
    }

    public Optional<Trainee> getTrainee(Long id) {
        return Optional.ofNullable(traineeService.findById(id));
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.findAll();
    }

    public Long createTrainer(TrainerCreateDTO trainer) {
        return trainerService.create(trainer);
    }

    public Trainer getTrainer(Long id) {
        return trainerService.findById(id);
    }

    public List<Trainer> getAllTrainers() {
        return trainerService.findAll();
    }

    public Long createTraining(Training training) {
        return trainingService.create(training);
    }

    public Training getTraining(Long id) {
        return trainingService.findById(id);
    }

    public List<Training> getAllTrainings() {
        return trainingService.findAll();
    }
}
