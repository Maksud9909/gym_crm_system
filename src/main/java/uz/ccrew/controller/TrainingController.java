package uz.ccrew.controller;

import uz.ccrew.service.TrainingService;
import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.dto.training.TrainerMonthlySummaryDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Training Controller", description = "Training API")
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping("/add")
    @Operation(summary = "Add Training")
    public ResponseEntity<?> addTraining(@Valid @RequestBody TrainingDTO dto) {
        trainingService.addTraining(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Training")
    public ResponseEntity<?> deleteTraining(@PathVariable("id") Long id) {
        trainingService.deleteTraining(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/monthly/workload/{username}")
    public ResponseEntity<TrainerMonthlySummaryDTO> getMonthlyWorkload(@PathVariable("username") String username, @RequestParam("year") int year, @RequestParam("month") int month) {
        TrainerMonthlySummaryDTO result = trainingService.getMonthlyWorkload(username, year, month);
        return ResponseEntity.ok(result);
    }
}
