package uz.ccrew.dto.training;

import uz.ccrew.enums.ActionType;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadDTO {
    @NotBlank(message = "Trainer username should not be empty")
    private String trainerUsername;
    @NotBlank(message = "Trainer first name should not be empty")
    private String trainerFirstName;
    @NotBlank(message = "Last name should not be empty")
    private String trainerLastName;
    private boolean isActive;
    @NotNull(message = "Training date should not be null")
    private LocalDateTime trainingDate;
    @NotNull(message = "Training duration should not be null")
    private Double trainingDuration;
    @NotNull(message = "Action type should not be null")
    private ActionType actionType;
}
