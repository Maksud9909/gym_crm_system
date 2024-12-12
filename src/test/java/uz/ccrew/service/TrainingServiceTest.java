package uz.ccrew.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uz.ccrew.config.AppConfig;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import uz.ccrew.exp.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TrainingServiceTest {

    @Autowired
    private TrainingService trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setTrainingName("Yoga Session");
        training.setTrainerId(1L);
        training.setTraineeId(2L);
        training.setTrainingType(TrainingType.GYM);
        training.setTrainingDate(LocalDate.of(2024, 12, 1));
    }

    @Test
    void create_ShouldAddTraining() {
        Long id = trainingService.create(training);
        assertNotNull(id);
        Training savedTraining = trainingService.findById(id);
        assertNotNull(savedTraining);
        assertEquals("Yoga Session", savedTraining.getTrainingName());
    }

    @Test
    void findById_ShouldReturnTraining() {
        Long id = trainingService.create(training);
        Training foundTraining = trainingService.findById(id);
        assertNotNull(foundTraining);
        assertEquals(training.getTrainingName(), foundTraining.getTrainingName());
    }

    @Test
    void findById_ShouldReturnNullIfNotFound() {
        assertThrows(EntityNotFoundException.class, () -> trainingService.findById(999L));
    }

    @Test
    void findAll_ShouldReturnAllTrainings() {
        trainingService.create(training);
        Training anotherTraining = new Training();
        anotherTraining.setTrainingName("Strength Training");
        anotherTraining.setTrainerId(3L);
        anotherTraining.setTraineeId(4L);
        anotherTraining.setTrainingType(TrainingType.RUN);
        anotherTraining.setTrainingDate(LocalDate.of(2024, 12, 2));
        trainingService.create(anotherTraining);

        List<Training> allTrainings = trainingService.findAll();
        assertEquals(4, allTrainings.size());
    }
}
