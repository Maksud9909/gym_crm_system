package uz.ccrew.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import uz.ccrew.config.AppConfig;
import uz.ccrew.entity.Trainer;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TrainerDAOTest {

    @Autowired
    private TrainerDAO trainerDAO;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setFirstName("Alice");
        trainer.setLastName("Smith");
        trainer.setSpecialization("GYM");
    }

    @Test
    void create_ShouldAddUniqueTrainer() {
        Long id = trainerDAO.create(trainer);
        assertNotNull(id);

        // the same username
        Trainer duplicateTrainer = new Trainer();
        duplicateTrainer.setFirstName("Alice");
        duplicateTrainer.setLastName("Smith");
        duplicateTrainer.setSpecialization("Table tennis");

        trainerDAO.create(duplicateTrainer);
        assertTrue(duplicateTrainer.getUsername().matches("Alice\\.Smith\\.\\d+")); // checking does it contain the next unique digit

    }

    @Test
    void findById_ShouldReturnTrainer() {
        Long id = trainerDAO.create(trainer);
        Trainer foundTrainer = trainerDAO.findById(id);
        assertNotNull(foundTrainer.getId());
        assertEquals("Alice", foundTrainer.getFirstName());
    }

    @Test
    void update() {
        Long id = trainerDAO.create(trainer);
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setFirstName("Taylor");
        updatedTrainer.setLastName("Swift");
        trainerDAO.update(id, updatedTrainer);
        Trainer savedTrainer = trainerDAO.findById(id);
        assertNotNull(savedTrainer.getFirstName());
        assertEquals("Taylor", savedTrainer.getFirstName());
        assertEquals("Swift", savedTrainer.getLastName());
    }

    @Test
    void delete() {
        Long id = trainerDAO.create(trainer);
        trainerDAO.delete(id);
        assertNull(trainerDAO.findById(id));
    }

    @Test
    void findAll() {
        trainerDAO.create(trainer);
        Trainer newTrainer = new Trainer();
        trainer.setFirstName("Taylor");
        trainer.setLastName("Swift");
        trainer.setSpecialization("Singing");
        trainerDAO.create(newTrainer);
        List<Trainer> trainerList = trainerDAO.findAll();
        assertNotNull(trainerList);
        assertEquals(2, trainerList.size());
    }
}