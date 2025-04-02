package uz.ccrew.dto.training;

import lombok.*;
import uz.ccrew.enums.ActionType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadDTO implements Serializable {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;
    private LocalDateTime trainingDate;
    private Double trainingDuration;
    private ActionType actionType;
}
