package uz.ccrew.service;

import uz.ccrew.entity.Trainer;

import java.util.List;

public interface TrainerService {
    Long create(Trainer trainer);

    void update(Long id, Trainer trainer);

    Trainer findById(Long id);

    List<Trainer> findAll();
}
