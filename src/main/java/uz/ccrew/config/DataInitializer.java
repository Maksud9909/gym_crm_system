package uz.ccrew.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.ccrew.service.impl.TraineeServiceImpl;
import uz.ccrew.service.impl.TrainerServiceImpl;
import uz.ccrew.service.impl.TrainingServiceImpl;

@Component
public class DataInitializer {

    private TraineeServiceImpl traineeServiceImpl;

    private TrainerServiceImpl trainerServiceImpl;

    private TrainingServiceImpl trainingService;

    @Value("${trainee.data.file}")
    private String traineeDataFile;

    @Value("${trainer.data.file}")
    private String trainerDataFile;

    @Value("${training.data.file}")
    private String trainingDataFile;

    @Autowired
    public void setTrainingService(TrainingServiceImpl trainingService) {
        this.trainingService = trainingService;
    }

    @Autowired
    public void setTrainerService(TrainerServiceImpl trainerServiceImpl) {
        this.trainerServiceImpl = trainerServiceImpl;
    }

    @Autowired
    public void setTraineeService(TraineeServiceImpl traineeServiceImpl) {
        this.traineeServiceImpl = traineeServiceImpl;
    }

    @PostConstruct
    public void init() {
//        DataLoader.loadData(traineeDataFile, EntityMapper::mapToTrainee, traineeService::create);
//        DataLoader.loadData(trainerDataFile, EntityMapper::mapToTrainer, trainerService::create);
//        DataLoader.loadData(trainingDataFile, EntityMapper::mapToTraining, trainingService::create);
    }

}
