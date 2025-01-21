package uz.ccrew.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@NotNull
public class TrainerUpdateDTO {
    @NotBlank(message = "Username should be not empty")
    private String username;
    @NotBlank(message = "FirstName should be not empty")
    private String firstName;
    @NotBlank(message = "LastName should be not empty")
    private String lastName;
    private String trainingTypeName;
    @NotNull(message = "IsActive should be not null")
    private Boolean isActive;
}
