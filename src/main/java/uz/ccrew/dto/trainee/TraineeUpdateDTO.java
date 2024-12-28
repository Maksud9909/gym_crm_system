package uz.ccrew.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record TraineeUpdateDTO(@NotBlank(message = "First name is required field")
                               String firstName,
                               @NotBlank(message = "Last name is required field")
                               String lastName,
                               @NotBlank(message = "Username is required field")
                               String username,
                               @NotBlank(message = "Password is required field")
                               String password,
                               @Past(message = "Date of birth must be in the past")
                               LocalDate birthOfDate,

                               String address) {}
