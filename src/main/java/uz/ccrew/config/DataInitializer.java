package uz.ccrew.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.ccrew.service.TraineeService;
import uz.ccrew.service.TrainerService;
import uz.ccrew.service.TrainingService;

@Component
public class DataInitializer {

    private TraineeService traineeService;

    private TrainerService trainerService;

    private TrainingService trainingService;

    @Value("${trainee.data.file}")
    private String traineeDataFile;

    @Value("${trainer.data.file}")
    private String trainerDataFile;

    @Value("${training.data.file}")
    private String trainingDataFile;

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostConstruct
    public void init() {
//        DataLoader.loadData(traineeDataFile, EntityMapper::mapToTrainee, traineeService::create);
//        DataLoader.loadData(trainerDataFile, EntityMapper::mapToTrainer, trainerService::create);
//        DataLoader.loadData(trainingDataFile, EntityMapper::mapToTraining, trainingService::create);
    }

}
