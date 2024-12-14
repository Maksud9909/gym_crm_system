package uz.ccrew;

import uz.ccrew.entity.*;
import uz.ccrew.config.AppConfig;
import uz.ccrew.config.ApplicationFacade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class ApplicationRunner {

    private final ApplicationFacade facade;

    public ApplicationRunner(ApplicationFacade facade) {
        this.facade = facade;
    }

    public void start() {
        log.info("---- Starting Application ----");

        handleTraineeOperations();
        handleTrainerOperations();
        handleTrainingOperations();

        log.info("---- Application Finished ----");
    }

    private void handleTraineeOperations() {
        log.info("---- Trainee Operations ----");
        List<Trainee> trainees = facade.getAllTrainees();
        trainees.forEach(t -> log.info("Trainee: {} {}", t.getFirstName(), t.getLastName()));

        Trainee newTrainee = Trainee.builder()
                .firstName("Michael")
                .lastName("Brown")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2005, 12, 4))
                .address("789 Oak St")
                .build();
        Long traineeId = facade.createTrainee(newTrainee);
        log.info("Created Trainee ID: {}", traineeId);
    }

    private void handleTrainerOperations() {
        log.info("---- Trainer Operations ----");
        List<Trainer> trainers = facade.getAllTrainers();
        trainers.forEach(t -> log.info("Trainer: {} {}", t.getFirstName(), t.getLastName()));

        Trainer newTrainer = Trainer.builder()
                .id(null)
                .firstName("Emily")
                .lastName("Davis")
                .username(null)
                .password(null)
                .isActive(true)
                .specialization("Pilates")
                .build();

        Long trainerId = facade.createTrainer(newTrainer);
        log.info("Created Trainer ID: {}", trainerId);
    }

    private void handleTrainingOperations() {
        log.info("---- Training Operations ----");
        List<Training> trainings = facade.getAllTrainings();
        trainings.forEach(t -> log.info("Training: {}", t.getTrainingName()));

        Training newTraining = Training.builder()
                .traineeId(1L)
                .trainerId(1L)
                .trainingName("Pilates Session")
                .trainingType(TrainingType.GYM)
                .trainingDate(LocalDate.now())
                .trainingDuration(90)
                .build();

        Long trainingId = facade.createTraining(newTraining);
        log.info("Created Training ID: {}", trainingId);
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ApplicationRunner runner = context.getBean(ApplicationRunner.class);
        runner.start();
    }
}
