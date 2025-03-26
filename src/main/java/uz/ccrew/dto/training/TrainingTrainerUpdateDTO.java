package uz.ccrew.dto.training;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTrainerUpdateDTO {
    private Long trainingId;
    private String trainerUsername;
    private String trainingName;
}
