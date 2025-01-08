package uz.ccrew;

import uz.ccrew.entity.*;
import uz.ccrew.config.AppConfig;
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

//        createAndListTrainees(facade);
//        createAndListTrainers(facade);
//        createAndListTrainings(facade);
//        advancedTrainingOperations(facade);
//        advancedTraineeOperations(facade);
//        advancedTrainerOperations(facade);
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

        List<Trainee> trainees = facade.getTraineeService().findAll();
        trainees.forEach(t -> log.info("Trainee: {} {}, ID: {}", t.getUser().getFirstName(), t.getUser().getLastName(), t.getId()));
    }

    private static void createAndListTrainers(@NotNull ApplicationFacade facade) {
        log.info("---- Trainer Operations ----");

        TrainingType trainingType = facade.getTrainingTypeService().findById(1L);

        User user = User.builder()
                .firstName("Sasha")
                .lastName("Jukov")
                .build();

        Trainer trainer = Trainer.builder()
                .trainingType(trainingType)
                .user(user)
                .build();
        Long id = facade.getTrainerService().create(trainer);
        log.info("Created Trainer ID: {}", id);

        List<Trainer> trainers = facade.getTrainerService().findAll();
        trainers.forEach(t -> log.info("Trainer: {} {}, ID: {}", t.getUser().getFirstName(), t.getUser().getLastName(), t.getId()));
    }

    private static void createAndListTrainings(@NotNull ApplicationFacade facade) {
        log.info("---- Training Operations ----");

        TrainingType trainingType = facade.getTrainingTypeService().findById(2L);

        Training training = Training.builder()
                .trainee(facade.getTraineeService().findById(1L))
                .trainer(facade.getTrainerService().findById(1L))
                .trainingName("Morning Yoga Session")
                .trainingType(trainingType)
                .trainingDate(LocalDate.now())
                .trainingDuration(60.0)
                .build();
        Long trainingId = facade.getTrainingService().create(training);
        log.info("Created Training ID: {}", trainingId);

        List<Training> trainings = facade.getTrainingService().findAll();
        trainings.forEach(t -> log.info("Training: {}, Date: {}, Duration: {} minutes",
                t.getTrainingName(), t.getTrainingDate(), t.getTrainingDuration()));
    }

    private static void advancedTraineeOperations(@NotNull ApplicationFacade facade) {
        log.info("---- Advanced Trainee Operations ----");

        String usernameToDelete = "John.Doe";
        facade.getTraineeService().deleteTraineeByUsername(usernameToDelete);
        log.info("Deleted Trainee with username: {}", usernameToDelete);

        facade.getTraineeService().updateTraineeTrainers(2L, List.of(15L, 16L));
        log.info("Updated trainers for Trainee ID: 2");
    }

    private static void advancedTrainerOperations(@NotNull ApplicationFacade facade) {
        log.info("---- Advanced Trainer Operations ----");

        List<Trainer> unassignedTrainers = facade.getTrainerService().getUnassignedTrainers("John.Doe.1");
        unassignedTrainers.forEach(tr -> log.info("Unassigned Trainer: {} {}", tr.getUser().getFirstName(), tr.getUser().getLastName()));
    }

    private static void advancedTrainingOperations(@NotNull ApplicationFacade facade) {
        List<Training> trainings = facade.getTrainingService().getTraineeTrainings(
                "Michael.Brown",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 12, 31),
                "John",
                null);
        log.info("Printing trainings for {} trainings", trainings.size());
        trainings.forEach(t -> log.info("Training: {}, Trainer: {}",
                t.getTrainingName(),
                t.getTrainer().getUser().getFirstName()));

        List<Training> list = facade.getTrainingService().getTrainerTrainings(
                "Test",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                "Jane");
        log.info("Printing trainings for {} trainings", list.size());
        trainings.forEach(t -> log.info("Training: {},Trainee {}",
                t.getTrainingName(),
                t.getTrainee().getUser().getFirstName()));
    }
}
