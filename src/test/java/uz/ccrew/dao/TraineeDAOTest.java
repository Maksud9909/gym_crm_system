package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;
import uz.ccrew.config.AppConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TraineeDAOTest {

    @Autowired
    private TraineeDAO traineeDAO;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
    }

    @Test
    void create_ShouldAddUniqueTrainee() {
        Long id = traineeDAO.create(trainee);
        assertNotNull(id);

        // the same username
        Trainee duplicateTrainee = new Trainee();
        duplicateTrainee.setFirstName("John");
        duplicateTrainee.setLastName("Doe");
        duplicateTrainee.setDateOfBirth(LocalDate.of(1995, 5, 15));

        traineeDAO.create(duplicateTrainee);
        assertTrue(duplicateTrainee.getUsername().matches("John\\.Doe\\.\\d+"));
    }


    @Test
    void findById_ShouldReturnTrainee() {
        Long id = traineeDAO.create(trainee);
        Optional<Trainee> foundTrainee = traineeDAO.findById(id);
        assertNotNull(foundTrainee.get().getId());
        assertEquals("John", foundTrainee.get().getFirstName());
    }

    @Test
    void update_ShouldUpdateTrainee() {
        Long id = traineeDAO.create(trainee);
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setFirstName("Jane");
        updatedTrainee.setLastName("Smith");
        traineeDAO.update(id, updatedTrainee);
        Optional<Trainee> savedTrainee = traineeDAO.findById(id);
        assertEquals("Jane", savedTrainee.get().getFirstName());
        assertEquals("Smith", savedTrainee.get().getLastName());
    }

    @Test
    void delete_ShouldRemoveTrainee() {
        Long id = traineeDAO.create(trainee);
        traineeDAO.delete(id);
        assertTrue(traineeDAO.findById(id).isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainees() {
        traineeDAO.create(trainee);
        Trainee anotherTrainee = new Trainee();
        anotherTrainee.setFirstName("Alice");
        anotherTrainee.setLastName("Johnson");
        traineeDAO.create(anotherTrainee);
        List<Trainee> allTrainees = traineeDAO.findAll();
        assertEquals(2, allTrainees.size());
    }
}
