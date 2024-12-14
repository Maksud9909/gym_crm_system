package uz.ccrew.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uz.ccrew.config.AppConfig;
import uz.ccrew.entity.Trainer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import uz.ccrew.service.TrainerService;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TrainerServiceTest {

    @Autowired
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setFirstName("Alice");
        trainer.setLastName("Johnson");
        trainer.setSpecialization("Yoga");
    }

    @Test
    void create_ShouldAddTrainer() {
        Long id = trainerService.create(trainer);
        assertNotNull(id);
        Trainer savedTrainer = trainerService.findById(id);
        assertNotNull(savedTrainer);
        assertEquals("Alice", savedTrainer.getFirstName());
        assertEquals("Johnson", savedTrainer.getLastName());
    }

    @Test
    void findById_ShouldReturnTrainer() {
        Long id = trainerService.create(trainer);
        Trainer foundTrainer = trainerService.findById(id);
        assertNotNull(foundTrainer);
        assertEquals("Alice", foundTrainer.getFirstName());
    }

    @Test
    void update_ShouldUpdateTrainer() {
        Long id = trainerService.create(trainer);
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setFirstName("Bob");
        updatedTrainer.setLastName("Smith");
        updatedTrainer.setSpecialization("Strength Training");
        trainerService.update(id, updatedTrainer);
        Trainer savedTrainer = trainerService.findById(id);
        assertEquals("Bob", savedTrainer.getFirstName());
        assertEquals("Smith", savedTrainer.getLastName());
    }

    @Test
    void findAll_ShouldReturnAllTrainers() {
        trainerService.create(trainer);
        Trainer anotherTrainer = new Trainer();
        anotherTrainer.setFirstName("John");
        anotherTrainer.setLastName("Doe");
        anotherTrainer.setSpecialization("Pilates");
        trainerService.create(anotherTrainer);
        List<Trainer> allTrainers = trainerService.findAll();
        assertEquals(4, allTrainers.size());
    }
}
