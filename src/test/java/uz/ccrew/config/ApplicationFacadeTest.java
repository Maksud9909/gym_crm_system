package uz.ccrew.config;

import org.springframework.test.annotation.DirtiesContext;
import uz.ccrew.entity.Trainee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApplicationFacadeTest {

    @Autowired
    private ApplicationFacade applicationFacade;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
    }

    @Test
    void createTrainee_ShouldDelegateToService() {
        Long id = applicationFacade.createTrainee(trainee);
        assertNotNull(id);

        Optional<Trainee> savedTrainee = applicationFacade.getTrainee(id);
        assertNotNull(savedTrainee);
        assertEquals("John", savedTrainee.get().getFirstName());
    }

    @Test
    void getAllTrainees_ShouldReturnAllTrainees() {
        applicationFacade.createTrainee(trainee);

        Trainee anotherTrainee = new Trainee();
        anotherTrainee.setFirstName("Alice");
        anotherTrainee.setLastName("Smith");
        applicationFacade.createTrainee(anotherTrainee);

        List<Trainee> allTrainees = applicationFacade.getAllTrainees();
        assertEquals(2, allTrainees.size());
    }
}
