package uz.ccrew.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import uz.ccrew.dto.trainer.*;
import uz.ccrew.service.TrainerService;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.dto.trainer.TrainerTrainingDTO;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Trainer Controller", description = "Trainer API")
public class TrainerController {
    private final TrainerService trainerService;

    @PostMapping("/")
    @Operation(summary = "Create a Trainer")
    public ResponseEntity<UserCredentials> create(@Valid @RequestBody TrainerCreateDTO dto) {
        UserCredentials result = trainerService.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile/{username}")
    @Operation(summary = "Get Trainer Profile")
    public ResponseEntity<TrainerProfileDTO> getProfile(@PathVariable("username") String username) {
        TrainerProfileDTO result = trainerService.getProfile(username);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/")
    @Operation(summary = "Update Trainer")
    public ResponseEntity<TrainerProfileUsernameDTO> update(@Valid @RequestBody TrainerUpdateDTO dto) {
        TrainerProfileUsernameDTO result = trainerService.update(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/trainees/{username}/trainers/unassigned")
    @Operation(summary = "Get not assigned on trainee active trainers")
    public ResponseEntity<List<TrainerDTO>> getUnassignedTrainers(@PathVariable("username") String username) {
        List<TrainerDTO> result = trainerService.getUnassignedTrainers(username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get Trainer Trainings List")
    public ResponseEntity<List<TrainerTrainingDTO>> getTrainerTrainings(@PathVariable("username") String username,
                                                                        @RequestParam(name = "periodFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime periodFrom,
                                                                        @RequestParam(name = "periodTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime periodTo,
                                                                        @RequestParam(name = "traineeName", required = false) String traineeName) {
        List<TrainerTrainingDTO> result = trainerService.getTrainerTrainings(username, periodFrom, periodTo, traineeName);
        return ResponseEntity.ok(result);
    }
}
