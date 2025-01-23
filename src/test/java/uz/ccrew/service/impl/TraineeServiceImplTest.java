package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.User;
import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.utils.UserUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UserUtils userUtils;

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
    void activateDeactivate_ShouldActivateTrainee_WhenExists() {
        when(userDAO.findByUsername("test_user")).thenReturn(Optional.of(user));
        user.setIsActive(Boolean.FALSE);

        traineeService.activateDeactivate("test_user", Boolean.TRUE);

        verify(userDAO, times(1)).activateDeactivate("test_user", true);
    }
}
