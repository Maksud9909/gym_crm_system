package uz.ccrew.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uz.ccrew.entity.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Training> trainingStorage;

    private String traineeDataFile;
    private String trainerDataFile;
    private String trainingDataFile;

    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

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
        logger.info("Initializing storage with data from files");
        loadTraineeData();
        loadTrainerData();
        loadTrainingData();
    }

    private void loadTraineeData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(traineeDataFile)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Example: id,firstName,lastName,dateOfBirth,address
                String[] data = line.split(",");
                Trainee trainee = new Trainee(
                        Long.parseLong(data[0]),
                        data[1],
                        data[2],
                        null, // Username will be generated
                        null, // Password will be generated
                        true,
                        java.sql.Date.valueOf(data[3]), // Date of birth
                        data[4]
                );
                traineeStorage.put(trainee.getId(), trainee);
            }
            logger.info("Loaded Trainee data from file");
        } catch (Exception e) {
            logger.error("Error loading Trainee data", e);
        }
    }

    private void loadTrainerData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(trainerDataFile)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Example: id,firstName,lastName,specialization
                String[] data = line.split(",");
                Trainer trainer = new Trainer(
                        Long.parseLong(data[0]),
                        data[1],
                        data[2],
                        null, // Username will be generated
                        null, // Password will be generated
                        true,
                        data[3]
                );
                trainerStorage.put(trainer.getId(), trainer);
            }
            logger.info("Loaded Trainer data from file");
        } catch (Exception e) {
            logger.error("Error loading Trainer data", e);
        }
    }

    private void loadTrainingData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(trainingDataFile)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Example: id,traineeId,trainerId,trainingName,trainingType,trainingDate,trainingDuration
                String[] data = line.split(",");
                Training training = new Training(
                        Long.parseLong(data[0]),
                        data[1], // traineeId
                        data[2], // trainerId
                        data[3], // trainingName
                        TrainingType.valueOf(data[4]), // trainingType
                        LocalDate.parse(data[5]), // trainingDate
                        Integer.parseInt(data[6]) // trainingDuration
                );
                trainingStorage.put(training.getId(), training);
            }
            logger.info("Loaded Training data from file");
        } catch (Exception e) {
            logger.error("Error loading Training data", e);
        }
    }
}
