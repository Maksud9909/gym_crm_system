package uz.ccrew.controller;

import jakarta.validation.Valid;
import uz.ccrew.dto.Response;
import uz.ccrew.dto.ResponseMaker;
import uz.ccrew.service.TrainingService;
import uz.ccrew.dto.training.TrainingDTO;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
@Tag(name = "Training Controller", description = "Training API")
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping("/add")
    @Operation(summary = "Add Training")
    public ResponseEntity<Response<?>> addTraining(@Valid @RequestBody TrainingDTO dto) {
        trainingService.addTraining(dto);
        return ResponseMaker.ok();
    }
}
