package uz.ccrew.utils;

import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;

import java.time.LocalDate;

public class EntityMapper {
    public static Trainee mapToTrainee(String[] data) {
        return new Trainee(
            Long.parseLong(data[0]),
            data[1],
            data[2],
            null,
            null,
            true,
            LocalDate.parse(data[3]),
            data[4]
        );
    }

    public static Trainer mapToTrainer(String[] data) {
        return new Trainer(
            Long.parseLong(data[0]),
            data[1],
            data[2],
            null,
            null,
            true,
            data[3]
        );
    }

    public static Training mapToTraining(String[] data) {
        return new Training(
            Long.parseLong(data[0]),
            Long.parseLong(data[1]),
            Long.parseLong(data[2]),
            data[3],
            TrainingType.valueOf(data[4]),
            LocalDate.parse(data[5]),
            Integer.parseInt(data[6])
        );
    }
}
