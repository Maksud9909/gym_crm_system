package uz.ccrew.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerCreateDTO {
    @NotBlank(message = "FirstName should be not empty")
    private String firstName;
    @NotBlank(message = "LastName should be not empty")
    private String lastName;
    @NotBlank(message = "TrainingTypeName should be not empty")
    private String trainingTypeName;
}
