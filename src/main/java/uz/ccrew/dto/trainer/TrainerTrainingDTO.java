package uz.ccrew.dto.trainer;

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
public class TrainerTrainingDTO {
    private String trainingName;
    private LocalDateTime trainingDate;
    private String trainingType;
    private Double trainingDuration;
    private String traineeName;
}

