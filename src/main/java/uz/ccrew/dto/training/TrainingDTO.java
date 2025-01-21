package uz.ccrew.dto.training;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Builder
@Getter
@NotNull
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
    @NotBlank(message = "Trainee username should be not empty")
    private String traineeUsername;
    @NotBlank(message = "Trainer username should be not empty")
    private String trainerUsername;
    @NotBlank(message = "Training name should be not empty")
    private String trainingName;
    @NotBlank(message = "Training date should be not empty")
    private LocalDate trainingDate;
    @NotBlank(message = "Training duration should be not empty")
    private Double trainingDuration;
}
