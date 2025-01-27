package uz.ccrew.controller;

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
}
