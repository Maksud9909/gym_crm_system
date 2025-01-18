package uz.ccrew.dao;

import uz.ccrew.entity.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDAO {
    Optional<TrainingType> findById(Long id);
    List<TrainingType> findAll();
}
