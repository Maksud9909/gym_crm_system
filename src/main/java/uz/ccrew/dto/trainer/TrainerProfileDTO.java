package uz.ccrew.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uz.ccrew.dto.trainee.TraineeShortDTO;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileDTO {
    private String firstName;
    private String lastName;
    private String trainingTypeName;
    private Boolean isActive;
    private List<TraineeShortDTO> trainees;
}
