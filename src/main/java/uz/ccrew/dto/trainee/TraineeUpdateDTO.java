package uz.ccrew.dto.trainee;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Builder
@Getter
@NotNull
@NoArgsConstructor
@AllArgsConstructor
public class TraineeUpdateDTO {
    @NotBlank(message = "Username should be not empty")
    private String username;
    @NotBlank(message = "First Name should be not empty")
    private String firstName;
    @NotBlank(message = "Last Name should be not empty")
    private String lastName;
    private LocalDate datOfBirth;
    private String address;
    @NotNull(message = "IsActive should be not empty")
    private Boolean isActive;
}
