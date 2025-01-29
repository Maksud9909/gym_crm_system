package uz.ccrew.dto.trainer;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NotNull
public class TrainerCreateDTO {
    @NotBlank(message = "FirstName should be not empty")
    private String firstName;
    @NotBlank(message = "LastName should be not empty")
    private String lastName;
    @NotBlank(message = "TrainingTypeName should be not empty")
    private String trainingTypeName;
}
