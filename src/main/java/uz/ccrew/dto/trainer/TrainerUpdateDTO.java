package uz.ccrew.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TrainerUpdateDTO(@NotBlank(message = "First name is required field")
                               String firstName,
                               @NotBlank(message = "Last name is required field")
                               String lastName,
                               @NotBlank(message = "Username is required field")
                               String username,
                               @NotBlank(message = "Password is required field")
                               String password,
                               Long trainingTypeId) {}
