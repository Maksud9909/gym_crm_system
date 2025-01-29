package uz.ccrew.dao;

import uz.ccrew.entity.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDAO {
    List<TrainingType> findAll();

    Optional<TrainingType> findByName(String trainingTypeName);
}
