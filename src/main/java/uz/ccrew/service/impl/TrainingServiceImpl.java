package uz.ccrew.service.impl;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.service.TrainingService;
import uz.ccrew.service.base.base.AbstractBaseService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;

@Slf4j
@Service
public class TrainingServiceImpl extends AbstractBaseService<Training> implements TrainingService {
    private static final String ENTITY_NAME = "Training";

    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingServiceImpl(TrainingDAO trainingDAO) {
        super(trainingDAO);
        this.trainingDAO = trainingDAO;
        log.debug("TrainingServiceImpl initialized");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, Long trainingTypeId) {
        log.info("Fetching trainings for Trainee username={} with filters", traineeUsername);
        return trainingDAO.getTraineeTrainings(traineeUsername, fromDate, toDate, trainerName, trainingTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        log.info("Fetching trainings for Trainer username={} with filters", trainerUsername);
        return trainingDAO.getTrainerTrainings(trainerUsername, fromDate, toDate, traineeName);
    }


    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
