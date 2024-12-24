package uz.ccrew.dto.trainer;

import uz.ccrew.entity.Specialization;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TrainerCreateDTO(@NotBlank(message = "First name is required field")
                               String firstName,
                               @NotBlank(message = "Last name is required field")
                               String lastName,
                               @Past(message = "Date of birth must be in the past")
                               LocalDate birthOfDate,
                               String address,
                               Specialization specialization) {}
