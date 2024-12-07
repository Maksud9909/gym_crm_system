package uz.ccrew.config;

import org.springframework.stereotype.Component;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.service.TraineeService;
import uz.ccrew.service.TrainerService;
import uz.ccrew.service.TrainingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component
public class ApplicationFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFacade.class);

    @Autowired
    public ApplicationFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        LOGGER.info("ApplicationFacade initialized with services");
    }

    public Long createTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    public Trainee getTrainee(Long id) {
        return traineeService.findById(id);
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.findAll();
    }

    public Long createTrainer(Trainer trainer) {
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
