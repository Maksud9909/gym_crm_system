package uz.ccrew.dao.impl;

import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainer;
import uz.ccrew.config.TestAppConfig;
import uz.ccrew.config.TestHibernateConfig;
import uz.ccrew.config.TestDataSourceConfig;
import uz.ccrew.exp.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.jdbc.Sql;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestAppConfig.class,
        TestDataSourceConfig.class,
        TestHibernateConfig.class
})
@Transactional
@Sql(scripts = "/training-type-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TrainerDAOImplTest {
    private Trainer trainer;
    @Autowired
    private TrainerDAO trainerDAO;
    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    @BeforeEach
    void setUp() {
        trainer = buildTrainer(trainingTypeDAO.findById(1L).get());
    }

    @Test
    @Rollback
    void create() {
        Long id = trainerDAO.create(trainer);
        assertNotNull(id, "ID after create() should not be null");
    }

    @Test
    @Rollback
    void update() {
        Long id = trainerDAO.create(trainer);
        trainer.setTrainingType(trainingTypeDAO.findById(2L).get());
        trainerDAO.update(trainer);

        Optional<Trainer> updatedOpt = trainerDAO.findById(id);
        assertTrue(updatedOpt.isPresent());
        assertEquals(updatedOpt.get().getTrainingType().getId(), 2L);
    }

    @Test
    @Rollback
    void findByUsername() {
        trainerDAO.create(trainer);
        Optional<Trainer> opt = trainerDAO.findByUsername(trainer.getUser().getUsername());
        assertTrue(opt.isPresent());
    }

    @Test
    @Rollback
    void changePassword() {
        Long id = trainerDAO.create(trainer);
        String newPassword = "NewPassword123";
        trainerDAO.changePassword(id, newPassword);

        Optional<Trainer> updatedOpt = trainerDAO.findById(id);
        assertTrue(updatedOpt.isPresent());
        assertEquals(newPassword, updatedOpt.get().getUser().getPassword());
    }

    @Test
    @Rollback
    void activateDeactivate() {
        Long id = trainerDAO.create(trainer);
        trainerDAO.activateDeactivate(id, false);

        Optional<Trainer> updatedOpt = trainerDAO.findById(id);
        assertTrue(updatedOpt.isPresent());
        assertFalse(updatedOpt.get().getUser().getIsActive());
    }

    @Test
    @Rollback
    void findById() {
        Long id = trainerDAO.create(trainer);
        Optional<Trainer> foundOpt = trainerDAO.findById(id);
        assertTrue(foundOpt.isPresent());
        assertEquals("trainerUser", foundOpt.get().getUser().getUsername());
    }

    @Test
    @Rollback
    void findAll() {
        trainerDAO.create(trainer);
        List<Trainer> allTrainers = trainerDAO.findAll();
        assertEquals(allTrainers.size(), 1L);
    }

    @Test
    @Rollback
    void delete() {
        Long id = trainerDAO.create(trainer);
        trainerDAO.delete(id);

        Optional<Trainer> foundOpt = trainerDAO.findById(id);
        assertTrue(foundOpt.isEmpty());
    }

    @Test
    @Rollback
    void deleteNotFoundThrowsException() {
        assertThrows(EntityNotFoundException.class, () -> trainerDAO.delete(9999L));
    }

    private static Trainer buildTrainer(TrainingType trainingType) {
        return Trainer.builder()
                .user(
                        User.builder()
                                .firstName("TrainerFirst")
                                .lastName("TrainerLast")
                                .username("trainerUser")
                                .password("pass")
                                .isActive(Boolean.TRUE)
                                .build()
                )
                .trainingType(trainingType)
                .build();
    }
}
