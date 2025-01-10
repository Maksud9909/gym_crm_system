package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import uz.ccrew.config.TestAppConfig;
import uz.ccrew.config.TestDataSourceConfig;
import uz.ccrew.config.TestHibernateConfig;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.dto.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.service.TrainingService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestAppConfig.class,
        TestDataSourceConfig.class,
        TestHibernateConfig.class
})
@Transactional
@Sql(scripts = "/training-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TrainingServiceImplTest {
    private Training training;
    private UserCredentials userCredentials;

    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    @Autowired
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        userCredentials = new UserCredentials("admin1.admin1", "123");
        Trainee trainee = traineeDAO.findById(1L).get();
        Trainer trainer = trainerDAO.findById(1L).get();
        TrainingType trainingType = trainingTypeDAO.findById(1L).get();
        training = buildTraining(trainee, trainer, trainingType);
    }

    @Test
    void create() {
        Long id = trainingService.create(training);
        assertNotNull(id);
    }

    @Test
    void findById() {
        Long id = trainingService.create(training);
        Training foundTraining = trainingService.findById(1L, userCredentials);
        assertNotNull(foundTraining);
    }

    @Test
    void findAll() {
        trainingService.create(training);
        List<Training> trainingList = trainingService.findAll(userCredentials);
        assertEquals(trainingList.size(), 1);
    }

    private static Training buildTraining(Trainee trainee, Trainer trainer, TrainingType trainingType) {
        return Training.builder()
                .trainingDuration(30.0)
                .trainingName("Training Test Name")
                .trainingDate(LocalDate.now())
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(trainingType)
                .build();
    }
}
