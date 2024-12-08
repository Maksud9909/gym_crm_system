package uz.ccrew.config;

import uz.ccrew.entity.*;
import uz.ccrew.utils.DataLoader;
import uz.ccrew.utils.EntityMapper;

import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
@Component
public class DataInitializer {
    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Training> trainingStorage;

    @Value("${trainee.data.file}")
    private String traineeDataFile;

    @Value("${trainer.data.file}")
    private String trainerDataFile;

    @Value("${training.data.file}")
    private String trainingDataFile;

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setTrainingStorage(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @PostConstruct
    public void init() {
        DataLoader.loadData(traineeDataFile, traineeStorage, EntityMapper::mapToTrainee, Trainee::getId);
        DataLoader.loadData(trainerDataFile, trainerStorage, EntityMapper::mapToTrainer, Trainer::getId);
        DataLoader.loadData(trainingDataFile, trainingStorage, EntityMapper::mapToTraining, Training::getId);
    }
}
