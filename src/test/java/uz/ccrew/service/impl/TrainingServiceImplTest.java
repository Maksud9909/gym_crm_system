package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.exp.EntityNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");
        trainingType.setId(1L);

        trainee = Trainee.builder()
                .id(1L)
                .address("123 Main St")
                .user(null)
                .training(null)
                .build();

        trainer = Trainer.builder()
                .id(1L)
                .user(null)
                .trainingType(trainingType)
                .build();
    }

    @Test
    void addTraining_ShouldCreateTraining_WhenValidDataProvided() {
        when(traineeDAO.findByUsername("trainee_user")).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("trainer_user")).thenReturn(Optional.of(trainer));

        TrainingDTO dto = TrainingDTO.builder()
                .traineeUsername("trainee_user")
                .trainerUsername("trainer_user")
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDuration(2.0)
                .build();

        trainingService.addTraining(dto);

        verify(trainingDAO, times(1)).create(any(Training.class));
    }

    @Test
    void addTraining_ShouldThrowException_WhenTraineeNotFound() {
        when(traineeDAO.findByUsername("nonexistent_trainee")).thenReturn(Optional.empty());

        TrainingDTO dto = TrainingDTO.builder()
                .traineeUsername("nonexistent_trainee")
                .trainerUsername("trainer_user")
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDuration(2.0)
                .build();

        assertThrows(EntityNotFoundException.class, () -> trainingService.addTraining(dto));
        verify(trainingDAO, never()).create(any(Training.class));
    }

    @Test
    void addTraining_ShouldThrowException_WhenTrainerNotFound() {
        when(traineeDAO.findByUsername("trainee_user")).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("nonexistent_trainer")).thenReturn(Optional.empty());

        TrainingDTO dto = TrainingDTO.builder()
                .traineeUsername("trainee_user")
                .trainerUsername("nonexistent_trainer")
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDuration(2.0)
                .build();

        assertThrows(EntityNotFoundException.class, () -> trainingService.addTraining(dto));
        verify(trainingDAO, never()).create(any(Training.class));
    }
}
