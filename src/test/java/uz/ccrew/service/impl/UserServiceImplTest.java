package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.auth.ChangePasswordDTO;
import uz.ccrew.entity.User;
import uz.ccrew.exp.exp.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("test_user")
                .password("old_password")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .build();
    }

    @Test
    void changePassword_ShouldChangePassword_WhenValidCredentialsProvided() {
        ChangePasswordDTO dto = ChangePasswordDTO.builder()
                .username("test_user")
                .oldPassword("old_password")
                .newPassword("new_password")
                .build();

        when(userDAO.findByUsername("test_user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old_password", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("new_password")).thenReturn("hashed_new_password");

        userService.changePassword(dto);

        verify(userDAO, times(1)).changePassword(user.getId(), "hashed_new_password");
    }

    @Test
    void changePassword_ShouldThrowException_WhenUserNotFound() {
        ChangePasswordDTO dto = ChangePasswordDTO.builder()
                .username("nonexistent_user")
                .oldPassword("wrong_password")
                .newPassword("new_password")
                .build();

        when(userDAO.findByUsernameAndPassword("nonexistent_user", "wrong_password")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.changePassword(dto));

        verify(userDAO, never()).changePassword(anyLong(), anyString());
    }

    @Test
    void changePassword_ShouldThrowException_WhenOldPasswordDoesNotMatch() {
        ChangePasswordDTO dto = ChangePasswordDTO.builder()
                .username("test_user")
                .oldPassword("wrong_password")
                .newPassword("new_password")
                .build();

        when(userDAO.findByUsernameAndPassword("test_user", "wrong_password")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.changePassword(dto));

        verify(userDAO, never()).changePassword(anyLong(), anyString());
    }

    @Test
    void activateDeactivate_ShouldActivateUser_WhenUserExists() {
        user.setIsActive(false);
        when(userDAO.findByUsername("test_user")).thenReturn(Optional.of(user));

        userService.activateDeactivate("test_user", true);

        verify(userDAO, times(1)).activateDeactivate("test_user", true);
    }


    @Test
    void activateDeactivate_ShouldDeactivateUser_WhenUserExists() {
        when(userDAO.findByUsername("test_user")).thenReturn(Optional.of(user));

        userService.activateDeactivate("test_user", false);

        verify(userDAO, times(1)).activateDeactivate("test_user", false);
    }

    @Test
    void activateDeactivate_ShouldThrowException_WhenUserDoesNotExist() {
        when(userDAO.findByUsername("nonexistent_user")).thenReturn(Optional.empty());

        userService.activateDeactivate("nonexistent_user", true);

        verify(userDAO, times(1)).activateDeactivate("nonexistent_user", true);
    }
}
