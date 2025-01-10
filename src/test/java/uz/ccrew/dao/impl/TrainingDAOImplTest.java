package uz.ccrew.dao.impl;

import uz.ccrew.entity.*;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.config.TestAppConfig;
import uz.ccrew.config.TestHibernateConfig;
import uz.ccrew.config.TestDataSourceConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.jdbc.Sql;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestAppConfig.class,
        TestDataSourceConfig.class,
        TestHibernateConfig.class
})
@Transactional
@Sql(scripts = "/training-type-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TrainingDAOImplTest {
    private Training training;
    @Autowired
    private TraineeDAO traineeDAO;
    @Autowired
    private TrainerDAO trainerDAO;
    @Autowired
    private TrainingDAO trainingDAO;
    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    @BeforeEach
    void setUp() {
        Trainee trainee = buildTrainee();
        traineeDAO.create(trainee);
        Trainer trainer = buildTrainer(trainingTypeDAO.findById(2L).get());
        trainerDAO.create(trainer);
        training = Training.builder()
                .trainer(trainer)
                .trainee(trainee)
                .trainingType(trainingTypeDAO.findById(2L).get())
                .trainingName("Test training name")
                .trainingDuration(120.0)
                .trainingDate(LocalDate.now())
                .build();
    }

    @Test
    void create() {
        Long id = trainingDAO.create(training);
        assertNotNull(id);
    }

    @Test
    void findById() {
        Long id = trainingDAO.create(training);
        Optional<Training> foundTraining = trainingDAO.findById(id);
        assertTrue(foundTraining.isPresent());
    }

    @Test
    void findAll() {
        trainingDAO.create(training);
        List<Training> trainings = trainingDAO.findAll();
        assertEquals(trainings.size(), 1);
    }

    private static Trainer buildTrainer(TrainingType trainingType) {
        return Trainer.builder()
                .user(User.builder()
                        .firstName("Test trainer name")
                        .lastName("Test trainer last name")
                        .username("Test Trainer username")
                        .password("Test Trainer password")
                        .isActive(Boolean.TRUE)
                        .build())
                .trainingType(trainingType)
                .build();
    }

    private static Trainee buildTrainee() {
        return Trainee.builder()
                .user(User.builder()
                        .firstName("Test trainee name")
                        .lastName("Test trainee last name")
                        .username("Test trainee username")
                        .password("Test trainee password")
                        .isActive(Boolean.TRUE)
                        .build())
                .address("Test trainee address")
                .dateOfBirth(LocalDate.now())
                .build();
    }
}
