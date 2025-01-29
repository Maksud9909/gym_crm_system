package uz.ccrew.dto.trainee;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import uz.ccrew.dto.training.TrainingTrainerUpdateDTO;

import java.util.List;

@Getter
@NotNull
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTraineeTrainersDTO {
    private String traineeUsername;
    private List<TrainingTrainerUpdateDTO> trainingTrainers;
}
