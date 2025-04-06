package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.dto.summary.MonthsDTO;
import uz.ccrew.dto.summary.TrainerMonthlySummaryDTO;
import uz.ccrew.dto.training.TrainerWorkloadDTO;
import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.dto.summary.YearsDTO;
import uz.ccrew.entity.*;
import uz.ccrew.exp.exp.EntityNotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
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
    private JmsTemplate jmsTemplate;

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
                .user(new User())
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
                .trainee(trainee)
                .trainingDate(LocalDateTime.now())
                .trainingDuration(2.0)
                .build();

        trainerMonthlySummaryDTO = TrainerMonthlySummaryDTO.builder()
                .trainerUsername("test")
                .trainerLastName("test")
                .trainerFirstName("test")
                .isActive(Boolean.TRUE)
                .years(List.of(YearsDTO.builder()
                        .year(2025)
                        .months(List.of(MonthsDTO.builder()
                                .month(Month.JANUARY)
                                .totalDuration(60)
                                .build()))
                        .build()))
                .build();

        ReflectionTestUtils.setField(trainingService, "queueName", "trainer.workload.queue");
    }

    @Test
    void testAddTraining_sendsMessageToQueue() {
        when(traineeDAO.findByUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername(trainer.getUser().getUsername())).thenReturn(Optional.of(trainer));
        TrainingDTO trainingDTO = TrainingDTO.builder()
                .trainerUsername(trainer.getUser().getUsername())
                .traineeUsername(trainee.getUser().getUsername())
                .trainingName(training.getTrainingName())
                .trainingDuration(training.getTrainingDuration())
                .trainingDate(training.getTrainingDate())
                .build();
        trainingService.addTraining(trainingDTO);
        ArgumentCaptor<TrainerWorkloadDTO> captor = ArgumentCaptor.forClass(TrainerWorkloadDTO.class);

        verify(jmsTemplate).convertAndSend(eq("trainer.workload.queue"), captor.capture());
        TrainerWorkloadDTO trainerWorkloadDTO = captor.getValue();
        assertEquals(trainer.getUser().getUsername(), trainerWorkloadDTO.getTrainerUsername());
    }

    @Test
    void addTraining_ShouldCreateTraining_WhenValidDataProvided() {
        when(traineeDAO.findByUsername("trainee_user")).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("trainer_user")).thenReturn(Optional.of(trainer));

        TrainingDTO dto = TrainingDTO.builder()
                .traineeUsername("trainee_user")
                .trainerUsername("trainer_user")
                .trainingName("Morning Yoga")
                .trainingDate(LocalDateTime.now())
                .trainingDuration(20.0)
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
                .trainingDate(LocalDateTime.now())
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
                .trainingDate(LocalDateTime.now())
                .trainingDuration(2.0)
                .build();

        assertThrows(EntityNotFoundException.class, () -> trainingService.addTraining(dto));
        verify(trainingDAO, never()).create(any(Training.class));
    }
}
