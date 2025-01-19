package uz.ccrew.controller;

import uz.ccrew.dto.Response;
import uz.ccrew.dto.ResponseMaker;
import uz.ccrew.service.UserService;
import uz.ccrew.dto.auth.ChangePasswordDTO;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User API")
public class UserController {
    private final UserService userService;

    @PostMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<Response<?>> changePassword(@RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return ResponseMaker.ok();
    }
}
