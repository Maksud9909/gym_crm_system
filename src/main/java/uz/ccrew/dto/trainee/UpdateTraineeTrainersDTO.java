package uz.ccrew.dto.trainee;

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
public class UpdateTraineeTrainersDTO {
    private String traineeUsername;
    private String trainerUsername;
    private Long trainingId;
}
