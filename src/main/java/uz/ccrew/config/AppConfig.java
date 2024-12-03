package uz.ccrew.config;

import uz.ccrew.dao.*;
import uz.ccrew.entity.*;
import uz.ccrew.service.*;
import uz.ccrew.service.impl.*;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Configuration
@ComponentScan(basePackages = "uz.ccrew")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${trainee.data.file}")
    private String traineeDataFile;

    @Value("${trainer.data.file}")
    private String trainerDataFile;

    @Value("${training.data.file}")
    private String trainingDataFile;

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        return new HashMap<>();
    }

    @Bean
    public Set<String> existingUsernames() {
        return Collections.synchronizedSet(new HashSet<>());
    }

    @Bean
    public DataInitializer dataInitializer() {
        DataInitializer initializer = new DataInitializer();
        initializer.setTraineeStorage(traineeStorage());
        initializer.setTrainerStorage(trainerStorage());
        initializer.setTrainingStorage(trainingStorage());
        initializer.setTraineeDataFile(traineeDataFile);
        initializer.setTrainerDataFile(trainerDataFile);
        initializer.setTrainingDataFile(trainingDataFile);
        return initializer;
    }

    @Bean
    public ApplicationFacade applicationFacade() {
        return new ApplicationFacade(traineeService(), trainerService(), trainingService());
    }

    @Bean
    public TraineeService traineeService() {
        TraineeServiceImpl service = new TraineeServiceImpl();
        service.setDao(traineeDAO());
        return service;
    }

    @Bean
    public TrainerService trainerService() {
        TrainerServiceImpl service = new TrainerServiceImpl();
        service.setDao(trainerDAO());
        return service;
    }

    @Bean
    public TrainingService trainingService() {
        TrainingServiceImpl service = new TrainingServiceImpl();
        service.setDao(trainingDAO());
        return service;
    }

    @Bean
    public TraineeDAO traineeDAO() {
        TraineeDAO dao = new TraineeDAO();
        dao.setTraineeStorage(traineeStorage());
        dao.setExistingUsernames(existingUsernames());
        return dao;
    }

    @Bean
    public TrainerDAO trainerDAO() {
        TrainerDAO dao = new TrainerDAO();
        dao.setTrainerStorage(trainerStorage());
        dao.setExistingUsernames(existingUsernames());
        return dao;
    }

    @Bean
    public TrainingDAO trainingDAO() {
        TrainingDAO dao = new TrainingDAO();
        dao.setTrainingStorage(trainingStorage());
        return dao;
    }
}
