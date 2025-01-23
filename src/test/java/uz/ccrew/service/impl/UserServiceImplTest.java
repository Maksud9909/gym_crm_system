package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.auth.ChangePasswordDTO;
import uz.ccrew.entity.User;
import uz.ccrew.exp.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

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

        when(userDAO.findByUsernameAndPassword("test_user", "old_password")).thenReturn(Optional.of(user));

        userService.changePassword(dto);

        verify(userDAO, times(1)).changePassword(user.getId(), "new_password");
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
}
