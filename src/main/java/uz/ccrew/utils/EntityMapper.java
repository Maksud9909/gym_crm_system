package uz.ccrew.utils;

import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;

import java.time.LocalDate;

public class EntityMapper {
    public static Trainee mapToTrainee(String[] data) {
        return Trainee.builder()
                .id(Long.parseLong(data[0]))
                .firstName(data[1])
                .lastName(data[2])
                .isActive(true)
                .dateOfBirth(LocalDate.parse(data[3]))
                .address(data[4])
                .build();
    }


    public static Trainer mapToTrainer(String[] data) {
        return Trainer.builder()
                .id(Long.parseLong(data[0]))
                .firstName(data[1])
                .lastName(data[2])
                .isActive(true)
                .specialization(data[3])
                .build();
    }


    public static Training mapToTraining(String[] data) {
        return Training.builder()
                .id(Long.parseLong(data[0]))
                .traineeId(Long.parseLong(data[1]))
                .trainerId(Long.parseLong(data[2]))
                .trainingName(data[3])
                .trainingType(TrainingType.valueOf(data[4]))
                .trainingDate(LocalDate.parse(data[5]))
                .trainingDuration(Integer.parseInt(data[6]))
                .build();
    }
}
