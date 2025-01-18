package uz.ccrew.service;

import uz.ccrew.dto.trainingType.TrainingTypeIdDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    TrainingType findById(Long id, UserCredentials userCredentials);
    List<TrainingTypeIdDTO> findAll();
}
