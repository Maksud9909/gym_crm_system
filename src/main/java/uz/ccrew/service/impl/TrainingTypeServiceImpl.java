package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.dto.UserCredentials;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.service.AuthService;
import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.service.TrainingTypeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeDAO dao;
    private final AuthService authService;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeDAO trainingTypeDAO, AuthService authService) {
        this.dao = trainingTypeDAO;
        this.authService = authService;
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingType findById(Long id, UserCredentials userCredentials) {
        authService.verifyUserCredentials(userCredentials);
        log.info("Finding TrainingType by ID={}", id);
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TrainingType with ID=" + id + " not found"));
    }
}
