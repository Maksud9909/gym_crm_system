package uz.ccrew.service;

import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.TrainingType;

public interface TrainingTypeService {
    TrainingType findById(Long id, UserCredentials userCredentials);
}
