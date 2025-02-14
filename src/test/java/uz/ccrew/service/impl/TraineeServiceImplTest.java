package uz.ccrew.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import uz.ccrew.dto.trainee.*;
import uz.ccrew.dto.training.TrainingTrainerUpdateDTO;
import uz.ccrew.entity.*;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.exp.exp.EntityNotFoundException;
import uz.ccrew.exp.exp.TrainingNotAssociatedException;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UserUtils userUtils;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private User user;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("test_user")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .isActive(Boolean.TRUE)
                .build();

        trainee = Trainee.builder()
                .id(1L)
                .address("123 Main St")
                .user(user)
                .training(new ArrayList<>())
                .build();
    }

    @Test
    void createTrainee_ShouldReturnUserCredentials_WhenValidDataProvided() {
        when(userUtils.generateUniqueUsername("John", "Doe")).thenReturn("John.Doe");
        when(userUtils.generateRandomPassword()).thenReturn("password123");
        when(passwordEncoder.encode("password123")).thenReturn("password123");

        TraineeCreateDTO dto = TraineeCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .datOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        UserCredentials credentials = traineeService.create(dto);

        assertEquals("John.Doe", credentials.getUsername());
        assertEquals("password123", credentials.getPassword());
    }

    @Test
    void updateTrainee_ShouldUpdateTrainee_WhenExists() {
        when(traineeDAO.findByUsername("test_user")).thenReturn(Optional.of(trainee));

        TraineeUpdateDTO dto = TraineeUpdateDTO.builder()
                .username("test_user")
                .firstName("Updated")
                .lastName("User")
                .address("456 Updated St")
                .datOfBirth(LocalDate.of(1991, 1, 1))
                .isActive(Boolean.FALSE)
                .build();

        traineeService.update(dto);

        assertEquals("Updated", trainee.getUser().getFirstName());
        assertEquals("User", trainee.getUser().getLastName());
        assertEquals("456 Updated St", trainee.getAddress());
        assertEquals(LocalDate.of(1991, 1, 1), trainee.getDateOfBirth());
        assertFalse(trainee.getUser().getIsActive());
        verify(traineeDAO, times(1)).update(trainee);
    }

    @Test
    void updateTrainee_ShouldThrowException_WhenTraineeDoesNotExist() {
        when(traineeDAO.findByUsername("nonexistent_user")).thenReturn(Optional.empty());

        TraineeUpdateDTO dto = TraineeUpdateDTO.builder()
                .username("nonexistent_user")
                .build();

        assertThrows(EntityNotFoundException.class, () -> traineeService.update(dto));
    }

    @Test
    void deleteTraineeByUsername_ShouldDeleteTrainee_WhenExists() {
        when(traineeDAO.findByUsername("test_user")).thenReturn(Optional.of(trainee));

        traineeService.deleteTraineeByUsername("test_user");

        verify(traineeDAO, times(1)).delete(1L);
    }

    @Test
    void deleteTraineeByUsername_ShouldThrowException_WhenTraineeDoesNotExist() {
        when(traineeDAO.findByUsername("nonexistent_user")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> traineeService.deleteTraineeByUsername("nonexistent_user"));
        verify(traineeDAO, never()).delete(anyLong());
    }

    @Test
    void updateTraineeTrainers_SuccessfulUpdate() {
        UpdateTraineeTrainersDTO dto = UpdateTraineeTrainersDTO.builder()
                .traineeUsername("validTrainee")
                .trainingTrainers(List.of(TrainingTrainerUpdateDTO.builder()
                        .trainingId(1L)
                        .trainerUsername("validTrainer")
                        .trainingName("Updated Training Name")
                        .build()))
                .build();

        Trainee trainee = new Trainee();
        Training training = new Training();
        training.setId(1L);
        trainee.setTraining(List.of(training));

        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Cardio");
        trainer.setTrainingType(trainingType);
        User trainerUser = new User();
        trainerUser.setUsername("validTrainer");
        trainerUser.setFirstName("John");
        trainerUser.setLastName("Doe");
        trainer.setUser(trainerUser);

        when(traineeDAO.findByUsername("validTrainee")).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("validTrainer")).thenReturn(Optional.of(trainer));
        when(trainingDAO.findById(1L)).thenReturn(Optional.of(training));

        List<TrainerDTO> result = traineeService.updateTraineeTrainers(dto);

        assertEquals(1, result.size());
        TrainerDTO trainerDTO = result.get(0);
        assertEquals("validTrainer", trainerDTO.getUsername());
        assertEquals("John", trainerDTO.getFirstName());
        assertEquals("Doe", trainerDTO.getLastName());
        assertEquals("Cardio", trainerDTO.getTrainingTypeName());
    }

    @Test
    void updateTraineeTrainers_ThrowsTrainingNotAssociatedException() {
        UpdateTraineeTrainersDTO dto = UpdateTraineeTrainersDTO.builder()
                .traineeUsername("trainee1")
                .trainingTrainers(List.of(TrainingTrainerUpdateDTO.builder()
                        .trainingId(1L)
                        .trainerUsername("trainer1")
                        .build()))
                .build();
        Trainee trainee = new Trainee();
        trainee.setTraining(new ArrayList<>());
        Training training = new Training();
        training.setId(1L);
        when(traineeDAO.findByUsername("trainee1")).thenReturn(Optional.of(trainee));
        when(trainingDAO.findById(1L)).thenReturn(Optional.of(training));
        TrainingNotAssociatedException exception = assertThrows(TrainingNotAssociatedException.class, () -> {
            traineeService.updateTraineeTrainers(dto);
        });
        assertEquals("Training with ID=1 is not associated with Trainee=trainee1", exception.getMessage());
    }


    @Test
    void getProfile() {
        String username = "test_user";

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.of(trainee));

        TraineeProfileDTO result = traineeService.getProfile(username);
        assertEquals("Test", result.getFirstName());
    }

    @Test
    void getTraineeTrainings() {
        String username = "testUser";
        LocalDate periodFrom = LocalDate.of(2023, 1, 1);
        LocalDate periodTo = LocalDate.of(2023, 12, 31);
        String trainerName = "John Doe";
        String trainingTypeName = "Cardio";

        User trainerUser = User.builder()
                .firstName("John")
                .build();

        Trainer trainer = Trainer.builder()
                .user(trainerUser)
                .build();

        TrainingType trainingType = TrainingType.builder()
                .trainingTypeName("Cardio")
                .build();

        Training mockTraining = Training.builder()
                .trainingName("Morning Cardio")
                .trainingDate(LocalDate.of(2023, 5, 15))
                .trainingDuration(1.5)
                .trainer(trainer)
                .trainingType(trainingType)
                .build();

        List<Training> mockTrainings = List.of(mockTraining);

        when(trainingDAO.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingTypeName))
                .thenReturn(mockTrainings);

        List<TraineeTrainingDTO> trainingDTOS = traineeService.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingTypeName);

        assertEquals(1, trainingDTOS.size());
        TraineeTrainingDTO dto = trainingDTOS.get(0);
        assertEquals("Morning Cardio", dto.getTrainingName());
        assertEquals(LocalDate.of(2023, 5, 15), dto.getTrainingDate());
        assertEquals("Cardio", dto.getTrainingType());
        assertEquals(1.5, dto.getTrainingDuration());
        assertEquals("John", dto.getTrainerName());
    }

}
