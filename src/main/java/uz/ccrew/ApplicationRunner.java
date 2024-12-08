package uz.ccrew;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import uz.ccrew.config.AppConfig;
import uz.ccrew.config.ApplicationFacade;
import uz.ccrew.entity.*;

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

        log.info("---- Trainee Operations ----");
        List<Trainee> trainees = facade.getAllTrainees();
        trainees.forEach(t -> log.info("Trainee: {} {}", t.getFirstName(), t.getLastName()));

        Trainee newTrainee = new Trainee(null,
                "Michael",
                "Brown",
                null,
                null,
                true,
                LocalDate.of(2005, 12, 4),
                "789 Oak St");
        Long traineeId = facade.createTrainee(newTrainee);
        log.info("Created Trainee ID: {}", traineeId);

        log.info("---- Trainer Operations ----");
        List<Trainer> trainers = facade.getAllTrainers();
        trainers.forEach(t -> log.info("Trainer: {} {}", t.getFirstName(), t.getLastName()));

        Trainer newTrainer = new Trainer(null,
                "Emily",
                "Davis",
                null,
                null,
                true,
                "Pilates");
        Long trainerId = facade.createTrainer(newTrainer);
        log.info("Created Trainer ID: {}", trainerId);

        log.info("---- Training Operations ----");
        List<Training> trainings = facade.getAllTrainings();
        trainings.forEach(t -> log.info("Training: {}", t.getTrainingName()));

        Training newTraining = new Training(
                null,
                traineeId,
                trainerId,
                "Pilates Session",
                TrainingType.GYM,
                LocalDate.now(),
                90
        );
        Long trainingId = facade.createTraining(newTraining);
        log.info("Created Training ID: {}", trainingId);

        log.info("---- Application Finished ----");
    }
}
