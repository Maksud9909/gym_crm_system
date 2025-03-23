package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dto.training.summary.TrainerMonthlySummaryDTO;
import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.entity.*;
import uz.ccrew.exp.exp.EntityNotFoundException;
import uz.ccrew.service.TrainerWorkloadClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainerWorkloadClient trainerWorkloadClient;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private Training training;
    private TrainerMonthlySummaryDTO trainerMonthlySummaryDTO;

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
                .user(User.builder()
                        .username("test")
                        .lastName("test")
                        .isActive(Boolean.TRUE)
                        .build())
                .trainingType(trainingType)
                .build();

        training = Training.builder()
                .id(1L)
                .trainer(trainer)
                .trainingDate(LocalDate.now())
                .trainingDuration(2.0)
                .build();

        trainerMonthlySummaryDTO.builder()
                .trainerUsername("test")
                .trainerLastName("test")
                .trainerFirstName("test")
                .isActive(Boolean.TRUE)
                .years(new ArrayList<>())
                .build();
    }

    @Test
    void deleteTraining_ShouldDeleteTraining_WhenTrainingExists() {
        when(trainingDAO.findById(1L)).thenReturn(Optional.of(training));

        trainingService.deleteTraining(1L);

        verify(trainingDAO, times(1)).delete(training);
        verify(trainerWorkloadClient, times(1)).sendTrainingData(any());
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

    @Test
    void getMonthlyWorkload() {
        ResponseEntity<List<TrainerMonthlySummaryDTO>> summaryDTO = new ResponseEntity<>(HttpStatusCode.valueOf(200));
        when(trainerWorkloadClient.getMonthlyWorkload(trainer.getUser().getUsername())).thenReturn(summaryDTO);
        trainingService.getMonthlyWorkload(trainer.getUser().getUsername());
    }
}
