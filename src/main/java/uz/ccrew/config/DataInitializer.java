package uz.ccrew.config;

import uz.ccrew.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class DataInitializer {
    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Training> trainingStorage;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

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

    public void setTraineeDataFile(String traineeDataFile) {
        this.traineeDataFile = traineeDataFile;
    }

    public void setTrainerDataFile(String trainerDataFile) {
        this.trainerDataFile = trainerDataFile;
    }

    public void setTrainingDataFile(String trainingDataFile) {
        this.trainingDataFile = trainingDataFile;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing storage with data from files");
        loadTraineeData();
        loadTrainerData();
        loadTrainingData();
    }

    private void loadTraineeData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(traineeDataFile)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Trainee trainee = new Trainee(
                        Long.parseLong(data[0]),
                        data[1],
                        data[2],
                        null, // Username will be generated
                        null, // Password will be generated
                        true,
                        LocalDate.parse(data[3]), // Date of birth
                        data[4]
                );
                traineeStorage.put(trainee.getId(), trainee);
            }
            LOGGER.info("Loaded Trainee data from file");
        } catch (Exception e) {
            LOGGER.error("Error loading Trainee data", e);
        }
    }

    private void loadTrainerData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(trainerDataFile)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Trainer trainer = new Trainer(
                        Long.parseLong(data[0]),
                        data[1],
                        data[2],
                        null, // username will be generated
                        null, // Password will be generated
                        true,
                        data[3]
                );
                trainerStorage.put(trainer.getId(), trainer);
            }
            LOGGER.info("Loaded Trainer data from file");
        } catch (Exception e) {
            LOGGER.error("Error loading Trainer data", e);
        }
    }

    private void loadTrainingData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(trainingDataFile)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Training training = new Training(
                        Long.parseLong(data[0]),
                        Long.parseLong(data[1]), // traineeId
                        Long.parseLong(data[2]), // trainerId
                        data[3], // trainingName
                        TrainingType.valueOf(data[4]), // trainingType
                        LocalDate.parse(data[5]), // trainingDate
                        Integer.parseInt(data[6]) // trainingDuration
                );
                trainingStorage.put(training.getId(), training);
            }
            LOGGER.info("Loaded Training data from file");
        } catch (Exception e) {
            LOGGER.error("Error loading Training data", e);
        }
    }
}
