package uz.ccrew.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uz.ccrew.config.AppConfig;
import uz.ccrew.entity.Trainee;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import uz.ccrew.exp.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TraineeServiceTest {

    @Autowired
    private Map<Long, Trainee> traineeStorage;

    @Autowired
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        traineeStorage.clear();
        trainee = new Trainee();
        trainee.setFirstName("Maksud");
        trainee.setLastName("Rustamov");
        trainee.setDateOfBirth(LocalDate.of(2005, 1, 1));
    }

    @Test
    void create_ShouldAddTrainee() {
        Long id = traineeService.create(trainee);
        assertNotNull(id);
        Trainee savedTrainee = traineeService.findById(id);
        assertNotNull(savedTrainee);
        assertEquals("Maksud", savedTrainee.getFirstName());
        assertEquals("Rustamov", savedTrainee.getLastName());
    }

    @Test
    void findById_ShouldReturnTrainee() {
        Long id = traineeService.create(trainee);
        Trainee foundTrainee = traineeService.findById(id);
        assertNotNull(foundTrainee);
        assertEquals("Maksud", foundTrainee.getFirstName());
    }

    @Test
    void update_ShouldUpdateTrainee() {
        Long id = traineeService.create(trainee);
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setFirstName("Jane");
        updatedTrainee.setLastName("Smith");
        updatedTrainee.setDateOfBirth(LocalDate.of(1995, 5, 20));
        traineeService.update(id, updatedTrainee);
        Trainee savedTrainee = traineeService.findById(id);
        assertEquals("Jane", savedTrainee.getFirstName());
        assertEquals("Smith", savedTrainee.getLastName());
    }

    @Test
    void delete_ShouldRemoveTrainee() {
        Long id = traineeService.create(trainee);
        traineeService.delete(id);
        assertThrows(EntityNotFoundException.class, () -> traineeService.findById(id));
    }


    @Test
    void findAll_ShouldReturnAllTrainees() {
        traineeService.create(trainee);
        Trainee anotherTrainee = new Trainee();
        anotherTrainee.setFirstName("Alice");
        anotherTrainee.setLastName("Johnson");
        anotherTrainee.setDateOfBirth(LocalDate.of(1998, 3, 15));
        traineeService.create(anotherTrainee);
        List<Trainee> allTrainees = traineeService.findAll();
        assertEquals(2, allTrainees.size());
    }
}
