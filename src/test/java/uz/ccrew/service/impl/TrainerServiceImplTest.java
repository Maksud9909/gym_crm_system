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
import uz.ccrew.dto.UserCredentials;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.entity.User;
import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.service.TrainerService;
import uz.ccrew.service.TrainingTypeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestAppConfig.class,
        TestDataSourceConfig.class,
        TestHibernateConfig.class
})
@Transactional
@Sql(scripts = "/trainer-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TrainerServiceImplTest {
    private Trainer trainer;
    private UserCredentials userCredentials;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setUp() {
        userCredentials = new UserCredentials("admin.admin", "123");
        trainer = buildTrainer(trainingTypeService.findById(1L, new UserCredentials("admin.admin", "123")));
    }

    @Test
    void create() {
        Long id = trainerService.create(trainer);
        assertNotNull(id);
    }

    @Test
    void update() {
        String newFirstName = "newFirstName";
        Long id = trainerService.create(trainer);
        trainer.getUser().setFirstName(newFirstName);
        trainerService.update(trainer, userCredentials);
        assertEquals(newFirstName, trainer.getUser().getFirstName());
    }

    @Test
    void findById() {
        Long id = trainerService.create(trainer);
        Trainer foundTrainer = trainerService.findById(id, userCredentials);
        assertEquals(foundTrainer.getUser().getUsername(), "Maksud.Rustamov");
    }

    @Test
    void findByUsername() {
        String username = "Maksud.Rustamov";
        trainerService.create(trainer);
        Trainer foundTrainer = trainerService.findByUsername(username, userCredentials);
        assertEquals(foundTrainer.getUser().getUsername(), "Maksud.Rustamov");
    }

    @Test
    void findAll() {
        trainerService.create(trainer);
        List<Trainer> trainers = trainerService.findAll(userCredentials);
        assertEquals(trainers.size(), 2);
    }

    @Test
    void delete() {
        Long id = trainerService.create(trainer);
        trainerService.delete(id, userCredentials);
        assertThrows(EntityNotFoundException.class,
                () -> trainerService.findById(id, userCredentials));
    }

    @Test
    void changePassword() {
        String newPassword = "newPassword";
        Long id = trainerService.create(trainer);
        trainer.getUser().setPassword(newPassword);
        trainerService.changePassword(id, newPassword, userCredentials);
        assertEquals(trainer.getUser().getPassword(), newPassword);
    }

    @Test
    void activateDeactivate() {
        Long id = trainerService.create(trainer);
        trainerService.activateDeactivate(id, Boolean.FALSE, userCredentials);
        assertEquals(trainer.getUser().getIsActive(), Boolean.FALSE);
    }

    private static Trainer buildTrainer(TrainingType trainingType) {
        return Trainer.builder()
                .user(
                        User.builder()
                                .firstName("Maksud")
                                .lastName("Rustamov")
                                .isActive(Boolean.TRUE)
                                .build()
                )
                .trainingType(trainingType)
                .build();
    }
}
