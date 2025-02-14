package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.dto.trainee.TraineeShortDTO;
import uz.ccrew.dto.trainer.*;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.entity.User;
import uz.ccrew.exp.exp.EntityNotFoundException;
import uz.ccrew.utils.UserUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @Mock
    private UserUtils userUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password123");
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

    @Test
    void getUnassignedTrainers_ShouldReturnUnassignedTrainers() {
        when(trainerDAO.getUnassignedTrainers("traineeUsername"))
                .thenReturn(List.of(trainer));

        List<TrainerDTO> result = trainerService.getUnassignedTrainers("traineeUsername");

        assertEquals(1, result.size());
        TrainerDTO trainerDTO = result.get(0);
        assertEquals("test_user", trainerDTO.getUsername());
        assertEquals("Test", trainerDTO.getFirstName());
        assertEquals("User", trainerDTO.getLastName());
        assertEquals("Yoga", trainerDTO.getTrainingTypeName());
    }

    @Test
    void getProfile_ShouldReturnTrainerProfile_WhenTrainerExists() {
        Trainee trainee = new Trainee();
        User traineeUser = User.builder().username("traineeUser").firstName("Trainee").lastName("User").build();
        trainee.setUser(traineeUser);

        Training training = new Training();
        training.setTrainee(trainee);

        trainer.setTraining(Collections.singletonList(training));

        when(trainerDAO.findByUsername("test_user")).thenReturn(Optional.of(trainer));

        TrainerProfileDTO profile = trainerService.getProfile("test_user");

        assertEquals("Test", profile.getFirstName());
        assertEquals("User", profile.getLastName());
        assertEquals("Yoga", profile.getTrainingTypeName());
        assertEquals(1, profile.getTrainees().size());

        TraineeShortDTO traineeDTO = profile.getTrainees().get(0);
        assertEquals("traineeUser", traineeDTO.getUsername());
        assertEquals("Trainee", traineeDTO.getFirstName());
        assertEquals("User", traineeDTO.getLastName());
    }

    @Test
    void getProfile_ShouldThrowException_WhenTrainerDoesNotExist() {
        when(trainerDAO.findByUsername("nonexistent_user")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainerService.getProfile("nonexistent_user"));
    }

    @Test
    void getTrainerTrainings_ShouldReturnTrainings() {
        Training training = new Training();
        training.setTrainingName("Yoga Class");
        training.setTrainingDate(LocalDate.of(2023, 1, 1));
        training.setTrainingDuration(60.0);

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");
        training.setTrainingType(trainingType);

        Trainee trainee = new Trainee();
        User traineeUser = User.builder().firstName("Trainee").build();
        trainee.setUser(traineeUser);
        training.setTrainee(trainee);

        when(trainingDAO.getTrainerTrainings("test_user", null, null, null))
                .thenReturn(List.of(training));

        List<TrainerTrainingDTO> result = trainerService.getTrainerTrainings("test_user", null, null, null);

        assertEquals(1, result.size());
        TrainerTrainingDTO dto = result.get(0);
        assertEquals("Yoga Class", dto.getTrainingName());
        assertEquals(LocalDate.of(2023, 1, 1), dto.getTrainingDate());
        assertEquals(60.0, dto.getTrainingDuration());
        assertEquals("Yoga", dto.getTrainingType());
        assertEquals("Trainee", dto.getTraineeName());
    }
}
