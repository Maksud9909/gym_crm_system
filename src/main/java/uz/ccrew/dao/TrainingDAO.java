package uz.ccrew.dao;

import uz.ccrew.entity.Training;
import uz.ccrew.dao.base.BaseDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

public interface TrainingDAO extends BaseDAO<Training> {
    List<Training> getTraineeTrainings(String traineeUsername, LocalDateTime fromDate,
                                       LocalDateTime toDate, String trainerName, String trainingTypeName);

    List<Training> getTrainerTrainings(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate, String traineeName);

    Optional<Training> findById(Long id);

    void update(Training training);

    void delete(Training training);
}
