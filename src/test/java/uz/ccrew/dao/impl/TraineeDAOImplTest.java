package uz.ccrew.dao.impl;

import org.springframework.test.annotation.DirtiesContext;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.*;
import uz.ccrew.config.TestAppConfig;
import uz.ccrew.config.TestHibernateConfig;
import uz.ccrew.config.TestDataSourceConfig;
import uz.ccrew.exp.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uz.ccrew.utils.UserUtils;

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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TraineeDAOImplTest {
    private Trainee trainee;
    @Autowired
    private TraineeDAO traineeDAO;
    @Autowired
    private UserUtils userUtils;

    @BeforeEach
    void setUp() {
        trainee = buildTrainee();
    }

    @Test
    @Rollback
    void create() {
        Long id = traineeDAO.create(trainee);
        assertNotNull(id);
    }

    @Test
    @Rollback
    void update() {
        String newAddress = "New Address";
        Long id = traineeDAO.create(trainee);
        trainee.setAddress(newAddress);
        traineeDAO.update(trainee);
        Optional<Trainee> traineeOptional = traineeDAO.findById(id);
        assertEquals(traineeOptional.get().getAddress(), newAddress);
    }

    @Test
    @Rollback
    void findByUsername() {
        traineeDAO.create(trainee);
        Optional<Trainee> traineeOptional = traineeDAO.findByUsername(trainee.getUser().getUsername());
        assertTrue(traineeOptional.isPresent());
    }

    @Test
    @Rollback
    void changePassword() {
        String newPassword = "New Password";
        Long id = traineeDAO.create(trainee);
        traineeDAO.changePassword(id, newPassword);
        Optional<Trainee> traineeOptional = traineeDAO.findById(id);
        assertEquals(traineeOptional.get().getUser().getPassword(), newPassword);
    }

    @Test
    @Rollback
    void activateDeactivate() {
        Long id = traineeDAO.create(trainee);
        traineeDAO.activateDeactivate(id, Boolean.FALSE);
        assertEquals(trainee.getUser().getIsActive(), Boolean.FALSE);
    }

    @Test
    @Rollback
    void findById() {
        Long id = traineeDAO.create(trainee);
        Optional<Trainee> foundOpt = traineeDAO.findById(id);
        assertTrue(foundOpt.isPresent());
        assertEquals("testUser", foundOpt.get().getUser().getUsername());
    }

    @Test
    @Rollback
    void findAll() {
        traineeDAO.create(trainee);
        List<Trainee> foundList = traineeDAO.findAll();
        assertEquals(1, foundList.size());
    }

    @Test
    @Rollback
    void delete() {
        Long id = traineeDAO.create(trainee);
        assertNotNull(id);
        traineeDAO.delete(id);

        Optional<Trainee> foundOpt = traineeDAO.findById(id);
        assertTrue(foundOpt.isEmpty());
    }

    @Test
    @Rollback
    void deleteNotFoundThrowsException() {
        assertThrows(EntityNotFoundException.class, () -> traineeDAO.delete(9999L));
    }

    private static Trainee buildTrainee() {
        return Trainee.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Test Address")
                .user(User.builder()
                        .firstName("Maksud")
                        .lastName("Rustamov")
                        .username("testUser")
                        .password("pass")
                        .isActive(Boolean.TRUE)
                        .build())
                .build();
    }
}
