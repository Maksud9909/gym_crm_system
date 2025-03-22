package uz.ccrew.controller;

import uz.ccrew.dto.training.TrainerMonthlySummaryDTO;
import uz.ccrew.service.TrainingService;
import uz.ccrew.dto.training.TrainingDTO;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {
    @Mock
    private TrainingService trainingService;
    @InjectMocks
    private TrainingController trainingController;

    @Test
    void addTraining() {
        TrainingDTO trainingDTO = TrainingDTO.builder()
                .trainingDate(LocalDate.now())
                .trainingDuration(90.0)
                .trainingName("Training Name")
                .traineeUsername("Trainee username")
                .trainerUsername("Trainer username")
                .build();

        ResponseEntity<?> response = trainingController.addTraining(trainingDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trainingService, times(1)).addTraining(trainingDTO);
    }

    @Test
    void deleteTraining() {
        Long trainingId = 1L;

        ResponseEntity<?> response = trainingController.deleteTraining(trainingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trainingService, times(1)).deleteTraining(trainingId);
    }

    @Test
    void getMonthlyWorkload() {
        String username = "trainer123";
        int year = 2025;
        int month = 3;
        TrainerMonthlySummaryDTO mockSummary = new TrainerMonthlySummaryDTO();

        when(trainingService.getMonthlyWorkload(username, year, month)).thenReturn(mockSummary);

        ResponseEntity<TrainerMonthlySummaryDTO> response = trainingController.getMonthlyWorkload(username, year, month);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockSummary, response.getBody());
        verify(trainingService, times(1)).getMonthlyWorkload(username, year, month);
    }
}
