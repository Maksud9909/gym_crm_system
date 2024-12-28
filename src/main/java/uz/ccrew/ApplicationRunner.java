package uz.ccrew;

import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.config.AppConfig;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.config.HibernateConfig;
import uz.ccrew.config.DataSourceConfig;
import uz.ccrew.config.ApplicationFacade;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;

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
        advancedTrainingOperations(facade);
        advancedTraineeOperations(facade);
        advancedTrainerOperations(facade);
        log.info("---- Application Finished ----");
    }

    private static void createAndListTrainees(@NotNull ApplicationFacade facade) {
        log.info("---- Trainee Operations ----");

        TraineeCreateDTO traineeDTO = TraineeCreateDTO.builder()
                .firstName("Maksud")
                .lastName("Rustamov")
                .birthOfDate(LocalDate.of(2005, 1, 1))
                .address("Test")
                .build();
        Long id = facade.getTraineeService().create(traineeDTO);
        log.info("Created Trainee ID: {}", id);

        List<Trainee> trainees = facade.getTraineeService().findAll();
        trainees.forEach(t -> log.info("Trainee: {} {}, ID: {}", t.getUser().getFirstName(), t.getUser().getLastName(), t.getId()));
    }

    private static void createAndListTrainers(@NotNull ApplicationFacade facade) {
        log.info("---- Trainer Operations ----");

        TrainerCreateDTO trainerDTO = TrainerCreateDTO.builder()
                .firstName("Lev")
                .lastName("Smith")
                .trainingTypeId(1L)
                .build();
        Long trainerId = facade.getTrainerService().create(trainerDTO);
        log.info("Created Trainer ID: {}", trainerId);

        List<Trainer> trainers = facade.getTrainerService().findAll();
        trainers.forEach(t -> log.info("Trainer: {} {}, ID: {}", t.getUser().getFirstName(), t.getUser().getLastName(), t.getId()));
    }

    private static void createAndListTrainings(@NotNull ApplicationFacade facade) {
        log.info("---- Training Operations ----");

        TrainingType trainingType = facade.getTrainingTypeService().findById(2L);

        Training training = Training.builder()
                .trainee(facade.getTraineeService().findById(1L))
                .trainer(facade.getTrainerService().findById(11L))
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

        String usernameToDelete = "Maksud.Doe";
        facade.getTraineeService().deleteTraineeByUsername(usernameToDelete);
        log.info("Deleted Trainee with username: {}", usernameToDelete);

        facade.getTraineeService().updateTraineeTrainers(1L, List.of(10L, 11L));
        log.info("Updated trainers for Trainee ID: 1");
    }

    private static void advancedTrainerOperations(@NotNull ApplicationFacade facade) {
        log.info("---- Advanced Trainer Operations ----");

        List<Trainer> unassignedTrainers = facade.getTrainerService().getUnassignedTrainers("John.Doe.1");
        unassignedTrainers.forEach(tr -> log.info("Unassigned Trainer: {} {}", tr.getUser().getFirstName(), tr.getUser().getLastName()));
    }

    private static void advancedTrainingOperations(@NotNull ApplicationFacade facade) {
        List<Training> trainings = facade.getTrainingService().getTraineeTrainings(
                "Jane.Smith",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                "Lev",
                3L);
        trainings.forEach(t -> log.info("Training: {}, Trainer: {}",
                t.getTrainingName(),
                t.getTrainer().getUser().getFirstName()));
                facade.getTrainingService().getTrainerTrainings(
                        "Test",
                        LocalDate.of(2023, 1, 1),
                        LocalDate.of(2023, 12, 31),
                        "Jane");
    }
}
