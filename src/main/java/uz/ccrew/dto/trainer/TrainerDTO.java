package uz.ccrew.dto.trainer;

import lombok.*;
import uz.ccrew.dto.trainingType.TrainingTypeDTO;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeDTO trainingTypeDTO;
}
