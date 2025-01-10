package uz.ccrew.config;

import uz.ccrew.service.TraineeService;
import uz.ccrew.service.TrainerService;
import uz.ccrew.service.TrainingService;
import uz.ccrew.service.TrainingTypeService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Getter
@Component
public class ApplicationFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public ApplicationFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService, TrainingTypeService trainingTypeService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        log.info("ApplicationFacade initialized with services");
    }
}
