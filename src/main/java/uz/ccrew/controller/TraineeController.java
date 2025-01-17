package uz.ccrew.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.ccrew.dto.Response;
import uz.ccrew.dto.ResponseMaker;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.service.TraineeService;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
@Tag(name = "Trainee Controller", description = "Trainee API")
public class TraineeController {
    private final TraineeService traineeService;

    @PostMapping("/create")
    @Operation(summary = "Create a Trainee")
    public ResponseEntity<Response<UserCredentials>> create(@RequestBody TraineeCreateDTO dto) {
        UserCredentials result = traineeService.create(dto);
        return ResponseMaker.ok(result);
    }
}
