package uz.ccrew.service;

import uz.ccrew.entity.Trainee;

import java.util.List;

public interface TraineeService {
    Long create(Trainee trainee);

    void delete(Long id);

    void update(Long id, Trainee trainee);

    Trainee findById(Long id);

    List<Trainee> findAll();
}
