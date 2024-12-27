package uz.ccrew.service;

import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.service.base.base.AbstractBaseService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.time.LocalDate;

@Slf4j
@Service
public class TrainingService extends AbstractBaseService<Training> {
    private static final String ENTITY_NAME = "Training";

    private final TrainingDAO trainingDAO;

    public TrainingService(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
        log.debug("TrainingServiceImpl initialized");
    }

    @Autowired
    public void setDao(TrainingDAO dao) {
        super.setDao(dao);
        log.debug("TrainingDAO injected into TrainingServiceImpl");
    }

    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        log.info("Fetching trainings for Trainee username={} with filters", traineeUsername);
        return trainingDAO.getTraineeTrainings(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
