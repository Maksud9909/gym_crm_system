package uz.ccrew.service;

import uz.ccrew.entity.Training;

import java.util.List;

public interface TrainingService {
    Long create(Training training);

    Training findById(Long id);

    List<Training> findAll();
}
