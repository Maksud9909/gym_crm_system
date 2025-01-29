package uz.ccrew.dto.training;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NotNull
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTrainerUpdateDTO {
    private Long trainingId;
    private String trainerUsername;
    private String trainingName;
}
