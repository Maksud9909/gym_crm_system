package uz.ccrew.dto.trainer;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

@Builder
public record TrainerCreateDTO(@NotBlank(message = "First name is required field")
                               String firstName,
                               @NotBlank(message = "Last name is required field")
                               String lastName,
                               @Past(message = "Date of birth must be in the past")
                               LocalDate birthOfDate,
                               String address,
                               Long trainingTypeId) {}
