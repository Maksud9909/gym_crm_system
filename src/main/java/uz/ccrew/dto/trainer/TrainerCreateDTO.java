package uz.ccrew.dto.trainer;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;

@Builder
public record TrainerCreateDTO(@NotBlank(message = "First name is required field")
                               String firstName,
                               @NotBlank(message = "Last name is required field")
                               String lastName,
                               String address,
                               Long trainingTypeId) {}
