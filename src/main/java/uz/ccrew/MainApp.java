package uz.ccrew;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uz.ccrew.config.AppConfig;
import uz.ccrew.config.ApplicationFacade;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        // Initialize Spring Application Context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get the Application Facade Bean
        ApplicationFacade facade = context.getBean(ApplicationFacade.class);

        // Interact with Trainee Service
        System.out.println("---- Trainee Operations ----");
        List<Trainee> trainees = facade.getAllTrainees();
        trainees.forEach(t -> System.out.println("Trainee: " + t.getFirstName() + " " + t.getLastName()));

        Trainee newTrainee = new Trainee(null, "Michael", "Brown", null, null, true, new Date(), "789 Oak St");
        Long traineeId = facade.createTrainee(newTrainee);
        System.out.println("Created Trainee ID: " + traineeId);

        // Interact with Trainer Service
        System.out.println("---- Trainer Operations ----");
        List<Trainer> trainers = facade.getAllTrainers();
        trainers.forEach(t -> System.out.println("Trainer: " + t.getFirstName() + " " + t.getLastName()));

        Trainer newTrainer = new Trainer(null, "Emily", "Davis", null, null, true, "Pilates");
        Long trainerId = facade.createTrainer(newTrainer);
        System.out.println("Created Trainer ID: " + trainerId);

        // Interact with Training Service
        System.out.println("---- Training Operations ----");
        List<Training> trainings = facade.getAllTrainings();
        trainings.forEach(t -> System.out.println("Training: " + t.getTrainingName()));

        Training newTraining = new Training(
                null,
                traineeId.toString(),
                trainerId.toString(),
                "Pilates Session",
                TrainingType.GYM,
                LocalDate.now(),
                90
        );
        Long trainingId = facade.createTraining(newTraining);
        System.out.println("Created Training ID: " + trainingId);
    }
}
