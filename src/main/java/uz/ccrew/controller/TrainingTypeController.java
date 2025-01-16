package uz.ccrew.controller;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training-types")
@RequiredArgsConstructor
@Tag(name = "Training Type Controller", description = "Training Type API")
public class TrainingTypeController {
    @GetMapping
    @Operation(summary = "Получить список типов тренировок")
    public List<String> getTrainingTypes() {
        return List.of("Yoga", "Crossfit", "Cardio");
    }
}
