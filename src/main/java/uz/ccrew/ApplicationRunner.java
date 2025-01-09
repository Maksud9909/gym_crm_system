package uz.ccrew;

import uz.ccrew.entity.*;
import uz.ccrew.config.AppConfig;
import uz.ccrew.dto.UserCredentials;
import uz.ccrew.config.HibernateConfig;
import uz.ccrew.config.DataSourceConfig;
import uz.ccrew.config.ApplicationFacade;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.time.LocalDate;

@Slf4j
public class ApplicationRunner {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, DataSourceConfig.class, HibernateConfig.class);
        ApplicationFacade facade = context.getBean(ApplicationFacade.class);

        log.info("---- Starting Application ----");

        createAndListTrainees(facade);
        createAndListTrainers(facade);
        createAndListTrainings(facade);
        log.info("---- Application Finished ----");
    }

    private static void createAndListTrainees(@NotNull ApplicationFacade facade) {
        log.info("---- Trainee Operations ----");

        User user = User.builder()
                .firstName("Rustam")
                .lastName("jj")
                .lastName("Rustamov")
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(2005, 1, 1))
                .address("Test")
                .build();
        Long id = facade.getTraineeService().create(trainee);
        log.info("Created Trainee ID: {}", id);

        List<Trainee> trainees = facade.getTraineeService().findAll(new UserCredentials("Test", "Test"));
        trainees.forEach(t -> log.info("Trainee: {} {}, ID: {}", t.getUser().getFirstName(), t.getUser().getLastName(), t.getId()));
    }

    private static void createAndListTrainers(@NotNull ApplicationFacade facade) {
        log.info("---- Trainer Operations ----");

        TrainingType trainingType = facade.getTrainingTypeService().findById(1L, new UserCredentials("Test", "Test"));

        User user = User.builder()
                .firstName("Maks")
                .lastName("Jukov")
                .build();

        Trainer trainer = Trainer.builder()
                .trainingType(trainingType)
                .user(user)
                .build();
        Long id = facade.getTrainerService().create(trainer);
        log.info("Created Trainer ID: {}", id);

        List<Trainer> trainers = facade.getTrainerService().findAll(new UserCredentials("Test", "Test"));
        trainers.forEach(t -> log.info("Trainer: {} {}, ID: {}", t.getUser().getFirstName(), t.getUser().getLastName(), t.getId()));
    }

    private static void createAndListTrainings(@NotNull ApplicationFacade facade) {
        log.info("---- Training Operations ----");

        TrainingType trainingType = facade.getTrainingTypeService().findById(2L, new UserCredentials("Test", "Test"));

        Training training = Training.builder()
                .trainee(facade.getTraineeService().findById(1L, new UserCredentials("Test", "Test")))
                .trainer(facade.getTrainerService().findById(1L, new UserCredentials("Test", "Test")))
                .trainingName("Morning Yoga Session")
                .trainingType(trainingType)
                .trainingDate(LocalDate.now())
                .trainingDuration(60.0)
                .build();
        Long trainingId = facade.getTrainingService().create(training);
        log.info("Created Training ID: {}", trainingId);

        List<Training> trainings = facade.getTrainingService().findAll(new UserCredentials("Test", "Test"));
        trainings.forEach(t -> log.info("Training: {}, Date: {}, Duration: {} minutes",
                t.getTrainingName(), t.getTrainingDate(), t.getTrainingDuration()));
    }
}
