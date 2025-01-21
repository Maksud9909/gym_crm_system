package uz.ccrew.dto.trainee;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NotNull
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeCreateDTO {
    @NotBlank(message = "FirstName should be not empty")
    private String firstName;
    @NotBlank(message = "LastName should be not empty")
    private String lastName;
    private LocalDate datOfBirth;
    private String address;
}
