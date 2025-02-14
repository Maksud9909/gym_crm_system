package uz.ccrew.service.impl;

import uz.ccrew.entity.User;
import uz.ccrew.security.jwt.JwtUtil;
import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.security.user.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.Authentication;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private String ip;
    private UserDetailsImpl userDetails;
    private Authentication authentication;
    private UserCredentials userCredentials;

    @BeforeEach
    void setUp() {
        ip = "127.0.0.1";
        userCredentials = new UserCredentials("test_user", "password123");
        userDetails = new UserDetailsImpl(User.builder().id(1L).username("test_user").password("password123").build());
        authentication = new UsernamePasswordAuthenticationToken(userDetails, userCredentials.getPassword());
    }

    @Test
    void login_ShouldReturnJwtResponse_WhenCredentialsAreValid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateAccessToken("test_user")).thenReturn("access_token");
        when(jwtUtil.generateRefreshToken("test_user")).thenReturn("refresh_token");
        when(loginAttemptService.isBlocked(ip)).thenReturn(false);

        JwtResponse response = authService.login(userCredentials, new MockHttpServletRequest());

        assertNotNull(response);
        assertEquals("access_token", response.getAccessToken());
        assertEquals("refresh_token", response.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateAccessToken("test_user");
        verify(jwtUtil, times(1)).generateRefreshToken("test_user");
    }


    @Test
    void login_ShouldThrowUnauthorizedException_WhenCredentialsAreInvalid() {
        String username = "invalidUser";
        String password = "invalidPassword";
        UserCredentials credentials = new UserCredentials(username, password);

        when(loginAttemptService.isBlocked(ip)).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(credentials, new MockHttpServletRequest())
        );

        assertEquals("Invalid username or password", exception.getMessage());

        verify(jwtUtil, never()).generateAccessToken(anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }
}
