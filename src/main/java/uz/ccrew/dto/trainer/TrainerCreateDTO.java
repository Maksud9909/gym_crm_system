package uz.ccrew.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import uz.ccrew.entity.TrainingType;

import java.time.LocalDate;

@Builder
public record TrainerCreateDTO(@NotBlank(message = "First name is required field")
                               String firstName,
                               @NotBlank(message = "Last name is required field")
                               String lastName,
                               @Past(message = "Date of birth must be in the past")
                               LocalDate birthOfDate,
                               String address,
                               TrainingType trainingType) {}
