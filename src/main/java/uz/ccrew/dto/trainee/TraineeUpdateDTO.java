package uz.ccrew.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeUpdateDTO {
    @NotBlank(message = "Username should be not empty")
    private String username;
    @NotBlank(message = "First Name should be not empty")
    private String firstName;
    @NotBlank(message = "First Name should be not empty")
    private String lastName;
    private LocalDate datOfBirth;
    private String address;
    @NotBlank(message = "isActive should be not empty")
    private Boolean isActive;
}
