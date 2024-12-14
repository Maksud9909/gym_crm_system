package uz.ccrew.utils;

import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;

import java.time.LocalDate;

public class EntityMapper {
    public static Trainee mapToTrainee(String[] data) {
        return Trainee.builder()
                .firstName(data[0])
                .lastName(data[1])
                .isActive(true)
                .dateOfBirth(LocalDate.parse(data[2]))
                .address(data[3])
                .build();
    }


    public static Trainer mapToTrainer(String[] data) {
        return Trainer.builder()
                .firstName(data[0])
                .lastName(data[1])
                .isActive(true)
                .specialization(data[2])
                .build();
    }


    public static Training mapToTraining(String[] data) {
        return Training.builder()
                .traineeId(Long.parseLong(data[0]))
                .trainerId(Long.parseLong(data[1]))
                .trainingName(data[2])
                .trainingType(TrainingType.valueOf(data[3]))
                .trainingDate(LocalDate.parse(data[4]))
                .trainingDuration(Integer.parseInt(data[5]))
                .build();
    }
}
