package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.dto.trainer.TrainerUpdateDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.entity.User;
import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.utils.UserUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private User user;
    private Trainer trainer;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("test_user")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .build();

        trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("Yoga");

        trainer = Trainer.builder()
                .id(1L)
                .user(user)
                .trainingType(trainingType)
                .training(new ArrayList<>())
                .build();
    }

    @Test
    void createTrainer_ShouldReturnUserCredentials_WhenValidDataProvided() {
        when(userUtils.generateUniqueUsername("Jane", "Doe")).thenReturn("Jane.Doe");
        when(userUtils.generateRandomPassword()).thenReturn("password123");
        when(trainingTypeDAO.findByName("Yoga")).thenReturn(Optional.of(trainingType));

        TrainerCreateDTO dto = TrainerCreateDTO.builder()
                .firstName("Jane")
                .lastName("Doe")
                .trainingTypeName("Yoga")
                .build();

        UserCredentials credentials = trainerService.create(dto);

        assertEquals("Jane.Doe", credentials.getUsername());
        assertEquals("password123", credentials.getPassword());
    }

    @Test
    void createTrainer_ShouldThrowException_WhenTrainingTypeNotFound() {
        when(userUtils.generateUniqueUsername("Jane", "Doe")).thenReturn("Jane.Doe");
        when(trainingTypeDAO.findByName("NonexistentType")).thenReturn(Optional.empty());

        TrainerCreateDTO dto = TrainerCreateDTO.builder()
                .firstName("Jane")
                .lastName("Doe")
                .trainingTypeName("NonexistentType")
                .build();

        assertThrows(EntityNotFoundException.class, () -> trainerService.create(dto));
    }

    @Test
    void activateDeactivate_ShouldActivateTrainer_WhenExists() {
        when(userDAO.findByUsername("test_user")).thenReturn(Optional.of(user));
        user.setIsActive(Boolean.FALSE);

        trainerService.activateDeactivate("test_user", Boolean.TRUE);

        verify(userDAO, times(1)).activateDeactivate("test_user", true);
    }

    @Test
    void updateTrainer_ShouldUpdateTrainer_WhenExists() {
        when(trainerDAO.findByUsername("test_user")).thenReturn(Optional.of(trainer));

        TrainerUpdateDTO dto = TrainerUpdateDTO.builder()
                .username("test_user")
                .firstName("Updated")
                .lastName("Trainer")
                .isActive(false)
                .build();

        trainerService.update(dto);

        assertEquals("Updated", trainer.getUser().getFirstName());
        assertEquals("Trainer", trainer.getUser().getLastName());
        assertFalse(trainer.getUser().getIsActive());
        verify(trainerDAO, times(1)).update(trainer);
    }

    @Test
    void updateTrainer_ShouldThrowException_WhenTrainerDoesNotExist() {
        when(trainerDAO.findByUsername("nonexistent_user")).thenReturn(Optional.empty());

        TrainerUpdateDTO dto = TrainerUpdateDTO.builder()
                .username("nonexistent_user")
                .build();

        assertThrows(EntityNotFoundException.class, () -> trainerService.update(dto));
    }
}
