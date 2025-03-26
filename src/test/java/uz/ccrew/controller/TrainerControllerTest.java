package uz.ccrew.controller;

import uz.ccrew.dto.trainer.*;
import uz.ccrew.service.TrainerService;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.dto.trainee.TraineeShortDTO;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @Test
    void testCreateTrainer() {
        TrainerCreateDTO dto = TrainerCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .trainingTypeName("Cardio")
                .build();

        UserCredentials mockCredentials = UserCredentials.builder()
                .username("johndoe")
                .password("password123")
                .build();

        when(trainerService.create(dto)).thenReturn(mockCredentials);

        ResponseEntity<UserCredentials> response = trainerController.create(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCredentials, response.getBody());
    }

    @Test
    void testGetProfile() {
        String username = "johndoe";

        TrainerProfileDTO mockProfile = TrainerProfileDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .trainingTypeName("Cardio")
                .isActive(true)
                .trainees(Arrays.asList(
                        new TraineeShortDTO("trainee1", "Jane", "Smith"),
                        new TraineeShortDTO("trainee2", "Mark", "Brown")
                ))
                .build();

        when(trainerService.getProfile(username)).thenReturn(mockProfile);

        ResponseEntity<TrainerProfileDTO> response = trainerController.getProfile(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockProfile, response.getBody());
    }

    @Test
    void testUpdateTrainer() {
        TrainerUpdateDTO dto = TrainerUpdateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .trainingTypeName("Pilates")
                .build();

        TrainerProfileUsernameDTO mockUpdatedProfile = TrainerProfileUsernameDTO.builder()
                .username("johndoe")
                .firstName("John")
                .lastName("Doe")
                .trainingTypeName("Pilates")
                .build();

        when(trainerService.update(dto)).thenReturn(mockUpdatedProfile);

        ResponseEntity<TrainerProfileUsernameDTO> response = trainerController.update(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUpdatedProfile, response.getBody());
    }

    @Test
    void testGetUnassignedTrainers() {
        String username = "trainee1";

        List<TrainerDTO> mockTrainers = Arrays.asList(
                new TrainerDTO("trainer1", "John", "Doe", "Cardio"),
                new TrainerDTO("trainer2", "Jane", "Smith", "Pilates")
        );

        when(trainerService.getUnassignedTrainers(username)).thenReturn(mockTrainers);

        ResponseEntity<List<TrainerDTO>> response = trainerController.getUnassignedTrainers(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTrainers, response.getBody());
    }

    @Test
    void testGetTrainerTrainings() {
        String username = "johndoe";
        LocalDateTime periodFrom = LocalDateTime.of(2023, 1, 1, 1, 0, 0);
        LocalDateTime periodTo = LocalDateTime.of(2023, 12, 31, 23, 59, 59);
        String traineeName = "Jane Smith";

        List<TrainerTrainingDTO> mockTrainings = Arrays.asList(
                new TrainerTrainingDTO("Training 1", LocalDateTime.of(2023, 5, 10,12,22), "Cardio", 1.5, "Jane Smith"),
                new TrainerTrainingDTO("Training 2", LocalDateTime.of(2023, 6, 15, 13, 22), "Pilates", 2.0, "Mark Brown")
        );

        when(trainerService.getTrainerTrainings(username, periodFrom, periodTo, traineeName)).thenReturn(mockTrainings);

        ResponseEntity<List<TrainerTrainingDTO>> response = trainerController.getTrainerTrainings(username, periodFrom, periodTo, traineeName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTrainings, response.getBody());
    }
}
