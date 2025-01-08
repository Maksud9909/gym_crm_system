package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.entity.TrainingType;
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

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeDAO trainingTypeDAO) {
        this.dao = trainingTypeDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingType findById(Long id) {
        log.info("Finding TrainingType by ID={}", id);
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TrainingType with ID=" + id + " not found"));
    }
}
