package uz.ccrew.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ccrew.dto.Response;
import uz.ccrew.dto.ResponseMaker;
import uz.ccrew.dto.TrainerUpdateDTO;
import uz.ccrew.dto.trainer.*;
import uz.ccrew.dto.trainer.TrainerTrainingDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.service.TrainerService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
@Tag(name = "Trainer Controller", description = "Trainer API")
public class TrainerController {
    private final TrainerService trainerService;

    @PostMapping("/create")
    @Operation(summary = "Create a Trainer")
    public ResponseEntity<Response<UserCredentials>> create(@RequestBody TrainerCreateDTO dto) {
        UserCredentials result = trainerService.create(dto);
        return ResponseMaker.ok(result);
    }

    @GetMapping("/profile/{username}")
    @Operation(summary = "Get Trainer Profile")
    public ResponseEntity<Response<TrainerProfileDTO>> getProfile(@PathVariable("username") String username) {
        TrainerProfileDTO result = trainerService.getProfile(username);
        return ResponseMaker.ok(result);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Trainer")
    public ResponseEntity<Response<TrainerProfileUsernameDTO>> update(@RequestBody TrainerUpdateDTO dto) {
        TrainerProfileUsernameDTO result = trainerService.update(dto);
        return ResponseMaker.ok(result);
    }

    @GetMapping("/trainees/{username}/trainers/unassigned")
    @Operation(summary = "Get not assigned on trainee active trainers")
    public ResponseEntity<Response<List<TrainerDTO>>> getUnassignedActiveTrainers(@PathVariable("username") String username) {
        List<TrainerDTO> result = trainerService.getUnassignedTrainers(username);
        return ResponseMaker.ok(result);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get Trainer Trainings List")
    public ResponseEntity<Response<List<TrainerTrainingDTO>>> getTrainerTrainings(@PathVariable("username") String username,
                                                                                  @RequestParam(name = "periodFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                                                                                  @RequestParam(name = "periodTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                                                                                  @RequestParam(name = "traineeName", required = false) String traineeName) {
        List<TrainerTrainingDTO> result = trainerService.getTrainerTrainings(username, periodFrom, periodTo, traineeName);
        return ResponseMaker.ok(result);
    }
}
