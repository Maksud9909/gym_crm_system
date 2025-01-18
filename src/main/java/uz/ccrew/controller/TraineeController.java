package uz.ccrew.controller;

import org.springframework.web.bind.annotation.*;
import uz.ccrew.dto.Response;
import uz.ccrew.dto.ResponseMaker;
import uz.ccrew.dto.trainee.TraineeProfile;
import uz.ccrew.dto.trainee.TraineeProfileUsername;
import uz.ccrew.dto.trainee.TraineeUpdateDTO;
import uz.ccrew.service.TraineeService;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.dto.trainee.TraineeCreateDTO;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

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

    @GetMapping("/{username}/profile")
    @Operation(summary = "Get Trainee Profile")
    public ResponseEntity<Response<TraineeProfile>> getProfile(@PathVariable("username") String username) {
        TraineeProfile result = traineeService.getProfile(username);
        return ResponseMaker.ok(result);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Trainee")
    public ResponseEntity<Response<TraineeProfileUsername>> update(@RequestBody TraineeUpdateDTO dto) {
        TraineeProfileUsername result = traineeService.update(dto);
        return ResponseMaker.ok(result);
    }
}
