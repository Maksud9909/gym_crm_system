package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.dto.trainingType.TrainingTypeIdDTO;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.service.TrainingTypeService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeDAO dao;

    @Override
    @Transactional(readOnly = true)
    public List<TrainingTypeIdDTO> findAll() {
        List<TrainingType> trainingTypes = dao.findAll();
        return trainingTypes.stream()
                .map(trainingType -> TrainingTypeIdDTO.builder()
                        .id(trainingType.getId())
                        .trainingTypeName(trainingType.getTrainingTypeName())
                        .build())
                .toList();
    }
}
