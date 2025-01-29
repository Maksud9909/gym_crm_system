package uz.ccrew.controller;

import uz.ccrew.service.TrainingTypeService;
import uz.ccrew.dto.trainingType.TrainingTypeIdDTO;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Test
    public void testGetTrainingTypes() {
        List<TrainingTypeIdDTO> mockTrainingTypes = Arrays.asList(
                new TrainingTypeIdDTO(1L, "Yoga"),
                new TrainingTypeIdDTO(2L, "Pilates"),
                new TrainingTypeIdDTO(3L, "Cardio")
        );

        when(trainingTypeService.findAll()).thenReturn(mockTrainingTypes);

        ResponseEntity<List<TrainingTypeIdDTO>> response = trainingTypeController.getTrainingTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTrainingTypes, response.getBody());
    }
}
