package uz.ccrew.controller;

import org.springframework.mock.web.MockHttpServletRequest;
import uz.ccrew.service.AuthService;
import uz.ccrew.service.UserService;
import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.dto.auth.ChangePasswordDTO;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthService authService;
    @InjectMocks
    private UserController userController;

    @Test
    void login() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        JwtResponse jwtResponse = new JwtResponse("123", "123");
        UserCredentials userCredentials = new UserCredentials("username", "password");
        when(authService.login(userCredentials,request)).thenReturn(jwtResponse);
        ResponseEntity<JwtResponse> response = userController.login(userCredentials, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), jwtResponse);
    }

    @Test
    void changePassword() {
        ChangePasswordDTO changePasswordDTO = ChangePasswordDTO.builder()
                .username("Username")
                .oldPassword("Old pass")
                .newPassword("New Pass")
                .build();

        ResponseEntity<?> response = userController.changePassword(changePasswordDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).changePassword(changePasswordDTO);
    }

    @Test
    void activateDeactivate() {
        String username = "username";
        Boolean isActive = Boolean.FALSE;
        ResponseEntity<?> response = userController.activateDeactivate(username, isActive);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).activateDeactivate(username, isActive);
    }

    @Test
    void logout(){
        ResponseEntity<?> response = userController.logout();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
