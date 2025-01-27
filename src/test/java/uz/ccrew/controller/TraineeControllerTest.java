package uz.ccrew.controller;

import uz.ccrew.dto.trainee.*;
import uz.ccrew.service.TraineeService;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.dto.user.UserCredentials;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {
    @Mock
    private TraineeService traineeService;
    @InjectMocks
    private TraineeController traineeController;

    @Test
    void create() {
        TraineeCreateDTO createDTO = TraineeCreateDTO.builder()
                .firstName("Test")
                .lastName("Test")
                .datOfBirth(LocalDate.now().minusYears(10L))
                .address("Test")
                .build();
        UserCredentials userCredentials = UserCredentials.builder()
                .username("test")
                .password("test")
                .build();
        when(traineeService.create(createDTO)).thenReturn(userCredentials);

        ResponseEntity<UserCredentials> response = traineeController.create(createDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userCredentials, response.getBody());
        verify(traineeService, times(1)).create(createDTO);
    }

    @Test
    void getProfile() {
        String username = "Test";
        TraineeProfileDTO traineeProfileDTO = TraineeProfileDTO.builder()
                .firstName("Test")
                .build();

        when(traineeService.getProfile(username)).thenReturn(traineeProfileDTO);
        ResponseEntity<TraineeProfileDTO> response = traineeController.getProfile(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(traineeProfileDTO, response.getBody());
        verify(traineeService, times(1)).getProfile(username);
    }

    @Test
    void update() {
        TraineeUpdateDTO traineeUpdateDTO = TraineeUpdateDTO.builder()
                .firstName("Test")
                .build();
        TraineeProfileUsernameDTO traineeProfileUsernameDTO = TraineeProfileUsernameDTO.builder()
                .firstName("Test")
                .build();
        when(traineeService.update(traineeUpdateDTO)).thenReturn(traineeProfileUsernameDTO);

        ResponseEntity<TraineeProfileUsernameDTO> response = traineeController.update(traineeUpdateDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(traineeProfileUsernameDTO, response.getBody());
        verify(traineeService, times(1)).update(traineeUpdateDTO);
    }

    @Test
    void deleteTraineeByUsername() {
        String username = "Test";

        ResponseEntity<?> response = traineeController.deleteTraineeByUsername(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(traineeService, times(1)).deleteTraineeByUsername(username);
    }

    @Test
    void updateTraineeTrainers() {
        List<UpdateTraineeTrainersDTO> updateTraineeTrainersDTOList = Collections.singletonList(
                UpdateTraineeTrainersDTO.builder()
                        .traineeUsername("Test1")
//                        .trainerUsername("Test2")
                        .build()
        );

        List<TrainerDTO> trainerDTOList = Collections.singletonList(
                TrainerDTO.builder()
                        .username("Test2")
                        .build()
        );

        when(traineeService.updateTraineeTrainers(updateTraineeTrainersDTOList)).thenReturn(trainerDTOList);

        ResponseEntity<List<TrainerDTO>> response = traineeController.updateTraineeTrainers(updateTraineeTrainersDTOList);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainerDTOList, response.getBody());
        verify(traineeService, times(1)).updateTraineeTrainers(updateTraineeTrainersDTOList);
    }

    @Test
    void getTrainerTrainings() {
        String username = "testUser";
        LocalDate periodFrom = LocalDate.of(2023, 1, 1);
        LocalDate periodTo = LocalDate.of(2023, 12, 31);
        String trainerName = "John Doe";
        String trainingTypeName = "Cardio";

        List<TraineeTrainingDTO> mockTrainings = Arrays.asList(
                new TraineeTrainingDTO("Training 1", LocalDate.of(2023, 5, 10), "Cardio", 1.5, "John Doe"),
                new TraineeTrainingDTO("Training 2", LocalDate.of(2023, 6, 15), "Cardio", 2.0, "John Doe")
        );

        when(traineeService.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingTypeName)).thenReturn(mockTrainings);

        ResponseEntity<List<TraineeTrainingDTO>> response = traineeController.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingTypeName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTrainings, response.getBody());
    }
}
