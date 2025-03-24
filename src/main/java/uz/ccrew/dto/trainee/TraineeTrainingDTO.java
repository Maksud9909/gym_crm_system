package uz.ccrew.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingDTO {
    private String trainingName;
    private LocalDateTime trainingDate;
    private String trainingType;
    private Double trainingDuration;
    private String trainerName;
}
