package uz.ccrew.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import uz.ccrew.dto.Response;
import uz.ccrew.dto.trainee.*;
import uz.ccrew.dto.ResponseMaker;
import uz.ccrew.dto.trainer.TrainerDTO;
import uz.ccrew.service.TraineeService;
import uz.ccrew.dto.user.UserCredentials;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Trainee Controller", description = "Trainee API")
public class TraineeController {
    private final TraineeService traineeService;

    @PostMapping("/create")
    @Operation(summary = "Create a Trainee")
    public ResponseEntity<Response<UserCredentials>> create(@Valid @RequestBody TraineeCreateDTO dto) {
        UserCredentials result = traineeService.create(dto);
        return ResponseMaker.ok(result);
    }

    @GetMapping("/profile/{username}")
    @Operation(summary = "Get Trainee Profile")
    public ResponseEntity<Response<TraineeProfileDTO>> getProfile(@PathVariable("username") String username) {
        TraineeProfileDTO result = traineeService.getProfile(username);
        return ResponseMaker.ok(result);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Trainee")
    public ResponseEntity<Response<TraineeProfileUsernameDTO>> update(@Valid @RequestBody TraineeUpdateDTO dto) {
        TraineeProfileUsernameDTO result = traineeService.update(dto);
        return ResponseMaker.ok(result);
    }

    @DeleteMapping("/delete/{username}")
    @Operation(summary = "Delete by username")
    public ResponseEntity<Response<?>> deleteTraineeByUsername(@PathVariable("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseMaker.ok();
    }

    @PatchMapping("/activate/deactivate")
    @Operation(summary = "Activate/Deactivate profile")
    public ResponseEntity<Response<?>> activateDeactivate(@RequestParam(name = "username") String username,
                                                          @RequestParam(name = "isActive", defaultValue = "true") Boolean isActive) {
        traineeService.activateDeactivate(username, isActive);
        return ResponseMaker.ok();
    }

    @PutMapping("/trainee/trainers")
    @Operation(summary = "Update Trainee's Trainer List")
    public ResponseEntity<Response<List<TrainerDTO>>> updateTraineeTrainers(@RequestParam("username") String username,
                                                                            @RequestBody List<String> trainers) {
        List<TrainerDTO> result = traineeService.updateTraineeTrainers(username, trainers);
        return ResponseMaker.ok(result);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get Trainee Trainings List")
    public ResponseEntity<Response<List<TraineeTrainingDTO>>> getTrainerTrainings(@PathVariable("username") String username,
                                                                                  @RequestParam(name = "periodFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                                                                                  @RequestParam(name = "periodTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                                                                                  @RequestParam(name = "trainerName", required = false) String trainerName,
                                                                                  @RequestParam(name = "trainingTypeName", required = false) String trainingTypeName) {
        List<TraineeTrainingDTO> result = traineeService.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingTypeName);
        return ResponseMaker.ok(result);
    }
}
