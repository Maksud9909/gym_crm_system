package uz.ccrew.controller;

import uz.ccrew.service.TrainingTypeService;
import uz.ccrew.dto.trainingType.TrainingTypeIdDTO;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training-types")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Training Type Controller", description = "Training Type API")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping
    @Operation(summary = "Find all training types")
    public ResponseEntity<List<TrainingTypeIdDTO>> getTrainingTypes() {
        List<TrainingTypeIdDTO> result = trainingTypeService.findAll();
        return ResponseEntity.ok(result);
    }
}
