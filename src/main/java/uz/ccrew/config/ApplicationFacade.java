package uz.ccrew.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.service.impl.TraineeServiceImpl;
import uz.ccrew.service.impl.TrainerServiceImpl;
import uz.ccrew.service.impl.TrainingServiceImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Component
public class ApplicationFacade {
    private final TraineeServiceImpl traineeServiceImpl;
    private final TrainerServiceImpl trainerServiceImpl;
    private final TrainingServiceImpl trainingService;

    public ApplicationFacade(TraineeServiceImpl traineeServiceImpl, TrainerServiceImpl trainerServiceImpl, TrainingServiceImpl trainingService) {
        this.traineeServiceImpl = traineeServiceImpl;
        this.trainerServiceImpl = trainerServiceImpl;
        this.trainingService = trainingService;
        log.info("ApplicationFacade initialized with services");
    }

    public Long createTrainee(TraineeCreateDTO trainee) {
        return traineeServiceImpl.create(trainee);
    }

    public Optional<Trainee> getTrainee(Long id) {
        return Optional.ofNullable(traineeServiceImpl.findById(id));
    }

    public List<Trainee> getAllTrainees() {
        return traineeServiceImpl.findAll();
    }

    public Long createTrainer(TrainerCreateDTO trainer) {
        return trainerServiceImpl.create(trainer);
    }

    public Trainer getTrainer(Long id) {
        return trainerServiceImpl.findById(id);
    }

    public List<Trainer> getAllTrainers() {
        return trainerServiceImpl.findAll();
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
