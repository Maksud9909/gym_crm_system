package uz.ccrew.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uz.ccrew.dto.trainee.TraineeShortDTO;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileUsernameDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String trainingTypeName;
    private Boolean isActive;
    private List<TraineeShortDTO> traineeShortDTOS;
}
