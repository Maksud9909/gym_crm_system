package uz.ccrew.service.impl;

import uz.ccrew.config.TestAppConfig;
import uz.ccrew.config.TestDataSourceConfig;
import uz.ccrew.config.TestHibernateConfig;
import uz.ccrew.dto.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.User;
import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.service.TraineeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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
@Sql(scripts = "/trainee-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TraineeServiceImplTest {
    private Trainee trainee;
    private UserCredentials userCredentials;
    @Autowired
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        userCredentials = new UserCredentials("admin.admin", "123");
        trainee = buildTrainee();
    }

    @Test
    void create() {
        Long id = traineeService.create(trainee);
        assertNotNull(id);
    }

    @Test
    void update() {
        Long id = traineeService.create(trainee);
        trainee.setAddress("New Address 1");
        traineeService.update(trainee, userCredentials);
        assertEquals(trainee.getAddress(), "New Address 1");
    }

    @Test
    void findById() {
        Long id = traineeService.create(trainee);
        Trainee foundTrainee = traineeService.findById(id, userCredentials);
        assertEquals(foundTrainee.getUser().getUsername(), "Maksud.Rustamov");
    }

    @Test
    void findByUsername() {
        traineeService.create(trainee);
        Trainee foundTrainee = traineeService.findByUsername("Maksud.Rustamov", userCredentials);
        assertEquals(foundTrainee.getUser().getUsername(), "Maksud.Rustamov");
    }

    @Test
    void findAll() {
        traineeService.create(trainee);
        List<Trainee> foundTrainees = traineeService.findAll(userCredentials);
        assertEquals(foundTrainees.size(), 2);
    }

    @Test
    void delete() {
        Long id = traineeService.create(trainee);
        traineeService.delete(id, userCredentials);
        assertThrows(EntityNotFoundException.class,
                () -> traineeService.findById(id, userCredentials));
    }

    @Test
    void deleteTraineeByUsername() {
        Long id = traineeService.create(trainee);
        traineeService.deleteTraineeByUsername(trainee.getUser().getUsername(), new UserCredentials("admin.admin", "123"));
        assertThrows(EntityNotFoundException.class,
                () -> traineeService.findById(id, userCredentials));
    }

    @Test
    void changePassword() {
        String newPassword = "New Password";
        Long id = traineeService.create(trainee);
        traineeService.changePassword(id, newPassword, userCredentials);
        assertEquals(trainee.getUser().getPassword(), newPassword);
    }

    @Test
    void activateDeactivate() {
        Long id = traineeService.create(trainee);
        traineeService.activateDeactivate(id, Boolean.FALSE, userCredentials);
        assertEquals(trainee.getUser().getIsActive(), Boolean.FALSE);
    }

    private static Trainee buildTrainee() {
        return Trainee.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Test Address")
                .user(User.builder()
                        .firstName("Maksud")
                        .lastName("Rustamov")
                        .isActive(Boolean.TRUE)
                        .build())
                .build();
    }
}
