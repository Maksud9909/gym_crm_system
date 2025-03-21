package uz.ccrew.dto.training;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
    @NotBlank(message = "Trainee username should be not empty")
    private String traineeUsername;
    @NotBlank(message = "Trainer username should be not empty")
    private String trainerUsername;
    @NotBlank(message = "Training name should be not empty")
    private String trainingName;
    private LocalDate trainingDate;
    private Double trainingDuration;
}
