package uz.ccrew.dto.trainer;

import lombok.*;
import uz.ccrew.entity.TrainingType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerCreateDTO {
    private String firstName;
    private String lastName;
    private String trainingTypeName;

    public String getTrainingTypeName(TrainingType trainingType) {
        return trainingTypeName;
    }
}
