package uz.ccrew.controller;

import uz.ccrew.service.TrainingService;
import uz.ccrew.dto.training.TrainingDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Void> addTraining(@Valid @RequestBody TrainingDTO dto) {
        trainingService.addTraining(dto);
        return ResponseEntity.ok().build();
    }
}
