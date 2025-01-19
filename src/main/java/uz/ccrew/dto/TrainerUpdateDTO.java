package uz.ccrew.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TrainerUpdateDTO {
    @NotBlank(message = "Username should be not empty")
    private String username;
    @NotBlank(message = "FirstName should be not empty")
    private String firstName;
    @NotBlank(message = "LastName should be not empty")
    private String lastName;
    private String trainingTypeName;
    @NotBlank(message = "IsActive should be not empty")
    private Boolean isActive;
}
