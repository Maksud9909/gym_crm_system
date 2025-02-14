package uz.ccrew.controller;

import uz.ccrew.service.AuthService;
import uz.ccrew.service.UserService;
import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.dto.auth.ChangePasswordDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Controller", description = "User API")
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login to account")
    public ResponseEntity<JwtResponse> login(@RequestBody UserCredentials userCredentials, HttpServletRequest request) {
        JwtResponse jwtResponse = authService.login(userCredentials, request);
        return ResponseEntity.ok(jwtResponse);
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate/deactivate")
    @Operation(summary = "Activate/Deactivate profile")
    public ResponseEntity<?> activateDeactivate(@RequestParam(name = "username") String username,
                                                @RequestParam(name = "isActive", defaultValue = "true") Boolean isActive) {
        userService.activateDeactivate(username, isActive);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout from account")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }
}
