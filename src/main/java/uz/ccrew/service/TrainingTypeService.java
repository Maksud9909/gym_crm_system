package uz.ccrew.service;

import uz.ccrew.dto.trainingType.TrainingTypeIdDTO;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeIdDTO> findAll();
}
