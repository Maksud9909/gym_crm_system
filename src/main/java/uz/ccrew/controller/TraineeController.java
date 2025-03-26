package uz.ccrew.controller;

import uz.ccrew.dto.trainee.*;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Trainee Controller", description = "Trainee API")
public class TraineeController {
    private final TraineeService traineeService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/")
    @Operation(summary = "Create a Trainee")
    public ResponseEntity<UserCredentials> create(@Valid @RequestBody TraineeCreateDTO dto) {
        UserCredentials result = traineeService.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile/{username}")
    @Operation(summary = "Get Trainee Profile")
    public ResponseEntity<TraineeProfileDTO> getProfile(@PathVariable("username") String username) {
        TraineeProfileDTO result = traineeService.getProfile(username);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/")
    @Operation(summary = "Update Trainee")
    public ResponseEntity<TraineeProfileUsernameDTO> update(@Valid @RequestBody TraineeUpdateDTO dto) {
        TraineeProfileUsernameDTO result = traineeService.update(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete by username")
    public ResponseEntity<?> deleteTraineeByUsername(@PathVariable("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/trainee/trainers")
    @Operation(summary = "Update Trainee's Trainer List")
    public ResponseEntity<List<TrainerDTO>> updateTraineeTrainers(@RequestBody UpdateTraineeTrainersDTO trainersDTOList) {
        List<TrainerDTO> result = traineeService.updateTraineeTrainers(trainersDTOList);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get Trainee Trainings List")
    public ResponseEntity<List<TraineeTrainingDTO>> getTraineeTrainings(@PathVariable("username") String username,
                                                                        @RequestParam(name = "periodFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime periodFrom,
                                                                        @RequestParam(name = "periodTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime periodTo,
                                                                        @RequestParam(name = "trainerName", required = false) String trainerName,
                                                                        @RequestParam(name = "trainingTypeName", required = false) String trainingTypeName) {
        List<TraineeTrainingDTO> result = traineeService.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingTypeName);
        return ResponseEntity.ok(result);
    }
}
