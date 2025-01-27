package uz.ccrew.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.entity.User;
import uz.ccrew.exp.UnauthorizedException;
import uz.ccrew.utils.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_ShouldReturnJwtResponse_WhenCredentialsAreValid() {
        String username = "validUser";
        String password = "validPassword";
        UserCredentials credentials = new UserCredentials(username, password);

        User mockUser = new User();
        when(userDAO.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(mockUser));

        when(jwtUtil.generateAccessToken(username)).thenReturn("validAccessToken");
        when(jwtUtil.generateRefreshToken(username)).thenReturn("validRefreshToken");

        JwtResponse response = authService.login(credentials);

        assertNotNull(response);
        assertEquals("validAccessToken", response.getAccessToken());
        assertEquals("validRefreshToken", response.getRefreshToken());

        verify(userDAO, times(1)).findByUsernameAndPassword(username, password);
        verify(jwtUtil, times(1)).generateAccessToken(username);
        verify(jwtUtil, times(1)).generateRefreshToken(username);
    }


    @Test
    void login_ShouldThrowUnauthorizedException_WhenCredentialsAreInvalid() {
        String username = "invalidUser";
        String password = "invalidPassword";
        UserCredentials credentials = new UserCredentials(username, password);

        when(userDAO.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.login(credentials));

        assertEquals("Invalid username or password", exception.getMessage());
        verify(userDAO, times(1)).findByUsernameAndPassword(username, password);
        verify(jwtUtil, never()).generateAccessToken(anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }
}
