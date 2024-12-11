package uz.ccrew.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uz.ccrew.config.AppConfig;
import uz.ccrew.entity.Training;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import uz.ccrew.entity.TrainingType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TrainingDAOTest {

    @Autowired
    private TrainingDAO trainingDAO;

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
        Long id = trainingDAO.create(training);
        assertNotNull(id);
        Optional<Training> savedTraining = trainingDAO.findById(id);
        assertNotNull(savedTraining);
        assertEquals("Yoga Session", savedTraining.get().getTrainingName());
    }

    @Test
    void findById_ShouldReturnTraining() {
        Long id = trainingDAO.create(training);
        Optional<Training> foundTraining = trainingDAO.findById(id);
        assertNotNull(foundTraining);
        assertEquals(training.getTrainingName(), foundTraining.get().getTrainingName());
    }

    @Test
    void findById_ShouldReturnNullIfNotFound() {
        Optional<Training> foundTraining = trainingDAO.findById(999L);
        assertTrue(foundTraining.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainings() {
        trainingDAO.create(training);
        Training anotherTraining = new Training();
        anotherTraining.setTrainingName("Strength Training");
        anotherTraining.setTrainerId(3L);
        anotherTraining.setTraineeId(4L);
        anotherTraining.setTrainingType(TrainingType.RUN);
        anotherTraining.setTrainingDate(LocalDate.of(2024, 12, 2));
        trainingDAO.create(anotherTraining);

        List<Training> allTrainings = trainingDAO.findAll();
        assertEquals(2, allTrainings.size());
    }
}
